package com.todo.mapper;

import com.todo.entity.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Update;

/**
 * 用户映射器
 */
@Mapper
public interface UserMapper {
    
    /**
     * 根据用户名查询用户
     */
    @Select("SELECT * FROM users WHERE username = #{username}")
    User findByUsername(String username);
    
    /**
     * 根据邮箱查询用户
     */
    @Select("SELECT * FROM users WHERE email = #{email}")
    User findByEmail(String email);
    
    /**
     * 根据ID查询用户
     */
    @Select("SELECT * FROM users WHERE id = #{id}")
    User findById(Long id);
    
    /**
     * 创建用户
     */
    @Insert("INSERT INTO users(username, password, email, full_name, role, status, created_at, updated_at) " +
            "VALUES(#{username}, #{password}, #{email}, #{fullName}, #{role}, #{status}, NOW(), NOW())")
    int insert(User user);
    
    /**
     * 更新用户
     */
    @Update("UPDATE users SET full_name = #{fullName}, status = #{status}, updated_at = NOW() WHERE id = #{id}")
    int update(User user);
}
