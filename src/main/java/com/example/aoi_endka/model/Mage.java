package com.example.aoi_endka.model;
import com.example.aoi_endka.interfaces.Combatant;
import com.example.aoi_endka.interfaces.Progressable;
import java.time.LocalDateTime;
public class Mage extends GameEntity implements Combatant,Progressable {
    private int mana;
    private int intelligence;
    private String spellSchool;
    public Mage() { super(); }

    public Mage(String name,int level,int mana,int intelligence,String spellSchool) {
        super(name, level);
        setMana(mana);
        setIntelligence(intelligence);
        this.spellSchool = spellSchool;
    }
    public Mage(int id, String name, int level, int experience, LocalDateTime createdDate,
                int mana, int intelligence, String spellSchool) {
        super(id, name, level, experience, createdDate);
        this.mana = mana;
        this.intelligence = intelligence;
        this.spellSchool = spellSchool;
    }
    @Override
    public int calculatePower() {
        // Mage power based on intelligence and mana
        return (intelligence * 3) + (mana / 2) + (getLevel() * 4);
    }

    @Override
    public void levelUp() {
        setLevel(getLevel() + 1);
        mana += 10;
        intelligence += 4;
        System.out.println(getName() + " leveled up to " + getLevel() + "!");
        System.out.println("Mana increased to " + mana);
        System.out.println("Intelligence increased to " + intelligence);
    }

    @Override
    public String getCharacterType() {
        return "MAGE";
    }

    // Combatant interface implementation
    @Override
    public int attack() {
        int spellDamage = intelligence * 3;
        System.out.println(getName() + " casts " + spellSchool + " spell for " + spellDamage + " damage!");
        return spellDamage;
    }

    @Override
    public int defend() {
        int magicShield = mana / 10;
        System.out.println(getName() + " creates magic shield with " + magicShield + " absorption!");
        return magicShield;
    }

    @Override
    public int calculateDamage() {
        return intelligence * 3 + getLevel();
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

    // Validation in setters
    public void setMana(int mana) {
        if (mana <= 0) {
            throw new IllegalArgumentException("Mana must be greater than 0");
        }
        this.mana = mana;
    }

    public void setIntelligence(int intelligence) {
        if (intelligence <= 0) {
            throw new IllegalArgumentException("Intelligence must be greater than 0");
        }
        this.intelligence = intelligence;
    }

    // Getters
    public int getMana() {
        return mana;
    }

    public int getIntelligence() {
        return intelligence;
    }

    public String getSpellSchool() {
        return spellSchool;
    }

    public void setSpellSchool(String spellSchool) {
        this.spellSchool = spellSchool;
    }

    @Override
    public void displayInfo() {
        super.displayInfo();
        System.out.println("Mana: " + mana);
        System.out.println("Intelligence: " + intelligence);
        System.out.println("Spell School: " + spellSchool);
    }
}

