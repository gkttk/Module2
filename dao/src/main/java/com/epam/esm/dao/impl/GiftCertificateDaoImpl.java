package com.epam.esm.dao.impl;

import com.epam.esm.dao.GiftCertificateDao;
import com.epam.esm.entity.GiftCertificate;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcCall;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import java.sql.PreparedStatement;
import java.sql.Statement;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.StringJoiner;


/**
 * Default implementation of {@link com.epam.esm.dao.GiftCertificateDao} interface.
 *
 * @since 1.0
 */
@Repository
public class GiftCertificateDaoImpl implements GiftCertificateDao {

    private final static String TABLE_NAME = "gift_certificate";
    private final static String GET_BY_ID_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE id = ?";
    private final static String GET_ALL_QUERY = "SELECT * FROM " + TABLE_NAME;
    private final static String SAVE_QUERY = "INSERT INTO " + TABLE_NAME + " (name, description, price, duration," +
            " create_date, last_update_date) " + "VALUES (?,?,?,?,?,?)";
    private final static String UPDATE_QUERY = "UPDATE " + TABLE_NAME + " SET name = ?, description = ?, " +
            "price = ?, duration = ?, last_update_date = ? WHERE id = ?";
    private final static String DELETE_QUERY = "DELETE FROM " + TABLE_NAME + " WHERE id = ?";

    private final static String GET_ALL_BY_TAG_NAME = "SELECT gc.id, gc.name, gc.description, gc.price, gc.duration," +
            " gc.create_date, gc.last_update_date FROM " + TABLE_NAME + " gc JOIN certificates_tags ct on gc.id = ct.certificate_id" +
            " JOIN tag t on t.id = ct.tag_id WHERE t.name = ?";

    private final static String SORTING_QUERY_FIRST_PART = "SELECT * FROM " + TABLE_NAME + " ORDER BY";

    private final static String GET_BY_NAME_QUERY = "SELECT * FROM " + TABLE_NAME + " WHERE name = ?";


    private final static String GET_BY_NAME_PART_PROCEDURE_NAME = "searchByPartOfName";
    private final static String GET_BY_DESCRIPTION_PART_PROCEDURE_NAME = "searchByPartOfDescription";
    private final static String RESULTS_SET_KEY = "certificates";

    private final static String NAME_IN_PARAM_FOR_PART_OF_DESCRIPTION = "in_partOfDescription";
    private final static String NAME_IN_PARAM_FOR_PART_OF_NAME = "in_partOfName";

    private final JdbcTemplate template;
    private final RowMapper<GiftCertificate> rowMapper;

    @Autowired
    public GiftCertificateDaoImpl(JdbcTemplate template, RowMapper<GiftCertificate> rowMapper) {
        this.template = template;
        this.rowMapper = rowMapper;
    }

    /**
     * This method get GiftCertificate entity by name.
     *
     * @param name GiftCertificate entity's name.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given name, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> getByName(String name) {
        GiftCertificate result = template.queryForStream(GET_BY_NAME_QUERY, rowMapper, name).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * This method saves GiftCertificate entity.
     * The method uses KayHolder for getting generated id for GiftCertificate entity from db.
     *
     * @param certificate GiftCertificate entity without id.
     * @return GiftCertificate entity with generated id.
     * @since 1.0
     */
    @Override
    public GiftCertificate save(GiftCertificate certificate) {

        KeyHolder keyHolder = new GeneratedKeyHolder();
        template.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SAVE_QUERY, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, certificate.getName());
            ps.setString(2, certificate.getDescription());
            ps.setBigDecimal(3, certificate.getPrice());
            ps.setInt(4, certificate.getDuration());
            ps.setString(5, certificate.getCreateDate());
            ps.setString(6, certificate.getLastUpdateDate());
            return ps;
        }, keyHolder);

        certificate.setId(keyHolder.getKey().longValue());

        return certificate;
    }

    /**
     * This method get GiftCertificate entity by id.
     *
     * @param id GiftCertificate entity's id.
     * @return Optional of GiftCertificate entity. If there is no GiftCertificate with given id, return Optional.empty().
     * @since 1.0
     */
    @Override
    public Optional<GiftCertificate> getById(long id) {
        GiftCertificate result = template.queryForStream(GET_BY_ID_QUERY, rowMapper, id).findFirst().orElse(null);
        return Optional.ofNullable(result);
    }

    /**
     * This method get all GiftCertificate entities.
     *
     * @return List of all GiftCertificate entities.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getAll() {
        return template.query(GET_ALL_QUERY, rowMapper);
    }


    /**
     * This method get GiftCertificate entities by part of their description.
     *
     * @param partOfDescription part of GiftCertificate entity's description for search
     * @return List of GiftCertificate entities which was found by part of description.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getAllByPartOfDescription(String partOfDescription) {

        SqlParameterSource params = new MapSqlParameterSource().addValue(NAME_IN_PARAM_FOR_PART_OF_DESCRIPTION, partOfDescription);
        Map<String, Object> procedureResult = executeProcedure(GET_BY_DESCRIPTION_PART_PROCEDURE_NAME, params);
        return (List<GiftCertificate>) procedureResult.get(RESULTS_SET_KEY);

    }

    /**
     * This method get GiftCertificate entities by part of their name.
     *
     * @param partOfName part of GiftCertificate entity's name for search
     * @return List of GiftCertificate entities which was found by part of name.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getAllByPartOfName(String partOfName) {

        SqlParameterSource params = new MapSqlParameterSource().addValue(NAME_IN_PARAM_FOR_PART_OF_NAME, partOfName);
        Map<String, Object> procedureResult = executeProcedure(GET_BY_NAME_PART_PROCEDURE_NAME, params);
        return (List<GiftCertificate>) procedureResult.get(RESULTS_SET_KEY);

    }


    /**
     * This method get all GiftCertificate entities sorted. Generates query by concatenation of sortingFieldNames
     * and sortingOrder params.
     *
     * @param sortingFieldNames fields of GiftCertificate entity for sorting
     * @param sortingOrder      order of sorting(ASC/DESC)
     * @return List of all sorted GiftCertificate entities.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getAllSorted(String[] sortingFieldNames, String sortingOrder) {
        String fieldNames = String.join(",", sortingFieldNames);

        String query = new StringJoiner(" ")
                .add(SORTING_QUERY_FIRST_PART)
                .add(fieldNames)
                .add(sortingOrder).toString();

        return template.query(query, rowMapper);
    }

    /**
     * This method get all GiftCertificate entities which is bound with a specific tag.
     *
     * @param tagName name of Tag entity
     * @return List of all GiftCertificate entities which is bound with tag that has name like tagName param.
     * @since 1.0
     */
    @Override
    public List<GiftCertificate> getAllByTagName(String tagName) {
        return template.query(GET_ALL_BY_TAG_NAME, rowMapper, tagName);
    }

    /**
     * This method updates all updatable fields for GiftCertificate entity.
     *
     * @param certificate GiftCertificate entity with fields for update.
     * @param id          GiftCertificate entity id.
     * @since 1.0
     */
    @Override
    public void update(GiftCertificate certificate, long id) {
        template.update(UPDATE_QUERY, certificate.getName(), certificate.getDescription(),
                certificate.getPrice(), certificate.getDuration(), certificate.getLastUpdateDate(), id);
    }

    /**
     * This method delete GiftCertificate entity.
     *
     * @param id GiftCertificate entity id.
     * @return a boolean which shows if in db was changed any row or not
     * @since 1.0
     */
    @Override
    public boolean delete(long id) {
        int updatedRows = template.update(DELETE_QUERY, id);
        return updatedRows >= 1;

    }

    /**
     * This method is execute a procedure in db.
     *
     * @param procedureName name of the executing procedure.
     * @param params        parameters for the procedure.
     * @return result of the procedure.
     */
    private Map<String, Object> executeProcedure(String procedureName, SqlParameterSource params) {

        SimpleJdbcCall simpleJdbcCall = new SimpleJdbcCall(template)
                .withProcedureName(procedureName)
                .returningResultSet(RESULTS_SET_KEY, rowMapper);

        return simpleJdbcCall.execute(params);
    }

}
