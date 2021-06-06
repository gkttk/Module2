package com.epam.esm.constants;


/**
 * This class contains all application constants.
 */
public final class ApplicationConstants {
    //defaults
    public final static String APPLICATION_NAME = "GiftsApplication";
    public final static int MAX_LIMIT = Integer.MAX_VALUE;
    public final static int DEFAULT_LIMIT = 5;
    public final static int DEFAULT_OFFSET = 0;

    //table names
    public final static String CERTIFICATE_TAGS_TABLE_NAME = "certificates_tags";
    public final static String USERS_ORDERS_TABLE_NAME = "users_orders";

    //GiftCertificate queries
    public final static String FIND_GC_BY_NAME_QUERY = "SELECT gc FROM GiftCertificate gc WHERE gc.name = :name";
    public final static String COUNT_GC_QUERY = "SELECT count(gc.id) FROM GiftCertificate gc";

    //Tag queries
    public final static String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name =:name";
    public final static String COUNT_TAG_QUERY = "SELECT count(t.id) FROM Tag t";

    public final static String FIND_MAX_WIDELY_USED_QUERY = "SELECT t.id, t.name from tag t " +
            "JOIN certificates_tags ct on t.id = ct.tag_id " +
            "JOIN orders_certificates oc on ct.certificate_id = oc.certificate_id " +
            "JOIN users_orders uo on oc.order_id = uo.order_id " +
            "WHERE uo.user_id = :userId " +
            "GROUP BY t.id " +
            "HAVING count(t.id)  = (SELECT count(t.id) as c from tag t " +
            "JOIN certificates_tags ct on t.id = ct.tag_id " +
            "JOIN orders_certificates oc on ct.certificate_id = oc.certificate_id " +
            "JOIN users_orders uo on oc.order_id = uo.order_id " +
            "WHERE uo.user_id = :userId " +
            "GROUP BY t.id order by c desc LIMIT 1)";

    //User queries
    public final static String GET_USER_BY_LOGIN = "SELECT u FROM User u WHERE u.login =:login";
    public final static String COUNT_USER_QUERY = "SELECT count(u.id) FROM User u";

    //Order queries
    public final static String COUNT_ORDER_BY_USER_ID_QUERY = "SELECT count(o.id) FROM Order o " +
            " JOIN o.user uo WHERE uo.id =:userId";

    //Refresh token queries
    public final static String DELETE_ALL_BY_DATE_BEFORE = "DELETE FROM RefreshToken t WHERE t.expiredTime <= :date";

    //certificate_tags queries
    public final static String SAVE_CERTIFICATE_TAGS_QUERY = "INSERT INTO " + CERTIFICATE_TAGS_TABLE_NAME + " VALUES (?,?)";
    public final static String SAVE_USER_ORDER_QUERY = "INSERT INTO " + USERS_ORDERS_TABLE_NAME + " VALUES (?,?)";
    public final static String DELETE_ALL_TAGS_FOR_CERTIFICATE = "DELETE FROM " + CERTIFICATE_TAGS_TABLE_NAME +
            " WHERE certificate_id = ?";
    public final static String DELETE_ALL_CERTIFICATES_FOR_TAG = "DELETE FROM " + CERTIFICATE_TAGS_TABLE_NAME +
            " WHERE tag_id = ?";

    //QueryBuilders
    public final static String USER_ID_KEY = "userId";
    public final static String USER_ID_FIELD = "id";
    public final static String AND_OPERATOR_NAME = "AND";

    //User QueryBuilder
    public final static String ROLE_KEY = "role";
    public final static String USER_ROLE_FIELD = "role";
    public final static String LOGIN_NAME_FIELD = "login";

    //GiftCertificate QueryBuilder
    public final static String ID_FIELD = "id";
    public final static String NAME_FIELD = "name";
    public final static String DESCRIPTION_FIELD = "description";
    public final static String PRICE_FIELD = "price";
    public final static String DURATION_FIELD = "duration";
    public final static String CREATE_DATE_FIELD = "createDate";
    public final static String LAST_UPDATE_DATE_FIELD = "lastUpdateDate";
    public final static String NAMES_PART_KEY = "namesPart";
    public final static String DESCRIPTION_PART_KEY = "descriptionsPart";
    public final static String TAG_NAMES_KEY = "tagNames";
    public final static String TAGS_ATTRIBUTE_NAME = "tags";

    //Tag QueryBuilder
    public final static String TAG_ID_FIELD = "id";
    public final static String TAG_NAME_FIELD = "name";
    public final static String CERTIFICATE_ID_KEY = "certificateId";
    public final static String GC_ATTRIBUTE_NAME = "giftCertificates";
    public final static String MAX_WIDELY_USED = "maxWidelyUsed";

    //Order QueryBuilder
    public final static String USER_ATTRIBUTE_NAME = "user";

    //QueryBuilders sorting
    public final static String SORT_FIELDS_KEY = "sortFields";
    public final static String ORDER_KEY = "order";
    public final static String DESC_ORDER = "desc";
    public final static String ASC_ORDER = "asc";

    //QueryBuilders order
    public final static String ORDER_ID_FIELD = "id";
    public final static String ORDER_COST_FIELD = "cost";
    public final static String ORDER_CREATION_DATE_FIELD = "creationDate";

    //Codes
    public final static int CERTIFICATE_NOT_FOUND_CODE = 40401;
    public final static int TAG_NOT_FOUND_ERROR_CODE = 40402;
    public final static int USER_NOT_FOUND_BY_ID_ERROR_CODE = 40403;
    public final static int USER_NOT_FOUND_BY_LOGIN_ERROR_CODE = 40405;
    public final static int ORDER_NOT_FOUND_ERROR_CODE = 40404;
    public final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;
    public final static int USER_SUCH_LOGIN_EXISTS_CODE = 42011;
    public final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;
    public final static int REFRESH_TOKEN_EXPIRED = 30010;
    public final static int ACCESS_TOKEN_EXPIRED = 30011;
    public final static int TOKEN_INVALID = 30200;
    public final static int ACCESS_DENIED_ERROR_CODE = 40343;
    public final static int UNAUTHORIZED_ERROR_CODE = 40141;
    public final static int INVALID_CREDENTIALS_ERROR_CODE = 40000;
    //Parameter parser
    public static final String REGEX_FOR_SPLIT_PARAMETERS = ",(?!$)";

    //JWT
    public static final String ACCESS_TOKEN_SECRET = "$2a$04$dt10Yo.GcZ3SrqBzYIPrr.j1TvSkbjSFd.P.vR8D0H2BxE51.SOL6";
    public static final String REFRESH_TOKEN_SECRET = "$2a$04$a3SxC0GgQv0srAfaOeDdO..qM6QD0CAfApBhbhhBz5dSMXP0gElHG";
    public static final long ACCESS_TOKEN_EXPIRED_TIME_IN_MILLISECONDS = 600000;
    public static final long REFRESH_TOKEN_EXPIRED_TIME_IN_MILLISECONDS = 3600000;

    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";

    //filters
    public static final String MAKE_ORDER_URI = "/users/*/orders";
    public static final String USER_ID_MAKE_ORDER_REGEX = "(?<=users/)\\d+(?=/orders)";
}
