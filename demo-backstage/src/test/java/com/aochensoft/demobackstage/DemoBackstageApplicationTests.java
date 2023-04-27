package com.aochensoft.demobackstage;

import cn.hutool.core.util.IdUtil;
import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.repository.sys.SysUserRepository;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class DemoBackstageApplicationTests {

    @Autowired
    private SysUserRepository sysUserRepository;

    private static Long insertId;

    @Test
    @Order(0)
    void saveSysUserTest() {
        SysUser insert = new SysUser();
        insert.setUsername("AhogeK");
        insert.setPassword(IdUtil.fastSimpleUUID());
        Assertions.assertDoesNotThrow(() -> {
            sysUserRepository.save(insert);
        });
        insertId = insert.getId();
    }

    @Test
    @Order(1)
    void softDelUserTest() {
        log.info("Check insert ID: {}", insertId);
        Assertions.assertNotNull(insertId);
        sysUserRepository.deleteById(insertId);
        Optional<SysUser> entity = sysUserRepository.findById(insertId);
        Assertions.assertFalse(entity.isPresent());
    }
}
