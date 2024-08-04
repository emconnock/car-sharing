import java.util.Scanner;

class Main {
    public static void main(String[] args) {

        Scanner scanner = new Scanner(System.in);

        int arraySize = scanner.nextInt();
        int[] arr = new int[arraySize];

        for (int i = 0; i < arr.length; i++) {
            arr[i] = scanner.nextInt();
        }
        int n = scanner.nextInt();
        int m = scanner.nextInt();

        boolean appearsNextTo = false;

        for (int i = 0; i < arr.length - 1; i++) {
            if (arr[i] == n && arr[i + 1] == m || arr[i] == m && arr[i + 1] == n) {
                appearsNextTo = true;
                break;
            }
        }
        System.out.println(!appearsNextTo);
    }
}

