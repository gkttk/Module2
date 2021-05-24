package com.epam.esm.dao.domain.impl;

import com.epam.esm.querybuilder.QueryBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Repository;

@Repository("giftCertificateCriteriaFindAllDao")
public class GiftCertificateCriteriaFindAllDao<GiftCertificate> extends AbstractCriteriaFindAllDao<GiftCertificate> {

    @Autowired
    protected GiftCertificateCriteriaFindAllDao(@Qualifier("giftCertificateQueryBuilder") QueryBuilder<GiftCertificate> queryBuilder) {
        super(queryBuilder);
    }
}
