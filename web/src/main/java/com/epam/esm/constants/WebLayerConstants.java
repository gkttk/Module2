package com.epam.esm.constants;

public class WebLayerConstants {
    //defaults
    public final static int DEFAULT_LIMIT = 5;
    public final static int DEFAULT_OFFSET = 0;

    //hateoas
    public final static String UPDATE = "update";
    public final static String PARTIAL_UPDATE = "partial_update";
    public final static String DELETE = "delete";
    public final static String FIRST_PAGE = "firstPage";
    public final static String NEXT_PAGE = "nextPage";
    public final static String MAKE_ORDER = "make_an_order";

    public final static String USER_ID_KEY = "userId";


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
