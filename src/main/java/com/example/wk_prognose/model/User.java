package com.example.wk_prognose.model;

import com.example.wk_prognose.util.Role;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Getter(AccessLevel.NONE)
    private long id;

    private String firstName;
    private String lastName;
    private String email;
    private String password;

    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @Setter
    private Team team;

    @OneToOne(mappedBy = "owner")
    private Team createdTeam;

    @OneToMany(mappedBy = "user")
    private List<Prediction> predictions;

    public void removeTeam(){
        this.team = null;
    }
}
