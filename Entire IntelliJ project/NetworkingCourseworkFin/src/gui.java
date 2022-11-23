import javax.swing.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

public class gui {
    public gui(){

        JFrame frame = new JFrame("PM Messaging");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(200,200);

        JButton mButton = new JButton("New Message");
        frame.getContentPane().add(mButton);

        mButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                //composeMessageGui cmp = new composeMessageGui("PM");
            }
        });

        frame.setVisible(true);


    }
}
