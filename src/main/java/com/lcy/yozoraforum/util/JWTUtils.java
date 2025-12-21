package com.lcy.yozoraforum.util;

import io.jsonwebtoken.*;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class JWTUtils {
    /**
     * jwt密钥
     */
    private static final String secretKey = "YoZoRaNET.SEN7";

    /**
     * 创建token
     * @param userId
     * @return
     */
    public static String createToken(int userId){
        Map<String,Object> claims = new HashMap<>();
        claims.put("userId",userId);
        JwtBuilder jwtBuilder = Jwts.builder()
                .signWith(SignatureAlgorithm.HS256,secretKey) //签发算法，设置密钥
                .setClaims(claims) //载荷的数据
                .setIssuedAt(new Date()) //jwt生成时间
                .setExpiration(new Date(System.currentTimeMillis() + 60 * 1000 * 60)); //jwt过期时间

        String token = jwtBuilder.compact();//生成最终的token
        return token;

    }

    /**
     * 解析token
     * @param token
     * @return
     */
    public static Claims checkToken(String token){
           Jws<Claims> jws = Jwts.parser()
                    .setSigningKey(secretKey)
                    .parseClaimsJws(token);
            return jws.getBody();
    }

}
