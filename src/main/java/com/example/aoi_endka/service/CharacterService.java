package com.example.aoi_endka.service;

import com.example.aoi_endka.cache.SimpleCashe;
import com.example.aoi_endka.exceptions.DatabaseOperationException;
import com.example.aoi_endka.exceptions.InvalidInputException;
import com.example.aoi_endka.exceptions.ResourceNotFoundException;
import com.example.aoi_endka.interfaces.Combatant;
import com.example.aoi_endka.interfaces.Progressable;
import com.example.aoi_endka.model.GameEntity;
import com.example.aoi_endka.model.Mage;
import com.example.aoi_endka.model.Rogue;
import com.example.aoi_endka.model.Warrior;
import com.example.aoi_endka.repository.CharacterRepository;

import java.util.List;
import com.example.aoi_endka.patterns.singleton.LoggingService;
import com.example.aoi_endka.patterns.singleton.ConfigurationManager;
import org.springframework.stereotype.Service;


@Service
public class CharacterService {
    private static final String CACHE_KEY_ALL = "characters:all";
    private final SimpleCashe cache = SimpleCashe.getInstance();
    private final LoggingService logger = LoggingService.getInstance();
    private final ConfigurationManager config = ConfigurationManager.getInstance();
    private CharacterRepository characterRepository;

    public CharacterService() {
        this.characterRepository = new CharacterRepository();
    }


    public int createCharacter(GameEntity entity) throws InvalidInputException, DatabaseOperationException {
        logger.info("Creating character: " + entity.getName());
        cache.invalidate(CACHE_KEY_ALL);

        // Валидация с использованием конфигурации
        int maxLevel = config.getIntProperty("character.max.level", 100);
        if (entity.getLevel() > maxLevel) {
            logger.error("Level exceeds maximum: " + maxLevel);
            throw new InvalidInputException("Level cannot exceed " + maxLevel);
        }

        validateCharacter(entity);


        int id = characterRepository.create(entity);
        logger.info("Character created successfully with ID: " + id);

        return id;
    }
    /**
     * Get all characters
     */
    public List<GameEntity> getAllCharacters() {

        return cache.get(CACHE_KEY_ALL, List.class)
                .map(v -> (List<GameEntity>) v)
                .orElseGet(() -> {
                    try {
                        List<GameEntity> list = characterRepository.getAll();
                        cache.put(CACHE_KEY_ALL, list);
                        return list;
                    } catch (DatabaseOperationException e) {
                        throw new RuntimeException(e);
                    }
                });
    }
    /**
     * Get character by ID
     */
    public GameEntity getCharacterById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        if (id <= 0) {
            throw new ResourceNotFoundException("Invalid character ID: " + id);
        }
        return characterRepository.getById(id);
    }


    public void updateCharacter(int id, GameEntity character) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        validateCharacter(character);
        characterRepository.update(id, character);
        cache.invalidate(CACHE_KEY_ALL);
    }


    public void deleteCharacter(int id) throws DatabaseOperationException, ResourceNotFoundException {
        characterRepository.delete(id);
        cache.invalidate(CACHE_KEY_ALL);
    }


    public void levelUpCharacter(int id) throws DatabaseOperationException, ResourceNotFoundException {
        GameEntity character = characterRepository.getById(id);
        character.levelUp();
        characterRepository.update(id, character);
    }


    public void addExperience(int id, int xp) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        if (xp <= 0) {
            throw new InvalidInputException("Experience points must be positive");
        }

        GameEntity character = characterRepository.getById(id);

        if (character instanceof Progressable) {
            ((Progressable) character).gainExperience(xp);
            characterRepository.update(id, character);
        }
    }


    public void demonstratePolymorphism() throws DatabaseOperationException {
        List<GameEntity> characters = characterRepository.getAll();

        System.out.println("\n========== POLYMORPHISM DEMONSTRATION ==========");
        for (GameEntity character : characters) {
            // Polymorphic method calls
            character.displayInfo();
            System.out.println("Calculated Power: " + character.calculatePower());
            System.out.println();
        }
    }


    public void demonstrateCombat(int attackerId, int defenderId) throws DatabaseOperationException, ResourceNotFoundException {
        GameEntity attacker = characterRepository.getById(attackerId);
        GameEntity defender = characterRepository.getById(defenderId);

        if (attacker instanceof Combatant && defender instanceof Combatant) {
            System.out.println("\n========== COMBAT SIMULATION ==========");
            Combatant attackerCombat = (Combatant) attacker;
            Combatant defenderCombat = (Combatant) defender;

            int attackDamage = attackerCombat.attack();
            int defense = defenderCombat.defend();

            int netDamage = Math.max(0, attackDamage - defense);
            System.out.println("Net damage dealt: " + netDamage);
        }
    }


    private void validateCharacter(GameEntity character) throws InvalidInputException {
        if (character == null) {
            throw new InvalidInputException("Character cannot be null");
        }

        if (character.getName() == null || character.getName().trim().isEmpty()) {
            throw new InvalidInputException("Character name cannot be empty");
        }

        if (character.getName().length() < 3 || character.getName().length() > 50) {
            throw new InvalidInputException("Character name must be between 3 and 50 characters");
        }

        if (character.getLevel() < 1 || character.getLevel() > 100) {
            throw new InvalidInputException("Character level must be between 1 and 100");
        }

        if (character.getExperience() < 0) {
            throw new InvalidInputException("Experience cannot be negative");
        }

        // Type-specific validation
        if (character instanceof Warrior) {
            Warrior w = (Warrior) character;
            if (w.getStrength() <= 0 || w.getArmor() < 0) {
                throw new InvalidInputException("Warrior stats must be valid (strength > 0, armor >= 0)");
            }
        } else if (character instanceof Mage) {
            Mage m = (Mage) character;
            if (m.getMana() <= 0 || m.getIntelligence() <= 0) {
                throw new InvalidInputException("Mage stats must be valid (mana > 0, intelligence > 0)");
            }
        } else if (character instanceof Rogue) {
            Rogue r = (Rogue) character;
            if (r.getAgility() <= 0 || r.getStealth() <= 0) {
                throw new InvalidInputException("Rogue stats must be valid (agility > 0, stealth > 0)");
            }
            if (r.getCriticalChance() < 0 || r.getCriticalChance() > 1) {
                throw new InvalidInputException("Critical chance must be between 0 and 1");
            }
        }
    }
    public void clearCache() {
        cache.invalidate(CACHE_KEY_ALL);
    }
}
