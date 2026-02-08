

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * Main Spring Boot Application
 * Entry point for the REST API
 */
@SpringBootApplication
public class CharacterManagementApplication {

    public static void main(String[] args) {
        SpringApplication.run(CharacterManagementApplication.class, args);

        System.out.println("\n╔════════════════════════════════════════════════════╗");
        System.out.println("║  Game Character Management API                     ║");
        System.out.println("║  REST API is running on http://localhost:8080      ║");
        System.out.println("╚════════════════════════════════════════════════════╝\n");

        System.out.println("Available Endpoints:");
        System.out.println("  GET    /api/characters       - Get all characters");
        System.out.println("  GET    /api/characters/{id}  - Get character by ID");
        System.out.println("  POST   /api/characters       - Create character");
        System.out.println("  PUT    /api/characters/{id}  - Update character");
        System.out.println("  DELETE /api/characters/{id}  - Delete character");
        System.out.println("  GET    /api/guilds           - Get all guilds");
        System.out.println("  POST   /api/guilds           - Create guild");
        System.out.println();
    }
}




