package sg.nus.iss.demoPAF.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.repository.UserRepository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
public class UserServiceTest {

    @Autowired
    UserRepository userRepo;

    @Autowired
    private UserService userSvc;

    @Test
    void shouldReturnUserFred() {
        Optional<User> opt = userSvc.findUserByUsername("fred");
        Assertions.assertTrue(opt.isPresent());
        Assertions.assertEquals(2,opt.get().getUserId());
    }

    @Test
    void shouldReturnTrueUserLogin() {
        User user = new User("fred", "password");
        boolean isSuccess = false;
        try {
            isSuccess = userSvc.userLogin(user);
        } catch (UserLoginException e) {
            fail("Did not find user when credentials exist.");
        }
        Assertions.assertTrue(isSuccess);
    }

    @Test
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
}
