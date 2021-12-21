import java.util.List;
import java.util.concurrent.CountDownLatch;

public class BfsStep implements Runnable {

    private final Graph graph;
    private final CountDownLatch latch;

    BfsStep(Graph graph, CountDownLatch latch) {
        this.graph = graph;
        this.latch = latch;
    }

    @Override
    public void run() {
        while (true) {
            Integer curNode = BFS.frontier.poll();
            if (curNode != null) {
                List<Integer> neighbours = graph.getNeighbours(curNode);
                for (Integer nextNode : neighbours) {
                    if (BFS.visited[nextNode].compareAndSet(false, true)) {
                        BFS.newFrontier.add(nextNode);
                        BFS.depth[nextNode] = BFS.depth[curNode] + 1;
                    }
                }
            } else {
                break;
            }
        }
        if (latch != null) {
            latch.countDown();
        }
    }
}
