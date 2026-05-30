-- Hockey League Database Schema (pgAdmin-friendly)
-- Run this script while connected to database: hockey_league

-- Reset schema objects (safe re-run order)
DROP TABLE IF EXISTS standing_entry CASCADE;
DROP TABLE IF EXISTS contract CASCADE;
DROP TABLE IF EXISTS match CASCADE;
DROP TABLE IF EXISTS team_coach CASCADE;
DROP TABLE IF EXISTS referee CASCADE;
DROP TABLE IF EXISTS coach CASCADE;
DROP TABLE IF EXISTS player CASCADE;
DROP TABLE IF EXISTS person CASCADE;
DROP TABLE IF EXISTS team CASCADE;
DROP TABLE IF EXISTS arena CASCADE;
DROP TABLE IF EXISTS league_season CASCADE;

-- Arena Table
CREATE TABLE arena (
    id SERIAL PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL,
    capacity INT NOT NULL
);

-- Team Table
CREATE TABLE team (
    id VARCHAR(20) PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    city VARCHAR(100) NOT NULL
);

-- Person Table (base for inheritance)
CREATE TABLE person (
    id VARCHAR(20) PRIMARY KEY,
    first_name VARCHAR(100) NOT NULL,
    last_name VARCHAR(100) NOT NULL,
    age INT NOT NULL,
    person_type VARCHAR(20) NOT NULL, -- 'PLAYER', 'COACH', 'REFEREE'
    team_id VARCHAR(20),
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE SET NULL
);

-- Player-specific attributes
CREATE TABLE player (
    id VARCHAR(20) PRIMARY KEY,
    position VARCHAR(50) NOT NULL,
    jersey_number INT NOT NULL,
    goals INT DEFAULT 0,
    assists INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE
);

-- Coach-specific attributes
CREATE TABLE coach (
    id VARCHAR(20) PRIMARY KEY,
    experience_years INT NOT NULL,
    strategy_style VARCHAR(100) NOT NULL,
    FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE
);

-- Referee-specific attributes
CREATE TABLE referee (
    id VARCHAR(20) PRIMARY KEY,
    license_level VARCHAR(50) NOT NULL,
    matches_officiated INT DEFAULT 0,
    FOREIGN KEY (id) REFERENCES person(id) ON DELETE CASCADE
);

-- Team-Coach relationship
CREATE TABLE team_coach (
    team_id VARCHAR(20) PRIMARY KEY,
    coach_id VARCHAR(20) NOT NULL,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE,
    FOREIGN KEY (coach_id) REFERENCES coach(id) ON DELETE CASCADE
);

-- Contract Table
CREATE TABLE contract (
    id SERIAL PRIMARY KEY,
    player_id VARCHAR(20) NOT NULL,
    team_id VARCHAR(20) NOT NULL,
    yearly_salary DOUBLE PRECISION NOT NULL,
    signed_at DATE NOT NULL,
    duration_years INT NOT NULL,
    FOREIGN KEY (player_id) REFERENCES player(id) ON DELETE CASCADE,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE
);

-- League Season Table
CREATE TABLE league_season (
    id SERIAL PRIMARY KEY,
    season_label VARCHAR(100) NOT NULL,
    start_year INT NOT NULL,
    end_year INT NOT NULL
);

-- Match Table
CREATE TABLE match (
    id VARCHAR(20) PRIMARY KEY,
    home_team_id VARCHAR(20) NOT NULL,
    away_team_id VARCHAR(20) NOT NULL,
    arena_id INT NOT NULL,
    match_date DATE NOT NULL,
    is_played BOOLEAN DEFAULT FALSE,
    home_goals INT DEFAULT 0,
    away_goals INT DEFAULT 0,
    FOREIGN KEY (home_team_id) REFERENCES team(id) ON DELETE CASCADE,
    FOREIGN KEY (away_team_id) REFERENCES team(id) ON DELETE CASCADE,
    FOREIGN KEY (arena_id) REFERENCES arena(id) ON DELETE CASCADE
);

-- Standing Entry Table
CREATE TABLE standing_entry (
    id SERIAL PRIMARY KEY,
    team_id VARCHAR(20) NOT NULL UNIQUE,
    matches_played INT DEFAULT 0,
    wins INT DEFAULT 0,
    draws INT DEFAULT 0,
    losses INT DEFAULT 0,
    goals_for INT DEFAULT 0,
    goals_against INT DEFAULT 0,
    points INT DEFAULT 0,
    FOREIGN KEY (team_id) REFERENCES team(id) ON DELETE CASCADE
);

-- Indexes for better performance
CREATE INDEX idx_match_home_team ON match(home_team_id);
CREATE INDEX idx_match_away_team ON match(away_team_id);
CREATE INDEX idx_contract_player ON contract(player_id);
CREATE INDEX idx_contract_team ON contract(team_id);
CREATE INDEX idx_person_team ON person(team_id);
CREATE INDEX idx_standing_team ON standing_entry(team_id);

