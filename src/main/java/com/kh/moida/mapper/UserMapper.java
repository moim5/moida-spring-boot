package com.kh.moida.mapper;

import com.kh.moida.model.User;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface UserMapper {
    void insertUser(User user);

    User findByUsername(String username);
}
