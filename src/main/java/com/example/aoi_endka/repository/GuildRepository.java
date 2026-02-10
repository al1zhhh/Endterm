package com.example.aoi_endka.repository;

import com.example.aoi_endka.model.Guild;
import com.example.aoi_endka.repository.interfaces.CrudRepository;
import com.example.aoi_endka.exceptions.DatabaseOperationException;
import com.example.aoi_endka.exceptions.ResourceNotFoundException;
import com.example.aoi_endka.utils.DatabaseConnection;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Repository for Guild CRUD operations
 */
public class GuildRepository implements CrudRepository<Guild>  {
    @Override
    public int create(Guild entity) throws DatabaseOperationException {
        String sql = "INSERT INTO guilds (guild_name, level, member_count) VALUES (?, ?, ?)";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS);
            ps.setString(1, entity.getGuildName());
            ps.setInt(2, entity.getLevel());
            ps.setInt(3, entity.getMemberCount());

            int affected = ps.executeUpdate();
            if (affected == 0) {
                throw new DatabaseOperationException("Creating guild failed, no rows affected.");
            }

            rs = ps.getGeneratedKeys();
            if (rs.next()) {
                int id = rs.getInt(1);
                entity.setId(id);
                System.out.println("Guild created successfully with ID: " + id);
                return id;
            }

            throw new DatabaseOperationException("Creating guild failed, no ID obtained.");

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to create guild: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps);
        }
    }

    public List<Guild> getAll() throws DatabaseOperationException {
        String sql = "SELECT * FROM guilds";
        List<Guild> guilds = new ArrayList<>();

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            rs = ps.executeQuery();

            while (rs.next()) {
                Guild guild = buildGuildFromResultSet(rs);
                guilds.add(guild);
            }

            return guilds;

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve guilds: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps);
        }
    }

    public Guild getById(int id) throws DatabaseOperationException, ResourceNotFoundException {
        String sql = "SELECT * FROM guilds WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);
            rs = ps.executeQuery();

            if (rs.next()) {
                return buildGuildFromResultSet(rs);
            } else {
                throw new ResourceNotFoundException("Guild with ID " + id + " not found");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to retrieve guild: " + e.getMessage(), e);
        } finally {
            closeResources(rs, ps);
        }
    }

    public void update(int id, Guild guild) throws DatabaseOperationException, ResourceNotFoundException {
        getById(id); // Check if exists

        String sql = "UPDATE guilds SET guild_name = ?, level = ?, member_count = ? WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setString(1, guild.getGuildName());
            ps.setInt(2, guild.getLevel());
            ps.setInt(3, guild.getMemberCount());
            ps.setInt(4, id);

            ps.executeUpdate();
            System.out.println("Guild updated successfully!");

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to update guild: " + e.getMessage(), e);
        } finally {
            closeResources(null, ps);
        }
    }

    public void delete(int id) throws DatabaseOperationException, ResourceNotFoundException {
        Guild guild = getById(id);

        // Business rule: cannot delete guild with members
        if (guild.getMemberCount() > 0) {
            throw new DatabaseOperationException("Cannot delete guild with active members. Remove all members first.");
        }

        String sql = "DELETE FROM guilds WHERE id = ?";

        Connection conn = null;
        PreparedStatement ps = null;

        try {
            conn = DatabaseConnection.getConnection();
            ps = conn.prepareStatement(sql);
            ps.setInt(1, id);

            int affected = ps.executeUpdate();
            if (affected > 0) {
                System.out.println("Guild deleted successfully!");
            }

        } catch (SQLException e) {
            throw new DatabaseOperationException("Failed to delete guild: " + e.getMessage(), e);
        } finally {
            closeResources(null, ps);
        }
    }

    private Guild buildGuildFromResultSet(ResultSet rs) throws SQLException {
        int id = rs.getInt("id");
        String guildName = rs.getString("guild_name");
        int level = rs.getInt("level");
        int memberCount = rs.getInt("member_count");
        Timestamp ts = rs.getTimestamp("created_date");
        LocalDateTime createdDate = ts != null ? ts.toLocalDateTime() : LocalDateTime.now();

        return new Guild(id, guildName, level, memberCount, createdDate);
    }

    private void closeResources(ResultSet rs, PreparedStatement ps) {
        if (rs != null) {
            try {
                rs.close();
            } catch (SQLException e) {
                System.err.println("Error closing ResultSet: " + e.getMessage());
            }
        }
        if (ps != null) {
            try {
                ps.close();
            } catch (SQLException e) {
                System.err.println("Error closing PreparedStatement: " + e.getMessage());
            }
        }
    }
}
