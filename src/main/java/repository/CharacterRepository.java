package repository;

import model.*;
import repository.interfaces.CrudRepository;
import exceptions.DatabaseOperationException;
import exceptions.ResourceNotFoundException;
import utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Character CRUD operations using JDBC
 */
public class CharacterRepository implements CrudRepository<GameEntity>{

    /**
     * Create a new character in database
     */
    @Override
    public int create(GameEntity entity) throws DatabaseOperationException {
        String sqlCharacter = "INSERT INTO characters (name, character_type, level, experience, health_points, guild_id) VALUES (?, ?, ?, ?, ?, ?)";
        String sqlAttributes = "INSERT INTO character_attributes (character_id, strength, armor, weapon_type, mana, intelligence, spell_school, agility, stealth, critical_chance) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        PreparedStatement psChar = null;
        PreparedStatement psAttr = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Insert character
            psChar = conn.prepareStatement(sqlCharacter, Statement.RETURN_GENERATED_KEYS);
            psChar.setString(1, entity.getName());
            psChar.setString(2, entity.getCharacterType());
            psChar.setInt(3, entity.getLevel());
            psChar.setInt(4, entity.getExperience());
            psChar.setInt(5, 100); // Default health
            psChar.setNull(6, Types.INTEGER); // No guild initially

            int affectedRows = psChar.executeUpdate();
            if (affectedRows == 0) {
                throw new DatabaseOperationException("Creating character failed, no rows affected.");
            }

            rs = psChar.getGeneratedKeys();
            int characterId = 0;
            if (rs.next()) {
                characterId = rs.getInt(1);
                entity.setId(characterId);
            }

            // Insert character-specific attributes
            psAttr = conn.prepareStatement(sqlAttributes);
            psAttr.setInt(1, characterId);

            if (entity instanceof Warrior) {
                Warrior w = (Warrior) entity;
                psAttr.setInt(2, w.getStrength());
                psAttr.setInt(3, w.getArmor());
                psAttr.setString(4, w.getWeaponType());
                psAttr.setNull(5, Types.INTEGER);
                psAttr.setNull(6, Types.INTEGER);
                psAttr.setNull(7, Types.VARCHAR);
                psAttr.setNull(8, Types.INTEGER);
                psAttr.setNull(9, Types.INTEGER);
                psAttr.setNull(10, Types.DECIMAL);
            } else if (entity instanceof Mage) {
                Mage m = (Mage) entity;
                psAttr.setNull(2, Types.INTEGER);
                psAttr.setNull(3, Types.INTEGER);
                psAttr.setNull(4, Types.VARCHAR);
                psAttr.setInt(5, m.getMana());
                psAttr.setInt(6, m.getIntelligence());
                psAttr.setString(7, m.getSpellSchool());
                psAttr.setNull(8, Types.INTEGER);
                psAttr.setNull(9, Types.INTEGER);
                psAttr.setNull(10, Types.DECIMAL);
            } else if (entity instanceof Rogue) {
                Rogue r = (Rogue) entity;
                psAttr.setNull(2, Types.INTEGER);
                psAttr.setNull(3, Types.INTEGER);
                psAttr.setNull(4, Types.VARCHAR);
                psAttr.setNull(5, Types.INTEGER);
                psAttr.setNull(6, Types.INTEGER);
                psAttr.setNull(7, Types.VARCHAR);
                psAttr.setInt(8, r.getAgility());
                psAttr.setInt(9, r.getStealth());
                psAttr.setDouble(10, r.getCriticalChance());
            }

            psAttr.executeUpdate();
            conn.commit();

            System.out.println("Character created successfully with ID: " + characterId);
            return characterId;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseOperationException("Rollback failed", ex);
                }
            }
            throw new DatabaseOperationException("Failed to create character: " + e.getMessage(), e);
        } finally {
            closeResources(rs, psAttr, psChar, null);
        }
    }

    /**
     * Get all characters
     */
    @Override
    public List<GameEntity> getAll() throws DatabaseOperationException {
        String sql = "SELECT c.*, a.* FROM characters c LEFT JOIN character_attributes a ON c.id = a.character_id";
        List<GameEntity> characters = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                GameEntity character = buildCharacterFromResultSet(rs);
                characters.add(character);
            }

            return characters;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve characters: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, null, null);
        }
    }

    /**
     * Get character by ID
     */
    @Override
    public GameEntity getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT c.*, a.* FROM characters c LEFT JOIN character_attributes a ON c.id = a.character_id WHERE c.id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return buildCharacterFromResultSet(rs);
            } else {
                throw new ResourceNotFoundException("Character with ID " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve character: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps, null, null);
        }
    }

    /**
     * Update character
     */
    @Override
    public void update(int id, GameEntity entity) throws DatabaseOperationException, ResourceNotFoundException {
        // First check if exists
        getById(id);

        String sqlChar = "UPDATE characters SET name = ?, level = ?, experience = ? WHERE id = ?";
        String sqlAttr = "UPDATE character_attributes SET strength = ?, armor = ?, weapon_type = ?, mana = ?, intelligence = ?, spell_school = ?, agility = ?, stealth = ?, critical_chance = ? WHERE character_id = ?";

        Connection conn = null;
        PreparedStatement psChar = null;
        PreparedStatement psAttr = null;

        try {
            conn = DatabaseConnection.getConnection();
            conn.setAutoCommit(false);

            // Update character
            psChar = conn.prepareStatement(sqlChar);
            psChar.setString(1, entity.getName());
            psChar.setInt(2, entity.getLevel());
            psChar.setInt(3, entity.getExperience());
            psChar.setInt(4, id);
            psChar.executeUpdate();

            // Update attributes
            psAttr = conn.prepareStatement(sqlAttr);

            if (entity instanceof Warrior) {
                Warrior w = (Warrior) entity;
                psAttr.setInt(1, w.getStrength());
                psAttr.setInt(2, w.getArmor());
                psAttr.setString(3, w.getWeaponType());
                psAttr.setNull(4, Types.INTEGER);
                psAttr.setNull(5, Types.INTEGER);
                psAttr.setNull(6, Types.VARCHAR);
                psAttr.setNull(7, Types.INTEGER);
                psAttr.setNull(8, Types.INTEGER);
                psAttr.setNull(9, Types.DECIMAL);
            } else if (entity instanceof Mage) {
                Mage m = (Mage) entity;
                psAttr.setNull(1, Types.INTEGER);
                psAttr.setNull(2, Types.INTEGER);
                psAttr.setNull(3, Types.VARCHAR);
                psAttr.setInt(4, m.getMana());
                psAttr.setInt(5, m.getIntelligence());
                psAttr.setString(6, m.getSpellSchool());
                psAttr.setNull(7, Types.INTEGER);
                psAttr.setNull(8, Types.INTEGER);
                psAttr.setNull(9, Types.DECIMAL);
            } else if (entity instanceof Rogue) {
                Rogue r = (Rogue) entity;
                psAttr.setNull(1, Types.INTEGER);
                psAttr.setNull(2, Types.INTEGER);
                psAttr.setNull(3, Types.VARCHAR);
                psAttr.setNull(4, Types.INTEGER);
                psAttr.setNull(5, Types.INTEGER);
                psAttr.setNull(6, Types.VARCHAR);
                psAttr.setInt(7, r.getAgility());
                psAttr.setInt(8, r.getStealth());
                psAttr.setDouble(9, r.getCriticalChance());
            }

            psAttr.setInt(10, id);
            psAttr.executeUpdate();

            conn.commit();
            System.out.println("Character updated successfully!");

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException ex) {
                    throw new DatabaseOperationException("Rollback failed", ex);
                }
            }
            throw new DatabaseOperationException("Failed to update character: " + e.getMessage(), e);
        } finally {
            closeResources(null, psAttr, psChar, null);
        }
    }

    /**
     * Delete character
     */
    @Override
    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        // First check if exists
        getById(id);

        String sql = "DELETE FROM characters WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                System.out.println("Character deleted successfully!");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete character: " + e.getMessage(), e);
        } finally {
            closeResources(null, ps, null, null);
        }
    }

    /**
     * Helper method to build character object from ResultSet
     */
    private GameEntity buildCharacterFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String name = rs.getString("name");
        String type = rs.getString("character_type");
        int level = rs.getInt("level");
        int experience = rs.getInt("experience");
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        GameEntity character = null;

        switch (type) {
            case "WARRIOR":
                int strength = rs.getInt("strength");
                int armor = rs.getInt("armor");
                String weaponType = rs.getString("weapon_type");
                character = new Warrior(id, name, level, experience, createdDate, strength, armor, weaponType);
                break;

            case "MAGE":
                int mana = rs.getInt("mana");
                int intelligence = rs.getInt("intelligence");
                String spellSchool = rs.getString("spell_school");
                character = new Mage(id, name, level, experience, createdDate, mana, intelligence, spellSchool);
                break;

            case "ROGUE":
                int agility = rs.getInt("agility");
                int stealth = rs.getInt("stealth");
                double criticalChance = rs.getDouble("critical_chance");
                character = new Rogue(id, name, level, experience, createdDate, agility, stealth, criticalChance);
                break;
        }

        return character;
    }

    /**
     * Helper method to close resources
     */
    private void closeResources(ResultSet rs, PreparedStatement... statements) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        for (PreparedStatement ps : statements) {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }
}