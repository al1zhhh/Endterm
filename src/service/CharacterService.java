package service;

import interfaces.Combatant;
import interfaces.Progressable;
import model.*;
import repository.CharacterRepository;
import exceptions.*;

import java.util.List;


public class CharacterService {
    private CharacterRepository characterRepository;

    public CharacterService() {
        this.characterRepository = new CharacterRepository();
    }


    public int createCharacter(GameEntity character) throws InvalidInputException, DatabaseOperationException {
        // Validation
        validateCharacter(character);

        // Check for duplicate name
        try {
            List<GameEntity> existingCharacters = characterRepository.getAll();
            for (GameEntity existing : existingCharacters) {
                if (existing.getName().equalsIgnoreCase(character.getName())) {
                    throw new DuplicateResourceException("Character with name '" + character.getName() + "' already exists");
                }
            }
        } catch (DatabaseOperationException e) {
            throw e;
        }

        // Create character
        return characterRepository.create(character);
    }

    /**
     * Get all characters
     */
    public List<GameEntity> getAllCharacters() throws DatabaseOperationException {
        return characterRepository.getAll();
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
    }


    public void deleteCharacter(int id) throws DatabaseOperationException, ResourceNotFoundException {
        characterRepository.delete(id);
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
}
