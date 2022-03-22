import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;
import java.util.Vector;
import java.util.stream.Collectors;

public class Main {
    public static String firstSpecies;
    public static String secondSpecies;
    public static List<TrainIris> trainIrises;
    public static List<String> allSpecies;
    public static int attributesCount;
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
                allSpecies.add(splittedLine[splittedLine.length - 1]);
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
        // wartosc poczatkowa wektora wag jest losowa
        double [] scaleVector = new double[trainIrises.get(0).attributes.length];
        for (int i = 0; i<scaleVector.length; i++){
            scaleVector[i] = getRandomNumber(-5,5);
        }


    }
    public static int getRandomNumber(int min, int max) {
        return (int) ((Math.random() * (max - min)) + min);
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

    public Perceptron(double teta, double learnParam, double[] scaleVector) {
        this.teta = teta;
        this.learnParam = learnParam;
        this.scaleVector = scaleVector;
    }

    public void modifyTeta(){

    }
}
