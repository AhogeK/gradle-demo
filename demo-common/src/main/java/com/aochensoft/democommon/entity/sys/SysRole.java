package com.aochensoft.democommon.entity.sys;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;

import java.time.LocalDateTime;

/**
 * 角色表实体类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-02-14 09:54:51
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE sys_role SET is_deleted = 1 WHERE id = ?")
@Where(clause = "is_deleted = 0")
public class SysRole {

    @Id
    @GenericGenerator(name = "custom_gen", strategy = "com.aochensoft.democommon.config.CustomIdGenerator")
    @GeneratedValue(generator = "custom_gen")
    private Long id;

    /**
     * 角色名称
     */
    private String name;

    /**
     * 角色编码
     */
    private String code;

    /**
     * 角色描述
     */
    private String description;

    /**
     * 创建时间
     */
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Byte isDeleted;
}
