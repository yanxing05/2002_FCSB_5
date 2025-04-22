package bto.system.models;

public class FlatType {
    private String type;
    private int totalUnits;
    private int availableUnits;

    public FlatType(String type, int units) {
        this.type = type;
        this.totalUnits = units;
        this.availableUnits = units;
    }

    // Getters
    public String getType() { return type; }
    public int getAvailableUnits() { return availableUnits; }
    public int getTotalUnits() { return totalUnits; }

    // Setters
    public void setAvailableUnits(int units) {
        if (units < 0) {
            throw new IllegalArgumentException("Invalid available unit count");
        }
        // If increasing units, grow total capacity
        if (units > totalUnits) {
            totalUnits = units;
        }
        this.availableUnits = units;
    }


    public void reduceAvailableUnits() {
        if (availableUnits > 0) {
            availableUnits--;
        }
    }

    @Override
    public String toString() {
        return type;
    }
}
