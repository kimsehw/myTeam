package com.kimsehw.myteam.application;

import com.kimsehw.myteam.constant.alarm.AlarmType;
import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.alarm.Alarm;
import com.kimsehw.myteam.domain.entity.alarm.MatchInviteAlarm;
import com.kimsehw.myteam.domain.entity.alarm.TeamMemInviteAlarm;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.factory.AlarmFactory;
import com.kimsehw.myteam.dto.alarm.AlarmDto;
import com.kimsehw.myteam.dto.alarm.AlarmResponseFormDto;
import com.kimsehw.myteam.dto.alarm.AlarmSearchDto;
import com.kimsehw.myteam.service.MatchService;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.TeamMemberService;
import com.kimsehw.myteam.service.TeamService;
import com.kimsehw.myteam.service.alarm.AlarmService;
import com.kimsehw.myteam.service.alarm.factory.AlarmServiceFactory;
import com.kimsehw.myteam.service.alarm.invite.InviteAlarmService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class AlarmFacade {

    public static final String WRONG_TO_INFO = "입력하신 수신자가 수신 팀의 접근 권한이 없는 팀입니다.";
    public static final String WRONG_FROM_INFO = "접근 권한이 없는 팀의 응답을 요청하셨습니다. 발신 팀 정보를 확인해주세요.";
    private final AlarmServiceFactory alarmServiceFactory;
    private final MemberService memberService;
    private final TeamMemberService teamMemberService;
    private final TeamService teamService;
    private final MatchService matchService;

    public Page<AlarmDto> getMyAlarmPages(AlarmSearchDto alarmSearchDto, String email, Pageable pageable) {
        Member member = memberService.getMemberByEmail(email);
        AlarmService alarmService = alarmServiceFactory.getService(alarmSearchDto.getAlarmType());
        return alarmService.getMyAlarms(alarmSearchDto, member.getId(), pageable);
    }

    /**
     * 응답 요청 정보에 대한 유효성 검사를 진행합니다. 응답에 맞게 응답 알람을 보내고 수락 여부에 따라 팀 멤버 or 매치를 추가합니다.
     *
     * @param alarmResponseFormDto 응답 정보
     * @param email                요청 회원 아이디
     * @throws IllegalArgumentException                    잘못된 응답 정보를 요청받음
     * @throws jakarta.persistence.EntityNotFoundException 존재하지 않는 아이디 값 정보를 요청 받음
     */
    public void sendResponseWithAction(AlarmResponseFormDto alarmResponseFormDto, String email) {
        Member member = memberService.getMemberByEmail(email);
        Member toMember = memberService.getMemberById(alarmResponseFormDto.getToMemId());
        AlarmType alarmType = alarmResponseFormDto.getAlarmType();

        InviteAlarmService alarmService = alarmServiceFactory.getInviteService(alarmType);
        Alarm alarm = alarmService.getAlarm(alarmResponseFormDto.getInviteAlarmId());
        alarmService.validateResponseForm(alarm, member.getId(), alarmResponseFormDto);
        if (alarmType.equals(AlarmType.TEAM_INVITE)) {
            sendTeamInviteResponse(alarmResponseFormDto, toMember, (TeamMemInviteAlarm) alarm, member, alarmService);
            alarmService.delete(alarm);
            return;
        }
        sendMatchInviteResponse(alarmResponseFormDto, toMember, member, (MatchInviteAlarm) alarm, alarmService);
        alarmService.delete(alarm);
    }

    private void sendTeamInviteResponse(AlarmResponseFormDto alarmResponseFormDto, Member toMember,
                                        TeamMemInviteAlarm teamMemInviteAlarm, Member member,
                                        InviteAlarmService<TeamMemInviteAlarm> alarmService) {
        try {
            memberService.isMyTeam(toMember.getEmail(), alarmResponseFormDto.getToTeamId());
            Team toTeam = teamService.findById(alarmResponseFormDto.getToTeamId());
            addTeamMember(alarmResponseFormDto.getResponse(), member, teamMemInviteAlarm, toTeam);
            alarmService.send(AlarmFactory.createTeamInviteResponseAlarm(member, toMember, toTeam,
                    alarmResponseFormDto.getResponse()));
        } catch (IllegalArgumentException e) {
            throw new IllegalArgumentException(WRONG_TO_INFO);
        }
    }

    private void addTeamMember(boolean response, Member member,
                               TeamMemInviteAlarm teamMemInviteAlarm, Team toTeam) {
        if (response) {
            teamMemberService.addTeamMemberIn(toTeam, member, TeamRole.MEMBER, teamMemInviteAlarm.getPlayerNum());
        }
    }

    private void sendMatchInviteResponse(AlarmResponseFormDto alarmResponseFormDto, Member toMember, Member member,
                                         MatchInviteAlarm matchInviteAlarm,
                                         InviteAlarmService<MatchInviteAlarm> alarmService) {
        boolean to_info_pass = false;
        try {
            to_info_pass = memberService.isMyTeam(toMember.getEmail(), alarmResponseFormDto.getToTeamId());
            memberService.isMyTeam(member.getEmail(), alarmResponseFormDto.getFromTeamId());
            Team toTeam = teamService.findById(alarmResponseFormDto.getToTeamId());
            Team fromTeam = teamService.findById(alarmResponseFormDto.getFromTeamId());
            addMatch(alarmResponseFormDto.getResponse(), matchInviteAlarm, toTeam, fromTeam);
            alarmService.send(AlarmFactory.createMatchInviteResponseAlarm(member, toMember, fromTeam, toTeam,
                    matchInviteAlarm.getMatchDate(), matchInviteAlarm.getMatchTime(),
                    alarmResponseFormDto.getResponse()));
        } catch (IllegalArgumentException e) {
            if (!to_info_pass) {
                throw new IllegalArgumentException(WRONG_TO_INFO);
            }
            throw new IllegalArgumentException(WRONG_FROM_INFO);
        }
    }

    private void addMatch(boolean response, MatchInviteAlarm matchInviteAlarm, Team toTeam, Team fromTeam) {
        if (response) {
            matchService.addMatchOn(fromTeam, toTeam, matchInviteAlarm.getMatchDate(), matchInviteAlarm.getMatchTime());
            matchService.addMatchOn(toTeam, fromTeam, matchInviteAlarm.getMatchDate(), matchInviteAlarm.getMatchTime());
        }
    }

    /**
     * 해당 알람의 삭제 권한을 검사하고 알람의 상태에 따라 알람을 삭제 혹은 요청한 유저로부터 숨깁니다.
     *
     * @param alarmId 알람 식별자
     * @param email   요청 유저 이메일
     */
    public void delete(Long alarmId, String email) {
        AlarmService alarmService = alarmServiceFactory.getService(AlarmType.ALARM);
        Alarm alarm = alarmService.getAlarmWithFromToMemberInfo(alarmId);

        alarmService = alarmServiceFactory.getService(alarm.getAlarmType());
        alarmService.validateAuthFrom(alarm, email);
        alarmService.deleteOrHide(alarm, email);
    }

    /**
     * 해당 알람을 읽습니다.
     *
     * @param alarmId 알람 식별자
     * @param email   요청 유저 이메일
     */
    public void read(Long alarmId, String email) {
        AlarmService alarmService = alarmServiceFactory.getService(AlarmType.ALARM);
        alarmService.read(alarmId, email);
    }
}
