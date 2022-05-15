package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.demoPAF.model.Quiz;

import java.util.List;

import java.util.Optional;

@SpringBootTest
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Test
    void shouldReturnListOfQuiz() {
        String area = "sat";
        Integer level = 1;

        Optional<List<Quiz>> opt = quizService.getQuiz(area,level);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    void shouldNotReturnListOfQuiz() {
        String area = "sat";
        Integer level = 11;

        Optional<List<Quiz>> opt = quizService.getQuiz(area,level);
        Assertions.assertFalse(opt.isPresent());
    }

}
