package assets;

public class Supporting {
    public static String aggregateString(String... args) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            res.append(args[i]).append(",");
        }
        res.append(args[args.length - 1]);
        return String.valueOf(res);
    }
}
