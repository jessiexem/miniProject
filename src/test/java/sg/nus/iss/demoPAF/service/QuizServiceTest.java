package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.demoPAF.model.Quiz;
import sg.nus.iss.demoPAF.model.User;

import java.util.List;

import java.util.Optional;

@SpringBootTest
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class QuizServiceTest {

    @Autowired
    private QuizService quizService;

    @Autowired
    private UserService userService;

    private User user;

    @BeforeAll
    void createUserTester() {
        userService.createUser("tester95","abc123","tester",
                "tan","tester@gmail.com","F");

        Optional<User> opt = userService.findUserByUsername("tester95");
        user = opt.get();
    }


    @Test
    @Order(1)
    void shouldReturnListOfQuiz() {
        String area = "sat";
        Integer level = 1;

        Optional<List<Quiz>> opt = quizService.getQuiz(area,level);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    @Order(2)
    void shouldNotReturnListOfQuiz() {
        String area = "sat";
        Integer level = 11;

        Optional<List<Quiz>> opt = quizService.getQuiz(area,level);
        Assertions.assertFalse(opt.isPresent());
    }

    @Test
    @Order(3)
    void shouldInsertQuizActivityStart() {
        boolean isInserted = quizService.insertQuizActivityStart(user.getUserId(),"sat",5);
        Assertions.assertTrue(isInserted);
    }

    @Test
    @Order(4)
    void shouldInsertQuizActivityEnd() {
        boolean isInserted = quizService.insertQuizActivityEnd(user.getUserId(),5);
        Assertions.assertTrue(isInserted);
    }

    @Test
    @Order(5)
    void shouldCreateScoreChartForPast5Attempts() {
        String resultUrl = quizService.createScoreChartForPast5Attempts(user.getUserId());
        Assertions.assertNotNull(resultUrl);
    }

    @Test
    @Order(6)
    void shouldCreateAvgScoreChartByDifficultyLevel() {
        String resultUrl = quizService.createAvgScoreChartByDifficultyLevel(user.getUserId());
        Assertions.assertNotNull(resultUrl);
    }

    @AfterAll
    void deleteUserTester() {
        quizService.deleteQuizActivity(user.getUserId());
        userService.deleteUser(user.getUserId());
    }

}
