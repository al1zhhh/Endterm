package com.example.aoi_endka.model;


public class Equipment {
    private int id;
    private String name;
    private String equipmentType;
    private String bonusStats;
    private String rarity;
    private int characterId;

    public Equipment(String name, String equipmentType, String bonusStats, String rarity) {
        setName(name);
        setEquipmentType(equipmentType);
        this.bonusStats = bonusStats;
        setRarity(rarity);
    }

    // Constructor for database loading
    public Equipment(int id, String name, String equipmentType, String bonusStats,
                     String rarity, int characterId) {
        this.id = id;
        this.name = name;
        this.equipmentType = equipmentType;
        this.bonusStats = bonusStats;
        this.rarity = rarity;
        this.characterId = characterId;
    }

    public void displayInfo() {
        System.out.println("=================================");
        System.out.println("Equipment: " + name);
        System.out.println("Type: " + equipmentType);
        System.out.println("Rarity: " + rarity);
        System.out.println("Bonus Stats: " + bonusStats);
        System.out.println("=================================");
    }


    public void setName(String name) {
        if (name == null || name.trim().isEmpty()) {
            throw new IllegalArgumentException("Equipment name cannot be empty");
        }
        this.name = name;
    }

    public void setEquipmentType(String equipmentType) {
        if (!equipmentType.equals("WEAPON") && !equipmentType.equals("ARMOR")
                && !equipmentType.equals("ACCESSORY")) {
            throw new IllegalArgumentException("Equipment type must be WEAPON, ARMOR, or ACCESSORY");
        }
        this.equipmentType = equipmentType;
    }

    public void setRarity(String rarity) {
        if (!rarity.equals("COMMON") && !rarity.equals("RARE")
                && !rarity.equals("EPIC") && !rarity.equals("LEGENDARY")) {
            throw new IllegalArgumentException("Rarity must be COMMON, RARE, EPIC, or LEGENDARY");
        }
        this.rarity = rarity;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public String getBonusStats() {
        return bonusStats;
    }

    public void setBonusStats(String bonusStats) {
        this.bonusStats = bonusStats;
    }

    public String getRarity() {
        return rarity;
    }

    public int getCharacterId() {
        return characterId;
    }

    public void setCharacterId(int characterId) {
        this.characterId = characterId;
    }
}