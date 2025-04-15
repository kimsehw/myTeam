package com.kimsehw.myteam.service;

import com.kimsehw.myteam.domain.entity.Member;
import com.kimsehw.myteam.domain.entity.TeamMember;
import com.kimsehw.myteam.domain.entity.team.Team;
import com.kimsehw.myteam.domain.entity.team.TeamLogo;
import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.repository.TeamLogoRepository;
import com.kimsehw.myteam.repository.TeamRepository;
import jakarta.persistence.EntityNotFoundException;
import java.io.IOException;
import java.util.List;
import lombok.RequiredArgsConstructor;
import lombok.extern.java.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;
import org.thymeleaf.util.StringUtils;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
@Log
public class TeamService {

    public static final String DUPLICATE_TEAM_NAME_EXCEPTION = "같은 이름의 팀이 존재합니다. 같은 이름의 팀은 한 개 이상 만들 수 없습니다.";
    public static final String THERE_IS_NO_TEAMS_LIKE_TEAM_NAME_ERROR = "존재하지 않는 팀명입니다. 팀명을 확인해주세요.";
    public static final String NO_TEAM_NAME_INPUT_ERROR = "팀명을 입력해주세요.";

    private final TeamRepository teamRepository;
    private final TeamLogoRepository teamLogoRepository;
    private final FileService fileService;

    @Value("${logoImgLocation}")
    private String logoImgLocation;

    @Transactional
    public Team saveTeam(TeamMember leader, TeamFormDto teamFormDto, MultipartFile teamLogoFile) {
        if (teamRepository.findByMemberIdAndTeamName(leader.getMember().getId(), teamFormDto.getTeamName()) != null) {
            throw new IllegalStateException(DUPLICATE_TEAM_NAME_EXCEPTION);
        }

        Team team = Team.createTeam(teamFormDto, leader);
        teamRepository.save(team);

        TeamLogo teamLogo = TeamLogo.of(team);
        if (teamLogoFile != null && !teamLogoFile.isEmpty()) {
            saveTeamLogo(teamLogo, teamLogoFile);
        }
        return team;
    }

    @Transactional
    public void saveTeamLogo(TeamLogo teamLogo, MultipartFile teamLogoFile) {
        String originImgName = teamLogoFile.getOriginalFilename();
        String imgName = "";
        String imgUrl = "";

        //파일 업로드
        if (!StringUtils.isEmpty(originImgName)) {
            try {
                imgName = fileService.uploadFile(logoImgLocation, originImgName, teamLogoFile.getBytes());
            } catch (IOException e) {
                log.info("파일 업로드 2");
                throw new RuntimeException("파일 업로드 중 오류 발생");
            }
            imgUrl = "/images/logoimgs/" + imgName;
        }

        teamLogo.updateLogoImg(originImgName, imgUrl, imgName);
        teamLogoRepository.save(teamLogo);
    }

    public TeamInfoDto getTeamInfoDtoOf(Long teamId) {
        Team team = teamRepository.findWithTeamMembersAndTeamLogoById(teamId);
        return TeamInfoDto.of(team);
    }

    @Transactional
    public void updateTeam(Member member, TeamInfoDto updateTeamInfoDto, MultipartFile updateTeamLogoFile) {

        log.info(updateTeamInfoDto.toString());
        Team team = teamRepository.findById(updateTeamInfoDto.getTeamId()).orElseThrow(EntityNotFoundException::new);

        if (updateNameIsDuplicate(member, updateTeamInfoDto, team)) {
            log.info(DUPLICATE_TEAM_NAME_EXCEPTION);
            throw new IllegalStateException(DUPLICATE_TEAM_NAME_EXCEPTION);
        }

        if (updateTeamLogoFile != null & !updateTeamLogoFile.isEmpty()) {
            TeamLogo teamLogo = team.getTeamLogo();
            String pastImgName = teamLogo.getImgName();
            saveTeamLogo(teamLogo, updateTeamLogoFile);
            fileService.deleteFiles(logoImgLocation + "/" + pastImgName);
            updateTeamInfoDto.setLogoUrl(teamLogo.getImgUrl());
        }
        team.updateTeam(updateTeamInfoDto);
        log.info(team.toString());
    }

    private boolean updateNameIsDuplicate(Member member, TeamInfoDto updateTeamInfoDto, Team team) {
        return teamRepository.findByMemberIdAndTeamName(member.getId(), updateTeamInfoDto.getTeamName()) != null &&
                !updateTeamInfoDto.getTeamName().equals(team.getTeamName());
    }

    @Transactional
    public void deleteTeam(Long teamId, String logoUrl) {
        teamRepository.deleteById(teamId);
        if (logoUrl != null && !logoUrl.isEmpty()) {
            String logoImgName = getLogoImgName(logoUrl);
            fileService.deleteFiles(logoImgLocation + "/" + logoImgName);
        }
    }

    private static String getLogoImgName(String logoUrl) {
        String[] split = logoUrl.split("/");
        return split[split.length - 1];
    }

    public String getTeamName(Long teamId) {
        Team team = teamRepository.findById(teamId).orElseThrow(EntityNotFoundException::new);
        return team.getTeamName();
    }

    public Team findById(Long teamId) {
        return teamRepository.findById(teamId).orElseThrow(EntityNotFoundException::new);
    }

    public List<Team> findAllByTeamNameWithLeaderInfo(String teamName, String myEmail) {
        if (teamName.isBlank()) {
            throw new IllegalArgumentException(NO_TEAM_NAME_INPUT_ERROR);
        }
        teamName = teamName.replaceAll(" ", "");
        teamName = "%" + teamName + "%";
        List<Team> teams = teamRepository.findAllByTeamNameWithLeaderInfo(teamName);
        if (teams == null || teams.isEmpty()) {
            throw new IllegalArgumentException(THERE_IS_NO_TEAMS_LIKE_TEAM_NAME_ERROR);
        }
        teams.removeIf(team -> isInMyTeam(myEmail, team));
        return teams;
    }

    private static boolean isInMyTeam(String myEmail, Team team) {
        return team.getLeader().getMember().getEmail().equals(myEmail);
    }

    public boolean areTheyInThisTeam(List<Long> teamMemIds, Long teamId) {
        Team team = teamRepository.findByIdFetchTeamMems(teamId);
        return team.areTheyTeamMember(teamMemIds);
    }
}
