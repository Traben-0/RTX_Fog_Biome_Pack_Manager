import com.sun.org.apache.xpath.internal.operations.Bool;

import javax.swing.*;
import javax.swing.Timer;
import java.awt.*;
import java.awt.event.*;
import java.io.*;
import java.net.URI;
import java.net.URL;
import java.nio.file.*;
import java.util.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import java.util.zip.ZipOutputStream;


public class BiomeMain {

    private static int currentVersion = 13;


    private static JMenuItem saveMenu;
    private static JMenuItem exportPackMenu;
    static JFrame frame;
    private static Manager manager = new Manager();
    //private static JPanel contain;
    private static ArrayList<fogFile> fogFiles = new ArrayList<>();
    private static launcher launch;
    static File activeDirectory;
    private static String biomeFileName = "biomes_client.json";
    private static JMenuBar menuBar;

    static JLabel saveMessage;

    private static JPanel contain;
    private static ArrayList<biome> biomes;
    private static ArrayList<String> copyableBiomeFogs;
    static File orignalZipFile;



    //private static String[] bNameList = {"default","plains""sunflower_plains""desert""extreme_hills"};



    public static void main(String[] args) {
        //Main bob = new Main("editor","", true,false);

        //https://raw.githubusercontent.com/btrab1/RTX_Fog_Biome_Pack_Manager/main/versionCheckFile


        start(args);
    }

    private static void versionCheck(String str, int thisVersion){
        try {
            URL url = new URL(str);
            //Retrieving the contents of the specified page
            Scanner sc = new Scanner(url.openStream());
            //Instantiating the StringBuffer class to hold the result
            StringBuffer sb = new StringBuffer();
            while (sc.hasNext()) {
                sb.append(sc.next());
                //System.out.println(sc.next());
            }
            //Retrieving the String from the String Buffer object
            String result = sb.toString();
            System.out.println("am version "+thisVersion);
            System.out.println("found version "+result);
            if (thisVersion < Integer.parseInt(result)){
                //utilityTraben.message(frame,"An update is available at\n ( https://github.com/btrab1/RTX_Fog_Biome_Pack_Manager )");
                int resulto = JOptionPane.showConfirmDialog(frame, "An update is available! \n\nWould you like to download?", "Update available",
                        JOptionPane.YES_NO_OPTION,
                        JOptionPane.QUESTION_MESSAGE);
                if (resulto == JOptionPane.YES_OPTION) {
                    Desktop desktop = Desktop.isDesktopSupported() ? Desktop.getDesktop() : null;
                    if (desktop != null && desktop.isSupported(Desktop.Action.BROWSE)) {
                        try {
                            desktop.browse(new URI("https://github.com/btrab1/RTX_Fog_Biome_Pack_Manager"));
                        } catch (Exception k) {
                            k.printStackTrace();
                        }
                    }
                    closingF();
                } else if (resulto == JOptionPane.NO_OPTION) {
                    //label.setText("You selected: No");
                } else {
                    //label.setText("None selected");
                }


            }else{
                System.out.println("up to date");
            }
            //Removing the HTML tags
            //result = result.replaceAll("<[^>]*>", "");
            //System.out.println("Contents of the web page: " + result);
        }catch(Exception e){
            System.out.println("version check failed");
        }
    }

    private static void start(String[] args){
        frame = new JFrame();
        //frame.setContentPane(distancePage.distance_content);
        frame.setTitle("Biome & Fog Manager BETA");
        initMenu();
        frame.setJMenuBar(menuBar);

        //frame.setMaximumSize(new Dimension(600,700));
        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);



        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (saveMessage.isVisible()) {
                    int result = JOptionPane.showConfirmDialog(frame, "Unsaved changes detected!\n(you can save the biomes in 'File/Save')\n \n Exit without saving?", "Exit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        closingF();
                    } else if (result == JOptionPane.NO_OPTION) {
                        //label.setText("You selected: No");
                    } else {
                        //label.setText("None selected");
                    }
                }else{
                    closingF();
                }

            }
        });

        startLauncher(false);

        render();
        frame.setVisible(true);
        versionCheck("https://raw.githubusercontent.com/btrab1/RTX_Fog_Biome_Pack_Manager/main/versionCheckFile", currentVersion);
        if (launch.exitASAP) exitLauncher();
    }
    static void closingF(){
        System.out.println("exiting");
        try {
            if (activeDirectory.getName().equals("temp_manager_mcpack_folder")) {
                deleteDirectoryStream(activeDirectory.toPath());
            }
        }catch(IOException i){
            System.out.println("not deleted");
        }catch(Exception p){
            //ignore
        }
        frame.dispose();
        System.exit(0);
    }

    static void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }

    private static void initMenu(){
        menuBar = new JMenuBar();
        JMenu menu = new JMenu("File");
        JMenu editorM = new JMenu("Fog editor");
        JMenuItem editor = new JMenuItem("Open editor");
        JMenuItem editorFF = new JMenuItem("Open with file...");
        JMenuItem importExisting = new JMenuItem("Copy from Existing");
        JMenuItem  newManageLocation= new JMenuItem("New");
        JMenuItem  newManageLocationOpen= new JMenuItem("Open");
        JMenuItem helpM = new JMenuItem("Help");

        saveMessage = new JLabel("* Unsaved changes (go to File/Save)");
        saveMessage.setForeground(new Color(180,0,0));


        saveMenu = new JMenuItem("Save Biome Changes");
        exportPackMenu = new JMenuItem("Export as .mcpack");


        menu.add(newManageLocation);
        menu.add(newManageLocationOpen);
        menu.add(new JSeparator());
        menu.add(importExisting);
        menu.add(new JSeparator());
        menu.add(saveMenu);
        menu.add(new JSeparator());
        menu.add(exportPackMenu);
        //menu.add(editor);
        editorM.add(editor);
        editorM.add(editorFF);

        //JSeparator menuSep = new JSeparator(1);
        JLabel sep = new JLabel("|");
        sep.setEnabled(false);

        menuBar.add(menu);
        //menuBar.add(menuSep);
        menuBar.add(sep);
        menuBar.add(editorM);

        menuBar.add(sep);
        menuBar.add(helpM);
        menuBar.add(sep);
        helpM.setMaximumSize(new Dimension(40,25));
        //frame.setJMenuBar(menuBar);

        menuBar.add(saveMessage);
        saveMessage.setVisible(false);

        helpM.addActionListener(e -> {new help(0);});

        editor.addActionListener(e -> new Main("editor","","",false,false));
        editorFF.addActionListener(explorer);
        //Main(String titleAppend, String filePath, String id, boolean closeFullProgramOnExit,boolean isManaged_Etc_UserCantName)
        newManageLocation.addActionListener(e -> startLauncher(true));
        newManageLocationOpen.addActionListener(e -> startLauncher(true));

        saveMenu.addActionListener(e -> saveBiomeFile(new File(activeDirectory+"\\biomes_client.json")));
        exportPackMenu.addActionListener(e -> {
            //make it a zip first

                final JFileChooser chooser = new JFileChooser(activeDirectory.getParent());
                chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
                chooser.addChoosableFileFilter(new utilityTraben.mcpackFilter());
                chooser.setDialogTitle("Export as .mcpack");
                chooser.setAcceptAllFileFilterUsed(false);

                int returnVal = chooser.showSaveDialog(frame);

                if (returnVal == JFileChooser.APPROVE_OPTION) {
                    //This is where a real application would open the file.
                    //log.append("Opening: " + file.getName() + "." + newline);
                    File f;
                    System.out.println(chooser.getSelectedFile().getAbsolutePath());
                    if(chooser.getSelectedFile().isDirectory()){
                        f=new File(chooser.getSelectedFile().getAbsolutePath()+"/Manager_Output_Pack.mcpack");
                    }else if (chooser.getSelectedFile().getAbsolutePath().endsWith(".mcpack")) {
                        f = chooser.getSelectedFile();
                    }else{
                        f = new File(chooser.getSelectedFile().getAbsolutePath()+".mcpack");
                    }

                    orignalZipFile = f;
                    saveBiomeFile(new File(activeDirectory+"\\biomes_client.json"));
                    orignalZipFile = null;
                    saveMessage.setVisible(false);

                } else {
                    //log.append("Open command cancelled by user." + newline);
                    //utilityTraben.message(frame, "ukfghfghnown error");
                }
            });



            //return to non zip



        importExisting.addActionListener(explorerImportCopy);
    }

    static ActionListener explorer = e -> {
        //System.out.println(System.getProperty("user.dir"));
        final JFileChooser chooser = new JFileChooser(BiomeMain.activeDirectory.getAbsolutePath()+"\\fogs");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new utilityTraben.fogFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
            String path = chooser.getSelectedFile().getAbsolutePath();
            fogFile ff = new fogFile(path);
            String idstr = ff.ID;
            new Main("editor",path,idstr,false,false);
        } else {
            //log.append("Open command cancelled by user." + newline);
        }
    };
    static ActionListener explorerImportCopy = e -> {
        //System.out.println(System.getProperty("user.dir"));
        final JFileChooser chooser = new JFileChooser(BiomeMain.activeDirectory.getParent());
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new utilityTraben.biomeClientFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            File f = chooser.getSelectedFile();
            if (f.isDirectory()){
                f=new File(f.getAbsolutePath()+"\\biomes_client.json");
                if (f.exists()) importBiomesFile(f);
            }else{
                //is file
                importBiomesFile(f);
            }


        } else {
            //log.append("Open command cancelled by user." + newline);
        }
    };

    private static void startLauncher(boolean ignoreRemember){


        launch = new launcher(ignoreRemember);
        menuBar.getComponent(0).setEnabled(false);
        manager.biomesListArea.removeAll();
        frame.setContentPane(launch.launcherPanel);
        frame.setTitle("Opening Manager...");
        frame.setSize(400,270); //pixel size of frame in width then height
        frame.setPreferredSize(new Dimension(400,270));
        frame.setResizable(false);
        render();
    }

    private static Timer toptimer = new Timer(10, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            toptimer.stop();
            manager.scrllpane.getViewport().setViewPosition(new Point(0,0));
            BiomeMain.saveMessage.setVisible(false);
        }
    });

    static void exitLauncher(){
        activeDirectory = launch.getChoice();
        if (!activeDirectory.isDirectory()){
            biomeFileName = activeDirectory.getName();
            activeDirectory = activeDirectory.getParentFile();

        }
        if (launch.isZip){
            orignalZipFile = launch.zipFile;
            saveMenu.setEnabled(false);
            saveMessage.setText("* Unsaved changes (go to File/Export)");
        }else{
            orignalZipFile = null;
            saveMessage.setText("* Unsaved changes (go to File/Save)");
        }

        frame.setContentPane(manager.ManagerContent);
        manager.scrllpane.getVerticalScrollBar().setUnitIncrement(20);
        frame.setTitle("Biome & Fog Manager BETA: ("+activeDirectory+'/'+biomeFileName+')');
        menuBar.getComponent(0).setEnabled(true);
        //launch = null;
        System.out.println(activeDirectory.getName());
        startBiomeManager();
        JButton toTop = new JButton("To Top /\\");
        toTop.addActionListener(e -> manager.scrllpane.getViewport().setViewPosition(new Point(0,0)));

        toTop.setSize(new Dimension(100,50));
        toTop.setMaximumSize(new Dimension(100,50));
        contain.add(toTop);
        toptimer.start();
    }

    private static void startBiomeManager(){

        frame.setTitle("Biome & Fog Manager BETA");
        frame.setSize(650,700); //pixel size of frame in width then height
        frame.setPreferredSize(new Dimension(650,700));
        frame.setResizable(true);

        System.out.println(activeDirectory.getAbsolutePath());


        contain = new JPanel();
        contain.setLayout(new GridLayout(0,2));
        manager.biomesListArea.add(contain);
        /////////MAKE ALL BIOMES DO FINAL SETUP NOW THAT THEY ARE ALL LOADED
        loadBiomeManager();


        frame.addComponentListener(new ComponentListener() {
            @Override
            public void componentResized(ComponentEvent e) {
                Dimension d = frame.getSize();
                //manager.ManagerContent.setSize(d);
                int columns =  d.width/325;
                if (columns == 0) columns = 1;
                contain.setLayout(new GridLayout(0,columns));
                //render();
            }
            @Override
            public void componentMoved(ComponentEvent e) {
            }
            @Override
            public void componentShown(ComponentEvent e) {
            }
            @Override
            public void componentHidden(ComponentEvent e) {
            }
        });

        render();


    }

    private static void findFogs(){
        try {
            Path dir = FileSystems.getDefault().getPath(activeDirectory+"/fogs/");
            Files.createDirectory(Files.createDirectory(dir));
            System.out.println("Fogs Folder Created");
            fogFiles = getFogs();
        }catch (FileAlreadyExistsException e){
            System.out.println("Fogs Folder Located");
            fogFiles = getFogs();
        }catch(IOException e){
//
        }

    }
    private static ArrayList<fogFile> getFogs(){
        ArrayList<fogFile>  fogList = new ArrayList<>();
        File f = new File(activeDirectory+"\\fogs\\");
        if (f.exists()){
        String[] fileNames = f.list();
        if (fileNames.length ==0){
            try {
                File newD = new File(activeDirectory + "\\fogs\\default.fogManager.json");
                FileWriter out = new FileWriter(newD);
                out.write(defaultFog);
                out.close();
                fileNames = f.list();
            }catch(Exception e){}
        }
        for (String file: fileNames){
            if (file.endsWith(".json")) {
                System.out.println("found fogfile: "+file);
                fogList.add(new fogFile(activeDirectory+"\\fogs\\"+file));
            }
        }
            return fogList;
        }else{
            System.out.println("no fogs");
        }

        return new ArrayList<>();
    }
    private static String holdName;
    private static File holdDirectory;
    private static void importBiomesFile(File newF){
        if (!newF.getParent().equals(activeDirectory.getAbsolutePath())) {
            holdName = biomeFileName;
            holdDirectory = new File(activeDirectory.getAbsolutePath());
            biomeFileName = newF.getName();
            activeDirectory = new File(newF.getParent());
            System.out.println("DEBUG: \n hold: "+holdDirectory.toPath()+"\n active: "+activeDirectory.toPath());
            //reload with

            ArrayList<fogFile> fogs = getFogs();
            loadCopyBiomeData(newF, fogs);
        }else{
            utilityTraben.message(frame, "cannot select own path");
        }

        saveMessage.setVisible(true);

    }
    private static void doImport(ArrayList<biome> finalListToSend){
        if (!finalListToSend.isEmpty()) {


            for (biome bCopy:
                 finalListToSend) {
                for (biome bOriginal:
                     biomes) {
                    if (bCopy.getName().equals(bOriginal.getName())){
                        try {
                            File to = new File(holdDirectory + "\\fogs\\" + bCopy.getName()+".fogManager.json");
                            File from = new File(activeDirectory + "\\fogs\\" + bCopy.getFileName());


                            System.out.println(to.getAbsolutePath() +" to <> from"+ from.getAbsolutePath());
                            if (from.exists()){
                                Files.copy(from.toPath(), to.toPath(), StandardCopyOption.REPLACE_EXISTING);

                                utilityTraben.message(frame,"copied file to manager as "+bCopy.getName()+".fogManager.json");
                            }else{
                                utilityTraben.message(frame,"copy of "+bCopy.getName()+" failed: file doesn't exist");
                            }



                        }catch(IOException i){
                            utilityTraben.message(frame,"copy of "+bCopy.getName()+" failed: ");
                        }

                        bOriginal.initializeAsCopy(bCopy.getName(),
                                "managed:"+bCopy.getName()+"_managed",
                                bCopy.getName()+".fogManager.json",
                                true,
                                false,
                                String.format("#%02X%02X%02X",
                                        bCopy.waterColour.getRed(),
                                        bCopy.waterColour.getGreen(),
                                        bCopy.waterColour.getBlue()),
                                bCopy.transparency,
                                bCopy.copyingOtherFog);
                        //System.out.println("changed reprint: \n"+bOriginal.printSelf());

                        render();
                    }
                }



            }


            //finish
            System.out.println("finish c=directory reset");
            biomeFileName = holdName;
            activeDirectory = new File(holdDirectory.getAbsolutePath());

            //after any biomes change
           // for (biome b : biomes) {
             //   b.receiveAllList(biomes);
            //}
            render();
        }else{
            utilityTraben.message(frame,"No biomes copied");
            biomeFileName = holdName;
            activeDirectory = new File(holdDirectory.getAbsolutePath());
        }
    }

    private static void loadCopyBiomeData(File toLoad,ArrayList<fogFile> fogs){
        ArrayList<biome> copyableBiomes = new ArrayList<>();
        JPanel copyContain = new JPanel();
        try{
            //get default
            Scanner input = new Scanner(toLoad);
            String line;
            while (input.hasNextLine()){
                line = input.nextLine();
                if (line.contains("default")){
                    oneBiomeRead("default", input, copyableBiomes,fogs);
                    break;
                }
            }

            //now full read and ignore default
            loopBiomes(input,copyableBiomes,fogs);
            input.close();

            promptCopyList(copyableBiomes );
            //here should have list of copyable biomes so prompt


        }catch(IOException i){
            //
        }

    }
    private static void promptCopyList(ArrayList<biome> copyableBiomesList){
        ArrayList<biome> finalListToSend = new ArrayList<>();
        JFrame prompt = new JFrame();
        JPanel cont = new JPanel();
        JPanel contcont = new JPanel();

        prompt.setSize(650,700); //pixel size of frame in width then height
        prompt.setPreferredSize(new Dimension(650,700));
        prompt.setResizable(true);
        //cont.setLayout(new GridLayout(0,1));
        //cont.setLayout(new GridBagLayout());
        //GridBagConstraints gbc = new GridBagConstraints();
        cont.setLayout(new BoxLayout(cont, BoxLayout.Y_AXIS));

        contcont.setLayout(new GridLayout(0,3));
        prompt.setContentPane(cont);

        prompt.setTitle("Select biomes to copy");

        if (!copyableBiomesList.isEmpty()) {
            JLabel bob = new JLabel("Copyable biomes found:");
            bob.setMaximumSize(new Dimension(400,50));
            cont.add(bob,Component.CENTER_ALIGNMENT);
            for (biome b :
                    copyableBiomesList) {
                JCheckBox bbox = new JCheckBox("Copy " + b.getName() + "?");
                bbox.addActionListener(e -> {
                    if ( finalListToSend.contains(b)){
                        finalListToSend.remove(b);
                    }else {
                        finalListToSend.add(b);
                    }
                });
                contcont.add(bbox);
            }
            cont.add(contcont);
            JButton button = new JButton("Copy Selected biomes to manager");
            button.setMaximumSize(new Dimension(400,50));

            button.addActionListener(e -> {
                doImport(finalListToSend);
                prompt.dispose();
                saveMessage.setVisible(true);
            });
            cont.add(button,Component.CENTER_ALIGNMENT);
        }else{
            cont.add( new JLabel("NO copyable biomes found there!"));
            utilityTraben.message(frame,"NO copyable biomes found there!");
            //biomeFileName = holdName;
            //activeDirectory = new File( holdDirectory.getAbsolutePath());
            prompt.dispatchEvent(new WindowEvent(frame, WindowEvent.WINDOW_CLOSING));
        }

        prompt.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        prompt.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent event) {
                System.out.println("prompt closed");
                biomeFileName = holdName;
                activeDirectory = new File( holdDirectory.getAbsolutePath());
                prompt.dispose();
            }
        });


        prompt.setVisible(true);
        prompt.pack();
    }



    private static void loadBiomeData(File toLoad,ArrayList<fogFile> fogs){
        biomes = new ArrayList<>();
        contain.removeAll();
        try{
            //get default
            Scanner input = new Scanner(toLoad);
            String line;
            while (input.hasNextLine()){
                line = input.nextLine();
                if (line.contains("default")){
                    oneBiomeRead("default", input ,biomes,fogs);
                    break;
                }
            }

            //now full read and ignore default
            loopBiomes(input,biomes,fogs);

            makeNotInFileBiomes();

            fogFiles = new ArrayList<>( fogs);
            input.close();

            for (biome b:biomes) {
                b.receiveAllList(biomes);
            }
            //BiomeMain.saveMessage.setVisible(false);
        }catch(IOException i){
            //
        }
        for (biome b: biomes){
            contain.add(b.display());
        }
        render();
    }

    private static void makeNotInFileBiomes() {

        for (String str : allBiomeNames) {
            Boolean isNeeded = true;
            for (biome b : biomes) {
                if (b.getName().equals(str)){
                    isNeeded = false;
                    break;
                }
            }
            if (isNeeded) biomes.add(new biome(str, biomes.get(0), biomes));
        }
    }

    private static void loopBiomes(Scanner input, ArrayList<biome> biomeList, ArrayList<fogFile> fogs){
        String line;
        while(input.hasNextLine()){
            line = input.nextLine();
            line = clearedline(line, "");
            if (line.contains("{") && !line.contains("default")){
                //biome name
                line = line.replace("{","");
                oneBiomeRead(line, input, biomeList,fogs);
            }
            if (line.contains("}")){
                break;
            }
        }
    }

    public static void oneBiomeRead(String biomeName, Scanner input, ArrayList<biome> biomeList, ArrayList<fogFile> fogs) {
        String line;
        String fogIdentifier="";
        String fogFileName="";
        String wColor="";
        boolean is_default_fog = false;
        boolean is_custom = false;
        double wTransparent = 4.0;

        while  (input.hasNextLine()){
            line = input.nextLine();
                if (line.contains("water_surface_color")){
                    wColor = clearedline(line, "water_surface_color");
                }
            if (line.contains("water_surface_transparency")){
                wTransparent = Double.parseDouble(clearedline(line, "water_surface_transparency"));
            }
            if (line.contains("fog_identifier")){
                fogIdentifier = clearedline(line, "fog_identifier");
                for (fogFile f : fogs){
                    if (f.ID.equals(fogIdentifier))fogFileName = f.fileName;
                }
            }



            if (line.contains("}")){
                if (biomeName.equals("default")){
                    is_default_fog = true;
                    is_custom = !fogFileName.equals("default.fogManager.json") && !fogIdentifier.equals("managed:default_managed");
                    biomeList.add(new biome(biomeName,fogIdentifier,fogFileName,is_custom,is_default_fog,wColor,wTransparent,new biome(true),false));

                }else{
                    is_default_fog = (fogIdentifier.equals(biomeList.get(0).getFogID()) || fogIdentifier.equals(""));

                    //not self
                    boolean otherFogCustom = (isManagedFog(fogFileName, fogIdentifier)  && !fogIdentifier.equals("managed:"+biomeName+"_managed"));

                    if (is_default_fog){
                        is_custom = false;
                    }else{
                        //if is default custom name   and is not own biomes custom name
                        otherFogCustom = (fogIdentifier.contains("_managed") && !fogIdentifier.equals("managed:"+biomeName+"_managed"));
                        is_custom = (!isManagedFog(fogFileName, fogIdentifier) && !otherFogCustom);
                    }

                    biomeList.add(new biome(biomeName,fogIdentifier,fogFileName,is_custom,is_default_fog,wColor,wTransparent,biomeList.get(0),otherFogCustom));
                }
                break;
            }
        }
    }

    private static boolean isManagedFog(String nameToTest, String idToTest){
        for (String name:managedBiomeNames){
            if (nameToTest.equals(name))return true;
        }
        for (String name:allManagedIds){
            if (idToTest.equals(name))return true;
        }
        return false;
    }

    private static String clearedline(String str, String remove){
        str = str.replace(",","");
        str = str.replace(" ","");
        str = str.replace("\"","");
        str = str.replaceFirst(":","");
        str = str.replace(remove,"");

        return str;
    }

    private static void loadBiomeFile(){
        File f = new File(activeDirectory+"\\"+biomeFileName);
        if (!f.exists() || f.isDirectory()) {
            System.out.println("creating biomesfile");
            createNewBiomeFile();

        }
        loadBiomeData(f,fogFiles);
    }

    private static void loadBiomeManager(){
        findFogs();
        loadBiomeFile();
    }
    private static void createNewBiomeFile(){
        try {
            File newFile = new File(activeDirectory+"\\"+biomeFileName);
            FileWriter output = new FileWriter(newFile);
            output.write("/*\n" +
                    " - Made by fog & biome manager BETA\n" +
                    " - made by @Traben in the minecraft RTX discord\n" +
                    "  */\n" +
                    "{\n" +
                    "  \"biomes\": {\n" +
                    "    \"default\": {\n" +
                    "      \"water_surface_color\": \"#44AFF5\",\n" +
                    "      \"water_surface_transparency\": 0.65,\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"plains\": {\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"sunflower_plains\": {\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"desert\": {\n" +
                    "      \"water_surface_color\": \"#32A598\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"extreme_hills\": {\n" +
                    "      \"water_surface_color\": \"#007BF7\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"forest\": {\n" +
                    "      \"water_surface_color\": \"#1E97F2\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"flower_forest\": {\n" +
                    "      \"water_surface_color\": \"#20A3CC\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"taiga\": {\n" +
                    "      \"water_surface_color\": \"#287082\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"taiga_mutated\": {\n" +
                    "      \"water_surface_color\": \"#1E6B82\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"swampland\": {\n" +
                    "      \"water_surface_color\": \"#4C6559\",\n" +
                    "      \"water_surface_transparency\": 1.0,\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"swampland_mutated\": {\n" +
                    "      \"water_surface_color\": \"#4C6156\",\n" +
                    "      \"water_surface_transparency\": 1.0,\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"river\": {\n" +
                    "      \"water_surface_color\": \"#0084FF\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"hell\": {\n" +
                    "      \"water_surface_color\": \"#905957\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"the_end\": {\n" +
                    "      \"water_surface_color\": \"#62529E\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"frozen_river\": {\n" +
                    "      \"water_surface_color\": \"#185390\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"ice_plains\": {\n" +
                    "      \"water_surface_color\": \"#14559B\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"ice_plains_spikes\": {\n" +
                    "      \"water_surface_color\": \"#14559B\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"ice_mountains\": {\n" +
                    "      \"water_surface_color\": \"#1156A7\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mushroom_island\": {\n" +
                    "      \"water_surface_color\": \"#8A8997\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mushroom_island_shore\": {\n" +
                    "      \"water_surface_color\": \"#818193\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"beach\": {\n" +
                    "      \"water_surface_color\": \"#157CAB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"desert_hills\": {\n" +
                    "      \"water_surface_color\": \"#1A7AA1\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"forest_hills\": {\n" +
                    "      \"water_surface_color\": \"#056BD1\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"taiga_hills\": {\n" +
                    "      \"water_surface_color\": \"#236583\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"extreme_hills_edge\": {\n" +
                    "      \"water_surface_color\": \"#045CD5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"jungle\": {\n" +
                    "      \"water_surface_color\": \"#14A2C5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"bamboo_jungle\": {\n" +
                    "      \"water_surface_color\": \"#14A2C5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"jungle_mutated\": {\n" +
                    "      \"water_surface_color\": \"#1B9ED8\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"jungle_hills\": {\n" +
                    "      \"water_surface_color\": \"#1B9ED8\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"bamboo_jungle_hills\": {\n" +
                    "      \"water_surface_color\": \"#1B9ED8\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"jungle_edge\": {\n" +
                    "      \"water_surface_color\": \"#0D8AE3\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"stone_beach\": {\n" +
                    "      \"water_surface_color\": \"#0D67BB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"cold_beach\": {\n" +
                    "      \"water_surface_color\": \"#1463A5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"birch_forest\": {\n" +
                    "      \"water_surface_color\": \"#0677CE\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"birch_forest_hills\": {\n" +
                    "      \"water_surface_color\": \"#0A74C4\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"roofed_forest\": {\n" +
                    "      \"water_surface_color\": \"#3B6CD1\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"cold_taiga\": {\n" +
                    "      \"water_surface_color\": \"#205E83\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"cold_taiga_mutated\": {\n" +
                    "      \"water_surface_color\": \"#205E83\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"cold_taiga_hills\": {\n" +
                    "      \"water_surface_color\": \"#245B78\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mega_taiga\": {\n" +
                    "      \"water_surface_color\": \"#2D6D77\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mega_spruce_taiga\": {\n" +
                    "      \"water_surface_color\": \"#2D6D77\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mega_taiga_mutated\": {\n" +
                    "      \"water_surface_color\": \"#2D6D77\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mega_spruce_taiga_mutated\": {\n" +
                    "      \"water_surface_color\": \"#2D6D77\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mega_taiga_hills\": {\n" +
                    "      \"water_surface_color\": \"#286378\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"extreme_hills_plus_trees\": {\n" +
                    "      \"water_surface_color\": \"#0E63AB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"extreme_hills_plus_trees_mutated\": {\n" +
                    "      \"water_surface_color\": \"#0E63AB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"extreme_hills_mutated\": {\n" +
                    "      \"water_surface_color\": \"#0E63AB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"savanna\": {\n" +
                    "      \"water_surface_color\": \"#2C8B9C\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"savanna_plateau\": {\n" +
                    "      \"water_surface_color\": \"#2590A8\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"savanna_mutated\": {\n" +
                    "      \"water_surface_color\": \"#2590A8\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mesa\": {\n" +
                    "      \"water_surface_color\": \"#4E7F81\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mesa_bryce\": {\n" +
                    "      \"water_surface_color\": \"#497F99\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mesa_mutated\": {\n" +
                    "      \"water_surface_color\": \"#497F99\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mesa_plateau_stone\": {\n" +
                    "      \"water_surface_color\": \"#55809E\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"mesa_plateau\": {\n" +
                    "      \"water_surface_color\": \"#55809E\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"ocean\": {\n" +
                    "      \"water_surface_color\": \"#1787D4\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"deep_ocean\": {\n" +
                    "      \"water_surface_color\": \"#1787D4\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"warm_ocean\": {\n" +
                    "      \"water_surface_color\": \"#02B0E5\",\n" +
                    "      \"water_surface_transparency\": 0.55,\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"deep_warm_ocean\": {\n" +
                    "      \"water_surface_color\": \"#02B0E5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"lukewarm_ocean\": {\n" +
                    "      \"water_surface_color\": \"#0D96DB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"deep_lukewarm_ocean\": {\n" +
                    "      \"water_surface_color\": \"#0D96DB\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"cold_ocean\": {\n" +
                    "      \"water_surface_color\": \"#2080C9\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"deep_cold_ocean\": {\n" +
                    "      \"water_surface_color\": \"#2080C9\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"frozen_ocean\": {\n" +
                    "      \"water_surface_color\": \"#2570B5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"deep_frozen_ocean\": {\n" +
                    "      \"water_surface_color\": \"#2570B5\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"warped_forest\": {\n" +
                    "      \"water_surface_color\": \"#905957\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"crimson_forest\": {\n" +
                    "      \"water_surface_color\": \"#905957\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"soulsand_valley\": {\n" +
                    "      \"water_surface_color\": \"#905957\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    },\n" +
                    "    \"basalt_deltas\": {\n" +
                    "      \"water_surface_color\": \"#3F76E4\",\n" +
                    "      \"fog_identifier\": \"managed:default_managed\"\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            output.close();
            }catch(IOException e){
            //
        }

    }



    static void saveBiomeFile(File f){

        saveMessage.setVisible(false);
        try {
            if (f.exists()) System.out.println("biomes_client_overwritten: " + f.delete());
            FileWriter output = new FileWriter(f);
            output.write("/*\n" +
                    " - Made by fog & biome manager BETA\n" +
                    " - made by @Traben in the minecraft RTX discord\n" +
                    "  */\n" +
                    "{\n" +
                    "  \"biomes\": {\n");
            boolean comma = false;
            for (biome b:
                 biomes) {
                if (comma)output.write(",\n");
                output.write(b.printSelf());
                comma=true;
            }

            output.write("\n" +
                    "  }\n" +
                    "}");
            output.close();
            System.out.println("saved");




            if (orignalZipFile !=null){

                System.out.println("zip append");
                /*   addFilesToZip(orignalZipFile, files, "");*/
                File dir = new File(f.getParent());


                List<File> fileList = new ArrayList<File>();
                System.out.println("---Getting references to all files in: " + dir.getCanonicalPath());
                getAllFiles(dir, fileList);
                System.out.println("---Creating zip file");
                writeZipFile(dir, fileList);
                System.out.println("---Done");
                utilityTraben.message(frame,"Saved to: "+orignalZipFile.getAbsolutePath());
            }else{
                utilityTraben.message(frame,"Saved to: "+activeDirectory);
            }

        }catch(Exception e){
            System.out.println("couldn't save because "+e.toString());
        }
    }


    //    filePath can be "" to be ignored



    private static void render(){
        frame.pack();
    }
    private static final String[] allBiomeNames = {"default",
            "plains",
            "sunflower_plains",
            "desert",
            "extreme_hills",
            "forest",
            "flower_forest",
            "taiga",
            "taiga_mutated",
            "swampland",
            "swampland_mutated",
            "river",
            "hell",
            "the_end",
            "frozen_river",
            "ice_plains",
            "ice_plains_spikes",
            "ice_mountains",
            "mushroom_island",
            "mushroom_island_shore",
            "beach",
            "desert_hills",
            "forest_hills",
            "taiga_hills",
            "extreme_hills_edge",
            "jungle",
            "bamboo_jungle",
            "jungle_mutated",
            "jungle_hills",
            "bamboo_jungle_hills",
            "jungle_edge",
            "stone_beach",
            "cold_beach",
            "birch_forest",
            "birch_forest_hills",
            "roofed_forest",
            "cold_taiga",
            "cold_taiga_mutated",
            "cold_taiga_hills",
            "mega_taiga",
            "mega_spruce_taiga",
            "mega_taiga_mutated",
            "mega_spruce_taiga_mutated",
            "mega_taiga_hills",
            "extreme_hills_plus_trees",
            "extreme_hills_plus_trees_mutated",
            "extreme_hills_mutated",
            "savanna",
            "savanna_plateau",
            "savanna_mutated",
            "mesa",
            "mesa_bryce",
            "mesa_mutated",
            "mesa_plateau_stone",
            "mesa_plateau",
            "ocean",
            "deep_ocean",
            "warm_ocean",
            "deep_warm_ocean",
            "lukewarm_ocean",
            "deep_lukewarm_ocean",
            "cold_ocean",
            "deep_cold_ocean",
            "frozen_ocean",
            "deep_frozen_ocean",
            "warped_forest",
            "crimson_forest",
            "soulsand_valley",
            "basalt_deltas"
    };
    static final String[] managedBiomeNames = {"default.fogManager.json",
            "plains.fogManager.json",
            "sunflower_plains.fogManager.json",
            "desert.fogManager.json",
            "extreme_hills.fogManager.json",
            "forest.fogManager.json",
            "flower_forest.fogManager.json",
            "taiga.fogManager.json",
            "taiga_mutated.fogManager.json",
            "swampland.fogManager.json",
            "swampland_mutated.fogManager.json",
            "river.fogManager.json",
            "hell.fogManager.json",
            "the_end.fogManager.json",
            "frozen_river.fogManager.json",
            "ice_plains.fogManager.json",
            "ice_plains_spikes.fogManager.json",
            "ice_mountains.fogManager.json",
            "mushroom_island.fogManager.json",
            "mushroom_island_shore.fogManager.json",
            "beach.fogManager.json",
            "desert_hills.fogManager.json",
            "forest_hills.fogManager.json",
            "taiga_hills.fogManager.json",
            "extreme_hills_edge.fogManager.json",
            "jungle.fogManager.json",
            "bamboo_jungle.fogManager.json",
            "jungle_mutated.fogManager.json",
            "jungle_hills.fogManager.json",
            "bamboo_jungle_hills.fogManager.json",
            "jungle_edge.fogManager.json",
            "stone_beach.fogManager.json",
            "cold_beach.fogManager.json",
            "birch_forest.fogManager.json",
            "birch_forest_hills.fogManager.json",
            "roofed_forest.fogManager.json",
            "cold_taiga.fogManager.json",
            "cold_taiga_mutated.fogManager.json",
            "cold_taiga_hills.fogManager.json",
            "mega_taiga.fogManager.json",
            "mega_spruce_taiga.fogManager.json",
            "mega_taiga_mutated.fogManager.json",
            "mega_spruce_taiga_mutated.fogManager.json",
            "mega_taiga_hills.fogManager.json",
            "extreme_hills_plus_trees.fogManager.json",
            "extreme_hills_plus_trees_mutated.fogManager.json",
            "extreme_hills_mutated.fogManager.json",
            "savanna.fogManager.json",
            "savanna_plateau.fogManager.json",
            "savanna_mutated.fogManager.json",
            "mesa.fogManager.json",
            "mesa_bryce.fogManager.json",
            "mesa_mutated.fogManager.json",
            "mesa_plateau_stone.fogManager.json",
            "mesa_plateau.fogManager.json",
            "ocean.fogManager.json",
            "deep_ocean.fogManager.json",
            "warm_ocean.fogManager.json",
            "deep_warm_ocean.fogManager.json",
            "lukewarm_ocean.fogManager.json",
            "deep_lukewarm_ocean.fogManager.json",
            "cold_ocean.fogManager.json",
            "deep_cold_ocean.fogManager.json",
            "frozen_ocean.fogManager.json",
            "deep_frozen_ocean.fogManager.json",
            "warped_forest.fogManager.json",
            "crimson_forest.fogManager.json",
            "soulsand_valley.fogManager.json",
            "basalt_deltas.fogManager.json"
    };
    private static final String[] allManagedIds = {"managed:default_managed",
            "managed:plains_managed",
            "managed:sunflower_plains_managed",
            "managed:desert_managed",
            "managed:extreme_hills_managed",
            "managed:forest_managed",
            "managed:flower_forest_managed",
            "managed:taiga_managed",
            "managed:taiga_mutated_managed",
            "managed:swampland_managed",
            "managed:swampland_mutated_managed",
            "managed:river_managed",
            "managed:hell_managed",
            "managed:the_end_managed",
            "managed:frozen_river_managed",
            "managed:ice_plains_managed",
            "managed:ice_plains_spikes_managed",
            "managed:ice_mountains_managed",
            "managed:mushroom_island_managed",
            "managed:mushroom_island_shore_managed",
            "managed:beach_managed",
            "managed:desert_hills_managed",
            "managed:forest_hills_managed",
            "managed:taiga_hills_managed",
            "managed:extreme_hills_edge_managed",
            "managed:jungle_managed",
            "managed:bamboo_jungle_managed",
            "managed:jungle_mutated_managed",
            "managed:jungle_hills_managed",
            "managed:bamboo_jungle_hills_managed",
            "managed:jungle_edge_managed",
            "managed:stone_beach_managed",
            "managed:cold_beach_managed",
            "managed:birch_forest_managed",
            "managed:birch_forest_hills_managed",
            "managed:roofed_forest_managed",
            "managed:cold_taiga_managed",
            "managed:cold_taiga_mutated_managed",
            "managed:cold_taiga_hills_managed",
            "managed:mega_taiga_managed",
            "managed:mega_spruce_taiga_managed",
            "managed:mega_taiga_mutated_managed",
            "managed:mega_spruce_taiga_mutated_managed",
            "managed:mega_taiga_hills_managed",
            "managed:extreme_hills_plus_trees_managed",
            "managed:extreme_hills_plus_trees_mutated_managed",
            "managed:extreme_hills_mutated_managed",
            "managed:savanna_managed",
            "managed:savanna_plateau_managed",
            "managed:savanna_mutated_managed",
            "managed:mesa_managed",
            "managed:mesa_bryce_managed",
            "managed:mesa_mutated_managed",
            "managed:mesa_plateau_stone_managed",
            "managed:mesa_plateau_managed",
            "managed:ocean_managed",
            "managed:deep_ocean_managed",
            "managed:warm_ocean_managed",
            "managed:deep_warm_ocean_managed",
            "managed:lukewarm_ocean_managed",
            "managed:deep_lukewarm_ocean_managed",
            "managed:cold_ocean_managed",
            "managed:deep_cold_ocean_managed",
            "managed:frozen_ocean_managed",
            "managed:deep_frozen_ocean_managed",
            "managed:warped_forest_managed",
            "managed:crimson_forest_managed",
            "managed:soulsand_valley_managed",
            "managed:basalt_deltas_managed"
    };

    private static final String defaultFog = "/*\n" +
            " - Fog Definition Made by Biome & Fog Manager's Fog Editor\n" +
            " - Coded by @Traben from the Minecraft RTX Discord\n" +
            "  */\n" +"{\n" +
            "  \"format_version\": \"1.16.100\",\n" +
            "  \"minecraft:fog_settings\": {\n" +
            "    \"description\": {\n" +
            "      \"identifier\": \"managed:default_managed\"\n" +
            "    },\n" +
            "    \"distance\": {\n" +
            "      \"air\": {\n" +
            "        \"fog_start\": 0.92,\n" +
            "        \"fog_end\": 1.0,\n" +
            "        \"fog_color\": \"#ABD2FF\",\n" +
            "        \"render_distance_type\": \"render\"\n" +
            "      },\n" +
            "      \"water\": {\n" +
            "        \"fog_start\": 0.92,\n" +
            "        \"fog_end\": 1.0,\n" +
            "        \"fog_color\": \"#44AFF5\",\n" +
            "        \"render_distance_type\": \"render\"\n" +
            "      },\n" +
            "      \"weather\": {\n" +
            "        \"fog_start\": 0.23,\n" +
            "        \"fog_end\": 0.7,\n" +
            "        \"fog_color\": \"#666666\",\n" +
            "        \"render_distance_type\": \"render\"\n" +
            "      },\n" +
            "      \"lava\": {\n" +
            "        \"fog_start\": 0.0,\n" +
            "        \"fog_end\": 0.64,\n" +
            "        \"fog_color\": \"#991A00\",\n" +
            "        \"render_distance_type\": \"fixed\"\n" +
            "      },\n" +
            "      \"lava_resistance\": {\n" +
            "        \"fog_start\": 2.0,\n" +
            "        \"fog_end\": 4.0,\n" +
            "        \"fog_color\": \"#991A00\",\n" +
            "        \"render_distance_type\": \"fixed\"\n" +
            "      }\n" +
            "    },\n" +
            "    \"volumetric\": {\n" +
            "      \"density\": {\n" +
            "        \"air\": {\n" +
            "          \"max_density\": 0.25,\n" +
            "          \"max_density_height\": 50,\n" +
            "          \"zero_density_height\": 128\n" +
            "        },\n" +
            "\t\t\"water\": {\n" +
            "          \"max_density\": 0.80,\n" +
            "          \"uniform\": true\n" +
            "        },\n" +
            "\t\t\"lava\": {\n" +
            "          \"max_density\": 0.25,\n" +
            "          \"max_density_height\": 50,\n" +
            "          \"zero_density_height\": 128\n" +
            "        },\n" +
            "\t\t\"lava_resistance\": {\n" +
            "          \"max_density\": 0.25,\n" +
            "          \"uniform\": true\n" +
            "        }\n" +
            "      },\n" +
            "      \"media_coefficients\": {\n" +
            "        \"air\": {\n" +
            "          \"scattering\": [ 0.02, 0.02, 0.02 ],\n" +
            "          \"absorption\": [ 0.0, 0.0, 0.0 ]\n" +
            "        },\n" +
            "        \"water\": {\n" +
            "          \"scattering\": [ 0.0012, 0.002, 0.003 ],\n" +
            "          \"absorption\": [ 0.16, 0.102, 0.102 ]\n" +
            "        },\n" +
            "        \"cloud\": {\n" +
            "          \"scattering\": [ 0.2, 0.2, 0.2 ],\n" +
            "          \"absorption\": [ 0.0, 0.0, 0.0 ]\n" +
            "        }\n" +
            "      }\n" +
            "    }\n" +
            "  }\n" +
            "}";

    public static void getAllFiles(File dir, List<File> fileList) {
        try {
            File[] files = dir.listFiles();
            for (File file : files) {
                fileList.add(file);
                if (file.isDirectory()) {
                    System.out.println("directory:" + file.getCanonicalPath());
                    getAllFiles(file, fileList);
                } else {
                    System.out.println("     file:" + file.getCanonicalPath());
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void writeZipFile(File directoryToZip, List<File> fileList) {

        try {
            FileOutputStream fos = new FileOutputStream(orignalZipFile);
            ZipOutputStream zos = new ZipOutputStream(fos);

            for (File file : fileList) {
                if (!file.isDirectory()) { // we only zip files, not directories
                    addToZip(directoryToZip, file, zos);
                }
            }

            zos.close();
            fos.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void addToZip(File directoryToZip, File file, ZipOutputStream zos) throws FileNotFoundException,
            IOException {

        FileInputStream fis = new FileInputStream(file);

        // we want the zipEntry's path to be a relative path that is relative
        // to the directory being zipped, so chop off the rest of the path
        //String zipFilePath = orignalZipFile.getParent()+"/[Managed]"+orignalZipFile.getName();

       String zipFilePath = file.getCanonicalPath().substring(directoryToZip.getCanonicalPath().length() + 1,
                file.getCanonicalPath().length());

        System.out.println("Writing '" + zipFilePath + "' to zip file");
        ZipEntry zipEntry = new ZipEntry(zipFilePath);
        zos.putNextEntry(zipEntry);

        byte[] bytes = new byte[1024];
        int length;
        while ((length = fis.read(bytes)) >= 0) {
            zos.write(bytes, 0, length);
        }

        zos.closeEntry();
        fis.close();
    }
}
