package utils;

import model.GameEntity;

import java.util.List;


public class SortingUtils {


    public static void sortByName(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c1.getName().compareTo(c2.getName()));
    }


    public static void sortByLevelDesc(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c2.getLevel() - c1.getLevel());
    }


    public static void sortByExperienceDesc(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c2.getExperience() - c1.getExperience());
    }


    public static void sortByPowerDesc(List<GameEntity> characters) {
        characters.sort((c1, c2) -> c2.calculatePower() - c1.calculatePower());
    }


    public static List<GameEntity> filterByMinLevel(List<GameEntity> characters, int minLevel) {
        return characters.stream()
                .filter(c -> c.getLevel() >= minLevel)
                .toList();
    }


    public static List<GameEntity> filterByType(List<GameEntity> characters, String type) {
        return characters.stream()
                .filter(c -> c.getCharacterType().equals(type))
                .toList();
    }
}
