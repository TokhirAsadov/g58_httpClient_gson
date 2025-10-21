package uz.pdp.service;

import uz.pdp.utill.AppUtills;

import java.io.IOException;
import java.net.URISyntaxException;

public class MainService {
    public static void run() throws IOException, URISyntaxException, InterruptedException {
        w:
        while (true) {
            showMainMenu();
            switch (AppUtills.intScanner.nextInt()) {
                case 1 -> {
                    GroupService.run();
                }
                case 2 -> {
                    StudentService.run();
                }
                case 3 -> {
                    System.out.println("Tashrif uchun rahmat..");
                    break w;
                }
                default -> {
                    System.out.println("Unknown Command.. Please try again.🔁");
                }
            }
        }
    }

    private static void showMainMenu() {
        System.out.println("""
                1. Group Service
                2. Student Service
                3. Exit
                """);
    }
}
