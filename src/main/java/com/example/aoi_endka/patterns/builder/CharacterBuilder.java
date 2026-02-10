package com.example.aoi_endka.patterns.builder;

import com.example.aoi_endka.model.GameEntity;
import com.example.aoi_endka.model.Mage;
import com.example.aoi_endka.model.Rogue;
import com.example.aoi_endka.model.Warrior;

/**
 * Builder Pattern - Character Builder
 * Allows step-by-step construction of complex character objects
 */
public class CharacterBuilder {

    // Required fields
    private String name;
    private int level;
    private String characterType;

    // Optional fields
    private int strength;
    private int armor;
    private String weaponType;
    private int mana;
    private int intelligence;
    private String spellSchool;
    private int agility;
    private int stealth;
    private double criticalChance;

    public CharacterBuilder(String name, int level, String characterType) {
        this.name = name;
        this.level = level;
        this.characterType = characterType;
    }

    // Warrior-specific
    public CharacterBuilder withStrength(int strength) {
        this.strength = strength;
        return this;
    }

    public CharacterBuilder withArmor(int armor) {
        this.armor = armor;
        return this;
    }

    public CharacterBuilder withWeaponType(String weaponType) {
        this.weaponType = weaponType;
        return this;
    }

    // Mage-specific
    public CharacterBuilder withMana(int mana) {
        this.mana = mana;
        return this;
    }

    public CharacterBuilder withIntelligence(int intelligence) {
        this.intelligence = intelligence;
        return this;
    }

    public CharacterBuilder withSpellSchool(String spellSchool) {
        this.spellSchool = spellSchool;
        return this;
    }

    // Rogue-specific
    public CharacterBuilder withAgility(int agility) {
        this.agility = agility;
        return this;
    }

    public CharacterBuilder withStealth(int stealth) {
        this.stealth = stealth;
        return this;
    }

    public CharacterBuilder withCriticalChance(double criticalChance) {
        this.criticalChance = criticalChance;
        return this;
    }

    /**
     * Build the character
     */
    public GameEntity build() {
        switch (characterType.toUpperCase()) {
            case "WARRIOR":
                return new Warrior(name, level, strength, armor, weaponType);

            case "MAGE":
                return new Mage(name, level, mana, intelligence, spellSchool);

            case "ROGUE":
                return new Rogue(name, level, agility, stealth, criticalChance);

            default:
                throw new IllegalArgumentException("Unknown character type: " + characterType);
        }
    }
}

// Usage example:
// GameEntity warrior = new CharacterBuilder("Arthas", 10, "WARRIOR")
//     .withStrength(50)
//     .withArmor(30)
//     .withWeaponType("Great Sword")
//     .build();