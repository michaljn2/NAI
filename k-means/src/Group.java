import java.util.ArrayList;
import java.util.List;

public class Group {
    private List<Iris> irises;
    public Group(){
        this.irises = new ArrayList<>();
    }

    public List<Iris> getIrises() {
        return irises;
    }
    public void addIris(Iris iris){
        this.irises.add(iris);
    }
    public double[] getCentroid(){
        double [] sumVector = new double[irises.get(0).getAttributes().length];
        for (Iris iris : this.irises){
            for (int i = 0; i < iris.getAttributes().length; i++)
                sumVector[i] += iris.getAttributes()[i];
        }
        for (int i = 0; i < sumVector.length; i++){
            sumVector[i] = sumVector[i]/irises.size();
        }
        return sumVector;
    }

    public double getSumOfDistances(){
        double sum = 0;
        double [] centroid = this.getCentroid();
        for (Iris iris : this.irises){
            for (int i = 0; i < iris.getAttributes().length; i++){
                sum += Math.pow((centroid[i] - iris.getAttributes()[i]), 2);
            }
        }
        return sum;
    }
}
