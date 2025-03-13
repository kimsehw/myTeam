package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.TeamFormDto;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.Team;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.service.MemberService;
import jakarta.persistence.EntityManager;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
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
    private static final Log log = LogFactory.getLog(TeamFacadeTest.class);
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
        TeamFormDto teamFormDto = new TeamFormDto();
        teamFormDto.setTeamName("test Team");
        teamFormDto.setRegion(Region.GYEONGGIDO);
        teamFormDto.setAgeRange(AgeRange.TWENTY);
        Long teamId = teamFacade.createTeam(email, teamFormDto);

        Team team = teamRepository.findById(teamId).orElseThrow();
        Member member1 = memberService.findMemberByEmail(email);

        Assertions.assertThat(team.getMember()).isEqualTo(member1);
    }
}