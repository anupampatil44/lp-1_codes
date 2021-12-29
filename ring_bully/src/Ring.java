/*
 * Problem Statement :-
	Simulation of election algorithms(Ring and Bully).
*/



import java.util.*;
public class Ring
{
	static int token[] = new int[100];
	static int l = 0;
	public static void main(String args[])
	{
		int winner, max = -1;
		Scanner sc = new Scanner(System.in);	
		System.out.print("\n\t Enter the Total Number of processes = ");
		int n = sc.nextInt();
		winner = n;
		int processes[] = new int[n+1];
		int status[] = new int[n+1];
		System.out.println("\n\t Enter the status of processes (1/0) .....");
		for(int i=0; i<n; i++)
		{
			processes[i] = i;
			System.out.print("\n\t Enter the status of process "+ i +" = ");
			status[i] = sc.nextInt();
		}
		System.out.print("\n\t Enter the process initiator = ");
		int x = sc.nextInt();
		
		int i = x;
		while(i<n)
		{
			if(status[i]==1)
			{
				int next = i+1;
				while(next<n)
				{
					if(status[next]==1)
					{
						System.out.println("\n\t Election message is sent from "+ i +" to "
								+ next);	
						token[l] = i;
						l++;
						print();
						winner = next;
						break;
					}
					else
					{
						next++;
					}
				}
			}
			i++;
		}

		System.out.println("\n\t Election message is sent from " + winner + " to 0");
		token[l] = winner;
		l++;
		print();
		i = 0; 
		while(i<x)
		{
			if(status[i]==1)
			{
				int next = i+1;
				while(next<n)
				{
					if(status[next]==1)
					{
						System.out.println("\n\t Election message is sent from "+ i +" to "
								+ next);	
						token[l] = i;
						l++;
						print();
						break;
					}
					else
					{
						next++;
					}
				}
			}
			i++;
		}

		for(int j=0; j<l; j++)
		{
			if(token[j] > max)
				max = token[j];
		}
		System.out.println("\n\t Co-ordinator is " + max);

		sc.close();
	}

	public static void print()
	{
		System.out.print("\n\t ___ Token received : ");
		for(int i=0; i<l; i++)	
			System.out.print(token[i] + ", ");
		System.out.println();
	}
}
