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
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@SQLDelete(sql = "UPDATE sys_user SET is_deleted = 1 WHERE id = ?")
@Where(clause = "is_deleted = 0")
public class SysUser implements UserDetails {
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

    @Transient
    @Enumerated(EnumType.STRING)
    private SysRole role = SysRole.USER;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return List.of(new SimpleGrantedAuthority(role.name()));
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
