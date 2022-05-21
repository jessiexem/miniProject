package sg.nus.iss.demoPAF.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import sg.nus.iss.demoPAF.exception.UserLoginException;
import sg.nus.iss.demoPAF.model.User;
import sg.nus.iss.demoPAF.repository.UserRepository;

import java.util.Optional;
import java.util.logging.Logger;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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

    @Transactional
    public void createUser(String username, String password,
                           String firstName, String lastName, String email, String gender) {

        try {
            Pattern p = Pattern.compile("[^a-z0-9 ]", Pattern.CASE_INSENSITIVE);
            Matcher m_first = p.matcher(firstName);
            boolean b_first = m_first.find();
            Matcher m_last = p.matcher(lastName);
            boolean b_last = m_last.find();

            if (b_first || b_last) {
                throw new IllegalArgumentException("Cannot have name with special characters!");
            }

            //insert into User table
            final Integer userId = userRepo.createUser(username,password);

            //insert into UserDetails table
            boolean isUserDetailsAdded = userRepo.createUserDetails(userId, firstName, lastName, email, gender);

            if (!isUserDetailsAdded) {
                throw new DataAccessException("Cannot create user") {};
            }

        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

    }

    @Transactional
    public void deleteUser(int userId) {
        boolean isDetailsDeleted = userRepo.deleteUserDetails(userId);
        boolean isUserDeleted = userRepo.deleteUser(userId);

        if ( !isDetailsDeleted || !isUserDeleted) {
            throw new DataAccessException("Cannot delete user") {};
        }
    }

}
