package com.codestates.movie_party.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import javax.persistence.*;
import java.time.LocalDateTime;

@NoArgsConstructor
@Getter
@Setter
@Entity
public class MovieParty {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long MoviePartyId;
    @Column(nullable = false, length = 200)
    private String title;
    @Column(nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd'T'HH:mm")
    private LocalDateTime meetingDate;
    @Column(nullable = false, length = 250)
    private String location;
    @Column(nullable = false)
    private Integer maxCapacity;
    @Column(nullable = false, length = 2000)
    private String content;
    @Column(nullable = false, length = 200)
    private String movieTitle;
}
