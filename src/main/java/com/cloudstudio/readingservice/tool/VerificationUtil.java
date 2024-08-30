package com.cloudstudio.readingservice.tool;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import java.util.Date;

/**
 * @ClassName VerificationUtil
 * @Author Create By matrix
 * @Date 2024/8/29 13:48
 * 验证工具类
 */
public class VerificationUtil {

    private static final String SECRET_KEY = "MATRIX_SHUAIXIAOHAI_CODE";

    /**
     * 生成Token
     * @param account
     * @return
     */
    public static String generateToken(String account) {
        return Jwts.builder()
                .setSubject(account)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                //.setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60 * 6)) // 6小时
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 5)) // 5min
                .signWith(SignatureAlgorithm.HS256, SECRET_KEY)
                .compact();
    }

    /**
     * 解析Token
     * @param token
     * @return
     * @throws Exception
     */
    public static Claims parseToken(String token) throws Exception {
        try {
            return Jwts.parser()
                    .setSigningKey(SECRET_KEY)
                    .parseClaimsJws(token)
                    .getBody();
        } catch (io.jsonwebtoken.ExpiredJwtException e) {
            // Token已过期
            throw new RuntimeException("Token已过期", e);
        } catch (io.jsonwebtoken.UnsupportedJwtException e) {
            // Token不受支持
            throw new RuntimeException("Token不受支持", e);
        } catch (io.jsonwebtoken.MalformedJwtException e) {
            // Token格式错误
            throw new RuntimeException("Token格式错误", e);
        } catch (io.jsonwebtoken.SignatureException e) {
            // Token签名不匹配
            throw new RuntimeException("Token签名不匹配", e);
        } catch (IllegalArgumentException e) {
            // Token为空或null
            throw new RuntimeException("Token为空或null", e);
        }
    }
}
