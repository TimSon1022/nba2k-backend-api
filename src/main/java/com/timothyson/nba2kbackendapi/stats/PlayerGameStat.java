package com.timothyson.nba2kbackendapi.stats;

import com.timothyson.nba2kbackendapi.player.Player;
import jakarta.persistence.*;

@Entity
@Table(name = "player_game_stats")
public class PlayerGameStat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(optional = false)
    @JoinColumn(name = "player_id")
    private Player player;

    @Column(nullable = false)
    private String gameLabel; // like "@ AFR" or "vs RKS"

    private Integer points;
    private Integer assists;
    private Integer rebounds;

    private Integer threesMade;
    private Integer threesAttempted;

    public PlayerGameStat() {}

    // getters/setters (generate in Eclipse)
    public Long getId() { return id; }
    public Player getPlayer() { return player; }
    public String getGameLabel() { return gameLabel; }
    public Integer getPoints() { return points; }
    public Integer getAssists() { return assists; }
    public Integer getRebounds() { return rebounds; }
    public Integer getThreesMade() { return threesMade; }
    public Integer getThreesAttempted() { return threesAttempted; }

    public void setId(Long id) { this.id = id; }
    public void setPlayer(Player player) { this.player = player; }
    public void setGameLabel(String gameLabel) { this.gameLabel = gameLabel; }
    public void setPoints(Integer points) { this.points = points; }
    public void setAssists(Integer assists) { this.assists = assists; }
    public void setRebounds(Integer rebounds) { this.rebounds = rebounds; }
    public void setThreesMade(Integer threesMade) { this.threesMade = threesMade; }
    public void setThreesAttempted(Integer threesAttempted) { this.threesAttempted = threesAttempted; }
}
