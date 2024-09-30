package com.kjs990114.goodong.application.service.user;

import com.kjs990114.goodong.adapter.in.web.dto.UserDTO.ContributionsDTO;
import com.kjs990114.goodong.adapter.in.web.dto.UserDTO.UserContributionDTO;
import com.kjs990114.goodong.application.port.in.user.GetUserContributionUseCase;
import com.kjs990114.goodong.application.port.out.db.LoadUserPort;
import com.kjs990114.goodong.domain.user.Contribution;
import com.kjs990114.goodong.domain.user.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class LoadContributionService implements GetUserContributionUseCase {

    private final LoadUserPort loadUserPort;

    @Transactional(readOnly = true)
    @Override
    public ContributionsDTO getContributions(LoadContributionQuery loadContributionQuery) {
        User user = loadUserPort.loadByUserId(loadContributionQuery.getUserId());
        List<Contribution> contributionList = user.getContributions();
        return new ContributionsDTO(contributionList.stream()
                .map(contribution -> new UserContributionDTO(contribution.getDate(),contribution.getCount())).toList());
    }
}
