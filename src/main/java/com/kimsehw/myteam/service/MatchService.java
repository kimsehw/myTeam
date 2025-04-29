package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.match.MatchUpdateFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.dto.teammember.TeamMemberForAddMatchDto;
import com.kimsehw.myteam.repository.match.MatchRepository;
import jakarta.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class MatchService {

    public static final int MIN_BEFORE_HOUR = 2;
    public static final String MIN_HOUR_OVER_ERROR = "매치 신청은 최소 경기 " + MIN_BEFORE_HOUR + "시간 전에 신청해야 합니다.";
    public static final String NULL_MATCH_DATE_ERROR = "매치 날짜가 입력되지 않았습니다. 매치 날짜를 입력해주세요";
    public static final String MATCH_DATE_PARSE_ERROR = "유효하지 않은 매치 날짜가 입력되었습니다.";
    public static final String MATCH_TIME_ERROR = "시간은 필수 입력값이며 음수가 될 수 없습니다.";
    public static final String WRONG_MATCH_INFO_ERROR = "잘못된 매치 정보가 요청되었습니다. 매치 정보를 확인해주세요.";
    private final MatchRepository matchRepository;

    /**
     * 검색 조건에 맞춰 대상 팀들의 모든 일정을 조회합니다.
     *
     * @param matchSearchDto 검색 조건
     * @param TeamIds        대상 팀 아이드 set
     * @param pageable       page 정보
     * @return Page<MatchDto>
     */
    public Page<MatchDto> getSearchedMatchDtoPages(MatchSearchDto matchSearchDto, Set<Long> TeamIds,
                                                   Pageable pageable) {
        Page<Match> matchPage = matchRepository.findAllSearchedMatchPage(matchSearchDto, TeamIds, pageable);
        return matchPage.map(match -> {
            if (match.getOpposingTeam() == null) {
                TeamInfoDto teamInfoDto = new TeamInfoDto();
                teamInfoDto.setTeamName(match.getNotUserOpposingTeamName());
                teamInfoDto.setRegion(match.getNotUserOpposingTeamRegion());
                teamInfoDto.setAgeRange(match.getNotUserOpposingTeamAgeRange());
                return MatchDto.ofNotUser(match, teamInfoDto);
            }
            return MatchDto.of(match);
        });
    }

    /**
     * 검색 조건에 맞춰 해당 회원이 속한 팀들의 일정 정보와 팀 이름 및 아이디를 반환합니다.
     *
     * @param myTeams        팀 정보 리스트
     * @param matchSearchDto 검색 조건
     * @param pageable       페이지 정보
     * @return MatchListResponse
     */
    public MatchListResponse getSearchedMatchListResponse(List<TeamInfoDto> myTeams, MatchSearchDto matchSearchDto,
                                                          Pageable pageable) {
        Map<Long, String> myTeamIdsAndNames = myTeams.stream()
                .collect(Collectors.toMap(TeamInfoDto::getTeamId, TeamInfoDto::getTeamName));
        Set<Long> myTeamIds = myTeamIdsAndNames.keySet();
        Page<MatchDto> matches = getSearchedMatchDtoPages(matchSearchDto, myTeamIds, pageable);
        return new MatchListResponse(myTeamIdsAndNames, matches);
    }

    /**
     * 경기 시간 유효성 검사
     *
     * @param matchTime 경기 시간
     * @throws IllegalArgumentException 음수 혹은 null 일 경우 (errorMessage = "matchTime")
     */
    public void validateMatchTime(Integer matchTime) {
        if (matchTime == null || matchTime < 0) {
            throw new IllegalArgumentException(MATCH_TIME_ERROR);
        }
    }

    /**
     * 매치 날짜 유효성 검사
     *
     * @param matchDate 매치 날짜
     * @throws NullPointerException     매치 날짜 null 입력
     * @throws IllegalArgumentException 최소 시간 이후 신청
     * @throws DateTimeParseException   유효하지 않은 날짜 형태
     */
    public void validateMatchDate(String matchDate) {
        try {
            LocalDateTime matchDateFormat = DateTimeUtil.formatting(matchDate, DateTimeUtil.Y_M_D_H_M_TYPE);
            if (matchDateFormat == null) {
                throw new NullPointerException(NULL_MATCH_DATE_ERROR);
            }
            if (matchDateFormat.minusHours(MIN_BEFORE_HOUR).isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException(MIN_HOUR_OVER_ERROR);
            }
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(MATCH_DATE_PARSE_ERROR, DateTimeUtil.Y_M_D_H_M_TYPE, 0);
        }
    }

    /**
     * 매치를 저장합니다. (회원)
     *
     * @param myTeam       내 팀
     * @param opposingTeam 상대 팀
     * @param matchDate    매치 날짜
     * @param matchTime    매치 시간
     */
    @Transactional
    public void addMatchOn(Team myTeam, Team opposingTeam, LocalDateTime matchDate, Integer matchTime) {
        Match match = Match.createMatchOf(myTeam, opposingTeam, matchDate, matchTime);
        matchRepository.save(match);
    }

    /**
     * 매치를 저장합니다. (비회원)
     *
     * @param myTeam          내 팀
     * @param inviteeTeamName 상대 팀 이름
     * @param matchDate       매치 날짜
     * @param matchTime       매치 시간
     */
    @Transactional
    public void addNotUserMatchOn(Team myTeam, String inviteeTeamName, String matchDate, Integer matchTime) {
        Match match = Match.createNotUserMatchOf(myTeam, inviteeTeamName, matchDate, matchTime);
        matchRepository.save(match);
    }

    /**
     * 추가 인원 정보에 따라 매치 참여 인원들을 추가 및 삭제 합니다. 기존에 있었지만 추가 인원정보에 포함 안될 경우 삭제합니다.
     *
     * @param matchId    매치 아이디
     * @param addMembers 추가 인원
     */
    @Transactional
    public void addMemberOn(Long matchId, List<TeamMember> addMembers) {
        Match match = matchRepository.findByIdFetchAll(matchId);
        match.addMembers(addMembers);
    }

    public List<TeamMemberForAddMatchDto> getTeamMemberForAddMatchDto(Long matchId, List<TeamMember> teamMembers) {
        Match match = matchRepository.findByIdFetchAll(matchId);
        return teamMembers.stream()
                .map(teamMember -> new TeamMemberForAddMatchDto(teamMember.getId(), teamMember.getPlayerNum(),
                        teamMember.getName(), match.isAlreadyIn(teamMember.getId())))
                .toList();
    }

    @Transactional
    public void updateBy(MatchUpdateFormDto matchUpdateFormDto) {
        Match match = getMatchById(matchUpdateFormDto.getId());
        match.update(matchUpdateFormDto);
    }

    private Match getMatchById(Long matchId) {
        return matchRepository.findById(matchId).orElseThrow(() -> new EntityNotFoundException(WRONG_MATCH_INFO_ERROR));
    }

    public void validateIsMatchOfTeam(Long matchId, Long teamId) {
        Match match = getMatchById(matchId);
        if (!match.getMyTeam().getId().equals(teamId)) {
            throw new IllegalArgumentException(WRONG_MATCH_INFO_ERROR);
        }
    }
}
