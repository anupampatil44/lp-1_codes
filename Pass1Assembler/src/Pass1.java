/*
 * Problem Statement :-
 * 	Design suitable data structures and implement pass-I of a two-pass assembler for 
 * 	Pseudo-machine in Java.
 * 	Implementation should consists of a few instructions from each category and few
 * 	assembler directives.
*/


import java.io.*;
import java.util.*;
import java.io.BufferedReader;

public class Pass1 {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	public static void main(String[] args) {

		try {
			BufferedReader BR = null;
			FileReader FR = null;

			FileWriter FW = null;
			BufferedWriter BW = null;

			String inputfilename = "input.txt";           // input file
			FR = new FileReader(inputfilename);
			BR = new BufferedReader(FR);

			String OUTPUTFILENAME = "IC.txt";             // ic file
			FW = new FileWriter(OUTPUTFILENAME);
			BW = new BufferedWriter(FW);

			// imperative statements and instruction codes
			Hashtable<String, String> is = new Hashtable<String, String>();
			is.put("STOP", "00");
			is.put("ADD", "01");
			is.put("SUB", "02");
			is.put("MULT", "03");
			is.put("MOVER", "04");
			is.put("MOVEM", "05");
			is.put("COMP", "06");
			is.put("BC", "07");
			is.put("DIV", "08");
			is.put("READ", "09");
			is.put("PRINT", "10");

			// assembler directives and instruction codes
			Hashtable<String, String> ad = new Hashtable<String, String>();
			ad.put("START", "01");
			ad.put("END", "02");
			ad.put("ORIGIN", "03");
			ad.put("EQU", "04");
			ad.put("LTORG", "05");

			// declarative statements and instruction codes
			Hashtable<String, String> ds = new Hashtable<String, String>();
			ds.put("DC", "01");
			ds.put("DS", "02");

			// Registers and instruction codes
			Hashtable<String, String> reg = new Hashtable<String, String>();
			reg.put("AREG", "1");
			reg.put("BREG", "2");
			reg.put("CREG", "3");
			reg.put("DREG", "4");

			// conditions and instruction codes
			Hashtable<String, String> cc = new Hashtable<String, String>();
			cc.put("LT", "1");
			cc.put("LE", "2");
			cc.put("EQ", "3");
			cc.put("GT", "4");
			cc.put("GE", "5");
			cc.put("ANY", "6");

			Hashtable<String, String> symtable = new Hashtable<String, String>();
			Hashtable<String, String> littable = new Hashtable<String, String>();
			ArrayList<Integer> pooltable = new ArrayList<Integer>();

			String Line;                    // used for reading current line
			int LC = 0;                     // for location counter
			int lTLine = 1;                 // for pointing literal table
			int sTLine = 1;                 // for pointing symbol table
			int pTLine = 1;                 // for pointing pool table

			// reading line by line
			Line = BR.readLine();

			String s1 = Line.split(" ")[1];
			if (s1.equals("START")) {
				BW.write("(AD,01)\t");
				String s2 = Line.split(" ")[2];
				BW.write("(C," + s2 + ")\n");
				LC = Integer.parseInt(s2);
			}

			while ((Line = BR.readLine()) != null) {

				int loc_assi = 0;     // checks whether address is assigned to current symbol
				int lc1 = 1;         // for checking location pointer updated or not

				// spliting and taking the first word in the line
				String s = Line.split(" ")[0];

				// for every s we check in symbol table it is there or not and assigning address
				for (Map.Entry m : symtable.entrySet()) {
					if (s.equals(m.getKey()))          // if current word is found in symtable
					{
						m.setValue(LC);              // assign address
						loc_assi = 1;
					}
				}

				// If symbol is not present then adding it in symbol table
				if (s.length() != 0 && loc_assi == 0) {

					symtable.put(s, String.valueOf(LC));
					sTLine++;
				}

				String s_type = null;            // for the type of current statement
				s = Line.split(" ")[1];         // consider the second word in the line //this is our first operand.

				// checking if it is an imperative statement
				for (Map.Entry m : is.entrySet()) {
					if (s.equals(m.getKey())) {
						BW.write("(IS," + m.getValue() + ")\t");
					}
				}

				// checking if its is an assembler directive and adding in ad
				for (Map.Entry m : ad.entrySet()) {
					if (s.equals(m.getKey())) {
						BW.write("(AD," + m.getValue() + ")\t");
					}
				}
 
				// checking if it is a declarative statement and adding in ds
				for (Map.Entry m : ds.entrySet()) {
					if (s.equals(m.getKey())) {
						BW.write("(DL," + m.getValue() + ")\t");
						if (m.getValue().equals("01")) {
							s_type = "dc";
						} 
						else {
							s_type = "ds";
						}
					}
				}

				// Assigning addresses to the literals
				if (s.equals("LTORG")) {
					pooltable.add(pTLine);

					for (Map.Entry m : littable.entrySet()) {
						if (m.getValue() == "") {
							m.setValue(LC);
							LC++;
							pTLine++;
							lc1 = 0;

						}
					}
				}

				// assigning address to literals and fill pool table
				if (s.equals("END")) {
					int poolptr = pTLine;    // for entering in pool table
					int lit_p = 0;

					for (Map.Entry m : littable.entrySet()) {
						if (m.getValue() == "") {
							m.setValue(LC);
							LC++;
							pTLine++;
							lc1 = 0;
							lit_p = 1;				
						}
					}

					if (lit_p == 1) {
						pooltable.add(poolptr);
					}
				}

				// Change the value of location counter
				if (s.equals("ORIGIN")) {
					s_type = "origin";
					lc1 = 0;
				}

				// update the address of the symbol to the allocated one
				if (s.equals("EQU")) {
					s_type = "equ";
					lc1 = 0;
				}

				if (Line.split(" |\\,").length > 2) { // if there are 3 words
					s = Line.split(" |\\,")[2];
					int d = 0;

					// checking if it is a condition code
					for (Map.Entry m : cc.entrySet()) {
						if (s.equals(m.getKey())) {
							BW.write(m.getValue() + "\t");
							d = 1;
						}
					}
					if (d == 0) {
						// checking if it is a register
						if (s.contains("REG")) {
							for (Map.Entry m : reg.entrySet()) {
								if (s.equals(m.getKey())) {
									BW.write(m.getValue() + "\t");
								}
							}
						}
						// checking if it is a declaration statement
						else if (s_type == "dc") {
							s.contains("\\'");
							String st = s.split("\\'")[1];
							BW.write("(C," + st + ")\t");
						} 
						else if (s_type == "ds") {
							BW.write("(C," + s + ")\t");
							LC += Integer.parseInt(s);
							lc1 = 0;
						}

						// Calculating the value of location counter
						else if (s_type == "origin") {
							LC = 0;
							if (s.contains("+")) {
								for (int i = 0; i < s.split("\\+").length; i++) {
									if (s.split("\\+|\\-")[i].matches("^[0-9]+$")) {
										LC += Integer.parseInt(s.split("\\+")[i]);
									} 
									else {
										LC += Integer.parseInt(symtable.get(s.split("\\+")[i]));
									}
								}
							} 
							else if (s.contains("-")) {
								LC = Integer.parseInt(symtable.get(s.split("\\-")[0]));
								for (int i = 1; i < s.split("\\-").length; i++) {
									if (s.split("\\-")[i].matches("^[0-9]+$")) {
										LC -= Integer.parseInt(s.split("\\-")[i]);
									} 
									else {
										LC -= Integer.parseInt(symtable.get(s.split("\\-")[i]));
									}
								}
							} 
							else {
								LC = Integer.parseInt(symtable.get(s));
							}
						}

						// Updating the value of address of the label
						else if (s_type == "equ") {
							int a = 0;             // for calculting updated LC
							if (s.contains("+")) {
								for (int i = 0; i < s.split("\\+").length; i++) {
									if (s.split("\\+")[i].matches("^[0-9]+$")) {
										a += Integer.parseInt(s.split("\\+")[i]);
									} else {
										a += Integer.parseInt(symtable.get(s.split("\\+|\\-")[i]));
									}
								}
							} else if (s.contains("-")) {
								a = Integer.parseInt(symtable.get(s.split("\\-")[0]));
								for (int i = 1; i < s.split("\\-").length; i++) {
									if (s.split("\\-")[i].matches("^[0-9]+$")) {
										a -= Integer.parseInt(s.split("\\-")[i]);
									} else {
										a -= Integer.parseInt(symtable.get(s.split("\\-")[i]));
									}
								}
							}
							 else {
								a = Integer.parseInt(symtable.get(s));
							}
							symtable.put(Line.split(" |\\,")[0], Integer.toString(a));
						}

						// it is a symbol then adding it to the symbol table
						else {
							int p = 0;                  // checking if it is present or not
							int pos = 1;                // for checking the position
							for (Map.Entry m : symtable.entrySet()) {
								if (s.equals(m.getKey())) {
									p = 1;
									BW.write("(S," + pos + ")\t");
									break;
								}
								pos++;
							}
							if (p == 0) {
								symtable.put(s, "");
								BW.write("(S," + sTLine + ")\t");
								sTLine++;
							}
						}
					}

					if (Line.split(" |\\,").length > 3) { // if there are 4 words
						s = Line.split(" |\\,")[3];

						// this is our 2nd operand it is either a literal, or a symbol
						// checking if it is a literal
						if (s.contains("=")) {
							littable.put(s, "");
							BW.write("(L," + lTLine + ")\t");
							lTLine++;
						}
						 else {
							int p = 0;
							int pos = 1;
							for (Map.Entry m : symtable.entrySet()) {
								if (s.equals(m.getKey())) {
									p = 1;
									BW.write("(S," + pos + ")\t");
									break;
								}
								pos++;
							}
							if (p == 0) {
								symtable.put(s, "");
								BW.write("(S," + sTLine + ")\t");
								sTLine++;
							}
						}
					}
				}
				BW.write("\n"); // done with a line

				if (lc1 == 1) {
					LC++;
				}
			}

			// writing to symbol table
			String f1 = "symbol_Table.txt";
			FileWriter fw1 = new FileWriter(f1);
			BufferedWriter bw1 = new BufferedWriter(fw1);
			System.out.println("========SYMBOL TABLE========");
			System.out.println("SYMBOL\tADDRESS");
			for (Map.Entry m : symtable.entrySet()) {
				bw1.write(m.getKey() + "\t" + m.getValue() + "\n");
				System.out.println(m.getKey() + "\t" + m.getValue());
			}
			System.out.println("\n");

			// writing to literal table
			String f2 = "Literal_Table.txt";
			FileWriter fw2 = new FileWriter(f2);
			BufferedWriter bw2 = new BufferedWriter(fw2);
			System.out.println("========LITERAL TABLE========");
			System.out.println("LITERAL\tADDRESS");
			for (Map.Entry m : littable.entrySet()) {
				bw2.write(m.getKey() + "\t" + m.getValue() + "\n");
				System.out.println(m.getKey() + "\t" + m.getValue());
			}

			System.out.println("\n");

			// writing to pool table
			String f3 = "Pool_Table.txt";
			FileWriter fw3 = new FileWriter(f3);
			BufferedWriter bw3 = new BufferedWriter(fw3);
			System.out.println("========POOL TABLE========");
			for (Integer item : pooltable) {
				bw3.write(item + "\n");
				System.out.println(item);
			}

			bw1.close();
			bw2.close();
			bw3.close();
			BW.close();
			BR.close();

		} catch (IOException e) {
			e.printStackTrace();
		}

	}

}


/*output*/

/*
========SYMBOLE TABLE========
SYMBOL  ADDRESS
LAST    216
A       217
LOOP    202
BACK    202
NEXT    214
B       218


========LITERAL TABLE========
LITERAL ADDRESS
='5'    211
='1'    219
='2'    212


========POOL TABLE========
1
3


*/
