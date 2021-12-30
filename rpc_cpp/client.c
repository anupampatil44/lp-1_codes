#include<stdio.h>
#include<sys/types.h>
#include<sys/socket.h>
#include<netinet/in.h>

int main(int argc,char*argv[])

{
     
      int sockfd;
      struct sockaddr_in serv_addr;
      char message[]="PICT";

      serv_addr.sin_family=AF_INET;
      serv_addr.sin_addr.s_addr=inet_addr("127.0.0.1");
      serv_addr.sin_port=htons(6000);


     if((sockfd=socket(AF_INET,SOCK_STREAM,0))<0)
     printf("Can't create a socket\n");

     if(connect(sockfd,(struct sockaddr*)&serv_addr,sizeof(serv_addr))<0)
     printf("client:can't connect to server\n");
 
     printf("Enter a message:\n");
     scanf("%s",message);

     write(sockfd,message,sizeof(message)); 

     read(sockfd,message,50);

     printf("Getting message from server:\n %s",message);
     printf("\n");
     close(sockfd);

}
