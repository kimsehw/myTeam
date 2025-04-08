package com.kimsehw.myteam.application;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatCode;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@AutoConfigureMockMvc
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

    private Member createMember(String email) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("test");
        memberFormDto.setEmail(email);
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @Test
    void 팀생성_테스트() {
        Member member = createMember("test@naver.com");
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");

        Team team = teamRepository.findById(teamId).orElseThrow();
        Member member1 = memberService.getMemberOf(email);

        assertThat(team.getLeader().getMember()).isEqualTo(member1);
    }

    @Test
    void 중복_팀명_테스트() {
        Member member = createMember("test@naver.com");
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
        return teamFacade.createTeam(email, teamFormDto, new MockMultipartFile("test", (byte[]) null));
    }

    @Test
    void 팀장_추가_테스트() {
        Member member = createMember("test@naver.com");
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        Pageable pageable = PageRequest.of(0, 6);
        Page<TeamsDto> teamsDtoPage = memberService.getTeamsDtoPage(email, pageable);

        TeamsDto teamsDto = teamsDtoPage.getContent().get(0);
        assertThat(teamsDto.getTeamId()).isEqualTo(teamId);
        assertThat(teamsDto.getTeamRole()).isEqualTo(TeamRole.LEADER);
    }

    @Test
    void test() {
        Member member = createMember("test@naver.com");
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        Member member2 = createMember("test2@naver.com");
        memberService.saveMember(member2);

        String email2 = member2.getEmail();
        Long teamId2 = createTeam(email2, "test Team");
        Team team = teamRepository.findById(teamId).orElseThrow();
        System.out.println(team.getTeamLogo());
        System.out.println("dddd");
        Team findTeam = teamRepository.findWithTeamMembersAndTeamLogoById(teamId);
        TeamInfoDto teamInfoDto = new TeamInfoDto(findTeam.getId(), findTeam.getTeamName(),
                findTeam.getTeamLogo().getImgUrl(),
                findTeam.getTeamMembers().size(), findTeam.getRegion(), findTeam.getAgeRange(),
                findTeam.getTeamRecord(),
                findTeam.getTeamDetail());
        System.out.println(teamInfoDto.toString());
        //em.flush();
    }

    @Test
    void 팀목록조회_테스트() {
        Member member = createMember("test@naver.com");
        Member member2 = createMember("test2@naver.com");
        memberService.saveMember(member);
        memberService.saveMember(member2);
        Long teamA = createTeam(member.getEmail(), "teamA");
        Long teamB = createTeam(member.getEmail(), "teamB");
        Long teamC = createTeam(member2.getEmail(), "teamB");
        Long teamD = createTeam(member2.getEmail(), "teamD");
        Pageable pageable = PageRequest.of(0, 6);
        Page<TeamsDto> teamsDtoPage = memberService.getTeamsDtoPage(member2.getEmail(), pageable);
        for (TeamsDto teamsDto : teamsDtoPage) {
            assertThat(teamsDto.getTeamName()).containsAnyOf("teamB", "teamD");
        }
    }
}