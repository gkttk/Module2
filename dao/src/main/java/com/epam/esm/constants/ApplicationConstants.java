package com.epam.esm.constants;

public final class ApplicationConstants {

    private ApplicationConstants() {}

    //queries
    //table names
    public final static String GIFT_CERTIFICATE_TABLE_NAME = "gift_certificate";
    public final static String TAG_TABLE_NAME = "tag";
    public final static String CERTIFICATE_TAGS_TABLE_NAME = "certificates_tags";

    //joins
    public final static String GET_ALL_GC_BY_TAG_NAME = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration," +
            " create_date, last_update_date FROM " + GIFT_CERTIFICATE_TABLE_NAME + " gc JOIN " + CERTIFICATE_TAGS_TABLE_NAME
            + " ct on gc.id = ct.certificate_id JOIN " + TAG_TABLE_NAME + " t on t.id = ct.tag_id WHERE t.name = ?";
    public final static String GET_ALL_TAG_BY_CERTIFICATE_ID = "SELECT t.id, t.name FROM " + TAG_TABLE_NAME +
            " t JOIN certificates_tags ct on t.id = ct.tag_id WHERE ct.certificate_id = ?";

    //gift_certificate table
    public final static String GET_ALL_GC_QUERY = "SELECT id, name, description, price, duration," +
            "create_date, last_update_date FROM " + GIFT_CERTIFICATE_TABLE_NAME;
    public final static String GET_BY_ID_GC_QUERY = "SELECT id, name, description, price, duration, create_date," +
            " last_update_date FROM " + GIFT_CERTIFICATE_TABLE_NAME + " WHERE id = ?";
    public final static String SAVE_GC_QUERY = "INSERT INTO " + GIFT_CERTIFICATE_TABLE_NAME + " (name, description, price, duration) " + "VALUES (?,?,?,?)";
    public final static String UPDATE_GC_QUERY = "UPDATE " + GIFT_CERTIFICATE_TABLE_NAME + " SET name = ?, description = ?, " +
            "price = ?, duration = ? WHERE id = ?";
    public final static String DELETE_GC_QUERY = "DELETE FROM " + GIFT_CERTIFICATE_TABLE_NAME + " WHERE id = ?";

    public final static String GET_BY_NAME_GC_QUERY = "SELECT id, name, description, price, duration, create_date," +
            " last_update_date FROM " + GIFT_CERTIFICATE_TABLE_NAME + " WHERE name = ?";

    //tag table
    public final static String GET_ALL_TAG_QUERY = "SELECT id, name FROM " + TAG_TABLE_NAME;
    public final static String GET_BY_ID_TAG_QUERY = "SELECT id, name FROM " + TAG_TABLE_NAME + " WHERE id = ?";
    public final static String SAVE_TAG_QUERY = "INSERT INTO " + TAG_TABLE_NAME + " (name) " +
            "VALUES (?)";
    public final static String DELETE_TAG_QUERY = "DELETE FROM " + TAG_TABLE_NAME + " WHERE id = ?";
    public final static String GET_BY_NAME_TAG_QUERY = "SELECT id, name FROM " + TAG_TABLE_NAME + " WHERE name = ?";

    //certificate_tags table
    public final static String SAVE_CERTIFICATE_TAGS_QUERY = "INSERT INTO " + CERTIFICATE_TAGS_TABLE_NAME  + " VALUES (?,?)";
    public final static String DELETE_ALL_TAGS_FOR_CERTIFICATE = "DELETE FROM " + CERTIFICATE_TAGS_TABLE_NAME +
            " WHERE certificate_id = ?";



    //DaoConfig
    public final static String HIKARI_PROPERTIES_PATH = "/hikari.properties";
    public final static String HIKARI_PROPERTIES_TEST_PATH = "/hikari-test.properties";
    //Criteria
    public final static String RESULTS_SET_KEY = "certificates";
    public final static String NAME_IN_PARAM_FOR_PART_OF_DESCRIPTION = "in_partOfDescription";
    public final static String GET_BY_DESCRIPTION_PART_PROCEDURE_NAME = "searchByPartOfDescription";
    public final static String GET_BY_NAME_PART_PROCEDURE_NAME = "searchByPartOfName";
    public final static String NAME_IN_PARAM_FOR_PART_OF_NAME = "in_partOfName";

    //GiftCertificate criteria factory
    public final static String NAMES_PART_KEY = "namesPart";
    public final static String DESCRIPTION_PART_KEY = "descriptionsPart";
    public final static String TAG_NAMES_KEY = "tagNames";

    public final static String NAMES_PART_CRITERIA_BEAN_NAME = "namePartsGCCriteria";
    public final static String DESCRIPTION_PART_CRITERIA_BEAN_NAME = "descriptionPartsGCCriteria";
    public final static String TAG_NAMES_CRITERIA_BEAN_NAME = "tagNamesGCCriteria";
    public final static String ALL_CRITERIA_BEAN_NAME = "allGCCriteria";

    //Tag factory
    public final static String ALL_CRITERIA_TAG_BEAN_NAME = "allTagCriteria";
    public final static String CERTIFICATE_ID_TAG_BEAN_NAME = "certificateIdTagCriteria";
    public final static String CERTIFICATE_ID_KEY = "certificateId";

    //Mappers
    public final static String DATE_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public final static String ID_RS_KEY = "id";

    //GiftCertificate mapper
    public final static String NAME_GC_RS_KEY = "name";
    public final static String DESCRIPTION_GC_RS_KEY = "description";
    public final static String PRICE_GC_RS_KEY = "price";
    public final static String DURATION_GC_RS_KEY = "duration";
    public final static String CREATE_DATE_GC_RS_KEY = "create_date";
    public final static String LAST_UPDATE_DATE_GC_RS_KEY = "last_update_date";

    //Tag mapper
    public final static String NAME_TAG_RS_KEY = "name";

    //Services
    public final static String SORT_FIELDS_KEY = "sortFields";
    public final static String ORDER_KEY = "order";

    //Codes
    public final static int CERTIFICATE_NOT_FOUND_CODE = 40401;
    public final static int TAG_NOT_FOUND_ERROR_CODE = 40402;
    public final static int INVALID_SORT_FIELD_ERROR_CODE = 44601;
    public final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;
    public final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;
    public final static int DEFAULT_VALIDATION_ERROR_CODE = 50000;

    //SortingHelper
    public final static String DESC_ORDER = "desc";

    //GiftCertificate SortingHelper
    public final static String ID_FIELD = "id";
    public final static String NAME_FIELD = "name";
    public final static String DESCRIPTION_FIELD = "description";
    public final static String PRICE_FIELD = "price";
    public final static String DURATION_FIELD = "duration";
    public final static String CREATE_DATE_FIELD = "createDate";
    public final static String LAST_UPDATE_DATE_FIELD = "lastUpdateDate";

    //Tag SortingHelper
    public final static String TAG_ID_FIELD = "id";
    public final static String TAG_NAME_FIELD = "name";

    //WebConfig
    public final static String VIEW_RESOLVER_PREFIX = "/WEB-INF/views/";
    public final static String VIEW_RESOLVER_SUFFIX = ".jsp";


}
