
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONObject;

import javafx.animation.FadeTransition;
import javafx.animation.PauseTransition;
import javafx.animation.SequentialTransition;
import javafx.animation.TranslateTransition;
import javafx.util.Duration;
import javafx.scene.layout.Region;
import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.StageStyle;
import javafx.scene.media.AudioClip;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.scene.text.TextAlignment;

import java.io.File;

public class test extends Application {
    private final int Height = 480;
    private final int Width = 800;
    private boolean lightSubModule;
    private boolean lightOn;
    private boolean musicPlaying;
    private double animationSpeed;
    private boolean onPi = false;
    private boolean keySubmenu;
    private Text textField;
    private Pane root;

    // Only can use my type of Top G button here
    private ArrayList<GButton> buttons;
    private ArrayList<GButton> lightSubButtons;
    private GButton myLightSwitch;
    private GButton myMusicPlayer;
    private GButton myKey;
    private GButton addKey;

    private GButton myLightClose;

    private GButton myMenu;
    private int numOfKeys = 0;
    // private int MenuAnimationDuration;

    private Circle colorPreviewWindow;
    private int buttonSize;
    private int subLightSize = 100;
    private double xOffset = 0;
    private double yOffset = 0;
    private boolean draggable = false;

    // ! I think the way the subbuttons are initialized might be wrong

    // ! Make sure the subbutons always know what value they are supposed to be on

    // ! make a method that returns if all lights are on after each subbutton press
    // ! or update that can also toggle the main switch

    @Override
    public void start(Stage primaryStage) {

        // General setup
        // ---------------------------

        buttonSize = 165;
        lightOn = false;
        lightSubModule = false;
        musicPlaying = false;
        // MenuAnimationDuration = 1000;
        buttons = new ArrayList<>();
        buttons.clear();
        lightSubButtons = new ArrayList<>();
        lightSubButtons.clear();
        keySubmenu = false;
        animationSpeed = .2;
        textField = new Text();

        // ---------------------------

        // Create a root node for the scene
        root = new Pane();

        // Set the background color of the Pane
        root.setStyle("-fx-background-color: rgb(237, 237, 241);");

        // Set the size of the root node
        root.setPrefSize(Width, Height); // Set the size to match your touchscreen resolution

        // Create the scene with the root node
        Scene scene = new Scene(root);

        // Set the scene on the stage
        primaryStage.setScene(scene);

        // Set the size of the root node
        // Set the stage style to UNDECORATED
        primaryStage.initStyle(StageStyle.UNDECORATED);
        if (onPi)
            // Set the stage to full screen
            primaryStage.setFullScreen(true);

        // Set the stage size and prevent resizing
        primaryStage.setResizable(false);
        primaryStage.setOnShown(e -> primaryStage.sizeToScene()); // Adjust the window size to match the scene

        // Background image--------------------
        Image image = new Image("70_perc_background.png");
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(Width);
        imageView.setFitHeight(Height);
        imageView.setOpacity(0);
        root.getChildren().addAll(imageView);
        // ------------------------------------

        // *This is the general setup */

        // ! Here you add your new buttons

        // * Examplified my Lightswitch button */

        myLightSwitch = new GButton();
        createImageButton(myLightSwitch, buttonSize, buttonSize, 100, 125, "bulb2.png");

        myLightClose = new GButton();
        createImageSubButton(myLightClose, subLightSize / 2, subLightSize / 2, Width, subLightSize / 4, "Close.png"); // !
                                                                                                                      // This
                                                                                                                      // is
        // still too
        // big

        myLightClose.setOpacity(.5);
        root.getChildren().add(myLightClose);
        myLightSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!draggable) {
                    // Light switch toggle

                    // Goes into the submodule and creates the subbuttons
                    if (lightSubModule) {
                        if (lightOn) {
                            lightOn = false;
                            for (GButton b : lightSubButtons) {
                                b.turnOff();
                                toggleSubButtonImage(b);
                            }
                            toggleButtonImage(myLightSwitch, "bulb2.png");
                        } else {
                            lightOn = true;
                            for (GButton b : lightSubButtons) {
                                b.turnOn();
                                toggleSubButtonImage(b);
                            }
                            toggleButtonImage(myLightSwitch, "bulb_on.png");
                        }
                    }
                    TranslateTransition MoveItIn = new TranslateTransition(
                            Duration.seconds(animationSpeed),
                            myLightClose);
                    MoveItIn.setToX(-.75 * subLightSize);
                    MoveItIn.play();

                    // TODO here one could add more code turning he light actually off
                    upDateButtonDatabase();
                    for (GButton b : lightSubButtons) {
                        // this works as well. It moves the subbuttons well and nicely under the main
                        // lightswitch

                        double dest = root.getHeight() / 2.4 - buttonSize / 2 + buttonSize;
                        TranslateTransition buttonMoveUp = new TranslateTransition(
                                Duration.seconds(animationSpeed),
                                b);
                        if (b.getSpawnedOutside()) {
                            buttonMoveUp.setToY(dest - Height);
                        } else {
                            buttonMoveUp.setToY(0);
                        }
                        buttonMoveUp.play();

                    }

                    lightSubModule = true;
                    buttonDistribute(myLightSwitch);

                    // Move all buttons when myLightSwitch is pressed
                }
            }
        });

        makeDraggable(myLightSwitch);

        // Add the button to the root pane
        root.getChildren().add(myLightSwitch);

        // *------------------------------------------------------------------- */

        // *-------------------------------------------------------------------- */

        // This is the close button for the Lightwindow:

        // The x animations later go to -subLightSize and 0 for in and out respectively

        myLightClose.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (lightSubModule) {
                    goBackToPosition();
                    TranslateTransition buttonGoBack = new TranslateTransition(
                            Duration.seconds(animationSpeed),
                            myLightClose);
                    buttonGoBack.setToX(0);
                    buttonGoBack.play();
                    lightSubModule = false;

                    for (GButton b : lightSubButtons) {
                        // this works as well. It moves the subbuttons well and nicely under the main
                        // lightswitch
                        double yInScreen = root.getHeight() / 2.4 - buttonSize / 2 + buttonSize;
                        double dest = Height;
                        TranslateTransition buttonMoveUp = new TranslateTransition(
                                Duration.seconds(animationSpeed),
                                b);
                        if (b.getSpawnedOutside())
                            buttonMoveUp.setToY(0);
                        else
                            buttonMoveUp.setToY(dest - yInScreen);
                        buttonMoveUp.play();

                    }

                }
            }
        });

        // *--------------------------------------------------------------------- */
        // music button

        myMusicPlayer = new GButton();

        createImageButton(myMusicPlayer, buttonSize, buttonSize, 300, 225, "play.png");

        myMusicPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!draggable) {
                    if (musicPlaying) {
                        toggleButtonImage(myMusicPlayer, "pause.png");
                        musicPlaying = false;
                    } else {
                        toggleButtonImage(myMusicPlayer, "play.png");
                        musicPlaying = true;
                        // here is marteens code for the
                        if (onPi) {
                            // !Activate if we have adjusted the path
                            // try {
                            // ProcessBuilder pb = new ProcessBuilder("python",
                            // "C:/Users/maart/IdeaProjects/untitled2/music_visualizer.py"); // ! replace
                            // file
                            // // path
                            // pb.inheritIO(); // Redirects the standard input, output, and error to the
                            // current Java
                            // // process
                            // pb.start();
                            // } catch (IOException ex) {
                            // ex.printStackTrace();
                            // }
                        }

                    }
                }
            }
        });

        // Make the button draggable
        makeDraggable(myMusicPlayer);

        // Add the button to the root pane
        root.getChildren().add(myMusicPlayer);

        // TODO add weather button and maybe an app for the weather
        // ---------------------------------------------------

        // the key adder
        GButton myKeyClose = new GButton();
        createImageSubButton(myKeyClose, subLightSize / 2, subLightSize / 2, Width, subLightSize / 4, "Close.png"); // !
                                                                                                                    // This
                                                                                                                    // is
        // still too
        // big

        myKeyClose.setOpacity(.5);
        root.getChildren().add(myKeyClose);

        myKey = new GButton();

        createImageButton(myKey, buttonSize, buttonSize, 500, 150, "key.png");

        myKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                if (!draggable && !keySubmenu) {
                    TranslateTransition MoveItIn = new TranslateTransition(
                            Duration.seconds(animationSpeed),
                            myKeyClose);
                    MoveItIn.setToX(-.75 * subLightSize);
                    MoveItIn.play();

                    // make a plus button and a text that explains how to calibrate a new key.
                    // after that is done, make a small text prompt that says "congrats, your keys
                    // weeigh btw"

                    keySubmenu = true;
                    buttonDistribute(myKey);
                    addTheKeyAddButton();
                    DBTest db = new DBTest();
                    String jsonString = db
                            .makeGETRequest("https://studev.groept.be/api/a23ib2b05/Get_total_nr_of_keys");
                    try {
                        // Parse JSON string to JSONArray
                        JSONArray array = new JSONArray(jsonString);

                        // Assuming the JSON array contains only one element
                        JSONObject curObject = array.getJSONObject(0);

                        // Get the count from the JSON object
                        numOfKeys = curObject.getInt("COUNT(key_id)");
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Handle JSON parsing or other exceptions
                    }

                    textFieldHudini(root, true);

                }
            }
        });

        makeDraggable(myKey);

        root.getChildren().add(myKey);

        myKeyClose.setOnAction(new EventHandler<ActionEvent>() {

            @Override
            public void handle(ActionEvent event) {
                if (keySubmenu) {
                    DBTest db = new DBTest();
                    db.makeGETRequest("https://studev.groept.be/api/a23ib2b05/update_key_addition_flag/" + 0);
                    goBackToPosition();
                    TranslateTransition buttonGoBack = new TranslateTransition(
                            Duration.seconds(animationSpeed),
                            myKeyClose);
                    buttonGoBack.setToX(0);
                    buttonGoBack.play();
                    keySubmenu = false;

                    TranslateTransition addButtonGoBack = new TranslateTransition(
                            Duration.seconds(animationSpeed),
                            addKey);
                    addButtonGoBack.setToY(0);
                    addButtonGoBack.play();
                    textFieldHudini(root, false);
                }
            }
        });

        // ---------------------------------

        // TODO add menu button
        myMenu = new GButton();
        createImageButton(myMenu, 100, 100, 0, 0, "menu.png");

        myMenu.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                // fadeIn(imageView);
                // myLightSwitch.setDisable(true);
                // myLightSwitch.setOpacity(1);
                // myMusicPlayer.setDisable(true);
                // myWheather.setDisable(true);
                // myLightSwitch.setOpacity(.2);
                // TODO add a menu if needed... Had difficulties getting the animation down
                buttonDistributeAll();
                initializeColorPicker();

            }
        });

        root.getChildren().add(myMenu);

        // ? Do we have more button ideas
        //
        // Show the stage
        primaryStage.show();
        System.out.println(buttons.size());
    }

    public void createImageButton(GButton button, int width, int height, int xPos, int yPos, String path) {
        // Load the image
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        // Create a button with the image
        button.setGraphic(imageView);

        // Remove default button styling
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Set the size of the button
        button.setPrefSize(width, height);

        // Set the position of the button
        button.setLayoutX(xPos);
        button.setLayoutY(yPos);
        buttons.add(button);
    }

    public void createImageSubButton(GButton button, int width, int height, int xPos, int yPos) {
        Image image = (button.getOn()) ? new Image(button.getOnPath()) : new Image(button.getOffPath());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        // Create a button with the image
        button.setGraphic(imageView);

        // Remove default button styling
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Set the size of the button
        button.setPrefSize(width, height);

        // Set the position of the button
        button.setLayoutX(xPos);
        button.setLayoutY(yPos);
    }

    public void createImageSubButton(GButton button, int width, int height, int xPos, int yPos, String path) {
        Image image = new Image(path);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(width);
        imageView.setFitHeight(height);

        // Create a button with the image
        button.setGraphic(imageView);

        // Remove default button styling
        button.setStyle("-fx-background-color: transparent; -fx-border-color: transparent;");

        // Set the size of the button
        button.setPrefSize(width, height);

        // Set the position of the button
        button.setLayoutX(xPos);
        button.setLayoutY(yPos);
    }

    public void toggleSubButtonImage(GButton button) {
        Image image = new Image((button.getOn()) ? button.getOffPath() : button.getOnPath());
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(subLightSize);
        imageView.setFitHeight(subLightSize);

        button.setGraphic(imageView);
    }

    public void toggleButtonImage(Button button, String newImage) {
        Image image = new Image(newImage);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(buttonSize);
        imageView.setFitHeight(buttonSize);

        button.setGraphic(imageView);
    }

    // ? do we like the way I made the dragable icons look or should we do smth else
    // to indicate dragability?
    public void makeDraggable(Button button) {
        button.setOnMousePressed(event -> {
            xOffset = event.getSceneX() - button.getLayoutX();
            yOffset = event.getSceneY() - button.getLayoutY();
        });

        button.setOnMouseDragged(event -> {

            double oldX = button.getLayoutX();
            double oldY = button.getLayoutY();
            double newX = event.getSceneX() - xOffset;
            double newY = event.getSceneY() - yOffset;
            if (Math.abs(newX - oldX) > 5 || Math.abs(newY - oldY) > 5) {
                draggable = true;
                button.setLayoutX(newX);
                button.setLayoutY(newY);
            }

        });

        button.setOnMouseReleased(event -> {
            draggable = false;
        });
    }

    // ? Couldn't figure out how to make the menu animation here
    public void fadeIn(ImageView imageView) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(animationSpeed), imageView);
        fadeTransition.setFromValue(0); // Start from fully transparent
        fadeTransition.setToValue(1); // End at fully opaque
        fadeTransition.play();
    }

    // Method to fade out the image
    public void fadeOut(ImageView imageView) {
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(animationSpeed), imageView);
        fadeTransition.setFromValue(1); // Start from fully opaque
        fadeTransition.setToValue(0); // End at fully transparent
        fadeTransition.play();
    }
    // ?----------------------------------------------------------

    public void moveComponennt(Region comp, int xDir, int yDir) {
        TranslateTransition transition = new TranslateTransition(Duration.seconds(animationSpeed), comp);
        double currentX = comp.getLayoutX();
        double currentY = comp.getLayoutY();
        double destinationX = currentX + xDir;
        double destinationY = currentY + yDir;
        transition.setToX(destinationX);
        transition.setToY(destinationY);
        transition.play();
    }

    public void buttonDistribute(GButton button) {
        double currentButtonX = button.getLayoutX();
        double currentButtonY = button.getLayoutY();
        // I wrote 2 new parameters for the GButton so it can go back where it came from
        // after button was pressed
        button.rememberPositionX();
        button.rememberPositionY();
        // Define the goal position of the screen. Is variable to the actual size of the
        // screen.
        double middleX = root.getWidth() / 2 - buttonSize / 2;
        double middleY = root.getHeight() / 2.4 - buttonSize / 2; // we want to show stuff below!
        // Move the button to the center of the screen
        TranslateTransition toCenter = new TranslateTransition(Duration.seconds(animationSpeed), button);
        toCenter.setToX(middleX - currentButtonX);
        toCenter.setToY(middleY - currentButtonY);
        toCenter.play();

        // Move other buttons out of the screen
        for (GButton b : buttons) {
            if (b != button && b != myMenu) { // Exclude the current button and the menu button
                b.rememberPositionX();
                b.rememberPositionY();
                double outTheScreenLeft = -buttonSize;
                double outTheScreenRight = Width;
                TranslateTransition buttonOut = new TranslateTransition(Duration.seconds(animationSpeed), b);
                double destinationX = currentButtonX > b.getLayoutX() ? outTheScreenLeft - b.getLayoutX()
                        : outTheScreenRight - b.getLayoutX();
                buttonOut.setToX(destinationX);
                buttonOut.play();
                if (b.getLayoutX() == Width || b.getLayoutX() == -buttonSize) {
                    b.setDisable(true);
                }

            }
        }
    }

    public void buttonDistributeAll() {
        for (GButton b : buttons) {
            // Exclude the current button and the menu button
            b.rememberPositionX();
            b.rememberPositionY();
            double outTheScreenLeft = -buttonSize;
            double outTheScreenRight = Width;
            TranslateTransition buttonOut = new TranslateTransition(Duration.seconds(animationSpeed), b);
            double destinationX = Width / 2 > b.getLayoutX() ? outTheScreenLeft - b.getLayoutX()
                    : outTheScreenRight - b.getLayoutX();
            buttonOut.setToX(destinationX);
            buttonOut.play();
            if (b.getLayoutX() == Width || b.getLayoutX() == -buttonSize) {
                b.setDisable(true);
            }

        }
    }

    public void goBackToPosition() {
        for (GButton b : buttons) {
            if (b != myMenu) {
                double goalX = b.getOldPositionX();
                TranslateTransition buttonBack = new TranslateTransition(Duration.seconds(animationSpeed), b);
                double destinationX = goalX - b.getLayoutX();
                buttonBack.setToX(destinationX);
                buttonBack.play();

                b.setDisable(false);
            }
        }
    }

    public void addSubLight(GButton subLight) {
        double speed = animationSpeed;

        double yHeight = Height / 2.4 - buttonSize / 2 + buttonSize;
        // The first light
        if (lightSubButtons.isEmpty()) {
            createImageSubButton(subLight, subLightSize, subLightSize, (int) (400 - 0.5 *
                    subLightSize),
                    (int) (Height));
            subLight.setSpawnedOutside(true);
            lightSubButtons.add(subLight);

            if (lightSubModule) {
                TranslateTransition cominInHot = new TranslateTransition(Duration.seconds(speed),
                        subLight);
                cominInHot.setToY(-yHeight + 1.05 * buttonSize / 2);// ? Here is a randomly guesed value
                cominInHot.play();
            }
        } else {
            if (lightSubButtons.size() < 7) {
                int spawnPoint = Width;
                for (int i = 0; i < lightSubButtons.size(); i++) {
                    TranslateTransition buttonAdd = new TranslateTransition(Duration.seconds(speed),
                            lightSubButtons.get(i));
                    if (i == 0)
                        buttonAdd.setToX(-lightSubButtons.size() * subLightSize / 2);

                    else {
                        buttonAdd.setToX(
                                -Width / 2 + i * subLightSize / 2
                                        - subLightSize / 2 * lightSubButtons.get(i).getMoveLeft());
                    }
                    lightSubButtons.get(i).moveLeft();
                    buttonAdd.play();

                }
                if (lightSubModule) {

                    createImageSubButton(subLight, subLightSize, subLightSize,
                            (int) (spawnPoint),
                            (int) (yHeight));
                    subLight.setSpawnedOutside(false);
                } else {
                    createImageSubButton(subLight, subLightSize, subLightSize,
                            (int) (spawnPoint),
                            (int) (Height));
                    subLight.setSpawnedOutside(true);
                }

                int goal = (int) (-Width / 2 + (lightSubButtons.size() - 1) * subLightSize / 2);

                lightSubButtons.add(subLight);
                TranslateTransition cominInHot = new TranslateTransition(Duration.seconds(speed),
                        subLight);

                cominInHot.setToX(goal);
                cominInHot.play();
                subLight.moveLeft();

            } else {
                System.out.println("You can only add a max of 6 Buttons rn");
            }
        }

        subLight.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (subLight.getOn()) {
                    subLight.turnOff();
                    toggleSubButtonImage(subLight);
                    if (checkAllSubsOn()) {
                        lightOn = true;
                        toggleButtonImage(myLightSwitch, "bulb_on.png");
                    } else {
                        lightOn = false;
                        toggleButtonImage(myLightSwitch, "bulb2.png");
                    }

                } else {
                    subLight.turnOn();
                    toggleSubButtonImage(subLight);
                    if (checkAllSubsOn()) {
                        lightOn = true;
                        toggleButtonImage(myLightSwitch, "bulb_on.png");
                    } else {
                        lightOn = false;
                        toggleButtonImage(myLightSwitch, "bulb2.png");
                    }

                }
            }

        });

        root.getChildren().add(subLight);
    }

    // *------------------------------------------------------------------------------------

    // * Database peripherals */

    public void upDateButtonDatabase() {
        DBTest db = new DBTest();
        String response = db.makeGETRequest("https://studev.groept.be/api/a23ib2b05/Get_all_lights");
        ArrayList<String> values = new ArrayList<>();
        values = db.parseJSON(response);
        for (int i = 0; i < values.size() - 4; i++) {
            int LightID = Integer.parseInt(values.get(i));
            boolean LightOn = toBooleanConverter(Integer.parseInt(values.get(i + 1)));
            String LightName = values.get(i + 2);
            String LightFunction = values.get(i + 3);
            Lights SwitchType = EnumConverter(values.get(i + 4));
            i += 4;
            if (LightFunction == null) {
                LightFunction = "";
            }
            GButton newButton = new GButton(LightID, LightOn, LightName, LightFunction, SwitchType);
            boolean IDfound = false;
            for (GButton b : lightSubButtons) {
                if (b.getLightID() == LightID) {
                    IDfound = true;
                    b.turnValueTo(LightOn);
                    toggleSubButtonImage(b);
                }
            }
            if (!IDfound && lightSubButtons.size() < 7) {
                addSubLight(newButton);
                toggleSubButtonImage(newButton);
            }
            System.out.println(LightID);
        }
        if (checkAllSubsOn()) {
            lightOn = true;
            toggleButtonImage(myLightSwitch, "bulb_on.png");
        } else {
            lightOn = false;
            toggleButtonImage(myLightSwitch, "bulb2.png");
        }

    }

    public boolean toBooleanConverter(int num) {
        if (num == 1) {
            return true;
        } else
            return false;
    }

    public int toTinIntConverter(boolean bool) {
        if (bool) {
            return 1;
        } else {
            return 0;
        }

    }

    public Lights EnumConverter(String s) {
        if (s.equals("SWITCH")) {
            return Lights.SWITCH;
        } else if (s.equals("BULB")) {
            return Lights.BULB;
        } else if (s.equals("LEDSTRIP")) {
            return Lights.LEDSTRIP;
        } else if (s.equals("PLUG")) {
            return Lights.PLUG;
        } else
            return null;

    }

    public void toggleValue(int LightID) {

    }

    // *--------------------------------------------------------------------------------------------
    // */
    public boolean checkAllSubsOn() {
        boolean allOn = true;
        for (GButton b : lightSubButtons) {
            if (b.getOn() != true) {
                allOn = false;
            }
        }
        return allOn;
    }

    public boolean checkOneSubOff() {
        boolean allOff = true;
        for (GButton b : lightSubButtons) {
            if (b.getOn() == true) {
                allOff = false;
            }
        }
        return allOff;
    }

    // ---------------------------------------------------------

    // Some code for the weightscale module

    public void addTheKeyAddButton() {
        double yHeight = Height / 2.4 - buttonSize / 2 + buttonSize;
        addKey = new GButton();
        createImageSubButton(addKey, subLightSize, subLightSize, (int) (400 - 0.5 *
                subLightSize),
                (int) (Height), "plus.png");
        addKey.setSpawnedOutside(true);
        root.getChildren().add(addKey);
        TranslateTransition cominInHot = new TranslateTransition(Duration.seconds(animationSpeed),
                addKey);
        cominInHot.setToY(-yHeight + 1.05 * buttonSize / 2);// ? Here is a randomly guesed value
        cominInHot.play();

        addKey.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                GButton confirm = new GButton();
                createImageSubButton(confirm, subLightSize, subLightSize, (int) (400 - 0.5 *
                        subLightSize),
                        (int) (yHeight), "confirmation.png");
                confirm.setOpacity(0);
                root.getChildren().add(confirm);
                DBTest db = new DBTest();
                db.makeGETRequest("https://studev.groept.be/api/a23ib2b05/update_key_addition_flag/" + 1);
                try {

                    File file = new File(
                            "src/confirmation.wav");
                    String filePath = file.toURI().toString();
                    AudioClip sound = new AudioClip(filePath);
                    sound.play();

                } catch (Exception ex) {
                    ex.printStackTrace();
                }
                // TODO here add that a buttons needs to be added to the database
                // Fade in animation
                FadeTransition fadeIn = new FadeTransition(Duration.seconds(.4), confirm);
                fadeIn.setFromValue(0);
                fadeIn.setToValue(.95);

                // Fade hold animation (staying visible for a shorter while)
                PauseTransition hold = new PauseTransition(Duration.seconds(0.1)); // Adjust the duration as needed
                hold.setOnFinished(e -> {
                    // Fade out animation
                    FadeTransition fadeOut = new FadeTransition(Duration.seconds(.4), confirm);
                    fadeOut.setFromValue(.95);
                    fadeOut.setToValue(0);
                    fadeOut.setOnFinished(ev -> {
                        // Remove the confirm button from the root after the fade out animation finishes
                        root.getChildren().remove(confirm);
                    });
                    fadeOut.play();
                });

                // Play animations sequentially
                SequentialTransition sequence = new SequentialTransition(fadeIn, hold);
                sequence.play();
                numOfKeys++;
                textField.setText("  Number of keys: " + numOfKeys);
            }
        });
    }

    public void textFieldHudini(Pane parentPane, boolean on) {
        // Set initial text
        textField.setText("  Number of keys: " + numOfKeys);

        // Apply CSS to center the text horizontally
        textField.setFont(Font.font("Montserrat-Medium", 20));
        // textField.setTextAlignment(TextAlignment.CENTER);
        textField.setLayoutX(325);
        textField.setLayoutY(420);
        // Set initial opacity based on the 'on' parameter
        textField.setOpacity(on ? 0.0 : .3);

        // Create a fade transition
        FadeTransition fadeTransition = new FadeTransition(Duration.seconds(1), textField);

        // Set the opacity values for the fade in and fade out animations
        if (on) {
            fadeTransition.setFromValue(0.0);
            fadeTransition.setToValue(.3);
        } else {
            fadeTransition.setFromValue(.3);
            fadeTransition.setToValue(0);
        }

        // Play the fade transition
        fadeTransition.play();

        // Add the text field to the parent pane
        parentPane.getChildren().add(textField);
    }
    // * Colorpicker following --------------------------------------- */

    public void initializeColorPicker() {

        // Create a color picker circle
        Circle colorPicker = createColorPicker();

        // Create a circle to display the selected color
        colorPreviewWindow = new Circle(20, Color.WHITE);
        colorPreviewWindow.setStroke(Color.BLACK);
        colorPreviewWindow.setStrokeWidth(2);
        colorPreviewWindow.setLayoutY(Height / 2 + colorPicker.getRadius() + 35);
        colorPreviewWindow.setLayoutX(Width / 2);

        // Add mouse click event listener
        colorPicker.setOnMouseClicked(event -> {
            // Get RGB value of the color at the clicked position
            Color pickedColor = getColorAtPosition(colorPicker, event.getX(), event.getY());
            updateColorDisplay(pickedColor);
        });
        root.getChildren().addAll(colorPreviewWindow);
    }

    public Color getColorAtPosition(Circle colorPicker, double x, double y) {
        // Calculate angle and distance from the center
        double angle = Math.toDegrees(Math.atan2(y - colorPicker.getCenterY(), x - colorPicker.getCenterX()));
        angle += 90; // Shift the hue so that red appears at the top
        if (angle < 0) {
            angle += 360;
        }
        double distance = Math
                .sqrt(Math.pow(x - colorPicker.getCenterX(), 2) + Math.pow(y - colorPicker.getCenterY(), 2));

        // Calculate saturation based on distance from the center
        double maxDistance = colorPicker.getRadius();
        double saturation = (distance / maxDistance);
        saturation = Math.max(0, saturation);

        // Convert angle and calculated saturation to hue and saturation, then create
        // color
        return Color.hsb(angle, saturation, 1.0);
    }

    // Method to update the color display circle
    private void updateColorDisplay(Color color) {
        colorPreviewWindow.setFill(color);
    }

    // Method to create a circular color picker
    public Circle createColorPicker() {
        int radius = 175;
        int centerOffsetY = 35;
        Circle colorPicker = new Circle(200, Color.WHITE);
        colorPicker.setRadius(radius);
        colorPicker.setStroke(Color.BLACK);
        colorPicker.setStrokeWidth(0);
        colorPicker.setLayoutX(Width / 2);
        colorPicker.setLayoutY(Height / 2 - centerOffsetY);
        Image image = new Image("Circle.png");
        ImageView imageView = new ImageView(image);
        imageView.setFitWidth(radius * 2);
        imageView.setFitHeight(radius * 2);
        imageView.setLayoutX(Width / 2 - radius);
        imageView.setLayoutY(Height / 2 - radius - centerOffsetY);
        // Make the image click-through
        imageView.setMouseTransparent(true);
        // Add the Circle and ImageView to the StackPane
        root.getChildren().addAll(colorPicker, imageView);
        // TODO add a backgdrop Shadow
        return colorPicker;
    }

    public static void main(String[] args) {
        launch(args);
    }
}
