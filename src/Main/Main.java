package Main;

import java.util.Scanner;
import java.security.MessageDigest;

public class Main {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        String username, password;

        System.out.print("Enter username: ");
        username = scanner.nextLine();

        System.out.print("Enter password: ");
        password = scanner.nextLine();

        try {
            // Use MessageDigest para hash da senha
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            md.update(password.getBytes());
            byte[] hash = md.digest();

            // Verifique a senha com hash em relação ao valor esperado
            String expectedHash = "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92";
            if (username.equals("admin") && new String(hash).equals(expectedHash)) {
                System.out.println("Login successful!");
            } else {
                System.out.println("Login failed. Incorrect username or password.");
            }
        } catch (Exception e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
}
