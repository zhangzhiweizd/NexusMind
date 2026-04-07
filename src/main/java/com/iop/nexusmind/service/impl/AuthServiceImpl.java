package com.iop.nexusmind.service.impl;

import com.iop.nexusmind.dto.AuthResponse;
import com.iop.nexusmind.dto.LoginRequest;
import com.iop.nexusmind.dto.RegisterRequest;
import com.iop.nexusmind.dto.UserDTO;
import com.iop.nexusmind.entity.Role;
import com.iop.nexusmind.entity.User;
import com.iop.nexusmind.repository.RoleRepository;
import com.iop.nexusmind.repository.UserRepository;
import com.iop.nexusmind.security.JwtUtil;
import com.iop.nexusmind.service.AuthService;
import org.modelmapper.ModelMapper;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final ModelMapper modelMapper;
    private final UserDetailsServiceImpl userDetailsService;

    /**
     * 构造函数，注入依赖
     */
    public AuthServiceImpl(AuthenticationManager authenticationManager,
                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          JwtUtil jwtUtil,
                          PasswordEncoder passwordEncoder,
                          ModelMapper modelMapper,
                          UserDetailsServiceImpl userDetailsService) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.modelMapper = modelMapper;
        this.userDetailsService = userDetailsService;
    }

    /**
     * 用户登录
     * 验证用户名和密码，生成JWT令牌
     */
    @Override
    public AuthResponse login(LoginRequest request) {
        // 认证
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getUsername(),
                        request.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // 生成 Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(request.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        // 获取用户信息
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow();
        UserDTO userDTO = convertToDTO(user);

        return new AuthResponse(token, refreshToken, userDTO);
    }

    /**
     * 用户注册
     * 创建新用户并分配默认角色，生成JWT令牌
     */
    @Override
    public AuthResponse register(RegisterRequest request) {
        // 检查用户名是否已存在
        if (userRepository.existsByUsername(request.getUsername())) {
            throw new RuntimeException("用户名已存在");
        }

        // 检查邮箱是否已存在
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new RuntimeException("邮箱已被注册");
        }

        // 创建用户
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setEmail(request.getEmail());
        user.setNickname(request.getNickname());
        user.setEnabled(true);

        // 分配默认角色
        Role userRole = roleRepository.findByName("ROLE_USER")
                .orElseGet(() -> {
                    Role newRole = new Role();
                    newRole.setName("ROLE_USER");
                    newRole.setDescription("普通用户");
                    return roleRepository.save(newRole);
                });
        
        user.setRoles(Arrays.asList(userRole));

        User savedUser = userRepository.save(user);

        // 生成 Token
        UserDetails userDetails = userDetailsService.loadUserByUsername(savedUser.getUsername());
        String token = jwtUtil.generateToken(userDetails);
        String refreshToken = jwtUtil.generateRefreshToken(userDetails);

        UserDTO userDTO = convertToDTO(savedUser);

        return new AuthResponse(token, refreshToken, userDTO);
    }

    /**
     * 刷新访问令牌
     * 验证刷新令牌的有效性，生成新的访问令牌和刷新令牌
     */
    @Override
    public AuthResponse refreshToken(String refreshToken) {
        if (!jwtUtil.validateToken(refreshToken)) {
            throw new RuntimeException("无效的刷新令牌");
        }

        String username = jwtUtil.extractUsername(refreshToken);
        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        String newToken = jwtUtil.generateToken(userDetails);
        String newRefreshToken = jwtUtil.generateRefreshToken(userDetails);

        User user = userRepository.findByUsername(username).orElseThrow();
        UserDTO userDTO = convertToDTO(user);

        return new AuthResponse(newToken, newRefreshToken, userDTO);
    }

    /**
     * 获取当前登录用户的信息
     * 从SecurityContext中获取当前用户名，查询用户信息并转换为DTO
     */
    @Override
    public UserDTO getCurrentUser() {
        String username = SecurityContextHolder.getContext().getAuthentication().getName();
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("用户不存在"));
        return convertToDTO(user);
    }

    /**
     * 将User实体转换为UserDTO
     * @param user 用户实体
     * @return 用户DTO对象
     */
    private UserDTO convertToDTO(User user) {
        UserDTO dto = modelMapper.map(user, UserDTO.class);
        List<String> roleNames = user.getRoles().stream()
                .map(Role::getName)
                .collect(Collectors.toList());
        dto.setRoles(roleNames);
        return dto;
    }
}
