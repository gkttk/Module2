package com.epam.esm.dao.security;

import com.epam.esm.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByAccessToken(String accessToken);

    @Transactional
    void removeAllByExpiredTimeLessThan(Date date);


}
