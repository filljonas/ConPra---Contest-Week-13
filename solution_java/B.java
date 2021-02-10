import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class B {

    /**
     * UnionFind class based on interface provided in lecture
     * works for int, not generic
     */
    public static class UnionFind {
        int[] size;
        int[] parent;

        public UnionFind(int cap) {
            this.size = new int[cap];
            this.parent = new int[cap];
            for (int i = 0; i < cap; i++) {
                size[i] = 1;
                parent[i] = i;
            }
        }

        public int find(int a) {
            // Step 1: find the root (representative node)
            int root = a;
            while (root != parent[root]) {
                root = parent[root];
            }
            // Step 2: compress run
            int curr = a;
            while (curr != root) {
                int next = parent[curr];
                parent[curr] = root;
                curr = next;
            }
            return root;
        }

        public void union(int a, int b) {
            a = find(a);
            b = find(b);
            if (a == b) {
                return;
            }
            if (size[a] < size[b]) {
                // swap a and b
                int tmp = a;
                a = b;
                b = tmp;
            }
            parent[b] = a;
            size[a] = size[a] + size[b];
        }
     
    }

    public static class Edge implements Comparable<Edge> {
        int u;
        int v;
        int w;
        public Edge(int u, int v, int w) {
            this.u = u;
            this.v = v;
            this.w = w;
        }
        @Override
        public int compareTo(Edge other) {
            return this.w - other.w;
        }

        public int[] toArr() {
            return new int[] {this.u, this.v};
        }
        
        public String toString() {
            return "(" + this.u + " " + this.v + " " + this.w + ")";
        }
    }

    private static int[] getIntArrFromLineString(String line) {
        String[] splitted = line.split(" ");
        int[] arr = new int[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            arr[i] = Integer.parseInt(splitted[i]);
        }
        return arr;
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(r);

        int t = Integer.parseInt(br.readLine());
        int[] results = new int[t];
        for (int i = 0; i < t; i++) {
            int n = Integer.parseInt(br.readLine());
            int[][] villages = new int[n][3];
            for (int j = 0; j < villages.length; j++) {
                villages[j] = getIntArrFromLineString(br.readLine());
            }
            results[i] = solve(villages);

            if (i < t - 1) {
                br.readLine();
            }
        }

        // output
        for (int i = 0; i < results.length; i++) {
            String res = results[i] > 0? results[i] + "" : "impossible";
            System.out.println("Case #" + (i + 1) + ": " + res);
        }
        br.close();
    }

    private static int solve(int[][] villages) {
        int nbVillages = villages.length;
        // plane has infinite power
        int startingNodeIdx = nbVillages;
        int[] startingNode = new int[] {0, 0, (int) 1e8};

        // any village (as well as (0, 0)) can be connected to anything
        // iff it has sufficient power
        
        // build possible edges in the network
        ArrayList<Edge> edges = new ArrayList<>();
        for (int i = 0; i < nbVillages + 1; i++) {
            int[] u = (i == nbVillages)? startingNode: villages[i];
            for (int j = i + 1; j < nbVillages + 1; j++) {
                int[] v = (j == nbVillages)? startingNode: villages[j];
                // there is an edge between u and v if:
                // distance^2 between them <= power either can consume
                int distSquared = distanceSquared(u, v);
                int maxPower = Math.min(power(u), power(v));
                if (distSquared <= maxPower) {
                    edges.add(new Edge(i, j, distSquared));
                }
            }
        }

        // sort edges
        Collections.sort(edges);

        // kruskal's algorithm to find MST
        UnionFind uf = new UnionFind(nbVillages + 1);
        ArrayList<Edge> mstEdges = new ArrayList<>();
        int totalCost = 0;

        for (Edge edge : edges) {
            if (uf.find(edge.u) != uf.find(edge.v)) {
                // include edge
                mstEdges.add(edge);
                totalCost += edge.w;
                // merge in union find
                uf.union(edge.u, edge.v);
            }
        }

        // sanity check that all villages & (0,0) included
        int n = nbVillages + 1;
        if (mstEdges.size() != n - 1) {
            return -1;
        }
        // setup cost is edge cost * 2 (power in both directions)
        return 2 * totalCost;
    }
    
    private static int power(int[] node) {
        return node[2];
    }
    
    private static int distanceSquared(int[] u, int[] v) {
        int ux = u[0];
        int uy = u[1];
        int vx = v[0];
        int vy = v[1];
        return (ux - vx) * (ux - vx) + (uy - vy) * (uy - vy);
    }
}