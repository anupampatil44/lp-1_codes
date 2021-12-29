import java.io.BufferedReader;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Iterator;
import java.util.LinkedHashMap;


public class MacroPass1 {

public static void main(String[] args) throws IOException{


	//filereader for reading input file
	FileReader FR=new FileReader("input.txt");
	BufferedReader br=new BufferedReader(FR);
	
	//file writer
	FileWriter mnt=new FileWriter("mnt.txt");              //Macro Name Table
	FileWriter mdt=new FileWriter("mdt.txt");              //Macro Definition Table
	FileWriter kpdt=new FileWriter("kpdt.txt");            //Keyword Parameter Default Table
	FileWriter pnt=new FileWriter("pntab.txt");            //Parameter name table
    FileWriter op=new FileWriter("output.txt");             //output file

	//for storing the parameters name and position
	LinkedHashMap<String, Integer> pntab=new LinkedHashMap<>();
	
	 
	String line;                         //for reading line by line
	String Macroname = null;             //for storing the name of macro

	int mdtp=1;                    //MDT Pointer
	int kpdtp=0;                   //KPDT Pointer
	int paramNo=1;                 //keeping track of no. of parameters
	int pp=0;                      //no. of positional parameters
	int kp=0;                      //no. of keyword parameters
	int flag=0;                    //checking for MACRO & MEND statement

	while((line=br.readLine())!=null)
	{
		
		String tokens[]=line.split("\\s+");                      //spliting by whitespaces
		if(tokens[0].equalsIgnoreCase("MACRO"))
		{
			flag=1;                  
			line=br.readLine();
			tokens=line.split("\\s+");
			Macroname=tokens[0];


			if(tokens.length<=1)                      //if there are no parameters
			{
				mnt.write(tokens[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
				continue;
			}
			for(int i=1;i<tokens.length;i++)         //processing of parameters
			{
				tokens[i]=tokens[i].replaceAll("[&,]", "");
		
			
				//for keyword parameters
				if(tokens[i].contains("="))
				{
					++kp;                           //increase no. of keyword parameters

					//for storing parameter and its default value
					String keywordParam[]=tokens[i].split("=");

					//Writing in LinkedHashMap---parameter name and no. of that parameter
					pntab.put(keywordParam[0], paramNo++);

					//writing in KDPT
                	if(keywordParam.length==2)                //if there is default parameter
					{
						kpdt.write(keywordParam[0]+"\t"+keywordParam[1]+"\n");
					}
					else
					{
						kpdt.write(keywordParam[0]+"\t-\n");
					}
				}

				//writing positional parameters
				else
				{
					pntab.put(tokens[i], paramNo++);
					pp++;                           //increasing no. of positional parameters
				}
			}

			mnt.write(tokens[0]+"\t"+pp+"\t"+kp+"\t"+mdtp+"\t"+(kp==0?kpdtp:(kpdtp+1))+"\n");
			kpdtp=kpdtp+kp;                       //increasing KDPT Pointer
			
			
			
		}

		//if MEND
		else if(tokens[0].equalsIgnoreCase("MEND"))
		{
			mdt.write(line+"\n");               //writing MEND in MDT

			flag=kp=pp=0;                       //reseting positional parameters and keyword parameters
			mdtp++;                 
			paramNo=1;
			pnt.write(Macroname+":\t");

			//iterator for keySet in HashMap
			Iterator<String> itr=pntab.keySet().iterator();

			//writing parameters in PNT
			while(itr.hasNext())
			{
				pnt.write(itr.next()+"\t");
			}

			pnt.write("\n");

			//clearing PNTAB LinkedHashMap
			pntab.clear();
		}

		//writing in MDT
		else if(flag==1)
		{
			for(int i=0;i<tokens.length;i++)
			{
				if(tokens[i].contains("&"))
				{
					tokens[i]=tokens[i].replaceAll("[&,]", "");
					mdt.write("(P,"+pntab.get(tokens[i])+")\t");               //writing parameters and position
				}
				else
				{
					mdt.write(tokens[i]+"\t");          //writing mnemonic
				}
			}
			mdt.write("\n");
			mdtp++;
		}
		else{
			op.write(line+"\n");
		}
		
	}
	br.close();
	mdt.close();
	mnt.close();
	pnt.close();
	kpdt.close();
	op.close();
	System.out.println("Macro Pass1 Processing done. :)");
}

}
