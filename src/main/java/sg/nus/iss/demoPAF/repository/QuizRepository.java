package sg.nus.iss.demoPAF.repository;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
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

    public HashMap<String,String> getLast5ScoreByUser(int userId) {
        SqlRowSet result = template.queryForRowSet(SQL_SELECT_SCORE_FOR_PAST_5_ATTEMPTS,userId);

        if(!result.isBeforeFirst()) {
            return null;
        }

        List<Integer> scoreList = new ArrayList<>();
        List<Integer> levelList = new ArrayList<>();
        while (result.next()) {
            scoreList.add(result.getInt("quiz_score"));
            levelList.add(result.getInt("difficulty_level"));
        }

        Collections.reverse(scoreList);
        Collections.reverse(levelList);

        String scoreListString = scoreList.stream().map(Object::toString)
                .collect(Collectors.joining(", "));

        String levelListString = levelList.stream().map(Object::toString)
                .collect(Collectors.joining(", "));

        HashMap<String,String> map = new HashMap<>();
        map.put("scoreListString",scoreListString);
        map.put("levelListString",levelListString);

        return map;
    }

    public SqlRowSet getAvgScoreByDifficultyLevelByUser(int userId) {
        return template.queryForRowSet(SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_BY_USER,userId);

    }

    public SqlRowSet getAvgScoreByDifficultyLevel() {
        return template.queryForRowSet(SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_ALL_USER);
    }




}
