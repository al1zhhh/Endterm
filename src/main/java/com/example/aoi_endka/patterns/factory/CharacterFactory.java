package com.example.aoi_endka.patterns.factory;

import com.example.aoi_endka.model.GameEntity;
import com.example.aoi_endka.model.Mage;
import com.example.aoi_endka.model.Rogue;
import com.example.aoi_endka.model.Warrior;

/**
 * Factory Pattern - Character Factory
 * Creates different character types without exposing creation logic
 */
public class CharacterFactory {

    /**
     * Create character based on type
     * @param type Character type (WARRIOR, MAGE, ROGUE)
     * @param name Character name
     * @param level Character level
     * @return GameEntity instance
     */
    public static GameEntity createCharacter(String type, String name, int level) {
        switch (type.toUpperCase()) {
            case "WARRIOR":
                return new Warrior(name, level, 50, 30, "Sword");

            case "MAGE":
                return new Mage(name, level, 200, 45, "Fire");

            case "ROGUE":
                return new Rogue(name, level, 40, 35, 0.25);

            default:
                throw new IllegalArgumentException("Unknown character type: " + type);
        }
    }

    /**
     * Create character with custom stats
     */
    public static GameEntity createWarrior(String name, int level, int strength, int armor, String weapon) {
        return new Warrior(name, level, strength, armor, weapon);
    }

    public static GameEntity createMage(String name, int level, int mana, int intelligence, String spellSchool) {
        return new Mage(name, level, mana, intelligence, spellSchool);
    }

    public static GameEntity createRogue(String name, int level, int agility, int stealth, double critChance) {
        return new Rogue(name, level, agility, stealth, critChance);
    }
}