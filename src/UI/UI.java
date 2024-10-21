package UI;

import domain.User;
import service.NetworkService;

import java.util.*;
import java.util.Scanner;
import java.util.stream.IntStream;

public class UI {
    private final NetworkService service;
    private Scanner scanner;

    public UI(NetworkService service) {
        this.service = service;
        this.scanner = new Scanner(System.in);
    }

    public void printMenu() {
        System.out.println("1. Add user");
        System.out.println("2. Delete user");
        System.out.println("3. Add Friendship");
        System.out.println("4. Delete Friendship");
        System.out.println("5. See all users");
        System.out.println("6. See all friendships");
        System.out.println("7. Show number of communities");
        System.out.println("8. Show the biggest community");
        System.out.println("0. Exit");
    }

    private void addUserText() {
        System.out.print("Enter first name: ");
        String firstName = scanner.nextLine();
        System.out.print("Enter last name: ");
        String lastName = scanner.nextLine();
        System.out.print("Enter username: ");
        String username = scanner.nextLine();
        service.addUser(firstName, lastName, username);
        System.out.println("\u001B[32mOperation successful\u001B[0m\n");
    }

    private void addFriendText() {
        System.out.print("Enter the first username: ");
        String firstUsername = scanner.nextLine();
        System.out.print("Enter the second username: ");
        String secondUsername = scanner.nextLine();
        service.addFriendship(firstUsername, secondUsername);
        System.out.println("\u001B[32mOperation successful\u001B[0m\n");
    }

    private void removeUser() {
        System.out.print("Enter the username: ");
        String username = scanner.nextLine();
        service.removeUser(username);
        System.out.println("\u001B[32mOperation successful\u001B[0m\n");
    }

    private void removeFriend() {
        System.out.print("Enter the first username: ");
        String username1 = scanner.nextLine();

        System.out.print("Enter the second username: ");
        String username2 = scanner.nextLine();
        service.removeFriendship(username1, username2);
        System.out.println("\u001B[32mOperation successful\u001B[0m\n");
    }

    private void getAllUsers() {
        System.out.println("\nAll users are:");
        service.getAllUsers().forEach(System.out::println);
        System.out.println();
    }

    private void getAllFriends() {
        System.out.println("\nAll friendships are:");
        service.getAllFriendships().forEach(System.out::println);
        System.out.println();
    }

    private void showNumberCommunities() {
        System.out.println("\nThe number of communities is: " + service.numberOfCommunities() + "\n");
    }

    private void showBiggestCommunity() {
        List<List<User>> com=service.biggestCommunity();
        System.out.println();
        IntStream.range(1,com.size()+1).
            forEach(index-> {
                if (index == 1) {
                    System.out.println("First community");
                } else if (index == 2) {
                    System.out.println("Second community");
                } else if (index == 3) {
                    System.out.println("Third community");
                } else {
                    System.out.println(index + "th community");
                }

                com.get(index-1).forEach(user-> System.out.println(user));
                System.out.println();
            });
    }

    public void launchUI() {
        while (true) {
            printMenu();
            System.out.print("Enter your choice: ");
            String choice = scanner.nextLine();
            try {
                switch(choice) {
                    case "1": addUserText();
                    break;
                    case "2": removeUser();
                    break;
                    case "3": addFriendText();
                    break;
                    case "4": removeFriend();
                    break;
                    case "5": getAllUsers();
                    break;
                    case "6": getAllFriends();
                    break;
                    case "7": showNumberCommunities();
                    break;
                    case "8": showBiggestCommunity();
                    break;
                    case "0": System.exit(0);
                    break;
                    default:
                        System.out.println("\u001B[31mInvalid choice\u001B[0m\n");
                        break;
                }
            } catch(RuntimeException message) {
                System.out.println("\u001B[31m" + message.getMessage() + "\u001B[0m\n");
            }
        }
    }
}
