package sample;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.Socket;

public class Client {
    public String adm;
    public int port;
    private Socket s;

    public Client(int port) {
        this.port = port;
    }

    public String getAdm() {
        return adm;
    }

    public void setAdm(String adm) {
        this.adm = adm;
    }

    public String connect() {
        String ip = "localhost";

        try {

            this.s = new Socket(ip, port);
            System.out.println("socket port is " + s);
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String str = br.readLine();

            System.out.println("server Data: " + str);
            return str;
        } catch (Exception ex) {
            System.out.println("exception encountered is " + ex);
            return ex.getMessage().toString();
        }
    }

    public void sendData() {
        try {
            String str = "TEST 1 BUDDY \n";

            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
            PrintWriter out = new PrintWriter(os);
            out.write(str);
            out.flush();

            System.out.println("Client finshed");
        } catch (Exception ex) {
            System.out.println("exception encountered is " + ex);
        }
    }
//    public static void main(String[] args) {
//
//        String ip = "localhost";
//        int port = 8000;
//        try {
//            Socket s = new Socket(ip, port);
//
//            String str = "TEST 1 BUDDY";
//            OutputStreamWriter os = new OutputStreamWriter(s.getOutputStream());
//            PrintWriter out = new PrintWriter(os);
//            os.write(str);
//            os.flush();
//        } catch (Exception ex) {
//            System.out.println("exception encountered is " + ex);
//        }
//    }
}


