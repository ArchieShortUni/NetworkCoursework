import javax.swing.*;
import java.io.IOException;
import java.util.Arrays;
import java.util.Vector;
import javax.swing.JOptionPane;

public class main {
    private static int version = 1;
    private static boolean isTestSender = true;
    public static void main (String args[]) throws IOException {
       // client c = new client();

        messageBoard ms = new messageBoard();

        if(isTestSender) {
            //pre loaded messages for the sake of testing
            String[] mes1 = {"Hello,", "This is a test"};
            Vector<String> m1v = new Vector<String>(Arrays.asList(mes1));
            String[] mes1o = {"", "", ""};
            Vector<String> m1vo = new Vector<String>(Arrays.asList(mes1o));
            message m1 = new message("9F86D081884C7D659A2FEAA0C55AD015A3BF4F1B2B0B822CD15D6C15B0F00A08", "1619187789", "TestUser", m1v, m1vo);

            String[] mes2 = {"no,", "   ", "AHHHH"};
            Vector<String> m2v = new Vector<String>(Arrays.asList(mes2));
            String[] mes2o = {"person", "", ""};
            Vector<String> m2vo = new Vector<String>(Arrays.asList(mes2o));
            message m2 = new message("9F86D081884C7D659A2FEAKLC55AD015A3BF4F1B2B0B822CD15D6C15BFSASV", "1619187789", "TestUser", m2v, m2vo);

            String[] mes3 = {"Hello everyone!", "This is the first message sent using PM."};
            Vector<String> m3v = new Vector<String>(Arrays.asList(mes3));
            String[] mes3o = {"", "Hello!", "#announcements"};
            Vector<String> m3vo = new Vector<String>(Arrays.asList(mes3o));
            message m3 = new message("bc18ecb5316e029af586fdec9fd533f413b16652bafe079b23e021a6d8ed69aa", "1614686400", "martin.brain@city.ac.uk", m3v, m3vo);


            ms.addMessage(m1);
            ms.addMessage(m2);
            ms.addMessage(m3);
        }

        server s = new server(version,ms);
        client c = new client(version,ms);

        JFrame frame = new LoginGui("Polite Messaging",c,s,ms);
        frame.setSize(600,300);
        frame.setVisible(true);

    }
}
