import java.util.Scanner;

public class Main {
    private static void pvp() {
        Battlefield player1 = new Battlefield(1, 1);
        Battlefield player2 = new Battlefield(2, 1);
        player1.initiateField();
        player2.initiateField();

        int moveCount = 0;
        while (player1.hasNotLost() && player2.hasNotLost()) {
            System.out.println("Press ENTER to start your move.");
            player1.waitNextPlayer();
            if (moveCount % 2 == 1) {
                System.out.println(player2.foggyField() + "\n-----------------------\n" + player1 +
                        "\nPlayer 1, it's your turn:");
                player2.shooting();
            } else {
                System.out.println(player1.foggyField() + "\n-----------------------\n" + player2 +
                        "\nPlayer 2, it's your turn:");
                player1.shooting();
            }

            moveCount++;
        }

        if (player1.hasNotLost()) {
            System.out.println("Player 1 won after " + moveCount + " total shots");
        } else if (player2.hasNotLost()) {
            System.out.println("Player 2 won after " + moveCount + " total shots");
        }
        System.out.println("Player 1:\n" + player1);
        System.out.println("Player 2:\n" + player2);
    }

    private static void pve() {
        Battlefield player = new Battlefield(1, 0);
        Battlefield computer = new Battlefield(0, 0);
        computer.initiateField();
        player.initiateField();

        int moveCount = 0;
        while (player.hasNotLost() && computer.hasNotLost()) {
            if (moveCount % 2 == 0) {
                System.out.println(Battlefield.printSituation(player, computer) +
                        "\n\nWhere do you want to shoot, captain?\n" +
                        "Enter coordinates:");
                computer.shooting();
            } else {
                player.computerShooting();
            }

            moveCount++;
        }
        if (player.hasNotLost()) {
            System.out.println("You won after " + moveCount + " total shots");
        } else if (computer.hasNotLost()) {
            System.out.println("You lost after " + moveCount + " total shots");
        }
        System.out.println("Enemy field:\n" + computer);
        System.out.println("Your field:\n" + player);
    }

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        // get game mode from input
        System.out.println("Welcome to a game of Battleships!\n" +
                "Define the game mode (1 = single player mode, 2 = two player mode):");
        String mode = scanner.next();
        while (!mode.equals("1") && !mode.equals("2")) {
            System.out.println("Invalid game mode. Try again:");
            mode = scanner.next();
        }

        if (mode.equals("2")) pvp();
        else pve();
    }
}
