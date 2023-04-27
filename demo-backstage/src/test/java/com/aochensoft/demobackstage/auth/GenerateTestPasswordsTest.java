package com.aochensoft.demobackstage.auth;

import cn.hutool.crypto.digest.DigestAlgorithm;
import cn.hutool.crypto.digest.Digester;
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
        String password = "aochensoft";
        Digester sha256 = new Digester(DigestAlgorithm.SHA256);
        String encryptPassword = sha256.digestHex(password);
        Assertions.assertNotNull(encryptPassword);
        log.info("encryptPassword: {}", encryptPassword);
    }
}
