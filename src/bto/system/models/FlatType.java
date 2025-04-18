package bto.system.models;

public class FlatType {
    private String type;
    private int availableUnits;

    public FlatType(String type, int availableUnits) {
        this.type = type;
        this.availableUnits = availableUnits;
    }

    // Getters
    public String getType() { return type; }
    public int getAvailableUnits() { return availableUnits; }

    // Setters
    public void setAvailableUnits(int units) {
        if (units < 0) throw new IllegalArgumentException("Units cannot be negative");
        this.availableUnits = units;
    }

    public void reduceAvailableUnits() {
        if (availableUnits > 0) {
            availableUnits--;
        }
    }
}