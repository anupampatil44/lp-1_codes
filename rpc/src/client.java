import java.io.*;
import java.net.*;
import java.util.Scanner;

public class client {
  public static void main(String[] args) throws IOException {
    Socket soc = null;
    String str = null;
    BufferedReader br = null;
    DataOutputStream dos = null;
    BufferedReader kyrd = new BufferedReader(new InputStreamReader(System.in));
    try {
      soc = new Socket(InetAddress.getLocalHost(), 3000);
      br = new BufferedReader(new InputStreamReader(soc.getInputStream()));
      dos = new DataOutputStream(soc.getOutputStream());
    } catch (UnknownHostException uhe) {
      System.out.println("Unknown Host");
      System.exit(0);
    }
    System.out.println("To start the dialog type the message in this client window \n Type exit to end");
    boolean more = true;
    while (more) {
      str = kyrd.readLine();
      dos.writeBytes(str);
      dos.write(13);
      dos.write(10);
      dos.flush();
      String s, s1;
      s = br.readLine();
      System.out.println("From server :" + s);
      if (s.equals("exit")) {
        break;
      }
    }
    br.close();
    dos.close();
    soc.close();
  }
}