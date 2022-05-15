package sg.nus.iss.demoPAF.model;

import jakarta.json.Json;
import jakarta.json.JsonArray;
import jakarta.json.JsonObject;
import jakarta.json.JsonReader;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

public class Quiz {

    private String area;
    private int difficultyLevel;
    private int answer;
    private List<String> questionArray;
    private List<String> optionsArray;

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public int getDifficultyLevel() {
        return difficultyLevel;
    }

    public void setDifficultyLevel(int difficultyLevel) {
        this.difficultyLevel = difficultyLevel;
    }

    public int getAnswer() {
        return answer;
    }

    public void setAnswer(int answer) {
        this.answer = answer;
    }

    public List<String> getQuestionArray() {
        return questionArray;
    }

    public void setQuestionArray(List<String> questionArray) {
        this.questionArray = questionArray;
    }

    public List<String> getOptionsArray() {
        return optionsArray;
    }

    public void setOptionsArray(List<String> optionsArray) {
        this.optionsArray = optionsArray;
    }

    public static List<Quiz> create (String json) {
        InputStream is = new ByteArrayInputStream(json.getBytes());
        JsonReader reader = Json.createReader(is);
        JsonObject quizObj = reader.readObject();

        List<Quiz> quizList = new ArrayList<>();
        JsonArray quizListArr = quizObj.getJsonArray("quizlist");
        quizListArr.stream().map(v-> (JsonObject) v).forEach(v-> {
            Quiz quiz = new Quiz();
            quiz.setArea(quizObj.getString("area"));
            quiz.setDifficultyLevel(quizObj.getInt("level"));
            quiz.setAnswer(v.getInt("correct"));
            JsonArray questionArr = v.getJsonArray("quiz");
            List<String> questionWords = new ArrayList<>();
            for (int i =0; i<3 ; i++) {
                questionWords.add(questionArr.getString(i));
            }
            quiz.setQuestionArray(questionWords);

            JsonArray optionsArr = v.getJsonArray("option");
            List<String> optionWords = new ArrayList<>();
            for (int i =0; i<2 ; i++) {
                optionWords.add(optionsArr.getString(i));
            }
            quiz.setOptionsArray(optionWords);
            quizList.add(quiz);
        });

        return quizList;
    }
}
