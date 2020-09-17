package maze;

import java.io.Serializable;
import java.util.*;

public class Maze implements Serializable {

    private static final long serialVersionUID = 1L;

    private Cell[][] cells;
    private ArrayList<Edge> edges = new ArrayList<>();

    private int nbRows;
    private int nbColumns;

    private Cell entranceCell;
    private Cell exitCell;

    private Stack<Cell> escapePath = new Stack<>();
    private boolean found = false;

    public Maze() {

        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the size of a new maze");
        int size = Integer.parseInt(scanner.nextLine());

        generate(size, size);
    }

    private void generate(int nbRows, int nbColumns) {

        this.nbRows = nbRows;
        nbRows = nbRows % 2 == 0 ? ((nbRows / 2) - 1) : (nbRows / 2);
        this.nbColumns = nbColumns;
        nbColumns = nbColumns % 2 == 0 ? ((nbColumns / 2) - 1) : (nbColumns / 2);

        cells = new Cell[nbRows][nbColumns];

        // create a matrix n,m of cells
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j] = new Cell((i * 100) + (j + 1));
            }
        }

        // for each cells except the cells from the last row and cells from the last column
        // create an edge between this cell and the cell below
        // create an edge between this cell and the cell on the right
        // adding edges to the list
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {

                if(i < cells.length - 1) {
                    Edge south = new Edge((i * 100) + (j + 1), new Cell[]{cells[i][j], cells[i + 1][j]});
                    cells[i][j].setSouth(south);
                    cells[i + 1][j].setNorth(south);

                    edges.add(south);
                }

                if(j < cells[0].length - 1) {
                    Edge east = new Edge((i * 100) + (j + 1), new Cell[]{cells[i][j], cells[i][j + 1]});
                    cells[i][j].setEast(east);
                    cells[i][j + 1].setWest(east);

                    edges.add(east);
                }
            }
        }

        // Prim's algorithm
        // Select the root node
        int x = new Random().nextInt(nbColumns), y = new Random().nextInt(nbRows);
        Cell root = cells[y == nbRows / 2 ? y - 1 : y][x == nbColumns / 2 ? x - 1 : x];
        root.setMarked(true);

        TreeSet<Cell> spanningTree = new TreeSet<>();
        spanningTree.add(root);


        TreeSet<Edge> workingEdges = new TreeSet<>();

        while(!allNodeSelected()) {

            // Consider all edges {x,y} such that x is a node of the current tree and y is not
            for(Cell c : spanningTree) {
                ArrayList<Edge> edgesNotSelected = c.getEdgesNotMarked();
                for(Edge e : edgesNotSelected) {
                    if(!e.getOther(c).isMarked()) {
                        workingEdges.add(e);
                    }
                }
            }

            // Find the edge with the smallest weight
            Edge min = workingEdges.first();
            for (Edge e : workingEdges) {
                if(e.getWeight() < min.getWeight()) {
                    min = e;
                }
            }

            // Select the edge (and the cells)
            // Add cells to the tree
            min.setMarked(true);
            min.getPair()[0].setMarked(true);
            min.getPair()[1].setMarked(true);
            spanningTree.addAll(Arrays.asList(min.getPair()));

            workingEdges.clear();
        }

        // Delete all the edges that weren't chosen
        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].deleteEdgesNotMarked();
            }
        }

        // Create entrance and exit
        int entrance = new Random().nextInt(nbRows);
        int exit = new Random().nextInt(nbRows);

        cells[entrance][0].setWest(new Edge(Integer.MIN_VALUE, new Cell[] {null, null}));
        cells[exit][nbColumns - 1].setEast(new Edge(Integer.MAX_VALUE, new Cell[] {null, null}));

        entranceCell = cells[entrance][0];
        exitCell = cells[exit][nbColumns - 1];

        resetMarks();
    }

    private boolean allNodeSelected() {

        boolean selected = true;

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                if(!cells[i][j].isMarked()) {
                    return false;
                }
            }
        }

        return true;
    }


    public void findEscape() {

        searchEscapePath(entranceCell);

        resetMarks();

        escapePath.get(0).getWest().setMarked(true);
        for (int i = 0; i < escapePath.size() - 1; i++) {
            escapePath.get(i).setMarked(true);
            for (Edge e : escapePath.get(i).getEdgesNotMarked()) {
                if (e.getOther(escapePath.get(i)).equals(escapePath.get(i + 1))) {
                    e.setMarked(true);
                }
            }
        }
        escapePath.peek().setMarked(true);
        escapePath.peek().getEast().setMarked(true);

        display();
        resetMarks();
    }

    // function inspired by "depth-first search" functions
    // look it up online for more informations
    public void searchEscapePath(Cell cell) {

        // mark the cell and push it in the stack
        cell.setMarked(true);
        escapePath.push(cell);

        // if the cell is the exit cell the function stop
        if (cell.equals(exitCell)) {
            found = true;
        }
        // if the function is not stopped and the cell's neighbors are all marked
        // then pop out the cell from the stack
        else if (!found && cell.getNeighborsNotMarked().isEmpty()) {
            escapePath.pop();
        }
        // if the cell is not the exit cell and if the cell have neighbors who are not marked
        else {
            // for every neighbors of the cell verify if the function is stopped
            // if not do the function on all cell's neighbors
            for (Cell c : cell.getNeighborsNotMarked()) {
                if (!found) {
                    searchEscapePath(c);
                }
            }
            // if the function isn't stopped
            // pop the cell out of stack
            if (!found) {
                escapePath.pop();
            }
        }
    }

    public void resetMarks() {

        for (int i = 0; i < cells.length; i++) {
            for (int j = 0; j < cells[0].length; j++) {
                cells[i][j].setMarked(false);
                for (Edge e : cells[i][j].getEdges()) {
                    e.setMarked(false);
                }
            }
        }
    }

    public void display() {

        String maze = "";

        boolean oddRows = nbRows % 2 != 0;
        boolean oddColumns = nbColumns % 2 != 0;

        int iLength = cells.length;
        int jLength = cells[0].length;

        // Write a line of blank
        for (int i = 0; i < nbColumns; i++) {
            maze += "\u2588\u2588";
        }

        maze += "\n";

        for (int i = 0; i < iLength; i++) {

            // Write the maze 2 lines by 2 lines
            // Each line start with a blank except the line with the entrance
            String east = cells[i][0].getWest() != null ? (cells[i][0].isMarked() ? "//" : "  ") : "\u2588\u2588";
            String south = "\u2588\u2588";


            for (int j = 0; j < jLength; j++) {

                String nodeSymbol = cells[i][j].isMarked() ? "//" : "  ";
                String edgeSymbol = cells[i][j].getEast() != null ? (cells[i][j].getEast().isMarked() ? "//" : "  ") : "\u2588\u2588";

                if (oddColumns) {
                    east += nodeSymbol + edgeSymbol;
                } else {
                    if (j == jLength - 1) {
                        east += (cells[i][j].equals(exitCell) ? nodeSymbol + edgeSymbol + (exitCell.isMarked() ? "//" : "  ") : nodeSymbol + "  \u2588\u2588");
                    } else {
                        east += nodeSymbol + edgeSymbol;
                    }
                }

                edgeSymbol = cells[i][j].getSouth() != null ? (cells[i][j].getSouth().isMarked() ? "//" : "  ") : "\u2588\u2588";

                if (oddRows) {
                    if (oddColumns) {
                        south += edgeSymbol + "\u2588\u2588";
                    } else {
                        south += (j == jLength - 1 ? edgeSymbol + "\u2588\u2588\u2588\u2588" : edgeSymbol + "\u2588\u2588");
                    }
                } else {
                    if (oddColumns) {
                        if (i == iLength -1) {
                            south += "  \u2588\u2588";
                        } else {
                            south += edgeSymbol + "\u2588\u2588";
                        }
                    } else {
                        if (j == jLength - 1) {
                            if (i == iLength - 1) {
                                south += "  \u2588\u2588\u2588\u2588";
                            } else {
                                south += edgeSymbol + "\u2588\u2588\u2588\u2588";
                            }
                        } else {
                            if (i == iLength - 1) {
                                south += "  \u2588\u2588";
                            } else {
                                south += edgeSymbol +"\u2588\u2588";
                            }
                        }
                    }
                }
            }

            maze += east + "\n" + south + "\n";
        }

        // If the number of rows is even then write a line of blank
        if (!oddRows) {
            for (int i = 0; i < nbColumns; i++) {
                maze += "\u2588\u2588";
            }
        }

        System.out.println(maze);
    }
}
