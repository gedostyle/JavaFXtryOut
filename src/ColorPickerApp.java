import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.StackPane;
import javafx.scene.paint.Color;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;

public class ColorPickerApp extends Application {

    private Circle colorDisplayCircle;

    @Override
    public void start(Stage primaryStage) {
        // Create a color picker circle
        Circle colorPicker = createColorPicker();

        // Create a circle to display the selected color
        colorDisplayCircle = new Circle(20, Color.WHITE);
        colorDisplayCircle.setStroke(Color.BLACK);
        colorDisplayCircle.setStrokeWidth(2);

        // Add mouse click event listener
        colorPicker.setOnMouseClicked(event -> {
            // Get RGB value of the color at the clicked position
            Color pickedColor = getColorAtPosition(colorPicker, event.getX(), event.getY());
            updateColorDisplay(pickedColor);
        });

        // Setup scene
        StackPane root = new StackPane();
        root.getChildren().addAll(colorPicker, colorDisplayCircle);
        Scene scene = new Scene(root, 400, 450);

        primaryStage.setTitle("Color Picker");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    // Method to create a circular color picker
    private Circle createColorPicker() {
        Circle colorPicker = new Circle(200, Color.WHITE);
        colorPicker.setRadius(150);
        colorPicker.setStroke(Color.BLACK);
        colorPicker.setStrokeWidth(2);

        return colorPicker;
    }

    // Method to get the color at the clicked position
    // Method to get the color at the clicked position
    private Color getColorAtPosition(Circle colorPicker, double x, double y) {
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
        colorDisplayCircle.setFill(color);
    }

    public static void main(String[] args) {
        launch(args);
    }
}
