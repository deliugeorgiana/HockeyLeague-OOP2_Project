public class Referee extends Person {
    private String licenseLevel;
    private int matchesOfficiated;

    public Referee(String id, String firstName, String lastName, int age, String licenseLevel) {
        super(id, firstName, lastName, age);
        this.licenseLevel = licenseLevel;
    }

    @Override
    public String getRole() {
        return "Referee";
    }

    public String getLicenseLevel() {
        return licenseLevel;
    }

    public void setLicenseLevel(String licenseLevel) {
        this.licenseLevel = licenseLevel;
    }

    public int getMatchesOfficiated() {
        return matchesOfficiated;
    }

    public void addOfficiatedMatch() {
        matchesOfficiated++;
    }

    @Override
    public String toString() {
        return getFullName() + " (license=" + licenseLevel + ", matches=" + matchesOfficiated + ")";
    }
}

