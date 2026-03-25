import java.time.LocalDate;

public class Match {
    private String id;
    private Team homeTeam;
    private Team awayTeam;
    private Arena arena;
    private LocalDate date;
    private boolean played;
    private int homeGoals;
    private int awayGoals;

    public Match(String id, Team homeTeam, Team awayTeam, Arena arena, LocalDate date) {
        this.id = id;
        this.homeTeam = homeTeam;
        this.awayTeam = awayTeam;
        this.arena = arena;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public Team getHomeTeam() {
        return homeTeam;
    }

    public Team getAwayTeam() {
        return awayTeam;
    }

    public Arena getArena() {
        return arena;
    }

    public LocalDate getDate() {
        return date;
    }

    public boolean isPlayed() {
        return played;
    }

    public int getHomeGoals() {
        return homeGoals;
    }

    public int getAwayGoals() {
        return awayGoals;
    }

    public void setResult(int homeGoals, int awayGoals) {
        this.played = true;
        this.homeGoals = homeGoals;
        this.awayGoals = awayGoals;
    }

    @Override
    public String toString() {
        String score = played ? (homeGoals + "-" + awayGoals) : "vs";
        return date + " | " + homeTeam.getName() + " " + score + " " + awayTeam.getName() + " @ " + arena.getName();
    }
}

