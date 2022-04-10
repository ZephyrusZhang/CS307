import assets.Supporting;
import com.github.javafaker.Faker;

public class Main {
    public static void main(String[] args) {
        Faker faker = new Faker();
        System.out.println(faker.phoneNumber().subscriberNumber(11));
    }
}
