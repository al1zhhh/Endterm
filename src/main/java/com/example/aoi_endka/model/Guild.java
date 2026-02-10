package com.example.aoi_endka.model;

import java.time.LocalDateTime;

/**
 * Guild class - demonstrates composition/aggregation
 * Characters can belong to a Guild
 */
public class Guild {
    private int id;
    private String guildName;
    private int level;
    private int memberCount;
    private LocalDateTime createdDate;

    // Constructor for new guilds
    public Guild(String guildName) {
        setGuildName(guildName);
        this.level = 1;
        this.memberCount = 0;
        this.createdDate = LocalDateTime.now();
    }

    // Constructor for database loading
    public Guild(int id, String guildName, int level, int memberCount, LocalDateTime createdDate) {
        this.id = id;
        this.guildName = guildName;
        this.level = level;
        this.memberCount = memberCount;
        this.createdDate = createdDate;
    }

    // Business logic methods
    public void addMember(GameEntity member) {
        this.memberCount++;
        System.out.println("New member joined " + guildName + "! Total members: " + memberCount);
    }

    public void removeMember() {
        if (memberCount > 0) {
            this.memberCount--;
            System.out.println("Member left " + guildName + ". Remaining members: " + memberCount);
        }
    }

    public void levelUp() {
        this.level++;
        System.out.println(guildName + " reached level " + level + "!");
    }

    public void displayInfo() {
        System.out.println("=================================");
        System.out.println("Guild: " + guildName);
        System.out.println("Level: " + level);
        System.out.println("Members: " + memberCount);
        System.out.println("Created: " + createdDate);
        System.out.println("=================================");
    }

    // Validation
    public void setGuildName(String guildName) {
        if (guildName == null || guildName.trim().isEmpty()) {
            throw new IllegalArgumentException("Guild name cannot be empty");
        }
        if (guildName.length() < 3 || guildName.length() > 100) {
            throw new IllegalArgumentException("Guild name must be between 3 and 100 characters");
        }
        this.guildName = guildName;
    }

    public void setLevel(int level) {
        if (level < 1) {
            throw new IllegalArgumentException("Guild level must be at least 1");
        }
        this.level = level;
    }

    public void setMemberCount(int memberCount) {
        if (memberCount < 0) {
            throw new IllegalArgumentException("Member count cannot be negative");
        }
        this.memberCount = memberCount;
    }

    // Getters
    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getGuildName() {
        return guildName;
    }

    public int getLevel() {
        return level;
    }

    public int getMemberCount() {
        return memberCount;
    }

    public LocalDateTime getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(LocalDateTime createdDate) {
        this.createdDate = createdDate;
    }


}