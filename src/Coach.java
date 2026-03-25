public class Coach extends Person {
    private int experienceYears;
    private String strategyStyle;

    public Coach(String id, String firstName, String lastName, int age, int experienceYears, String strategyStyle) {
        super(id, firstName, lastName, age);
        this.experienceYears = experienceYears;
        this.strategyStyle = strategyStyle;
    }

    @Override
    public String getRole() {
        return "Coach";
    }

    public int getExperienceYears() {
        return experienceYears;
    }

    public void setExperienceYears(int experienceYears) {
        this.experienceYears = experienceYears;
    }

    public String getStrategyStyle() {
        return strategyStyle;
    }

    public void setStrategyStyle(String strategyStyle) {
        this.strategyStyle = strategyStyle;
    }

    @Override
    public String toString() {
        return getFullName() + " (" + experienceYears + " years, style=" + strategyStyle + ")";
    }
}

