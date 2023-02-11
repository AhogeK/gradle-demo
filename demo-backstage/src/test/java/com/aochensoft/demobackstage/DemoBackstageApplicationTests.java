package com.aochensoft.demobackstage;

import cn.hutool.core.util.IdUtil;
import com.aochensoft.democommon.dao.sys.SysUserDAO;
import com.aochensoft.democommon.entity.sys.SysUser;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class DemoBackstageApplicationTests {

    @Autowired
    private SysUserDAO sysUserDAO;

    @Test
    void saveSysUserTest() {
        SysUser insert = new SysUser();
        insert.setUsername("AhogeK");
        insert.setPassword(IdUtil.fastSimpleUUID());
        Assertions.assertDoesNotThrow(() -> {
            sysUserDAO.save(insert);
        });
    }

}
