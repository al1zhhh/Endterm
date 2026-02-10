package com.example.aoi_endka.controller;

import com.example.aoi_endka.exceptions.DatabaseOperationException;
import com.example.aoi_endka.exceptions.InvalidInputException;
import com.example.aoi_endka.exceptions.ResourceNotFoundException;
import com.example.aoi_endka.model.GameEntity;
import com.example.aoi_endka.service.CharacterService;
import com.example.aoi_endka.patterns.factory.CharacterFactory;
import com.example.aoi_endka.patterns.builder.CharacterBuilder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * REST Controller for Character operations
 * Handles HTTP requests for character management
 */
@RestController
@RequestMapping("/api/characters")
@CrossOrigin(origins = "*")
public class CharacterRestController {

    @Autowired
    private CharacterService characterService;

    /**
     * GET /api/characters - Get all characters
     */
    @GetMapping
    public ResponseEntity<List<GameEntity>> getAllCharacters() {
        try {
            List<GameEntity> characters = characterService.getAllCharacters();
            return ResponseEntity.ok(characters);
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * GET /api/characters/{id} - Get character by ID
     */
    @GetMapping("/{id}")
    public ResponseEntity<GameEntity> getCharacterById(@PathVariable int id) {
        try {
            GameEntity character = characterService.getCharacterById(id);
            return ResponseEntity.ok(character);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (DatabaseOperationException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * POST /api/characters - Create new character using Factory pattern
     */
    @PostMapping
    public ResponseEntity<?> createCharacter(@RequestBody Map<String, Object> characterData) {
        try {
            String type = (String) characterData.get("type");
            String name = (String) characterData.get("name");
            int level = (int) characterData.get("level");

            // Using Factory Pattern
            GameEntity character = CharacterFactory.createCharacter(type, name, level);

            int id = characterService.createCharacter(character);
            character.setId(id);

            return ResponseEntity.status(HttpStatus.CREATED).body(character);
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(Map.of("error", "Failed to create character"));
        }
    }

    /**
     * POST /api/characters/builder - Create character using Builder pattern
     */
    @PostMapping("/builder")
    public ResponseEntity<?> createCharacterWithBuilder(@RequestBody Map<String, Object> data) {
        try {
            String name = (String) data.get("name");
            int level = (int) data.get("level");
            String type = (String) data.get("type");

            // Using Builder Pattern
            CharacterBuilder builder = new CharacterBuilder(name, level, type);

            if (type.equalsIgnoreCase("WARRIOR")) {
                builder.withStrength((int) data.get("strength"))
                        .withArmor((int) data.get("armor"))
                        .withWeaponType((String) data.get("weaponType"));
            } else if (type.equalsIgnoreCase("MAGE")) {
                builder.withMana((int) data.get("mana"))
                        .withIntelligence((int) data.get("intelligence"))
                        .withSpellSchool((String) data.get("spellSchool"));
            } else if (type.equalsIgnoreCase("ROGUE")) {
                builder.withAgility((int) data.get("agility"))
                        .withStealth((int) data.get("stealth"))
                        .withCriticalChance((double) data.get("criticalChance"));
            }

            GameEntity character = builder.build();
            int id = characterService.createCharacter(character);
            character.setId(id);

            return ResponseEntity.status(HttpStatus.CREATED).body(character);
        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        }
    }

    /**
     * PUT /api/characters/{id} - Update character
     */
    @PutMapping("/{id}")
    public ResponseEntity<?> updateCharacter(@PathVariable int id, @RequestBody GameEntity character) {
        try {
            characterService.updateCharacter(id, character);
            return ResponseEntity.ok(character);
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (InvalidInputException e) {
            return ResponseEntity.badRequest().body(Map.of("error", e.getMessage()));
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    /**
     * DELETE /api/characters/{id} - Delete character
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteCharacter(@PathVariable int id) {
        try {
            characterService.deleteCharacter(id);
            return ResponseEntity.ok(Map.of("message", "Character deleted successfully"));
        } catch (ResourceNotFoundException e) {
            return ResponseEntity.notFound().build();
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }



}