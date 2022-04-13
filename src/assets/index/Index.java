package assets.index;

/*
   B+ Tree Indexing

   This program implements the concept of B+ Tree indexing which is done in a
   database for a data file that is provided in txt format. The program will create
   an index file for the data file that contains the key and value of each record.
   The program computes the offset value of each record and creates the index
   based on the key supplied and the offset value where a record lies in the file.

   This program does 4 operations:
   Create the index
   Search for a record through index file
   Insert new record in the file
   List n records from the file from the supplied key value.

   The index file will hold the first 1k bytes as Metadata and the records are
   inserted as 1k blocks of data. The Metadata will provide the information about
   the data file and the size of the key which is followed by the key and offset value
   of each record.

   The size of the key with which the index should be created is provided as an input.
   A Serializable class of B+ Tree is created which will hold the key, offset value,
   length of the record, whether is it a leaf node or not and its parent and children
   The tree will be constructed on B+ tree concept and the constructed tree will be
   inserted into the index file.

   Search function will search for a record by entering the key value. If the key length
   entered is more than the key size present in the index file, the key will be truncated and
   if the key length is less than the indexed file key length, blank spaces will be
   appended at the end of the key. If the record is present in the file, it will display
   the line at which the record is present and also the contents of the key.

   List function will get the numbers of records to be listed and the key from which the
   program should print from as input. It searches for the record in the data file and
   prints the n records proceeding it. If a record is not available in the file, the
   pointer goes to the next record and starts printing from that location.

   Written by Adithya Ganapathy (axg172330) at The University of Texas at Dallas
   starting November 09, 2017.
 */

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.RandomAccessFile;
import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
/*
 * The B+ Tree class structure which will hold the record indexes and is inserted
 * in the index file. The structure has key, offset value, data length as list 
 * and it also holds the information for the parent, left pointer, right pointer and
 * whether the node is leaf node or not.
 */
class Tree implements Serializable {
	public List<String> key; 
	public List<Tree> ptr;
	public List<Long> offsetvalue;
	public List<Integer> dataLength; 
	public Tree parent;
	public Tree rightpointer; 
	public Tree leftpointer; 
	public boolean isLeaf;
	
	public Tree() {
		this.key = new ArrayList<>();
		this.ptr = new ArrayList<>();
		this.offsetvalue = new ArrayList<>();
		this.dataLength = new ArrayList<>();
		this.parent = null;
		this.rightpointer = null;
		this.leftpointer = null;
		this.isLeaf = false;
	}
}

@SuppressWarnings("ALL")
public class Index {

	/*
	 * static variables of the Class Tree and size of node which 
	 * will be used throughout the program.
	 */
	static Tree root;
	static int Nodesize = 0; 

	/*
	 * Insert function which inserts the record into the tree. It validates 
	 * whether a record is already present in the index file and skips it.
	 * When the nodes inserted equals the node size of the tree, the split function 
	 * is called and the tree is balanced.
	 */
	@SuppressWarnings("null")
	private static void insert(Tree node, String key,long offset, int reclength) throws IOException {
		
		if ((node == null || node.key.isEmpty()) && node == root) {
			node.key.add(key);
			node.offsetvalue.add(offset);
			node.dataLength.add(reclength);
			node.isLeaf = true;
			root = node;
		}
		else if (node != null || !Objects.requireNonNull(node).key.isEmpty()) {
			for (int i = 0; i < node.key.size(); i++) {

				if (key.compareTo(node.key.get(i)) == 0) {
					System.out.println("Duplicate Record " + key + "at line:" + offset);
					return;
				}
				else if (key.compareTo(node.key.get(i)) < 0) {
					if (!node.isLeaf && node.ptr.get(i) != null) {
						insert((Tree) node.ptr.get(i), key, offset, reclength);
						return;
					}
					else if (node.isLeaf) {
						node.key.add("");
						node.offsetvalue.add(0L);
						node.dataLength.add(0);
						for (int j = node.key.size() - 2; j >= i; j--) {
							node.key.set(j + 1, node.key.get(j));
							node.offsetvalue.set(j + 1, node.offsetvalue.get(j));
							node.dataLength.set(j + 1, node.dataLength.get(j));
						}
						node.key.set(i, key);
						node.offsetvalue.set(i, offset);
						node.dataLength.set(i, reclength);
						if (node.key.size() == Nodesize) {
							split(node);
						}
						return;
					}
				}
				else if (key.compareTo(node.key.get(i)) > 0) {
					if (i < node.key.size() - 1) {
						continue;
					}
					else if (i == node.key.size() - 1) {
						if (!node.isLeaf && node.ptr.get(i + 1) != null) {
							insert((Tree) node.ptr.get(i + 1),key, offset, reclength);
							return;
						}

						else if (node.isLeaf) {
							node.key.add("");
							node.offsetvalue.add(0l);
							node.dataLength.add(0);
							node.key.set(i + 1, key);
							node.offsetvalue.set(i + 1, offset);
							node.dataLength.set(i + 1, reclength);
						}

						if (node.key.size() == Nodesize) {
							split(node);
						}
						return;
					}
				}
			}
		}
	}

	/*
	 * This function splits the tree and balances the node in it. 
	 * When the nodes inserted exceeds the node size this function is called 
	 * After split it creates the pointer and stores the pointer of the 
	 * parent, right node and left node to it if it is an internal node and 
	 * if it is a leaf node stores the right pointer to the node. Before splitting
	 * the function sorts the key in ascending order and perform the split.
	 */
	private static void split(Tree node) throws IOException {
		Tree leftnode = new Tree();
		Tree rightnode = new Tree(); 
		Tree tempparent = new Tree(); 
		Tree parent;
		int newPosKey = 0, split = 0;
		
		if (node.isLeaf) {
			if (node.key.size() % 2 == 0)
				split = (node.key.size() / 2) - 1;
			else
				split = node.key.size() / 2;

			rightnode.isLeaf = true;
			for (int i = split; i < node.key.size(); i++) {
				rightnode.key.add(node.key.get(i));
				rightnode.offsetvalue.add(node.offsetvalue.get(i));
				rightnode.dataLength.add(node.dataLength.get(i));
			}
			
			leftnode.isLeaf = true;
			for (int i = 0; i < split; i++) {
				leftnode.key.add(node.key.get(i));
				leftnode.offsetvalue.add(node.offsetvalue.get(i));
				leftnode.dataLength.add(node.dataLength.get(i));
			}
			
			if (node.rightpointer != null)
				rightnode.rightpointer = node.rightpointer;
			else
				rightnode.rightpointer = null;
			if (node.leftpointer != null)
				leftnode.leftpointer = node.leftpointer;
			else
				leftnode.leftpointer = null;

			leftnode.rightpointer = rightnode;
			rightnode.leftpointer = leftnode;

			if (node.parent == null) {
				tempparent.isLeaf = false;
				tempparent.key.add(rightnode.key.get(0));
				tempparent.ptr.add(leftnode);
				tempparent.ptr.add(rightnode);
				leftnode.parent = tempparent;
				rightnode.parent = tempparent;
				root = tempparent;
				node = tempparent;
			}
			else if (node.parent != null) {
				parent = node.parent;				
				parent.key.add(rightnode.key.get(0));
				Collections.sort(parent.key);
				leftnode.parent = parent;
				rightnode.parent = parent;
				newPosKey = parent.key.indexOf(rightnode.key.get(0));

				if (newPosKey < parent.key.size() - 1) {
					parent.ptr.add(null);

					for (int i = parent.key.size() - 1; i > newPosKey; i--) {
						parent.ptr.set(i + 1, parent.ptr.get(i));
					}

					parent.ptr.set(newPosKey + 1, rightnode);
					parent.ptr.set(newPosKey, leftnode);
				}

				else if (newPosKey == parent.key.size() - 1) {
					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.add(rightnode);
				}
				if (node.leftpointer != null) {
					node.leftpointer.rightpointer = leftnode;
					leftnode.leftpointer = node.leftpointer;
				}
				if (node.rightpointer != null) {
					node.rightpointer.leftpointer = rightnode;
					rightnode.rightpointer = node.rightpointer;
				}
				if (parent.key.size() == Nodesize) {
					split(parent);
				}
				return;
			}
		}
		else {
			rightnode.isLeaf = false;
			if (node.key.size() % 2 == 0)
				split = (node.key.size() / 2) - 1;
			else
				split = node.key.size() / 2;

			String popKey = node.key.get(split);
			int k = 0, p = 0;
			for (int i = split + 1; i < node.key.size(); i++) {
				rightnode.key.add(node.key.get(i));
			}
			for (int i = split + 1; i < node.ptr.size(); i++) {
				rightnode.ptr.add(node.ptr.get(i));
				rightnode.ptr.get(k++).parent = rightnode;
			}
			k = 0;
			for (int i = 0; i < split; i++) {
				leftnode.key.add(node.key.get(i));
			}
			for (int i = 0; i < split + 1; i++) {
				leftnode.ptr.add(node.ptr.get(i));
				leftnode.ptr.get(p++).parent = leftnode;
			}
			p = 0;
			if (node.parent == null) {
				tempparent.isLeaf = false;
				tempparent.key.add(popKey);
				tempparent.ptr.add(leftnode);
				tempparent.ptr.add(rightnode);
				leftnode.parent = tempparent;
				rightnode.parent = tempparent;
				node = tempparent;
				root = tempparent;
				return;
			}
			else if (node.parent != null) {
				parent = node.parent;
				parent.key.add(popKey);
				Collections.sort(parent.key);
				newPosKey = parent.key.indexOf(popKey);

				if (newPosKey == parent.key.size() - 1) {
					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.add(rightnode);
					rightnode.parent = parent;
					leftnode.parent = parent;
				}
				else if (newPosKey < parent.key.size() - 1) {
					int ptrSize = parent.ptr.size();
					parent.ptr.add(null);
					for (int i = ptrSize - 1; i > newPosKey; i--) {
						parent.ptr.set(i + 1, parent.ptr.get(i));
					}

					parent.ptr.set(newPosKey, leftnode);
					parent.ptr.set(newPosKey + 1, rightnode);
					leftnode.parent = parent;
					rightnode.parent = parent;
				}
				
				if (parent.key.size() == Nodesize) {
					split(parent);
				}
				return;
			}
		}
	}

	/*
	 * This function retrieves the data file path from the index file 
	 * and calls the corresponding functions for searching a record or 
	 * listing records.
	 */
	private static void searchindex(String indexFile, String pSearchKey,String fnchoice) throws IOException, ClassNotFoundException {
		FileInputStream fin = new FileInputStream(indexFile);
		FileChannel fc = fin.getChannel();
		fc.position(1025l);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Tree newRoot = (Tree) ois.readObject();
		ois.close();
		if(fnchoice.equals(" "))
			searchData(newRoot, indexFile, pSearchKey);
		else
			ListData(newRoot, indexFile, pSearchKey, Integer.parseInt(fnchoice));
	}

	/*
	 * This function finds whether the record is present in the data file or not. 
	 * It opens the index file and obtains the offset value of the record and 
	 * calls the corresponding retrieve data function to display the record 
	 */
	private static void searchData(Tree node, String indexFile, String key) throws IOException {
		int indexfilekeylen = Integer.parseInt(getmetadata(indexFile,"key"));
		if(key.length() > indexfilekeylen) {
			key = key.substring(0, indexfilekeylen);
		}
		else if(key.length() < indexfilekeylen) {
			for(int i = key.length();i < indexfilekeylen; i++)
				key = key + " ";
		}
		for (int i = 0; i < node.key.size(); i++) {
			if (node.isLeaf) {
				int keyIndex = node.key.indexOf(key);
				if (keyIndex == -1) {
					System.out.println("Data not found");
					return;
				} else if (keyIndex != -1) { 
					long offsetvalue = node.offsetvalue.get(keyIndex);
					int dataLength = node.dataLength.get(keyIndex);
					retrieverecord(indexFile, offsetvalue, dataLength);
					return;
				}
			}
			else if (key.compareTo(node.key.get(i)) < 0) {
				if (node.ptr.get(i) != null) {
					searchData(node.ptr.get(i), indexFile, key);
					return;
				}
			}
			else if (key.compareTo(node.key.get(i)) >= 0) {
				if (i < node.key.size() - 1) {
					continue;
				}

				else if (i == node.key.size() - 1) {
					if (node.ptr.get(i + 1) != null) {
						searchData((Tree) node.ptr.get(i + 1),indexFile, key);
						return;
					}
				}
			}
		}
	}
	
	/*
	 * This function checks whether the key from which the records to be listed 
	 * is present in the data file or not. If it is not present, it goes to the 
	 * next bigger key and prints the n records from that position.
	 */
	private static void ListData(Tree node, String indexFile,String key, int listSize) throws IOException {
		int indexfilekeylen = Integer.parseInt(getmetadata(indexFile,"key"));
		if(key.length() > indexfilekeylen) {
			key = key.substring(0, indexfilekeylen);
		}
		else if(key.length() < indexfilekeylen) {
			for(int i = key.length();i < indexfilekeylen; i++)
				key = key + " ";
		}
		for (int count = 0; count < node.key.size(); count++) {
			if (node.isLeaf) {
				int keyIndex = node.key.indexOf(key);
				if (keyIndex == -1) { 
					System.out.println("Record " +key+ " not found. The next keys are: ");
					for(int i = 0; i < node.key.size();i++) {
						if(node.key.get(i).compareTo(key) > 0) {
							keyIndex = node.key.indexOf(node.key.get(i));
							long offsetvalue = node.offsetvalue.get(keyIndex);
							int dataLength = node.dataLength.get(keyIndex);
							retrieverecord(indexFile, offsetvalue, dataLength);
							int ct = 2;
							for (int j = keyIndex + 1; j < node.key.size(); j++, ct++) {
								if (ct <= listSize)
									retrieverecord(indexFile, node.offsetvalue.get(j),
											node.dataLength.get(j));
							}
							
							Tree nextLeaf = node.rightpointer;
							while (nextLeaf != null) {
								for (int j = 0; j < nextLeaf.key.size(); j++, ct++) {
									if (ct <= listSize)
										retrieverecord(indexFile,nextLeaf.offsetvalue.get(j),nextLeaf.dataLength.get(j));
								}
								nextLeaf = nextLeaf.rightpointer;
							}
							return;
						}
					}
				} 
				else if (keyIndex != -1) { 
					long offsetvalue = node.offsetvalue.get(keyIndex);
					int dataLength = node.dataLength.get(keyIndex);
					retrieverecord(indexFile, offsetvalue, dataLength);
					int ct = 2;
					for (int i = keyIndex + 1; i < node.key.size(); i++, ct++) {
						if (ct <= listSize)
							retrieverecord(indexFile, node.offsetvalue.get(i),
									node.dataLength.get(i));
					}
					
					Tree nextLeaf = node.rightpointer;
					while (nextLeaf != null) {
						for (int i = 0; i < nextLeaf.key.size(); i++, ct++) {
							if (ct <= listSize)
								retrieverecord(indexFile,nextLeaf.offsetvalue.get(i),nextLeaf.dataLength.get(i));
						}
						nextLeaf = nextLeaf.rightpointer;
					}
					return;
				}
			}

			else if (key.compareTo(node.key.get(count)) < 0) {
				if (node.ptr.get(count) != null) {
					ListData(node.ptr.get(count), indexFile, key, listSize);
					return;
				}
			}

			else if (key.compareTo(node.key.get(count)) >= 0) {
				if (count < node.key.size() - 1) {
					continue;
				}

				else if (count == node.key.size() - 1) {
					if (node.ptr.get(count + 1) != null) {
						ListData((Tree) node.ptr.get(count + 1), indexFile, key, listSize);
						return;
					}
				}
			}
		}
	}

	/*
	 * This function retrieves the data from the data file and prints it 
	 * along with the line number of the record in the data file.
	 */
	private static void retrieverecord(String indexFile, long offset, int dataLength) throws IOException {		
		String inputFileName = getmetadata(indexFile, "file");
		RandomAccessFile file = new RandomAccessFile(inputFileName, "r");
		file.seek(offset);
		byte buffer[] = new byte[dataLength + 1];
		file.read(buffer);
		String str = new String(buffer);
		str = str.replace("\n", "");
		System.out.println("At " + offset + ", record: " + str);
		file.close();
	}

	/*
	 * This function obtains the key length from the index file and 
	 * calls the function record check function for insertion.
	 */
	private static void insertNewData(String indexFile, String pData) throws IOException, ClassNotFoundException {
		int keyLength = Integer.parseInt(getmetadata(indexFile, "key"));
		String key = (String) pData.subSequence(0, keyLength);
		FileInputStream fin = new FileInputStream(indexFile);
		FileChannel fc = fin.getChannel();
		fc.position(1025l);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Tree node = (Tree) ois.readObject();
		ois.close();
		if (node != null)
			recordcheck(node, indexFile, key, pData);
	}
	
	/*
	 * This function checks if the record is already available in the file or not. 
	 * If the record is present, it displays record already exists if not calls the
	 * updateBTree function for insertion.
	 */
	private static void recordcheck(Tree node, String indexFile,String pSearchKey, String pData) throws IOException, ClassNotFoundException {
		for (int i = 0; i < node.key.size(); i++) {
			if (node.isLeaf) {
				int keyIndex = node.key.indexOf(pSearchKey);
				if (keyIndex == -1) {
					String inputFileName = getmetadata(indexFile, "file");
					int fileOffset = updateInputFile(inputFileName, pData);
					updateBTree(inputFileName, indexFile, pSearchKey,fileOffset, pData.length() + 1,getmetadata(indexFile, "key"));
					return;
				} else if (keyIndex != -1) {
					System.out.println("Record Already exists...");
					return;
				}
			}

			else if (pSearchKey.compareTo(node.key.get(i)) < 0) {
				if (node.ptr.get(i) != null) {
					recordcheck(node.ptr.get(i), indexFile,pSearchKey, pData);
					return;
				}
			}

			else if (pSearchKey.compareTo(node.key.get(i)) >= 0) {
				if (i < node.key.size() - 1) {
					continue;
				}

				else if (i == node.key.size() - 1) {
					if (node.ptr.get(i + 1) != null) {
						recordcheck((Tree) node.ptr.get(i + 1),indexFile, pSearchKey, pData);
						return;
					}
				}
			}
		}
	}

	/*
	 * This function calls the insert function to insert the new node into the 
	 * Tree and calls the write function to write the update tree in the form of 
	 * blocks in the data file.
	 */
	private static void updateBTree(String inputFile, String indexFile,String key, int fileOffset, int length, String pkeyLength) throws IOException, ClassNotFoundException {

		FileInputStream fin = new FileInputStream(indexFile);
		FileChannel fc = fin.getChannel();
		fc.position(1025l);
		ObjectInputStream ois = new ObjectInputStream(fin);
		Tree newRoot = (Tree) ois.readObject();
		ois.close();
		root = newRoot;
		insert(newRoot, key, fileOffset, length);
		writefile(pkeyLength, inputFile, indexFile);
	}

	/*
	 * This function writes the Tree into the index file in the form of bytes. 
	 * The first 1k block of data is allocated for the meta data and the next
	 * following 1k blocks contains the key,offset values and data length of 
	 * the record from the file.
	 */
	private static void writefile(String key, String datafilepath, String indexfilepath) throws IOException {
		FileOutputStream fout = new FileOutputStream(indexfilepath);
		byte[] inputFileName = datafilepath.getBytes();
		byte[] keyLength = key.getBytes();
		byte[] rootOffset = (" " + root.key.get(0)).getBytes();
		FileChannel fc = fout.getChannel();
		fc.write(ByteBuffer.wrap(inputFileName));
		fc.write(ByteBuffer.wrap(keyLength), 257l);
		fc.write(ByteBuffer.wrap(rootOffset), 260l);
		fc.position(1025l);
		ObjectOutputStream oos = new ObjectOutputStream(fout);
		oos.writeObject(root);
		oos.close();
	}
	
	/*
	 * This function inserts the new record in the data file at the end of the file.
	 * It obtains the length of the file and seeks the file pointer position 
	 * to the end of the file and inserts it.
	 */
	private static int updateInputFile(String inputFile, String pData) throws IOException {
		File inFile = new File(inputFile);
		int offset = 0;
		if (inFile.exists())
			offset = (int) inFile.length();
		RandomAccessFile file = null;
		file = new RandomAccessFile(inputFile, "rw");
		file.seek(offset);
		file.writeBytes("\r\n");
		file.writeBytes(pData);
		System.out.println("Record inserted successfully...");
		file.close();
		return offset;
	}
	
	/*
	 * This function determines the size of the node by the formulae
	 *     (Block Size - key length)/(key length + Block Pointer)
	 * It calls the insert function for the index file to be created.Once the index 
	 * file is created it calls the write file function to insert into the file path.
	 */
	private static void index(String key, String datafilepath,String indexfilepath) throws IOException {
		
		int keyLength = Integer.parseInt(key);
		Nodesize = (1024 - keyLength) / keyLength + 8 ;
		int offset = 0;	
		String s;
		BufferedReader br = new BufferedReader(new FileReader(datafilepath));
		while ((s = br.readLine()) != null) {
			insert(root,(String) s.subSequence(0, keyLength), offset, s.length());
			offset += s.length() + 2;
		}
		br.close();
		writefile(key, datafilepath, indexfilepath);
	}

	/*
	 * This function retrieves the data file name and the key value from the 
	 * index file.If the command is file it returns data file name and if the 
	 * command is key it returns key length.
	 */
	private static String getmetadata(String indexpath, String command) throws IOException {

		if(command == "file") {
			RandomAccessFile file = new RandomAccessFile(indexpath, "r");
			byte[] inputFileByte = new byte[256];
			file.read(inputFileByte);
			String inputFileName = new String(inputFileByte);
			file.close();
			return inputFileName.trim();
		}
		else {
			RandomAccessFile file = new RandomAccessFile(indexpath, "r");
			byte[] key = new byte[3];
			file.seek(257L);
			file.read(key);
			String keyLength = new String(key);
			file.close();
			return keyLength.trim();
		}
	}

	/*
	 * The main class which gets input in the form of arguments 
	 * and calls the corresponding functions based on the input.
	 */
	public static void main(String[] args) throws IOException, ClassNotFoundException {
		
		root = new Tree();
		if (args[0].equalsIgnoreCase("-create")) {
			index(args[3], args[1], args[2]);
			System.out.println("Index created successfully");
		}

		else if (args[0].equalsIgnoreCase("-find")) {
			searchindex(args[1], args[2], " ");
		}

		else if (args[0].equalsIgnoreCase("-insert")) {
			insertNewData(args[1], args[2]);
		}

		else if (args[0].equalsIgnoreCase("-list")) {
			searchindex(args[1], args[2], args[3]);
		}
	}
}