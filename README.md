# Huffman-coding-and-decoding

Encode method:
We use readFile method to transform every byte of the input file into 8 digit binary and store them as the key of a
HashMap. Then we calculate the frequency of each binary key occurs. This frequency is stored as value. After readFile
method returns the HashMap, we use writeFromMap method to write this HashMap into the freqFile in the required format.
Then we establish a priority queue(every element is a Node) and let it copy everything from the frequency HashMap. And
then we build a tree according to the priority queue using Node class. By applying buildHuffmanTree method, we take two
smallest value from the priority queue and use them as the bottom of the tree. To be notice, once a Node is applied in
the tree, it is removed from the priority queue. After finish the tree, we establish a new HashMap that stores ASCII(8
digit binary) as key and huffman code(consists of 0 and 1) as value. Then we call buildHuffmanCode method to find the
huffman code for each binary value and fill them into the HashMap we just established. After this, we can call the
buildOutputFile method to write huffman code into the encode file.

Decode method:
For decoding, we first need our frequency data back. So we establish a HashMao with 8 digit binary as key and the
frequency it occurs as value, and then we call freqReader method to read the freqFile and fill in our HashMap according
to the file. Then we establish a priority queue and copy the values from the frequency HashMap. Then we call the method
buildHuffmanTree to build the tree and get its root. Similar to the encode process, we establish a HashMap that stores
8 digit binary as value and huffman code(0 and 1) as key. Then using the root of the tree, we call decodeHuffmanMap
method to fill out the value(huffman code corresponding to each binary)HashMap we just established. Finally, we call
decodeHuffmanCode method to write the original data into the decode file.
PS: The MOST IMPORTANT part is the flush() in the decodedOutputFile method which help us get the complete binary output
stream.
