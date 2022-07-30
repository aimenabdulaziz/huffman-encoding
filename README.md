# Huffman-Encoding

A program that uses Huffman encoding to compress and decompress text files. This project brings together trees, maps, priority queues, and file i/o, all to help save the bits!

File compression compacts the contents of a file (or bunch of files, as when you turn in a zip file of this homework) to save disk space and transfer time. Some compression schemes (like jpeg for images) are lossy, in that they throw away information. Other compression schemes (like zip) are lossless — you get back exactly what you put in, but it's just stored in a more compact manner.

One of the earliest schemes for lossless file compression was invented by Huffman (as a term project!). Instead of using 7 bits to encode each character, as ASCII does, it uses a variable-length encoding of characters. Frequently occurring characters get shorter code words than infrequently occurring ones. (A code word is the sequence of 0's and 1's used to encode the character.)

Huffman encoding gives the smallest possible fixed encoding of a file. A fixed encoding means that a given letter is represented by the same code wherever it appears in the file. Some more recent schemes that do better than Huffman's are adaptive, which means that how a letter is encoded changes as the file is processed.

## Testing

The program handles a number of edge cases such as an empty file, a file with a single character, and a file with single character repeated a bunch of times. It has been tested on txt files containing the aforementioned test cases and bigger files (such as the U.S. Constitution and War And Peace).

## Usage
1. Open Huffman.java
2. To compress or decompress a txt file, pass the directory of the file as a parameter to the compressor or decompressor method of Huffman class accordingly:
`compress("directory")` or `decompress("directory")`.

Please follow the sample compression and decompression commands in the main method of the Huffman class. 

This project was done as an assignment for Dartmouth’s Computer Science course. If you are a professor teaching this course and would like me to make the repository private, please reach out to me [here](mailto:aimen.a.abdulaziz.25@dartmouth.edu). Thanks!
