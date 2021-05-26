package com.epam.esm.security.blacklist;

import com.epam.esm.constants.ApplicationConstants;
import com.epam.esm.dao.security.RefreshTokenDao;
import com.epam.esm.entity.RefreshToken;
import io.jsonwebtoken.Jwts;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Component
public class JwtTokenBlackListImpl implements JwtTokenBlackList {
    private final Map<String, String> blackList;
    private final RefreshTokenDao refreshTokenDao;

    @Autowired
    public JwtTokenBlackListImpl(RefreshTokenDao refreshTokenDao) {
        this.refreshTokenDao = refreshTokenDao;
        this.blackList = new ConcurrentHashMap<>();
    }


    public boolean containsId(String jwtId) {
        return blackList.containsKey(jwtId);
    }

    public void add(String accessToken) {
        RefreshToken token = refreshTokenDao.findByAccessToken(accessToken);
        if (token != null) {
            refreshTokenDao.deleteById(token.getId());
            String jwtId = getTokenId(accessToken, ApplicationConstants.ACCESS_TOKEN_SECRET);
            this.blackList.put(jwtId, accessToken);
            SecurityContextHolder.clearContext();
        }
    }

    public void remove(String jwtId) {
        blackList.remove(jwtId);
    }


    private String getTokenId(String token, String currentSecret) {
        return Jwts.parser().setSigningKey(currentSecret).parseClaimsJws(token).getBody().getId();
    }

}
