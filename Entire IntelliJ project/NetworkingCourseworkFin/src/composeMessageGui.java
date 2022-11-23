import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.*;

public class composeMessageGui extends JFrame{
    private JPanel messagePanel;
    private JTextArea textArea1;
    private JButton composeMessageButton;
    private JTextField textField1;
    private JTextField textField2;
    private JTextField textField3;
    private JCheckBox includeImageCheckBox;

    private JButton chooseImageButton;
    private JLabel imageLabel;
    private client c;
    private Frame f = this;
    private File FileImage = null;

    public composeMessageGui(String appName,client cl, messageBoard ms){
        super(appName);
        this.setSize(800,600);

        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);

        this.setContentPane(messagePanel);
        c = cl;


        composeMessageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                Vector<String> messageInfo = new Vector<String>();
                Vector<String> message = new Vector<String>();

                String s[] = textArea1.getText().split("\\r?\\n");
                Collections.addAll(message,s);


                messageInfo.add("Computer-Archie.name");
                messageInfo.add(textField1.getText());
                messageInfo.add(textField2.getText());
                messageInfo.add(textField3.getText());
                //System.out.println(textArea1.getText());
               // messageInfo.add()
                message m = new message(messageInfo, message);

                if(includeImageCheckBox.isSelected()){
                    if(!(FileImage == null)){

                        imageLabel.setIcon(rescaleImage(FileImage,350,350));

                        BufferedImage bi = (BufferedImage) rescaleImage(FileImage,350,350).getImage();

                        File outputfile = new File("saved.png");
                        try {
                            ImageIO.write(bi, "png", outputfile);
                        } catch (IOException ioException) {
                            ioException.printStackTrace();
                        }

                        try {
                                FileInputStream fileInputStreamReader = new FileInputStream(outputfile);
                                byte[] bytes = new byte[(int)outputfile.length()];
                                fileInputStreamReader.read(bytes);
                                String im = new String(Base64.getEncoder().encode(bytes), "UTF-8");
                                 m.addImage(im);
                                 f.dispose();

                        } catch (IOException ioException) { ioException.printStackTrace();}
                    }}
                ms.addMessage(m);
                f.dispose();
            }
        });
        this.pack();
        chooseImageButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                File file;
                Scanner fileIn;
                int response;
                JFileChooser chooser = new JFileChooser(".");
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                response = chooser.showOpenDialog(null);

                if(response == JFileChooser.APPROVE_OPTION){
                    file = chooser.getSelectedFile();
                   String[] splitt = file.getAbsolutePath().split("\\.", 4);
                   Vector<String> vec = new Vector<String>();
                    vec.addAll(Arrays.asList(splitt));
                    System.out.println(vec.lastElement());
                    if(!vec.lastElement().equals("png")){

                        JOptionPane.showMessageDialog(null, "Only select a png file", "Must be a png", JOptionPane.PLAIN_MESSAGE);

                    }
                    else{
                        FileImage = file;
                        imageLabel.setIcon(rescaleImage(file,500,500));
                        imageLabel.setText("");

                    }
                }
            }
        });
    }

    public void addClient(client ct){c = ct;}

    public static byte[] toByteArray(BufferedImage bi, String format)
            throws IOException {

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        ImageIO.write(bi, format, baos);
        byte[] bytes = baos.toByteArray();
        return bytes;

    }

/// code written here to resize image https://stackoverflow.com/questions/6714045/how-to-resize-jlabel-imageicon

    public  ImageIcon rescaleImage(File source, int maxHeight, int maxWidth)
    {
        int newHeight = 0, newWidth = 0;        // Variables for the new height and width
        int priorHeight = 0, priorWidth = 0;
        BufferedImage image = null;
        ImageIcon sizeImage;

        try {
            image = ImageIO.read(source);        // get the image
        } catch (Exception e) {

            e.printStackTrace();
            System.out.println("Picture upload attempted & failed");
        }

        sizeImage = new ImageIcon(image);

        if(sizeImage != null)
        {
            priorHeight = sizeImage.getIconHeight();
            priorWidth = sizeImage.getIconWidth();
        }

        // Calculate the correct new height and width
        if((float)priorHeight/(float)priorWidth > (float)maxHeight/(float)maxWidth)
        {
            newHeight = maxHeight;
            newWidth = (int)(((float)priorWidth/(float)priorHeight)*(float)newHeight);
        }
        else
        {
            newWidth = maxWidth;
            newHeight = (int)(((float)priorHeight/(float)priorWidth)*(float)newWidth);
        }


        // Resize the image

        // 1. Create a new Buffered Image and Graphic2D object
        BufferedImage resizedImg = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_RGB);
        Graphics2D g2 = resizedImg.createGraphics();

        // 2. Use the Graphic object to draw a new image to the image in the buffer
        g2.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2.drawImage(image, 0, 0, newWidth, newHeight, null);
        g2.dispose();

        // 3. Convert the buffered image into an ImageIcon for return
        return (new ImageIcon(resizedImg));
    }

}