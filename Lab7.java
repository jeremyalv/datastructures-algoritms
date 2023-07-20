import java.io.*;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.PriorityQueue;
import java.util.StringTokenizer;
import java.util.Comparator;
import java.util.HashMap;

// REFERENCES
// 1. https://takeuforward.org/data-structure/dijkstras-algorithm-using-priority-queue-g-32/

public class Lab7 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        int N = in.nextInt(), M = in.nextInt();

        // Create Kingdom
        Kingdom kingdom = new Kingdom(N + 1);

        // Set attacked
        for (int i = 0; i < M; i++) {
            int F = in.nextInt();
            
            // Set attacked flag
            kingdom.setAttacked(F);
        }

        int E = in.nextInt();
        for (int i = 0; i < E; i++) {
            int A = in.nextInt(), B = in.nextInt(), W = in.nextInt();
            
            kingdom.addFortress(A, B, W);            
        }

        // Calculate shortest distance
        kingdom.calculateShortestDistance();

        int Q = in.nextInt();
        while (Q-- > 0) {
            int S = in.nextInt(), K = in.nextInt();
            
            out.println(kingdom.canBeSaved(S, K) ? "YES" : "NO");
        }

        out.close();
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the
    // usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit
    // Exceeded caused by slow input-output (IO)
    static class InputReader {
        public BufferedReader reader;
        public StringTokenizer tokenizer;

        public InputReader(InputStream stream) {
            reader = new BufferedReader(new InputStreamReader(stream), 32768);
            tokenizer = null;
        }

        public String next() {
            while (tokenizer == null || !tokenizer.hasMoreTokens()) {
                try {
                    tokenizer = new StringTokenizer(reader.readLine());
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
            return tokenizer.nextToken();
        }

        public int nextInt() {
            return Integer.parseInt(next());
        }

    }
}

class Fortress {
    int id;
    long distance;

    public Fortress(int id, long distance) {
        this.distance = distance;
        this.id = id;
    }

    public long getDistance() {
        return distance;
    }

    @Override
    public String toString() {
        return String.format("{id: %d, dist: %d}", id, distance);
    }
}

class Kingdom {
    // Graph
    private ArrayList<ArrayList<Fortress>> kingdom;
    private ArrayList<Integer> attacked; // Stores Id of attacked
    private HashMap<Integer, long[]> shortestPaths;

    public Kingdom(int fortressAmt) {
        // Initialize # of fortresses
        kingdom = new ArrayList<>();
        for (int i = 0; i < fortressAmt; i++) {
            kingdom.add(new ArrayList<>());
        }

        attacked = new ArrayList<>();
        shortestPaths = new HashMap<>();
    }
    
    public void addFortress(int sourceId, int destinationId, long distance) {
        // Flip the id to filter out only attacked fortresses
        kingdom.get(destinationId).add(new Fortress(sourceId, distance));
    }

    public void setAttacked(int attackedId) {
        attacked.add(attackedId);
    }

    public void calculateShortestDistance() {
        for (int i = 0; i < attacked.size(); i++) {
            long[] dist = dijkstra(kingdom.size(), kingdom, attacked.get(i));

            shortestPaths.put(attacked.get(i), dist);
        }
    }

    /* Find shortest distance of all vertices from source vertex S */
    public long[] dijkstra(int V, ArrayList<ArrayList<Fortress>> adj, int S) {
        // Create PQ for storing nodes as a Pair  {dist, node} where dist is distance from source to node
        PriorityQueue<Fortress> pq = new PriorityQueue<Fortress>(Comparator.comparingLong(Fortress::getDistance));

        long[] dist = new long[V];

        // Initialize list as unvisited
        for (int i = 0; i < V; i++) {
            // Set max distance of Fortress
            dist[i] = Long.MAX_VALUE;
        }

        // Source is initialized with distance 0
        dist[0] = 0;
        dist[S] = 0;
        pq.add(new Fortress(S, 0));

        // Pop min distance node first from min-heap, then traverse for all adjacent nodes
        while (pq.size() != 0) {
            Fortress f = pq.poll();
            long dis = f.distance;
            int fortId = f.id;

            for (int i = 0; i < adj.get(fortId).size(); i++) {
                long edgeWeight = adj.get(fortId).get(i).distance;
                int adjFortress = adj.get(fortId).get(i).id;

                // If current dist is smaller, push it into queue
                if (dis + edgeWeight < dist[adjFortress]) {
                    dist[adjFortress] = dis + edgeWeight;

                    pq.add(new Fortress(adjFortress, dist[adjFortress]));
                }
            }
        }

        // Return the list containing the shortest distances from source to all of the fortresses
        return dist;
    }

    public boolean canBeSaved(int source, long sentTroops) {
        long minDist = Long.MAX_VALUE;

        for (int i = 0; i < attacked.size(); i++) {
            int id = attacked.get(i);
            long[] distances = shortestPaths.get(id);
            long sourceDistance = distances[source];

            if (sourceDistance < minDist) {
                minDist = sourceDistance;
            }
        }

        if (minDist < sentTroops) {
            return true;
        }

        return false;
    }

    @Override
    public String toString() {
        String res = "";

        return res;
    }

    public ArrayList<ArrayList<Fortress>> getKingdom() {
        return kingdom;
    }

    public ArrayList<Integer> getAttacked() {
        return attacked;
    }

    public HashMap<Integer, long[]> getShortestPaths() {
        return shortestPaths;
    }
}

