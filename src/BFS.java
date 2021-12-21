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

    public void runFirstVariant() throws Exception {
        long timeBfs = bfs();
        long timeParallelBfs = parallelBfs();
        System.out.println("First variant");
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        //bfs.printDepth();
        timeParallelBfs = parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfs();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
    }

    public void runSecondVariant() throws Exception {
        System.out.println("Second variant");
        long timeBfs = bfsNew();
        long timeParallelBfs = parallelBfsNew();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        //bfs.printDepth();
        timeParallelBfs = parallelBfsNew();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfsNew();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfsNew();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
        timeParallelBfs = parallelBfsNew();
        System.out.println((double)timeBfs/(double)timeParallelBfs);
    }

    public long bfs() {
        init();
        long start = System.nanoTime();
        BfsStep task = new BfsStep(graph, null);
        task.run();
        while (!newFrontier.isEmpty()) {
            frontier = newFrontier;
            newFrontier = new LinkedBlockingQueue<Integer>();
            (new BfsStep(graph, null)).run();
        }
        long time = (System.nanoTime() - start);
        System.out.println("bfs:" + ((double)time/1_000_000_000.0));
        return time;
    }

    public long bfsNew() {
        init();
        long start = System.nanoTime();
        BfsStepNew task = new BfsStepNew(graph, null, startNode);
        task.run();
        while (!newFrontier.isEmpty()) {
            frontier = newFrontier;
            newFrontier = new LinkedBlockingQueue<Integer>();
            for (int curNode : frontier) {
                (new BfsStepNew(graph, null, curNode)).run();
            }
        }
        long time = (System.nanoTime() - start);
        System.out.println("bfs new:" + ((double)time/1_000_000_000.0));
        return time;
    }

    public long parallelBfs() throws Exception {
        init();
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
        System.out.println("parallel bfs:" + ((double)time/1_000_000_000.0));
        return time;
    }

    public long parallelBfsNew() throws Exception {
        init();
        long start = System.nanoTime();
        BfsStepNew task = new BfsStepNew(graph, null, startNode);
        task.run();
        while (!newFrontier.isEmpty()) {
            frontier = newFrontier;
            newFrontier = new LinkedBlockingQueue<Integer>();
            latch = new CountDownLatch(frontier.size());
            for (int curNode : frontier) {
                threadPool.execute(new BfsStepNew(graph, latch, curNode));
            }
            latch.await();

        }
        long time = (System.nanoTime() - start);
        System.out.println("parallel bfs new:" + ((double)time/1_000_000_000.0));
        return time;
    }

    private void printDepth() {
        for (int i = 0; i < depth.length; i++) {
            System.out.println(i + ": " + depth[i]);
        }
    }

    private void init() {
        frontier = new LinkedBlockingQueue<>();
        newFrontier = new LinkedBlockingQueue<>();
        for (int i = 0; i < nodesSize; i++) {
            visited[i] = new AtomicBoolean(false);
        }
        frontier.add(startNode);
        visited[startNode] = new AtomicBoolean(true);
        depth[startNode] = 0;
    }

    public static void main(String[] args) throws Exception {
        BFS bfs = new BFS();
        bfs.runFirstVariant();
        bfs.runSecondVariant();
        bfs.threadPool.shutdown();
    }
}
