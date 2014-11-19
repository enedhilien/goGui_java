package main;

public enum PointClassification {
    START("green"), END("red"), MERGE("blue"), SPLIT("yellow"), REGULAR("brown");

    private String color;

    PointClassification(String green) {
        this.color = green;
    }

    public String getColor() {
        return color;
    }
}
