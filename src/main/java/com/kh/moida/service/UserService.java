package com.kh.moida.service;

import com.kh.moida.mapper.UserMapper;
import com.kh.moida.model.User;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    public List<User> findUser(int offset, int limit) {
        int startRow = offset + 1;
        int endRow = offset + limit;

        Map<String, Object> params = new HashMap<>();
        params.put("startRow", startRow);
        params.put("endRow", endRow);

        return userMapper.findUser(params);
    }

    public User findUserByUserId(Long userId) {
        return userMapper.findUserByUserId(userId);
    }

    public void UpdateUserForAdmin(User user) {
        userMapper.updateUserForAdmin(user);
    }
}
