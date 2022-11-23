import java.awt.image.BufferedImage;
import java.io.*;
import java.net.*;
import javax.imageio.ImageIO;
import javax.swing.JOptionPane;
import java.util.*;

public class client extends Thread{

    private Scanner scan = new Scanner(System.in);
    private int version;
    private String name;
    private String peerName;
    private messageBoard mb;

    private String IP;
    private int port;

    private boolean socketclosed = false;
    private Socket sock;
    private boolean con = false;
    private String lastData = "";
    private String currentTime = "0";

    private Vector<String> currentCriteria;
    private String currentRequest = "";


    private int incoming = 0;
    private boolean Complete = false;
    private message lastImage;
    private Vector<String> ImageData;
    private int toReceive;
    private String lastRequest = "";


    public client(String ip, int p,String us,int v,String n, messageBoard m){
       IP = ip; port = p; version = v; name=n; mb = m;
    }

    public client(int v, messageBoard m){
        version = v; mb =m;
    }

    public void populateData(int p,String us,String ip){
        port = p; name = us; IP = ip;
    }

    public void run(){

        connect();
    }

    public void connect(){
        while(!con && !socketclosed){
            System.out.println("Trying to connect");
            try{

                sock = new Socket(IP,port);
                System.out.println("Connected to Laptop");

                //Protocol Checking
                String pro = "PROTOCOL? "+version+" "+name;
                sendData(pro);
                DataInputStream in= new DataInputStream(sock.getInputStream());


                boolean versionConfirmed = false;
                //checking deadlock

                while(!versionConfirmed){
                    in= new DataInputStream(sock.getInputStream());
                    lastData = in.readUTF();
                    lastData.trim();

                    if(lastData.startsWith("PROTOCOL?")){
                        String temp = lastData.substring(10);
                        temp = temp.replaceAll("\\s.*", "");
                        System.out.println(temp);

                        if(Integer.parseInt(temp) >= version){
                            versionConfirmed = true;
                            System.out.println("Peer version verified, connection established");
                            peerName = lastData.substring(temp.length()+11);
                            System.out.println(peerName);
                        }
                        else{
                            System.out.println("Peer is out of date, cannot verify");
                            sock.close();
                            break;
                        }}}


                while(sock.isConnected()) {
                    con = true;
                    //Logic loop
                     in= new DataInputStream(sock.getInputStream());

//YES
                    lastData = in.readUTF();
                    lastData.trim();



                    if(incoming > 0 && !Complete){
                        incoming--;
                        ImageData.add(lastData);
                        if(incoming == 0){
                            Complete = true;
                            String image = "";
                            for(String s:ImageData){

                                image += s;

                            }


                            image = image.replace("\n", "");
                            System.out.println(image);
                            lastImage.addImage(image);
                        }
                    }

                    else{

                    if(lastData.startsWith("NOW")){
                        currentTime = lastData.substring(4);

                        if(currentRequest.equals("request message")){
                            List(currentCriteria.size()-1);
                        }


                    }

                    else if(lastData.startsWith("GET?")){
                        String id = lastData.substring(13).trim();
                        System.out.println("ID TO GET: "+id);
                        message mess = mb.getMessageByID(id);
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

                    else if(lastData.startsWith("MESSAGE")){

                        Vector<String> messageIDs = new Vector<String>();
                        String s[] = lastData.split("\\r?\\n");
                        Collections.addAll(messageIDs,s);
                        messageIDs.remove(0);

                        for(String id:messageIDs){
                            get(id);
                        }
                    }

                    else if(lastData.startsWith("LIST?")){
                        String temp = lastData.substring(6);
                        temp =temp.replaceAll("\\s.*", "");

                        Vector<String> headers = new Vector<String>();
                        String s[] = lastData.split("\\r?\\n");
                        Collections.addAll(headers,s);

                        Vector<String> ids = mb.getMessageByFilter(headers);
                        String messageNum = "MESSAGES "+ids.size() +"\n";

                        for(String id: ids){ messageNum += id+"\n"; }
                        sendData(messageNum);
                        System.out.println(headers);

                    }

                    else if(lastData.contentEquals("TIME?")){
                        String time ="NOW " + String.valueOf((System.currentTimeMillis() /1000));
                        sendData(time);
                    }

                    else if(lastData.startsWith("FOUND")){
                        Vector<String> mess = new Vector<String>();
                        String s[] = lastData.split("\\r?\\n");
                        Collections.addAll(mess,s);
                        mess.remove(0);
                        message m = new message(mess);

                        if(mess.lastElement().startsWith("Image:")){
                             incoming =Integer.parseInt(mess.lastElement().substring(6).trim());
                             ImageData = new Vector<String>();
                             lastImage = m;
                             Complete = false;
                        }

                        mb.addMessage(m);
                    }

                    else if(lastData.startsWith("BYE!")){
                        socketclosed = true;
                        sock.close();
                    }

                    }


                    System.out.println(lastData);
                }
                sock.close();
                con = false;

            } catch (Exception e){con = false;}

        }
    }


    public boolean isConn(){return sock.isConnected();}

    public void time(){
        sendData("TIME?");

    }

    public void get(String id){
        String idString = "GET? SHA-256 "+id;
        sendData(idString);
    }

    public void List(int Contents){

        String listString = "LIST? "+currentCriteria.elementAt(0) + " " + Contents;
        currentCriteria.remove(0);
        for(String s:currentCriteria){
            listString += "\n"+s;
        }
            sendData(listString);
    }

    public void requestMessage(Vector<String> criteria) throws InterruptedException {
        currentRequest = "request message";
        currentCriteria = criteria;

        String tempTime = currentTime;
        time();
    }

    public void sendData(String data){
        if(isConn()){
        try {
            DataOutputStream out = new DataOutputStream(sock.getOutputStream());
            out.writeUTF(data);
            out.flush();
            System.out.println("SENT: "+data);
        } catch (IOException e) { e.printStackTrace();}

        }

    }}
