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
    static int nv[] = new int[100];
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
//			System.out.print("\n\t\t Priority : ");
			pro[i] = (i+1); //in.nextInt();
        }
         
		System.out.print("\n\t Which process will initiate election? : ");
		int ele = in.nextInt();

	         
		elect(ele);
		System.out.println("\n\t Final coordinator is " + co);
		in.close();
    }
     
    static void elect(int ele) {
        if(nv[ele-1]==1)
        {
            return;
        }
        else
        {
            nv[ele-1]=1;
        }
        for (int j=ele; j < n;j++ ) {
            for (int i = ele; i < n; i++) {
                if (pro[ele - 1] < pro[i]) {
                    System.out.println("\n\t Election message is sent from " + (ele)
                            + " to " + (i + 1));
                    if (sta[i] == 1) {
                        System.out.println("\n\t OK message is sent from " + (i + 1)
                                + " to " + (ele));
                        if (i + 1 > co) {
                            co = i + 1;
                        }

                        continue;

                    }

                    if (sta[i] == 0) {
                        System.out.println("As node status is crashed OK message is not sent from " + (i + 1)
                                + " to " + (ele));
                    }
                }
            }

//            System.out.println("J: "+j);
            if(sta[j]==1 && nv[j]!=1)
            {
                elect(j+1);
                if(j+1-ele<=1)
                    return;
            }

        }
    }


}
