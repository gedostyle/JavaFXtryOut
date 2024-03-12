import javafx.scene.control.Button;

public class GButton extends Button {

    protected double oldPositionX;
    protected double oldPositionY;
    protected String onPath;
    protected String offPath;
    protected boolean on;
    protected double moveLeft;
    protected boolean spwanedOutside;

    public GButton() {
        super();
        oldPositionX = 0;
        oldPositionY = 0;
    }

    public GButton(String onPath, String offPath, boolean on) {
        super();
        this.onPath = onPath;
        this.offPath = offPath;
        this.on = on;
        moveLeft = 1;
    }

    public boolean isOn() {
        return this.on;
    }

    public boolean getOn() {
        return this.on;
    }

    public void setOn(boolean on) {
        this.on = on;
    }

    public void setOnPath(String onPath) {
        this.onPath = onPath;
    }

    public void setOffPath(String offPath) {
        this.offPath = offPath;
    }

    public String getOnPath() {
        return this.onPath;
    }

    public String getOffPath() {
        return this.offPath;
    }

    public void rememberPositionX() {
        oldPositionX = this.getLayoutX();
    }

    public void rememberPositionY() {
        oldPositionY = this.getLayoutY();
    }

    public double getOldPositionX() {
        return oldPositionX;
    }

    public double getOldPositionY() {
        return oldPositionY;
    }

    public void moveLeft() {
        moveLeft++;
    }

    public double getMoveLeft() {
        return moveLeft;
    }

    public void setSpawnedOutside(boolean outside) {
        spwanedOutside = outside;
    }

    public boolean getSpawnedOutside() {
        return spwanedOutside;
    }
}
