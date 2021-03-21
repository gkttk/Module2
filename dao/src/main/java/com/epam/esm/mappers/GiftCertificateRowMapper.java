package com.epam.esm.mappers;

import com.epam.esm.model.GiftCertificate;
import org.springframework.jdbc.core.RowMapper;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

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
        long id = rs.getLong(ID);
        String name = rs.getString(NAME);
        String description = rs.getString(DESCRIPTION);
        BigDecimal price = rs.getBigDecimal(PRICE);
        int duration = rs.getInt(DURATION);
        Date createDate = rs.getDate(CREATE_DATE);
        Date lastUpdateDate = rs.getDate(LAST_UPDATE_DATE);
        return new GiftCertificate(id, name, description, price, duration, createDate, lastUpdateDate);
    }
}
