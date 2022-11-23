import javax.swing.*;


import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;

public class messageReceiveGui extends JFrame {

    private client cl;


    private JPanel receiverPanel;
    private JButton getMessageButton;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JTextField topic;
    private JTextField dateTime;
    private JCheckBox fromCheckBox;
    private JCheckBox toCheckBox;
    private JCheckBox subjectCheckBox;
    private JCheckBox topicCheckBox;
    private Frame f = this;


    public messageReceiveGui(String appName, client c) {
        super(appName);
        this.setSize(1400, 600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setContentPane(receiverPanel);

        cl = c;
        SimpleDateFormat formatter= new SimpleDateFormat("yyyy-MM-dd HH:mm");
        Date date = new Date(System.currentTimeMillis() - 86400000);

        dateTime.setText(formatter.format(date));

        this.pack();
        getMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> toSearch = new Vector<String>();

                if (dateValidate(dateTime.getText())) {
                    String tempTime = dateTime.getText()+":00";
                    SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date date = null;
                    try {
                        date = format.parse(tempTime);
                    } catch (ParseException parseException) {
                        parseException.printStackTrace();
                    }

                    long timestamp = date.getTime();
                    toSearch.add(String.valueOf(timestamp/1000));
                    if(fromCheckBox.isSelected()){ toSearch.add("From: "+from.getText());}
                    if(toCheckBox.isSelected()){toSearch.add("To: "+to.getText());}
                    if(subjectCheckBox.isSelected()){toSearch.add("Subject: "+subject.getText());}
                    if(topicCheckBox.isSelected()){toSearch.add("Topic: "+topic.getText());}



                    try {
                        cl.requestMessage(toSearch);
                    } catch (InterruptedException interruptedException) {
                        interruptedException.printStackTrace();
                    }

                    f.dispose();

                } else {
                   JOptionPane.showMessageDialog(null, "This is the wrong date / time format try entering again", "Wrong Format", JOptionPane.PLAIN_MESSAGE);
                }

            }
        });
    }

    private static boolean dateValidate(String inputDate) {
        try {
            String[] datePattern = {"yyyy-MM-dd HH:mm", "yyyy-MM-dd"};
            for (String pattern : datePattern) {
                SimpleDateFormat sdf = new SimpleDateFormat(pattern);
                Date date = sdf.parse(inputDate);
                String formattedDate = sdf.format(date);
                if (inputDate.equals(formattedDate)) {
                    return true; }}
        } catch (ParseException ex) {
            return false;
        }
        return false; }

}
