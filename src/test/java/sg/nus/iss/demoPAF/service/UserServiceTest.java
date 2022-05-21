package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
public class UserServiceTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private UserService userSvc;

    @Test
    @Order(1)
    void shouldNotCreateUserWithSpecialCharInName() {
        Assertions.assertThrows(IllegalArgumentException.class,
                ()->userSvc.createUser("bernie95","abc123",
                        "bern!e","t@n","bern@gmail.com",
                        "F"));
    }

    @Test
    @Order(2)
    void createUserTesterAndShouldReturnUser() {
        userSvc.createUser("tester95","abc123","tester",
                "tan","tester@gmail.com","F");

        Optional<User> opt = userSvc.findUserByUsername("tester95");
        Assertions.assertTrue(opt.isPresent());
    }


    @Test
    @Order(3)
    void shouldReturnTrueUserLogin() {
        User user = new User("tester95", "abc123");
        boolean isSuccess = false;
        try {
            isSuccess = userSvc.userLogin(user);
        } catch (UserLoginException e) {
            fail("Did not find user when credentials exist.");
        }
        Assertions.assertTrue(isSuccess);
    }

    @Test
    @Order(4)
    void shouldReturnFalseUserLogin() {
        User user = new User("minion", "password");
        boolean isSuccess = false;
        try {
            isSuccess = userSvc.userLogin(user);
        } catch (UserLoginException e) {
            fail("Did not find user when credentials exist.");
        }
        Assertions.assertFalse(isSuccess);
    }

    @Test
    @Order(5)
    void shouldDeleteUserTester() {

        Optional<User> opt = userSvc.findUserByUsername("tester95");
        if (opt.isEmpty()) {
            fail("user does not exists");
        }

        userSvc.deleteUser(opt.get().getUserId());
        Optional<User> opt1 = userSvc.findUserByUsername("tester95");
        Assertions.assertTrue(opt1.isEmpty());
    }

}
