import java.util.HashMap;

import javafx.scene.control.Button;

public class GButton extends Button {

    protected double oldPositionX;
    protected double oldPositionY;
    protected String onPath;
    protected String offPath;
    protected boolean on;
    protected double moveLeft;
    protected boolean spwanedOutside;
    protected int lightID;
    protected String LightName;
    protected String LightFunction;
    private HashMap<Lights, String[]> LightDic;

    public GButton() {
        super();

        LightDic = new HashMap<>();
        // SubLights all get a know weather they are on or off and they can tell you
        // their current image path
        LightDic.put(Lights.SWITCH, new String[] { "SwitchOn.png", "SwitchOff.png" });
        // TODO add other filepaths

        oldPositionX = 0;
        oldPositionY = 0;
        LightDic = new HashMap<>();
        // SubLights all get a know weather they are on or off and they can tell you
        // their current image path
        LightDic.put(Lights.SWITCH, new String[] { "SwitchOn.png", "SwitchOff.png" });
    }

    public GButton(int lightID, boolean on, String name, String function, Lights type) {
        super();

        LightDic = new HashMap<>();
        // SubLights all get a know weather they are on or off and they can tell you
        // their current image path
        LightDic.put(Lights.SWITCH, new String[] { "SwitchOn.png", "SwitchOff.png" });
        // TODO add other filepaths

        this.offPath = LightDic.get(type)[0];
        this.onPath = LightDic.get(type)[1];
        this.on = on;
        moveLeft = 1;
        this.lightID = lightID;
        this.LightName = name;
        this.LightFunction = function;

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

    public void turnValueTo(boolean on) {
        if (on) {
            turnOn();
        } else {
            turnOff();
        }
    }

    public void turnOn() {
        on = true;
        DBTest db = new DBTest();
        db.makeGETRequest("https://studev.groept.be/api/a23ib2b05/update_value_of/" + 1 + "/" + lightID);
    }

    public void turnOff() {
        on = false;
        DBTest db = new DBTest();
        db.makeGETRequest("https://studev.groept.be/api/a23ib2b05/update_value_of/" + 0 + "/" + lightID);
    }

    public int getLightID() {
        return lightID;
    }
}
