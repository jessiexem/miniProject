package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;
import sg.nus.iss.demoPAF.controller.MainController;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.logging.Logger;

import static sg.nus.iss.demoPAF.repository.Queries.*;

@Repository
public class WordRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(WordRepository.class.getName());

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

    public Optional<List<String>> getAllFavouriteByUser(int user_id, Integer limit, Integer offset) {
        final SqlRowSet rs = template.queryForRowSet(SQL_SELECT_ALL_FAVOURITE_BY_USER_ORDER_BY_CREATED_DATE,
                user_id,limit,offset);

        if(!rs.isBeforeFirst()) {
            logger.warning(">>>WordRepo: no data found.");
            return Optional.empty();
        }
        else {
            List<String> wordList= new ArrayList<>();
            while (rs.next()) {
                wordList.add(rs.getString("word"));
            }
            return Optional.of(wordList);
        }
    }

    public boolean deleteFavouriteByUser(int userId) {
        int isDeleted = template.update(SQL_DELETE_FAVOURITE_BY_USER_ID,userId);
        return isDeleted>0;
    }
}
