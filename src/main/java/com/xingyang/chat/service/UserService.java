package com.xingyang.chat.service;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.xingyang.chat.model.dto.LoginDTO;
import com.xingyang.chat.model.dto.RegisterDTO;
import com.xingyang.chat.model.dto.UserUpdateDTO;
import com.xingyang.chat.model.entity.User;
import com.xingyang.chat.model.vo.UserVO;

import java.util.Map;

/**
 * User Service Interface
 *
 * @author XingYang
 */
public interface UserService extends IService<User> {

    /**
     * Get user by username
     *
     * @param username Username
     * @return User
     */
    User getUserByUsername(String username);

    /**
     * Login
     *
     * @param loginDTO Login parameters
     * @return JWT token and user information
     */
    Map<String, Object> login(LoginDTO loginDTO);

    /**
     * Register
     *
     * @param registerDTO Register parameters
     * @return User information
     */
    UserVO register(RegisterDTO registerDTO);

    /**
     * Get current user information
     *
     * @return User information
     */
    UserVO getCurrentUser();

    /**
     * Update user information
     *
     * @param userUpdateDTO User update parameters
     * @return User information
     */
    UserVO updateUserInfo(UserUpdateDTO userUpdateDTO);

    /**
     * Change password
     *
     * @param oldPassword Old password
     * @param newPassword New password
     * @return Result
     */
    boolean changePassword(String oldPassword, String newPassword);

    /**
     * Page query
     *
     * @param page Page
     * @param user User query conditions
     * @return User page
     */
    Page<UserVO> pageQuery(Page<User> page, User user);
} 