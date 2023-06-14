package com.xian.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import javax.servlet.http.HttpServletRequest;
import java.util.Date;

/**
 * JWT工具类
 */
@Component
public class JwtUtil {

    //有效期为
    public static final Long EXPIRE = 24 * 60 * 60 * 1000L; // 24 * 60 * 60 * 1000  1天
    //设置秘钥明文
    public static final String SECRET_KEY = "ukc8BDbRigUDaY6pZFfWus2jZWLPHO";

    public static String getJwtToken(String id, String nickname) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("edu-user")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("nickname", nickname)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }
    public static String getUserJwtToken(String id, String username) {
        return Jwts.builder()
                .setHeaderParam("typ", "JWT")
                .setHeaderParam("alg", "HS256")
                .setSubject("edu-admin")
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRE))
                .claim("id", id)
                .claim("username", username)
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 判断token是否存在与有效
     *
     * @param jwtToken
     * @return
     */
    public static boolean checkToken(String jwtToken) {
        if (StringUtils.isEmpty(jwtToken)) return false;
        try {
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 判断token是否存在与有效
     *
     * @param request
     * @return
     */
    public static boolean checkToken(HttpServletRequest request) {
        try {
            String jwtToken = request.getHeader("Authentication");
            if (StringUtils.isEmpty(jwtToken)) return false;
            Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 根据token获取会员id
     *
     * @param request
     * @return
     */
    public static String getIdByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authentication");
        if (!StringUtils.hasText(jwtToken)) return "";
        Jws<Claims> claimsJws =
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("id");
    }

    /**
     * 根据token获取用戶用戶名
     *
     * @param request
     * @return
     */
    public static String getUsernameByJwtToken(HttpServletRequest request) {
        String jwtToken = request.getHeader("Authentication");
        if (!StringUtils.hasText(jwtToken)) return "";
        Jws<Claims> claimsJws =
                Jwts.parser().setSigningKey(SECRET_KEY).parseClaimsJws(jwtToken);
        Claims claims = claimsJws.getBody();
        return (String) claims.get("username");
    }
}

