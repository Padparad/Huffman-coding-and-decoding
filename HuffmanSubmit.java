import java.io.*;
import java.util.HashMap;
import java.util.PriorityQueue;
import java.util.Scanner;

public class HuffmanSubmit implements Huffman {

    //Internal class which would help us construct our tree
    private static class Node implements Comparable<Node>{
        Node left;
        Node right;
        //frequency of this char
        long fr;
        //binary value of this char
        String binaryValue;
        //constructor
        public Node(String value, long frequency) {
            this.binaryValue = value;
            this.fr = frequency;
        }

        public Node(long frequency) {
            this.binaryValue = null;
            this.fr = frequency;
        }
        //override compareTo method
        public int compareTo(Node a){
            if(this.fr > a.fr){
                return 1;
            }
            else if(this.fr == a.fr){
                return 0;
            }
            return -1;
        }

        //getter
        public long getFrequency(){
            return fr;
        }

        public Node getLeft(){
            return left;
        }

        public Node getRight(){
            return right;
        }
        //return ASCII value of this node
        public String getValue(){
            return binaryValue;
        }
        //setter
        public void setLeft(Node left){
            this.left = left;
        }
        public void setRight(Node right){
            this.right = right;
        }

        //return boolean that if one has right or left
        public boolean haveLeft(){
            return left != null;
        }

        public boolean haveRight(){
            return right != null;
        }
    }

    //testing method that print the provided HashMap
    public static void printHashMap(HashMap input) {
        for (Object key: input.keySet()) {
            System.out.println(key + ":" + input.get(key));
        }
    }

    //read input file and return a hashmap with 8 digit binary as key and the frequency it occurs as value
    public static HashMap<String, Integer> readFile(String originalFile) {
        HashMap<String, Integer> frequency = new HashMap<>();
        BinaryIn in = new BinaryIn(originalFile);
        char currentChar;
        while (!in.isEmpty()) {
            currentChar = in.readChar();
            String binary = String.format("%8s", Integer.toBinaryString(currentChar)).replace(' ', '0');
            if (frequency.containsKey(binary)) {
                frequency.put(binary, frequency.get(binary) + 1);
            } else {
                frequency.put(binary, 1);
            }
        }
        return frequency;
    }

    //write into frequency file from the hashmap
    public static void writeFromMap(String freqFile, HashMap<String, Integer> frequency) {
        try {
            FileWriter myWriter = new FileWriter(freqFile);

            for (Object key : frequency.keySet()) {
                myWriter.write(key + ":" + frequency.get(key));
                myWriter.write("\n");
            }
            myWriter.close();
        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }
    }

    //using priority queue to build huffman tree which return the front node
    public Node buildHuffmanTree(PriorityQueue a){
        while(a.size() > 1){
            Node b = (Node) a.remove();
            Node c = (Node) a.remove();
            long fr1 = b.getFrequency() + c.getFrequency();
            Node newOne = new Node(fr1);
            newOne.setLeft(b);
            newOne.setRight(c);
            a.add(newOne);
        }
        return (Node) a.peek();
    }

    public void buildHuffmanCode(Node node, HashMap<String, String> huffmanCode , String string){
        if(node != null){
            if(node.haveRight()){
                buildHuffmanCode(node.getRight(), huffmanCode, string + "1");
            }
            if(node.haveLeft()){
                buildHuffmanCode(node.getLeft(), huffmanCode, string + "0");
            }
            if(node.getValue() != null){
                huffmanCode.put(node.getValue(), string);
            }
        }
    }

    //To build huffman code map in decoding process
    public void decodedHuffmanMap(Node node, HashMap<String, String> huffmanCode, String string){
        if(node != null){
            if(node.haveRight()){
                decodedHuffmanMap(node.getRight(), huffmanCode, string + "1");
            }
            if(node.haveLeft()){
                decodedHuffmanMap(node.getLeft(), huffmanCode, string + "0");
            }
            if(! node.haveRight() && ! node.haveLeft()){
                huffmanCode.put(string, node.getValue());
            }
        }
    }

    public void buildOutputFile(HashMap<String, String> huffmanMap, String inputFile, String outputFile){
        BinaryIn in = new BinaryIn(inputFile);
        BinaryOut a = new BinaryOut(outputFile);
        char currentChar;
        while (! in.isEmpty()) {
            //transform character to ASCII value
            currentChar = in.readChar();
            //Add huffman code of such ASCII value to output File
            String binary = String.format("%8s", Integer.toBinaryString(currentChar)).replace(' ', '0');
            String huffmanCode = huffmanMap.get(binary);
            //write binary value for each huffman code
            for(int i = 0; i < huffmanCode.length(); i++){
                if(huffmanCode.charAt(i) == '0')
                {
                    a.write(false);
                }
                else if(huffmanCode.charAt(i) == '1'){
                    a.write(true);
                }
            }
        }
        a.close();
    }

    public void decodedOutputFile(HashMap<String, String> huffmanMap, String inputFile, String outputFile){
        BinaryIn in = new BinaryIn(inputFile);
        String a = "";
        BinaryOut bo = new BinaryOut(outputFile);
        while(! in.isEmpty()){
            if(in.readBoolean()){
                a += "1";
            }
            else{
                a += "0";
            }
            for(String key: huffmanMap.keySet()){
                if(a.equals(key)){
                    a = "";
                    int ascii = Integer.parseInt(huffmanMap.get(key),2);
                    bo.write((char) ascii);
                }
            }
        }
        bo.flush();
    }

    //freqFile reader
    public HashMap<String, Integer> freqReader (String fileName) throws FileNotFoundException {
        HashMap<String, Integer> frequency = new HashMap<>();
        File file = new File(fileName);
        Scanner scanner = new Scanner(file);
        String line;
        String key;
        int value;
        while(scanner.hasNextLine()) {
            line = scanner.nextLine();
            key = line.substring(0, line.indexOf(":"));
            value = Integer.parseInt(line.substring(line.indexOf(":") + 1));
            frequency.put(key, value);
        }
        return frequency;
    }

    public void encode(String inputFile, String outputFile, String freqFile){
        // first build hashmap to store values and build freqFile
        HashMap<String, Integer> frequency = readFile(inputFile);
        writeFromMap(freqFile, frequency);
        //build priority to store values and frequency
        PriorityQueue<Node> huffmanPQ = new PriorityQueue<Node>();
        for(Object key: frequency.keySet()){
            Node x = new Node((String) key, frequency.get(key));
            huffmanPQ.add(x);
        }
        //then build huffman tree
        Node front = buildHuffmanTree(huffmanPQ);
        //use a map to contain ASCII and its huffman code
        HashMap<String, String> huffmanCode = new HashMap<>();
        this.buildHuffmanCode(front , huffmanCode, "");
        // using huffman code map to write compressed huffmanCode file in output file
        buildOutputFile(huffmanCode, inputFile , outputFile);
    }


    public void decode(String inputFile, String outputFile, String freqFile){
        //build huffman code hashmap using freqFile
        HashMap<String, Integer> frequencyMap = null;
        try {
            frequencyMap = freqReader(freqFile);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        //again build priority queue
        PriorityQueue<Node> huffmanPQ = new PriorityQueue<Node>();
        for(Object key: frequencyMap.keySet()){
            Node x = new Node((String)key, frequencyMap.get(key));
            huffmanPQ.add(x);
        }
        Node start = buildHuffmanTree(huffmanPQ);
        HashMap<String,String> huffmanCode = new HashMap<String,String>();
        this.decodedHuffmanMap(start, huffmanCode, "");
        //read inputFile write accordingly original text for each huffmanCode on outputFile
        decodedOutputFile(huffmanCode, inputFile, outputFile);
    }


    public static void main(String[] args) {
        Huffman huffman = new HuffmanSubmit();
        huffman.encode("ur.jpg", "encode.txt", "freqFile.txt");
        huffman.decode("encode.txt", "output.jpg", "freqFile.txt");
    }

}
