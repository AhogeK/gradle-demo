package com.aochensoft.demobackstage;

import cn.hutool.core.util.IdUtil;
import com.aochensoft.democommon.dao.sys.SysUserDAO;
import com.aochensoft.democommon.entity.sys.SysUser;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Optional;

@Slf4j
@SpringBootTest
class DemoBackstageApplicationTests {

    @Autowired
    private SysUserDAO sysUserDAO;

    private static Long insertId;

    @Test
    @Order(0)
    void saveSysUserTest() {
        SysUser insert = new SysUser();
        insert.setUsername("AhogeK");
        insert.setPassword(IdUtil.fastSimpleUUID());
        Assertions.assertDoesNotThrow(() -> {
            sysUserDAO.save(insert);
        });
        insertId = insert.getId();
    }

    @Test
    @Order(1)
    void softDelUserTest() {
        log.info("Check insert ID: {}", insertId);
        Assertions.assertNotNull(insertId);
        sysUserDAO.deleteById(insertId);
        Optional<SysUser> entity = sysUserDAO.findById(insertId);
        Assertions.assertFalse(entity.isPresent());
    }
}
