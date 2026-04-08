package com.example.wk_prognose.model;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Prediction {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Getter(AccessLevel.NONE)
    private long id;

    private int predictedHomeScore;
    private int predictedAwayScore;

    @ManyToOne(optional = false) // predictie MOET bij een user horen
    private User user;

    @ManyToOne(optional = false) // predictie MOET bij een match horen
    private Match match;

}
