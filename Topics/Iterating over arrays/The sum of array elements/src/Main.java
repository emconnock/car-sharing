import java.util.ArrayList;
import java.util.Scanner;

class Main {
    public static  void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        int size = scanner.nextInt();
        ArrayList<Integer> array = new ArrayList<>();
        for (int i = 0; i < size; i++) {
            array.add(scanner.nextInt());
        }

        int result = array.stream().reduce(0, (sum, elem) -> sum + elem);
        System.out.println(result);



    }
}