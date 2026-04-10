package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.hibernate.annotations.Comment;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
@Comment("角色表")
public class Role extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    @Comment("角色名称")
    private String name; // ROLE_ADMIN, ROLE_USER

    @Column(length = 200)
    @Comment("角色描述")
    private String description;
}
