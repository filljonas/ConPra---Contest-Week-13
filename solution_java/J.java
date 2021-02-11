import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class J {
    /**
     * Resources for reaching the solution:
     * https://math.stackexchange.com/questions/2389139/determining-neighbors-in-a-geometric-hexagon-pattern
     */
    static final int START = 1;
    static final int[] RESOURCES = {1, 2, 3, 4, 5};

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(r);

        int t = Integer.parseInt(br.readLine());
        int[] results = new int[t];
        int[] inputs = new int[t];
        int maxN = 0;
        
        for (int i = 0; i < t; i++) { 
            int n = Integer.parseInt(br.readLine());
            maxN = Math.max(maxN, n);
            inputs[i] = n;
        }

        // since the spiral is a sequence, we can compute just once for the largest n
        int[] spiral = nthInSpiral(maxN);
        for (int i = 0; i < t; i++) {
            results[i] = spiral[inputs[i] - 1];          
        }

        // output
        for (int i = 0; i < results.length; i++) {
            System.out.println("Case #" + (i + 1) + ": " + results[i]);
        }
        br.close();
    }

    private static int[] nthInSpiral(int n) {
        int[] frequencies = new int[RESOURCES.length + 1]; // for 1-based indexing
        int[] spiral = new int[n];

        // we always start at 1, in ring 0
        spiral[0] = START;
        frequencies[1]++;

        // first ring special case
        // only inner neighbour is 1
        for (int i = 1; i < Math.min(7, n); i++) {
            Set<Integer> forbidden = new HashSet<>();
            forbidden.add(1);
            forbidden.add(spiral[i - 1]);

            int chosen = getHighestPriorityResource(frequencies, forbidden);
            spiral[i] = chosen;
            frequencies[chosen]++;
        }
        
        // general case
        // reason on r, ring, and p, position in ring
        // other vars: e for edge, (where a ring has 6 edges), pe for position in edge
        for (int i = 7; i < n; i++) {
            int r = (int) Math.floor((3 + (Math.sqrt(12 * i - 3))) / 6);
            int p = i - (3 * r) * (r - 1) - 1;
            int e = p / r;

            Set<Integer> forbidden = new HashSet<>();
            // the prevous in the spiral is always a neighbour
            forbidden.add(spiral[i - 1]);

            // every edge has length r
            // position in edge
            int pe = p % r; 
            
            // previous / neighbouring ring / inner ring
            int nr = (r - 1);
            // position of at least one neighbour in inner ring
            // min(pe, nr - 1) because the current ring always has 1 more element in its edge than previous ring
            int np = e * nr + Math.min(pe, nr - 1);

            // idx of neighbour in spiral, based on its position in inner ring
            int nIdx = np + (3 * nr) * (nr - 1) + 1;

            /**
             * General notes:
             * If the current element is a "corner" of the current ring,
             * it only has 2 neighbours: the previous element in the spiral, the corresponding corner in the inner ring
             * If the element is not a corner, an element on an edge,
             * it has 3 neighours: the previous element in the spiral, and 2 consecutive neighbours in the inner ring
             * 
             * Corners of a ring can be recogonized by 6 positions:
             * p = r - 1, p = 2r -1, p = 3r - 1, p = 4r - 1, p = 5r - 1, p = 6r - 1
             * or more generally, p = 2 * (e + 1) - 1, where e is an edge .. [0..5]
             * 
             * Special cases:
             * #1: If element is the last element (also corner) in the ring, 
             *          then it also has the first element in the ring as a neighbour.
             * #2: If the corresponding neighbour in the inner ring is the first one in the ring, 
             *          then the other neighbour is the last element of the inner ring.
             * 
             * For equations relating i, r, p, e, refer to resource
             */

            // neighbour that is definitely there
            forbidden.add(spiral[nIdx]);

            // special case 1
            if (p == 6 * r - 1) {
                // last element in ring
                int firstInRingIdx = 0 + (3 * r) * (r - 1) + 1;
                forbidden.add(spiral[firstInRingIdx]);
            }

            // if element is not a corner and on an edge
            if (p % r != r - 1) {
                if (np == 0) {
                    // special case 2
                    // the last thing in prev ring / the thing before the first thing in this ring
                    int firstInRingIdx = 0 + (3 * r) * (r - 1) + 1;
                    forbidden.add(spiral[firstInRingIdx - 1]);
                } else {
                    forbidden.add(spiral[nIdx - 1]);
                }
            }

            int chosen = getHighestPriorityResource(frequencies, forbidden);
            spiral[i] = chosen;
            frequencies[chosen]++;
        }
        return spiral;
    }

    private static int getHighestPriorityResource(int[] frequencies, Set<Integer> forbiddenResources) {
        int minFreq = (int) 1e4;
        int minResource = (int) 1e4;
        for (int i = 1; i < frequencies.length; i++) {
            // since we're iterating in order (min resource first),
            // priority only depends on freq
            // don't need to check for f1 == f2 && r1 < r2
            if (frequencies[i] < minFreq && !forbiddenResources.contains(i)) {
                minFreq = frequencies[i];
                minResource = i;
            }
        }
        return minResource;
    }
}
