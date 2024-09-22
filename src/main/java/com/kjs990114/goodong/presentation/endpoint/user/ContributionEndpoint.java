package com.kjs990114.goodong.presentation.endpoint.user;

import com.kjs990114.goodong.application.user.UserService;
import com.kjs990114.goodong.presentation.common.CommonResponseEntity;
import com.kjs990114.goodong.presentation.dto.UserDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/contributions")
public class ContributionEndpoint {
    private final UserService userService;

    @GetMapping
    public CommonResponseEntity<List<UserDTO.UserContribution>> getContributionList(
            @RequestParam("userId") Long userId
    ) {
        return new CommonResponseEntity<>(userService.getContributionList(userId));
    }
}
