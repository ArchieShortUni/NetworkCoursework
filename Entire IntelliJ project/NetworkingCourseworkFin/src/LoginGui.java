import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class LoginGui extends JFrame{

    private String userName;
    private String port;
    private String ip;
    private JPanel LoginGUI;
    private JButton hostButton;
    private JButton connectButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JFrame f = this;

    private client cl;
    private server sv;
    private messageBoard ms;
    public LoginGui(String appName, client c, server s, messageBoard m){
        super(appName);
        this.setSize(800,600);
        cl = c; sv = s; ms = m;

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(LoginGUI);


        this.pack();
        connectButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField1.getText().equals("") || textField2.getText().equals("") || textField3.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "All information is needed to host and connect", "Need details", JOptionPane.PLAIN_MESSAGE);
                }
                else{
                    try{
                        int pt = Integer.parseInt(textField2.getText().trim());

                        sv.populateInfo(20111,textField1.getText().trim());
                        sv.start();
                        cl.populateData(pt,textField1.getText().trim(),textField3.getText().trim());
                        cl.start();
                        JFrame frame = new messageBoardGui("Polite Messaging",ms,cl,false);
                        frame.setSize(2000,600);
                        frame.setVisible(true);
                        f.dispose();


                    } catch (NumberFormatException numberFormatException) { JOptionPane.showMessageDialog(null, "Must be a number", "Invalid format", JOptionPane.PLAIN_MESSAGE); }
                }

            }
        });
        hostButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if(textField1.getText().equals("")){
                    JOptionPane.showMessageDialog(null, "The username is needed to host", "Need name", JOptionPane.PLAIN_MESSAGE);

                }
                else{
                    sv.populateInfo(20111,textField1.getText().trim());
                    sv.start();
                    JFrame frame = new messageBoardGui("Polite Messaging",ms,cl,true);
                    frame.setVisible(true);
                    f.dispose();
                }
            }
        });
    }


    public String getUserName(){return userName;}

    public String getPort(){return port;}

    public String getIp(){return ip;}



}
