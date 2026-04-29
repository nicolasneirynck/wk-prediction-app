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

    @Column(nullable = false, unique = true)
    private String email;

    @Column(nullable = false)
    private String password;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private Role role;

    @ManyToOne
    @Setter
    private Team team;

    @OneToOne(mappedBy = "owner")
    private Team createdTeam;

    @OneToMany(mappedBy = "user")
    private List<Prediction> predictions;

    public User(String firstName, String lastName, String email, String password, Role role){
        // TODO -> error handling + miss builder?
        this.firstName = firstName;
        this.lastName = lastName;
        this.email = email;
        this.password = password;
        this.role = role;
    }

    public String getDisplayName(){
        return firstName + " " + lastName;
    }

    public void removeTeam(){
        this.team = null;
    }
}
