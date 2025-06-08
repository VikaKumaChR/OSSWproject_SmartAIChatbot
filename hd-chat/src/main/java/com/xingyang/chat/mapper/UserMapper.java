package com.xingyang.chat.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.xingyang.chat.model.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * User Mapper
 *
 * @author XingYang
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    /**
     * 获取用户角色代码
     * 
     * @param userId 用户ID
     * @return 角色代码数组
     */
    @Select("SELECT r.code FROM role r INNER JOIN user_role ur ON r.id = ur.role_id " +
            "WHERE ur.user_id = #{userId} AND ur.deleted = 0 AND r.deleted = 0")
    String[] getUserRoles(@Param("userId") Long userId);
} 