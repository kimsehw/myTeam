package com.kimsehw.myteam.service;

import com.kimsehw.myteam.constant.Position;
import com.kimsehw.myteam.constant.teammember.TeamRole;
import com.kimsehw.myteam.domain.FieldError;
import com.kimsehw.myteam.dto.team.TeamsDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDetailDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberUpdateDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.TeamMember;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.exception.FieldErrorException;
import com.kimsehw.myteam.repository.teammember.TeamMemberRepository;
import jakarta.persistence.EntityNotFoundException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
@Log
public class TeamMemberService {

    public static final String NO_NUM_INPUT_ERROR = "등 번호를 입력해주세요.";
    public static final String DUPLICATE_NUM_ERROR = "중복된 등 번호 선수가 존재합니다.";
    private final TeamMemberRepository teamMemberRepository;

    public Page<TeamsDto> getTeamsDtoPage(Long memberId, Pageable pageable) {
        return teamMemberRepository.getTeamsDtoPage(memberId, pageable);
    }

    /**
     * 팀 생성 시 팀 초기 멤버(리더)를 등록
     *
     * @param team     소속 팀
     * @param member   팀원이 될 회원
     * @param teamRole 팀내 직책
     * @return teamMemberId
     */
    @Transactional
    public Long addInitialTeamMember(Team team, Member member, TeamRole teamRole) {
        TeamMember teamMember = TeamMember.createInitialTeamMember(team, member, teamRole);
        return teamMember.getId();
    }

    /**
     * 등번호를 배정하며 회원인 팀 멤버 등록
     *
     * @param team      소속 팀
     * @param member    팀원이 될 회원
     * @param teamRole  팀내 직책
     * @param playerNum 등번호
     * @return teamMemberId
     */
    @Transactional
    public Long addTeamMemberIn(Team team, Member member, TeamRole teamRole, int playerNum) {
        TeamMember teamMember = TeamMember.createTeamMember(team, member, teamRole, playerNum);
        return teamMember.getId();
    }

    /**
     * 비회원 팀 멤버 등록
     *
     * @param team
     * @param name
     * @param teamRole
     * @param playerNum
     * @param position
     */
    @Transactional
    public Long addTeamMemberIn(Team team, String name, TeamRole teamRole, Integer playerNum, Position position) {
        TeamMember teamMember = TeamMember.createNotUserTeamMember(team, name, teamRole, playerNum, position);
        return teamMember.getId();
    }

    public Page<TeamMemberDto> getTeamMemberDtoPagesOf(Long teamId, Pageable pageable) {
        return teamMemberRepository.findAllTeamMemberDtoByTeamId(teamId, pageable);
    }

    /**
     * 초대 시 등번호의 유효성을 검사합니다(입력 여부 & 팀 내 중복 번호 유무)
     *
     * @param teamId
     * @param playerNum
     */
    public void validatePlayerNum(Long teamId, Integer playerNum) {
        if (playerNum == null) {
            throw new FieldErrorException(FieldError.of("playerNum", NO_NUM_INPUT_ERROR));
        }

        TeamMember byPlayerNumAndTeamId = teamMemberRepository.findByPlayerNumAndTeamId(
                playerNum, teamId);
        if (byPlayerNumAndTeamId != null) {
            throw new FieldErrorException(FieldError.of("playerNum", DUPLICATE_NUM_ERROR));
        }
    }

    /**
     * 정보 업데이트 시 등번호의 유효성을 검사합니다(입력 여부 & 자기자신 외 팀 내 중복 번호 유무)
     *
     * @param teamId
     * @param playerNum
     */
    public void validatePlayerNum(Long teamId, Integer playerNum, Long ownTeamMemId) {
        if (playerNum == null) {
            throw new FieldErrorException(FieldError.of("playerNum", NO_NUM_INPUT_ERROR));
        }

        TeamMember teamMember = teamMemberRepository.findByPlayerNumAndTeamId(
                playerNum, teamId);
        if (teamMember != null && !Objects.equals(teamMember.getId(), ownTeamMemId)) {
            throw new FieldErrorException(FieldError.of("playerNum", DUPLICATE_NUM_ERROR));
        }
    }

    public TeamMemberDetailDto getTeamMemberDetailDto(Long teamMemId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemId).orElseThrow(EntityNotFoundException::new);
        return TeamMemberDetailDto.of(teamMember);
    }

    /**
     * 팀원 삭제
     *
     * @param teamMemId
     */
    @Transactional
    public void deleteTeamMemById(Long teamMemId) {
        teamMemberRepository.deleteById(teamMemId);
    }

    /**
     * 해당 회원이 편집 권한이 있는 직책인지 알려줍니다.
     *
     * @param memberId
     * @param teamId
     * @return
     */
    public boolean isAuthorizeMemberToManageTeam(Long memberId, Long teamId) {
        TeamMember teamMem = teamMemberRepository.findByMemberIdAndTeamId(memberId, teamId);
        TeamRole teamRole = teamMem.getTeamRole();
        return teamRole.isAuthorizedToManageTeam();
    }

    /**
     * 편집 요청한 팀원들의 정보를 받아 update 합니다.
     *
     * @param teamMemberUpdateDtos
     */
    @Transactional
    public void updateTeamMems(List<TeamMemberUpdateDto> teamMemberUpdateDtos) {
        List<Long> teamMemIds = teamMemberUpdateDtos.stream().map(TeamMemberUpdateDto::getTeamMemId).toList();
        List<TeamMember> allById = teamMemberRepository.findAllById(teamMemIds);

        Map<Long, TeamMember> forSorting = new HashMap<>();
        for (TeamMember teamMember : allById) {
            forSorting.put(teamMember.getId(), teamMember);
        }
        List<TeamMember> sortedTeamMembers = teamMemIds.stream()
                .map(forSorting::get)
                .toList();
        for (int i = 0; i < sortedTeamMembers.size(); i++) {
            TeamMember teamMember = sortedTeamMembers.get(i);
            teamMember.updateBy(teamMemberUpdateDtos.get(i));
        }
    }

    /*
     * 나중 블로그 정리
     * */
    @Transactional
    public void updateTeamMems2(List<TeamMemberUpdateDto> teamMemberUpdateDtos) {
        for (TeamMemberUpdateDto teamMemberUpdateDto : teamMemberUpdateDtos) {
            TeamMember teamMember = teamMemberRepository.findById(teamMemberUpdateDto.getTeamMemId())
                    .orElseThrow(EntityNotFoundException::new);
            teamMember.updateBy(teamMemberUpdateDto);
        }
    }

    public boolean isTeamLeader(Long teamMemId) {
        TeamMember teamMember = teamMemberRepository.findById(teamMemId).orElseThrow(EntityNotFoundException::new);
        return teamMember.getTeamRole() == TeamRole.LEADER;
    }
}
