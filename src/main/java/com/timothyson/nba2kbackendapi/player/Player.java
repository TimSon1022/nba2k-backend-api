package com.timothyson.nba2kbackendapi.player;

import jakarta.persistence.*;

@Entity
@Table(name = "players")
public class Player {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Sheet name (player name)
    @Column(nullable = false, unique = true)
    private String name;

    // Your custom team (Team 1, Team 2, etc.)
    @Column(nullable = false)
    private String team;

    // Optional – you can fill this later or leave null
    private String position;

    public Player() {
    }

    public Player(String name, String team) {
        this.name = name;
        this.team = team;
    }

    public Player(String name, String team, String position) {
        this.name = name;
        this.team = team;
        this.position = position;
    }

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getTeam() {
        return team;
    }

    public String getPosition() {
        return position;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public void setPosition(String position) {
        this.position = position;
    }
}
