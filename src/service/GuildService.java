package service;

import model.Guild;
import model.GameEntity;
import repository.GuildRepository;
import repository.CharacterRepository;
import exceptions.*;

import java.sql.*;
import utils.DatabaseConnection;
import java.util.List;


public class GuildService {
    private GuildRepository guildRepository;
    private CharacterRepository characterRepository;

    public GuildService() {
        this.guildRepository = new GuildRepository();
        this.characterRepository = new CharacterRepository();
    }


    public int createGuild(Guild guild) throws InvalidInputException, DatabaseOperationException {

        validateGuild(guild);

        try {
            List<Guild> existingGuilds = guildRepository.getAll();
            for (Guild existing : existingGuilds) {
                if (existing.getGuildName().equalsIgnoreCase(guild.getGuildName())) {
                    throw new DuplicateResourceException("Guild with name '" + guild.getGuildName() + "' already exists");
                }
            }
        } catch (DatabaseOperationException e) {
            throw e;
        }

        return guildRepository.create(guild);
    }


    public List<Guild> getAllGuilds() throws DatabaseOperationException {
        return guildRepository.getAll();
    }


    public Guild getGuildById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        if (id <= 0) {
            throw new ResourceNotFoundException("Invalid guild ID: " + id);
        }
        return guildRepository.getById(id);
    }


    public void updateGuild(int id, Guild guild) throws InvalidInputException, DatabaseOperationException, ResourceNotFoundException {
        validateGuild(guild);
        guildRepository.update(id, guild);
    }


    public void deleteGuild(int id) throws DatabaseOperationException, ResourceNotFoundException {
        guildRepository.delete(id);
    }


    public void addCharacterToGuild(int characterId, int guildId) throws DatabaseOperationException, ResourceNotFoundException {
        GameEntity character = characterRepository.getById(characterId);
        Guild guild = guildRepository.getById(guildId);


        String sql = "UPDATE characters SET guild_id = ? WHERE id = ?";
        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, guildId);
            ps.setInt(2, characterId);
            ps.executeUpdate();


            guild.addMember();
            guildRepository.update(guildId, guild);

            System.out.println(character.getName() + " joined " + guild.getGuildName() + "!");

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to add character to guild: " + e.getMessage(), e);
        } finally {
            if (ps != null) {
                try {
                    ps.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Remove character from guild
     */
    public void removeCharacterFromGuild(int characterId) throws DatabaseOperationException, ResourceNotFoundException {
        GameEntity character = characterRepository.getById(characterId);

        // Get current guild
        String sqlGet = "SELECT guild_id FROM characters WHERE id = ?";
        String sqlUpdate = "UPDATE characters SET guild_id = NULL WHERE id = ?";

        Connection conn = null;
        PreparedStatement psGet = null;
        PreparedStatement psUpdate = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();

            // Get guild_id
            psGet = conn.prepareStatement(sqlGet);
            psGet.setInt(1, characterId);
            rs = psGet.executeQuery();

            if (rs.next()) {
                int guildId = rs.getInt("guild_id");

                if (!rs.wasNull()) {
                    // Update character
                    psUpdate = conn.prepareStatement(sqlUpdate);
                    psUpdate.setInt(1, characterId);
                    psUpdate.executeUpdate();

                    // Decrement guild member count
                    Guild guild = guildRepository.getById(guildId);
                    guild.removeMember();
                    guildRepository.update(guildId, guild);

                    System.out.println(character.getName() + " left the guild!");
                } else {
                    System.out.println("Character is not in any guild.");
                }
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to remove character from guild: " + e.getMessage(), e);
        } finally {
            if (rs != null) {
                try {
                    rs.close();
                } catch (SQLException e) {
                    System.err.println("Error closing ResultSet: " + e.getMessage());
                }
            }
            if (psGet != null) {
                try {
                    psGet.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
            if (psUpdate != null) {
                try {
                    psUpdate.close();
                } catch (SQLException e) {
                    System.err.println("Error closing PreparedStatement: " + e.getMessage());
                }
            }
        }
    }

    /**
     * Level up guild
     */
    public void levelUpGuild(int id) throws DatabaseOperationException, ResourceNotFoundException {
        Guild guild = guildRepository.getById(id);
        guild.levelUp();
        guildRepository.update(id, guild);
    }

    /**
     * Validate guild data
     */
    private void validateGuild(Guild guild) throws InvalidInputException {
        if (guild == null) {
            throw new InvalidInputException("Guild cannot be null");
        }

        if (guild.getGuildName() == null || guild.getGuildName().trim().isEmpty()) {
            throw new InvalidInputException("Guild name cannot be empty");
        }

        if (guild.getGuildName().length() < 3 || guild.getGuildName().length() > 100) {
            throw new InvalidInputException("Guild name must be between 3 and 100 characters");
        }

        if (guild.getLevel() < 1) {
            throw new InvalidInputException("Guild level must be at least 1");
        }

        if (guild.getMemberCount() < 0) {
            throw new InvalidInputException("Member count cannot be negative");
        }
    }
}
