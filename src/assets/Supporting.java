package assets;

import com.github.javafaker.Faker;

import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Random;

public class Supporting {
    public static final Faker faker = new Faker();

    public static String aggregateString(String... args) {
        StringBuilder res = new StringBuilder();
        for (int i = 0; i < args.length - 1; i++) {
            res.append(args[i]).append(",");
        }
        res.append(args[args.length - 1]);
        return String.valueOf(res);
    }

    public static void generateSupplyCenterData() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("supply_center.txt"))) {
            for (int i = 0; i < 500000; i++) {
                writer.write(faker.name().title() + "," + faker.name().fullName() + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void generateProductAndModelData() {
        Random random = new Random();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter("product_model.txt"))) {
            for (int i = 0; i < 50000; i++) {
                writer.write(faker.code().asin().substring(0, 7) + ";" + (faker.name().fullName() + " " + faker.name().title()) + ";" + faker.name().name() + ";" + random.nextInt(500) + "\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
