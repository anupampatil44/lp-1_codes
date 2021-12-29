// SERVER CODE:

import java.io.*;
import java.net.*;

public class server {
  public static void main(String[] args) throws UnknownHostException, IOException {

    ServerSocket ss = null;
    try {
      ss = new ServerSocket(3000);
    } catch (IOException ioe) {
      System.out.println("Error finding port");
      System.exit(1);
    }
    Socket soc = null;
    try {
      soc = ss.accept();
      System.out.println("Connection accepted at :" + soc);
    } catch (IOException ioe) {
      System.out.println("Server failed to accept");
      System.exit(1);
    }
    DataOutputStream dos = new DataOutputStream(soc.getOutputStream());
    BufferedReader br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
    String s;
    System.out.println("Server waiting for message from the client");
    boolean quit = false;
    do {
      String msg = "";
      s = br.readLine();
      int len = s.length();
      if (s.equals("exit")) {
        quit = true;
      }
      for (int i = 0; i < len; i++) {
        msg = msg + s.charAt(i);
        dos.write((byte) s.charAt(i));
      }

      System.out.println("From client :" + msg);
      dos.write(13);
      dos.write(10);
      dos.flush();
    } while (!quit);
    dos.close();
    soc.close();
  }
}

// CLIENT CODE:

// OUTPUT:
// Server:
// Connection accepted at :Socket[addr=/192.168.219.1,port=52592,localport=95]
// Server waiting for message from the client
// From client :Hi
// From client :This is client
// From client :Bye
// From client :exit

// Client:
// To start the dialog type the message in this client window
// Type exit to end
// Hi
// From server :Hi
// This is client
// From server :This is client
// Bye
// From server :Bye
// exit
// From server :exit
