package software.plusminus.admin.model;

public enum UiColor {

    PRIMARY,
    SUCCESS,
    DANGER,
    WARNING,
    INFO,
    LIGHT,
    DARK;

    @Override
    public String toString() {
        return name().toLowerCase();
    }

}
