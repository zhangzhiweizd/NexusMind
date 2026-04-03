package com.iop.nexusmind.service;

import com.iop.nexusmind.dto.AuthResponse;
import com.iop.nexusmind.dto.LoginRequest;
import com.iop.nexusmind.dto.RegisterRequest;
import com.iop.nexusmind.dto.UserDTO;

public interface AuthService {
    
    AuthResponse login(LoginRequest request);
    
    AuthResponse register(RegisterRequest request);
    
    AuthResponse refreshToken(String refreshToken);
    
    UserDTO getCurrentUser();
}
