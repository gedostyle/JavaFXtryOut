
import javax.swing.*;

public class HelloWorld {
    public static void main(String[] args) {
        // Create a JFrame
        JFrame frame = new JFrame("Hello, World!");

        // Create a JLabel with the text "Hello, World!"
        JLabel label = new JLabel("Hello, World!");

        // Add the label to the frame
        frame.getContentPane().add(label);

        // Set frame properties
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(300, 200); // Set the size of the frame
        frame.setLocationRelativeTo(null); // Center the frame on the screen
        frame.setVisible(true); // Make the frame visible
    }
}
