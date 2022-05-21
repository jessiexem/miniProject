package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import static sg.nus.iss.demoPAF.repository.Queries.*;

@Repository
public class QuizRepository {

    @Autowired
    private JdbcTemplate template;

    private final Logger logger = Logger.getLogger(QuizRepository.class.getName());

    public boolean insertStartQuizActivity(int userId, String area, int difficultyLevel) {
        int added = template.update(SQL_INSERT_QUIZ_ACTIVITY_START, userId, area, difficultyLevel);
        return added>0;
    }

    public boolean endQuizActivity(int userId, int score) {
        SqlRowSet result = template.queryForRowSet(SQL_SELECT_QUIZ_ACTIVITY_ID_TO_UPDATE,userId);

        if(!result.next()) {
            logger.severe("No result is returned from SQL query");
            throw new RuntimeException();
        }

        int activityId = result.getInt("quiz_activity_id");

        int updated = template.update(SQL_UPDATE_QUIZ_ACTIVITY_END, score, activityId);

        return updated==1;
    }

    public boolean deleteQuizActivity(int userId) {
        int deleted = template.update(SQL_DELETE_QUIZ_ACTIVITY_BY_USER_ID,userId);
        return deleted>0;
    }

    public String getLast5ScoreByUser(int userId) {
        SqlRowSet result = template.queryForRowSet(SQL_SELECT_SCORE_FOR_PAST_5_ATTEMPTS,userId);

        if(!result.isBeforeFirst()) {
            return null;
        }

        List<Integer> list = new ArrayList<>();
        while (result.next()) {
            list.add(result.getInt("quiz_score"));
        }

        Collections.reverse(list);

        String listString = list.stream().map(Object::toString)
                .collect(Collectors.joining(", "));

        return listString;
    }

    public SqlRowSet getAvgScoreByDifficultyLevelByUser(int userId) {
        return template.queryForRowSet(SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_BY_USER,userId);

    }

    public SqlRowSet getAvgScoreByDifficultyLevel() {
        return template.queryForRowSet(SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_ALL_USER);
    }




}
