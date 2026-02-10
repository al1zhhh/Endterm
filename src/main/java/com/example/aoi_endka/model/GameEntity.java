package com.example.aoi_endka.model;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import java.time.LocalDateTime;

/**
 * Abstract base class for all game entities
 * Demonstrates: Abstraction, Encapsulation
 */
@JsonTypeInfo(
        use = JsonTypeInfo.Id.NAME,
        include = JsonTypeInfo.As.PROPERTY,
        property = "type"
)
@JsonSubTypes({
        @JsonSubTypes.Type(value = Warrior.class, name = "WARRIOR"),
        @JsonSubTypes.Type(value = Mage.class, name = "MAGE"),
        @JsonSubTypes.Type(value = Rogue.class, name = "ROGUE")
})
public abstract class GameEntity {

    private int id;
    private String name;
    private int level;
    private int experience;
    private LocalDateTime createdDate;

    /**
     * No-args constructor for Jackson (required for JSON -> object conversion).
     */
    protected GameEntity() {
        this.createdDate = LocalDateTime.now();
    }

    // Your constructors
    public GameEntity(String name, int level) {
        this.name = name;
        this.level = level;
        this.experience = 0;
        this.createdDate = LocalDateTime.now();
    }

    public GameEntity(int id, String name, int level, int experience, LocalDateTime createdDate) {
        this.id = id;
        this.name = name;
        this.level = level;
        this.experience = experience;
        this.createdDate = createdDate;
    }

    // Abstract methods - must be implemented by subclasses
    public abstract int calculatePower();
    public abstract void levelUp();
    public abstract String getCharacterType();

    // Concrete method
    public void displayInfo() {
        System.out.println("=================================");
        System.out.println("Character: " + name);
        System.out.println("Type: " + getCharacterType());
        System.out.println("Level: " + level);
        System.out.println("Experience: " + experience);
        System.out.println("Power: " + calculatePower());
        System.out.println("=================================");
    }

    // Setters with validation
    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Name cannot be empty");
        }
        if (name.length() < 3 || name.length() > 50) {
            throw new IllegalArgumentException("Name must be between 3 and 50 characters");
        }
        this.name = name;
    }

    public void setLevel(int level) {
        if (level < 1 || level > 100) {
            throw new IllegalArgumentException("Level must be between 1 and 100");
        }
        this.level = level;
    }

    public void setExperience(int experience) {
        if (experience < 0) {
            throw new IllegalArgumentException("Experience cannot be negative");
        }
        this.experience = experience;
    }

    public void setId(int id) {
        this.id = id;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }

    // Getters
    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public int getLevel() {
        return level;
    }

    public int getExperience() {
        return experience;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }
}