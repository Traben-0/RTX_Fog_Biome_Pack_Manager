import javax.swing.*;
import java.awt.*;

public class help {
    private JTabbedPane tabbedPane1;
    private JTextPane blahBlahBlahTextPane;
    private JPanel cont;


    help(int tabIndex){
        try {
            tabbedPane1.setSelectedIndex(tabIndex);
        }catch(Exception e){
            //
        }
        JFrame frame = new JFrame();

        frame.setContentPane(cont);
        frame.setTitle("Help & Information");

        frame.setSize(500,700); //pixel size of frame in width then height
        frame.setPreferredSize(new Dimension(500,700));
        frame.setMaximumSize(new Dimension(500,700));
        frame.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
        frame.pack();
        frame.setVisible(true);
    }


}
