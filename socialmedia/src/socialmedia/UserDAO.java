package socialmedia;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class UserDAO extends DAO{
    private Scanner scanner = new Scanner(System.in);

    public User authenticateUser(String username, String password) {
        User authenticatedUser = null;
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "SELECT * FROM users WHERE username = ? AND password = ?"
             )) {
            statement.setString(1, username);
            statement.setString(2, password);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                int id = resultSet.getInt("id");
                authenticatedUser = new User(id, username, password);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return authenticatedUser;
    }

    public void createUser(User user) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO users (username, password) VALUES (?, ?)"
             )) {
            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public User loginOrRegister() {
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        System.out.print("Enter password: ");
        String password = scanner.nextLine();

        User authenticatedUser = authenticateUser(username, password);

        if (authenticatedUser == null) {
            System.out.println("Authentication failed. User not found.");

            // Ask user if they want to register
            System.out.print("Do you want to register? (yes/no): ");
            String registerChoice = scanner.nextLine().toLowerCase();

            if (registerChoice.equals("yes")) {
                // Register the user
                System.out.print("Enter a new password: ");
                String newPassword = scanner.nextLine();
                User newUser = new User(username, newPassword);
                createUser(newUser);
                System.out.println("Registration successful. Welcome, " + newUser.getUsername() + "!");
                return newUser;
            } else {
                System.out.println("Exiting...");
                System.exit(0);
            }
        } else {
            System.out.println("Authentication successful. Welcome, " + authenticatedUser.getUsername() + "!");
            return authenticatedUser;
        }

        return null;
    }
}
class PostDAO extends UserDAO {
	public void createPost(SocialMediaPost post) {
        try (Connection connection = DBConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(
                     "INSERT INTO social_media_posts (platform, content, schedule, user_id) VALUES (?, ?, ?, ?)"
             )) {
            statement.setString(1, post.getPlatform());
            statement.setString(2, post.getContent());
            statement.setTimestamp(3, Timestamp.valueOf(post.getSchedule()));
            statement.setInt(4, 1); // Set the user_id
            statement.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

        public List<SocialMediaPost> getAllPosts() {
            List<SocialMediaPost> posts = new ArrayList<>();
            try (Connection connection = DBConnection.getConnection();
                 Statement statement = connection.createStatement();
                 ResultSet resultSet = statement.executeQuery("SELECT * FROM social_media_posts")) {

                while (resultSet.next()) {
                    int id = resultSet.getInt("id");
                    String platform = resultSet.getString("platform");
                    String content = resultSet.getString("content");
                    LocalDateTime schedule = resultSet.getTimestamp("schedule").toLocalDateTime();

                    SocialMediaPost post = new SocialMediaPost(id, platform, content, schedule);
                    post.setId(id);
                    post.setPlatform(platform);
                    post.setContent(content);
                    post.setSchedule(schedule);

                    posts.add(post);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return posts;
        }
        public void updatePost(SocialMediaPost post) {
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "UPDATE social_media_posts SET platform = ?, content = ?, schedule = ? WHERE id = ?"
                 )) {
                statement.setString(1, post.getPlatform());
                statement.setString(2, post.getContent());
                statement.setTimestamp(3, Timestamp.valueOf(post.getSchedule()));
                statement.setInt(4, post.getId());
                statement.executeUpdate();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }

        public boolean deletePost(int postId) {
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "DELETE FROM social_media_posts WHERE id = ?"
                 )) {
                statement.setInt(1, postId);
                int rowsAffected = statement.executeUpdate();
                return rowsAffected > 0;
            } catch (SQLException e) {
                e.printStackTrace();
                return false;
            }
        }
        public SocialMediaPost getPostById(int postId) {
            SocialMediaPost post = null;
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM social_media_posts WHERE id = ?"
                 )) {
                statement.setInt(1, postId);
                ResultSet resultSet = statement.executeQuery();

                if (resultSet.next()) {
                    String platform = resultSet.getString("platform");
                    String content = resultSet.getString("content");
                    LocalDateTime schedule = resultSet.getTimestamp("schedule").toLocalDateTime();
                    int userId = resultSet.getInt("id");

                    post = new SocialMediaPost();
                    post.setId(postId);
                    post.setPlatform(platform);
                    post.setContent(content);
                    post.setSchedule(schedule);
                    post.setId(userId);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return post;
        }
        public List<SocialMediaPost> getPostsByUserId(int userId) {
            List<SocialMediaPost> posts = new ArrayList<>();
            try (Connection connection = DBConnection.getConnection();
                 PreparedStatement statement = connection.prepareStatement(
                         "SELECT * FROM social_media_posts WHERE user_id = ?"
                 )) {
                statement.setInt(1, userId);
                ResultSet resultSet = statement.executeQuery();

                while (resultSet.next()) {
                    int postId = resultSet.getInt("id");
                    String platform = resultSet.getString("platform");
                    String content = resultSet.getString("content");
                    LocalDateTime schedule = resultSet.getTimestamp("schedule").toLocalDateTime();

                    SocialMediaPost post = new SocialMediaPost();
                    post.setId(postId);
                    post.setPlatform(platform);
                    post.setContent(content);
                    post.setSchedule(schedule);
                    post.setId(userId);
                    posts.add(post);
                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return posts;
        }
    
    }


