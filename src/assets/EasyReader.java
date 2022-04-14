package assets;

import java.io.InputStream;
import java.util.Scanner;

public class EasyReader {
    private final Scanner in;

    public EasyReader(InputStream inputStream) {
        in = new Scanner(inputStream);
    }

    public String nextLine(String invoke) {
        System.out.print(invoke + ": ");
        return in.nextLine();
    }

    public int nextInt(String invoke) {
        System.out.print(invoke + ": ");
        return in.nextInt();
    }
}
