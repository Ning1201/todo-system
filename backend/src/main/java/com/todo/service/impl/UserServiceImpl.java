package com.todo.service.impl;

import com.todo.entity.User;
import com.todo.dto.LoginRequest;
import com.todo.dto.LoginResponse;
import com.todo.dto.UserDTO;
import com.todo.exception.BusinessException;
import com.todo.mapper.UserMapper;
import com.todo.service.UserService;
import com.todo.security.JwtTokenProvider;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Objects;

/**
 * 用户服务实现
 */
@Slf4j
@Service
public class UserServiceImpl implements UserService {
    
    @Autowired
    private UserMapper userMapper;
    
    @Autowired
    private PasswordEncoder passwordEncoder;
    
    @Autowired
    private JwtTokenProvider jwtTokenProvider;
    
    @Override
    public User findByUsername(String username) {
        return userMapper.findByUsername(username);
    }
    
    @Override
    public User findById(Long id) {
        return userMapper.findById(id);
    }
    
    @Override
    public LoginResponse login(LoginRequest request) {
        log.info("用户登录: {}", request.getUsername());
        
        User user = userMapper.findByUsername(request.getUsername());
        if (Objects.isNull(user)) {
            throw new BusinessException(401, "用户不存在");
        }
        
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            throw new BusinessException(401, "密码错误");
        }
        
        if (!"ACTIVE".equals(user.getStatus())) {
            throw new BusinessException(401, "用户已被禁用");
        }
        
        String token = jwtTokenProvider.generateToken(user.getId(), user.getUsername());
        
        return LoginResponse.builder()
                .token(token)
                .user(convertToDTO(user))
                .build();
    }
    
    @Override
    public User register(String username, String password, String email, String fullName) {
        log.info("用户注册: {}", username);
        
        User existingUser = userMapper.findByUsername(username);
        if (Objects.nonNull(existingUser)) {
            throw new BusinessException(400, "用户名已存在");
        }
        
        User user = User.builder()
                .username(username)
                .password(passwordEncoder.encode(password))
                .email(email)
                .fullName(fullName)
                .role("USER")
                .status("ACTIVE")
                .build();
        
        userMapper.insert(user);
        log.info("用户注册成功: {}", username);
        
        return user;
    }
    
    @Override
    public UserDTO convertToDTO(User user) {
        if (Objects.isNull(user)) {
            return null;
        }
        return UserDTO.builder()
                .id(user.getId())
                .username(user.getUsername())
                .email(user.getEmail())
                .fullName(user.getFullName())
                .role(user.getRole())
                .status(user.getStatus())
                .createdAt(user.getCreatedAt())
                .build();
    }
}
