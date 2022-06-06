public class Main {
    static int [] sizes = {2, 6, 10, 7, 5, 2,  4, 1, 2, 9,  7, 9, 6, 4,   1, 6, 9, 9,   5, 4, 7, 2,  3, 8, 1, 7};
    static int [] values = {6, 8, 16, 12, 6, 17, 9, 20, 4, 4, 4, 12, 4, 15, 7, 19, 13, 9, 18, 6, 8, 1, 15, 7, 17, 10};
    static int capacity = 40;
    public static void main(String[] args) {
        int maxValsSum = 0;
        String vector = "";
        for (int i = 0; i < Math.pow(2, (sizes.length - 1)); i++){
            int valsSum = 0;
            int sizesSum = 0;
            String binary = Integer.toBinaryString(i);
            for (int j = 0; j < binary.length(); j++){
                if (binary.charAt(j) == '1'){
                    valsSum += values[j];
                    sizesSum += sizes[j];
                }
                if (sizesSum > capacity){
                    break;
                }
            }

            if (sizesSum <= capacity && valsSum > maxValsSum){
                maxValsSum = valsSum;
                vector = binary;
                System.out.println("Obecna maksymalna wartosc: " + maxValsSum + ", wektor: " + vector);
            }
        }

        System.out.println(maxValsSum);
        System.out.println(vector);
    }
}
