package com.codestates.movie_party.service;

import com.codestates.exception.BusinessLogicException;
import com.codestates.exception.ExceptionCode;
import com.codestates.movie_party.entity.MovieParty;
import com.codestates.movie_party.repository.MoviePartyRepository;
import com.codestates.review_board.entity.ReviewBoard;
import com.codestates.user.entity.MoviePartyUser;
import com.codestates.user.entity.User;
import com.codestates.utils.UserUtils;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.util.Optional;

@Service
@Transactional
public class MoviePartyService {
    private final MoviePartyRepository moviePartyRepository;

    public MoviePartyService(MoviePartyRepository moviePartyRepository) {
        this.moviePartyRepository = moviePartyRepository;
    }

    public MovieParty createMovieParty(User user, ReviewBoard reviewBoard, MovieParty movieParty) {
        // user mapping
        user.addMovieParty(movieParty);
        movieParty.setUser(user);

        // review board mapping
        reviewBoard.addMovieParty(movieParty);
        movieParty.setReviewBoard(reviewBoard);

        MoviePartyUser moviePartyUser = new MoviePartyUser();
        moviePartyUser.setMovieParty(movieParty);
        moviePartyUser.setUser(user);
        moviePartyUser.setProfileImage(user.getProfileImage());
        movieParty.addMoviePartyUser(moviePartyUser);

        return moviePartyRepository.save(movieParty);
    }

    public MovieParty updateMovieParty(String email, MovieParty movieParty) {
        MovieParty findMovieParty = findVerifiedMoviePartyId(movieParty.getMoviePartyId());
        if(!findMovieParty.getUser().getEmail().equals(email))
            throw new BusinessLogicException(ExceptionCode.CANNOT_UPDATE_MOVIE_PARTY);

        Optional.ofNullable(movieParty.getTitle())
                .ifPresent(title -> findMovieParty.setTitle(title));
        Optional.ofNullable(movieParty.getMeetingDate())
                .ifPresent(date -> findMovieParty.setMeetingDate(date));
        Optional.ofNullable(movieParty.getLocation())
                .ifPresent(location -> findMovieParty.setLocation(location));
        Optional.ofNullable(movieParty.getMaxCapacity())
                .ifPresent(capacity -> findMovieParty.setMaxCapacity(capacity));
        Optional.ofNullable(movieParty.getContent())
                .ifPresent(content -> findMovieParty.setContent(content));

        return moviePartyRepository.save(findMovieParty);
    }

    public MovieParty findMovieParty(long groupId, User user) {
        MovieParty movieParty = findVerifiedMoviePartyId(groupId);
        Period age = UserUtils.getAge(user);

        if(age.getYears() < 19 && movieParty.getReviewBoard().getMovie().isAdulted())
            throw new BusinessLogicException(ExceptionCode.CANNOT_SHOW_MOVIE_PARTY);

        return movieParty;
    }

    public Page<MovieParty> findMovieParties(int page, int size, User user) {
        Period age = UserUtils.getAge(user);

        if(age.getYears() >= 19)
            return moviePartyRepository.findAll(PageRequest.of(
                page - 1, size, Sort.by("moviePartyId").descending())
            );
        else
            return moviePartyRepository.findAllByAdulted(false, PageRequest.of(
                    page - 1, size, Sort.by("moviePartyId").descending()
            ));
    }

    public void deleteMovieParty(String email, long moviePartyId) {
        MovieParty movieParty = findVerifiedMoviePartyId(moviePartyId);
        if(!movieParty.getUser().getEmail().equals(email))
            throw new BusinessLogicException(ExceptionCode.CANNOT_UPDATE_MOVIE_PARTY);
        moviePartyRepository.deleteById(moviePartyId);
    }

    private void verifyMoviePartyId(long moviePartyId) {
        Optional<MovieParty> optionalGroup = moviePartyRepository.findById(moviePartyId);
        optionalGroup.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MOVIE_PARTY_NOT_FOUND));
    }

    private MovieParty findVerifiedMoviePartyId(long moviePartyId) {
        Optional<MovieParty> optionalGroup = moviePartyRepository.findById(moviePartyId);
        MovieParty group = optionalGroup.orElseThrow(() -> new BusinessLogicException(ExceptionCode.MOVIE_PARTY_NOT_FOUND));

        return group;
    }
}