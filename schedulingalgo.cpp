#include <bits/stdc++.h>
using namespace std;

class fcfs
{
	int *processes=new int[10];
	int n;
	int *burst_time=new int[10];
	int *arrival_time=new int[10];

	public:
	fcfs(int p[], int n, int b[], int a[])
	{
		for(int i=0;i<n;i++)
		{
			processes[i]=p[i];
			burst_time[i]=b[i];
			arrival_time[i]=a[i];
		}

		this->n=n;


	}
	void findWaitingTime(int processes[], int n, int bt[],int wt[], int at[])
	{
		int service_time[n];
		service_time[0] = at[0];
		wt[0] = 0;


		for (int i = 1; i < n ; i++)
		{
			service_time[i] = service_time[i-1] + bt[i-1];
			wt[i] = service_time[i] - at[i];

			if (wt[i] < 0)
				wt[i] = 0;
		}
	}

	void findTurnAroundTime(int processes[], int n, int bt[],int wt[], int tat[])
	{
		for (int i = 0; i < n ; i++)
			tat[i] = bt[i] + wt[i];
	}

	void findavgTime()
	{
		int wt[n], tat[n],compl_time[n];

		findWaitingTime(processes, n, burst_time, wt, arrival_time);

		findTurnAroundTime(processes, n, burst_time, wt, tat);

		cout << "Processes " << " Burst Time " << " Arrival Time "
			<< " Waiting Time " << " Turn-Around Time "
			<< " Completion Time \n";
		int total_wt = 0, total_tat = 0;
		for (int i = 0 ; i < n ; i++)
		{
			total_wt = total_wt + wt[i];
			total_tat = total_tat + tat[i];
			compl_time[i] = tat[i] + arrival_time[i];
			cout << " " << i+1 << "\t\t" << burst_time[i] << "\t\t"
				<< arrival_time[i] << "\t\t" << wt[i] << "\t\t "
				<< tat[i] << "\t\t " << compl_time[i] << endl;
		}

		cout<<"Chart:\n";
		int i, j; 
		for (i = 0; i < n-1; i++)     
		{
			for (j = 0; j < n-i-1; j++) 
			{
				if (compl_time[j] > compl_time[j+1]) 
				{
					int temp;
					temp=processes[j];
					processes[j]=processes[j+1];
					processes[j+1]=temp;
				}
			}
		}

		for(i=0;i<n;i++)
		{
			if(i<n-1)
				cout<<processes[i]<<"-> ";
			else
				cout<<processes[i]<<endl;
		}

		cout << "Average waiting time = "
			<< (float)total_wt / (float)n;
		cout << "\nAverage turn around time = "
			<< (float)total_tat / (float)n<<endl;
	}
};


class psa
{

	public:

	void findmetrics(int n,int proc[],int a[],int b[],int pr[])
	{
		int waiting[10],turnaround[10],completion[10],temp[10],count=0,time,smallest,end;
		for(int i=0;i<n;i++)
		{
			temp[i]=b[i];
		}

		for(time=0;count!=n;time++)
    	{
			smallest=9;
			for(int i=0;i<n;i++)
			{
				if(a[i]<=time && pr[i]>pr[smallest] && b[i]>0 )
					smallest=i;
			}
			time+=b[smallest]-1;
			b[smallest]=-1;
			count++;
			end=time+1;
			completion[smallest] = end;
			waiting[smallest] = end - a[smallest] - temp[smallest];
			turnaround[smallest] = end - a[smallest];
    	}

		cout<<"Process"<<"\t\t"<< "burst-time"<<"\t"<<"arrival-time" <<"\t"<<"waiting-time" <<"\t"<<"turnaround-time"<< "\t"<<"completion-time"<<"\t"<<"Priority"<<endl;
		double avg,tt;
		for(int i=0;i<n;i++)
		{
			cout<<proc[i]<<"\t\t"<<temp[i]<<"\t\t"<<a[i]<<"\t\t"<<waiting[i]<<"\t\t"<<turnaround[i]<<"\t\t"<<completion[i]<<"\t\t"<<pr[i]<<endl;
			avg = avg + waiting[i];
			tt = tt + turnaround[i];
		}

		for (int i = 0; i < n; i++)     
		{
			for (int j = 0; j < n-i-1; j++) 
			{
				if (completion[j] > completion[j+1]) 
				{
					int temp=proc[j];
					proc[j]=proc[j+1];
					proc[j+1]=temp;
					
					int t2=completion[j];
					completion[j]=completion[j+1];
					completion[j+1]=t2;
				}
			}
		}


		cout<<"Gantt Chart:"<<endl;
		for(int i=0;i<n;i++)
		{
			if(i<n-1)
				cout<<proc[i]<<"-> ";
			else
				cout<<proc[i]<<endl;
		}
    
	cout<<"\nAverage waiting time ="<<avg/n<<endl;
    cout<<"Average Turnaround time ="<<tt/n<<endl;

	}
};


struct Process {
	int pid; // Process ID
	int bt; // Burst Time
	int art; // Arrival Time
};


class sjf
{
	
	public:
		void findWaitingTime(Process proc[],int n,int wt[])
		{
			int rt[n];

		for (int i = 0; i < n; i++)
			rt[i] = proc[i].bt;

		int complete = 0, t = 0, minm = INT_MAX;
		int shortest = 0, finish_time;
		bool check = false;

		cout<<"Gantt Chart:"<<endl;
		while (complete != n) {

			for (int j = 0; j < n; j++) 
			{
				if ((proc[j].art <= t) &&
				(rt[j] < minm) && rt[j] > 0) {
					minm = rt[j];
					shortest = j;
					check = true;
					cout<<proc[shortest].pid<<"->";
				}
			}

		if (check == false) {
			t++;
			continue;
		}

		// Reduce remaining time by one
		rt[shortest]--;
		
		// Update minimum with the smallest
		minm = rt[shortest];
		if (minm == 0)
			minm = INT_MAX;

		// If a process gets completely
		// executed
		if (rt[shortest] == 0) 
		{
			complete++;
			check = false;

			finish_time = t + 1;

			// waiting time
			wt[shortest] = finish_time -
						proc[shortest].bt -
						proc[shortest].art;

			if (wt[shortest] < 0)
				wt[shortest] = 0;
		}

		t++;
	}
	cout<<"\n\n";
}


void findTurnAroundTime(Process proc[], int n, int wt[], int tat[])
{

	for (int i = 0; i < n; i++)
		tat[i] = proc[i].bt + wt[i];
}

// Function to calculate average time
void findavgTime(Process proc[], int n)
{
	int wt[n], ct[n], tat[n], total_wt = 0,total_tat = 0;

	findWaitingTime(proc, n, wt);

	findTurnAroundTime(proc, n, wt, tat);

	cout << "Processes "<<" Arrival Time "<<" Burst time "<<" Completion time "<<" Waiting time "<<" Turn around time\n";

	for (int i = 0; i < n; i++) {
		total_wt = total_wt + wt[i];
		total_tat = total_tat + tat[i];
		ct[i]=tat[i]+proc[i].art;
		cout << " " << proc[i].pid << "\t\t"
			<< proc[i].art << "\t\t"
			<< proc[i].bt << "\t\t " <<ct[i]<<"\t\t"<< wt[i]
			<< "\t\t " << tat[i] << endl;
	}

	cout << "\nAverage waiting time = "
		<< (float)total_wt / (float)n;
	cout << "\nAverage turn around time = "
		<< (float)total_tat / (float)n;
	
	cout<<"\n\n";
}



};

class rr
{
	public:

	void findWaitingTime(int processes[],int arrival_time[], int n,
			int bt[], int wt[], int quantum)
	{

	int rem_bt[n];
	for (int i = 0 ; i < n ; i++)
		rem_bt[i] = bt[i];

	int t = 0; // Current time

	// Keep traversing processes in round robin manner until all of them are not done.
	cout<<"Gantt Chart:-"<<endl;
	while (1)
	{
		bool done = true;
		for (int i = 0 ; i < n; i++)
		{
	
			if (rem_bt[i] > 0)
			{
				done = false; // There is a pending process

				if (rem_bt[i] > quantum)
				{
					t += quantum;
					rem_bt[i] -= quantum;
				}

				else
				{
					// how much time a process has been processed
					t = t + rem_bt[i];
					wt[i] = t - bt[i];

					rem_bt[i] = 0;
				}

				cout<<processes[i]<<"->";
			}
		}

		if (done == true)
		{	
			for (int i = 0 ; i < n; i++)
			{
				wt[i] -=arrival_time[i];
			}
			cout<<"\n"<<endl;
			break;

		}
		
	}
}


void findTurnAroundTime(int processes[],int arrival_time[],  int n, int bt[], int wt[], int tat[])
{
	for (int i = 0; i < n ; i++)
		tat[i] = bt[i] + wt[i];// - arrival_time[i];
}

void findavgTime(int processes[], int n,int arrival_time[], int bt[],int quantum)
{
	int wt[n],ct[n], tat[n], total_wt = 0, total_tat = 0;

	findWaitingTime(processes,arrival_time, n, bt, wt, quantum);

	findTurnAroundTime(processes,arrival_time, n, bt, wt, tat);

	cout << "Processes "<< " Burst time "<<" Completion Time "
		<< " Waiting time " << " Turn around time\n";

	for (int i=0; i<n; i++)
	{
		total_wt = total_wt + wt[i];
		total_tat = total_tat + tat[i];
		ct[i]=tat[i]+arrival_time[i];
		cout << " " << i+1 << "\t\t" << bt[i] <<"\t "<<ct[i]<<"\t "
			<< wt[i] <<"\t\t " << tat[i] <<endl;
	}

	cout << "Average waiting time = "
		<< (float)total_wt / (float)n;
	cout << "\nAverage turn around time = "
		<< (float)total_tat / (float)n<<endl;
}
};


int main()
{
	int ch,n;
	while(ch!=5)
	{
		cout<<"Enter choice of which algorithm simulation you wish to run:\n1) FCFS\n2) Priority Scheduling Algorithm\n3) Shortest Job First Algorithm\n4) Round Robin Algorithm\n5) Exit\n";
		cin>>ch;
		if(ch==5)
			break;
		cout<<"Enter Total number of processes you wish to run: ";
		cin>>n;
		if(ch==1)
		{
			cout<<"Enter the process values:"<<endl;
			int processes[10];
			int burst_time[10];
			int arrival_time[10];
			for(int i=0;i<n;i++)
			{
				cout<<"Enter process no: ";
				cin>>processes[i];
				cout<<"Enter burst time for the process: ";
				cin>>burst_time[i];
				cout<<"Enter arrival time for the process: ";
				cin>>arrival_time[i];
			}
			fcfs obj(processes, n, burst_time, arrival_time);
			obj.findavgTime();
			continue;
		}

		else if(ch==2)
		{
			cout<<"Enter the process values:"<<endl;
			int processes[10];
			int burst_time[10];
			int arrival_time[10];
			int priority[10]={0};
			for(int i=0;i<n;i++)
			{
				cout<<"Enter process no: ";
				cin>>processes[i];
				cout<<"Enter burst time for the process: ";
				cin>>burst_time[i];
				cout<<"Enter arrival time for the process: ";
				cin>>arrival_time[i];
				cout<<"Enter priority for the process: ";
				cin>>priority[i];
				// proc[i].at=arrival_time[i];
				// proc[i].bt=burst_time[i];
				// proc[i].pr=priority[i];
				// proc[i].pno=processes[i];
			}
			
			psa obj;
			priority[9]=-1;
			obj.findmetrics(n,processes,arrival_time,burst_time,priority);
			continue;
		}

		else if(ch==3)
		{
			cout<<"Enter Process values:"<<endl;
			Process proc[10];
			for(int i=0;i<n;i++)
			{
				cout<<"Enter process no: ";
				cin>>proc[i].pid;
				cout<<"Enter burst time for the process: ";
				cin>>proc[i].bt;
				cout<<"Enter arrival time for the process: ";
				cin>>proc[i].art;
			}

			sjf obj;
			obj.findavgTime(proc, n);
		}

		else if(ch==4)
		{
			cout<<"Enter the process values:"<<endl;
			int processes[10];
			int burst_time[10];
			int arrival_time[10];
			int quantum;
			for(int i=0;i<n;i++)
			{
				cout<<"Enter process no: ";
				cin>>processes[i];
				cout<<"Enter burst time for the process: ";
				cin>>burst_time[i];
				cout<<"Enter arrival time for the process: ";
				cin>>arrival_time[i];
			}
			rr obj;
			cout<<"Enter Time Quantum for the processes: ";
			cin>>quantum;
			obj.findavgTime(processes, n,arrival_time, burst_time, quantum);
			continue;

		}


	}

	return 0;

}
