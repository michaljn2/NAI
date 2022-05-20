import javax.swing.*;
import java.awt.*;
import java.util.Arrays;

public class GUI extends JFrame {
    public void showGUI() {
        this.setTitle("Siec jednowarstwowa");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());

        JLabel label = new JLabel("Type attributes separated by comma");
        JTextField textField = new JFormattedTextField();
        JButton button = new JButton("Get result");
        JLabel resultLabel = new JLabel("The result will be shown here");

        button.addActionListener(e -> {
            TestObj test = new TestObj(Arrays.asList(textField.getText().split(",")),"");
            resultLabel.setText(Main.getDecision(test,Main.czyWygladzanie));
        });
        panel.add(label, BorderLayout.PAGE_START);
        panel.add(textField, BorderLayout.CENTER);
        panel.add(button, BorderLayout.LINE_END);
        panel.add(resultLabel, BorderLayout.PAGE_END);
        this.add(panel);
        this.pack();
        this.setVisible(true);
    }
}
