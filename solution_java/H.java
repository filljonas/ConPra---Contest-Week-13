import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.*;

public class H {
    
    private static double[] getDoubleArrFromLineString(String line) {
        String[] splitted = line.split(" ");
        double[] arr = new double[splitted.length];
        for (int i = 0; i < splitted.length; i++) {
            arr[i] = Double.parseDouble(splitted[i]);
        }
        return arr;
    }

    public static void main(String[] args) throws IOException {
        InputStreamReader r = new InputStreamReader(System.in);
        BufferedReader br = new BufferedReader(r);

        int t = Integer.parseInt(br.readLine());
        boolean[] results = new boolean[t];
        for (int i = 0; i < t; i++) {
            String[] inputs = br.readLine().split(" ");
            int nx = Integer.parseInt(inputs[0]);
            int ny = Integer.parseInt(inputs[1]);
            double w = Double.parseDouble(inputs[2]);
            double[] startXs = getDoubleArrFromLineString(br.readLine());
            double[] startYs = getDoubleArrFromLineString(br.readLine());
            results[i] = coversField(w, startXs, startYs);
            
            if (i < t - 1) {
                br.readLine();
            }
        }
        // output
        for (int i = 0; i < results.length; i++) {
            String res = results[i]? "YES" : "NO";
            System.out.println("Case #" + (i + 1) + ": " + res);
        }
        br.close();
    }

    private static boolean coversField(double lawnWidth, double[] startXs, double[] startYs) {
        // given length and width for the field
        double length = 100.0;
        double width = 75.0;
        double halfLawn = lawnWidth / 2.0;

        // greedily check if the whole field is covered
        
        double covered = 0;
        Arrays.sort(startXs);
        for (int i = 0; i < startXs.length; i++) {
            if (covered + 1e-7 < startXs[i] - halfLawn) {
                // some parts already not covered
                return false;
            }
            covered = startXs[i] + halfLawn;
        }
        if (covered < width - 1e-7) {
            return false;
        }

        covered = 0;
        Arrays.sort(startYs);
        for (int i = 0; i < startYs.length; i++) {
            if (covered + 1e-7 < startYs[i] - halfLawn) {
                // some parts already not covered
                return false;
            }
            covered = startYs[i] + halfLawn;
        }
        if (covered < length - 1e-7) {
            return false;
        }

        // both passes covered everything
        return true;
    }

}
