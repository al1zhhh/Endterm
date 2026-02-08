package controller;

import model.Guild;
import service.GuildService;
import exceptions.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Guild operations
 */
@RestController
@RequestMapping("/api/guilds")
@CrossOrigin(origins = "*")
public class GuildRestController {

    @Autowired
    private GuildService guildService;

    /**
     * GET /api/guilds - Get all guilds
     */
    @GetMapping
    public ResponseEntity<List<Guild>> getAllGuilds() {
        try {
            List<Guild> guilds = guildService.getAllGuilds();
            return ResponseEntity.ok(guilds);
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/guilds/{id} - Get guild by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<Guild> getGuildById(@PathVariable int id) {
        try {
            Guild guild = guildService.getGuildById(id);
            return ResponseEntity.ok(guild);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/guilds - Create new guild
     */
    @PostMapping
    public ResponseEntity<?> createGuild(@RequestBody Map<String, String> guildData) {
        try {
            String name = guildData.get("name");
            Guild guild = new Guild(name);

            int id = guildService.createGuild(guild);
            guild.setId(id);

            return ResponseEntity.status(HttpStatus.CREATED).body(guild);
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create guild"));
        }
    }

    /**
     * PUT /api/guilds/{id} - Update guild
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateGuild(@PathVariable int id, @RequestBody Guild guild) {
        try {
            guildService.updateGuild(id, guild);
            return ResponseEntity.ok(guild);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/guilds/{id} - Delete guild
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteGuild(@PathVariable int id) {
        try {
            guildService.deleteGuild(id);
            return ResponseEntity.ok(Map.of("message", "Guild deleted successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DatabaseOperationException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/guilds/{guildId}/members/{characterId} - Add character to guild
     */
    @PostMapping("/{guildId}/members/{characterId}")
    public ResponseEntity<?> addMember(
            @PathVariable int guildId,
            @PathVariable int characterId) {
        try {
            guildService.addCharacterToGuild(characterId, guildId);
            return ResponseEntity.ok(Map.of("message", "Character added to guild successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * DELETE /api/guilds/members/{characterId} - Remove character from guild
     */
    @DeleteMapping("/members/{characterId}")
    public ResponseEntity<?> removeMember(@PathVariable int characterId) {
        try {
            guildService.removeCharacterFromGuild(characterId);
            return ResponseEntity.ok(Map.of("message", "Character removed from guild"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}