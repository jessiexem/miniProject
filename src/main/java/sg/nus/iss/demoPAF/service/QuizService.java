package sg.nus.iss.demoPAF.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sg.nus.iss.demoPAF.model.Quiz;
import sg.nus.iss.demoPAF.repository.QuizRepository;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class QuizService {

    @Autowired
    private QuizRepository quizRepo;

    @Value("${QUIZ_API_KEY}")
    private String apiKey;

    private static final Logger logger = Logger.getLogger(QuizService.class.getName());

    private static final String WORD_QUIZ_URL
            = "https://twinword-word-association-quiz.p.rapidapi.com/type1/";

    private static final String API_HOST = "twinword-word-association-quiz.p.rapidapi.com";

    private static final String PAST_5_SCORE_CHART_URL = "https://quickchart.io/chart/render/zm-c2eab983-b47e-422c-bffe-79b845ff93fd";

    private static final String AVG_SCORE_BY_DIFFICULTY_CHART_URL = "https://quickchart.io/chart/render/zm-6c54888a-b3f2-4a80-9919-6b66b7dbe9c8";

    public Optional<List<Quiz>> getQuiz(String area, Integer level) {

        String url = UriComponentsBuilder
                .fromUriString(WORD_QUIZ_URL)
                .queryParam("level",level)
                .queryParam("area",area)
                .toUriString();

        RequestEntity req = RequestEntity.get(url)
                .header("X-RapidAPI-Host",API_HOST)
                .header("X-RapidAPI-Key",apiKey)
                .build();

        RestTemplate template = new RestTemplate();

        ResponseEntity<String> resp = template.exchange(req,String.class);

        logger.info(">>>>QuizService getQuiz: "+resp.getBody());

        try {
            List<Quiz> quizList = Quiz.create(resp.getBody());
            return Optional.of(quizList);
        } catch (Exception e) {
            logger.severe(">>>> QuizService - getQuiz: Error creating List<Quiz>");
            e.printStackTrace();
        }
        return Optional.empty();
    }

    public void calculateQuizScore(@RequestBody MultiValueMap<String,String> payload, HttpSession sess) {

        String correct = payload.getFirst("correctAnswers");
        String [] correctArr = correct.substring(1,29).split(", ");

        List<Integer> correctAns = new ArrayList<>();
        for (int i=0; i< correctArr.length; i++) {
            correctAns.add(Integer.parseInt(correctArr[i]));
        }

        //put the user's answer
        //calculate the score
        List<Integer> userInput = new ArrayList<>();
        int score = 0;

        try {
            for(int i=0; i<10 ; i++) {

                if(payload.getFirst(("answer%s").formatted(i+1))!=null) {
                    userInput.add(Integer.valueOf(payload.getFirst(("answer%s").formatted(i+1))));
                }

                if (userInput.get(i)==correctAns.get(i)) {
                    score++;
                }
            }

        } catch (Exception e) {
            logger.warning("There is an error in parsing the user inputs.");
        }

        sess.setAttribute("userScore",score);
        sess.setAttribute("userInput",userInput);
        sess.setAttribute("correctAnswers",correctAns);
    }

    public boolean insertQuizActivityStart(int userId,String area,int difficultyLevel){
        return quizRepo.insertStartQuizActivity(userId,area,difficultyLevel);
    }

    public boolean insertQuizActivityEnd(int userId,int score) {
        return quizRepo.endQuizActivity(userId,score);
    }

    public String createScoreChartForPast5Attempts(int userId) {

        String data = quizRepo.getLast5ScoreByUser(userId);

        logger.info("getLast5ScoreByUser - Data: "+data);

        if (data == null) {
            return null;
        }

        String url = UriComponentsBuilder
                .fromUriString(PAST_5_SCORE_CHART_URL)
                .queryParam("data1",data)
                .toUriString();

        return url;
    }

    public String createAvgScoreChartByDifficultyLevel(int userId) {

        //get user score
        SqlRowSet userResult = quizRepo.getAvgScoreByDifficultyLevelByUser(userId);
        String userData = transformResultSetToString(userResult);

        logger.info(" getAvgScoreByDifficultyLevel - UserData: "+userData);

        if (userData == null) {
            return null;
        }

        //get average score of all users
        SqlRowSet avgResult = quizRepo.getAvgScoreByDifficultyLevel();
        String avgData = transformResultSetToString(avgResult);

        logger.info(" getAvgScoreByDifficultyLevel - avgData: "+userData);

        String url = UriComponentsBuilder
                .fromUriString(AVG_SCORE_BY_DIFFICULTY_CHART_URL)
                .queryParam("data1",userData)
                .queryParam("data2",avgData)
                .toUriString();

        return url;

    }

    public String transformResultSetToString(SqlRowSet result) {
        if(!result.isBeforeFirst()) {
            return null;
        }

        HashMap<Integer,Double> scoreMap = new HashMap<>();
        while (result.next()) {
            scoreMap.put(result.getInt("difficulty_level"),result.getDouble("avg_score"));
        }

        StringBuilder sb = new StringBuilder();
        for(int i=1; i<11; i++) {
            if (scoreMap.get(i) !=null) {
                sb.append(scoreMap.get(i));
            }
            else {
                sb.append(0);
            }
            sb.append(",");
        }

        return sb.toString();
    }

    public boolean deleteQuizActivity(int userId) {
        return quizRepo.deleteQuizActivity(userId);
    }

}
