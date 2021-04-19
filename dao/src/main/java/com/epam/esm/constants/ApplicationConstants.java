package com.epam.esm.constants;

public final class ApplicationConstants {

    private ApplicationConstants() {}

    //table names
    public final static String CERTIFICATE_TAGS_TABLE_NAME = "certificates_tags";

    //joins


    public final static String TAGS_ATTRIBUTE_NAME = "tags";
    public final static String GC_ATTRIBUTE_NAME = "giftCertificates";
    public final static String FIND_ALL_GC_BY_TAG_NAMES = "SELECT gc FROM GiftCertificate gc JOIN gc.tags t WHERE t.name =:name";
    public final static String GET_ALL_TAG_BY_CERTIFICATE_ID = "SELECT t FROM Tag t JOIN t.giftCertificates gc WHERE gc.id = :certificateId";

    //gift_certificate table
    public final static String FIND_ALL_GC_QUERY = "SELECT gc FROM GiftCertificate gc";


    public final static String FIND_GC_BY_NAME_QUERY = "SELECT gc FROM GiftCertificate gc WHERE gc.name = :name";
    //tag table
    public final static String GET_ALL_TAG_QUERY = "SELECT t FROM Tag t";
    public final static String GET_TAG_BY_NAME = "SELECT t FROM Tag t WHERE t.name =:name";

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
    public final static String LIMIT_KEY = "limit";
public final static String OFFSET_KEY = "offset";





    public final static String NAMES_PART_CRITERIA_BEAN_NAME = "namePartsGCCriteria";
    public final static String DESCRIPTION_PART_CRITERIA_BEAN_NAME = "descriptionPartsGCCriteria";
    public final static String TAG_NAMES_CRITERIA_BEAN_NAME = "tagNamesGCCriteria";
    public final static String ALL_CRITERIA_BEAN_NAME = "allGCCriteria";

    //Tag factory
    public final static String ALL_CRITERIA_TAG_BEAN_NAME = "allTagCriteria";
    public final static String CERTIFICATE_ID_TAG_BEAN_NAME = "certificateIdTagCriteria";
    public final static String CERTIFICATE_ID_KEY = "certificateId";

    //User QueryBuilder
    public final static String ROLE_KEY = "role";


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
    public final static int USER_FOUND_ERROR_CODE = 40403;
    public final static int INVALID_SORT_FIELD_ERROR_CODE = 44601;
    public final static int CERTIFICATE_WITH_SUCH_NAME_EXISTS_CODE = 42010;
    public final static int TAG_WITH_SUCH_NAME_EXISTS_ERROR_CODE = 42000;
    public final static int DEFAULT_VALIDATION_ERROR_CODE = 50000;

    //SortingHelper
    public final static String DESC_ORDER = "desc";
    public final static String ASC_ORDER = "asc";

    //User
    public final static String USER_ID_FIELD = "id";
    public final static String USER_ROLE_FIELD = "role";


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


}
