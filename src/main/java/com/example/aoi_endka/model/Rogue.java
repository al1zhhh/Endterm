package com.example.aoi_endka.model;

import com.example.aoi_endka.interfaces.Combatant;
import com.example.aoi_endka.interfaces.Progressable;
import java.time.LocalDateTime;


public class Rogue extends GameEntity implements Combatant, Progressable {
    private int agility;
    private int stealth;
    private double criticalChance;
    public Rogue() { super(); }

    public Rogue(String name, int level, int agility, int stealth, double criticalChance) {
        super(name, level);
        setAgility(agility);
        setStealth(stealth);
        setCriticalChance(criticalChance);
    }


    public Rogue(int id, String name, int level, int experience, LocalDateTime createdDate,
                 int agility, int stealth, double criticalChance) {
        super(id, name, level, experience, createdDate);
        this.agility = agility;
        this.stealth = stealth;
        this.criticalChance = criticalChance;
    }

    @Override
    public int calculatePower() {
        // Rogue power based on agility and stealth with critical chance modifier
        return (int)((agility * 2) + stealth + (getLevel() * 3) * (1 + criticalChance));
    }

    @Override
    public void levelUp() {
        setLevel(getLevel() + 1);
        agility += 4;
        stealth += 2;
        criticalChance += 0.02;
        System.out.println(getName() + " leveled up to " + getLevel() + "!");
        System.out.println("Agility increased to " + agility);
        System.out.println("Stealth increased to " + stealth);
        System.out.println("Critical Chance increased to " + String.format("%.2f", criticalChance * 100) + "%");
    }

    @Override
    public String getCharacterType() {
        return "ROGUE";
    }

    // Combatant interface implementation
    @Override
    public int attack() {
        int baseDamage = agility * 2;
        boolean isCritical = Math.random() < criticalChance;
        int damage = isCritical ? baseDamage * 2 : baseDamage;

        if (isCritical) {
            System.out.println(getName() + " strikes with a CRITICAL HIT for " + damage + " damage!");
        } else {
            System.out.println(getName() + " strikes from the shadows for " + damage + " damage!");
        }
        return damage;
    }

    @Override
    public int defend() {
        int evasion = stealth + agility / 2;
        System.out.println(getName() + " evades with " + evasion + " evasion!");
        return evasion;
    }

    @Override
    public int calculateDamage() {
        return (int)(agility * 2 * (1 + criticalChance) + getLevel());
    }

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


    public void setAgility(int agility) {
        if (agility <= 0) {
            throw new IllegalArgumentException("Agility must be greater than 0");
        }
        this.agility = agility;
    }

    public void setStealth(int stealth) {
        if (stealth <= 0) {
            throw new IllegalArgumentException("Stealth must be greater than 0");
        }
        this.stealth = stealth;
    }

    public void setCriticalChance(double criticalChance) {
        if (criticalChance < 0 || criticalChance > 1) {
            throw new IllegalArgumentException("Critical chance must be between 0 and 1");
        }
        this.criticalChance = criticalChance;
    }

    public int getAgility() {
        return agility;
    }

    public int getStealth() {
        return stealth;
    }

    public double getCriticalChance() {
        return criticalChance;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Agility: " + agility);
        System.out.println("Stealth: " + stealth);
        System.out.println("Critical Chance: " + String.format("%.2f", criticalChance * 100) + "%");
    }
}