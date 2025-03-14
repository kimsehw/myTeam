package com.kimsehw.myteam.service;

import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Log
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService implements UserDetailsService {

    public static final String DUPLICATE_MEMBER_EXIST = "중복된 회원이 존재합니다.";
    private final MemberRepository memberRepository;

    @Transactional
    public String saveMember(Member member) {
        validateDuplicate(member);
        memberRepository.save(member);
        return member.getEmail();
    }

    private void validateDuplicate(Member member) {
        if (memberRepository.findById(member.getEmail()).isPresent()) {
            throw new IllegalStateException(DUPLICATE_MEMBER_EXIST);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberRepository.findById(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder()
                .username(findMember.getEmail())
                .password(findMember.getPassword())
                .roles(findMember.getRole().toString())
                .build();
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findById(email).orElseThrow();
    }
}
