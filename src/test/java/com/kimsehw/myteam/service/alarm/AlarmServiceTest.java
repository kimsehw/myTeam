package com.kimsehw.myteam.service.alarm;

import com.kimsehw.myteam.application.TeamFacade;
import com.kimsehw.myteam.constant.team.AgeRange;
import com.kimsehw.myteam.constant.team.Region;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.factory.AlarmFactory;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.dto.member.MemberFormDto;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.repository.TeamRepository;
import com.kimsehw.myteam.repository.member.MemberRepository;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mockito;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.context.TestPropertySource;
import org.springframework.transaction.annotation.Transactional;


@SpringBootTest
@TestPropertySource(locations = "classpath:application-test.properties")
@Transactional
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class AlarmServiceTest {

    private static final Logger log = LoggerFactory.getLogger(AlarmServiceTest.class);
    @Autowired
    Map<String, AlarmService> serviceMap;
    @Autowired
    MemberRepository memberRepository;
    @Autowired
    TeamRepository teamRepository;
    @Autowired
    PasswordEncoder passwordEncoder;
    @Autowired
    TeamFacade teamFacade;

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

        alarmService.send(AlarmFactory.createResponseAlarm(m3, m2, t2, t3, false));
        alarmService.send(AlarmFactory.createResponseAlarm(m1, m3, t1, t3, false));
    }

    @ParameterizedTest
    @MethodSource("generateSearchCase")
    void queryTest(String serviceName, int expected, AlarmSearchDto alarmSearchDto) {
        log.info("================");
        log.info(serviceName);
        AlarmService alarmService = serviceMap.get(serviceName);
        Pageable pageable = PageRequest.of(0, 10);
        List<AlarmDto> alarms = alarmService.getMyAlarms(alarmSearchDto, 1L, pageable).getContent();
        log.info(alarms.toString());
        Assertions.assertThat(alarms.size()).isEqualTo(expected);
    }


    private static Stream<Arguments> generateSearchCase() {
        AlarmSearchDto sentSearchDto = new AlarmSearchDto();
        sentSearchDto.setIsSent(true);
        AlarmSearchDto receiveSearchDto = new AlarmSearchDto();
        receiveSearchDto.setIsSent(false);
        return Stream.of(
                Arguments.of("alarmServiceImpl", 8, null),
                Arguments.of("alarmServiceImpl", 5, sentSearchDto),
                Arguments.of("alarmServiceImpl", 8 - 5, receiveSearchDto),
                Arguments.of("teamMemInviteAlarmServiceImpl", 3, null),
                Arguments.of("teamMemInviteAlarmServiceImpl", 2, sentSearchDto),
                Arguments.of("teamMemInviteAlarmServiceImpl", 3 - 2, receiveSearchDto),
                Arguments.of("matchInviteAlarmServiceImpl", 4, null),
                Arguments.of("matchInviteAlarmServiceImpl", 2, sentSearchDto),
                Arguments.of("matchInviteAlarmServiceImpl", 4 - 2, receiveSearchDto)
        );
    }
}