package com.kh.moida.mapper;

import com.kh.moida.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findByUsername(String username);

    void updateUserInfo(User user);

    String findPasswordById(@Param("userId") Long userId);

    void updatePassword(@Param("userId") Long userId, @Param("password") String newHash);
}
