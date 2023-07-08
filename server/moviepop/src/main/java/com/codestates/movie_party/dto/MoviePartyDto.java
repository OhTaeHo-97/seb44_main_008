package com.codestates.movie_party.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.*;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Positive;
import java.time.LocalDateTime;

public class MoviePartyDto {
    @Getter
    @AllArgsConstructor
    @NoArgsConstructor
    public static class Post {
        @NotBlank
        private String title;
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime meetingDate;
        @NotBlank
        private String location;
        @Positive
        private int maxCapacity;
        @NotBlank
        private String content;
        @NotBlank
        private String movieTitle;
    }

    @Getter
    @Builder
    public static class Patch {
        @Setter
        private long moviePartyId;
        @NotBlank
        private String title;
        @NotNull
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime meetingDate;
        @NotBlank
        private String location;
        @Positive
        private int maxCapacity;
        @NotBlank
        private String content;
    }

    @Getter
    @AllArgsConstructor
    public static class Response {
        private long moviePartyId;
        private String title;
        @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd'T'HH:mm", timezone = "Asia/Seoul")
        private LocalDateTime meetingDate;
        private String location;
        private int maxCapacity;
        private String content;
        private String movieTitle;
    }
}
