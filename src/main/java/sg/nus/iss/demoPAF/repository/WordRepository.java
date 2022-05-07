package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import static sg.nus.iss.demoPAF.repository.Queries.*;

@Repository
public class WordRepository {

    @Autowired
    private JdbcTemplate template;

    public int getWordByUser(int user_id, String word) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_WORD_AND_USER_ID,user_id,word);
        if (!rs.first()) {
            return 0;
        }
        return rs.getInt("count");
    }

    public boolean insertFavouriteWordByUserId(int user_id, String word) {
        int added = template.update(SQL_INSERT_FAVOURITE_BY_USERID,user_id,word);
        return added>0;
    }
}
