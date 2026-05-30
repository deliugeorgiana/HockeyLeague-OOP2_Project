public class StandingEntry {
    private Team team;
    private int played;
    private int wins;
    private int draws;
    private int losses;
    private int goalsFor;
    private int goalsAgainst;

    public StandingEntry(Team team) {
        this.team = team;
    }

    public Team getTeam() {
        return team;
    }

    public int getPlayed() {
        return played;
    }
    
    public int getMatchesPlayed() {
        return played;
    }

    public int getWins() {
        return wins;
    }

    public int getDraws() {
        return draws;
    }

    public int getLosses() {
        return losses;
    }

    public int getGoalsFor() {
        return goalsFor;
    }

    public int getGoalsAgainst() {
        return goalsAgainst;
    }

    public int getGoalDifference() {
        return goalsFor - goalsAgainst;
    }

    public int getPoints() {
        return wins * 3 + draws;
    }
    
    // Setters for DAO
    public void setMatchesPlayed(int played) {
        this.played = played;
    }

    public void setWins(int wins) {
        this.wins = wins;
    }

    public void setDraws(int draws) {
        this.draws = draws;
    }

    public void setLosses(int losses) {
        this.losses = losses;
    }

    public void setGoalsFor(int goalsFor) {
        this.goalsFor = goalsFor;
    }

    public void setGoalsAgainst(int goalsAgainst) {
        this.goalsAgainst = goalsAgainst;
    }

    public void registerGame(int goalsScored, int goalsAllowed) {
        played++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        if (goalsScored > goalsAllowed) {
            wins++;
        } else if (goalsScored == goalsAllowed) {
            draws++;
        } else {
            losses++;
        }
    }

    @Override
    public String toString() {
        return team.getName() + " | P=" + played + " W=" + wins + " L=" + losses + " GF=" + goalsFor + " GA=" + goalsAgainst + " Pts=" + getPoints();
    }
}

