import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import javafx.animation.FadeTransition;

import javafx.animation.TranslateTransition;

import javafx.util.Duration;
import javafx.scene.layout.Region;

import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;

import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;

import javafx.scene.layout.Pane;

import javafx.stage.Stage;
import javafx.stage.StageStyle;

public class test extends Application {
    private final int Height = 480;
    private final int Width = 800;
    private boolean lightSubModule;
    private boolean lightOn;
    private boolean musicPlaying;
    private double animationSpeed;
    private boolean onPi = false;

    private Pane root;

    // Only can use my type of Top G button here
    private ArrayList<GButton> buttons;
    private ArrayList<GButton> lightSubButtons;
    private GButton myLightSwitch;
    private GButton myMusicPlayer;
    private GButton myWeather;

    private GButton myLightClose;
    private HashMap<Lights, String[]> LightDic;
    private GButton myMenu;
    // private int MenuAnimationDuration;

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
        LightDic = new HashMap<>();
        animationSpeed = .5;
        // SubLights all get a know weather they are on or off and they can tell you
        // their current image path
        LightDic.put(Lights.SWITCH, new String[] { "SwitchOn.png", "SwitchOff.png" });
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

                                b.setOn(false);
                            }
                            toggleButtonImage(myLightSwitch, "bulb2.png");
                        } else {
                            lightOn = true;
                            for (GButton b : lightSubButtons) {
                                b.setOn(true);

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

        // TODO make an Animation in the Lightbutton module to move in the button

        // Add the button to the root pane

        // *--------------------------------------------------------------------- */
        // TODO add music button

        myMusicPlayer = new GButton();

        createImageButton(myMusicPlayer, buttonSize, buttonSize, 300, 225, "play.png");

        myMusicPlayer.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!draggable) {
                    if (musicPlaying) {
                        toggleButtonImage(myMusicPlayer, "pause.png");
                        musicPlaying = false;
                        // TODO here one could add more code turning he light actually off
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
                        // TODO here one could add more code turning light actually on

                    }
                }
            }
        });

        // Make the button draggable
        makeDraggable(myMusicPlayer);

        // Add the button to the root pane
        root.getChildren().add(myMusicPlayer);

        // TODO add weather button and maybe an app for the weather
        myWeather = new GButton();

        createImageButton(myWeather, buttonSize, buttonSize, 500, 150, "weather.png");
        makeDraggable(myWeather);

        root.getChildren().add(myWeather);

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
                if (lightSubButtons.size() < 7) {
                    GButton subLight3 = new GButton(LightDic.get(Lights.SWITCH)[0], LightDic.get(Lights.SWITCH)[1],
                            false);
                    // ! When we later have the database for lights, one has to create lights over
                    // ! here
                    addSubLight(subLight3);
                }
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
        // TODO here you can add code that gets the lights from the database
        // You can choose from a List of options what your light is supposed to be
        // TODO for loop through all the lights in the database
        // TODO In the following you could add this to the for loop and just replace
        // Switch by the type of Light you are using.

        double yHeight = Height / 2.4 - buttonSize / 2 + buttonSize;
        ;
        if (lightSubButtons.isEmpty()) {
            createImageSubButton(subLight, subLightSize, subLightSize, (int) (400 - 0.5 *
                    subLightSize),
                    (int) (Height));
            subLight.setSpawnedOutside(true);
            lightSubButtons.add(subLight);
            TranslateTransition cominInHot = new TranslateTransition(Duration.seconds(animationSpeed),
                    subLight);
            if (lightSubModule) {
                cominInHot.setToY(-yHeight + 1.05 * buttonSize / 2);// ? Here is a randomly guesed value

                cominInHot.play();
            }

        } else {
            if (lightSubButtons.size() < 7) {
                int spawnPoint = Width;
                for (int i = 0; i < lightSubButtons.size(); i++) {
                    TranslateTransition buttonAdd = new TranslateTransition(Duration.seconds(animationSpeed),
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
                TranslateTransition cominInHot = new TranslateTransition(Duration.seconds(animationSpeed),
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
                    toggleSubButtonImage(subLight);
                    subLight.setOn(false);

                } else {
                    toggleSubButtonImage(subLight);
                    subLight.setOn(true);
                }
            }

            // ! Add functionality here with if statements hehe. Make external methods that
            // ! are called here depending on what happens

        });

        root.getChildren().add(subLight);
    }

    // ! Add a delete a button function that should litterally the oposite of the
    // ! add function

    public static void main(String[] args) {
        launch(args);
    }
}
