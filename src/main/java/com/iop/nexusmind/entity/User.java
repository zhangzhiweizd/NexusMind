package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * 用户实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "\"user\"")
@Comment("用户表")
@EntityListeners(AuditingEntityListener.class)
public class User extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    @Comment("用户名")
    private String username;

    @Column(nullable = false)
    @Comment("密码")
    private String password;

    @Column(unique = true, nullable = false, length = 100)
    @Comment("邮箱")
    private String email;

    @Column(length = 50)
    @Comment("昵称")
    private String nickname;

    @Column(length = 20)
    @Comment("手机号")
    private String phone;

    @Column(length = 500)
    @Comment("头像URL")
    private String avatar;

    @Comment("是否启用")
    private Boolean enabled = true;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_role",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private List<Role> roles = new ArrayList<>();

    @OneToMany(mappedBy = "creator", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Workspace> workspaces = new ArrayList<>();

    @CreatedDate
    @Column(updatable = false)
    private LocalDateTime createdAt;

    @LastModifiedDate
    private LocalDateTime updatedAt;
}
