package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.match.Match;
import com.kimsehw.myteam.domain.utill.DateTimeUtil;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchListResponse;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.repository.match.MatchRepository;
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
    private final MatchRepository matchRepository;

    public Page<MatchDto> getSearchedMatchDtoPages(MatchSearchDto matchSearchDto, Set<Long> TeamIds,
                                                   Pageable pageable) {
        Page<Match> matchPage = matchRepository.findAllSearchedMatchPage(matchSearchDto, TeamIds, pageable);
        return matchPage.map(MatchDto::of);
    }

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
            LocalDateTime matchDateFormat = DateTimeUtil.formatting(matchDate, "yyyy-MM-dd'T'HH:mm");
            if (matchDateFormat == null) {
                throw new NullPointerException(NULL_MATCH_DATE_ERROR);
            }
            if (matchDateFormat.minusHours(MIN_BEFORE_HOUR).isBefore(LocalDateTime.now())) {
                throw new IllegalArgumentException(MIN_HOUR_OVER_ERROR);
            }
        } catch (DateTimeParseException e) {
            throw new DateTimeParseException(MATCH_DATE_PARSE_ERROR, "yyyy-MM-dd'T'HH:mm", 0);
        }


    }
}
