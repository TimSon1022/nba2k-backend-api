package com.timothyson.nba2kbackendapi.player;

import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/players")
public class PlayerController {

    private final PlayerRepository repo;

    public PlayerController(PlayerRepository repo) {
        this.repo = repo;
    }

    @GetMapping
    public List<Player> getAll() {
        return repo.findAll();
    }

    @PostMapping
    public Player create(@RequestBody Player player) {
        return repo.save(player);
    }
}
