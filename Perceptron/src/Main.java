import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Main {
    public static String firstSpecies;
    public static String secondSpecies;
    public static List<TrainIris> trainIrises;
    public static List<String> allSpecies;
    public static int attributesCount;
    public static int trainLoops;
    public static void main(String[] args) {
        // rozpoczynam czytanie z pliku treningowego
        trainIrises = new ArrayList<>();
        allSpecies = new ArrayList<>();
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("iristrain.csv")));
            String line;
            boolean firstLoop = true;
            while((line = br.readLine()) != null){
                if (firstLoop){
                    attributesCount = line.split(",").length - 2;
                    firstLoop = false;
                    continue;
                }
                String [] splittedLine = line.split(",");
                double [] attributes = new double[splittedLine.length - 2];
                for (int i = 1; i <= splittedLine.length-2; i++){
                    attributes[i - 1] = Double.parseDouble(splittedLine[i]);
                }
                trainIrises.add(new TrainIris(attributes, splittedLine[splittedLine.length - 1]));
                allSpecies.add(splittedLine[splittedLine.length-1]);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        allSpecies = allSpecies.stream().distinct().collect(Collectors.toList());
        // uzytkownik wybiera gatunki, które chce badac
        Scanner scanner = new Scanner(System.in);
        String species = "";
        for (int i = 0; i<allSpecies.size(); i++)
            species =species + (i+1) + " - " + allSpecies.get(i) + ", ";
        System.out.println("Wybierz pierwszy badany gatunek podajac numer sposrod: " + species + ". Ten gatunek bedzie \"pozytywny\"");
        firstSpecies = allSpecies.get(scanner.nextInt()-1);
        System.out.println("Wybierz drugi badany gatunek podajac numer sposrod: " + species + ". Ten gatunek bedzie \"negatywny\"");
        secondSpecies = allSpecies.get(scanner.nextInt()-1);
        System.out.println(firstSpecies + " " + secondSpecies);
        // uzytkownik podaje wartosc progu teta i parametru uczacego
        System.out.println("Podaj wartosc progu Teta: ");
        double teta = scanner.nextDouble();
        System.out.println("Podaj wartosc parametru uczacego: ");
        double learnParam = scanner.nextDouble();
        System.out.println("Podaj ilosc petli treningowych");
        //uzytkownik podaje ilosc petli treningowych
        trainLoops = scanner.nextInt();
        // wartosc poczatkowa wektora wag jest losowa
        double [] scaleVector = new double[trainIrises.get(0).attributes.length];
        for (int i = 0; i<scaleVector.length; i++){
            scaleVector[i] = getRandomNumber(0,5);
        }
        // uczenie perceptronu
        Perceptron perceptron = new Perceptron(teta, learnParam, scaleVector, firstSpecies, secondSpecies);
        // powtarzamy uczenie tyle razy ile podal uzytkownik
        int counter = 0;
        while(counter < trainLoops) {
            for (TrainIris trainIris : trainIrises) {
                if (!trainIris.species.equals(firstSpecies) && !trainIris.species.equals(secondSpecies))
                    continue;
                int rightDecision;
                if (trainIris.species.equals(firstSpecies))
                    rightDecision = 1;
                else
                    rightDecision = 0;

                int output = perceptron.getOutput(trainIris.attributes);
                if (output != rightDecision) {
                    perceptron.modifyTeta(rightDecision, output);
                    perceptron.modifyScaleVector(rightDecision, output, trainIris.attributes);
                }
            }
            counter++;
       }

        // teraz zaczynamy testowanie
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("iristest.csv")));
            String line;
            int testCounter = 0;
            int rightDecisions = 0;
            boolean firstLoop = true;
            while((line = br.readLine()) != null){
                if (firstLoop){
                    firstLoop = false;
                    continue;
                }
                String [] splittedLine = line.split(",");
                species = splittedLine[splittedLine.length - 1];
                if (!species.equals(firstSpecies) && !species.equals(secondSpecies))
                    continue;
                double [] attributes = new double[splittedLine.length - 2];
                for (int i = 1; i <= splittedLine.length - 2; i++){
                    attributes[i - 1] = Double.parseDouble(splittedLine[i]);
                }
                int rightDecision;
                if (species.equals(firstSpecies))
                    rightDecision = 1;
                else
                    rightDecision = 0;

                if (perceptron.getOutput(attributes) == rightDecision)
                    rightDecisions++;
                testCounter++;
            }
            System.out.println(rightDecisions + " : " + testCounter);
        } catch (IOException e) {
            e.printStackTrace();
        }
        GUI gui = new GUI();
        gui.showGUI(perceptron, attributesCount, firstSpecies, secondSpecies);

    }
    public static double getRandomNumber(double min, double max) {
        return  ((Math.random() * (max - min)) + min);
    }
}
class TrainIris{
    double [] attributes;
    String species;

    public TrainIris(double[] attributes, String species) {
        this.attributes = attributes;
        this.species = species;
    }
}
class Perceptron{
    double teta;
    double learnParam;
    double[] scaleVector;
    String positiveOutput;
    String negativeOutput;

    public Perceptron(double teta, double learnParam, double[] scaleVector, String positiveOutput, String negativeOutput) {
        this.teta = teta;
        this.learnParam = learnParam;
        this.scaleVector = scaleVector;
        this.positiveOutput = positiveOutput;
        this.negativeOutput = negativeOutput;
    }

    public int getOutput(double [] attributes){
        double scalarProduct = 0;
        for (int i = 0; i < attributes.length; i++){
            scalarProduct+= attributes[i] * scaleVector[i];
        }
        if (scalarProduct >= this.teta)
            return 1;
        else
            return 0;
    }

    public void modifyTeta(int rightDecision, int output){
        this.teta = this.teta + ((output - rightDecision) * this.learnParam);
    }

    public void modifyScaleVector (int rightDecision, int output, double[] attributes){
        for (int i = 0; i < this.scaleVector.length; i++){
            this.scaleVector[i] = this.scaleVector[i] + ((rightDecision - output) * learnParam * attributes[i]);
        }
    }
}
