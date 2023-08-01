package socialmedia;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Scanner;

public class ConsoleApplication {
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        PostDAO postDAO = new PostDAO();
        UserDAO userDAO = new UserDAO();

        // Ask for user credentials
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User authenticatedUser = userDAO.authenticateUser(username, password);

        if (authenticatedUser != null) {
            System.out.println("Authentication successful. Welcome, " + authenticatedUser.getUsername() + "!");
            while (true) {
                System.out.println("1. Create Post");
                System.out.println("2. View My Posts");
                System.out.println("3. Update Post");
                System.out.println("4. Delete Post");
                System.out.println("5. Exit");
                System.out.print("Enter your choice: ");
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume the newline character after reading the integer.

                switch (choice) {
                    case 1:
                    	Posts(scanner, postDAO,authenticatedUser);
                        break;
                    case 2:
                        Posts(postDAO, authenticatedUser);
                        break;
                    case 3:
                        updatePost(scanner, postDAO);
                        break;
                    case 4:
                        deletePost(scanner, postDAO);
                        break;
                    case 5:
                        System.out.println("Existing...");
                        scanner.close();
                        System.exit(0);
                    default:
                        System.out.println("Invalid choice. Please try again.");
                        break;
                }
            }
        } else {
            System.out.println("Authentication failed. Exiting...");
            scanner.close();
            System.exit(1);
        }
    }

    private static void Posts(Scanner scanner, PostDAO postDAO, User user ) {
        System.out.print("Enter platform (e.g., Facebook, Twitter): ");
        String platform = scanner.nextLine();
        System.out.print("Enter content: ");
        String content = scanner.nextLine();
        System.out.print("Enter schedule (YYYY-MM-DD HH:mm): ");
        String scheduleStr = scanner.nextLine();
        LocalDateTime schedule = LocalDateTime.parse(scheduleStr, formatter);

        SocialMediaPost post = new SocialMediaPost(0, scheduleStr, scheduleStr, schedule);
        post.setPlatform(platform);
        post.setContent(content);
        post.setSchedule(schedule);
        postDAO.createPost(post);
        post.setUserId(user.getId());
        System.out.println("Post created successfully!");
    }

    private static void Posts(PostDAO postDAO, User user) {
        List<SocialMediaPost> posts = postDAO.getPostsByUserId(user.getId());
        System.out.println("My Posts:");
        for (SocialMediaPost post : posts) {
            System.out.println(
                post.getId() + " | " + post.getPlatform() + " | " + post.getContent() + " | " + post.getSchedule().format(formatter)
            );
        }
    }

    private static void updatePost(Scanner scanner, PostDAO postDAO) {
        System.out.print("Enter the ID of the post to update: ");
        int postId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading the integer.

        SocialMediaPost postToUpdate = postDAO.getPostById(postId);
        if (postToUpdate == null) {
            System.out.println("Post with ID " + postId + " not found.");
        } else {
            System.out.print("Enter new platform (e.g., Facebook, Twitter): ");
            String platform = scanner.nextLine();
            System.out.print("Enter new content: ");
            String content = scanner.nextLine();
            System.out.print("Enter new schedule (YYYY-MM-DD HH:mm): ");
            String scheduleStr = scanner.nextLine();
            LocalDateTime schedule = LocalDateTime.parse(scheduleStr, formatter);

            postToUpdate.setPlatform(platform);
            postToUpdate.setContent(content);
            postToUpdate.setSchedule(schedule);
            postDAO.updatePost(postToUpdate);
            System.out.println("Post updated successfully!");
        }
    }

    private static void deletePost(Scanner scanner, PostDAO postDAO) {
        System.out.print("Enter the ID of the post to delete: ");
        int postId = scanner.nextInt();
        scanner.nextLine(); // Consume the newline character after reading the integer.

        boolean deleted = postDAO.deletePost(postId);
        if (deleted) {
            System.out.println("Post with ID " + postId + " deleted successfully.");
        } else {
            System.out.println("Post with ID " + postId + " not found.");
        }
    }

}