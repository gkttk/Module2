package com.epam.esm.constants;


/**
 * This class contains all application constants.
 */
public final class ApplicationConstants {

    //defaults
    public final static int MAX_LIMIT = Integer.MAX_VALUE;
    public final static int DEFAULT_LIMIT = 5;
    public final static int DEFAULT_OFFSET = 0;

    //hateoas
    public final static String UPDATE = "update";
    public final static String PARTIAL_UPDATE = "partial_update";
    public final static String DELETE = "delete";
    public final static String FIRST_PAGE = "firstPage";
    public final static String NEXT_PAGE = "nextPage";
    public final static String MAKE_ORDER = "make_an_order";

    //table names
    public final static String CERTIFICATE_TAGS_TABLE_NAME = "certificates_tags";
    public final static String USERS_ORDERS_TABLE_NAME = "users_orders";

    //GiftCertificate queries
    public final static String FIND_GC_BY_NAME_QUERY = "SELECT gc FROM GiftCertificate gc WHERE gc.name = :name";

    //Tag queries
    public final static String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name =:name";

    //User queries
    public final static String GET_USER_BY_LOGIN = "SELECT u FROM User u WHERE u.login =:login";

    //certificate_tags queries
    public final static String SAVE_CERTIFICATE_TAGS_QUERY = "INSERT INTO " + CERTIFICATE_TAGS_TABLE_NAME + " VALUES (?,?)";
    public final static String SAVE_USER_ORDER_QUERY = "INSERT INTO " + USERS_ORDERS_TABLE_NAME + " VALUES (?,?)";
    public final static String DELETE_ALL_TAGS_FOR_CERTIFICATE = "DELETE FROM " + CERTIFICATE_TAGS_TABLE_NAME +
            " WHERE certificate_id = ?";

    //QueryBuilders
    public final static String USER_ID_KEY = "userId";
    public final static String USER_ID_FIELD = "id";

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
    public final static String TAG_AND_NAMES_KEY = "tagNames(and)";
    public final static String TAGS_ATTRIBUTE_NAME = "tags";

    //Tag QueryBuilder
    public final static String TAG_ID_FIELD = "id";
    public final static String TAG_NAME_FIELD = "name";
    public final static String CERTIFICATE_ID_KEY = "certificateId";
    public final static String GC_ATTRIBUTE_NAME = "giftCertificates";

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
    public final static int USER_NOT_FOUND_ERROR_CODE = 40403;
    public final static int ORDER_NOT_FOUND_ERROR_CODE = 40404;
    public final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;
    public final static int USER_SUCH_LOGIN_EXISTS_CODE = 42011;
    public final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;
    public final static int DEFAULT_VALIDATION_ERROR_CODE = 50000;
}
