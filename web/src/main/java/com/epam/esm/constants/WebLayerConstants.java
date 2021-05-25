package com.epam.esm.constants;

public class WebLayerConstants {
    //defaults
    public final static int DEFAULT_LIMIT = 5;
    public final static int DEFAULT_OFFSET = 0;

    //keys
    public final static String LIMIT = "limit";
    public final static String OFFSET = "offset";


    //hateoas
    public final static String UPDATE = "update";
    public final static String PARTIAL_UPDATE = "partial_update";
    public final static String DELETE = "delete";
    public final static String FIRST_PAGE = "firstPage";
    public final static String NEXT_PAGE = "nextPage";
    public final static String LAST_PAGE = "lastPage";
    public final static String MAKE_ORDER = "make_an_order";

    //Codes
    public final static int CERTIFICATE_NOT_FOUND_CODE = 40401;
    public final static int TAG_NOT_FOUND_ERROR_CODE = 40402;
    public final static int USER_NOT_FOUND_BY_ID_ERROR_CODE = 40403;
    public final static int USER_NOT_FOUND_BY_LOGIN_ERROR_CODE = 40405;
    public final static int ORDER_NOT_FOUND_ERROR_CODE = 40404;
    public final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;
    public final static int USER_SUCH_LOGIN_EXISTS_CODE = 42011;
    public final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;
    public final static int DEFAULT_VALIDATION_ERROR_CODE = 50000;
    public final static int MISMATCH_PARAMETER_ERROR_CODE = 50001;
    public final static int REFRESH_TOKEN_EXPIRED = 30010;
    public final static int ACCESS_TOKEN_EXPIRED = 30011;
    public final static int ACCESS_TOKEN_INVALID = 30200;
    public final static int ACCESS_DENIED_ERROR_CODE = 40343;
    public final static int UNAUTHORIZED_ERROR_CODE = 40141;
    public final static int INVALID_CREDENTIALS_ERROR_CODE = 40000;
    public final static int ACCESS_TOKEN_NOT_FOUND = 30300;

    //security
    public static final String AUTH_HEADER = "Authorization";
    public static final String BEARER_PREFIX = "Bearer";
    public static final String ALL_URL_REGEX_PATTERN = "/**";
    public static final String ALL_AUTH_URL_REGEX_PATTERN = "/auth/**";


}
