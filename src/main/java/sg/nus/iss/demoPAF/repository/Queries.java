package sg.nus.iss.demoPAF.repository;

public interface Queries {

    public static final String SQL_AUTHENTICATE_USER =
            "select count(*) as result_count from user where username=? and password=sha1(?);";

    public static final String SQL_SELECT_WORD_AND_USER_ID =
            "select count(*) as count from favourite where user_id=? and word=?;";

    public static final String SQL_SELECT_USER_BY_USERNAME =
            "select * from user where username=?;";

    public static final String SQL_INSERT_FAVOURITE_BY_USERID =
            "insert into favourite (user_id, word) values (?,?);";

    public static final String SQL_SELECT_ALL_FAVOURITE_BY_USER_ORDER_BY_CREATED_DATE =
            "select word from favourite where user_id=? order by created_date desc limit ? offset ?;";

    public static final String SQL_INSERT_QUIZ_ACTIVITY_START =
            "insert into user_quiz_activity (user_id, test_type, difficulty_level) values (?, ?, ?);";
}
