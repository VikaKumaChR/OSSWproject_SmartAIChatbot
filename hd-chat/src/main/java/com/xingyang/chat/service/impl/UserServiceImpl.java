package com.xingyang.chat.service.impl;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.xingyang.chat.exception.BusinessException;
import com.xingyang.chat.mapper.UserMapper;
import com.xingyang.chat.model.dto.LoginDTO;
import com.xingyang.chat.model.dto.RegisterDTO;
import com.xingyang.chat.model.dto.UserUpdateDTO;
import com.xingyang.chat.model.entity.User;
import com.xingyang.chat.model.vo.Result;
import com.xingyang.chat.model.vo.UserVO;
import com.xingyang.chat.service.UserService;
import com.xingyang.chat.util.JwtTokenUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.context.annotation.Lazy;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.jdbc.core.JdbcTemplate;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

/**
 * User Service Implementation
 *
 * @author XingYang
 */
@Slf4j
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    private final AuthenticationManager authenticationManager;
    private final JwtTokenUtil jwtTokenUtil;
    private final PasswordEncoder passwordEncoder;
    private final JdbcTemplate jdbcTemplate;

    public UserServiceImpl(@Lazy AuthenticationManager authenticationManager, JwtTokenUtil jwtTokenUtil, PasswordEncoder passwordEncoder, JdbcTemplate jdbcTemplate) {
        this.authenticationManager = authenticationManager;
        this.jwtTokenUtil = jwtTokenUtil;
        this.passwordEncoder = passwordEncoder;
        this.jdbcTemplate = jdbcTemplate;
    }

    @Override
    public User getUserByUsername(String username) {
        User user = lambdaQuery()
                .eq(User::getUsername, username)
                .eq(User::getDeleted, 0)
                .one();
        
        // 如果找到用户，加载角色信息
        if (user != null) {
            // 获取用户角色
            String[] roles = baseMapper.getUserRoles(user.getId());
            user.setRoles(roles != null && roles.length > 0 ? roles : new String[]{"USER"});
        }
        
        return user;
    }

    @Override
    public Map<String, Object> login(LoginDTO loginDTO) {
        // Authenticate user
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginDTO.getUsername(), loginDTO.getPassword())
        );
        
        // Generate JWT token
        String token = jwtTokenUtil.generateToken(authentication);
        
        // Get user information
        User user = getUserByUsername(loginDTO.getUsername());
        
        // Update last login time
        user.setLastLoginTime(new Date());
        updateById(user);
        
        // Convert to UserVO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        // Prepare response
        Map<String, Object> result = new HashMap<>(2);
        result.put("token", token);
        result.put("user", userVO);
        
        return result;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO register(RegisterDTO registerDTO) {
        // Check if username already exists
        User existingUser = getUserByUsername(registerDTO.getUsername());
        if (existingUser != null) {
            throw new BusinessException(Result.ResultCode.PARAM_ERROR.getCode(), "Username already exists");
        }
        
        // Check if email already exists
        User existingEmail = lambdaQuery()
                .eq(User::getEmail, registerDTO.getEmail())
                .eq(User::getDeleted, 0)
                .one();
        if (existingEmail != null) {
            throw new BusinessException(Result.ResultCode.PARAM_ERROR.getCode(), "Email already registered");
        }
        
        // Check if passwords match
        if (!Objects.equals(registerDTO.getPassword(), registerDTO.getConfirmPassword())) {
            throw new BusinessException(Result.ResultCode.PARAM_ERROR.getCode(), "Passwords do not match");
        }
        
        // Create new user
        User user = new User();
        user.setUsername(registerDTO.getUsername());
        user.setPassword(passwordEncoder.encode(registerDTO.getPassword()));
        user.setNickname(registerDTO.getNickname() != null ? registerDTO.getNickname() : registerDTO.getUsername());
        user.setEmail(registerDTO.getEmail());
        user.setPhone(registerDTO.getPhone());
        user.setStatus(1); // Enabled by default
        
        // Save user
        save(user);
        
        try {
            // 为新用户分配默认USER角色
            jdbcTemplate.update(
                "INSERT INTO user_role (user_id, role_id, create_time, update_time) " +
                "VALUES (?, (SELECT id FROM role WHERE code = 'USER' AND deleted = 0 LIMIT 1), NOW(), NOW())",
                user.getId());
        } catch (Exception e) {
            log.error("Failed to assign default role to user: {}", e.getMessage());
        }
        
        // 设置返回值中的角色信息
        user.setRoles(new String[]{"USER"});
        
        // Convert to UserVO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }

    @Override
    public UserVO getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication == null || !authentication.isAuthenticated()) {
            throw new BusinessException(Result.ResultCode.UNAUTHORIZED);
        }
        
        String username = authentication.getName();
        User user = getUserByUsername(username);
        
        if (user == null) {
            throw new BusinessException(Result.ResultCode.NOT_FOUND.getCode(), "User not found");
        }
        
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public UserVO updateUserInfo(UserUpdateDTO userUpdateDTO) {
        // Get current user if no ID is provided
        User user;
        if (userUpdateDTO.getId() == null) {
            UserVO currentUser = getCurrentUser();
            user = getById(currentUser.getId());
            
            if (user == null) {
                throw new BusinessException(Result.ResultCode.NOT_FOUND.getCode(), "User not found");
            }
        } else {
            // Check if user exists
            user = getById(userUpdateDTO.getId());
            if (user == null) {
                throw new BusinessException(Result.ResultCode.NOT_FOUND.getCode(), "User not found");
            }
        }
        
        // Update user info
        if (userUpdateDTO.getNickname() != null) {
            user.setNickname(userUpdateDTO.getNickname());
        }
        if (userUpdateDTO.getAvatar() != null) {
            user.setAvatar(userUpdateDTO.getAvatar());
        }
        if (userUpdateDTO.getEmail() != null) {
            user.setEmail(userUpdateDTO.getEmail());
        }
        if (userUpdateDTO.getPhone() != null) {
            user.setPhone(userUpdateDTO.getPhone());
        }
        
        // Save updated user
        updateById(user);
        
        // Convert to UserVO
        UserVO userVO = new UserVO();
        BeanUtils.copyProperties(user, userVO);
        
        return userVO;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public boolean changePassword(String oldPassword, String newPassword) {
        // Get current user
        UserVO currentUser = getCurrentUser();
        User user = getById(currentUser.getId());
        
        // Check old password
        if (!passwordEncoder.matches(oldPassword, user.getPassword())) {
            throw new BusinessException(Result.ResultCode.PARAM_ERROR.getCode(), "Old password is incorrect");
        }
        
        // Update password
        user.setPassword(passwordEncoder.encode(newPassword));
        return updateById(user);
    }

    @Override
    public Page<UserVO> pageQuery(Page<User> page, User user) {
        Page<User> userPage = lambdaQuery()
                .eq(user.getStatus() != null, User::getStatus, user.getStatus())
                .like(user.getUsername() != null, User::getUsername, user.getUsername())
                .like(user.getNickname() != null, User::getNickname, user.getNickname())
                .like(user.getEmail() != null, User::getEmail, user.getEmail())
                .eq(User::getDeleted, 0)
                .orderByDesc(User::getCreateTime)
                .page(page);
        
        // Convert to UserVO page
        Page<UserVO> userVOPage = new Page<>(userPage.getCurrent(), userPage.getSize(), userPage.getTotal());
        userVOPage.setRecords(userPage.getRecords().stream().map(u -> {
            UserVO userVO = new UserVO();
            BeanUtils.copyProperties(u, userVO);
            return userVO;
        }).toList());
        
        return userVOPage;
    }
} 