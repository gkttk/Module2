package com.epam.esm.security.blacklist;

public interface JwtTokenBlackList {

    boolean containsId(String jwtId);

    void add(String accessToken);

    void remove(String jwtId);

}
