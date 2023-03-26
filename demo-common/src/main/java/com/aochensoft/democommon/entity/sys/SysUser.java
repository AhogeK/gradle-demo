package com.aochensoft.democommon.entity.sys;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.SQLDelete;
import org.hibernate.annotations.Where;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

/**
 * 系统用户实体类
 *
 * @author AhogeK ahogek@gmail.com
 * @since 2023-01-20 11:41:34
 */
@Getter
@Setter
@ToString
@RequiredArgsConstructor
@Entity
@AllArgsConstructor
@SQLDelete(sql = "UPDATE sys_user SET is_deleted = 1 WHERE id = ?")
@Where(clause = "is_deleted = 0")
public class SysUser implements UserDetails {
    @Id
    @GenericGenerator(name = "custom_gen", strategy = "com.aochensoft.democommon.config.CustomIdGenerator")
    @GeneratedValue(generator = "custom_gen")
    private Long id;

    /**
     * 用户名称
     */
    private String username;

    /**
     * 密码
     */
    private String password;

    /**
     * 头像
     */
    private String avatar;

    /**
     * 电子邮箱
     */
    private String email;

    /**
     * 手机号码
     */
    private String mobileNum;

    /**
     * 性别
     */
    private Byte gender;

    /**
     * 最后登录时间
     */
    private LocalDateTime lastLoginTime;

    /**
     * 创建时间
     */
    @Column(insertable = false, updatable = false)
    private LocalDateTime createTime;

    /**
     * 更新时间
     */
    @Column(insertable = false, updatable = false)
    private LocalDateTime updateTime;

    /**
     * 是否删除
     */
    private Byte isDeleted = 0;

    /**
     * 角色
     */
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "sys_user_role",
            joinColumns = {@JoinColumn(name = "user_id")},
            inverseJoinColumns = {@JoinColumn(name = "role_id")}
    )
    private List<SysRole> roles;

    /**
     * 获取权限
     *
     * @return 权限
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        // 将每个角色转换为SimpleGrantedAuthority对象，并添加到集合中
        List<GrantedAuthority> authorities = new ArrayList<>();
        for (SysRole role : roles) {
            authorities.add(new SimpleGrantedAuthority(role.getCode()));
        }
        return authorities;
    }

    /**
     * 账户是否未过期
     *
     * @return true
     */
    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    /**
     * 账户是否未锁定
     *
     * @return true
     */
    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    /**
     * 密码是否未过期
     *
     * @return true
     */
    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    /**
     * 是否可用
     *
     * @return true
     */
    @Override
    public boolean isEnabled() {
        return true;
    }
}
