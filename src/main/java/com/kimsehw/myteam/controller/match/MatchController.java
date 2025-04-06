package com.kimsehw.myteam.controller.match;

import com.kimsehw.myteam.application.MatchFacade;
import com.kimsehw.myteam.constant.search.SearchDateType;
import com.kimsehw.myteam.dto.match.MatchDto;
import com.kimsehw.myteam.dto.match.MatchSearchDto;
import java.security.Principal;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
@RequiredArgsConstructor
public class MatchController {

    public static final int MAX_MATCH_SHOW = 10;
    private final MatchFacade matchFacade;

    @GetMapping("/matches")
    public String matches(Model model, Principal principal, @RequestParam(value = "page", defaultValue = "0") int page,
                          @ModelAttribute("matchSearch") MatchSearchDto matchSearchDto) {
        String email = principal.getName();
        PageRequest pageable = PageRequest.of(page, MAX_MATCH_SHOW);
        Page<MatchDto> matchDtoPages = matchFacade.getSearchedMyMatchDtoPages(email, pageable, matchSearchDto);
        model.addAttribute("matches", matchDtoPages);
        model.addAttribute("searchDateTypes", SearchDateType.values());
        model.addAttribute("maxPage", MAX_MATCH_SHOW);
        model.addAttribute("page", pageable.getPageNumber());
        return "match/matchList";
    }
}
