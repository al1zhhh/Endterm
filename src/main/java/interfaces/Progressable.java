package interfaces;

public interface Progressable {

    void gainExperience(int xp);
    boolean canLevelUp();

    default int calculateRequiredXP(int level) {
        return level * 1000;  // Default formula
    }
    static int getMaxLevel() {
        return 100;
    }

    static boolean isValidLevel(int level) {
        return level >= 1 && level <= getMaxLevel();
    }
}