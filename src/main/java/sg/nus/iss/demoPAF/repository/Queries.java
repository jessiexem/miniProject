package sg.nus.iss.demoPAF.repository;

public interface Queries {

    public static final String SQL_AUTHENTICATE_USER =
            "select count(*) as result_count from user where username=? and password=sha1(?);";

    public static final String SQL_SELECT_WORD_AND_USER_ID =
            "select count(*) as count from favourite where user_id=? and word=?;";

    public static final String SQL_SELECT_USER_BY_USERNAME =
            "select * from user where username=?;";

    public static final String SQL_INSERT_USER =
            "insert into user(username,password) values (?, sha1(?));";

    public static final String SQL_INSERT_USER_DETAILS =
            "insert into user_details (user_id, first_name, last_name, email, gender) values (?,?,?,?,?);";

    public static final String SQL_INSERT_FAVOURITE_BY_USERID =
            "insert into favourite (user_id, word) values (?,?);";

    public static final String SQL_SELECT_ALL_FAVOURITE_BY_USER_ORDER_BY_CREATED_DATE =
            "select word from favourite where user_id=? order by created_date desc limit ? offset ?;";

    public static final String SQL_INSERT_QUIZ_ACTIVITY_START =
            "insert into user_quiz_activity (user_id, test_type, difficulty_level) values (?, ?, ?);";

    public static final String SQL_SELECT_QUIZ_ACTIVITY_ID_TO_UPDATE =
            "select quiz_activity_id from user_quiz_activity where user_id=? order by start_time desc limit 1;";

    public static final String SQL_UPDATE_QUIZ_ACTIVITY_END =
            "update user_quiz_activity set quiz_score = ?, end_time = current_timestamp where quiz_activity_id = ?;";

    public static final String SQL_SELECT_SCORE_FOR_PAST_5_ATTEMPTS =
            "select difficulty_level, quiz_score from user_quiz_activity where user_id=? order by end_time desc limit 5;";

    public static final String SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_BY_USER =
            "select difficulty_level, CAST(avg(quiz_score) AS DECIMAL (4,2)) as avg_score from user_quiz_activity where user_id=? group by difficulty_level order by difficulty_level asc;";

    public static final String SQL_SELECT_AVG_SCORE_BY_DIFFICULTY_LEVEL_ALL_USER =
            "select difficulty_level, CAST(avg(quiz_score) AS DECIMAL (4,2)) as avg_score from user_quiz_activity group by difficulty_level order by difficulty_level asc;";

    public static final String SQL_DELETE_FAV_WORD_BY_USER_ID =
            "delete from favourite where word=? and user_id=?";

    //for testing
    public static final String SQL_DELETE_USER_DETAILS_BY_USER_ID =
            "delete from user_details where user_id=?";

    public static final String SQL_DELETE_USER_BY_USER_ID =
            "delete from user where user_id=?";

    public static final String SQL_DELETE_QUIZ_ACTIVITY_BY_USER_ID =
            "delete from user_quiz_activity where user_id=?;";

    public static final String SQL_DELETE_FAVOURITE_BY_USER_ID =
            "delete from favourite where user_id=?;";
}