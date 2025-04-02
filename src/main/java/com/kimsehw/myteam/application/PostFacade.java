package com.kimsehw.myteam.application;

import com.kimsehw.myteam.dto.member.MyTeamsInfoDto;
import com.kimsehw.myteam.dto.post.PostDto;
import com.kimsehw.myteam.dto.post.PostFormDto;
import com.kimsehw.myteam.dto.post.PostSearchDto;
import com.kimsehw.myteam.entity.team.Team;
import com.kimsehw.myteam.service.MemberService;
import com.kimsehw.myteam.service.PostService;
import com.kimsehw.myteam.service.TeamService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class PostFacade {

    private final PostService postService;
    private final MemberService memberService;
    private final TeamService teamService;


    public List<MyTeamsInfoDto> findMyTeamsInfoByEmail(String email) {
        return memberService.findMyTeamsInfoByEmail(email);
    }

    public void savePost(PostFormDto postFormDto) {
        Team team = null;
        Long teamId = postFormDto.getTeamId();
        if (teamId != null) {
            team = teamService.findById(postFormDto.getTeamId());
        }

        postService.savePost(postFormDto, team);
    }

    public Page<PostDto> getPosts(PostSearchDto postSearchDto, Pageable pageable) {
        return postService.getPosts(postSearchDto, pageable);
    }
}
