package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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
    public Long saveMember(Member member) {
        validateDuplicate(member);
        memberRepository.save(member);
        return member.getId();
    }

    private void validateDuplicate(Member member) {
        if (memberRepository.findByEmail(member.getEmail()).isPresent()) {
            throw new IllegalStateException(DUPLICATE_MEMBER_EXIST);
        }
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Member findMember = memberRepository.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException(email));

        return User.builder()
                .username(findMember.getEmail())
                .password(findMember.getPassword())
                .roles(findMember.getRole().toString())
                .build();
    }

    public Member findMemberByEmail(String email) {
        return memberRepository.findByEmail(email).orElseThrow(EntityNotFoundException::new);
    }

    /**
     * 내 팀의 정보들을 가져옵니다. (teamLogo 및 멤버 수 제외)
     *
     * @param email
     * @return List<TeamInfoDto> (teamLogo 및 멤버 수 제외)
     */
    public List<TeamInfoDto> findMyTeamsInfoByEmail(String email) {
        Member member = memberRepository.findWithMyTeamsInfoByEmail(email).orElseThrow(EntityNotFoundException::new);
        return member.getMyTeams().stream()
                .map(this::getMyTeamsInfoDto)
                .toList();
    }

    private TeamInfoDto getMyTeamsInfoDto(TeamMember tm) {
        return TeamInfoDto.withOutLogoAndMemberNumOf(tm.getTeam());
    }

    /**
     * 회원이 소속된 팀 목록을 조회합니다.
     *
     * @param email    회원 아이디
     * @param pageable 페이징
     * @return Page<TeamsDto>
     */
    public Page<TeamsDto> getTeamsDtoPage(String email, Pageable pageable) {
        return memberRepository.findMyTeamsDtoPageByEmail(email, pageable);
    }
}
