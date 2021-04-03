package com.epam.esm.mappers;

import com.epam.esm.entity.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * This class maps ResultSet from db to GiftCertificate entity.
 */
@Component
public class GiftCertificateRowMapper implements RowMapper<GiftCertificate> {

    private final static String ID = "id";
    private final static String NAME = "name";
    private final static String DESCRIPTION = "description";
    private final static String PRICE = "price";
    private final static String DURATION = "duration";
    private final static String CREATE_DATE = "create_date";
    private final static String LAST_UPDATE_DATE = "last_update_date";

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(ID));
        giftCertificate.setName(rs.getString(NAME));
        giftCertificate.setDescription(rs.getString(DESCRIPTION));
        giftCertificate.setPrice(rs.getBigDecimal(PRICE));
        giftCertificate.setDuration(rs.getInt(DURATION));
        giftCertificate.setCreateDate(rs.getString(CREATE_DATE));
        giftCertificate.setLastUpdateDate(rs.getString(LAST_UPDATE_DATE));
        return giftCertificate;
    }
}
