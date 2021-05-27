package com.epam.esm.dao.security;

import com.epam.esm.entity.TokenPair;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface TokenPairDao extends JpaRepository<TokenPair, Long> {

    TokenPair findByAccessToken(String accessToken);

    boolean existsByAccessToken(String accessToken);

    void deleteAllByAccessToken(String accessToken);

    @Transactional
    void removeAllByRefreshTokenExpiredTimeLessThan(Date date);


}
