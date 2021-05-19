package com.epam.esm.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import java.time.LocalDate;
import java.util.Date;

@Data
@NoArgsConstructor
@Entity
@Table(name = "refresh_token")
public class RefreshToken {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "access_token", nullable = false)
    private String accessToken;


    @Column(name = "refresh_token", nullable = false)
    private String refreshToken;

    @CreationTimestamp
    @Column(name = "expired_time", nullable = false)
    private Date expiredTime;


    public RefreshToken(String accessToken, String refreshToken, Date expiredTime) {
        this.accessToken = accessToken;
        this.refreshToken = refreshToken;
        this.expiredTime = expiredTime;
    }
}
