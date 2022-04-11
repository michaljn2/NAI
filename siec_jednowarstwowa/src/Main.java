import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    private static final Path trainingsPath = Paths.get("C:\\Users\\micha\\OneDrive\\Pulpit\\NAI\\siec_jednowarstwowa\\trainings");
    private static final Path testsPath = Paths.get("C:\\Users\\micha\\OneDrive\\Pulpit\\NAI\\siec_jednowarstwowa\\tests");

    public static void main(String[] args) {
        File file = new File(trainingsPath.toString());
        for(String dir : Objects.requireNonNull(file.list())){
            // posotive output danego perceptronu to nazwa kraju
            Perceptron.addPerceptronToList(new Perceptron(dir));
        }

        // petle treningowa wykonuje 5 razy
        for (int i = 0; i < 5; i++) {
            // petla dla kazdego folderu w folderze languages
            for (String language : Objects.requireNonNull(file.list())) {
                File langDir = new File(trainingsPath + "\\" + language);
                // petla dla kazdego pliku w podfolderze danego jezyka
                for (String trainFile : Objects.requireNonNull(langDir.list())) {
                    // trenujemy kazdy perceptron na kazdym pliku
                    for (Perceptron p : Perceptron.getPerceptrons()) {
                        p.trainPerceptron(new File(langDir + "\\" + trainFile), language);
                    }
                }
            }
        }

        int testCounter = 0;
        int rightDecisions = 0;
        // testujemy
        // pliki testowe znajduja sie w katalogu tests i poszczegolne nazwy podfolederow maja takie same nazwy
        // jak w folderze z danymi treningowymi
        File tests = new File(testsPath.toString());
        for (String language : Objects.requireNonNull(tests.list())){
            File langDir = new File(testsPath + "\\" + language);
            for (String testFile : Objects.requireNonNull(langDir.list())){
                File test = new File(langDir + "\\" + testFile);
                Perceptron biggestOutputPerceptron = Perceptron.getBiggestOutputPerceptronFromContent(Perceptron.getContentFromFile(test));
                // sprawdzam, czy positiveOutput perceptronu zgadza się z nazwa jezyka
                if (biggestOutputPerceptron.getPositiveOutput().equals(language))
                    rightDecisions++;
                testCounter++;
            }
        }
        System.out.println(rightDecisions + " : " + testCounter);

        GUI gui = new GUI();
        gui.showGUI(Perceptron.getPerceptrons());
    }

}
