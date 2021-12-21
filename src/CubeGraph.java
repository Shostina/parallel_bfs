import java.util.ArrayList;

public class CubeGraph implements Graph {
    private final int n;

    public CubeGraph(int n) {
        this.n = n;
    }

    private int getIdByCoordinates(int x, int y, int z) {
        return x * n * n + y * n + z;
    }

    private ArrayList<Integer> getCoordinatesById(int id) {
        int x = (id / n / n) % n;
        int y = (id / n) % n;
        int z = (id % n);
        ArrayList<Integer> res = new ArrayList<>();
        res.add(x);
        res.add(y);
        res.add(z);
        return res;
    }

    @Override
    public ArrayList<Integer> getNeighbours(int id) {
        ArrayList<Integer> neighbours = new ArrayList<>();
        ArrayList<Integer> coordinates = getCoordinatesById(id);
        int x = coordinates.get(0);
        int y = coordinates.get(1);
        int z = coordinates.get(2);
        if (x > 0) {
            neighbours.add(getIdByCoordinates(x - 1, y, z));
        }
        if (y > 0) {
            neighbours.add(getIdByCoordinates(x, y - 1, z));
        }
        if (z > 0) {
            neighbours.add(getIdByCoordinates(x, y, z - 1));
        }
        if (x < n - 1) {
            neighbours.add(getIdByCoordinates(x + 1, y, z));
        }
        if (y < n - 1) {
            neighbours.add(getIdByCoordinates(x, y + 1, z));
        }
        if (z < n - 1) {
            neighbours.add(getIdByCoordinates(x, y, z + 1));
        }
        return neighbours;
    }
}
