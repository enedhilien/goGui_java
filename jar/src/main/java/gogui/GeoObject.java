package gogui;

public abstract class GeoObject {

    private Status status = Status.Normal;

    public enum Status {
        Normal, Active, Processed;

        @Override
        public String toString() {
            return this.name().toLowerCase();
        }
    }

    public Status getStatus() {
        return status;
    }

    public void setStatus(Status s) {
        status = s;
    }

    public void activate() {
        status = Status.Active;
    }

    public void processed() {
        status = Status.Processed;
    }

    public void normalize() {
        status = Status.Normal;
    }


}
