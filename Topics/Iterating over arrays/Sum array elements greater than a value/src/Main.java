import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int arraySize = scanner.nextInt();
        int[] arr = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            arr[i] = scanner.nextInt();
        }

        int n = scanner.nextInt();

        int temp = 0;
        for (int i : arr) {
            if (i > n) {
                temp += i;
            }
        }
        System.out.println(temp);
    }
}