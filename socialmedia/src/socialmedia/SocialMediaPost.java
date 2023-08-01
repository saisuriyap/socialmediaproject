package socialmedia;

import java.time.LocalDateTime;
public class SocialMediaPost {
    private int id;
    private String platform;
    private String content;
    private LocalDateTime schedule;
    private int userId; // Use userId instead of user_id

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public LocalDateTime getSchedule() {
        return schedule;
    }

    public void setSchedule(LocalDateTime schedule) {
        this.schedule = schedule;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    // Constructors
    public SocialMediaPost(int id, String platform, String content, LocalDateTime schedule) {
        this.id = id;
        this.platform = platform;
        this.content = content;
        this.schedule = schedule;
    }

    public SocialMediaPost() {
    }
}
