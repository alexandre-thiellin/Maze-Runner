package maze;

import java.io.Serializable;
import java.util.Random;

public class Edge implements Comparable<Edge>, Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Cell[] pair = new Cell[2];
    private int weight;

    private boolean marked = false;

    public Edge(int id, Cell[] pair) {

        this.id = id;
        this.pair = pair;
        weight = new Random().nextInt(100);
    }

    public Cell getOther(Cell cell) {
        return (cell.equals(pair[0])) ? pair[1] : pair[0];
    }

    public boolean contains(Cell cell) {

        return cell.equals(pair[0]) || cell.equals(pair[1]);
    }

    public Cell[] getPair() {

        return pair;
    }

    public void setPair(Cell[] pair) {

        this.pair = pair;
    }

    public int getWeight() {

        return weight;
    }

    public void setWeight(int weight) {

        this.weight = weight;
    }

    public boolean isMarked() {

        return marked;
    }

    public void setMarked(boolean marked) {

        this.marked = marked;
    }

    @Override
    public int compareTo(Edge edge) {

        return id - edge.id;
    }
}
