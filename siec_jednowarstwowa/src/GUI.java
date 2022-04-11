import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.List;

public class GUI extends JFrame{
    public void showGUI(List<Perceptron> perceptronList){
        this.setTitle("Siec jednowarstwowa");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        String countries = "";
        for (Perceptron p : perceptronList){
            countries += p.getPositiveOutput() + ", ";
        }
        JLabel label = new JLabel("Paste the text in a language from one of countries: " + countries);
        JTextField textField = new JFormattedTextField();
        JButton button = new JButton("Get result");
        JLabel resultLabel = new JLabel("The result will be shown here");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Perceptron resultPerceptron = Perceptron.getBiggestOutputPerceptronFromContent(textField.getText());
                resultLabel.setText(resultPerceptron.getPositiveOutput());
            }
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
