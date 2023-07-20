import java.io.*;
import java.util.Map;
import java.util.Set;
import java.util.HashMap;
import java.util.HashSet;
import java.util.StringTokenizer;

public class Lab3 {
    private static InputReader in;
    private static PrintWriter out;

    public static char[] A;
    public static int N;

    private static int[][] cache = new int[0][0];

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Inisialisasi Array Input
        N = in.nextInt();
        A = new char[N];

        // Membaca File Input
        for (int i = 0; i < N; i++) {
            A[i] = in.nextChar();
        }

        cache = new int[N][N];

        for (int i = 0; i < N; i++) {
            for (int j = 0; j < N; j++) {
                cache[i][j] = -5;
            }
        }

        // Run Solusi
        int solution = getMaxRedVotes(0, N - 1);
        out.print(solution);

        // Tutup OutputStream
        out.close();
    }

    public static int getMaxRedVotes(int start, int end) {
        int maxResult = 0;

        if (start > end) {
            return 0;
        }
        if (start == end) {
            if (A[end] == 'R') {
                return 1;
            } else {
                return 0;
            }
        } else {
            for (int i = start; i < end; i++) {
                int left = 0;
                int right = 0;

                if (cache[start][i] >= 0) {
                    left = cache[start][i];
                } else {
                    left = getMaxRedVotes(start, i); 
                }

                if (cache[i + 1][end] >= 0) {
                    right = cache[i + 1][end];
                } else {
                    right = getMaxRedVotes(i + 1, end);
                }

                int result = left + right;

                if (result > maxResult) {
                    maxResult = result;
                }                                                 
            }

            int rCount = 0;
            int bCount = 0;
            int tempResult = 0;

            for (int i = start; i <= end; i++) {
                if (A[i] == 'R') {
                    rCount++;
                } else {
                    bCount++;
                }
            }

            if (rCount > bCount) {
                tempResult = rCount + bCount;
            } else {
                tempResult = 0;
            }
            
            if (tempResult > maxResult) {
                maxResult = tempResult;
            }

            cache[start][end] = maxResult;

            return maxResult;
        }
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

        public char nextChar() {
            return next().equals("R") ? 'R' : 'B';
        }
    }
}