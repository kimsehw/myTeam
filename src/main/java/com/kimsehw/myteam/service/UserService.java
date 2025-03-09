package com.kimsehw.myteam.service;

import com.kimsehw.myteam.entity.User;
import com.kimsehw.myteam.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {

    public static final String DUPLICATE_MEMBER_EXIST = "중복된 회원이 존재합니다.";
    private final UserRepository userRepository;

    @Transactional
    public Long saveUser(User user) {
        validateDuplicate(user);
        userRepository.save(user);
        return user.getId();
    }

    private void validateDuplicate(User user) {
        User findUser = userRepository.findByEmail(user.getEmail());
        if (findUser != null) {
            throw new IllegalStateException(DUPLICATE_MEMBER_EXIST);
        }
    }
}
