package bto.system.utils;

import bto.system.models.BTOProject;
import java.util.List;
import java.util.Scanner;
import java.util.stream.Collectors; 

public class MenuPrinter {
    private static final String HEADER_LINE = "=================================";
    private static final Scanner scanner = new Scanner(System.in);

    /**
     * Prints a formatted menu header
     */
    public static void printHeader(String title) {
        System.out.println("\n" + HEADER_LINE);
        System.out.println("   " + title);
        System.out.println(HEADER_LINE);
    }

    /**
     * Displays a numbered menu and returns user selection
     */
    public static int printNumberedMenu(String title, List<String> options) {
        printHeader(title);
        for (int i = 0; i < options.size(); i++) {
            System.out.println((i + 1) + ". " + options.get(i));
        }
        System.out.println("0. Back");
        System.out.print("Select option: ");

        return scanner.nextInt();
    }

    /**
     * Displays projects in a tabular format
     */
    public static void printProjects(List<BTOProject> projects) {
        System.out.printf("\n%-20s %-15s %-12s %-10s %-15s\n",
                "Project", "Neighborhood", "Flat Types", "Units", "Status");
        System.out.println(HEADER_LINE);

        projects.forEach(p -> {
            String flatTypes = String.join("/",
                    p.getFlatTypes().stream()
                            .map(ft -> ft.getType())
                            .toList());

            String units = p.getFlatTypes().stream()
                    .map(ft -> String.valueOf(ft.getAvailableUnits()))
                    .collect(Collectors.joining("/"));

            System.out.printf("%-20s %-15s %-12s %-10s %-15s\n",
                    p.getName(),
                    p.getNeighborhood(),
                    flatTypes,
                    units,
                    p.isAcceptingApplications() ? "OPEN" : "CLOSED");
        });
    }

    /**
     * Gets user input with prompt
     */
    public static String getInput(String prompt) {
        System.out.print(prompt + ": ");
        return scanner.nextLine();
    }

    /**
     * Displays a confirmation dialog
     */
    public static boolean confirmAction(String message) {
        System.out.print(message + " (Y/N): ");
        String input = scanner.nextLine();
        return input.equalsIgnoreCase("Y");
    }
}
