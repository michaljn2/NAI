import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Perceptron {
    private double teta = Math.random();
    private double learnParam = Math.random();
    private double [] scaleVector;
    private String positiveOutput;
    private static List<Perceptron> perceptrons = new ArrayList<>();
    // wektor wag jest losowy
    public Perceptron(String positiveOutput){
        this.positiveOutput = positiveOutput;
        this.scaleVector = new double[26];
        for (int i = 0; i<scaleVector.length; i++) {
            this.scaleVector[i] = (Math.random() * 2) - 1;
        }
    }

    private int getDiscreteOutput(int [] attributes){
        double scalarProduct = 0;
        for (int i = 0; i < attributes.length; i++){
            scalarProduct+= attributes[i] * this.scaleVector[i];
        }
        if (scalarProduct >= this.teta)
            return 1;
        else
            return 0;
    }

    private double getContinousOutput(int[] attributes){
        double scalarProduct = 0;
        for (int i = 0; i < attributes.length; i++){
            scalarProduct+= attributes[i] * this.scaleVector[i];
        }
        return scalarProduct;
    }

    public void trainPerceptron (File file, String rightDecision){
        // czytam plik
        int [] attributes = getAttributesFromContent(getContentFromFile(file));

        // sprawdzam jaki jest oczekiwany output
        int rightOutput;
        if (rightDecision.equals(this.positiveOutput))
            rightOutput = 1;
        else
            rightOutput = 0;

        // liczę output
        int actualOutput = getDiscreteOutput(attributes);
        // jesli actualOutput i rightOutput sie roznia, modyfikuje wektor wag i tete
        if (!(rightOutput == actualOutput)){
            this.modifyScaleVector(rightOutput, actualOutput, attributes);
            this.modifyTeta(rightOutput, actualOutput);
        }
    }

    private void modifyTeta(int rightDecision, int output){
        this.teta = this.teta + ((output - rightDecision) * this.learnParam);
    }

    private void modifyScaleVector (int rightDecision, int output, int[] attributes){
        for (int i = 0; i < this.scaleVector.length; i++){
            this.scaleVector[i] = this.scaleVector[i] + ((rightDecision - output) * learnParam * attributes[i]);
        }
    }

    public String getPositiveOutput(){
        return this.positiveOutput;
    }

    public static List<Perceptron> getPerceptrons() {
        return perceptrons;
    }

    public static void addPerceptronToList(Perceptron perceptron){
        perceptrons.add(perceptron);
    }

    public static String getContentFromFile(File file){
        String content = "";
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(file)));
            String line;
            while ((line = br.readLine()) != null){
                line = line.toLowerCase();
                content = content + line;
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return content;
    }

    public static int[] getAttributesFromContent(String content){
        // tworze mape, ktora bedzie przechowywac znaki i ich ilosc w tekscie
        Map<Character, Integer> letters = new LinkedHashMap<>();
        // inicjalizuje mape wartosciami 0 dla kazdej litery, w razie gdyby ktoras z nich nie wystapila w tekscie
        for (int i = 97; i<=122; i++){
            letters.put((char)i, 0);
        }
        for (Character c : content.toCharArray()){
            if (!((int)c >= 97 && (int)c <= 122))
                continue;
            letters.put(c, letters.get(c) + 1);
        }
        // lista ilosci znakow posortowanych alfabetycznie
        List<Integer> tmpList = new ArrayList<>(letters.values());
        int [] attributes = new int[26];
        for (int i = 0; i<attributes.length; i++)
            attributes[i] = tmpList.get(i);
        return attributes;
    }

    public static Perceptron getBiggestOutputPerceptronFromContent(String content){
        // tworze mape, ktora bedzie przechowywac perceptrony i ich ciagle wyjscia dla poszczegolnego pliku
        Map<Perceptron, Double> perceptronsOutputsMap = new LinkedHashMap<>();
        for (Perceptron p : perceptrons){
            perceptronsOutputsMap.put(p, p.getContinousOutput(Perceptron.getAttributesFromContent(content)));
        }
        // sortuje mape, tak aby pierwszy byl perceptron z najwiekszym outputem
        perceptronsOutputsMap = perceptronsOutputsMap.entrySet().stream()
                .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                        (oldVal, newVal) -> oldVal, LinkedHashMap::new));
        // wybieram z mapy pierwszy rekord (czyli ten, który ma najwieksza wartosc)
        Map.Entry<Perceptron, Double> biggestOutput = perceptronsOutputsMap.entrySet().iterator().next();
        return biggestOutput.getKey();
    }

}
