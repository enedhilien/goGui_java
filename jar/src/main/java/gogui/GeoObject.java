package gogui;

public abstract class GeoObject {

    private Status status = Status.NORMAL;
    private String color;

    public enum Status {
        NORMAL("black"), ACTIVE("red"), PROCESSED("green");

        private String color;

        Status(String black) {
            color = black;
        }

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }

        public String getColor() {
            return color;
        }

    }
    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        status = s;
        color = null;
    }

    public void activate() {
        setStatus(Status.ACTIVE);
    }

    public void processed() {
        setStatus(Status.PROCESSED);
    }

    public void normalize() {
        setStatus(Status.NORMAL);
    }

    public String getColor() {
        if (hasCustomColor()) {
            return color;
        } else {
            return status.getColor();
        }
    }

    public boolean hasCustomColor() {
        return color != null && !color.isEmpty();
    }

    public void setColor(String color) {
        this.color = color;
    }
}
