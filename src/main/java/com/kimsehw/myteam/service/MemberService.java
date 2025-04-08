package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.repository.member.MemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;
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
    public static final String WRONG_TEAM_INFO_ERROR = "팀 정보가 잘못되었습니다. 팀 정보를 확인해주세요.";
    public static final String WRONG_EMAIL_ERROR = "존재하지 않는 회원입니다. 이메일을 확인해주세요.";
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

    public Member getMemberOf(String email) {
        Optional<Member> optionalMember = memberRepository.findWithMyTeamsInfoByEmail(email);
        if (optionalMember.isEmpty()) {
            throw new EntityNotFoundException(WRONG_EMAIL_ERROR);
        }
        return optionalMember.get();
    }

    /**
     * 내 팀의 정보들을 가져옵니다. (teamLogo 및 멤버 수 제외)
     *
     * @param email
     * @return List<TeamInfoDto> (teamLogo 및 멤버 수 제외)
     */
    public List<TeamInfoDto> findMyTeamsInfoByEmail(String email) {
        Member member = getMemberOf(email);
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

    /**
     * 해당 유저의 팀인지 검사
     *
     * @param email    유저 이메일
     * @param teamId   검사 하고자하는 팀 아이디
     * @param teamName 검사 하고자하는 팀 명
     * @return 내 팀의 정보가 맞다면 true
     * @throws EntityNotFoundException  해당 이메일의 유저가 존재하지 않음
     * @throws IllegalArgumentException 해당 유저의 팀 목록에 존재하지 않는 팀 아이디 혹은 팀 명
     */
    public boolean isMyTeam(String email, Long teamId, String teamName) {
        Member member = getMemberOf(email);
        Map<Long, String> myTeamIdsAndNames = member.getMyTeams().stream()
                .map(TeamMember::getTeam)
                .collect(Collectors.toMap(Team::getId, Team::getTeamName));
        if (!myTeamIdsAndNames.containsKey(teamId)) {
            throw new IllegalArgumentException(WRONG_TEAM_INFO_ERROR);
        }
        if (!myTeamIdsAndNames.get(teamId).equals(teamName)) {
            throw new IllegalArgumentException(WRONG_TEAM_INFO_ERROR);
        }
        return true;
    }
}
