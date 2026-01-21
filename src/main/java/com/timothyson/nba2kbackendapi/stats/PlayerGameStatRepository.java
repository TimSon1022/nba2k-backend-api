package com.timothyson.nba2kbackendapi.stats;

import org.springframework.data.jpa.repository.JpaRepository;

public interface PlayerGameStatRepository extends JpaRepository<PlayerGameStat, Long> {
}
