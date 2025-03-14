package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.Team;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamService;
import jakarta.persistence.EntityManager;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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

        Assertions.assertThat(team.getMember()).isEqualTo(member1);
    }

    @Test
    void 중복_팀명_테스트() {
        Member member = createMember();
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        Assertions.assertThatCode(() -> createTeam(email, "test Team diff")).doesNotThrowAnyException();
        Assertions.assertThatCode(() -> createTeam(email, "test Team"))
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
}