import com.sun.xml.internal.ws.util.StringUtils;

import javax.swing.*;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.ItemEvent;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Scanner;


public class biome {

        private String name = "...";
        public Color waterColour;
        private boolean hasColour;
        double transparency;
        private String fogID = "";

        private String fileName="";
        //true if using fog manager naming scheme
        //false if using custom filepath
        private boolean isUsingCustom;
        // whether to point to default biome's listing
        private boolean isUsingDefaultFog;
        private boolean isDefaultTrans;

        private File customFile;

        private JPanel thisBiomePanel;

        //refering to other biomes
        private biome copyFrom;
        private biome defaultBiome;
        boolean copyingOtherFog = false;
        ArrayList<biome> allBiomes;
        boolean isEnabled;

        private String copyIDBackup = "";



    private JTabbedPane tabbedPane1;
    private JCheckBox customCheck;
    private JCheckBox waterCheck;
    private JPanel customCont;
    private JTextField customText;

    private JLabel description;
    private JButton image;
    private JLabel descriptionW;
    private JLabel descriptionF;
    private JLabel descriptionA;
    private JSlider waterBlueSlider;
    private JSlider waterGreenSlider;
    private JSlider waterRedSlider;
    private JSpinner waterRed;
    private JSpinner waterGreen;
    private JSpinner waterBlue;
    private JSlider transparencySlider;
    private JSpinner transparencyField;
    private JButton fileButton;
    private JPanel waterCont;
    private JPanel fogCont;
    private JCheckBox biomeEnabled;
    private JPanel wPanel;
    private JButton openEditorManaged;
    private JComboBox<biome> copyableList;
    private JCheckBox transCheck;
    private JPanel transCont;
    private JCheckBox UseThisFogCheck;
    private JPanel colourDisplay;
    private JLabel orFogText;
    private JScrollPane scrollr;

    private boolean amDefault = false;

    //////DO NOT USE UNLESS IN DEFAULT INITIALISE////////////////////////////////////////////////////////////////
    public boolean YOUAREDEFAULT = false;
    biome(boolean confirm_only_used_for_default){
        System.out.println("default found");
        YOUAREDEFAULT =confirm_only_used_for_default;
    }
    //////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //biome(String biomeName, String fogIdentifier, String fogFileName, Boolean is_custom, Boolean is_default_fog, String wColor,Double wTranparent, biome default_biome) {
    //    initialize(biomeName, fogIdentifier, fogFileName, is_custom, is_default_fog, wColor,wTranparent, default_biome, "",null);
    //}
    biome(String biomeName,
          String fogIdentifier,
          String fogFileName,
          Boolean is_custom,
          Boolean is_default_fog,
          String wColor,
          Double wTransparent,
          biome default_biome,
          boolean iscopy){
        isEnabled = true;

        initialize(biomeName, fogIdentifier, fogFileName, is_custom, is_default_fog, wColor,wTransparent, default_biome,iscopy);

    }
    //not from file empty
    biome(String biomeName,
          biome default_biome,
          ArrayList<biome> allBiomes){


        customCheck.setEnabled(false);
        waterCheck.setEnabled(false);

        isEnabled = false;

        initialize(biomeName, default_biome.fogID, default_biome.fileName, false, true, "",0.0, default_biome, false);

    }
    void initializeAsCopy(String biomeName,
                    String fogIdentifier,
                    String fogFileName,
                    Boolean is_custom,
                    Boolean is_default_fog,
                    String wColor,
                    Double wTransparent,
                    boolean isCopy){
        isEnabled = true;
        name = "...";
        transparency = 4;
        fogID = fogIdentifier;
        fileName=fogFileName;
        //true if using fog manager naming scheme
        //false if using custom filepath
        isUsingCustom = false;

        copyingOtherFog = false;


        copyIDBackup = "";

        if (replaceFogId(new File(fileName),fogID)){
            System.out.println("copied id set");
        };
        initialize(biomeName, fogIdentifier, fogFileName, is_custom, is_default_fog, wColor,wTransparent, defaultBiome,isCopy);


    }

    private void initialize(String biomeName,
                            String fogIdentifier,
                            String fogFileName,
                            Boolean is_custom,
                            Boolean is_default_fog,
                            String wColor,
                            Double wTransparent,
                            biome default_biome,
                            boolean isCopy){
        copyingOtherFog = isCopy;
        if (copyingOtherFog){
            copyIDBackup = fogIdentifier;
        }
        setFogID(fogIdentifier);

        scrollr.getVerticalScrollBar().setUnitIncrement(50);

        if (image.getActionListeners().length==0)
            image.addActionListener(imagesAL);

        initTrans(wTransparent);

        setBiomeName(biomeName);

        setWaterColor(wColor);

        setFileName(fogFileName);
        setCustom(is_custom);
        setDefault(is_default_fog);
        setDisplay();
        setDescription();
        initListeners();
        setDefaultBiome(default_biome);
        //hideFogsIfCustom();
        UseThisFogCheck.setText("Use "+name+"'s fog file?");

        if (fileButton.getActionListeners().length==0)
            fileButton.addActionListener(explorer);

        if (openEditorManaged.getActionListeners().length==0)
            openEditorManaged.addActionListener(e -> {
                if (copyableList.getSelectedItem() != this){
                    biome b = (biome)copyableList.getSelectedItem();

                    if (b.copyingOtherFog){
                        customFile = new File( BiomeMain.activeDirectory+"/fogs/"+b.copyFrom.name+".fogManager.json");
                        utilityTraben.message(BiomeMain.frame,"Redirect: "+b.name+" is copying \n"+b.copyFrom.name +" getting that file");
                    }else{
                        customFile = new File( BiomeMain.activeDirectory+"/fogs/"+b.name+".fogManager.json");
                    }

                    if (!customFile.exists()){
                        utilityTraben.message(BiomeMain.frame,"Error: selected biome has no file\n - Creating new file or loading existing file for this biome");
                        customFile = new File( BiomeMain.activeDirectory+"/fogs/"+name+".fogManager.json");
                        PrintEmptyFogFile(customFile,"managed:"+name+"_managed");
                        System.out.println("test");
                    }
                    //customText.setText( "Custom File Loaded");
                    copyingOtherFog = false;
                    fogID = "managed:"+name+"_managed";
                    prepareCustomFile();
                    copyableList.setSelectedItem(this);
                    changeAndReactCopyList();
                    BiomeMain.saveMessage.setVisible(true);
                }


            //System.out.println(BiomeMain.activeDirectory+"\\fogs\\"+fileName);
            String fnamehere = name+".fogManager.json";
            String id = "managed:"+name+"_managed";
            if (copyingOtherFog){
                fnamehere = copyFrom.name+".fogManager.json";
                id = "managed:"+copyFrom.name+"_managed";
            }
            File f = new File(BiomeMain.activeDirectory+"\\fogs\\"+fnamehere);
            if (!f.exists()) {
                PrintEmptyFogFile(f,id);
            }
            openLockedFogFile(f,id);

        });
        copyingOtherFog = isCopy;
        System.out.println(printSelf());
    }
    private void PrintEmptyFogFile(File f , String id){
        try {

            System.out.println( "making new file "+f.getAbsolutePath());

            FileWriter writer = new FileWriter(f);

            writer.write("/*\n" +
                    " - Fog Definition Made by Biome & Fog Manager's Fog Editor\n" +
                    " - Coded by @Traben from the Minecraft RTX Discord\n" +
                    "  */\n" +
                    "{\n" +
                    "  \"format_version\": \"1.16.100\",\n" +
                    "  \"minecraft:fog_settings\": {\n" +
                    "    \"description\": {\n" +
                    "      \"identifier\": \"" + id + "\"\n" +
                    "    },\n" +
                    "    \"distance\": {\n" +
                    "    },\n" +
                    "    \"volumetric\": {\n" +
                    "      \"density\": {\n" +
                    "      },\n" +
                    "      \"media_coefficients\": {\n" +
                    "      }\n" +
                    "    }\n" +
                    "  }\n" +
                    "}");
            writer.close();
        }catch(Exception hjg){
            System.out.println("file not written wtf");
        }
    }

    private ActionListener imagesAL = e -> {
        BiomeMain.saveMessage.setVisible(true);
        isEnabled = !isEnabled;
        tabbedPane1.setEnabledAt(1,isEnabled);
        tabbedPane1.setEnabledAt(2,isEnabled);
        tabbedPane1.setEnabledAt(3,isEnabled);
        System.out.println("???gh");
        System.out.println(printSelf());
        imageFade(isEnabled);
    };

    private void initTrans(double wTransparent){
        if (wTransparent==4.0){
            isDefaultTrans=true;
            transparency=0.5;
        }else{
            isDefaultTrans=false;
            transparency=wTransparent;
        }
        transCheck.setSelected(isDefaultTrans);
        setEnables(transCont,!isDefaultTrans);

        transparencyField.setModel(new SpinnerNumberModel(0, 0, 1, 0.01));
        transSet();
        if (transparencySlider.getChangeListeners().length==0)
        transparencySlider.addChangeListener(e -> {
            BiomeMain.saveMessage.setVisible(true);
            transparency = transparencySlider.getValue()/100d;
            transSet();
        });
        if (transparencyField.getChangeListeners().length==0)
        transparencyField.addChangeListener(e -> {
            BiomeMain.saveMessage.setVisible(true);
            transparency = (double)transparencyField.getValue();
            transSet();
        });

    }
    private void transSet(){
        transparencyField.setValue(transparency);
        transparencySlider.setValue((int)(transparency*100));
    }

    ActionListener explorer = e -> {
        //System.out.println(System.getProperty("user.dir"));
        final JFileChooser chooser = new JFileChooser(BiomeMain.activeDirectory.getAbsolutePath()+"\\fogs");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new utilityTraben.fogFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(thisBiomePanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
            //openCustomEditorButton.setEnabled(true);
            customFile = chooser.getSelectedFile();
            customText.setText( "Custom File Loaded");
            copyingOtherFog = false;
            fogID = "managed:"+name+"_managed";
            prepareCustomFile();
            copyableList.setSelectedItem(this);
            changeAndReactCopyList();
            BiomeMain.saveMessage.setVisible(true);

        } else {
            //log.append("Open command cancelled by user." + newline);
            //openCustomEditorButton.setEnabled(false);
            customText.setText("");
        }
    };

    private void openLockedFogFile(File filePath, String fogIdentifier){
        System.out.println(filePath.getAbsolutePath()+ ", "+ fogIdentifier);
       new Main(name, filePath.getAbsolutePath(), fogIdentifier, false,true);
    }

        String getName(){
            return name;
        }
        String getFogID(){
            return fogID;
        }
        String getFileName(){
            return fileName;
        }
        boolean is_custom(){
            return isUsingCustom;
        }
        boolean is_Default(){
            return isUsingDefaultFog;
        }

        void setBiomeName(String biomeName){
            name = biomeName;
        }
        void setFogID(String fogIdentifier){
            fogID=fogIdentifier;
            System.out.println(fogID+" set!");
        }
        void setFileName(String fogFileName){
            fileName=fogFileName;
            System.out.println("filename set: "+fileName);
        }
        void setCustom(Boolean is_custom){
            toggleEnables(customCont,is_custom);
            customCheck.setSelected(is_custom);
            isUsingCustom = is_custom;
            if (is_custom){

                System.out.println("is_custom//////////////////////////////////////////////////////////////////////// \n" +
                        "name: "+name+
                        "\nfname: "+fileName+
                        "\nid: "+fogID);
                File customPosition = new File(BiomeMain.activeDirectory + "\\fogs\\" + name+".fogManager.json");

                //if not managed custom file and it does exist copy it to that and set
                if (!fileName.equals(name+".fogManager.json") && new File(BiomeMain.activeDirectory + "\\fogs\\" + fileName).exists() && !fileName.equals("")){
                    try {
                        Path copied = Paths.get(BiomeMain.activeDirectory + "\\fogs\\" + name + ".fogManager.json");

                        Path originalPath = Paths.get(BiomeMain.activeDirectory + "\\fogs\\" + fileName);
                        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

                        customFile = copied.toFile();
                        replaceFogId(customFile,"managed:"+name+"_managed");
                    }catch(IOException i){
                        //
                    }
                }

                if (customPosition.exists()) {
                    customFile = customPosition;
                    customText.setText(customFile.getName());
                }
            }
           /* if (openCustomEditorButton.getActionListeners().length==0)
                openCustomEditorButton.addActionListener(e -> openingCustomEditor());*/

        }

        private void prepareCustomFile(){
            BiomeMain.saveMessage.setVisible(true);
            if (customFile!=null) {
                try {
                    if (!customFile.getName().equals(name+".fogManager.json")) {
                        Path copied = Paths.get(BiomeMain.activeDirectory + "\\fogs\\" + name + ".fogManager.json");

                        Path originalPath = customFile.toPath();
                        Files.copy(originalPath, copied, StandardCopyOption.REPLACE_EXISTING);

                        customFile = copied.toFile();
                        //(Files.readAllLines(originalPath).equals(Files.readAllLines(copied)));

                        if (replaceFogId(customFile, "managed:" + name + "_managed")) {
                           // openLockedFogFile(customFile, "managed:"+name+"_managed");
                        } else {
                          /*  utilityTraben.message(BiomeMain.frame, "Error: Editor not opened please manually change fog id of" +
                                    "\n " + customFile.getName() +
                                    "\n To" +
                                    "\n\" managed:\"+name+\"_managed\"");*/
                        }
                    }else{
                        //openLockedFogFile(customFile, "managed:"+name+"_managed");
                    }


                }catch (IOException i){
                    utilityTraben.message(BiomeMain.frame,"Error: file not copied correctly");
                }
            }else{
                utilityTraben.message(BiomeMain.frame,"Error: no custom file selected");
            }
        }


        //true if success
        private boolean replaceFogId(File file,String newId){
            try{
                StringBuilder fileString = new StringBuilder();
                fileString.append("/*\n" +
                        " - Fog Definition Made by Biome & Fog Manager's Fog Editor\n" +
                        " - Coded by @Traben from the Minecraft RTX Discord\n" +
                        "  */\n");
                Scanner input = new Scanner(file);
                String line;
                //skip over any comment not ours
                while(input.hasNextLine()){
                    line = input.nextLine();
                    if (line.contains("{")){
                        fileString.append(line).append("\n");
                        break;
                    }

                }
                while(input.hasNextLine()){
                    line = input.nextLine();
                    if (line.contains("identifier")){
                        line = "      \"identifier\": \""+newId+"\"";
                    }
                    fileString.append(line).append("\n");

                }
                input.close();
                if (file.delete()) {
                    String out = fileString.toString();
                    FileWriter output = new FileWriter(file);
                    output.write(out);
                    output.close();
                    return true;
                }else{
                    utilityTraben.message(BiomeMain.frame,"ERROR: couldn't overwrite files fog ID");
                    return false;
                }
            }catch(IOException i){
                return false;
            }
        }

        void setDefault(Boolean is_default){
            //toggleEnables(fogCont,!is_default);
            if (is_default){
                copyFrom = defaultBiome;
            }
            isUsingDefaultFog = is_default;
        }
        void setWaterTransparency(double val){ transparency = val; }
        void setWaterColor(String hexColor){


            waterRed.setModel(new SpinnerNumberModel(0, 0, 255, 1));
            waterGreen.setModel(new SpinnerNumberModel(0, 0, 255, 1));
            waterBlue.setModel(new SpinnerNumberModel(0, 0, 255, 1));



            if (hexColor.matches("")){
                hasColour= false;
                waterColour = new Color(0,0,0);
            }else {
                hasColour = true;

                waterColour = Color.decode(hexColor);
                waterRedSlider.setValue(waterColour.getRed());
                waterGreenSlider.setValue(waterColour.getGreen());
                waterBlueSlider.setValue(waterColour.getBlue());
                waterRed.setValue(waterColour.getRed());
                waterGreen.setValue(waterColour.getGreen());
                waterBlue.setValue(waterColour.getBlue());

                setDisplayColour();
            }

            if (waterRed.getChangeListeners().length==0)
            waterRed.addChangeListener(colText);
            if (waterGreen.getChangeListeners().length==0)
            waterGreen.addChangeListener(colText);
            if (waterBlue.getChangeListeners().length==0)
            waterBlue.addChangeListener(colText);
            if (waterRedSlider.getChangeListeners().length==0)
            waterRedSlider.addChangeListener(colSlider);
            if (waterGreenSlider.getChangeListeners().length==0)
            waterGreenSlider.addChangeListener(colSlider);
            if (waterBlueSlider.getChangeListeners().length==0)
            waterBlueSlider.addChangeListener(colSlider);

            toggleEnables(waterCont,hasColour);
            waterCheck.setSelected(!hasColour);
            if (name.equals("default")) {
                toggleEnables(waterCont,true);
                waterCheck.setSelected(true);
            }

    }
    private ChangeListener colSlider = e ->  setColour(true);
    private ChangeListener colText = e ->  setColour(false);

    private void setColour(boolean fromSliders){
        BiomeMain.saveMessage.setVisible(true);
        if(fromSliders) {
            waterColour = new Color(waterRedSlider.getValue(),waterGreenSlider.getValue(),waterBlueSlider.getValue());
        }else {
            waterColour = new Color((int)waterRed.getValue(),(int)waterGreen.getValue(),(int)waterBlue.getValue());
        }
        setDisplayColour();
    }


    private void setDisplayColour(){
        waterRedSlider.setValue(waterColour.getRed());
        waterGreenSlider.setValue(waterColour.getGreen());
        waterBlueSlider.setValue(waterColour.getBlue());
        waterRed.setValue(waterColour.getRed());
        waterGreen.setValue(waterColour.getGreen());
        waterBlue.setValue(waterColour.getBlue());
            colourDisplay.setBackground(waterColour);
    }

   private boolean doChangeAndReact = false;

    private void initListeners(){
        if (customCheck.getActionListeners().length==0)
        customCheck.addActionListener(toggleEnable);

        if (waterCheck.getActionListeners().length==0)
        waterCheck.addActionListener(toggleEnable);
        if (transCheck.getActionListeners().length==0)
        transCheck.addActionListener(toggleEnable);

        if (UseThisFogCheck.getActionListeners().length==0)
        UseThisFogCheck.addActionListener(e -> {
            BiomeMain.saveMessage.setVisible(true);
            if (UseThisFogCheck.isSelected()) {
                copyableList.setSelectedItem(this);
                changeAndReactCopyList();
            }else{
                copyableList.setSelectedItem(defaultBiome);
                changeAndReactCopyList();
            }
        });
        copyableList.addItemListener(e -> {
            BiomeMain.saveMessage.setVisible(true);
            if (doChangeAndReact) changeAndReactCopyList();
            doChangeAndReact= !doChangeAndReact;
        });


    }

    private void changeAndReactCopyList(){
        BiomeMain.saveMessage.setVisible(true);
        //openEditorManaged.setEnabled(true);
        System.out.println("what");
        isUsingDefaultFog = (copyableList.getSelectedIndex() == 0);
        if(copyableList.getSelectedIndex() == 0 && copyableList.isEnabled()){
            openEditorManaged.setEnabled(true);
            UseThisFogCheck.setSelected(false);
            copyingOtherFog = true;
            copyFrom = allBiomes.get(0);
            openEditorManaged.setText("Copy Default fog settings to this biome to edit?");
            UseThisFogCheck.setSelected(false);
            isUsingDefaultFog = true;
            fogID= defaultBiome.fogID;
        }else if (copyableList.getSelectedItem() ==this && copyableList.isEnabled()){
            openEditorManaged.setEnabled(true);
            UseThisFogCheck.setSelected(true);
            isUsingDefaultFog = false;
            copyingOtherFog = false;
            copyFrom = allBiomes.get(copyableList.getSelectedIndex());
            openEditorManaged.setText("Edit this biomes fog");
            fogID="managed:"+name+"_managed";
        }else if ( copyableList.isEnabled()){
            openEditorManaged.setEnabled(false);
            UseThisFogCheck.setSelected(false);
            isUsingDefaultFog = false;
            copyingOtherFog = true;
            copyFrom = allBiomes.get(copyableList.getSelectedIndex());
            openEditorManaged.setText("Copy "+copyFrom.name+"'s fog to this biome to edit?");
            openEditorManaged.setEnabled(true);
            if (!copyFrom.isEnabled){
                if (ignoreNextMSG) utilityTraben.message(BiomeMain.frame,"Warning: The selected biome has not been enabled yet" +
                    "\n(Hint: Click on that biomes image to Enable it)");
                ignoreNextMSG = !ignoreNextMSG;
            }
        }
    }
    private boolean ignoreNextMSG = false;

    private final ActionListener toggleEnable = new ActionListener() {

        @Override
        public void actionPerformed(ActionEvent e) {
            BiomeMain.saveMessage.setVisible(true);
            Object source = e.getSource();
            if (source == waterCheck){
                toggleEnables(waterCont);
                hasColour = waterCheck.isSelected();

            }else if(source == customCheck){
                toggleEnables(customCont);
                //openCustomEditorButton.setEnabled(customText.getText().length() > 0);
                isUsingCustom = customCheck.isSelected();
                if (isUsingCustom) {
                    utilityTraben.message(BiomeMain.frame, "Disclaimer: This option is for technical users only." +
                            "\n it will copy the selected file to the active directory/pack being managed" +
                            "\n if a new file is selected it WILL OVERWRITE any previous file of this biome!");
                    fogID = "managed:"+name+"_managed";
                    copyingOtherFog = false;
                }else{
                   // fogID = "managed:"+name+"_managed";
                    changeAndReactCopyList();
                }
                //hideFogsIfCustom();
            }else if(source == transCheck){
                toggleEnables(transCont);
                isDefaultTrans = transCheck.isSelected();
            }

        }
    };

/*    private void hideFogsIfCustom(){
      //  isUsingCustom

        if (is_custom()){
            isUsingDefaultFog = false;
            //setEnables(fogCont,false);
            tabbedPane1.setEnabledAt(2,false);
        }else{
            isUsingDefaultFog = copyableList.getSelectedIndex() == 0;
            //setEnables(fogCont,true);
            tabbedPane1.setEnabledAt(2,true);
        }

    }*/
    private static void setEnables(JPanel panel, boolean set){
        BiomeMain.saveMessage.setVisible(true);
        panel.setEnabled(set);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component.getClass().getName().equals("javax.swing.JPanel")) {
                setEnables((JPanel) component,set);
            }

            component.setEnabled(set);
        }
    }

    private static void toggleEnables(JPanel panel){
        BiomeMain.saveMessage.setVisible(true);
        panel.setEnabled(!panel.isEnabled());

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component.getClass().getName().equals("javax.swing.JPanel")) {
                toggleEnables((JPanel) component);
            }

            component.setEnabled(!component.isEnabled());
        }
    }
    private static void toggleEnables(JPanel panel, boolean set){
        BiomeMain.saveMessage.setVisible(true);
        panel.setEnabled(set);

        Component[] components = panel.getComponents();

        for (Component component : components) {
            if (component.getClass().getName().equals("javax.swing.JPanel")) {
                toggleEnables((JPanel) component, set);
            }

            component.setEnabled(set);
        }
    }

    public void setDefaultBiome(biome defaultB) {
        defaultBiome = defaultB;
        if (name.equals("default")){
            initDefault();
        }


    }

    private void initDefault(){
        isUsingDefaultFog = true;
        copyableList.setEnabled(false);
        waterCheck.setEnabled(false);
        transCheck.setEnabled(false);
        toggleEnables(transCont,true);
        toggleEnables(waterCont,true);
        toggleEnables(fogCont,true);
        if (image.getActionListeners().length!=0)
        image.removeActionListener(imagesAL);
        copyableList.setEnabled(false);
        orFogText.setEnabled(false);
        UseThisFogCheck.setEnabled(false);
        UseThisFogCheck.setSelected(true);
        waterCheck.setSelected(true);
        transCheck.setSelected(true);

        defaultBiome = this;
        amDefault = true;
    }

    public void setDescription() {
        String str = name;
        str=str.toUpperCase();
        str = " "+str.replace("_"," ");

        if (str.length()> 12){
            int sz = 24;
            if (str.length()> 24)sz = 14;
            description.setFont(new Font("Candara",Font.PLAIN,sz));
            descriptionW.setFont(new Font("Candara",Font.PLAIN,sz));
            descriptionF.setFont(new Font("Candara",Font.PLAIN,sz));
            descriptionA.setFont(new Font("Candara",Font.PLAIN,sz));
        }
       // str = StringUtils.capitalize(str);
        str =str.toUpperCase();
        description.setText(str);
        descriptionW.setText(str);
        descriptionF.setText(str);
        descriptionA.setText(str);

    }

    private void setDisplay(){

        imageFade(isEnabled);
        tabbedPane1.setEnabledAt(1,isEnabled);
        tabbedPane1.setEnabledAt(2,isEnabled);
        tabbedPane1.setEnabledAt(3,isEnabled);


    }


    private void imageFade(Boolean isClear){
        BufferedImage back;
        back = utilityTraben.getImage("assets/" + name + ".png",265,175,!isClear);
        //back = new ImageIcon(getClass().getResource("assets/" + name + ".png"));
        image.setIcon(new ImageIcon(back));
    }

    public JPanel display(){
          return thisBiomePanel;
    }



    void updateFogList(){
        if (!name.equals("default")) {
            copyableList.removeAllItems();
            for (biome b : allBiomes) {
                copyableList.addItem(b);
            }
            copyableList.setSelectedIndex(0);

            System.out.println("ayy0");
            //now find what we have selected
            System.out.println("ayy.5 "+fogID);
            System.out.println("managed:" + name + "_managed");
         //   if (!is_custom()) {
                if (fogID.equals("managed:" + name + "_managed")) {
                    copyableList.setSelectedItem(this);
                    System.out.println("ayy");
                    isUsingDefaultFog = false;
                    copyingOtherFog = false;
                } else {
                    for (biome b : allBiomes) {
                        if (fogID.equals("managed:" + b.name + "_managed")) {
                            copyableList.setSelectedItem(b);
                            System.out.println("ayy2");
                            copyFrom = b;
                            copyingOtherFog = true;
                            isUsingDefaultFog = b.name.equals("default");

                            break;
                        }else if(fogID.equals("managed:" + b.name + "_managed")) {
                            copyableList.setSelectedItem(b);
                            System.out.println("ayy2");
                            copyFrom = b;
                            copyingOtherFog = true;
                            isUsingDefaultFog = b.name.equals("default");
                            break;
                        }

                    }
                }
           // }
        }else{
            //fuck off default
            copyableList.addItem(this);
            copyableList.setSelectedIndex(0);
            copyingOtherFog = false;
            copyFrom = this;
            openEditorManaged.setText("Edit this biomes fog");
            copyableList.setEnabled(false);
        }


    }
    void receiveAllList(ArrayList<biome> b){
        copyableList.setEnabled(true);
        allBiomes = new ArrayList<>( b);
        if (waterColour.equals(allBiomes.get(0).waterColour) && !amDefault){
            hasColour= false;
            toggleEnables(waterCont,hasColour);
            waterCheck.setSelected(!hasColour);
        }
        System.out.println(name+" is copying = "+ copyingOtherFog);
        if (copyingOtherFog) fogID = copyIDBackup;
        updateFogList();
    }

    @Override
    public String toString() {
        return name;
    }

    //still primitive
    public String printSelf(){

        // final chance to check biome validity
        if (isEnabled) {
            String output = "    \"" + name + "\": {\n";
            boolean comma = false;
            if (hasColour || amDefault) {
                output = output + "      \"water_surface_color\": \"" +
                        String.format("#%02X%02X%02X",
                                waterColour.getRed(),
                                waterColour.getGreen(),
                                waterColour.getBlue())
                        + "\"";
                comma = true;
            }else{

            }
            if (!isDefaultTrans || amDefault) {
                if (comma) output = output + ",\n";
                output = output +
                        "      \"water_surface_transparency\": " + transparency;
            }
            if (isUsingDefaultFog && !isUsingCustom) {
                if (amDefault) {
                    if (comma) output = output + ",\n";
                    output = output +
                                        "      \"fog_identifier\": \"" + fogID + "\"";
                }else{
                    if (comma) output = output + ",\n";
                    output = output +
                            "      \"fog_identifier\": \"" + defaultBiome.fogID + "\"";
                }
            } else if (copyingOtherFog && copyFrom !=null) {
                if (comma) output = output + ",\n";

                if (copyFrom.isUsingCustom){
                    output = output +
                            "      \"fog_identifier\": \"" + copyFrom.fogID + "\"";
                }else{
                    output = output +
                            "      \"fog_identifier\": \"" + copyFrom.fogID + "\"";
                }

            }else if (isUsingCustom){
                if (comma) output = output + ",\n";
                output = output +
                        "      \"fog_identifier\": \"managed:"+name+"_managed\"";
            }else{
                if (comma) output = output + ",\n";
                output = output +
                        "      \"fog_identifier\": \"" + fogID + "\"";
            }
            output = output + "\n    }";

            return output;
        }else{
            return "";
        }
    }



    }

