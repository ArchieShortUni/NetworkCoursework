import javax.imageio.ImageIO;
import java.io.*;
import java.net.*;
import java.util.*;

public class server extends Thread {

    private Scanner scan = new Scanner(System.in);
    private boolean con = false;
    private boolean socketclosed = false;
    private int port;
    private String lastData = "";
    private int version;
    private String name;

    private messageBoard ms;
    private int toReceive;
    private String lastRequest = "";
    private Vector<String> currentFilter = new Vector<String>();

    public server(int p, int v,String n,messageBoard m) throws IOException {port= p; version = v; name = n; ms=m;}
    public server(int v,messageBoard m) throws IOException{version = v; ms=m;}

    public void populateInfo(int p, String n){port= p; name = n;}

    private Socket sock;

    public void run(){
        serverReceive();
    }

    public void sendData(String data){
       // System.out.println("ATTEMPTING TO SEND: "+data);
        if(isConn()){
            try {
                DataOutputStream out = new DataOutputStream(sock.getOutputStream());
                out.writeUTF(data);
                out.flush();
                System.out.println("SENT: "+data);
            } catch (IOException e) { e.printStackTrace();}

        }
    }
    public boolean isConn(){return sock.isConnected();}

    public void serverReceive(){
        while(!con && !socketclosed){
        try {
            ServerSocket server = new ServerSocket(port);
            sock = server.accept();

            DataOutputStream out =new DataOutputStream(sock.getOutputStream());
            System.out.println("Connected to by PC");

            while(sock.isConnected()){
            con = true;

            DataInputStream in= new DataInputStream(sock.getInputStream());
            lastData = in.readUTF();
            lastData.trim();
            System.out.println(lastData);


            //return all IDs
                if(lastData.startsWith("LIST?")){
                String temp = lastData.substring(6);
                temp =temp.replaceAll("\\s.*", "");

                Vector<String> headers = new Vector<String>();
                String s[] = lastData.split("\\r?\\n");
                Collections.addAll(headers,s);

                Vector<String> ids = ms.getMessageByFilter(headers);
                String messageNum = "MESSAGES "+ids.size() +"\n";

                for(String id: ids){ messageNum += id+"\n"; }
                sendData(messageNum);
                System.out.println(headers);

            }

                else if(lastData.startsWith("GET?")){
                    String id = lastData.substring(13).trim();
                    System.out.println("ID TO GET: "+id);
                    message mess = ms.getMessageByID(id);
                    String toSend = "FOUND\n";

                    if(!(mess == null)){
                        toSend += mess.toSend();

                        if(mess.isImage()){
                            List<String> parts =mess.getParts( 1000);
                            toSend += "Image: "+parts.size() +"\n";
                            sendData(toSend);

                            for(String s:parts){
                                System.out.println(s);
                                sendData(s);
                            } }
                        else{ sendData(toSend); }}
                    else{
                        sendData("SORRY");
                    }
                }

                else if(lastData.startsWith("PROTOCOL?")){
                String pro = "PROTOCOL? " +version +" "+name;
                sendData(pro);
            }

            else if(lastData.contentEquals("TIME?")){
                String time ="NOW " + String.valueOf((System.currentTimeMillis() /1000));
                sendData(time);
            }


                else if(lastData.startsWith("BYE!")){
                    socketclosed = true;
                    sock.close();
                }

            }
            sock.close();
            con=false;

        } catch(Exception e){con = false;}
    }
    }
}
