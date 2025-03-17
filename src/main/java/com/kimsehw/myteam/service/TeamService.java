package com.kimsehw.myteam.service;

import com.kimsehw.myteam.dto.team.TeamFormDto;
import com.kimsehw.myteam.dto.team.TeamInfoDto;
import com.kimsehw.myteam.entity.Member;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.entity.team.TeamLogo;
import com.kimsehw.myteam.repository.TeamLogoRepository;
import com.kimsehw.myteam.repository.TeamRepository;
import java.io.IOException;
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

    private final TeamRepository teamRepository;
    private final TeamLogoRepository teamLogoRepository;
    private final FileService fileService;

    @Value("${logoImgLocation}")
    private String logoImgLocation;

    @Transactional
    public Team saveTeam(Member member, TeamFormDto teamFormDto, MultipartFile teamLogoFile) {
        if (teamRepository.findByMemberIdAndTeamName(member.getId(), teamFormDto.getTeamName()) != null) {
            throw new IllegalStateException(DUPLICATE_TEAM_NAME_EXCEPTION);
        }

        Team team = Team.createTeam(teamFormDto, member);
        teamRepository.save(team);

        TeamLogo teamLogo = TeamLogo.of(team);
        if (teamLogoFile != null & !teamLogoFile.isEmpty()) {
            saveTeamLogo(teamLogo, teamLogoFile);
        }
        return team;
    }

    public Team findTeamByMemberId(Long memberId) {
        return teamRepository.findByMemberId(memberId);
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

        teamLogo.updateItemImg(originImgName, imgUrl, imgName);
        teamLogoRepository.save(teamLogo);
    }

    public TeamInfoDto getTeamInfoDtoOf(Long teamId) {
        Team team = teamRepository.findWithTeamMembersAndTeamLogoById(teamId);
        return TeamInfoDto.of(team);
    }
}
