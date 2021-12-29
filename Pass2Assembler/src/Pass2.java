import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.ArrayList;




public class Pass2 {
	ArrayList<TableRow> SYMTAB,LITTAB; //Resizable-array implementation of the List interface

	public Pass2()
	{
		SYMTAB=new ArrayList<>();
		LITTAB=new ArrayList<>();
	}
	public static void main(String[] args) {
		Pass2 pass2=new Pass2();
		
		try {
			pass2.generateCode("IC.txt");
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	public void readtables()
	{
		BufferedReader br;
		String line;
		try
		{
			br=new BufferedReader(new FileReader("SYMTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+"); 
				/*split("\\s+"); This combines all-white spaces as a delimiter. 
				So if you have the string: "Hello[space][tab]World" This will yield the strings "Hello" and "World" 
				and eliminate the space among the [space] and the [tab]*/
				//System.out.print(parts[1]);  = A,L1 etc
				//System.out.println(parts[2]);= 100,103 etc (locations)
				
				SYMTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0]) ));
				
			}
			br.close();
			br=new BufferedReader(new FileReader("LITTAB.txt"));
			while((line=br.readLine())!=null)
			{
				String parts[]=line.split("\\s+");
				LITTAB.add(new TableRow(parts[1], Integer.parseInt(parts[2]),Integer.parseInt(parts[0])));
			}
			br.close();
		}
		catch (Exception e) {
			System.out.println(e.getMessage());
		}
	}

	public void generateCode(String filename) throws Exception
	{
		readtables();
		BufferedReader br=new BufferedReader(new FileReader(filename));

		BufferedWriter bw=new BufferedWriter(new FileWriter("PASS2.txt"));
		String line,code;
		while((line=br.readLine())!=null)
		{
			String parts[]=line.split("\\s+");
			if(parts[0].contains("AD")||parts[0].contains("DL,01")) // assembler directive or a declarative statement
			{
				bw.write("\n");   //?
				continue;
			}
			else if(parts.length==2)
			{
				if(parts[0].contains("DL,02")) //declarative statement
				{
					
					
					parts[0]=parts[0].replaceAll("[^0-9]", ""); //removing everything except decimals
				
					
					//if(Integer.parseInt(parts[0])==1)  // DS
					
						
						int constant=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
						code="00\t0\t"+String.format("%03d", constant)+"\n"; // constant 
						bw.write(code);
						
						
					
				}
				else if(parts[0].contains("IS"))
				{
					int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // replacing all non digits with a ""
					
					if(opcode==10)  // opcode for print
					{
						if(parts[1].contains("S"))  //symbol
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", "")); //symindex is the index in the symbol table
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n"; 
							// "%03d" it's padding. if the number has 1 or 2 digits than some leading zeros will be added to the number to make its width equal to 3.
							bw.write(code);
							//System.out.println(code);
						}
						else if(parts[1].contains("L"))
						{
							int symIndex=Integer.parseInt(parts[1].replaceAll("[^0-9]", ""));
							code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
							bw.write(code);
						}
						
					}
				}
			}
			else if(parts.length==1 && parts[0].contains("IS"))
			{
				int opcode=Integer.parseInt(parts[0].replaceAll("[^0-9]", ""));
			    
				code=String.format("%02d", opcode)+"\t0\t"+String.format("%03d", 0)+"\n"; //only 0s
				
				bw.write(code);
			}
			else if(parts[0].contains("IS") && parts.length==3) //All OTHER IS INSTR
			{
			int opcode=	Integer.parseInt(parts[0].replaceAll("[^0-9]", "")); // OPCODE given in MOT
			
			int regcode=Integer.parseInt(parts[1]);   // indicates whether a register is present
			
			if(parts[2].contains("S"))   //symbol
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", "")); // the 2nd value in tuple contains the location e.g. (S,03)
				//System.out.println(SYMTAB.get(symIndex-1).getSymbol());
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", SYMTAB.get(symIndex-1).getAddress())+"\n";
				
				bw.write(code);
			}
			else if(parts[2].contains("L"))     // literal 
			{
				int symIndex=Integer.parseInt(parts[2].replaceAll("[^0-9]", ""));
				
				code=String.format("%02d", opcode)+"\t"+regcode+"\t"+String.format("%03d", LITTAB.get(symIndex-1).getAddress())+"\n";
				bw.write(code);
			}		
			}
			
		}
		bw.close();
		br.close();

	}



	
	
	
	
}
