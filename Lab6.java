import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.io.FileWriter;
import java.io.FileInputStream;
import java.util.InputMismatchException;
import java.util.*;

/* REFERENCE
 * 1. https://www.geeksforgeeks.org/max-heap-in-java/ (18 Nov, 10.50)
 * 2. https://www.geeksforgeeks.org/heap-sort/ (18 Nov, 11.21)
 * 3. https://stackoverflow.com/questions/164163/quicksort-choosing-the-pivot (18 Nov, 19.10)
 * 4. https://codepad.co/snippet/median-of-three-numbers (18 Nov, 19.22)
 * 5. https://www.geeksforgeeks.org/min-heap-in-java/ (18 Nov, 19.50)
 * 6. https://www.baeldung.com/java-stream-integers-median-using-heap (18 Nov, 20.20)
 */

public class Lab6 {

    private static FastIO io = new FastIO();
    private static MedianHeap heap;
    private static ArrayList<Saham> priceArray;

    public static void main(String[] args) {
        int N = io.nextInt();

        // Initialize heap and priceArray[0] <- seri as it's index
        heap = new MedianHeap();
        priceArray = new ArrayList<>();
        priceArray.add(null);

        for (int i = 1; i <= N; i++) {
            int seri = i;
            int harga = io.nextInt();
            Saham saham = new Saham(seri, harga);

            heap.insert(saham);            
            priceArray.add(saham);
        }

        int Q = io.nextInt();

        for (int i = 0; i < Q; i++) {
            String q = io.next();

            if (q.equals("TAMBAH")) {
                int harga = io.nextInt();

                TAMBAH(harga);
            } else if (q.equals("UBAH")) {
                int seri = io.nextInt();
                int newHarga = io.nextInt();

                UBAH(seri, newHarga); 
            }
        }

        io.close();
    }

    private static void TAMBAH(int harga) {
        Saham saham = new Saham(heap.getSize() + 1, harga);

        heap.insert(saham);
        priceArray.add(saham);

        int seriOptimal = heap.getOptimal().getSeri();

        io.println(seriOptimal);
    }

    private static void UBAH(int seri, int newHarga) {
        Saham target;
        boolean inLowerHalf = true;

        // Determine if it's in minHeap or maxHeap, by looking at seriHarga
        target = priceArray.get(seri);
        int targetHarga = target.getHarga();

        Saham maxHeapRoot = heap.getMaxHeap().peek();

        if (targetHarga == maxHeapRoot.getHarga()) {
            if (seri > maxHeapRoot.getSeri()) {
                // Look at minHeap (it's greater than this root) 
                inLowerHalf = false;
            }
            else {
                inLowerHalf = true;
            }
        }
        else if (targetHarga > maxHeapRoot.getHarga()) {
            // if seriHarga[seri] > maxHeap.peek(), then it's in minHeap
            inLowerHalf = false;
        }
        else {
            // Else it's in maxHeap
            inLowerHalf = true;
        }

        // Get target from appropriate heap
        if (inLowerHalf) {
            target = heap.getMaxHeap().find(seri);
        }
        else {
            target = heap.getMinHeap().find(seri);
        }

        // Change harga value of target
        target.setHarga(newHarga);

        // Check if target belongs in another heap
        Saham maxRoot = heap.getMaxHeap().peek();
        Saham minRoot = heap.getMinHeap().peek();

        if (minRoot == null) {
            io.println(0);
            return;
        }

        if (maxRoot == null) {
            // Guaranteed to be in minHeap
            // Trigger heapify by deleting + readding item
            Saham toAddBack;
            toAddBack = heap.getMinHeap().poll();
            heap.getMinHeap().insert(toAddBack);

            int seriOptimal = heap.getOptimal().getSeri();

            io.println(seriOptimal);

            return;
        }

        if (target.getHarga() == maxRoot.getHarga()) {
            if (target.getSeri() > maxRoot.getHarga()) {
                // Move to minHeap
                Saham tmp = maxRoot;
                ArrayList<Saham> toAddBack = new ArrayList<>();

                while (tmp != target) {
                    toAddBack.add(heap.getMaxHeap().poll());
                    tmp = heap.getMaxHeap().peek();
                }
                
                // tmp = target
                Saham targetToBeMoved = heap.getMaxHeap().poll();

                // Move target to other heap
                heap.getMinHeap().insert(targetToBeMoved);

                // Reinsert previous items to heap
                for (Saham s : toAddBack) {
                    heap.getMaxHeap().insert(s);
                }
            }
            else {
                // Doesn't move heap
                Saham toAdd = heap.getMaxHeap().poll();
                heap.getMaxHeap().insert(toAdd);
            }
        }
        else if (target.getHarga() == minRoot.getHarga()) {
            if (target.getSeri() < minRoot.getHarga()) {
                // Move to maxHeap
                Saham tmp = minRoot;
                ArrayList<Saham> toAddBack = new ArrayList<>();

                while (tmp != target) {
                    toAddBack.add(heap.getMinHeap().poll());
                    tmp = heap.getMinHeap().peek();
                }
                
                // tmp = target
                Saham targetToBeMoved = heap.getMinHeap().poll();

                // Move target to other heap
                heap.getMaxHeap().insert(targetToBeMoved);

                // Reinsert previous items to heap
                for (Saham s : toAddBack) {
                    heap.getMinHeap().insert(s);
                }
            }
            else {
                // Doesn't move heap
                Saham toAdd = heap.getMinHeap().poll();
                heap.getMinHeap().insert(toAdd);
            }
        }
        else if (target.getHarga() > maxRoot.getHarga()) {
            // Move to minHeap
            Saham tmp = maxRoot;
            ArrayList<Saham> toAddBack = new ArrayList<>();

            while (tmp != target) {
                toAddBack.add(heap.getMaxHeap().poll());
                tmp = heap.getMaxHeap().peek();
            }
            
            // tmp = target
            Saham targetToBeMoved = heap.getMaxHeap().poll();

            // Move target to other heap
            heap.getMinHeap().insert(targetToBeMoved);

            // Reinsert previous items to heap
            for (Saham s : toAddBack) {
                heap.getMaxHeap().insert(s);
            }
            
        }
        else if (target.getHarga() < minRoot.getHarga()) {
            // Move to maxHeap
            Saham tmp = minRoot;
            ArrayList<Saham> toAddBack = new ArrayList<>();

            while (tmp != target) {
                toAddBack.add(heap.getMinHeap().poll());
                tmp = heap.getMinHeap().peek();
            }
            
            // tmp = target
            Saham targetToBeMoved = heap.getMinHeap().poll();

            // Move target to other heap
            heap.getMaxHeap().insert(targetToBeMoved);

            // Reinsert previous items to heap
            for (Saham s : toAddBack) {
                heap.getMinHeap().insert(s);
            }

        }
        else {
            // Stay in the current heap
            // Trigger heapify by deleting + readding item
            Saham toAddBack;

            if (inLowerHalf) { // MaxHeap
                toAddBack = heap.getMaxHeap().poll();
                heap.getMaxHeap().insert(toAddBack);
            }
            else {
                toAddBack = heap.getMinHeap().poll();
                heap.getMinHeap().insert(toAddBack);
            }
        }

        int seriOptimal = heap.getOptimal().getSeri();

        io.println(seriOptimal);
    }

    /* Taken from https://usaco.guide/general/fast-io?lang=java */
    static class FastIO extends PrintWriter {
        private InputStream stream;
        private byte[] buf = new byte[1 << 16];
        private int curChar;
        private int numChars;
    
        // standard input
        public FastIO() { this(System.in, System.out); }
    
        public FastIO(InputStream i, OutputStream o) {
            super(o);
            stream = i;
        }
    
        // file input
        public FastIO(String i, String o) throws IOException {
            super(new FileWriter(o));
            stream = new FileInputStream(i);
        }
    
        // throws InputMismatchException() if previously detected end of file
        private int nextByte() {
            if (numChars == -1) {
                throw new InputMismatchException();
            }
            if (curChar >= numChars) {
                curChar = 0;
                try {
                    numChars = stream.read(buf);
                } catch (IOException e) {
                    throw new InputMismatchException();
                }
                if (numChars == -1) {
                    return -1;  // end of file
                }
            }
            return buf[curChar++];
        }
    
        // to read in entire lines, replace c <= ' '
        // with a function that checks whether c is a line break
        public String next() {
            int c;
            do {
                c = nextByte();
            } while (c <= ' ');
    
            StringBuilder res = new StringBuilder();
            do {
                res.appendCodePoint(c);
                c = nextByte();
            } while (c > ' ');
            return res.toString();
        }
    
        public int nextInt() {  // nextLong() would be implemented similarly
            int c;
            do {
                c = nextByte();
            } while (c <= ' ');
    
            int sgn = 1;
            if (c == '-') {
                sgn = -1;
                c = nextByte();
            }
    
            int res = 0;
            do {
                if (c < '0' || c > '9') {
                    throw new InputMismatchException();
                }
                res = 10 * res + c - '0';
                c = nextByte();
            } while (c > ' ');
            return res * sgn;
        }
    
        public double nextDouble() { return Double.parseDouble(next()); }
    }
}

class Saham implements Comparable<Saham> {
    private int seri;
    private int harga;

    public Saham(int seri, int harga) {
        this.seri = seri;
        this.harga = harga;
    }

    @Override
    public int compareTo(Saham other) {
        if (other == null) return -1;

        if (this.harga == other.getHarga()) {
            if (this.seri == other.getSeri()) return 0;
            else if (this.seri < other.getSeri()) return -1;
            else return 1;
        }
        else if (this.harga < other.getHarga()) {
            return -1;
        }
        else {
            return 1;
        }
    }

    public int getSeri() {
        return seri;
    }

    public int getHarga() {
        return harga;
    }

    public void setSeri(int seri) {
        this.seri = seri;
    }

    public void setHarga(int harga) {
        this.harga = harga;
    }
}

class MedianHeap {
    private MaxHeap maxHeap;
    private MinHeap minHeap;
    private int size;

    public MedianHeap() {
        maxHeap = new MaxHeap();
        minHeap = new MinHeap();
    }

    public void insert(Saham saham) {
        if (maxHeap.getSize() == minHeap.getSize()) {
            maxHeap.insert(saham);
            minHeap.insert(maxHeap.poll());
        }
        else {
            minHeap.insert(saham);
            maxHeap.insert(minHeap.poll());
        }

        size++;
    }

    public Saham getOptimal() {
        Saham optimal = minHeap.peek();

        return optimal;
    }

    public int getSize() {
        return size;
    }

    public MaxHeap getMaxHeap() {
        return maxHeap;
    }

    public MinHeap getMinHeap() {
        return minHeap;
    }

}

class MaxHeap {
    private int size;
    private ArrayList<Saham> heap;

    public MaxHeap() {
        size = 0;
        heap = new ArrayList<>();
    }

    private void swap(int idx1, int idx2) {
        Saham element = heap.get(idx1);
        heap.set(idx1, heap.get(idx2));
        heap.set(idx2, element);
    }

    public void insert(Saham saham) {
        heap.add(saham);
        
        size++;
        
        heapifyUp();
    }

    public Saham peek() {
        if (size == 0) return null;
        return heap.get(0);
    }

    public Saham poll() {
        if (size == 0) return null;

        Saham largest = heap.get(0);

        heap.set(0, heap.get(size - 1));

        heap.remove(size - 1);
        
        // Update size before heapify!
        size--;

        heapifyDown();

        return largest;
    }

    public Saham find(int seri) {
        for (Saham s : heap) {
            if (s.getSeri() == seri) {
                return s;
            }
        }

        return null;
    }

    private void heapifyUp() {
        int idx = size - 1;

        while (hasParent(idx) && parent(idx).compareTo(heap.get(idx)) < 0) {
            swap(getParentIndex(idx), idx);
            idx = getParentIndex(idx);
        }
    }

    private void heapifyDown() {
        int idx = 0;

        while (hasLeftChild(idx)) {
            int largestChildIdx = getLeftChildIndex(idx);

            if (hasRightChild(idx) && rightChild(idx).compareTo(leftChild(idx)) > 0) {
                largestChildIdx = getRightChildIndex(idx);
            }

            if (heap.get(idx).compareTo(heap.get(largestChildIdx)) < 0) {
                swap(idx, largestChildIdx);
            }
            else {
                break;
            }

            idx = largestChildIdx;
        }
    }

    // Returning position of parent
    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    // Returning left child
    private int getLeftChildIndex(int parentIndex) {
        return (2 * parentIndex) + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return (2 * parentIndex) + 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
      }
    
    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private Saham leftChild(int parentIndex) {
        return heap.get(getLeftChildIndex(parentIndex));
    }
    
    private Saham rightChild(int parentIndex) {
        return heap.get(getRightChildIndex(parentIndex));
    }
    
    private Saham parent(int childIndex) {
        return heap.get(getParentIndex(childIndex));
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Saham> getHeap() {
        return heap;
    }
}

class MinHeap {
    private int size;
    private ArrayList<Saham> heap;

    public MinHeap() {
        size = 0;
        heap = new ArrayList<>();
    }

    private void swap(int idx1, int idx2) {
        Saham element = heap.get(idx1);
        heap.set(idx1, heap.get(idx2));
        heap.set(idx2, element);
    }

    public void insert(Saham saham) {
        heap.add(saham);
        
        size++;
        
        heapifyUp();
    }

    public Saham peek() {
        if (size == 0) return null;
        return heap.get(0);
    }

    public Saham poll() {
        if (size == 0) return null;

        Saham smallest = heap.get(0);

        heap.set(0, heap.get(size - 1));

        heap.remove(size - 1);
        
        // Update size before heapify
        size--;

        heapifyDown();

        return smallest;
    }

    public Saham find(int seri) {
        for (Saham s : heap) {
            if (s.getSeri() == seri) {
                return s;
            }
        }

        return null;
    }

    private void heapifyUp() {
        int idx = size - 1;

        while (hasParent(idx) && parent(idx).compareTo(heap.get(idx)) > 0) {
            swap(getParentIndex(idx), idx);
            idx = getParentIndex(idx);
        }
    }

    private void heapifyDown() {
        int idx = 0;

        while (hasLeftChild(idx)) {
            int smallestChildIdx = getLeftChildIndex(idx);

            if (hasRightChild(idx) && rightChild(idx).compareTo(leftChild(idx)) < 0) {
                smallestChildIdx = getRightChildIndex(idx);
            }

            if (heap.get(idx).compareTo(heap.get(smallestChildIdx)) > 0) {
                swap(idx, smallestChildIdx);
            }
            else {
                break;
            }

            idx = smallestChildIdx;
        }
    }

    // Returning position of parent
    private int getParentIndex(int childIndex) {
        return (childIndex - 1) / 2;
    }

    // Returning left child
    private int getLeftChildIndex(int parentIndex) {
        return (2 * parentIndex) + 1;
    }

    private int getRightChildIndex(int parentIndex) {
        return (2 * parentIndex) + 2;
    }

    private boolean hasLeftChild(int index) {
        return getLeftChildIndex(index) < size;
      }
    
    private boolean hasRightChild(int index) {
        return getRightChildIndex(index) < size;
    }

    private boolean hasParent(int index) {
        return getParentIndex(index) >= 0;
    }

    private Saham leftChild(int parentIndex) {
        return heap.get(getLeftChildIndex(parentIndex));
    }
    
    private Saham rightChild(int parentIndex) {
        return heap.get(getRightChildIndex(parentIndex));
    }
    
    private Saham parent(int childIndex) {
        return heap.get(getParentIndex(childIndex));
    }

    public int getSize() {
        return size;
    }

    public ArrayList<Saham> getHeap() {
        return heap;
    }
}

