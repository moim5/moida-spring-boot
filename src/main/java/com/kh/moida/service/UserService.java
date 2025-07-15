package com.kh.moida.service;

import com.kh.moida.mapper.UserMapper;
import com.kh.moida.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;

    public void save(User user) {
        user.setIsActive("Y");
        user.setIsAdmin("N");
        userMapper.insertUser(user);
    }

    public User findByUsername(String email) {
        return userMapper.findByUsername(email);
    }

    public void updateUserInfo(User user) {
        userMapper.updateUserInfo(user);
    }
}
