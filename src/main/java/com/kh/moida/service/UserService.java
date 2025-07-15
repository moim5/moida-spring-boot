package com.kh.moida.service;

import com.kh.moida.mapper.UserMapper;
import com.kh.moida.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserMapper userMapper;
    private final BCryptPasswordEncoder passwordEncoder;

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

    public boolean updatePassword(Long userId, String prevPassword, String password) {
        String currentHash = userMapper.findPasswordById(userId);
        if (!passwordEncoder.matches(prevPassword, currentHash)) {
            return false;
        }

        String newHash = passwordEncoder.encode(password);
        userMapper.updatePassword(userId, newHash);
        return true;
    }
}
