package controller;

import interfaces.Combatant;
import interfaces.Progressable;
import model.*;
import repository.CharacterRepository;
import service.CharacterService;
import exceptions.*;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;


public class CharacterController {
    private CharacterService characterService;
    private Scanner scanner;

    public CharacterController() {
        this.characterService = new CharacterService();
        this.scanner = new Scanner(System.in);
    }

    public CharacterController(Scanner scanner) {
        this.characterService = new CharacterService();
        this.scanner = scanner;
    }


    public void createCharacter() {
        try {
            System.out.println("\n========== CREATE CHARACTER ==========");
            System.out.println("Select character type:");
            System.out.println("1. Warrior");
            System.out.println("2. Mage");
            System.out.println("3. Rogue");
            System.out.print("Choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline

            System.out.print("Enter name (3-50 characters): ");
            String name = scanner.nextLine();

            System.out.print("Enter level (1-100): ");
            int level = scanner.nextInt();
            scanner.nextLine();

            GameEntity character = null;

            switch (choice) {
                case 1:
                    character = createWarrior(name, level);
                    break;
                case 2:
                    character = createMage(name, level);
                    break;
                case 3:
                    character = createRogue(name, level);
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            if (character != null) {
                int id = characterService.createCharacter(character);
                System.out.println("\n✓ SUCCESS: Character created with ID: " + id);
                character.displayInfo();
            }

        } catch (InvalidInputException e) {
            System.err.println("✗ VALIDATION ERROR: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ UNEXPECTED ERROR: " + e.getMessage());
        }
    }


    private Warrior createWarrior(String name, int level) {
        System.out.println("\n--- Warrior Stats ---");
        System.out.print("Strength (1-100): ");
        int strength = scanner.nextInt();

        System.out.print("Armor (0-100): ");
        int armor = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Weapon Type (e.g., Sword, Axe, Hammer): ");
        String weaponType = scanner.nextLine();

        return new Warrior(name, level, strength, armor, weaponType);
    }


    private Mage createMage(String name, int level) {
        System.out.println("\n--- Mage Stats ---");
        System.out.print("Mana (1-500): ");
        int mana = scanner.nextInt();

        System.out.print("Intelligence (1-100): ");
        int intelligence = scanner.nextInt();
        scanner.nextLine();

        System.out.print("Spell School (e.g., Fire, Ice, Arcane): ");
        String spellSchool = scanner.nextLine();

        return new Mage(name, level, mana, intelligence, spellSchool);
    }


    private Rogue createRogue(String name, int level) {
        System.out.println("\n--- Rogue Stats ---");
        System.out.print("Agility (1-100): ");
        int agility = scanner.nextInt();

        System.out.print("Stealth (1-100): ");
        int stealth = scanner.nextInt();

        System.out.print("Critical Chance (0.0-1.0): ");
        double criticalChance = scanner.nextDouble();
        scanner.nextLine();

        return new Rogue(name, level, agility, stealth, criticalChance);
    }


    public void getAllCharacters() {
        try {
            System.out.println("\n========== ALL CHARACTERS ==========");
            List<GameEntity> characters = characterService.getAllCharacters();

            if (characters.isEmpty()) {
                System.out.println("No characters found in database.");
                return;
            }

            System.out.println("Total characters: " + characters.size());
            System.out.println();

            for (GameEntity character : characters) {
                character.displayInfo();
                System.out.println();
            }

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }


    public void getCharacterById() {
        try {
            System.out.print("\nEnter character ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n========== CHARACTER DETAILS ==========");
            GameEntity character = characterService.getCharacterById(id);
            character.displayInfo();

            // Show interface implementations
            System.out.println("\n--- Capabilities ---");
            if (character instanceof Combatant) {
                System.out.println("✓ Can engage in combat");
            }
            if (character instanceof Progressable) {
                System.out.println("✓ Can gain experience and level up");
            }

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }


    public void updateCharacter() {
        try {
            System.out.print("\nEnter character ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // First, get the current character
            GameEntity character = characterService.getCharacterById(id);
            System.out.println("\n--- Current Character ---");
            character.displayInfo();

            System.out.println("\n--- Update Character ---");
            System.out.print("New name (or press Enter to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                character.setName(newName);
            }

            System.out.print("New level (or 0 to keep current): ");
            int newLevel = scanner.nextInt();
            scanner.nextLine();
            if (newLevel > 0) {
                character.setLevel(newLevel);
            }

            // Update type-specific attributes
            if (character instanceof Warrior) {
                updateWarriorStats((Warrior) character);
            } else if (character instanceof Mage) {
                updateMageStats((Mage) character);
            } else if (character instanceof Rogue) {
                updateRogueStats((Rogue) character);
            }

            characterService.updateCharacter(id, character);
            System.out.println("\n✓ SUCCESS: Character updated!");
            character.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.err.println("✗ VALIDATION ERROR: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    private void updateWarriorStats(Warrior warrior) {
        System.out.print("New strength (or 0 to keep current): ");
        int strength = scanner.nextInt();
        if (strength > 0) {
            warrior.setStrength(strength);
        }

        System.out.print("New armor (or -1 to keep current): ");
        int armor = scanner.nextInt();
        if (armor >= 0) {
            warrior.setArmor(armor);
        }
        scanner.nextLine();

        System.out.print("New weapon type (or press Enter to keep current): ");
        String weapon = scanner.nextLine();
        if (!weapon.trim().isEmpty()) {
            warrior.setWeaponType(weapon);
        }
    }

    private void updateMageStats(Mage mage) {
        System.out.print("New mana (or 0 to keep current): ");
        int mana = scanner.nextInt();
        if (mana > 0) {
            mage.setMana(mana);
        }

        System.out.print("New intelligence (or 0 to keep current): ");
        int intelligence = scanner.nextInt();
        if (intelligence > 0) {
            mage.setIntelligence(intelligence);
        }
        scanner.nextLine();

        System.out.print("New spell school (or press Enter to keep current): ");
        String spell = scanner.nextLine();
        if (!spell.trim().isEmpty()) {
            mage.setSpellSchool(spell);
        }
    }

    private void updateRogueStats(Rogue rogue) {
        System.out.print("New agility (or 0 to keep current): ");
        int agility = scanner.nextInt();
        if (agility > 0) {
            rogue.setAgility(agility);
        }

        System.out.print("New stealth (or 0 to keep current): ");
        int stealth = scanner.nextInt();
        if (stealth > 0) {
            rogue.setStealth(stealth);
        }

        System.out.print("New critical chance (or -1 to keep current, 0.0-1.0): ");
        double crit = scanner.nextDouble();
        if (crit >= 0) {
            rogue.setCriticalChance(crit);
        }
        scanner.nextLine();
    }


    public void deleteCharacter() {
        try {
            System.out.print("\nEnter character ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // Show character before deletion
            GameEntity character = characterService.getCharacterById(id);
            System.out.println("\n--- Character to Delete ---");
            character.displayInfo();

            System.out.print("\nAre you sure you want to delete this character? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                characterService.deleteCharacter(id);
                System.out.println("\n✓ SUCCESS: Character deleted!");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    public void addExperience() {
        try {
            System.out.print("\nEnter character ID: ");
            int id = scanner.nextInt();

            System.out.print("Enter experience points to add: ");
            int xp = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n========== ADDING EXPERIENCE ==========");
            characterService.addExperience(id, xp);

            // Show updated character
            GameEntity character = characterService.getCharacterById(id);
            System.out.println("\n--- Updated Character ---");
            character.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.err.println("✗ VALIDATION ERROR: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }


    public void levelUpCharacter() {
        try {
            System.out.print("\nEnter character ID to level up: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n========== LEVELING UP ==========");
            characterService.levelUpCharacter(id);

            GameEntity character = characterService.getCharacterById(id);
            character.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    public void simulateCombat() {
        try {
            System.out.println("\n========== COMBAT SIMULATION ==========");
            System.out.print("Enter attacker character ID: ");
            int attackerId = scanner.nextInt();

            System.out.print("Enter defender character ID: ");
            int defenderId = scanner.nextInt();
            scanner.nextLine();

            if (attackerId == defenderId) {
                System.out.println("✗ ERROR: A character cannot fight itself!");
                return;
            }

            characterService.demonstrateCombat(attackerId, defenderId);

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }


    public void demonstratePolymorphism() {
        try {
            System.out.println("\n========== POLYMORPHISM DEMONSTRATION ==========");
            System.out.println("Calling polymorphic methods on all characters...\n");

            characterService.demonstratePolymorphism();

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }

    public void getCharactersByType() {
        try {
            System.out.println("\n========== FILTER BY TYPE ==========");
            System.out.println("1. Warriors");
            System.out.println("2. Mages");
            System.out.println("3. Rogues");
            System.out.print("Select type: ");

            int choice = scanner.nextInt();
            scanner.nextLine();

            String type = "";
            switch (choice) {
                case 1:
                    type = "WARRIOR";
                    break;
                case 2:
                    type = "MAGE";
                    break;
                case 3:
                    type = "ROGUE";
                    break;
                default:
                    System.out.println("Invalid choice!");
                    return;
            }

            List<GameEntity> allCharacters = characterService.getAllCharacters();
            List<GameEntity> filtered = new java.util.ArrayList<>();

            for (GameEntity character : allCharacters) {
                if (character.getCharacterType().equals(type)) {
                    filtered.add(character);
                }
            }

            if (filtered.isEmpty()) {
                System.out.println("\nNo " + type.toLowerCase() + "s found.");
            } else {
                System.out.println("\nFound " + filtered.size() + " " + type.toLowerCase() + "(s):\n");
                for (GameEntity character : filtered) {
                    character.displayInfo();
                    System.out.println();
                }
            }

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }


    public void showStatistics() {
        try {
            System.out.println("\n========== CHARACTER STATISTICS ==========");
            List<GameEntity> characters = characterService.getAllCharacters();

            if (characters.isEmpty()) {
                System.out.println("No characters in database.");
                return;
            }

            int warriorCount = 0, mageCount = 0, rogueCount = 0;
            int totalLevel = 0;
            int maxPower = 0;
            GameEntity mostPowerful = null;

            for (GameEntity character : characters) {
                // Count by type
                switch (character.getCharacterType()) {
                    case "WARRIOR":
                        warriorCount++;
                        break;
                    case "MAGE":
                        mageCount++;
                        break;
                    case "ROGUE":
                        rogueCount++;
                        break;
                }

                // Calculate stats
                totalLevel += character.getLevel();
                int power = character.calculatePower();
                if (power > maxPower) {
                    maxPower = power;
                    mostPowerful = character;
                }
            }

            System.out.println("Total Characters: " + characters.size());
            System.out.println("Warriors: " + warriorCount);
            System.out.println("Mages: " + mageCount);
            System.out.println("Rogues: " + rogueCount);
            System.out.println("Average Level: " + (totalLevel / characters.size()));

            if (mostPowerful != null) {
                System.out.println("\nMost Powerful Character:");
                System.out.println("  Name: " + mostPowerful.getName());
                System.out.println("  Type: " + mostPowerful.getCharacterType());
                System.out.println("  Power: " + maxPower);
            }

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }
}

