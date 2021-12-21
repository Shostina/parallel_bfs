import java.util.Queue;
import java.util.concurrent.*;
import java.util.concurrent.atomic.AtomicBoolean;

public class BFS {
    public static final int THREADS_NUM = 4;
    static int startNode = 0;
    public static final int n = 400;
    public static final int nodesSize = n * n * n;
    public static final CubeGraph graph = new CubeGraph(n);
    public static final AtomicBoolean[] visited = new AtomicBoolean[n * n * n];
    public static final int[] depth = new int[n * n * n];
    public static Queue<Integer> frontier = new LinkedBlockingQueue<>();
    public static Queue<Integer> newFrontier = new LinkedBlockingQueue<>();
    CountDownLatch latch = new CountDownLatch(THREADS_NUM);
    ExecutorService threadPool = Executors.newFixedThreadPool(THREADS_NUM);

    public long bfs() {
        initVisited();
        frontier.add(startNode);
        visited[startNode] = new AtomicBoolean(true);
        depth[startNode] = 0;

        long start = System.nanoTime();
        BfsStep task = new BfsStep(graph, null);
        task.run();
        while (!newFrontier.isEmpty()) {
            frontier = newFrontier;
            newFrontier = new LinkedBlockingQueue<Integer>();
            (new BfsStep(graph, null)).run();
        }
        long time = (System.nanoTime() - start);
        System.out.println("bfs:" + time);
        return time;
    }

    public long parallelBfs() throws Exception {
        initVisited();
        frontier.add(startNode);
        visited[startNode] = new AtomicBoolean(true);
        depth[startNode] = 0;
        long start = System.nanoTime();
        BfsStep task = new BfsStep(graph, null);
        task.run();
        while (!newFrontier.isEmpty()) {
            frontier = newFrontier;
            newFrontier = new LinkedBlockingQueue<Integer>();
            for (int i = 0; i < THREADS_NUM; i++) {
                threadPool.execute(new BfsStep(graph, latch));
            }
            latch.await();
            latch = new CountDownLatch(THREADS_NUM);
        }
        long time = (System.nanoTime() - start);
        System.out.println("parallel bfs:" + time);
        return time;
    }

    private void printDepth() {
        for (int i = 0; i < depth.length; i++) {
            System.out.println(i + ": " + depth[i]);
        }
    }

    private void initVisited() {
        frontier = new LinkedBlockingQueue<>();
        newFrontier = new LinkedBlockingQueue<>();
        for (int i = 0; i < nodesSize; i++) {
            visited[i] = new AtomicBoolean(false);
        }
    }

    public static void main(String[] args) throws Exception {
        BFS bfs = new BFS();
        long timeBfs = bfs.bfs();
        long timeParallelBfs = bfs.parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = bfs.parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = bfs.parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = bfs.parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = bfs.parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        bfs.threadPool.shutdown();
    }
}
