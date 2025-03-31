package com.kimsehw.myteam.application;

import static org.assertj.core.api.Assertions.assertThat;

import com.kimsehw.myteam.constant.AgeRange;
import com.kimsehw.myteam.constant.Region;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemInviteFormDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.stream.Stream;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
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
class TeamMemFacadeTest {

    @Autowired
    private TeamMemFacade teamMemFacade;

    @Autowired
    private TeamMemberService teamMemberService;

    @Autowired
    private MemberService memberService;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private TeamFacade teamFacade;

    @ParameterizedTest
    @MethodSource("generateValidateCase")
    void 초대_데이터_유효성검사(TeamMemInviteFormDto teamMemInviteFormDto, Map<String, String> errorMessages) {
        Member member = createMember("test@naver.com");
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");

        Map<String, String> errors = new HashMap<>();

        // when
        teamMemFacade.validateInviteInfo(teamId, teamMemInviteFormDto, errors, email);

        //then
        assertThat(!errors.isEmpty()).isTrue();

        for (Entry<String, String> entry : errorMessages.entrySet()) {
            assertThat(errors.get(entry.getKey())).isEqualTo(entry.getValue());
        }
    }

    private static Stream<Arguments> generateValidateCase() {
        String noEmail = "초대 회원의 이메일을 입력해주세요.";
        String noMember = "존재하지 않는 회원입니다. 초대 회원의 이메일을 확인해주세요.";
        String noNum = "등 번호를 입력해주세요.";
        String duplicateNum = "중복된 등 번호 선수가 존재합니다.";
        return Stream.of(
                Arguments.of(getInviteDto("", false, 9, ""), Map.of("email", noEmail)),
                Arguments.of(getInviteDto("tt", false, 9, ""), Map.of("email", noMember)),
                Arguments.of(getInviteDto("test@naver.com", true, null, "name"), Map.of("playerNum", noNum)),
                Arguments.of(getInviteDto("test@naver.com", true, 0, ""), Map.of("playerNum", duplicateNum)),
                Arguments.of(getInviteDto("", false, null, ""), Map.of("email", noEmail,
                        "playerNum", noNum)),
                Arguments.of(getInviteDto("tt", false, 0, ""), Map.of("email", noMember,
                        "playerNum", duplicateNum))
        );
    }

    @Test
    void 비회원_초대_테스트() {
        Member member = createMember("test@naver.com");
        memberService.saveMember(member);

        String email = member.getEmail();
        Long teamId = createTeam(email, "test Team");
        String testName = "test";
        TeamMemInviteFormDto inviteDto = getInviteDto("test@naver.com", true, 20, testName);
        Pageable pageable = PageRequest.of(0, 10);

        Page<TeamMemberDto> teamMemberDtoPagesOf = teamMemberService.getTeamMemberDtoPagesOf(teamId, pageable);
        assertThat(teamMemberDtoPagesOf.getContent().size()).isEqualTo(1);

        teamMemFacade.invite(email, teamId, inviteDto);

        Page<TeamMemberDto> teamMemberDtoPagesOf2 = teamMemberService.getTeamMemberDtoPagesOf(teamId, pageable);
        for (TeamMemberDto teamMemberDto : teamMemberDtoPagesOf2.getContent()) {
            System.out.println(teamMemberDto.toString());
        }
        assertThat(teamMemberDtoPagesOf2.getContent().size()).isEqualTo(2);
    }

    private static TeamMemInviteFormDto getInviteDto(String email, boolean isNotUser, Integer playerNum, String name) {
        TeamMemInviteFormDto teamMemInviteFormDto = new TeamMemInviteFormDto();
        teamMemInviteFormDto.setPlayerNum(playerNum);
        teamMemInviteFormDto.setEmail(email);
        teamMemInviteFormDto.setNotUser(isNotUser);
        teamMemInviteFormDto.setName(name);
        return teamMemInviteFormDto;
    }

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
        return teamFacade.createTeam(email, teamFormDto, new MockMultipartFile("test", (byte[]) null));
    }
}