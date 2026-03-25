public class StandingEntry {
    private Team team;
    private int played;
    private int wins;
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

    public int getWins() {
        return wins;
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
        return wins * 3;
    }

    public void registerGame(int goalsScored, int goalsAllowed) {
        played++;
        goalsFor += goalsScored;
        goalsAgainst += goalsAllowed;
        if (goalsScored > goalsAllowed) {
            wins++;
        } else {
            losses++;
        }
    }

    @Override
    public String toString() {
        return team.getName() + " | P=" + played + " W=" + wins + " L=" + losses + " GF=" + goalsFor + " GA=" + goalsAgainst + " Pts=" + getPoints();
    }
}

