package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import sg.nus.iss.demoPAF.controller.MainController;
import sg.nus.iss.demoPAF.model.User;

import java.util.Optional;
import java.util.logging.Logger;

import static sg.nus.iss.demoPAF.repository.Queries.*;


@Repository
public class UserRepository {

    @Autowired
    JdbcTemplate template;

    private final Logger logger = Logger.getLogger(UserRepository.class.getName());

    public SqlRowSet authenticateUserByUsernameAndPassword(User user) {

        SqlRowSet result = template.queryForRowSet(SQL_AUTHENTICATE_USER,
                user.getUsername(),
                user.getPassword());

        if(!result.next()) {
            logger.severe("No result is returned from SQL query");
            throw new RuntimeException();
        }

        return result;
    }

    public Optional<User> findUserByUsername(String username) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_USER_BY_USERNAME,username);
        if(!rs.first()) {
            logger.warning(">>>> UserRepository: findUserByUsername: no data found");
            return Optional.empty();
        }
        else return Optional.of(User.create(rs));
    }
}
