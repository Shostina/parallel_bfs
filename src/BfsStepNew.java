import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BfsStepNew implements Runnable {

    private final Graph graph;
    private final CountDownLatch latch;
    private final int curNode;

    BfsStepNew(Graph graph, CountDownLatch latch, int curNode) {
        this.graph = graph;
        this.latch = latch;
        this.curNode = curNode;
    }

    @Override
    public void run() {
        List<Integer> neighbours = graph.getNeighbours(curNode);
        for (Integer nextNode : neighbours) {
            if (BFS.visited[nextNode].compareAndSet(false, true)) {
                BFS.newFrontier.add(nextNode);
                BFS.depth[nextNode] = BFS.depth[curNode] + 1;
            }
        }
        if (latch != null) {
            latch.countDown();
        }
    }
}
