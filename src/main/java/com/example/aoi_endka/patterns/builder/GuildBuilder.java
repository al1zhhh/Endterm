package com.example.aoi_endka.patterns.builder;

import com.example.aoi_endka.model.Guild;
import com.example.aoi_endka.model.GameEntity;
import com.example.aoi_endka.patterns.singleton.LoggingService;
import com.example.aoi_endka.patterns.singleton.ConfigurationManager;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Builder Pattern Implementation for Guild
 * Purpose: Constructs complex Guild objects with multiple optional parameters
 * Provides fluent interface for readable guild creation
 */
public class GuildBuilder {
    
    private static final LoggingService logger = LoggingService.getInstance();
    private static final ConfigurationManager config = ConfigurationManager.getInstance();
    
    // Required parameters
    private String guildName;
    
    // Optional parameters with defaults
    private String description = "No description";
    private int level = 1;
    private List<GameEntity> members = new ArrayList<>();
    private LocalDateTime createdDate = LocalDateTime.now();
    private Integer id = null;
    private int maxMembers;
    
    /**
     * Private constructor
     */
    private GuildBuilder() {
        // Get max members from configuration
        this.maxMembers = config.getIntProperty("guild.max.members", 50);
    }
    
    /**
     * Start building a new guild
     */
    public static GuildBuilder newGuild() {
        return new GuildBuilder();
    }
    
    /**
     * Set guild name (REQUIRED)
     * @param name Guild name
     * @return this builder for chaining
     */
    public GuildBuilder withName(String name) {
        this.guildName = name;
        logger.debug("Guild name set to: " + name);
        return this;
    }
    
    /**
     * Set guild description (OPTIONAL)
     * @param description Guild description
     * @return this builder for chaining
     */
    public GuildBuilder withDescription(String description) {
        this.description = description;
        logger.debug("Guild description set");
        return this;
    }
    
    /**
     * Set guild level (OPTIONAL - defaults to 1)
     * @param level Guild level
     * @return this builder for chaining
     */
    public GuildBuilder atLevel(int level) {
        this.level = level;
        logger.debug("Guild level set to: " + level);
        return this;
    }
    
    /**
     * Add a member to the guild (OPTIONAL)
     * @param member GameEntity to add
     * @return this builder for chaining
     */
    public GuildBuilder withMember(GameEntity member) {
        if (members.size() >= maxMembers) {
            throw new IllegalStateException("Cannot add more members. Max: " + maxMembers);
        }
        this.members.add(member);
        logger.debug("Member added: " + member.getName());
        return this;
    }
    
    /**
     * Add multiple members to the guild (OPTIONAL)
     * @param membersList List of members to add
     * @return this builder for chaining
     */
    public GuildBuilder withMembers(List<GameEntity> membersList) {
        if (this.members.size() + membersList.size() > maxMembers) {
            throw new IllegalStateException("Cannot add members. Would exceed max: " + maxMembers);
        }
        this.members.addAll(membersList);
        logger.debug("Added " + membersList.size() + " members");
        return this;
    }
    
    /**
     * Set maximum number of members (OPTIONAL)
     * @param maxMembers Maximum member count
     * @return this builder for chaining
     */
    public GuildBuilder withMaxMembers(int maxMembers) {
        if (maxMembers < 1) {
            throw new IllegalArgumentException("Max members must be at least 1");
        }
        this.maxMembers = maxMembers;
        logger.debug("Max members set to: " + maxMembers);
        return this;
    }
    
    /**
     * Set creation date (OPTIONAL)
     * @param createdDate Creation timestamp
     * @return this builder for chaining
     */
    public GuildBuilder createdAt(LocalDateTime createdDate) {
        this.createdDate = createdDate;
        logger.debug("Creation date set to: " + createdDate);
        return this;
    }
    
    /**
     * Set database ID (OPTIONAL - for existing guilds)
     * @param id Database ID
     * @return this builder for chaining
     */
    public GuildBuilder withId(int id) {
        this.id = id;
        logger.debug("Guild ID set to: " + id);
        return this;
    }
    
    /**
     * Build the guild object
     * @return Fully constructed Guild
     * @throws IllegalStateException if required fields are missing
     */
    public Guild build() {
        // Validate required fields
        validateRequiredFields();
        
        logger.info("Building guild: " + guildName + " with " + members.size() + " members");
        
        // Create guild instance
        Guild guild = new Guild(guildName);
        
        // Set optional fields
        if (id != null) {
            guild.setId(id);
        }
        guild.setLevel(level);
        guild.setCreatedDate(createdDate);
        
        // Add members
        for (GameEntity member : members) {
            guild.addMember(member);
        }
        
        logger.info("Guild built successfully: " + guild.getGuildName());
        return guild;
    }
    
    /**
     * Validate required fields
     */
    private void validateRequiredFields() {
        if (guildName == null || guildName.trim().isEmpty()) {
            throw new IllegalStateException("Guild name is required. Use withName() method.");
        }
        
        if (guildName.length() < 3 || guildName.length() > 50) {
            throw new IllegalStateException("Guild name must be between 3 and 50 characters");
        }
        
        if (level < 1 || level > 100) {
            throw new IllegalStateException("Guild level must be between 1 and 100");
        }
    }
    
    /**
     * Reset builder to initial state
     */
    public GuildBuilder reset() {
        this.guildName = null;
        this.description = "No description";
        this.level = 1;
        this.members = new ArrayList<>();
        this.createdDate = LocalDateTime.now();
        this.id = null;
        this.maxMembers = config.getIntProperty("guild.max.members", 50);
        
        logger.debug("Guild builder reset");
        return this;
    }
    
    /**
     * Display current builder state
     */
    public void displayState() {
        System.out.println("\n========== Guild Builder State ==========");
        System.out.println("Name: " + (guildName != null ? guildName : "NOT SET"));
        System.out.println("Description: " + description);
        System.out.println("Level: " + level);
        System.out.println("Members: " + members.size() + "/" + maxMembers);
        System.out.println("ID: " + (id != null ? id : "NEW"));
        System.out.println("Created: " + createdDate);
        
        if (!members.isEmpty()) {
            System.out.println("\nMember List:");
            for (int i = 0; i < members.size(); i++) {
                System.out.println("  " + (i + 1) + ". " + members.get(i).getName());
            }
        }
        System.out.println("========================================\n");
    }
}
