import java.util.Scanner;
import java.util.Arrays;

class Main {
    // implement me
    private static void rotate(int[] arr, int steps) {

        for (int i = 0; i < arr.length; i++) {
           //System.out.print(arr[i] + " ");
        }

        for (int i = 0; i < steps; i++){
            int last = 0;
            last = arr[arr.length - 1];
            for (int j = arr.length -1; j > 0; j--) {
                arr[j] = arr[j-1];
            } arr[0] = last;
        }
    }


    // do not change code below
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        int[] arr = Arrays.stream(scanner.nextLine().split(" "))
                .mapToInt(Integer::parseInt)
                .toArray();

        int steps = Integer.parseInt(scanner.nextLine());

        rotate(arr, steps);

        for (int i : arr) {
            System.out.print(i + " ");
        }
    }
}