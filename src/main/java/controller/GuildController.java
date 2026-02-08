package controller;

import model.Guild;
import service.GuildService;
import exceptions.*;

import java.util.List;
import java.util.Scanner;

/**
 * Controller layer for Guild operations
 * Handles user input/output and delegates to service layer
 */
public class GuildController {
    private GuildService guildService;
    private Scanner scanner;

    public GuildController() {
        this.guildService = new GuildService();
        this.scanner = new Scanner(System.in);
    }

    public GuildController(Scanner scanner) {
        this.guildService = new GuildService();
        this.scanner = scanner;
    }


    public void createGuild() {
        try {
            System.out.println("\n========== CREATE GUILD ==========");
            System.out.print("Enter guild name (3-100 characters): ");
            String name = scanner.nextLine();

            Guild guild = new Guild(name);
            int id = guildService.createGuild(guild);

            System.out.println("\n✓ SUCCESS: Guild created with ID: " + id);
            guild.displayInfo();

        } catch (DuplicateResourceException e) {
            System.err.println("✗ DUPLICATE ERROR: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.err.println("✗ VALIDATION ERROR: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    public void getAllGuilds() {
        try {
            System.out.println("\n========== ALL GUILDS ==========");
            List<Guild> guilds = guildService.getAllGuilds();

            if (guilds.isEmpty()) {
                System.out.println("No guilds found in database.");
                return;
            }

            System.out.println("Total guilds: " + guilds.size());
            System.out.println();

            for (Guild guild : guilds) {
                guild.displayInfo();
                System.out.println();
            }

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }


    public void getGuildById() {
        try {
            System.out.print("\nEnter guild ID: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n========== GUILD DETAILS ==========");
            Guild guild = guildService.getGuildById(id);
            guild.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    /**
     * UPDATE - Update guild
     */
    public void updateGuild() {
        try {
            System.out.print("\nEnter guild ID to update: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // Get current guild
            Guild guild = guildService.getGuildById(id);
            System.out.println("\n--- Current Guild ---");
            guild.displayInfo();

            System.out.println("\n--- Update Guild ---");
            System.out.print("New name (or press Enter to keep current): ");
            String newName = scanner.nextLine();
            if (!newName.trim().isEmpty()) {
                guild.setGuildName(newName);
            }

            System.out.print("New level (or 0 to keep current): ");
            int newLevel = scanner.nextInt();
            scanner.nextLine();
            if (newLevel > 0) {
                guild.setLevel(newLevel);
            }

            guildService.updateGuild(id, guild);
            System.out.println("\n✓ SUCCESS: Guild updated!");
            guild.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (InvalidInputException e) {
            System.err.println("✗ VALIDATION ERROR: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    /**
     * DELETE - Delete guild
     */
    public void deleteGuild() {
        try {
            System.out.print("\nEnter guild ID to delete: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            // Show guild before deletion
            Guild guild = guildService.getGuildById(id);
            System.out.println("\n--- Guild to Delete ---");
            guild.displayInfo();

            if (guild.getMemberCount() > 0) {
                System.out.println("\n⚠ WARNING: This guild has " + guild.getMemberCount() + " member(s)!");
                System.out.println("Business rule: Cannot delete a guild with active members.");
                System.out.println("Please remove all members first.");
                return;
            }

            System.out.print("\nAre you sure you want to delete this guild? (yes/no): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("yes")) {
                guildService.deleteGuild(id);
                System.out.println("\n✓ SUCCESS: Guild deleted!");
            } else {
                System.out.println("Deletion cancelled.");
            }

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    /**
     * Add character to guild (Composition demonstration)
     */
    public void addCharacterToGuild() {
        try {
            System.out.println("\n========== ADD CHARACTER TO GUILD ==========");
            System.out.print("Enter character ID: ");
            int characterId = scanner.nextInt();

            System.out.print("Enter guild ID: ");
            int guildId = scanner.nextInt();
            scanner.nextLine();

            guildService.addCharacterToGuild(characterId, guildId);

            System.out.println("\n✓ SUCCESS: Character added to guild!");

            // Show updated guild
            Guild guild = guildService.getGuildById(guildId);
            guild.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    /**
     * Remove character from guild
     */
    public void removeCharacterFromGuild() {
        try {
            System.out.println("\n========== REMOVE CHARACTER FROM GUILD ==========");
            System.out.print("Enter character ID: ");
            int characterId = scanner.nextInt();
            scanner.nextLine();

            guildService.removeCharacterFromGuild(characterId);
            System.out.println("\n✓ SUCCESS: Character removed from guild!");

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }


    public void levelUpGuild() {
        try {
            System.out.print("\nEnter guild ID to level up: ");
            int id = scanner.nextInt();
            scanner.nextLine();

            System.out.println("\n========== LEVELING UP GUILD ==========");
            guildService.levelUpGuild(id);

            // Show updated guild
            Guild guild = guildService.getGuildById(id);
            guild.displayInfo();

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }

    public void showGuildStatistics() {
        try {
            System.out.println("\n========== GUILD STATISTICS ==========");
            List<Guild> guilds = guildService.getAllGuilds();

            if (guilds.isEmpty()) {
                System.out.println("No guilds in database.");
                return;
            }

            int totalMembers = 0;
            int totalLevel = 0;
            Guild largestGuild = null;
            int maxMembers = 0;
            Guild highestLevel = null;
            int maxLevel = 0;

            for (Guild guild : guilds) {
                totalMembers += guild.getMemberCount();
                totalLevel += guild.getLevel();

                if (guild.getMemberCount() > maxMembers) {
                    maxMembers = guild.getMemberCount();
                    largestGuild = guild;
                }

                if (guild.getLevel() > maxLevel) {
                    maxLevel = guild.getLevel();
                    highestLevel = guild;
                }
            }

            System.out.println("Total Guilds: " + guilds.size());
            System.out.println("Total Members Across All Guilds: " + totalMembers);
            System.out.println("Average Guild Level: " + (guilds.size() > 0 ? totalLevel / guilds.size() : 0));

            if (largestGuild != null) {
                System.out.println("\nLargest Guild:");
                System.out.println("  Name: " + largestGuild.getGuildName());
                System.out.println("  Members: " + largestGuild.getMemberCount());
            }

            if (highestLevel != null) {
                System.out.println("\nHighest Level Guild:");
                System.out.println("  Name: " + highestLevel.getGuildName());
                System.out.println("  Level: " + highestLevel.getLevel());
            }

        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        }
    }


    public void viewGuildMembers() {
        try {
            System.out.print("\nEnter guild ID: ");
            int guildId = scanner.nextInt();
            scanner.nextLine();

            Guild guild = guildService.getGuildById(guildId);
            System.out.println("\n========== GUILD MEMBERS ==========");
            guild.displayInfo();

            // Note: This would require a method in GuildService to get members
            // For now, we just show the guild info
            System.out.println("\nTo see individual members, use 'View All Characters' and filter by guild.");

        } catch (ResourceNotFoundException e) {
            System.err.println("✗ NOT FOUND: " + e.getMessage());
        } catch (DatabaseOperationException e) {
            System.err.println("✗ DATABASE ERROR: " + e.getMessage());
        } catch (Exception e) {
            System.err.println("✗ ERROR: " + e.getMessage());
        }
    }
}