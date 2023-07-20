import java.io.*;
import java.util.ArrayDeque;
import java.util.Iterator;
import java.util.Stack;
import java.util.StringTokenizer;

public class Lab02 {
    private static InputReader in;
    private static PrintWriter out;

    // Buat conveyor belt
    private static ArrayDeque<Stack<Integer>> conveyorBelt = new ArrayDeque<>();
    
    static int geserKanan() {
        // Dapatkan elemen pertama dari queue dan hapus elemen tersebut
        Stack<Integer> headToples = conveyorBelt.poll();

        // Tambahkan rightmostToples ke akhir deque
        conveyorBelt.add(headToples);

        if (headToples.isEmpty()) {
            return -1; 
        } 

        return headToples.peek();
    }

    static int beliRasa(int rasa) {
        int shiftAmt = 0;
        int targetIndex = 0;
        boolean isRasaFound = false;
        int N = conveyorBelt.size();

        if (rasa < 0 || rasa > 3) {
            return -1;
        }
        
        for (Iterator<Stack<Integer>> itr = conveyorBelt.descendingIterator(); itr.hasNext(); ) {
            Stack<Integer> nextToples = itr.next();
            if (nextToples.isEmpty() == false
                && nextToples.peek() == rasa) {
                isRasaFound = true;
                break;
            } else {
                targetIndex++;
            }
        }

        if (isRasaFound == false) {
            return -1;
        }

        if (targetIndex == 0) {
            conveyorBelt.getLast().pop();
            return 0;
        }

        // If rasa exists, but have to shift k (shiftAmt) times, where 0 < k < N
        shiftAmt = N - targetIndex;

        for (int i = 0; i < shiftAmt; i++) {
            geserKanan();
        }

        if (conveyorBelt.getLast().peek() == rasa) {
            conveyorBelt.getLast().pop();
        }

        // for (int i = 0; i < shiftAmt; i++) {
        //     if (conveyorBelt.getLast().peek() == rasa) {
        //         conveyorBelt.getLast().pop();

        //         break;
        //     } else {
        //         geserKanan();
        //     }
        // }

        return targetIndex;
    }

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
               
        int N = in.nextInt();
        int X = in.nextInt();
        int C = in.nextInt();

        for (int i = 0; i < N; ++i) {
            // Inisiasi toples ke-i
            Stack<Integer> toples = new Stack<>();

            for (int j = 0; j < X; j++) {
                int rasaKeJ = in.nextInt();

                // Inisiasi kue ke-j ke dalam toples ke-i
                toples.push(rasaKeJ);
            }
            
            // Masukkan toples ke conveyor belt
            conveyorBelt.add(toples);
        }

        for (int i = 0; i < C; i++) {
            String perintah = in.next();
            if (perintah.equals("GESER_KANAN")) {
                out.println(geserKanan());
            } else if (perintah.equals("BELI_RASA")) {
                int namaRasa = in.nextInt();
                out.println(beliRasa(namaRasa));
            }
        }
        out.close();
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