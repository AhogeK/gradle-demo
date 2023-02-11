package com.aochensoft.democommon.entity.sys;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 系统用户实体类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 11:41:34
 */
@Getter
@Setter
@Entity
@SQLDelete(sql = "UPDATE sys_user SET is_deleted = 1 WHERE id = ?")
@Where(clause = "is_deleted = 0")
public class SysUser {
    @Id
    @GenericGenerator(name = "custom_gen", strategy = "com.aochensoft.democommon.config.CustomIdGenerator")
    @GeneratedValue(generator = "custom_gen")
    private Long id;

    private String username;

    private String password;

    private String avatar;

    private String email;

    private String mobileNum;

    private Byte gender;

    private LocalDateTime lastLoginTime;

    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;

    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;

    private Byte isDeleted = 0;
}
