
import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Vector;

public class viewMessageGui extends  JFrame{
    private JTextArea messagebody;
    private JTextField messageid;
    private JTextField timesent;
    private JTextField from;
    private JTextField to;
    private JTextField subject;
    private JTextField topic;
    private JPanel panel;
    private JLabel Image;

    public viewMessageGui(String Appname,message m) throws IOException {
        super(Appname);
        this.setSize(1400,600);
        this.setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        this.setContentPane(panel);
        Image.setText("No Image");

        messageid.setText(m.getId());
        timesent.setText(m.getTime());
        from.setText(m.getSender());
        to.setText(m.getReceiver());
        subject.setText(m.getSubject());
        topic.setText(m.getTopic());

        String tempMess = "";
        Vector<String> body = m.getMessage();
        for(String s: body){tempMess += s+"\n";}

        if(m.isImage()){

            byte[] btDataFile = Base64.getDecoder().decode(m.getImage());
            BufferedImage image = ImageIO.read(new ByteArrayInputStream(btDataFile));
            File outputfile = new File("MessageImage.png");
            ImageIO.write(image, "png", outputfile);
            Image.setIcon(rescaleImage(outputfile,500,500));
            Image.setText("");
        }

        messagebody.setText(tempMess);
        this.pack();

    }

    public  BufferedImage toBufferedImage(byte[] bytes)
            throws IOException {

        InputStream is = new ByteArrayInputStream(bytes);
        BufferedImage bi = ImageIO.read(is);
        return bi;

    }

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
