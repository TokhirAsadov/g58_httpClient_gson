package uz.pdp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import uz.pdp.entity.Student;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

import static uz.pdp.utill.AppUtills.intScanner;

public class StudentService {
    public static void run() throws URISyntaxException, IOException, InterruptedException {
        w: while (true) {
            showStudentMenu();
            switch (intScanner.nextInt()) {
                case 1 -> {
                    // Show all students
                }
                case 2 -> {
                    showSingleStudentById();
                }
                case 3 -> {
                    // Create new student
                }
                case 4 -> {
                    // Update student
                }
                case 5 -> {
                    // Delete student
                }
                case 6 -> {
                    break w;
                }
                default -> {
                    System.out.println("Unknown Command.. Please try again.🔁");
                }
            }
        }
    }

    private static void showSingleStudentById() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter student id: ");
        long studentId = intScanner.nextLong();
        // Logic to fetch and display student by id
        String url = "http://localhost:8080/api/students/" + studentId;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        if (body==null || body.isEmpty()){
            System.out.println("Id=%d li student mavjud emas❌".formatted(studentId));
        } else {
            Gson gson = new GsonBuilder().create();
            Student student = gson.fromJson(body, Student.class);
            System.out.println(student);
        }
        System.out.println("---------------------");
    }

    private static void showStudentMenu() {
        System.out.println("""
                1. Show all students
                2. Show single student by id
                3. Create new student
                4. Update student
                5. Delete student
                6. Back to menu
                """);
    }
}
