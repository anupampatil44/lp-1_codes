/*
 * Problem Statement :-
	Simulation of election algorithms(Ring and Bully).
*/


import java.io.*;
import java.util.Scanner;

public class Bully 
{
    static int n;
    static int pro[] = new int[100];
    static int sta[] = new int[100];
    static int co;
     
    public static void main(String args[])throws IOException
    {
		System.out.print("\nEnter Total number of processes : ");
        Scanner in = new Scanner(System.in);
        n = in.nextInt();

         
        for(int i=0;i<n;i++)
        {
			System.out.print("\n\t For process " + (i+1) + ":");
			System.out.print("\n\t\t Status (0/1) : ");
			sta[i] = in.nextInt();
			System.out.print("\n\t\t Priority : ");
			pro[i] = in.nextInt();
        }
         
		System.out.print("\n\t Which process will initiate election? : ");
		int ele = in.nextInt();
	         
		elect(ele);
		System.out.println("\n\t Final coordinator is " + co);
		in.close();
    }
     
    static void elect(int ele)
    {
        ele--;
        co = ele+1;
        for(int i=0; i<n; i++)
        {
            if(pro[ele] < pro[i])
            {
				System.out.println("\n\t Election message is sent from " + (ele+1)
						+ " to " + (i+1));
                if(sta[i]==1)
                    elect(i+1);
            }
        }
    }
}
