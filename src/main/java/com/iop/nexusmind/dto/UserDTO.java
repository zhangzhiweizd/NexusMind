package com.iop.nexusmind.dto;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

/**
 * 用户信息DTO
 */
@Data
public class UserDTO {
    /** 用户ID */
    private Long id;
    
    /** 用户名 */
    private String username;
    
    /** 邮箱 */
    private String email;
    
    /** 昵称 */
    private String nickname;
    
    /** 手机号 */
    private String phone;
    
    /** 头像URL */
    private String avatar;
    
    /** 角色列表 */
    private List<String> roles;
    
    /** 创建时间 */
    private LocalDateTime createdAt;
    
    /** 更新时间 */
    private LocalDateTime updatedAt;
}
