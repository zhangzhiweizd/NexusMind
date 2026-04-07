package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.AuthResponse;
import com.iop.nexusmind.dto.LoginRequest;
import com.iop.nexusmind.dto.RegisterRequest;
import com.iop.nexusmind.dto.UserDTO;

public interface AuthService {
    
    /**
     * 用户登录
     * @param request 登录请求，包含用户名和密码
     * @return 认证响应，包含访问令牌、刷新令牌和用户信息
     */
    AuthResponse login(LoginRequest request);
    
    /**
     * 用户注册
     * @param request 注册请求，包含用户名、密码、邮箱等信息
     * @return 认证响应，包含访问令牌、刷新令牌和用户信息
     */
    AuthResponse register(RegisterRequest request);
    
    /**
     * 刷新访问令牌
     * @param refreshToken 刷新令牌
     * @return 新的认证响应，包含新的访问令牌和刷新令牌
     */
    AuthResponse refreshToken(String refreshToken);
    
    /**
     * 获取当前登录用户信息
     * @return 当前用户的DTO对象
     */
    UserDTO getCurrentUser();
}
