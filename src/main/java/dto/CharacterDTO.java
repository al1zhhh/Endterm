package dto;


public class CharacterDTO {
    private Integer id;
    private String name;
    private String type;
    private Integer level;
    private Integer experience;
    private Integer power;

    // Warrior fields
    private Integer strength;
    private Integer armor;
    private String weaponType;

    // Mage fields
    private Integer mana;
    private Integer intelligence;
    private String spellSchool;

    // Rogue fields
    private Integer agility;
    private Integer stealth;
    private Double criticalChance;

    // Constructors
    public CharacterDTO() {}

    // Getters and Setters
    public Integer getId() { return id; }
    public void setId(Integer id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }

    public Integer getLevel() { return level; }
    public void setLevel(Integer level) { this.level = level; }

    public Integer getExperience() { return experience; }
    public void setExperience(Integer experience) { this.experience = experience; }

    public Integer getPower() { return power; }
    public void setPower(Integer power) { this.power = power; }

    // Warrior
    public Integer getStrength() { return strength; }
    public void setStrength(Integer strength) { this.strength = strength; }

    public Integer getArmor() { return armor; }
    public void setArmor(Integer armor) { this.armor = armor; }

    public String getWeaponType() { return weaponType; }
    public void setWeaponType(String weaponType) { this.weaponType = weaponType; }

    // Mage
    public Integer getMana() { return mana; }
    public void setMana(Integer mana) { this.mana = mana; }

    public Integer getIntelligence() { return intelligence; }
    public void setIntelligence(Integer intelligence) { this.intelligence = intelligence; }

    public String getSpellSchool() { return spellSchool; }
    public void setSpellSchool(String spellSchool) { this.spellSchool = spellSchool; }

    // Rogue
    public Integer getAgility() { return agility; }
    public void setAgility(Integer agility) { this.agility = agility; }

    public Integer getStealth() { return stealth; }
    public void setStealth(Integer stealth) { this.stealth = stealth; }

    public Double getCriticalChance() { return criticalChance; }
    public void setCriticalChance(Double criticalChance) { this.criticalChance = criticalChance; }
}