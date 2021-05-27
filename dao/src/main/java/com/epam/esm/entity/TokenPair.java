package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "token_pair")
public class TokenPair {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;


    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;


    @Column(name = "refresh_token_expired_time", nullable = false)
    @Temporal(value = TemporalType.TIMESTAMP)
    private Date refreshTokenExpiredTime;


    public TokenPair(String accessToken, String refreshToken, Date expiredTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.refreshTokenExpiredTime = expiredTime;
    }
}
