package com.aochensoft.democommon.auth;

import cn.hutool.json.JSONUtil;
import com.aochensoft.democommon.constant.RedisPrefixEnum;
import com.aochensoft.democommon.exception.AochenGlobalException;
import com.aochensoft.democommon.service.cache.RedisService;
import com.aochensoft.democommon.vo.auth.LoginUser;
import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.*;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.Collection;

/**
 * JWT TOKEN 提供者
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-24 10:52:51
 */
@Getter
@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final RedisService redisService;

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
        LoginUser userPrincipal = (LoginUser) authentication.getPrincipal();

        LocalDateTime now = LocalDateTime.now();
        LocalDateTime expiryDate = now.plusNanos(jwtExpirationMs);

        Algorithm algorithm = Algorithm.HMAC512(jwtSecret);
        String jwt = JWT.create().withIssuer("AhogeK").withSubject(userPrincipal.getId().toString())
                .withExpiresAt(expiryDate.atZone(ZoneId.systemDefault()).toInstant()).sign(algorithm);
        // 将 jwt 及用户信息存入 redis (带 redis prefix)
        redisService.set(RedisPrefixEnum.JWT_TOKEN.getPrefix() + userPrincipal.getId(), JSONUtil.toJsonStr(userPrincipal),
                jwtExpirationMs);
        // 将用户权限存入 redis
        redisService.set(RedisPrefixEnum.USER_AUTHORITY.getPrefix() + userPrincipal.getId(),
                JSONUtil.toJsonStr(authentication.getAuthorities()), jwtExpirationMs);
        return jwt;
    }

    /**
     * 通过 token 获取用户信息
     *
     * @param token JWT TOKEN
     * @return 用户信息
     */
    public LoginUser getLoginUserFromJWT(String token) {
        Long userId = getUserIdFromJWT(token);
        return redisService.get(RedisPrefixEnum.JWT_TOKEN.getPrefix() + userId, LoginUser.class);
    }

    /**
     * 通过 token 获取用户权限
     *
     * @param token JWT TOKEN
     * @return 用户权限
     */
    public Collection<SimpleGrantedAuthority> getGrantedAuthorities(String token) {
        Long userId = getUserIdFromJWT(token);
        return redisService.getList(RedisPrefixEnum.USER_AUTHORITY.getPrefix() + userId, SimpleGrantedAuthority.class);
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
