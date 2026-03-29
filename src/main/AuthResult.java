package main;

public class AuthResult {

    private final boolean authenticated;
    private final String username;
    private final String fullName;
    private final String role;
    private final String message;

    private AuthResult(boolean authenticated, String username, String fullName, String role, String message) {
        this.authenticated = authenticated;
        this.username = username;
        this.fullName = fullName;
        this.role = role;
        this.message = message;
    }

    public static AuthResult success(String username, String fullName, String role) {
        return new AuthResult(true, username, fullName, role, "Login successful.");
    }

    public static AuthResult failure(String message) {
        return new AuthResult(false, null, null, null, message);
    }

    public boolean isAuthenticated() {
        return authenticated;
    }

    public String getUsername() {
        return username;
    }

    public String getFullName() {
        return fullName;
    }

    public String getRole() {
        return role;
    }

    public String getMessage() {
        return message;
    }
}
