import javax.imageio.ImageIO;
import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

public final class utilityTraben {
        private utilityTraben() {
            throw new UnsupportedOperationException();
        }

        public static void message(JFrame frame,String text){
            //System.out.println("error: " + text);
            JOptionPane.showMessageDialog(frame,
                text,
                "message",
                JOptionPane.PLAIN_MESSAGE);


        }


        public static void displayImageWindow(String imagePath, String windowTitle,int width, int height) {
            JFrame bob = new JFrame();
            bob.setTitle(windowTitle);
            JLabel bob2 = new JLabel();
            ImageIcon back;
            try {
                //BufferedImage bob3 = d(new File("assets/render_help.png"));
                BufferedImage bob3 = ImageIO.read(utilityTraben.class.getResource(imagePath));
                bob3 = resizeImage(bob3,450,700,false);
                back = new ImageIcon(bob3);
            }catch (NullPointerException e2  ){
                back = new ImageIcon(utilityTraben.class.getResource("assets/ayy.jpg"));
                System.out.println("null");
            }catch (IOException b){
                back = new ImageIcon(utilityTraben.class.getResource("assets/ayy.jpg"));
            }
            bob2.setIcon(back);
            bob.add(bob2);
            bob.setSize(width,height); //pixel size of frame in width then height
            bob.setPreferredSize(new Dimension(width,height));
            //bob.setMaximumSize(new Dimension(450,700));
            bob2.setSize(width,height);
            bob.setVisible(true);
        }
        public static BufferedImage getImage(String imagePath,int width, int height){
            return getImage(imagePath,width,height, false);
        }

        public static BufferedImage getImage(String imagePath,int width, int height, boolean fadeImage){
                BufferedImage bob3;
                    try {
                        //BufferedImage bob3 = d(new File("assets/render_help.png"));
                        bob3 = ImageIO.read(utilityTraben.class.getResource(imagePath));
                    }catch (NullPointerException e2  ){
                        System.out.println("Image utility error fghjg");
                       bob3 = new BufferedImage(1,1,1);

                    }catch (IOException b){
                        System.out.println("Image utility error fh,jjf,hj,bn");
                        bob3 = new BufferedImage(1,1,1);
                    }catch (IllegalArgumentException arg){
                        System.out.println("Image utility errofgjghjghjgj,bn");
                        bob3 = new BufferedImage(1,1,1);
                    }
                    bob3 = resizeImage(bob3,width,height,fadeImage);
                    return bob3;
        }

    public static BufferedImage resizeImage(BufferedImage img, int newW, int newH,boolean fadeImage) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);

        if (fadeImage){
            g2d.setPaint ( new Color ( 200,221,242,200 ) );
            g2d.fillRect ( 0, 0, newW, newH );
        }

        g2d.dispose();

        return dimg;
    }

    public static boolean fileContains(File file, String searchString){
            boolean toRet = false;
        try{
            Scanner input = new Scanner(file);
            while (input.hasNextLine()) {
                if (input.nextLine().contains(searchString)){
                    toRet =true;
                    break;
                }
            }
            input.close();
        }catch(IOException i){
            return false;
        }
        return toRet;
    }

    public static class fogFileFilter  extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            if (f.getName().matches("biomes_client.json") ||
                    f.getName().matches("manifest.json")) {
                return false;
            }
            if (
                    f.getName().matches("src") ||
                            f.getName().matches("out")) {
                return false;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("json")
                    //  || extension.equals("json")
                ) {
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "fog definition file \".json\"";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }

    public static class biomeClientFilter  extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) {
 /*              for (String str:
                    f.list()) {
                    if (str.equals("biomes_client.json") ||
                            str.equals("manifest.json")) {

                    }

                }*/
                return true;
            }
            if (f.getName().equals("biomes_client.json") ||
                    f.getName().equals("manifest.json")) {
                return true;
            }
/*            if (
                    f.getName().matches("src") ||
                            f.getName().matches("out")) {
                return false;
            }*/

/*            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("json")
                    //  || extension.equals("json")
                ) {
                    return true;
                } else {
                    return false;
                }
            }*/

            return false;
        }

        @Override
        public String getDescription() {
            return "fog definition file \".json\"";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
    public static class mcpackFilter  extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) {
 /*              for (String str:
                    f.list()) {
                    if (str.equals("biomes_client.json") ||
                            str.equals("manifest.json")) {

                    }

                }*/
                return true;
            }
            if (f.getName().endsWith(".mcpack")) {
                return true;
            }
/*            if (
                    f.getName().matches("src") ||
                            f.getName().matches("out")) {
                return false;
            }*/

/*            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("json")
                    //  || extension.equals("json")
                ) {
                    return true;
                } else {
                    return false;
                }
            }*/

            return false;
        }

        @Override
        public String getDescription() {
            return "Minecraft Pack (.mcpack)";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 && i < s.length() - 1) {
                ext = s.substring(i + 1).toLowerCase();
            }
            return ext;
        }
    }
}

