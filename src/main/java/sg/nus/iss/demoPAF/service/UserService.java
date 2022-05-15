package sg.nus.iss.demoPAF.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import sg.nus.iss.demoPAF.controller.MainController;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.repository.UserRepository;

import java.util.Optional;
import java.util.logging.Logger;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepo;

    private final Logger logger = Logger.getLogger(UserService.class.getName());

    public boolean userLogin(User user) throws UserLoginException {

        SqlRowSet result = userRepo.authenticateUserByUsernameAndPassword(user);

        logger.info(">>> Result_count: "+ result.getInt("result_count"));

        int result_count = result.getInt("result_count");

        if (result_count==0 ) {
            return false;
        }

        else if (result_count==1 ) {
            return true;
        }

        else if (result_count>1 ){
            throw new UserLoginException("There are more than 1 matching credentials");
        }

        else return false;
    }

    public Optional<User> findUserByUsername(String username) {
        return userRepo.findUserByUsername(username);
    }

}
