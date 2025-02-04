package entity;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class User {
    private int userId;
    private String username;
    private String password;
    private String email;

    public User() {}

    public User(int userId, String username, String password, String email) {
        this.userId = userId;
        this.username = username;
        this.password = hashPassword(password);
        this.email = email;
    }

    public int getUserId() { return userId; }
    public void setUserId(int userId) { this.userId = userId; }

    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = hashPassword(password); }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    // Hashing password for security
    private String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] bytes = md.digest();
            StringBuilder sb = new StringBuilder();
            for (byte b : bytes) {
                sb.append(String.format("%02x", b));
            }
            return sb.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException("Error hashing password", e);
        }
    }
}
