package uz.pdp.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import uz.pdp.entity.Group;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static uz.pdp.utill.AppUtills.intScanner;
import static uz.pdp.utill.AppUtills.strScanner;

public class GroupService {
    public static void run() throws IOException, URISyntaxException, InterruptedException {
        w:
        while (true) {
            showGroupMenu();
            switch (intScanner.nextInt()) {
                case 1 -> {
                    showGroups();
                }
                case 2 -> {
                    showSingleGroupById();
                }
                case 3 -> {
                    createNewGroup();
                }
                case 4 -> {
                    updateGroup();
                }
                case 5 -> {
                    deleteGroup();
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

    private static void deleteGroup() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter group id: ");
        long groupId = intScanner.nextLong();

        String url = "http://localhost:8080/api/groups/" + groupId;

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .DELETE()
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        int statusCode = response.statusCode();
        if (statusCode==204){
            System.out.println("Group is deleted..🎉");
        } else {
            System.out.println("Something went wrong..❌");
        }
        System.out.println("---------------------");
    }

    private static void updateGroup() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter group id: ");
        long groupId = intScanner.nextLong();
        System.out.print("Enter group new name: ");
        String name = strScanner.nextLine();
        System.out.print("Enter group new level: ");
        Integer level = intScanner.nextInt();

        Group newGroup = Group.builder()
                .name(name)
                .level(level)
                .build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(newGroup);


        String url = "http://localhost:8080/api/groups/" + groupId;


        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                //.connectTimeout(Duration.of(1000, TimeUnit.MILLISECONDS.toChronoUnit()))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type","application/json")
                .PUT(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        Group group = gson.fromJson(body, Group.class);
        System.out.println(group);
        System.out.println("Group is updated...🎉");
        System.out.println("---------------------");
    }

    private static void createNewGroup() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter new group name: ");
        String name = strScanner.nextLine();
        System.out.print("Enter group level: ");
        Integer level = intScanner.nextInt();

        Group newGroup = Group.builder()
                .name(name)
                .level(level)
                .build();

        Gson gson = new GsonBuilder().create();
        String jsonBody = gson.toJson(newGroup);


        String url = "http://localhost:8080/api/groups";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                //.connectTimeout(Duration.of(1000, TimeUnit.MILLISECONDS.toChronoUnit()))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .header("Content-Type","application/json")
                .POST(HttpRequest.BodyPublishers.ofString(jsonBody))
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();
        Group group = gson.fromJson(body, Group.class);
        System.out.println(group);
        System.out.println("New group is created...🎉");
        System.out.println("---------------------");

    }

    private static void showSingleGroupById() throws URISyntaxException, IOException, InterruptedException {
        System.out.print("Enter group id (int): ");
        long groupId = intScanner.nextLong();

        String url = "http://localhost:8080/api/groups/" + groupId;

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
            System.out.println("Id=%d li group mavjud emas❌".formatted(groupId));
        } else {
            Gson gson = new GsonBuilder().create();
            Group group = gson.fromJson(body, Group.class);
            System.out.println(group);
        }
        System.out.println("---------------------");
    }

    private static void showGroups() throws IOException, InterruptedException, URISyntaxException {
        String url = "http://localhost:8080/api/groups";

        HttpClient client = HttpClient.newBuilder()
                .version(HttpClient.Version.HTTP_2)
                //.connectTimeout(Duration.of(1000, TimeUnit.MILLISECONDS.toChronoUnit()))
                .build();

        HttpRequest request = HttpRequest.newBuilder()
                .uri(new URI(url))
                .GET()
                .build();

        HttpResponse<String> response = client
                .send(request, HttpResponse.BodyHandlers.ofString());

        String body = response.body();


        Gson gson = new GsonBuilder().create();
        Group[] groups = gson.fromJson(body, Group[].class);

        for (Group group : groups) {
            System.out.println(group);
        }
        System.out.println("---------------------");
//        System.out.println(body);

    }

    private static void showGroupMenu() {
        System.out.println("""
                1. Show all groups
                2. Show single group by id
                3. Create new group
                4. Update group
                5. Delete group
                6. Back to menu
                """);
    }
}
