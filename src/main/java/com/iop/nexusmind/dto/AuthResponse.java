package com.iop.nexusmind.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

/**
 * 认证响应DTO
 * 包含JWT令牌、刷新令牌和用户信息
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
public class AuthResponse {
    
    /** JWT访问令牌 */
    private String token;
    
    /** JWT刷新令牌 */
    private String refreshToken;
    
    /** 用户信息 */
    private UserDTO user;
}
