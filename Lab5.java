import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.StringTokenizer;
import java.util.Stack;
import java.io.FileInputStream;

/*
 * REFERENCE
 * 1. https://www.geeksforgeeks.org/binary-search-tree-set-1-search-and-insertion/ (diakses 4 Nov 2022, 13.15)
 * 2. https://www.geeksforgeeks.org/smallest-number-in-bst-which-is-greater-than-or-equal-to-n/ (diakses 4 Nov 2022, 13.40)
 * 3. https://www.geeksforgeeks.org/largest-number-bst-less-equal-n/ (diakses 4 Nov 2022, 15.10)
 * 4. https://codereview.stackexchange.com/questions/204199/smallest-number-in-bst-which-is-greater-than-or-equal-to-n (diakses 5 Nov 2022, 11.54)
 * 5. https://github.com/eugenp/tutorials/blob/master/data-structures/src/main/java/com/baeldung/avltree/AVLTree.java (diakses 5 Nov 2022, 16.23)
 * 6. https://www.geeksforgeeks.org/inorder-tree-traversal-without-recursion/ (diakses 5 Nov 17.48)
 */

public class Lab5 {

    private static FastIO in;
    static PrintWriter out;
    static AVLTree tree = new AVLTree();

    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new FastIO(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);
        
        try {
            int numOfInitialPlayers = in.nextInt();
            for (int i = 0; i < numOfInitialPlayers; i++) {
                String playerName = in.next();
                int powerLevel = in.nextInt();
    
                tree.insert(powerLevel, playerName);
            }
    
            int numOfQueries = in.nextInt();
            for (int i = 0; i < numOfQueries; i++) {
                String cmd = in.next();
                if (cmd.equals("MASUK")) {
                    String playerName = in.next();
                    int powerLevel = in.nextInt();
    
                    handleQueryMasuk(playerName, powerLevel);
                } else {
                    int low = in.nextInt();
                    int high = in.nextInt();
    
                    handleQueryDuo(low, high);
                }
            }
            out.close();
        } catch (Exception e) {
            System.out.println("Error occured");
        }
    }

    static void handleQueryMasuk(String playerName, int powerLevel) {
        int weakerCount = tree.countWeakerPlayers(powerLevel);
        tree.insert(powerLevel, playerName);

        out.println(weakerCount);
    }

    static void handleQueryDuo(int low, int high) {
        Node lowestPowerInRange = tree.lowerBound(low);
        Node largestPowerInRange = tree.upperBound(high);

        if (lowestPowerInRange == null || largestPowerInRange == null) {
            out.println("-1 -1");

            return;
        }

        String weakestPlayer = lowestPowerInRange.removeRecentPlayer();
        lowestPowerInRange.decrementSize();

        String strongestPlayer = largestPowerInRange.removeRecentPlayer();
        largestPowerInRange.decrementSize();

        if (weakestPlayer == null || strongestPlayer == null) {
            out.println("-1 -1");


            // Re-add players to stack
            if (weakestPlayer != null) {
                lowestPowerInRange.addPlayer(weakestPlayer);
                lowestPowerInRange.incrementSize();
            }

            if (strongestPlayer != null) {
                largestPowerInRange.addPlayer(strongestPlayer);
                largestPowerInRange.incrementSize();
            }
        }
        else {
            // Print text according to question
            if (weakestPlayer.compareTo(strongestPlayer) <= 0) {
                out.println(weakestPlayer + " " + strongestPlayer);
            }
            else {
                out.println(strongestPlayer + " " + weakestPlayer);
            }
            
            // If nodes are empty after removal, remove the node
            if (lowestPowerInRange.getPlayerCount() == 0) {
                tree.delete(lowestPowerInRange.key);
            }

            if (largestPowerInRange.getPlayerCount() == 0) {
                tree.delete(largestPowerInRange.key);
            }
        }

    }

    static class FastIO {
        InputStream dis;
        byte[] buffer = new byte[1 << 17];
        int pointer = 0;
        public FastIO(String fileName) throws Exception {
            dis = new FileInputStream(fileName);
        }
        public FastIO(InputStream is) {
            dis = is;
        }
        int nextInt() throws Exception {
            int ret = 0;
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            boolean negative = false;
            if (b == '-') {
                negative = true;
                b = nextByte();
            }
            while (b >= '0' && b <= '9') {
                ret = 10 * ret + b - '0';
                b = nextByte();
            }
            return (negative) ? -ret : ret;
        }
        long nextLong() throws Exception {
            long ret = 0;
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            boolean negative = false;
            if (b == '-') {
                negative = true;
                b = nextByte();
            }
            while (b >= '0' && b <= '9') {
                ret = 10 * ret + b - '0';
                b = nextByte();
            }
            return (negative) ? -ret : ret;
        }
        Integer[] readArray(int n) throws Exception {
            Integer[] a = new Integer[n];
            for (int i = 0; i<n; i++) a[i] = nextInt();
            return a;
        }
        byte nextByte() throws Exception {
            if (pointer == buffer.length) {
                dis.read(buffer, 0, buffer.length);
                pointer = 0;
            }
            return buffer[pointer++];
        }
        String next() throws Exception {
            StringBuilder ret = new StringBuilder();
            byte b;
            do {
                b = nextByte();
            } while (b <= ' ');
            while (b > ' ') {
                ret.appendCodePoint(b);
                b = nextByte();
            }
            return ret.toString();
        }
        public void close() throws Exception {
            if (dis == null) return;
            dis.close();
        }
    }

    // taken from https://codeforces.com/submissions/Petr
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

class Node {
    public int key, height;
    public Node left, right;
    public Stack<String> players;
    public int size;

    public Node(int key) {
        this.key = key;
        this.players = new Stack<>();
        this.size = 0;
    }

    public void addPlayer(String playerName) {
        players.push(playerName);
        this.size++;
    }

    public int getSize() {
        return this.size;
    }

    public void decrementSize() {
        this.size--;
    }

    public void incrementSize() {
        this.size++;
    }

    public int getPlayerCount() {
        return this.players.size();
    }

    public String removeRecentPlayer() {
        if (this.players.size() > 0) {
            this.size--;
            return players.pop();
        }

        return null;
    }
}

class AVLTree {
    private Node root;

    public void insert(int key, String playerName) {
        root = insert(root, key, playerName);
    }

    public void delete(int key) {
        root = delete(root, key);
    }

    public Node getRoot() {
        return root;
    } 

    private int height(Node n) {
        return n == null ? -1 : n.height;
    }

    private Node insert(Node root, int key, String playerName) {
        if (root == null) {
            Node newNode = new Node(key);
            newNode.addPlayer(playerName);

            return newNode;
        }
        else if (root.key > key) {
            root.left = insert(root.left, key, playerName);
        } else if (root.key < key) {
            root.right = insert(root.right, key, playerName);
        } else {
            // If node already exists and is found, add playerName to stack
            root.addPlayer(playerName);
        }

        // The tree structure has changed, update the root height & size
        updateHeight(root);
        updateSize(root);

        return rebalance(root);
    }

    private Node delete(Node root, int key) {
        // 1. Perform standard BST delete
        if (root == null) {
            return root;
        } 
        else if (root.key > key) {
            root.left = delete(root.left, key);
        } 
        else if(root.key < key) {
            root.right = delete(root.right, key);
        }
        else {
            if (root.left == null || root.right == null) {
                // 0 or 1 child case
                root = (root.left == null) ? root.right : root.left;
            }
            else {
                // 2 child case
                Node mostLeftChild = mostLeftChild(root.right);
                root.key = mostLeftChild.key;
                root.players = mostLeftChild.players;
                root.right = delete(root.right, root.key);
            }
        }

        // If root is null, return null
        if (root == null) return root;

        // The tree structure has changed, update the root height & size
        updateHeight(root);
        updateSize(root);

        // 2. Rebalance Tree
        root = rebalance(root);

        // The tree structure has changed, update the root height & size
        updateHeight(root);
        updateSize(root);

        return root;
    }

    public Node lowerBound(int N) {
        return lowerBound(root, N);
    }

    public Node upperBound(int N) {
        return upperBound(root, N);
    }

    public int countWeakerPlayers(int minPowerLevel) {
        return countWeakerPlayers(root, minPowerLevel);
    }

    /* Private Methods*/

    private Node lowerBound(Node root, int N) {
        /* Return node with the smallest key that is >= N */
        Node smallest = null;

        while (root != null) {
            if (root.key >= N) {
                smallest = root;
                root = root.left;
            }
            else {
                root = root.right;
            }
        }

        return smallest;
    }

    private Node upperBound(Node root, int N) {
        /* Return node with the largest key that is <= N */
        Node largest = null;

        while (root != null) {
            if (root.key <= N) {
                largest = root;
                root = root.right;
            }
            else {
                root = root.left;
            }
        }

        return largest;
    }

    private int countWeakerPlayers(Node root, int minPowerLevel) {
        if (root == null) return 0; 

        if (root.key > minPowerLevel) {
            return countWeakerPlayers(root.left, minPowerLevel);
        }
        else if (root.key < minPowerLevel) {
            if (root.left != null) {
                return root.left.getSize() + root.getPlayerCount() + countWeakerPlayers(root.right, minPowerLevel);
            }

            return root.getPlayerCount() + countWeakerPlayers(root.right, minPowerLevel);
        }
        else {
            // If root.key == MPL
            if (root.left != null) {
                return root.left.getSize();
            }
            else {
                return 0;
            }
        }
    }

    /* Helper Methods */
    // Utility function to get balance factor of node
    public int getBalance(Node n) {
        return (n == null) ? 0 : height(n.right) - height(n.left);
    }

    public int getSize(Node n) {
        if (n == null) {
            return 0;
        }

        return n.getSize();
    }
    
    private Node mostLeftChild(Node root) {
        Node current = root;

        while (current.left != null) {
            current = current.left;
        }
        return current;
    }

    private void updateHeight(Node n) {
        n.height = 1 + Math.max(height(n.left), height(n.right));
    }

    private void updateSize(Node n) {
        n.size = n.getPlayerCount() + getSize(n.left) + getSize(n.right);
    }

    private Node rebalance(Node z) {
        updateHeight(z);
        int balance = getBalance(z);
        if (balance > 1) {
            if (height(z.right.right) > height(z.right.left)) {
                z = leftRotate(z);
            } else {
                z.right = rightRotate(z.right);
                z = leftRotate(z);
            }
        } else if (balance < -1) {
            if (height(z.left.left) > height(z.left.right)) {
                z = rightRotate(z);
            } else {
                z.left = leftRotate(z.left);
                z = rightRotate(z);
            }
        }
        
        return z;
    }

    // Right rotate a subtree with node as its root
    private Node rightRotate(Node y) {
        Node x = y.left;
        Node z = x.right;
        x.right = y;
        y.left = z;
        updateHeight(y);
        updateHeight(x);
        updateSize(y);
        updateSize(x);

        return x;
    }

    // Left rotate a subtree with node as its root
    private Node leftRotate(Node y) {
        Node x = y.right;
        Node z = x.left;
        x.left = y;
        y.right = z;
        updateHeight(y);
        updateHeight(x);
        updateSize(y);
        updateSize(x);

        return x;
    }
}