package com.aochensoft.demobackstage.democommon.service.cache;

import com.aochensoft.democommon.entity.sys.SysUser;
import com.aochensoft.democommon.service.cache.RedisService;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.concurrent.TimeUnit;

/**
 * @author AhogeK ahogek@gmail.com
 * @since 2023-04-27 14:09:32
 */
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
class RedisServiceTest {

    @Autowired
    private RedisService redisService;

    @Test
    @Order(0)
    void setRedis() {
        Assertions.assertDoesNotThrow(() -> {
            SysUser sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setUsername("测试redis set");
            redisService.set("test", sysUser);
        });
    }

    @Test
    @Order(1)
    void getRedis() {
        SysUser sysUser = redisService.get("test", SysUser.class);
        Assertions.assertNotNull(sysUser);
        Assertions.assertEquals("测试redis set", sysUser.getUsername());
    }

    @Test
    @Order(2)
    void delRedis() {
        redisService.del("test");
        Assertions.assertNull(redisService.get("test", SysUser.class));
    }

    @Test
    @Order(3)
    void setRedisWithExpire() throws InterruptedException {
        Assertions.assertDoesNotThrow(() -> {
            SysUser sysUser = new SysUser();
            sysUser.setId(1L);
            sysUser.setUsername("测试redis set");
            redisService.set("test", sysUser, 100);
        });

        Awaitility.await().atMost(200, TimeUnit.MILLISECONDS)
                .until(() -> redisService.get("test", SysUser.class) == null);
        Assertions.assertNull(redisService.get("test", SysUser.class));
    }
}
