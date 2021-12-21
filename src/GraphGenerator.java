import java.io.BufferedWriter;
import java.io.FileWriter;
import java.util.ArrayList;

public class GraphGenerator {

    int n = 3;
    //ArrayList<ArrayList<Integer>> nodes = new ArrayList<>(n*n*n);
    //boolean[] flags = new boolean[n * n * n];

    private void generateGraph() throws Exception {
        int id = 0;
        int[][][] helpNodes = new int[n][n][n];
        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                for(int z = 0; z < n; z++) {
                    helpNodes[x][y][z] = id;
                    id += 1;
                }
            }
        }
        BufferedWriter writer = new BufferedWriter(new FileWriter("graph.txt"));

        for (int x = 0; x < n; x++) {
            for (int y = 0; y < n; y++) {
                for(int z = 0; z < n; z++) {
                    int curId = helpNodes[x][y][z];
                    writer.write(curId + " ");
                    if (x > 0) {
                        writer.write(helpNodes[x - 1][y][z] + " ");
                    }
                    if (y > 0) {
                        writer.write(helpNodes[x][y - 1][z] + " ");
                    }
                    if (z > 0) {
                        writer.write(helpNodes[x][y][z - 1] + " ");
                    }
                    if (x < n - 1) {
                        writer.write(helpNodes[x + 1][y][z] + " ");
                    }
                    if (y < n - 1) {
                        writer.write(helpNodes[x][y + 1][z] + " ");
                    }
                    if (z < n - 1) {
                        writer.write(helpNodes[x][y][z + 1] + " ");
                    }
                    writer.write('\n');
                }
            }
        }
        writer.close();
    }

/*    private void printGraph() {
        for (int i = 0; i < 10; i++) {
            System.out.print(i);
            for (int j = 0; j < nodes.get(i).size(); j++) {
                System.out.print(nodes.get(i).get(j) + " ");
            }
            System.out.println();
        }
    }*/

/*    public void bfs(ArrayList<Integer> layer, int[] distance) {
        ArrayList<Integer> newLayer = new ArrayList<>();
        for (Integer curInd : layer) {
            for (int i = 0; i < 6; i++) {
                
            }
        }
    }*/

    public static void main(String[] args) throws Exception {
        GraphGenerator myGraphGenerator = new GraphGenerator();
        myGraphGenerator.generateGraph();
    }
}
