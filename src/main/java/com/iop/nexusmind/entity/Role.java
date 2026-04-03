package com.iop.nexusmind.entity;

import jakarta.persistence.*;
import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 角色实体类
 */
@Data
@EqualsAndHashCode(callSuper = true)
@Entity
@Table(name = "role")
public class Role extends BaseEntity {

    @Column(unique = true, nullable = false, length = 50)
    private String name; // ROLE_ADMIN, ROLE_USER

    @Column(length = 200)
    private String description;
}
