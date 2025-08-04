package com.kh.moida.mapper;

import com.kh.moida.model.User;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;
import java.util.Map;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findByUsername(String username);

    void updateUserInfo(User user);

    String findPasswordById(@Param("userId") Long userId);

    void updatePassword(@Param("userId") Long userId, @Param("password") String newHash);

    List<User> findUser(Map<String, Object> params);

    User findUserByUserId(Long userId);

    void updateUserForAdmin(User user);
}
