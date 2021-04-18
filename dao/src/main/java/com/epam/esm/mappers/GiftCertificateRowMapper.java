package com.epam.esm.mappers;

import com.epam.esm.constants.ApplicationConstants;
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

    @Override
    public GiftCertificate mapRow(ResultSet rs, int rowNum) throws SQLException {
        GiftCertificate giftCertificate = new GiftCertificate();
        giftCertificate.setId(rs.getLong(ApplicationConstants.ID_RS_KEY));
        giftCertificate.setName(rs.getString(ApplicationConstants.NAME_GC_RS_KEY));
        giftCertificate.setDescription(rs.getString(ApplicationConstants.DESCRIPTION_GC_RS_KEY));
        giftCertificate.setPrice(rs.getBigDecimal(ApplicationConstants.PRICE_GC_RS_KEY));
        giftCertificate.setDuration(rs.getInt(ApplicationConstants.DURATION_GC_RS_KEY));
      //  giftCertificate.setCreateDate(rs.getString(ApplicationConstants.CREATE_DATE_GC_RS_KEY));
      // giftCertificate.setLastUpdateDate(rs.getString(ApplicationConstants.LAST_UPDATE_DATE_GC_RS_KEY));

        return giftCertificate;
    }
}
