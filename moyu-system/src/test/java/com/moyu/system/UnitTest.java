package com.moyu.system;

import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.RandomUtil;
import com.google.common.base.CaseFormat;
import com.moyu.common.security.constant.SecurityConstants;
import com.nimbusds.jose.*;
import com.nimbusds.jose.crypto.MACSigner;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.joda.time.DateTime;
import org.junit.jupiter.api.Test;

/**
 * 本地测试类
 *
 * @author shisong
 * @since 2024-11-25
 */
@Slf4j
public class UnitTest {

    @Test
    public void test() {
        log.info("测试基类正确执行，不要改");
    }

    @Test
    public void testCamelCase() {
        String underscoreName = "Hello_world_DTO";
        String camelCaseName = CaseFormat.LOWER_UNDERSCORE.to(CaseFormat.LOWER_CAMEL, underscoreName);
        log.info(camelCaseName);
    }


    @Test
    public void testJWT() throws JOSEException {
        String token = "";
        DateTime now = DateTime.now();
        // 头部
        JWSHeader header = new JWSHeader.Builder(JWSAlgorithm.HS256).type(JOSEObjectType.JWT).build();
        // 声明
        JWTClaimsSet claimsSet = new JWTClaimsSet.Builder()
                // 主题,即username
                .subject("username")
                // 签发时间
                .issueTime(now.toDate())
                // 过期时间
                .expirationTime(now.plusSeconds(SecurityConstants.Token.TOKEN_VALID_TIME).toDate())
                .jwtID(IdUtil.fastSimpleUUID())
                // 自定义声明
                .claim("claim", true)
                .build();
        // 签名器
        JWSSigner jwsSigner = new MACSigner(SecurityConstants.Token.SECRET);
        // 创建jwt对象
        SignedJWT signedJWT = new SignedJWT(header, claimsSet);
        // 签名，根据header和payload生成签名
        signedJWT.sign(jwsSigner);
        // 生成token字符串
        token = signedJWT.serialize();
        log.info(token);
    }
}
