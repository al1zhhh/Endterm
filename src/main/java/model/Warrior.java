package model;

import interfaces.Combatant;
import interfaces.Progressable;
import java.time.LocalDateTime;

public class Warrior extends GameEntity implements Combatant, Progressable {
    private int strength;
    private int armor;
    private String weaponType;

    // Constructor для новых воинов
    public Warrior(String name, int level, int strength, int armor, String weaponType) {
        super(name, level);
        setStrength(strength);
        setArmor(armor);
        this.weaponType = weaponType;
    }

    // Constructor для загрузки из БД
    public Warrior(int id, String name, int level, int experience, LocalDateTime createdDate,
                   int strength, int armor, String weaponType) {
        super(id, name, level, experience, createdDate);
        this.strength = strength;
        this.armor = armor;
        this.weaponType = weaponType;
    }

    // ⭐ ВАЖНО: @Override для ВСЕХ абстрактных методов
    @Override
    public int calculatePower() {
        return (strength * 2) + armor + (getLevel() * 5);
    }

    @Override
    public void levelUp() {
        setLevel(getLevel() + 1);
        strength += 3;
        armor += 2;
        System.out.println(getName() + " leveled up to " + getLevel() + "!");
    }

    @Override
    public String getCharacterType() {
        return "WARRIOR";
    }

    // Combatant interface methods
    @Override
    public int attack() {
        int baseDamage = strength * 2;
        System.out.println(getName() + " attacks with " + weaponType + " for " + baseDamage + " damage!");
        return baseDamage;
    }

    @Override
    public int defend() {
        int defense = armor;
        System.out.println(getName() + " defends with " + defense + " armor!");
        return defense;
    }

    @Override
    public int calculateDamage() {
        return strength * 2 + getLevel();
    }

    // Progressable interface methods
    @Override
    public void gainExperience(int xp) {
        if (xp <= 0) {
            throw new IllegalArgumentException("Experience points must be positive");
        }
        setExperience(getExperience() + xp);
        System.out.println(getName() + " gained " + xp + " XP! Total: " + getExperience());

        if (canLevelUp()) {
            levelUp();
        }
    }

    @Override
    public boolean canLevelUp() {
        int requiredXP = getLevel() * 1000;
        return getExperience() >= requiredXP;
    }

    // Validation
    public void setStrength(int strength) {
        if (strength <= 0) {
            throw new IllegalArgumentException("Strength must be greater than 0");
        }
        this.strength = strength;
    }

    public void setArmor(int armor) {
        if (armor < 0) {
            throw new IllegalArgumentException("Armor cannot be negative");
        }
        this.armor = armor;
    }

    // Getters
    public int getStrength() {
        return strength;
    }

    public int getArmor() {
        return armor;
    }

    public String getWeaponType() {
        return weaponType;
    }

    public void setWeaponType(String weaponType) {
        this.weaponType = weaponType;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Strength: " + strength);
        System.out.println("Armor: " + armor);
        System.out.println("Weapon: " + weaponType);
    }
}