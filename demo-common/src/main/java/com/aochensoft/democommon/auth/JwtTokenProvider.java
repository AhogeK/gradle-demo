package com.aochensoft.democommon.auth;

import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;

/**
 * JWT TOKEN 提供者
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 10:52:51
 */
@Component
public class JwtTokenProvider {
    @Value("${app.jwtSecret}")
    private String jwtSecret;

    @Value("${app.jwtExpirationMs}")
    private int jwtExpirationMs;

    /**
     * 生成 JWT TOKEN
     *
     * @param authentication 认证
     * @return JWT TOKEN
     */
    public String generateToken(Authentication authentication) {
        SysUser userPrincipal = (SysUser) authentication.getPrincipal();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusNanos(jwtExpirationMs);

        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        return JWT.create().withIssuer("AhogeK").withSubject(userPrincipal.getId().toString())
                .withExpiresAt(expiryDate.atZone(ZoneId.systemDefault()).toInstant()).sign(algorithm);
    }

    /**
     * 从 JWT TOKEN 中获取用户 ID
     *
     * @param token JWT TOKEN
     * @return 用户 ID
     */
    public Long getUserIdFromJWT(String token) {
        return Long.parseLong(JWT.decode(token).getSubject());
    }

    public boolean validateToken(String token) {
        try {
            Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
            JWT.require(algorithm).build().verify(token);
            return true;
        } catch (AlgorithmMismatchException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证算法不匹配");
        } catch (SignatureVerificationException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证签名不匹配");
        } catch (TokenExpiredException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证已过期");
        } catch (MissingClaimException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证缺少必要的声明");
        } catch (IncorrectClaimException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证声明不正确");
        } catch (InvalidClaimException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证声明无效");
        } catch (JWTDecodeException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证解码失败");
        } catch (JWTVerificationException e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证验证失败");
        } catch (Exception e) {
            throw new AochenGlobalException(HttpStatus.UNAUTHORIZED, "认证失败");
        }
    }
}
