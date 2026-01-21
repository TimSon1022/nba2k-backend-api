package com.timothyson.nba2kbackendapi.importer;

import com.timothyson.nba2kbackendapi.player.Player;
import com.timothyson.nba2kbackendapi.player.PlayerRepository;
import com.timothyson.nba2kbackendapi.stats.PlayerGameStat;
import com.timothyson.nba2kbackendapi.stats.PlayerGameStatRepository;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;

@RestController
@RequestMapping("/import")
public class ExcelImportController {

    private final PlayerRepository playerRepo;
    private final PlayerGameStatRepository statRepo;

    public ExcelImportController(PlayerRepository playerRepo, PlayerGameStatRepository statRepo) {
        this.playerRepo = playerRepo;
        this.statRepo = statRepo;
    }

    @PostMapping("/player-stats")
    public ResponseEntity<String> importPlayerStats(
            @RequestParam("file") MultipartFile file,
            @RequestParam("team") String team
    ) {
        int playersCreated = 0;
        int statsInserted = 0;

        try (InputStream in = file.getInputStream();
             Workbook workbook = new XSSFWorkbook(in)) {

            for (int s = 0; s < workbook.getNumberOfSheets(); s++) {
                Sheet sheet = workbook.getSheetAt(s);
                String sheetName = sheet.getSheetName();

                // Skip non-player sheets
                if (sheetName.equalsIgnoreCase("Team Stats") ||
                    sheetName.equalsIgnoreCase("Opponent Stats")) {
                    continue;
                }

                // Player name is the sheet name
                Player player = playerRepo.findByName(sheetName).orElse(null);
                if (player == null) {
                    player = new Player();
                    player.setName(sheetName);
                    player.setTeam(team);
                    player = playerRepo.save(player);
                    playersCreated++;
                }

                // Row 0 is headers; start at row 1
                for (int r = 1; r <= sheet.getLastRowNum(); r++) {
                    Row row = sheet.getRow(r);
                    if (row == null) continue;

                    String gameLabel = getString(row.getCell(0));
                    if (gameLabel == null || gameLabel.isBlank()) continue;

                    // Skip summary rows if they exist
                    if (isExcluded(gameLabel)) continue;

                    PlayerGameStat stat = new PlayerGameStat();
                    stat.setPlayer(player);
                    stat.setGameLabel(gameLabel);

                    stat.setPoints(getInt(row.getCell(1)));           // PTS
                    stat.setRebounds(getInt(row.getCell(2)));         // REB
                    stat.setAssists(getInt(row.getCell(3)));          // AST
                    stat.setThreesMade(getInt(row.getCell(9)));       // 3PTM
                    stat.setThreesAttempted(getInt(row.getCell(10))); // 3PTA

                    statRepo.save(stat);
                    statsInserted++;
                }
            }

        } catch (Exception e) {
            return ResponseEntity.internalServerError()
                    .body("Import failed: " + e.getMessage());
        }

        return ResponseEntity.ok("Import OK. Players created: " + playersCreated +
                ", Stat rows inserted: " + statsInserted);
    }

    private static boolean isExcluded(String v) {
        String x = v.trim();
        return x.equalsIgnoreCase("AVG")
                || x.equalsIgnoreCase("TOTAL")
                || x.equalsIgnoreCase("Games")
                || x.equals("0");
    }

    private static String getString(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.STRING) return cell.getStringCellValue();
        if (cell.getCellType() == CellType.NUMERIC) return String.valueOf((int) cell.getNumericCellValue());
        return cell.toString();
    }

    private static Integer getInt(Cell cell) {
        if (cell == null) return null;
        if (cell.getCellType() == CellType.NUMERIC) return (int) cell.getNumericCellValue();
        if (cell.getCellType() == CellType.STRING) {
            String t = cell.getStringCellValue().trim();
            if (t.isEmpty()) return null;
            try { return (int) Double.parseDouble(t); } catch (Exception ignored) {}
        }
        return null;
    }
}
