import java.io.*;
import java.util.*;
import java.util.stream.Collectors;

public class Main {
    static List<String> results;
    static List<TrainIris> irises;
    static int k;
    public static void main(String[] args){
        // tworze liste, ktora przechowuje treningowe dane, czyli obiekty TrainIris
        irises = new ArrayList<>();
        results = new ArrayList<>();
        String line;
        try {
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("iristrain.csv")));
            boolean firstLoop = true;
            while ((line = br.readLine()) != null){
                // pomijam pierwsza linie opisowa
                if (firstLoop){
                    firstLoop = false;
                    continue;
                }
                String[] splittedLine = line.split(",");
                results.add(splittedLine[splittedLine.length-1]);
                double[] attributes = new double[splittedLine.length-2];
                for (int i=1;i<=splittedLine.length-2;i++){
                    attributes[i-1] = Double.parseDouble(splittedLine[i]);
                }
                irises.add(new TrainIris(attributes, splittedLine[splittedLine.length-1]));
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        // lista results przechowuje wszystkie mozliwe atrybuty decyzyjne
        results = results.stream().distinct().collect(Collectors.toList());
        // pytam uzytkownika o podanie parametru k
        Scanner scanner = new Scanner(System.in);
        System.out.println("Podaj parametr k:");
        k = scanner.nextInt();
        int testsCounter = 0;
        int rightDecisions = 0;
        // zaczynamy testy
        try{
            BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream("iristest.csv")));
            boolean firstLoop = true;
            while ((line = br.readLine()) != null){
                if (firstLoop){
                    firstLoop = false;
                    continue;
                }
                String[] splittedLine = line.split(",");
                double[] attributes = new double[splittedLine.length-2];
                for (int i=1; i<=splittedLine.length-2; i++){
                    attributes[i-1] = Double.parseDouble(splittedLine[i]);
                }
                String decision = getResult(attributes, results, irises, k);
                testsCounter++;
                // sprawdzam czy podjeta decyzja zgadza sie z danymi z pliku
                if (decision.equals(splittedLine[splittedLine.length-1]))
                    rightDecisions++;
            }
            System.out.println("Poprawność wykonanych testów wynosi: " + ((rightDecisions*100)/testsCounter) + "%");
            System.out.println(rightDecisions + " : " + testsCounter);
        } catch (IOException e){
            e.printStackTrace();
        }
        // ewentualnie można uruchomić GUI do sprawdzania pojedynczych danych
        GUI gui = new GUI();
        gui.showGUI(irises.get(0).attributes.length);
    }

    public static String getResult(double[] testAttributes, List<String> results, List<TrainIris> irises, int k){
        // tworze mape, ktora przechowywac bedzie ilosc wystapien kazdego atrybutu decyzyjnego
        // w badanym podzbiorze najblizszych obserwacji
        Map<String, Integer> decisions = new LinkedHashMap<>();
        for (String result : results){
            decisions.put(result, 0);
        }
        // w strumieniu sortuje zbior treningowy po odleglosci euklidesowej, a nastepnie dla k najblizszych
        // obserwacji aktualizuje liczbe wystapien poszczegolnyych decyzji w mapie
        irises.stream()
                .sorted(new Comparator<TrainIris>() {
                    @Override
                    public int compare(TrainIris o1, TrainIris o2) {
                        double o1Length = 0;
                        double o2Length = 0;
                        for (int i = 0; i<o1.attributes.length; i++){
                            o1Length += Math.pow((o1.attributes[i] - testAttributes[i]), 2);
                            o2Length += Math.pow((o2.attributes[i] - testAttributes[i]), 2);
                        }
                        o1Length = Math.sqrt(o1Length);
                        o2Length = Math.sqrt(o2Length);
                        if (o1Length <= o2Length)
                            return -1;
                        else
                            return 1;
                    }
                })
                .limit(k)
                .forEach(iris -> {
//                            double length = 0;
//                            for (int i = 0; i<iris.attributes.length; i++){
//                                length = length + Math.pow((iris.attributes[i] - testAttributes[i]), 2);
//                            }
//                            length = Math.sqrt(length);
//                            System.out.println(length);
                    decisions.put(iris.decision, decisions.get(iris.decision)+1);
                });
        // sprawdzam maksymalna ilosc danej obserwacji z podzbioru
        int maxDecisions = decisions.values().stream().max(new Comparator<Integer>() {
            @Override
            public int compare(Integer o1, Integer o2) {
                if (o1 > o2)
                    return 1;
                else
                    return -1;
            }
        }).get();
        // przy kazdej obserwacji pokażę ilość wystapien kazdej mozliwej decyzji
        for (Map.Entry<String, Integer> decision : decisions.entrySet()){
            System.out.println(decision.getKey() + " : " + decision.getValue());
        }
        System.out.println();
        String decision = "";
        // przypadek, gdy moze byc kilka decyzji o tej samej ilosci wystapien
        if (maxDecisions <= k/2){
            List<String> potentialDecisions = new ArrayList<>();
            for(Map.Entry<String, Integer> tmp : decisions.entrySet()){
                if (tmp.getValue() == maxDecisions){
                    potentialDecisions.add(tmp.getKey());
                }
            }
            Random random = new Random();
            decision = potentialDecisions.get(random.nextInt(potentialDecisions.size()));
        }
        else{
            for(Map.Entry<String, Integer> tmp : decisions.entrySet()){
                if (tmp.getValue() == maxDecisions){
                    decision = tmp.getKey();
                    break;
                }
            }
        }
        return decision;
    }
}
class TrainIris{
    double[] attributes;
    String decision;
    public TrainIris(double[] attributes, String decision){
        this.attributes = attributes;
        this.decision = decision;
    }
}
