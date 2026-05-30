public class Player extends Person {
    private String position;
    private int jerseyNumber;
    private int goals;
    private int assists;
    private String currentTeamId;

    public Player(String id, String firstName, String lastName, int age, String position, int jerseyNumber) {
        super(id, firstName, lastName, age);
        this.position = position;
        this.jerseyNumber = jerseyNumber;
    }

    @Override
    public String getRole() {
        return "Player";
    }

    public String getCurrentTeamId() {
        return currentTeamId;
    }

    public void setCurrentTeamId(String currentTeamId) {
        this.currentTeamId = currentTeamId;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public int getJerseyNumber() {
        return jerseyNumber;
    }

    public void setJerseyNumber(int jerseyNumber) {
        this.jerseyNumber = jerseyNumber;
    }

    public int getGoals() {
        return goals;
    }

    public int getAssists() {
        return assists;
    }

    public void registerGoal() {
        goals++;
    }

    public void registerAssist() {
        assists++;
    }

    @Override
    public String toString() {
        return getFullName() + " (#" + jerseyNumber + ", " + position + ", G=" + goals + ", A=" + assists + ")";
    }
}

