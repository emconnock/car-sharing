import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int arraySize = scanner.nextInt();
        int[] arr = new int[arraySize];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = scanner.nextInt();
        }

        boolean sorted = true;

        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] > arr[i + 1]) {
                sorted = false;
                break;
            }
        }
        System.out.println(sorted);
    }
}