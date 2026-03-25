public class LeagueSeason {
    private String seasonLabel;
    private int startYear;
    private int endYear;

    public LeagueSeason(String seasonLabel, int startYear, int endYear) {
        this.seasonLabel = seasonLabel;
        this.startYear = startYear;
        this.endYear = endYear;
    }

    public String getSeasonLabel() {
        return seasonLabel;
    }

    public void setSeasonLabel(String seasonLabel) {
        this.seasonLabel = seasonLabel;
    }

    public int getStartYear() {
        return startYear;
    }

    public void setStartYear(int startYear) {
        this.startYear = startYear;
    }

    public int getEndYear() {
        return endYear;
    }

    public void setEndYear(int endYear) {
        this.endYear = endYear;
    }

    @Override
    public String toString() {
        return seasonLabel + " (" + startYear + "-" + endYear + ")";
    }
}

