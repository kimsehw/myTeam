package com.kimsehw.myteam.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.constant.TeamRole;
import com.kimsehw.myteam.dto.TeamsDto;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityManager;
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
class TeamFacadeTest {

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TeamFacade teamFacade;

    @Autowired
    private TeamRepository teamRepository;

    @Autowired
    EntityManager em;

    @Autowired
    private TeamMemberService teamMemberService;

    private Member createMember() {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("test");
        memberFormDto.setEmail("test@naver.com");
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    void 팀생성_테스트() {
        Member member = createMember();
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");

        Team team = teamRepository.findById(teamId).orElseThrow();
        Member member1 = memberService.findMemberByEmail(email);

        assertThat(team.getMember()).isEqualTo(member1);
    }

    @Test
    void 중복_팀명_테스트() {
        Member member = createMember();
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        assertThatCode(() -> createTeam(email, "test Team diff")).doesNotThrowAnyException();
        assertThatCode(() -> createTeam(email, "test Team"))
                .isInstanceOf(IllegalStateException.class)
                .hasMessage(TeamService.DUPLICATE_TEAM_NAME_EXCEPTION);
    }

    private Long createTeam(String email, String teamName) {
        TeamFormDto teamFormDto = new TeamFormDto();
        teamFormDto.setTeamName(teamName);
        teamFormDto.setRegion(Region.GYEONGGIDO);
        teamFormDto.setAgeRange(AgeRange.TWENTY);
        return teamFacade.createTeam(email, teamFormDto);
    }

    @Test
    void 팀장_추가_테스트() {
        Member member = createMember();
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        Pageable pageable = PageRequest.of(0, 6);
        Page<TeamsDto> teamsDtoPage = teamMemberService.getTeamsDtoPage(member.getId(), pageable);

        TeamsDto teamsDto = teamsDtoPage.getContent().get(0);
        assertThat(teamsDto.getTeamId()).isEqualTo(teamId);
        assertThat(teamsDto.getTeamRole()).isEqualTo(TeamRole.LEADER);
    }
}