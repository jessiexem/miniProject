package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.*;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.model.Word;
import sg.nus.iss.demoPAF.repository.WordRepository;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class WordServiceTest {

    @Autowired
    private WordService wordService;

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

    @Autowired
    private WordRepository wordRepo;

    @Test
    @Order(1)
    void shouldReturnListOfWords() {
        String searchTerm = "happy";
        Optional<List<Word>> opt = wordService.searchWord(searchTerm);
        Assertions.assertTrue(opt.isPresent());
    }

    @Test
    @Order(2)
    void shouldNotReturnListOfWords() {
        String searchTerm = "sss";
        Optional<List<Word>> opt = wordService.searchWord(searchTerm);
        Assertions.assertFalse(opt.isPresent());
    }

    @Test
    @Order(3)
    void shouldAddFavWord() {
        int userId = user.getUserId();
        String favWord = "favourite";
        boolean isAdded = wordService.addFavWord(userId,favWord);
        Assertions.assertTrue(isAdded);
    }

    @Test
    @Order(4)
    void shouldGetFavouriteByUser() {
        Optional<List<String>> opt = wordService.getAllFavouriteByUser(user.getUserId());
        List<String> favList = opt.get();

        Assertions.assertEquals(1,favList.size());
    }

    @AfterAll
    void deleteUserWilma() {
        wordService.deleteAllFavouriteByUser(user.getUserId());
        userService.deleteUser(user.getUserId());
    }

}
