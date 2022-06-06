import java.io.*;
import java.util.*;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class Main {
    private static final String filePath = "C:\\Users\\micha\\OneDrive\\Pulpit\\NAI\\k-means\\iris_all.csv";
    private static int k;
    private static List<Group> groups = new ArrayList<>();
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Type number of clasters: ");
        k = scanner.nextInt();
        List<Iris> irises = new ArrayList<>();
        try(BufferedReader br = new BufferedReader(new InputStreamReader(new FileInputStream(filePath)))){
            String line;
            boolean firstLine = true;
            while ((line = br.readLine()) != null){
                if (firstLine){
                    firstLine = false;
                    continue;
                }
                String [] splittedLine = line.split(",");
                double [] attributes = new double[splittedLine.length - 2];
                for (int i = 1; i < splittedLine.length - 1; i++){
                    attributes[i - 1] = Double.parseDouble(splittedLine[i]);
                }
                irises.add(new Iris(attributes));
            }
        } catch (IOException e){
            e.printStackTrace();
        }
        // tworze k obiektów Group i kazdemu z nich przypisuje jeden obiekt Iris, zeby po inicjalizacji zaden nie byl pusty
        for (int i = 0; i < k; i++){
            groups.add(new Group());
            groups.get(i).getIrises().add(irises.get(i));
        }
        // przypisuje reszte Irysow do losowo wybranej grupy
        for (int i = k; i < irises.size(); i++){
            Random rand = new Random();
            groups.get(rand.nextInt(groups.size())).addIris(irises.get(i));
        }
        // tworze zmienna boolean, ktora bedzie sprawdzac, czy w petli zaszla jakas zmiana
        boolean changeAppeared = true;
        while(changeAppeared){
            changeAppeared = false;
            // wypisuje sume odlegosci irysow od ich centroidu dla kazdej grupy
            for(Group group : groups)
                System.out.println(group.getSumOfDistances() + "     " + group.getIrises().size());
            // tworze mape, ktora przechowuje grupe jako klucz i jej centroid jako wartosc
            Map<Group, double[]> centroids = new LinkedHashMap<>();
            for (Group group : groups){
                centroids.put(group, group.getCentroid());
            }
            // petla dla wszystkich irysow
            for (Iris iris : irises){
                    // sortujemy mape tak, aby pierwszy element mial najblizszy centroid dla danego Irysa
                    centroids = centroids.entrySet().stream()
                            .sorted((o1, o2) -> {
                                if (iris.getDistanceFrom(o1.getValue()) <= iris.getDistanceFrom(o2.getValue()))
                                    return -1;
                                else
                                    return 1;
                            })
                            .collect(Collectors.toMap(Map.Entry::getKey, Map.Entry::getValue,
                                    (oldVal, newVal) -> oldVal, LinkedHashMap::new));
                    // pobieramy grupe o centroidzie najblizszym aktualnemu irysowi
                    Group closestCentroid = centroids.entrySet().iterator().next().getKey();
                    // jesli ta grupa zawiera tego irysa, to znaczy, ze zadna zmiana nie zachodzi
                    if (closestCentroid.getIrises().contains(iris))
                        continue;
                    else{
                        changeAppeared = true;
                        for (Group group1 : groups){
                            if (group1.getIrises().contains(iris))
                                group1.getIrises().remove(iris);
                        }
                        closestCentroid.addIris(iris);
                    }
            }
            System.out.println("---------------------------------");
        }

        for (int i = 0; i < groups.size(); i++) {
            System.out.println("Group " + (i+1) + " " + groups.get(i).getIrises().size() + "/" + irises.size());
            for (Iris iris : groups.get(i).getIrises())
                System.out.println(iris.toString());
            System.out.println("------------------------------");
        }
    }
}
