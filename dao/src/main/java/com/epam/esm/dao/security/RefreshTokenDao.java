package com.epam.esm.dao.security;

import com.epam.esm.entity.RefreshToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RefreshTokenDao extends JpaRepository<RefreshToken, Long> {

    RefreshToken findByAccessToken(String accessToken);


}
