package com.kimsehw.myteam.service;

import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    public static final String DUPLICATE_MEMBER_EXIST = "중복된 회원이 존재합니다.";
    private final MemberRepository memberRepository;

    @Transactional
    public Long saveUser(Member user) {
        validateDuplicate(user);
        memberRepository.save(user);
        return user.getId();
    }

    private void validateDuplicate(Member user) {
        Member findUser = memberRepository.findByEmail(user.getEmail());
        if (findUser != null) {
            throw new IllegalStateException(DUPLICATE_MEMBER_EXIST);
        }
    }
}
