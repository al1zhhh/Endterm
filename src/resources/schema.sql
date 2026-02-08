
\c game_character_db;


DROP TABLE IF EXISTS equipment CASCADE;
DROP TABLE IF EXISTS character_attributes CASCADE;
DROP TABLE IF EXISTS characters CASCADE;
DROP TABLE IF EXISTS guilds CASCADE;


CREATE TABLE guilds (
                        id SERIAL PRIMARY KEY,
                        guild_name VARCHAR(100) NOT NULL UNIQUE,
                        level INTEGER DEFAULT 1 CHECK (level >= 1),
                        member_count INTEGER DEFAULT 0 CHECK (member_count >= 0),
                        created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
);

CREATE INDEX idx_guild_name ON guilds(guild_name);

CREATE TABLE characters (
                            id SERIAL PRIMARY KEY,
                            name VARCHAR(100) NOT NULL,
                            character_type VARCHAR(20) NOT NULL CHECK (character_type IN ('WARRIOR', 'MAGE', 'ROGUE')),
                            level INTEGER DEFAULT 1 CHECK (level BETWEEN 1 AND 100),
                            experience INTEGER DEFAULT 0 CHECK (experience >= 0),
                            health_points INTEGER NOT NULL CHECK (health_points > 0),
                            guild_id INTEGER,
                            created_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,

                            CONSTRAINT fk_guild FOREIGN KEY (guild_id)
                                REFERENCES guilds(id) ON DELETE SET NULL
);

CREATE INDEX idx_character_name ON characters(name);
CREATE INDEX idx_character_type ON characters(character_type);
CREATE INDEX idx_guild_id ON characters(guild_id);


CREATE TABLE character_attributes (
                                      character_id INTEGER PRIMARY KEY,

    -- Warrior attributes
                                      strength INTEGER,
                                      armor INTEGER,
                                      weapon_type VARCHAR(50),

    -- Mage attributes
                                      mana INTEGER,
                                      intelligence INTEGER,
                                      spell_school VARCHAR(50),

    -- Rogue attributes
                                      agility INTEGER,
                                      stealth INTEGER,
                                      critical_chance NUMERIC(5,2),

                                      CONSTRAINT fk_character_attr FOREIGN KEY (character_id)
                                          REFERENCES characters(id) ON DELETE CASCADE
);


CREATE TABLE equipment (
                           id SERIAL PRIMARY KEY,
                           name VARCHAR(100) NOT NULL,
                           equipment_type VARCHAR(50) NOT NULL CHECK (equipment_type IN ('WEAPON', 'ARMOR', 'ACCESSORY')),
                           bonus_stats VARCHAR(255),
                           rarity VARCHAR(20) NOT NULL CHECK (rarity IN ('COMMON', 'RARE', 'EPIC', 'LEGENDARY')),
                           character_id INTEGER,

                           CONSTRAINT fk_character_equip FOREIGN KEY (character_id)
                               REFERENCES characters(id) ON DELETE SET NULL
);

CREATE INDEX idx_equipment_type ON equipment(equipment_type);
CREATE INDEX idx_character_equipment ON equipment(character_id);



-- Insert Guilds
INSERT INTO guilds (guild_name, level, member_count) VALUES
                                                         ('Dragon Slayers', 5, 0),
                                                         ('Shadow Brotherhood', 3, 0),
                                                         ('Arcane Order', 7, 0),
                                                         ('Iron Legion', 4, 0);

-- Insert Warriors
INSERT INTO characters (name, character_type, level, experience, health_points, guild_id) VALUES
                                                                                              ('Thorin Ironshield', 'WARRIOR', 10, 5000, 150, 1),
                                                                                              ('Grom Hellscream', 'WARRIOR', 8, 3200, 140, 4);

INSERT INTO character_attributes (character_id, strength, armor, weapon_type) VALUES
                                                                                  (1, 50, 30, 'Great Sword'),
                                                                                  (2, 45, 28, 'Battle Axe');

-- Insert Mages
INSERT INTO characters (name, character_type, level, experience, health_points, guild_id) VALUES
                                                                                              ('Gandalf the Wise', 'MAGE', 15, 12000, 80, 3),
                                                                                              ('Jaina Proudmoore', 'MAGE', 12, 8500, 75, 3);

INSERT INTO character_attributes (character_id, mana, intelligence, spell_school) VALUES
                                                                                      (3, 200, 45, 'Fire Magic'),
                                                                                      (4, 180, 42, 'Frost Magic');

-- Insert Rogues
INSERT INTO characters (name, character_type, level, experience, health_points, guild_id) VALUES
                                                                                              ('Shadow Blade', 'ROGUE', 8, 3500, 100, 2),
                                                                                              ('Valeera Sanguinar', 'ROGUE', 9, 4200, 95, 2);

INSERT INTO character_attributes (character_id, agility, stealth, critical_chance) VALUES
                                                                                       (5, 40, 35, 0.25),
                                                                                       (6, 42, 38, 0.28);

-- Update guild member counts
UPDATE guilds SET member_count = 1 WHERE id = 1;
UPDATE guilds SET member_count = 2 WHERE id = 2;
UPDATE guilds SET member_count = 2 WHERE id = 3;
UPDATE guilds SET member_count = 1 WHERE id = 4;

-- Insert Equipment
INSERT INTO equipment (name, equipment_type, bonus_stats, rarity, character_id) VALUES
                                                                                    ('Frostmourne', 'WEAPON', '+50 Strength, +20 Critical', 'LEGENDARY', 1),
                                                                                    ('Dragon Scale Armor', 'ARMOR', '+40 Armor, +10 Health', 'EPIC', 1),
                                                                                    ('Staff of the Archmage', 'WEAPON', '+60 Intelligence, +50 Mana', 'LEGENDARY', 3),
                                                                                    ('Shadowstep Boots', 'ARMOR', '+15 Agility, +20 Stealth', 'RARE', 5),
                                                                                    ('Ring of Power', 'ACCESSORY', '+10 All Stats', 'EPIC', 3);


