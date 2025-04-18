package bto.system.models;

public class Neighborhood {
    private String name;
    private String region;

    public Neighborhood(String name) {
        this.name = name;
    }

    public String getName() { return name; }
    public String getRegion() { return region; }
    public void setRegion(String region) { this.region = region; }
}
