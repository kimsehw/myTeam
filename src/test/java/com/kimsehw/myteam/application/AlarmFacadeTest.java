package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.factory.AlarmFactory;
import com.kimsehw.myteam.dto.alarm.AlarmResponseFormDto;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.repository.member.MemberRepository;
import com.kimsehw.myteam.service.alarm.AlarmService;
import java.util.Map;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;

@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlarmFacadeTest {

    @Autowired
    Map<String, AlarmService> serviceMap;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    AlarmFacade alarmFacade;

    private Team createTeam() {
        TeamFormDto teamFormDto = new TeamFormDto();
        teamFormDto.setTeamName("test");
        teamFormDto.setRegion(Region.GYEONGGIDO);
        teamFormDto.setAgeRange(AgeRange.TWENTY);
        return Team.createTeam(teamFormDto, Mockito.mock(TeamMember.class));
    }

    private Member createMember(String email) {
        MemberFormDto memberFormDto = new MemberFormDto();
        memberFormDto.setName("test");
        memberFormDto.setEmail(email);
        memberFormDto.setPassword("1234");
        return Member.createMember(memberFormDto, passwordEncoder);
    }

    @BeforeAll
    void initAlarmDb() {
        AlarmService alarmService = serviceMap.get("alarmServiceImpl");

        Member m1 = createMember("aaa@test.com");
        memberRepository.save(m1);
        Member m2 = createMember("aaa1@test.com");
        memberRepository.save(m2);
        Member m3 = createMember("aaa3@test.com");
        memberRepository.save(m3);
        Team t1 = createTeam();
        teamRepository.save(t1);
        Team t2 = createTeam();
        teamRepository.save(t2);
        Team t3 = createTeam();
        teamRepository.save(t3);

        alarmService.send(AlarmFactory.createTeamMemInviteAlarm(m1, m2, t1, 3));
        alarmService.send(AlarmFactory.createTeamMemInviteAlarm(m1, m3, t1, 3));
        alarmService.send(AlarmFactory.createTeamMemInviteAlarm(m2, m1, t1, 3));

        alarmService.send(AlarmFactory.createMatchInviteAlarm(m1, m2, t1, t2, null, 2));
        alarmService.send(AlarmFactory.createMatchInviteAlarm(m1, m3, t1, t3, null, 2));
        alarmService.send(AlarmFactory.createMatchInviteAlarm(m2, m1, t2, t1, null, 2));
        alarmService.send(AlarmFactory.createMatchInviteAlarm(m3, m1, t3, t1, null, 2));

        alarmService.send(AlarmFactory.createMatchInviteResponseAlarm(m3, m2, t2, t3, false));
        alarmService.send(AlarmFactory.createMatchInviteResponseAlarm(m1, m3, t1, t3, false));
    }

    @ParameterizedTest
    @Transactional
    void t() {
        AlarmResponseFormDto alarmResponseFormDto = new AlarmResponseFormDto();
        alarmFacade.sendResponseWithAction(alarmResponseFormDto, "aaa@test.com");
    }
}