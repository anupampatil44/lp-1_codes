import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.util.HashMap;
import java.util.Vector;



//for storing and accessing MNT
class MNTable {
String name;
int pp,kp,mdtp,kpdtp;


 //storing MNT
public MNTable(String name, int pp, int kp, int mdtp, int kpdtp) {
    
    this.name = name;                            //macro name
    this.pp = pp;                                //no. of positional parameters
    this.kp = kp;                                //no. of keyword parameters
    this.mdtp = mdtp;                            //mdt pointer
    this.kpdtp = kpdtp;                          //kpdt pointer
}

//returns name of macro
public String getName() {        
    return name;
}


public int getPp() {
    return pp;
}

public int getKp() {
    return kp;
}

public int getMdtp() {
    return mdtp;
}

public int getKpdtp() {
    return kpdtp;
}


}

public class MacroPass2 {

public static void main(String[] args) throws Exception {

	//for reading files
	BufferedReader irb=new BufferedReader(new FileReader("output.txt"));
	BufferedReader mdtb=new BufferedReader(new FileReader("mdt.txt"));
	BufferedReader kpdtb=new BufferedReader(new FileReader("kpdt.txt"));
	BufferedReader mntb=new BufferedReader(new FileReader("mnt.txt"));
	
	//for writing to file
	FileWriter fr=new FileWriter("ppt2.txt");
	
	//for storing MNT
	HashMap<String, MNTable> mnt=new HashMap<>();

	//for storing argument table
	HashMap<Integer, String> aptab=new HashMap<>();

	//for getting values from argument table
	HashMap<String,Integer> aptabInverse=new HashMap<>();
	
   //for storing MDT
	Vector<String>mdt=new Vector<String>();

	//for storing KPDT
	Vector<String>kpdt=new Vector<String>();
	
    //varibles used in program
	int pp;                          //no. of positional parameters 
	int kp;                          //no. of keyword parameters
	int mdtp;                        //MDT pointer
	int kpdtp;                       //KPDT pointer
	int paramNo;                      //for keeping track of position of parameter

	String line;                      //for reading line by line 

	//storing MDT in mdt vector
	while((line=mdtb.readLine())!=null)
	{
		mdt.addElement(line);
	}

	//storing KPDT in kpdt vector
	while((line=kpdtb.readLine())!=null)
	{
		kpdt.addElement(line);
}

	//storing MNT in mnt HashMap
	while((line=mntb.readLine())!=null)
	{
		String parts[]=line.split("\\s+");
		mnt.put(parts[0], new MNTable(parts[0], Integer.parseInt(parts[1]), Integer.parseInt(parts[2]), Integer.parseInt(parts[3]), Integer.parseInt(parts[4])));
		
	}
	
	//reading the intermediate file from Pass1 line by line
	while((line=irb.readLine())!=null)
	{
		String []parts=line.split("\\s+");             //spliting by white spaces

		//checking Macro name in MNT
		if(mnt.containsKey(parts[0]))
		{
			pp=mnt.get(parts[0]).getPp();              
			kp=mnt.get(parts[0]).getKp();              
			kpdtp=mnt.get(parts[0]).getKpdtp();       
			mdtp=mnt.get(parts[0]).getMdtp();

			paramNo=1;                                        //setting parameter no. to 1   
			
			//putting positional parameters in aptab
			for(int i=0;i<pp;i++)
			{ 
				parts[paramNo]=parts[paramNo].replace(",", "");
				aptab.put(paramNo, parts[paramNo]);             //putting parameter no. and parameter in aptab   
				aptabInverse.put(parts[paramNo], paramNo);       //putting parameter and its number
				paramNo++;                                       //incresing paraemter no.
			}

			int j=kpdtp-1;                                       //initializing kpdp pointer

			//putting keyword parameters in aptab
			for(int i=0;i<kp;i++)
			{
				String temp[]=kpdt.get(j).split("\t");             //spliting kpdt by tab
				aptab.put(paramNo,temp[1]);                        //putting parameter no. and parameter in aptab
				aptabInverse.put(temp[0],paramNo);                 //putting formal parameter and its number
				j++;                                               //increasing
				paramNo++;                                         //increasing parameter no.
			}
			
            //writing the actual parameter values for keyword arguments(not default) in aptab
			for(int i=pp+1;i<parts.length;i++)
			{
				parts[i]=parts[i].replace(",", "");             
				String splits[]=parts[i].split("=");             //spliting the parameter name and value in macro call
				String name=splits[0].replaceAll("&", "");
				//writing the parameter number from aptabInverse and name from macro call
				aptab.put(aptabInverse.get(name),splits[1]);    
			}

			int i=mdtp-1;                                  //setting the mdt pointer

			//writing in pass2 file
			while(!mdt.get(i).equalsIgnoreCase("MEND"))
			{
				String splits[]=mdt.get(i).split("\\s+");                  //spliting mdt space 
				fr.write("+");
				for(int k=0;k<splits.length;k++)
				{
					//if it is parameter
					if(splits[k].contains("(P,"))
					{
						splits[k]=splits[k].replaceAll("[^0-9]", "");       //replacing all non digit values to space
						//taking the parameter from aptab corresponding the parameter position 
						String value=aptab.get(Integer.parseInt(splits[k])); 
						fr.write(value+"\t");
					}
					//if it is instruction
					else
					{
						//writing in pass2
						fr.write(splits[k]+"\t");
					}
				}
				fr.write("\n");	
				i++;                 //increasing mdt pointer
			}

			//printing aptab for the corresponding macro
			System.out.println("APTAB");
			aptab.forEach((k,v)->System.out.println(k+" : "+v));
			System.out.println();

			//clearing aptab and aptabInverse
			aptab.clear();
			aptabInverse.clear();
		}
		else
		{
			fr.write(line+"\n");
		}
		
}

fr.close();
mntb.close();
mdtb.close();
kpdtb.close();
irb.close();
}
}


/*output*/

/*

APTAB
1 : 10
2 : 20
3 : AREG
4 : CREG

APTAB
1 : 100
2 : 200
3 : BREG
4 : AREG

*/