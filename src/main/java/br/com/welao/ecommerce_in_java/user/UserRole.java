package br.com.welao.ecommerce_in_java.user;

public enum UserRole {

    ADMIN("admin"),

    USER("user");

    private String role;

    UserRole(String role) {
        this.role = role;
    }

    public String getROle() {
        return role;
    }


}
