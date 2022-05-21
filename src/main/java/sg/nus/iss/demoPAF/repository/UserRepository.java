package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import sg.nus.iss.demoPAF.controller.MainController;
import sg.nus.iss.demoPAF.model.User;

import java.math.BigInteger;
import java.sql.PreparedStatement;
import java.sql.Statement;
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

    public Integer createUser(String username, String password) {

        KeyHolder keyholder = new GeneratedKeyHolder();
        template.update(conn -> {
            PreparedStatement ps = conn.prepareStatement(SQL_INSERT_USER,
                    Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, username);
            ps.setString(2, password);
            return ps;
        }, keyholder);

        BigInteger bigint = (BigInteger) keyholder.getKey();
        return bigint.intValue();
    }

    public boolean createUserDetails(int userId, String firstName, String lastName, String email, String gender) {
        int added = template.update(SQL_INSERT_USER_DETAILS, userId, firstName, lastName, email, gender);
        return added==1;
    }

    public boolean deleteUserDetails(int userId) {
        int deleted = template.update(SQL_DELETE_USER_DETAILS_BY_USER_ID, userId);
        return deleted==1;
    }

    public boolean deleteUser(int userId) {
        int deleted = template.update(SQL_DELETE_USER_BY_USER_ID, userId);
        return deleted==1;
    }
}
