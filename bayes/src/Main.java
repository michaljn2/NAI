import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    private static final Path trainFile = Paths.get("C:\\Users\\micha\\OneDrive\\Pulpit\\NAI\\bayes\\train");
    private static final Path testFile = Paths.get("C:\\Users\\micha\\OneDrive\\Pulpit\\NAI\\bayes\\test");
    static List<TrainObj> trainObjs = new ArrayList<>();
    static List<TestObj> testObjs = new ArrayList<>();
    static List<String> decisions = new ArrayList<>();
    static boolean czyWygladzanie;
    // mapa przechowujaca dla kazdego atrybutu (tutaj jako index) mozliwe wartosci
    static Map<Integer, List<String>> attributesCount = new LinkedHashMap<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Czy chcesz aby wygladzanie bylo stosowane dla wszystkich atrybutów? Wpisz 'tak' lub 'nie'");
        String input = scanner.next();
        czyWygladzanie = input.equals("tak");


        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(trainFile.toFile())));
            String line;
            boolean firstLoop = true;
            while ((line = br.readLine()) != null){
                String[] splittedLine = line.split(",");
                List<String> attrs = new ArrayList<>();
                for (int i = 0; i<splittedLine.length - 1; i++) {
                    attrs.add(splittedLine[i]);
                    List<String> attrCount;
                    if (firstLoop) {
                        attrCount = new ArrayList<>();
                    } else {
                        // dodaje do mapy z mozliwymi decyzjami atrybutu wartosc
                        attrCount = attributesCount.get(i);
                    }
                    attrCount.add(splittedLine[i]);
                    attributesCount.put(i, attrCount);
                    decisions.add(splittedLine[splittedLine.length - 1]);
                }
                trainObjs.add(new TrainObj(attrs, splittedLine[splittedLine.length-1]));
                firstLoop = false;
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        // zostawiam pojedyncze wartosci w liscie z mozliwymi decyzjami
        decisions = decisions.stream().distinct().collect(Collectors.toList());

// dla mapy z iloscia mozliwych wartosci atrybutow wykonuje distinct, tak zeby zostały tylko pojedyncze wartosci
        for (int i = 0; i<attributesCount.size(); i++){
            attributesCount.put(i, attributesCount.get(i).stream().distinct().collect(Collectors.toList()));
        }


        // z plikut z danymi testowymi zczytuje dane i tworze obiekty TestObj
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(testFile.toFile())));
            String line;
            while ((line = br.readLine()) != null){
                String[] splittedLine = line.split(",");
                List<String> attrs = new ArrayList<>();
                attrs.addAll(Arrays.asList(splittedLine).subList(0, splittedLine.length - 1));

                testObjs.add(new TestObj(attrs, splittedLine[splittedLine.length-1]));
            }
        } catch (IOException e){
            e.printStackTrace();
        }

        int tests = 0;
        int rightDecisions = 0;
        // przechodze w petli po kazdym obiekcie testowym
        for (TestObj test : testObjs){
            String bayesDecision = getDecision(test, czyWygladzanie);
            if (bayesDecision.equals(test.decision))
                rightDecisions++;
            tests++;
        }

        System.out.println(rightDecisions + " : " + tests);

        GUI gui = new GUI();
        gui.showGUI();
    }

    public static String getDecision (TestObj test, boolean czyWygladzanie){
            // tworze mape, ktora bedzie przechowywac dla kazdej potencjalnej decyzji jej output (prawdopodobienstwo)
            Map<String, Double> decisionOutput = new HashMap<>();
            // przechodze w petli po wszystkich decyzjach
            for (String decision : decisions){
                // tworze zmienne licznika, mianownika i outputu
                double numerator = 0;
                double denominator = 0;
                double output = 1;
                // przechodze w petli po kazdym atrybucie obiektu testowego
                for(int i = 0; i<test.attributes.size(); i++) {
                    // wyliczam ze wszystkich obiektow treningowych prawdopodobienstwo (output)
                    for (TrainObj train : trainObjs) {
                        if (train.attributes.get(i).equals(test.attributes.get(i)) && train.decision.equals(decision))
                            numerator++;
                        if (train.decision.equals(decision))
                            denominator++;
                    }
                    // po wyliczeniu prawdopodobienstwa dla danego atrybutu mnoze przez nie wczesniejszy output
                    // wykonuje wygladzanie dla kazdego atrybutu
                    if (czyWygladzanie) {
                        output = output * ((numerator + 1) / (denominator + attributesCount.get(i).size()));
                    }
                    // wygladzanie tylko w przypadku gdy licznik jest rowny 0
                    else{
                        if (numerator == 0)
                            output = output * ((numerator + 1) / (denominator + attributesCount.get(i).size()));
                        else
                            output = output * ( numerator / denominator);
                    }
                    numerator = 0;
                    denominator = 0;
                }
                // do mapy dodaje decyzje i jej prawdopodobienstwo
                decisionOutput.put(decision, output);
            }
            // po wyliczeniu prawdopodobienstw dla kazdej decyzji sortuje mape, tak aby decyzja z najwiekszym byla pierwsza
            decisionOutput = decisionOutput.entrySet().stream()
                    .sorted(Map.Entry.comparingByValue(Comparator.reverseOrder()))
                    .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                            (oldVal, newVal) -> oldVal, LinkedHashMap::new));

        return decisionOutput.entrySet().iterator().next().getKey();
    }
}
