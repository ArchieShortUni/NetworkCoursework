import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.*;
import java.util.List;

public class message {
    //must haves
    //always the first
    private String id;
    private String time;
    private String sender;

    //last header
    private int contents;

    private String receiver = "";
    private String subject = "";
    private String topic = "";
    private Vector<String> message;
    private boolean hasImage = false;
    private String image;

    public message(Vector<String> messageV){
        message = new Vector<String>();
        int index = 0;
        for(String str : messageV){
           String temp =str.replaceAll("\\s.*", "");

            if(temp.equals("Message-id:")){id =str.substring(19).trim();}
            else if(temp.equals("Time-sent:")){time = str.substring(10).trim();}

            else if(temp.equals("From:")){sender = str.substring(5).trim();}
            else if(temp.equals("To:")){receiver = str.substring(3).trim();}
            else if(temp.equals("Subject:")){subject = str.substring(8).trim();}
            else if(temp.equals("Topic:")){topic = str.substring(6).trim();}
            else if(temp.equals("Contents:")){
                contents = Integer.parseInt(str.substring(9).trim());
                 int count = 1;
                 while(count != contents+1){
                     message.add(messageV.elementAt(index+count));
                     count++;
                 }
            }
            else if(temp.equals("Image:")){hasImage = true; }




            index++;
        }




    }

    public  List<String> getParts( int partitionSize) {

        List<String> parts = new ArrayList<String>();
        int len = image.length();
        for (int i=0; i<len; i+=partitionSize)
        {
            parts.add(image.substring(i, Math.min(len, i + partitionSize)));
        }
        return parts;

    }
    public void addImage(String i){
        hasImage = true;
        image = i;

    }

    public message(String i, String t, String s, Vector<String> m, Vector<String> op){
        id = i; time = t; sender = s; message = m;
        if(!op.elementAt(0).equals("")){ receiver = op.elementAt(0); }
        if(!op.elementAt(1).equals("")){ subject = op.elementAt(1); }
        if(!op.elementAt(2).equals("")){ topic = op.elementAt(2);}
        contents = message.size();

    }

    public message(Vector<String> messageInfo, Vector<String> mes) {

        time = String.valueOf((System.currentTimeMillis() /1000));
        sender = messageInfo.elementAt(0);

        if(!messageInfo.elementAt(1).equals("")){ receiver = messageInfo.elementAt(1); }
        if(!messageInfo.elementAt(2).equals("")){ subject = messageInfo.elementAt(2); }
        if(!messageInfo.elementAt(3).equals("")){ topic = messageInfo.elementAt(3);}

        message = mes;
        contents = message.size();

        //creating ID
        String toHash = time + " "+ sender + " "+ receiver+" "+subject+" "+topic+" "+contents;
        for(String line: message){toHash += " " +line;}
        MessageDigest digest = null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        byte[] hash = digest.digest(toHash.getBytes(StandardCharsets.UTF_8));
        String encoded = bytesToHex(hash);

        id = encoded;
    }


    private static String bytesToHex(byte[] hash) {
        StringBuilder hexString = new StringBuilder(2 * hash.length);
        for (int i = 0; i < hash.length; i++) {
            String hex = Integer.toHexString(0xff & hash[i]);
            if(hex.length() == 1) {
                hexString.append('0');
            }
            hexString.append(hex);
        }
        return hexString.toString();
    }

    public Vector<String> tableData(){
        Vector<String> forTable = new Vector<String>();
        forTable.add(sender);

        if(!receiver.equals("")){forTable.add(receiver);} else{forTable.add("");}

        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(Long.parseLong(time)*1000);

        forTable.add(formatter.format(date));
        forTable.add(subject);
        if(hasImage){forTable.add("yes");}else{forTable.add("no");}

        return forTable;
    }

    public String getTime(){return time;}
    public String getSender(){return sender;}
    public String getReceiver(){return receiver;}
    public String getSubject(){return subject;}
    public String getTopic(){return  topic; }
    public String getId(){return id;}
    public Vector<String> getMessage(){return message;}
    public boolean isImage(){return  hasImage;}
    public String getImage(){return image;}

    public String toSend(){
        String composed = "";
        composed += "Message-id: SHA-256 " + id +"\n";
        composed += "Time-sent: "+time+"\n";
        composed += "From: "+sender+"\n";
        if(!receiver.equals("")){composed+= "To: "+receiver+"\n";}
        if(!subject.equals("")){composed+= "Subject: "+subject+"\n";}
        if(!topic.equals("")){composed+= "Topic: "+topic+"\n";}
        composed += "Contents: "+contents+"\n";
        for(String line:message ){
            composed += line + "\n";
        }

        return composed;
    }
}
