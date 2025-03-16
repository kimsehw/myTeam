package com.kimsehw.myteam.service;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.TeamsDto;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.TeamMember;
import com.kimsehw.myteam.repository.MemberRepository;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.repository.teammember.TeamMemberRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
class TeamMemberServiceTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    @Autowired
    TeamFacade teamFacade;

    @Autowired
    TeamMemberService teamMemberService;
    @Autowired
    TeamMemberRepository teamMemberRepository;
    @Autowired
    private TeamRepository teamRepository;
    @Autowired
    private MemberRepository memberRepository;

    private Member createMember(String email) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("test");
        memberFormDto.setEmail(email);
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }


    private Long createTeam(String email, String teamName) {
        TeamFormDto teamFormDto = new TeamFormDto();
        teamFormDto.setTeamName(teamName);
        teamFormDto.setRegion(Region.GYEONGGIDO);
        teamFormDto.setAgeRange(AgeRange.TWENTY);
        return teamFacade.createTeam(email, teamFormDto);
    }

    /*
     * 팀에 팀 멤버 추가랑 멤버가 팀 만들 때 팀 리더 되면서 팀 멤버 추가 기능 추가해야함
     * 테스트에서 일단은 그냥 넣어줘서 만들고 테스트 먼저 해보던지?
     * 커밋은 팀 만들면서 팀 멤버 추가 1번
     * 팀에 팀 멤버 추가 하는 거 2번
     * 팀 레포지토리 기능 구현 및 테스트 3번
     * */
    @Test
    void 팀목록조회_테스트() {
        Member member = createMember("test@naver.com");
        Member member2 = createMember("test2@naver.com");
        memberRepository.save(member);
        memberRepository.save(member2);
        Long teamA = createTeam(member.getEmail(), "teamA");
        Long teamB = createTeam(member.getEmail(), "teamB");
        Long teamC = createTeam(member2.getEmail(), "teamB");
        Long teamD = createTeam(member2.getEmail(), "teamD");
        TeamMember teamMemberA = TeamMember.createTeamMember(teamRepository.findById(teamA).orElseThrow(), member,
                TeamRole.LEADER);
        TeamMember teamMemberB = TeamMember.createTeamMember(teamRepository.findById(teamB).orElseThrow(), member,
                TeamRole.LEADER);
        TeamMember teamMemberC = TeamMember.createTeamMember(teamRepository.findById(teamC).orElseThrow(), member2,
                TeamRole.LEADER);
        TeamMember teamMemberD = TeamMember.createTeamMember(teamRepository.findById(teamD).orElseThrow(), member2,
                TeamRole.LEADER);
        teamMemberRepository.save(teamMemberA);
        teamMemberRepository.save(teamMemberB);
        teamMemberRepository.save(teamMemberC);
        teamMemberRepository.save(teamMemberD);
        Pageable pageable = PageRequest.of(0, 6);
        Page<TeamsDto> teamsDtoPage = teamMemberService.getTeamsDtoPage(member2.getId(), pageable);
        for (TeamsDto teamsDto : teamsDtoPage) {
            System.out.println("teamsDto = " + teamsDto);
        }
    }

}