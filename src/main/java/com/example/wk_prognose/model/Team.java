package com.example.wk_prognose.model;

import jakarta.persistence.*;
import lombok.*;
import java.util.List;

@Entity
@EqualsAndHashCode(of = "name")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
public class Team {

    public Team(String name, User owner, String inviteCode){
        this.name = name;
        this.owner = owner;
        this.inviteCode = inviteCode;
    }

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    //@Getter(AccessLevel.NONE)
    private long id;

    private String name;
    @Setter private String inviteCode;

    @OneToOne
    private User owner;

    @OneToMany(mappedBy = "team")
    private List<User> users;
}
