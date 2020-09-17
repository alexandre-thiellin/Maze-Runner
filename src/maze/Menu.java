package maze;

import java.io.IOException;
import java.util.Scanner;

public class Menu {

    Maze currentMaze = null;

    private boolean finished = false;

    public Menu() {

        while(!finished) {

            if (currentMaze == null) {

                System.out.println("=== Menu ===");
                System.out.println("1. Generate a new maze");
                System.out.println("2. Load a maze");
                System.out.println("0. Exit");

                Scanner scanner = new Scanner(System.in);
                String action = scanner.nextLine();

                switch (action) {
                    case "1":
                        currentMaze = new Maze();
                        currentMaze.display();
                        break;
                    case "2":
                        loadMaze();
                        break;
                    case "0":
                        System.out.println("Bye!");
                        scanner.close();
                        finished = true;
                        break;
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
            } else {

                System.out.println("=== Menu ===");
                System.out.println("1. Generate a new maze");
                System.out.println("2. Load a maze");
                System.out.println("3. Save the maze");
                System.out.println("4. Display the maze");
                System.out.println("5. Find the escape");
                System.out.println("0. Exit");

                Scanner scanner = new Scanner(System.in);
                String action = scanner.nextLine();

                switch (action) {
                    case "1":
                        currentMaze = new Maze();
                        currentMaze.display();
                        break;
                    case "2":
                        loadMaze();
                        break;
                    case "3":
                        saveMaze();
                        break;
                    case "4":
                        currentMaze.display();
                        break;
                    case "5":
                        currentMaze.findEscape();
                        break;
                    case "0":
                        System.out.println("Bye!");
                        scanner.close();
                        finished = true;
                        break;
                    default:
                        System.out.println("Incorrect option. Please try again");
                        break;
                }
            }

            System.out.println();
        }
    }

    public void loadMaze() {

        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();

        try {
            currentMaze = (Maze) SerializationUtils.deserialize(file);
        } catch (IOException e) {
            System.out.println("The file ... does not exist");
        } catch (ClassNotFoundException e) {
            System.out.println("Cannot load the maze. It has an invalid format");
        }
    }

    public void saveMaze() {

        Scanner scanner = new Scanner(System.in);
        String file = scanner.nextLine();

        try{
            SerializationUtils.serialize(currentMaze,file);
        } catch(IOException e){
            e.printStackTrace();
        }
    }
}
