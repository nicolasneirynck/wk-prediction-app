package com.example.wk_prognose.service;

import com.example.wk_prognose.dto.request.InputMatchDTO;
import com.example.wk_prognose.dto.response.MatchDTO;
import com.example.wk_prognose.model.Location;
import com.example.wk_prognose.model.Match;
import com.example.wk_prognose.repository.LocationRepository;
import com.example.wk_prognose.repository.MatchRepository;
import com.example.wk_prognose.util.MatchStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class MatchService {
    private final MatchRepository matchRepository;
    private final LocationRepository locationRepository;

    public List<MatchDTO> findAllMatches(){
        List<Match> matches = matchRepository.findAllByOrderByMatchDateTimeAsc();

        return matches.stream()
                .map(this::toDTO)
                .toList();
    }

    public Optional<MatchDTO> findMatchById(long id) {
        return matchRepository.findById(id)
                .map(this::toDTO); // .map loopt enkel als !match.isEmpty(), anders returned hij een empty Optional
    }

    public void addMatch(InputMatchDTO inputMatchDTO){
        Location location = locationRepository.findById(inputMatchDTO.locationId())
                .orElseThrow(() -> new IllegalArgumentException("No location found with this id"));
        Match match = new Match(inputMatchDTO.homeCountry(),inputMatchDTO.awayCountry(),inputMatchDTO.matchDateTime()
                ,location,inputMatchDTO.stadiumCode(),inputMatchDTO.checksum(),inputMatchDTO.matchStage());

        matchRepository.save(match);
    }

    public void editMatch(Long id, InputMatchDTO inputMatchDTO){
        Match match = matchRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("No match found with this id"));

        Location location = locationRepository.findById(inputMatchDTO.locationId())
                .orElseThrow(() -> new IllegalArgumentException("No location found with this id"));

        match.update(
                inputMatchDTO.homeCountry(),
                inputMatchDTO.awayCountry(),
                inputMatchDTO.matchDateTime(),
                location,
                inputMatchDTO.stadiumCode(),
                inputMatchDTO.checksum(),
                inputMatchDTO.matchStage()
        );

        MatchStatus updatedMatchStatus = match.getStatus();
        boolean canUpdateScore = updatedMatchStatus == MatchStatus.AWAITING_RESULT
                || updatedMatchStatus == MatchStatus.COMPLETED;
        boolean hasAnyScore = inputMatchDTO.homeScore() != null || inputMatchDTO.awayScore() != null;
        boolean isMissingOneScore = inputMatchDTO.homeScore() == null || inputMatchDTO.awayScore() == null;

        if (!canUpdateScore && hasAnyScore) {
            throw new IllegalArgumentException("Scores can only be entered when the match is awaiting result or completed.");
        }

        if (canUpdateScore && hasAnyScore) {
            if (isMissingOneScore) {
                throw new IllegalArgumentException("Both scores are required.");
            }

            match.updateFinalScore(inputMatchDTO.homeScore(), inputMatchDTO.awayScore());
        }

        matchRepository.save(match);

    }

    public List<Location> findAllLocations() {
        return locationRepository.findAllByOrderByCountryAscCityAscStadiumNameAsc();
    }

    private MatchDTO toDTO(Match match){
        return new MatchDTO(
                match.getId(),
                match.getHomeCountry().getDisplayName(), match.getHomeCountry().getIsoCode(), match.getHomeCountry(),
                match.getAwayCountry().getDisplayName(), match.getAwayCountry().getIsoCode(), match.getAwayCountry(), match.getMatchDateTime(),
                match.getLocation().getId(), match.getLocation().getCity(), match.getLocation().getCountry().name(), match.getLocation().getStadiumName(),
                match.getMatchStage(), match.getStadiumCode(), match.getChecksum(), match.getFinalHomeScore(),match.getFinalAwayScore(),
                match.getStatus());
    }
}
