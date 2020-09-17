package maze;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Objects;

public class Cell implements Comparable<Cell>, Serializable {

    private static final long serialVersionUID = 1L;

    private int id;
    private Edge north = null;
    private Edge east = null;
    private Edge south = null;
    private Edge west = null;

    private boolean marked = false;

    public Cell(int id) {

        this.id = id;
    }

    public ArrayList<Cell> getNeighborsNotMarked() {
        ArrayList<Cell> cells = new ArrayList<>();
        if(north != null
                && north.getOther(this) != null
                && !north.getOther(this).isMarked()) {

            cells.add(north.getOther(this));
        }
        if(east != null
                && east.getOther(this) != null
                && !east.getOther(this).isMarked()) {

            cells.add(east.getOther(this));
        }
        if(south != null
                && south.getOther(this) != null
                && !south.getOther(this).isMarked()) {

            cells.add(south.getOther(this));
        }
        if(west != null
                && west.getOther(this) != null
                && !west.getOther(this).isMarked()) {

            cells.add(west.getOther(this));
        }
        return cells;
    }

    public ArrayList<Edge> getEdges() {
        ArrayList<Edge> edges = new ArrayList<>();
        if(north != null) {
            edges.add(north);
        }
        if(east != null) {
            edges.add(east);
        }
        if(south != null) {
            edges.add(south);
        }
        if(west != null) {
            edges.add(west);
        }
        return edges;
    }

    public ArrayList<Edge> getEdgesNotMarked() {
        ArrayList<Edge> edges = new ArrayList<>();
        if(north != null && !north.isMarked()) {
            edges.add(north);
        }
        if(east != null && !east.isMarked()) {
            edges.add(east);
        }
        if(south != null && !south.isMarked()) {
            edges.add(south);
        }
        if(west != null && !west.isMarked()) {
            edges.add(west);
        }
        return edges;
    }

    public void deleteEdgesNotMarked() {
        if(north != null && !north.isMarked()) {
            north = null;
        }
        if(east != null && !east.isMarked()) {
            east = null;
        }
        if(south != null && !south.isMarked()) {
            south = null;
        }
        if(west != null && !west.isMarked()) {
            west = null;
        }
    }

    public Edge getNorth() {

        return north;
    }

    public void setNorth(Edge north) {

        this.north = north;
    }

    public Edge getEast() {

        return east;
    }

    public void setEast(Edge east) {

        this.east = east;
    }

    public Edge getSouth() {

        return south;
    }

    public void setSouth(Edge south) {

        this.south = south;
    }

    public Edge getWest() {

        return west;
    }

    public void setWest(Edge west) {

        this.west = west;
    }

    public boolean isMarked() {

        return marked;
    }

    public void setMarked(boolean marked) {

        this.marked = marked;
    }

    @Override
    public boolean equals(Object o) {

        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cell cell = (Cell) o;
        return id == cell.id;
    }

    @Override
    public int hashCode() {

        return Objects.hash(north, east, south, west, marked);
    }

    @Override
    public int compareTo(Cell cell) {

        return id - cell.id;
    }

    @Override
    public String toString() {

        return "Cell{" +
                "id=" + id +
                '}';
    }
}
