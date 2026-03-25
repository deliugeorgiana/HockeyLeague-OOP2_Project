import java.time.LocalDate;

public class Contract {
    private Player player;
    private Team team;
    private double yearlySalary;
    private LocalDate signedAt;
    private int durationYears;

    public Contract(Player player, Team team, double yearlySalary, LocalDate signedAt, int durationYears) {
        this.player = player;
        this.team = team;
        this.yearlySalary = yearlySalary;
        this.signedAt = signedAt;
        this.durationYears = durationYears;
    }

    public Player getPlayer() {
        return player;
    }

    public Team getTeam() {
        return team;
    }

    public double getYearlySalary() {
        return yearlySalary;
    }

    public void setYearlySalary(double yearlySalary) {
        this.yearlySalary = yearlySalary;
    }

    public LocalDate getSignedAt() {
        return signedAt;
    }

    public void setSignedAt(LocalDate signedAt) {
        this.signedAt = signedAt;
    }

    public int getDurationYears() {
        return durationYears;
    }

    public void setDurationYears(int durationYears) {
        this.durationYears = durationYears;
    }

    @Override
    public String toString() {
        return player.getFullName() + " -> " + team.getName() + " | salary=" + yearlySalary + " | years=" + durationYears;
    }
}

