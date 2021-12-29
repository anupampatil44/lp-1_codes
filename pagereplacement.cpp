#include<bits/stdc++.h>
using namespace std;

struct pages
{
    int page=-1;
    int age;
};

struct pages4optimal
{
    int page=-1;
    int hitcount=0;
    int farthest=100; //arbitrary super high value just in case a farthest/next occurrence doesn't exist 
};

class pagesegmentation
{
    pages pagesarr[100]={-1,-1};
    queue<int> pageframes;


    public:

    void fifo()
    {
        int size,count=0,exit=1,temp;
        int pagefault=0;
        cout<<"Enter max. page limit in your programme: ";
        cin>>size;
        while(exit!=0)
        {
            cout<<"Enter Value you wish to add: ";
            cin>>temp;
            queue<int> temp_traverse=pageframes;
            bool ispresent=false;

            while(!temp_traverse.empty())
            {
                if(temp_traverse.front()==temp)
                {
                    ispresent=true;
                    break;
                }
                temp_traverse.pop();
            }

            if(count<size and ispresent==false)
            {
                pageframes.push(temp);
                pagefault++;
                count++;
            }

            else
            {
                if(ispresent==false)
                {
                    pageframes.pop();
                    pageframes.push(temp);
                    pagefault++;
                }
                
            }

            cout<<"Pages:\n";
            queue<int> traverse_out=pageframes;

            while(!traverse_out.empty())
            {
                cout<<traverse_out.front()<<"  ";
                traverse_out.pop();
            }
            
            cout<<"\n";
            cout<<"Page faults: "<<pagefault<<endl;
            cout<<"continue simulation?(1/0): ";
            cin>>exit;

        }
    }

    void optimal()
    {   
        int size,pagesinp,count=0,faults=0;
        cout<<"Enter max. page limit in your programme: ";
        cin>>size;

        cout<<"Enter total number of pages you wish to add: ";
        cin>>pagesinp;
        pages4optimal simulation[size];
        pages4optimal pages_arr[pagesinp];

        for(int i=0;i<pagesinp;i++)
        {
            cin>>pages_arr[i].page;
        }

        for(int i=0;i<pagesinp-1;i++)
        {
            for(int j=i+1;j<pagesinp;j++)
            {
                if(pages_arr[j].page==pages_arr[i].page)
                {
                    pages_arr[i].farthest=j-i;
                }
            }
        }

        // now to simulate the page frame occupation:
        for(int j=0;j<pagesinp;j++)
        {
            bool ispresent=false;
            // iterating to check if that value is already present inside the frame
            for(int k=0;k<size;k++)
            {
                if(simulation[k].page==pages_arr[j].page)
                {
                    ispresent=true;
                    // simulation[k]=pages_arr[j];
                    break;
                }
            }

            if(ispresent==false and count>=size)
            {
                faults++;
                for(int a=0;a<size;a++)
                {
                    for(int b=0;b<size-a-1;b++)
                    {
                        if (simulation[b].farthest<simulation[b+1].farthest)
                        {
                            pages4optimal temp=simulation[b];
                            simulation[b]=simulation[b+1];
                            simulation[b+1]=temp;
                        }
                    }
                }

                simulation[0]=pages_arr[j];

            }

            else
            {
                if(ispresent==false and count<size)
                {
                    simulation[count]=pages_arr[j];
                    count++;
                    faults++;
                }
            }

            cout<<"Pages:\n";
            for(int l=0;l<size;l++)
            {
                cout<<simulation[l].page<<"\t";
            }
            cout<<endl;
            cout<<"Page faults: "<<faults<<endl;
        }

            
    }




    void least_recently_used()
    {
        int size,count=0;
        cout<<"Enter max. page limit in your programme: ";
        cin>>size;
        int pagefault=0;
        int temp,exit=1,age=0;
        bool ispresent=false;

        while(exit!=0)
        {
            ispresent=false;
            cout<<"Enter Value you wish to add: ";
            cin>>temp;

            for(int i=0;i<size;i++)
            {
                if(pagesarr[i].page==temp)
                {
                    ispresent=true;
                    pagesarr[i].age=age;
                    age++;
                    break;
                }
            }

            if(count<size and ispresent==false)
            {
                pagesarr[count].page=temp;
                pagesarr[count].age=age;
                age++;
                count++;
                pagefault++;
            }

            else if(ispresent==false)
            {
                //sorting array based on which page was present earlier
                for(int i=0;i<size;i++)
                {
                    for(int j=0;j<size-i-1;j++)
                    {
                        if(pagesarr[j].age>pagesarr[j+1].age)
                        {
                            pages t=pagesarr[j];
                            pagesarr[j]=pagesarr[j+1];
                            pagesarr[j+1]=t;
                        }
                    }
                }


                // replacing oldest process with the new one
                pagesarr[0].page=temp;
                pagesarr[0].age=age;
                age++;
                pagefault++;

            }

            cout<<"Pages:\n";
            for(int i=0;i<size;i++)
            {
                if(pagesarr[i].page!=-1)
                    cout<<pagesarr[i].page<<"\t";
            }
            cout<<endl;

            cout<<"Page Fault: "<<pagefault<<endl;
            cout<<"continue simulation?(1/0): ";
            cin>>exit;
        }

    }


};


int main()
{
    
    while(1)
    {
        pagesegmentation obj;
        int choice;
        cout<<"MENU:\n1) FIFO Page Replacement\n2) Optimal Page Replacement\n3) Least Recently Used\n4) Exit\n\n";
        cout<<"Enter choice: ";
        cin>>choice;
        switch (choice)
        {
        case 1:
            obj.fifo();
            break;
        
        case 2:
            obj.optimal();
            break;
        
        case 3:
            obj.least_recently_used();
        
        case 4:
            return 0;
            break;
        default:
            cout<<"Wrong choice entered, try again.\n";
            break;
        }
    }

    return 0;
}