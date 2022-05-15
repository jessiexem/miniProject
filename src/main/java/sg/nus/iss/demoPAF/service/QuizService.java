package sg.nus.iss.demoPAF.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.MultiValueMap;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;
import sg.nus.iss.demoPAF.model.Quiz;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

@Service
public class QuizService {

    @Value("${QUIZ_API_KEY}")
    private String apiKey;

    private static final Logger logger = Logger.getLogger(QuizService.class.getName());

    private static final String WORD_QUIZ_URL
            = "https://twinword-word-association-quiz.p.rapidapi.com/type1/";

    private static final String API_HOST = "twinword-word-association-quiz.p.rapidapi.com";


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
    }


}
