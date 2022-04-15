public class Iris {
    private double [] attributes;
    public Iris (double[] attributes){
        this.attributes = attributes;
    }

    public double[] getAttributes() {
        return attributes;
    }

    public double getDistanceFrom (double[] centroid){
        double sum = 0;
        for (int i = 0; i < this.attributes.length; i++){
            sum += Math.pow((centroid[i] - this.attributes[i]), 2);
        }
        return sum;
    }

    public String toString(){
        String text = "";
        for (Double d : this.attributes){
            text += d + ", ";
        }
        return text;
    }
}
