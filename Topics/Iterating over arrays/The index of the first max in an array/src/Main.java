import java.util.Scanner;

class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int arraySize = scanner.nextInt();
        int[] arr = new int[arraySize];

        for (int i = 0; i < arraySize; i++) {
            arr[i] = scanner.nextInt();
        }

        int temp = 0;
        for (int i = 0; i < arr.length; i++) {
            if (arr[i] > arr[temp]) {
                temp = i;
            }
        }
        System.out.println(temp);
    }
}