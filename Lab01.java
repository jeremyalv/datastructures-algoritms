import java.io.*;
import java.util.StringTokenizer;
import java.util.Map;
import java.util.HashMap;

public class Lab01 {
    private static InputReader in;
    private static PrintWriter out;

    public static void main(String[] args) throws IOException {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Read value of N
        int N = in.nextInt();

        // Read value of x
        char[] x = new char[N];
        for (int i = 0; i < N; ++i) {
            x[i] = in.next().charAt(0);
        }

        int ans = getTotalDeletedLetters(N, x);
        out.println(ans);

        // don't forget to close/flush the output
        out.close();
    }

    static int getTotalDeletedLetters(int N, char[] x) {
        // Create counter for 
        Map<Character, Integer> map = new HashMap<>();
        map.put('S', 0);
        map.put('O', 0);
        map.put('F', 0);
        map.put('I', 0);
        map.put('T', 0);
        map.put('A', 0);

        for (int i = 0; i < N; i++) {
            if (map.containsKey(x[i])) {                                
                switch (x[i]) {
                    case 'S':
                        map.put(x[i], map.get(x[i]) + 1);
                    case 'O':
                        if (map.get('S') > 0) {
                            map.put('S', map.get('S') - 1);
                            map.put(x[i], map.get(x[i]) + 1);
                        }
                        break;
                    case 'F':
                        if (map.get('O') > 0) {
                            map.put('O', map.get('O') - 1);
                            map.put(x[i], map.get(x[i]) + 1);
                        }
                        break;
                    case 'I':
                        if (map.get('F') > 0) {
                            map.put('F', map.get('F') - 1);
                            map.put(x[i], map.get(x[i]) + 1);
                        }
                        break;
                    case 'T':
                        if (map.get('I') > 0) {
                            map.put('I', map.get('I') - 1);
                            map.put(x[i], map.get(x[i]) + 1);
                        }
                        break;
                    case 'A':
                        if (map.get('T') > 0) {
                            map.put('T', map.get('T') - 1);
                            map.put(x[i], map.get(x[i]) + 1);
                        }
                        break;
                }
            }
        }        
        

        // After calculating sofitaOccurence, count the deleted letters
        int deletedAmount = N - (6 * map.get('A'));

        return deletedAmount;
    }

    // taken from https://codeforces.com/submissions/Petr
    // together with PrintWriter, these input-output (IO) is much faster than the usual Scanner(System.in) and System.out
    // please use these classes to avoid your fast algorithm gets Time Limit Exceeded caused by slow input-output (IO)
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