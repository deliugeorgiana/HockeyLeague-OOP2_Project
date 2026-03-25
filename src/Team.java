import java.util.ArrayList;
import java.util.List;

public class Team {
    private String id;
    private String name;
    private String city;
    private Coach headCoach;
    private final List<Player> roster;

    public Team(String id, String name, String city) {
        this.id = id;
        this.name = name;
        this.city = city;
        this.roster = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public Coach getHeadCoach() {
        return headCoach;
    }

    public void setHeadCoach(Coach headCoach) {
        this.headCoach = headCoach;
    }

    public List<Player> getRoster() {
        return new ArrayList<>(roster);
    }

    public void addPlayer(Player player) {
        roster.add(player);
    }

    public boolean removePlayerById(String playerId) {
        return roster.removeIf(player -> player.getId().equals(playerId));
    }

    @Override
    public String toString() {
        return name + " (" + city + ")";
    }
}

