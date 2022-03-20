import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class GUI extends JFrame {
    public void showGUI(int attributesCount){
        this.setTitle("k-NN algorithm");
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        JPanel panel = new JPanel(new BorderLayout());
        JLabel label = new JLabel("Podaj, oddzielając przecinkami kolejne "+ attributesCount + " parametry Irysa");
        JTextField textField = new JFormattedTextField();
        JButton button = new JButton("Get result");
        JLabel resultLabel = new JLabel("Tu pojawi się wynik");
        button.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                String[] splittedLine = textField.getText().split(",");
                if (splittedLine.length != attributesCount){
                    resultLabel.setText("Podano nieprawidłową ilość atrybutów");
                }
                double[] attributes = new double[splittedLine.length];
                try{
                    for (int i=0; i<splittedLine.length;i++){
                        attributes[i] = Double.parseDouble(splittedLine[i]);
                    }
                    resultLabel.setText("Ten irys jest gatunku: " + Main.getResult(attributes,Main.results, Main.irises,Main.k));
                } catch (NumberFormatException nfe){
                    resultLabel.setText("Podano dane nienumeryczne");
                    nfe.printStackTrace();
                }
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
