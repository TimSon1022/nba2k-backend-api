package com.timothyson.nba2kbackendapi.stats;

import com.timothyson.nba2kbackendapi.player.Player;
import com.timothyson.nba2kbackendapi.player.PlayerRepository;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/stats")
public class PlayerGameStatController {

    private final PlayerGameStatRepository statRepo;
    private final PlayerRepository playerRepo;

    public PlayerGameStatController(PlayerGameStatRepository statRepo, PlayerRepository playerRepo) {
        this.statRepo = statRepo;
        this.playerRepo = playerRepo;
    }

    @GetMapping
    public List<PlayerGameStat> all() {
        return statRepo.findAll();
    }

    @PostMapping("/{playerId}")
    public PlayerGameStat create(@PathVariable Long playerId, @RequestBody PlayerGameStat stat) {
        Player player = playerRepo.findById(playerId).orElseThrow();
        stat.setPlayer(player);
        return statRepo.save(stat);
    }
}
