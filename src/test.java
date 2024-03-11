import javafx.application.Application;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

public class test extends Application {
    private boolean lightOn;
    private Pane root;
    private Button myLightSwitch;
    private int buttonSize;

    private double xOffset = 0;
    private double yOffset = 0;
    private boolean draggable = false;

    @Override
    public void start(Stage primaryStage) {
        buttonSize = 165;
        lightOn = false;

        // Create a root node for the scene
        root = new Pane();

        // Set the background color of the Pane
        root.setStyle("-fx-background-color: rgb(237, 237, 241);");

        // Set the size of the root node
        root.setPrefSize(800, 480); // Set the size to match your touchscreen resolution

        // Create the scene with the root node
        Scene scene = new Scene(root);

        // Set the scene on the stage
        primaryStage.setScene(scene);

        myLightSwitch = new Button();
        createImageButton(myLightSwitch, buttonSize, buttonSize, 50, 50, "bulb2.png");
        myLightSwitch.setOnAction(new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {

                if (!draggable) {
                    if (lightOn) {
                        toggleButtonImage(myLightSwitch, "bulb2.png");
                        lightOn = false;

                    } else {
                        toggleButtonImage(myLightSwitch, "bulb_on.png");
                        lightOn = true;
                    }
                }
            }
        });

        // Make the button draggable
        makeDraggable(myLightSwitch);

        // Add the button to the root pane
        root.getChildren().add(myLightSwitch);

        // Show the stage
        primaryStage.show();
    }

    public void createImageButton(Button button, int width, int height, int xPos, int yPos, String path) {
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
    }

    public void toggleButtonImage(Button button, String newImage) {
        Image image = new Image(newImage);
        ImageView imageView = new ImageView(image);

        imageView.setFitWidth(buttonSize);
        imageView.setFitHeight(buttonSize);

        button.setGraphic(imageView);
    }

    public void makeDraggable(Button button) {
        // Event handler for mouse pressed
        button.setOnMousePressed(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                xOffset = event.getSceneX() - button.getLayoutX();
                yOffset = event.getSceneY() - button.getLayoutY();
                draggable = false;
                new Thread(() -> {
                    try {
                        Thread.sleep(2000);
                        draggable = true;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }).start();
            }
        });

        // Event handler for mouse dragged
        button.setOnMouseDragged(new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if (draggable) {
                    double newX = event.getSceneX() - xOffset;
                    double newY = event.getSceneY() - yOffset;

                    // Set the new button position
                    button.setLayoutX(newX);
                    button.setLayoutY(newY);
                }
            }
        });
    }

    public static void main(String[] args) {
        launch(args);
    }
}
