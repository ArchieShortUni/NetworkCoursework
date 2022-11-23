import javax.swing.*;
import javax.swing.table.DefaultTableColumnModel;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.TableCellRenderer;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.io.IOException;
import java.util.Vector;

public class messageBoardGui extends JFrame{
    private JPanel messageBoardPannel;
    private JTable table1;
    private JButton composeMessageButton;
    private JButton refreshButton;
    private JButton requestMessagesButton;
    private client cl;
    private messageBoard messages;
    private messageBoardGui self;

    public messageBoardGui(String appName, messageBoard ms,client c,boolean onlyHost){
        super(appName);
        this.setSize(1400,600);
        this.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        this.setContentPane(messageBoardPannel);

        cl = c; messages = ms;

        String[] colNames = new String[]{"From","To","Time","Subject","Has image"};
        DefaultTableModel tableModel = new DefaultTableModel(){
            //setting the jtable read only
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        tableModel.setColumnIdentifiers(colNames);

        table1.setModel(tableModel);
        table1.addMouseListener(new JTableButtonMouseListener(table1,messages));

        updateTable();
        if(onlyHost){
            requestMessagesButton.setVisible(false);
        }



        this.pack();

        composeMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new composeMessageGui("Polite Messaging",cl,ms);
                frame.setVisible(true);

            }
        });
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                updateTable();
            }
        });

        requestMessagesButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFrame frame = new messageReceiveGui("Polite Messaging",cl);
                frame.setVisible(true);
            }
        });
    }



    public void updateTable(){
        DefaultTableModel model = (DefaultTableModel) table1.getModel();
        model.setRowCount(0);
        for(message m: messages.getMessages()){
            model.addRow(m.tableData());
        }
    }
}


//All code below from https://www.javaer101.com/en/article/3868387.html

class ClientsTableRenderer extends JPanel implements TableCellRenderer {
    @Override
    public Component getTableCellRendererComponent(final JTable table, Object value, boolean isSelected, boolean hasFocus, int row, int column) {
        setBackground(Color.WHITE);
        if(column < 5) {
            JLabel label =  new JLabel(value.toString());
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,9));
            panel.setBackground(Color.WHITE);
            panel.add(label);
            this.add( panel);
        } else {

            JButton button = new JButton(value.toString());
            button.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent arg0) {
                    System.out.println("Clicked !");
                }
            });
            JPanel panel = new JPanel(new FlowLayout(FlowLayout.CENTER,0,3));
            panel.setBackground(Color.WHITE);
            panel.add(button);
            this.add(panel);
        }


        return this;
    }


}

class JTableButtonMouseListener extends MouseAdapter {
    private final JTable table;
    private messageBoard ms;
    public JTableButtonMouseListener(JTable table, messageBoard m) {
        this.table = table;
        this.ms = m;
    }

    @Override public void mouseClicked(MouseEvent e) {
        int column = table.getColumnModel().getColumnIndexAtX(e.getX());
        int row    = e.getY()/table.getRowHeight();
        //System.out.println("Col :"+column + "row:"+row);

        if (row < table.getRowCount() && row >= 0 && column < table.getColumnCount() && column >= 0) {
            Object value = table.getValueAt(row, column);
          //  System.out.println("Value :"+value.getClass().getName());
            JFrame frame = null;
            try {
                frame = new viewMessageGui("Polite Messaging",ms.getMessageByRow(row));
            } catch (IOException ioException) {
                ioException.printStackTrace();
            }
            frame.setSize(800,800);
            frame.setVisible(true);

            if (value instanceof JButton) {
                ((JButton)value).doClick();
            }

        }
    }
}