package com.aochensoft.demobackstage.auth;

import cn.hutool.crypto.SecureUtil;
import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
import cn.hutool.crypto.symmetric.AES;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

/**
 * @author AhogeK ahogek@gmail.com
 * @since 2023-03-26 15:14:40
 */
@Slf4j
class GenerateTestPasswordsTest {

    @Test
    void generatePassword() {
        String password = "111111";
        Digester digester = new Digester(DigestAlgorithm.SHA256);
        byte[] sha256Byte = digester.digest("ahogek:gradle-demo");
        AES aes = SecureUtil.aes(sha256Byte);
        String encryptPassword = aes.encryptHex(sha256Byte);
        Assertions.assertNotNull(encryptPassword);
        log.info("encryptPassword: {}", encryptPassword);
    }
}
