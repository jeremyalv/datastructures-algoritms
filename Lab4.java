import java.io.IOException;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.*;

public class Lab4 {
    private static InputReader in;
    static PrintWriter out;
    private static Komplek komplek;
    private static Denji denji;
    private static Iblis iblis;

    // TODO RUN LAB4
    public static void main(String[] args) {
        InputStream inputStream = System.in;
        in = new InputReader(inputStream);
        OutputStream outputStream = System.out;
        out = new PrintWriter(outputStream);

        // Create a new Komplek instance
        komplek = new Komplek();

        int jumlahGedung = in.nextInt();
        for (int i = 0; i < jumlahGedung; i++) {
            String namaGedung = in.next();
            int jumlahLantai = in.nextInt();

            // Create new Gedung instance
            Gedung gedung = new Gedung(namaGedung, jumlahLantai);
            
            // Add gedung instance to Komplek
            komplek.addGedung(gedung);
        }

        String gedungDenji = in.next();
        int lantaiDenji = in.nextInt();
        
        // Create Denji instance
        denji = new Denji(komplek.getGedung(gedungDenji), lantaiDenji);


        String gedungIblis = in.next();
        int lantaiIblis = in.nextInt();
        
        // Create Iblis instance
        iblis = new Iblis(komplek.getGedung(gedungIblis), lantaiIblis);

        int Q = in.nextInt();

        for (int i = 0; i < Q; i++) {
            String command = in.next();

            if (command.equals("GERAK")) {
                gerak();
            } else if (command.equals("HANCUR")) {
                hancur();
            } else if (command.equals("TAMBAH")) {
                tambah();
            } else if (command.equals("PINDAH")) {
                pindah();
            }
        }

        out.close();
    }

    static void gerak() {
        // Move denji
        denji.gerak();

        // Check whether denji meets iblis
        if (denji.isMeetIblis(iblis)) {
            denji.meetsIblis();
        }

        // Move iblis
        iblis.gerak();
        iblis.gerak();

        // Check whether denji meets iblis
        if (denji.isMeetIblis(iblis)) {
            denji.meetsIblis();
        }

        // Prints output
        out.print(denji.getGedung().getName() + " ");
        out.print(denji.getCurrentLantai() + " ");
        out.print(iblis.getGedung().getName() + " ");
        out.print(iblis.getCurrentLantai() + " ");
        out.print(denji.getMeetCount());
        out.println();
    }

    static void hancur() {
        Gedung gedungDenji = denji.getGedung();
        int lantaiDenji = denji.getCurrentLantai();
        Gedung gedungIblis = iblis.getGedung();
        int lantaiIblis = iblis.getCurrentLantai();
        int destroyedFloor;

        if (lantaiDenji == 1) {
            out.println(gedungDenji.getName() + " " + "-1");
        } else if (gedungDenji == gedungIblis && lantaiDenji == lantaiIblis + 1) {
            out.println(gedungDenji.getName() + " " + "-1");
        } else {
            destroyedFloor = lantaiDenji - 1;

            // Destroy 1 floor below denji
            gedungDenji.removeFloor();
            
            // Denji's floor will be decremented by 1
            denji.setLantai(lantaiDenji - 1);

            // If iblis is at or above iblis' floor, decrement lantai iblis as well
            if (gedungDenji == gedungIblis && lantaiIblis >= lantaiDenji) {
                iblis.setLantai(lantaiIblis - 1);
            }

            // Prints output
            out.println(gedungDenji.getName() + " " + destroyedFloor);
        }
    }

    static void tambah() {
        Gedung gedungIblis = iblis.getGedung();
        int prevLantaiIblis = iblis.getCurrentLantai();
        int addedFloor = 0;

        // Add floor to gedung iblis
        gedungIblis.addFloor();

        // Increment lantaiIblis by 1
        iblis.setLantai(prevLantaiIblis + 1);

        // If denji is at or above lantaiIblis, increment lantaiDenji
        if (denji.getGedung() == gedungIblis && denji.getCurrentLantai() >= prevLantaiIblis) {
            denji.setLantai(denji.getCurrentLantai() + 1);
        }
        
        // Store value of added Floor to the previous lantaiIblis
        addedFloor = prevLantaiIblis;
        

        // On command 'tambah', denji can never meet iblis

        // Prints output
        out.println(gedungIblis.getName() + " " + addedFloor);
    }

    static void pindah() {
        int newLantai = 0;
        // Move denji to next gedung
        denji.setGedung(denji.getGedung().getNext());

        // Determine denji's currentLantai based on the direction
        if (denji.getIsUpward()) {
            newLantai = 1;
        } else {
            newLantai = denji.getGedung().getSize();
            
        }
        
        // Modify denji's lantai
        denji.setLantai(newLantai);
        
        // Check whether denji meets iblis
        if (denji.isMeetIblis(iblis)) {
            denji.meetsIblis();
        }

        out.println(denji.getGedung().getName() + " " + newLantai);
    }

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

// Custom implementation of a Circular Linked List
class Komplek {
    private Gedung head = null;
    private Gedung tail = null;

    public Komplek() {

    }

    public void addGedung(Gedung gedung) {
        if (head == null) {
            head = gedung;
        } else {
            tail.next = gedung;
        }

        tail = gedung;
        tail.next = head;
    }

    public Gedung getGedung(String searchedGedung) {
        Gedung currentGedung = head;

        if (head == null) {
            return null;
        } else {
            do {
                if (currentGedung.name.equals(searchedGedung)) {
                    return currentGedung;
                }

                currentGedung = currentGedung.next;
            } while (currentGedung != head);

            return null;
        }
    }
}

// Custom implementation of a ListNode
class Gedung {
    String name;
    int size;
    Gedung next;

    public Gedung() {

    }

    public Gedung(String name, int size) {
        this.name = name;
        this.size = size;
        this.next = null;
    }

    public String getName() {
        return this.name;
    }

    public int getSize() {
        return this.size;
    }

    public Gedung getNext() {
        return this.next;
    }

    public void addFloor() {
        this.size++;
    }

    public void removeFloor() {
        this.size--;
    }
}


class Karakter {
    Gedung gedung;
    int currentLantai;
    boolean isUpward;

    public Karakter() {

    }

    public Karakter(Gedung gedung, int currentLantai, 
                    boolean isUpward) {
        this.gedung = gedung;
        this.currentLantai = currentLantai;
        this.isUpward = isUpward;
    }

    public Gedung getGedung() {
        return this.gedung;
    }

    public int getCurrentLantai() {
        return this.currentLantai;
    }

    public boolean getIsUpward() {
        return this.isUpward;
    }
    
    public void setGedung(Gedung newGedung) {
        this.gedung = newGedung;
    }
    
    public void setLantai(int newCurrentLantai) {
        this.currentLantai = newCurrentLantai;
    }

    public void goUp() {
        this.currentLantai++;
    }
    
    public void goDown() {
        this.currentLantai--;
    }

    public void changeDirection() {
        this.isUpward = !this.isUpward;
    }

    public void gerak() {
        // Move karakter
        if (isUpward) {
            if (currentLantai + 1 > this.gedung.getSize()) {
                this.gedung = this.gedung.getNext();
                this.currentLantai = this.gedung.getSize();
                changeDirection();
            } else {
                goUp();
            }
        } else {
            if (currentLantai - 1 < 1) {
                this.gedung = this.gedung.getNext();
                this.currentLantai = 1;
                changeDirection();
            } else {
                goDown();
            }
        }
    }
}

class Denji extends Karakter {
    int meetCount;

    public Denji() {

    }

    public Denji(Gedung gedung, int currentLantai) {
        super(gedung, currentLantai, true);
        this.meetCount = 0;
    }

    public boolean isMeetIblis(Karakter iblis) {
        if (this.gedung == iblis.gedung) {
            if (this.currentLantai == iblis.currentLantai) {
                return true;
            }
        }

        return false;
    }

    public void meetsIblis() {
        this.meetCount++;
    }

    public int getMeetCount() {
        return this.meetCount;
    }
}

class Iblis extends Karakter {
    public Iblis() {

    }

    public Iblis(Gedung gedung, int currentLantai) {
        super(gedung, currentLantai, false);
    }
}

