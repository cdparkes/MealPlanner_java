import java.util.Scanner;

public class Main {

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        String regex = "2[0-3]:[0-5]\\d|[0-1]\\d:[0-5]\\d";

        String time = scanner.nextLine();
        System.out.println(time.matches(regex) ? "YES" : "NO");
    }
}