# BPlus-Tree-Indexing
Implementation of B+ Tree Indexing of a file using memory blocks

This program performs the following operations:
1. Create an Index: Creates an index of a data file using 1k memory blocks of data based on the key size provided as input

2. Insert a record: Inserts the record in the data file and updates the index correspondingly.

3. Find a record: Looks for the record's position in the file from the index and retreives it.

4. List n records: Looks for the record's position in the file and lists n records from that record position. If the record is not found,      it finds the next record position and lists from that position.

Compiling the program: javac index.java

Sample Scripts for running the program:

Creation: index -create datafile indexfilename keysize
  
Insertion: index -insert indexfilename "key and data to be inserted"
  
Search: index -find indexfilename key
  
List: index -list indexfilename key numberofrecords
