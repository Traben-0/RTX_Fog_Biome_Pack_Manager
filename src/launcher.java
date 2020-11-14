import javax.swing.*;
import javax.swing.filechooser.FileFilter;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.nio.file.*;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Enumeration;
import java.util.Scanner;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;


public class launcher {
    JPanel launcherPanel;
    private JTextField choice;
    private JButton fileExplorer;
    private JCheckBox remember;
    private JButton useCreateAboveFileButton;
    private JButton image;
    private JPanel fileSelected;
    private JPanel selectFile;
    private JPanel loadingMessage;
    private JLabel img;
    private final File defaultOutputFolder = new File(System.getProperty("user.dir")+"\\manager_output_pack\\");
    private File selectedTested = defaultOutputFolder;
    private String customBiomeFilename="biomes_client.json";
    boolean exitASAP = false;
    boolean isZip = false;
    File zipFile;

    launcher( boolean ignoreRemember) {



        if (new File(defaultOutputFolder.getAbsolutePath()).exists()){
            useCreateAboveFileButton.setText("Manage in the folder here? (manager_output_pack)");
        }
        fileExplorer.addActionListener(explorer);
        image.addActionListener(explorer);

        useCreateAboveFileButton.addActionListener(e -> {
            handleChoice(selectedTested);

        });
        fileSelected.setVisible(false);


        img.setIcon(new ImageIcon( utilityTraben.getImage("assets/unpack.png",100,125)));
        loadingMessage.setVisible(false);

        if (!ignoreRemember) {
            checkRememberSettings();
        }
    }

    private void checkRememberSettings(){
        File setting= new File(System.getProperty("user.dir")+"\\rememberLast");
        if (setting.exists()){
            remember.setSelected(true);
            try{
                Scanner input = new Scanner(setting);
                selectedTested = new File(input.nextLine());
                customBiomeFilename = input.nextLine();
                String ziptest = input.nextLine();
                if (ziptest.contains(".mcpack")){
                    zipFile = new File(ziptest);
                    isZip = true;

                    Path pth = selectedTested.toPath();
                    createFolder(pth, true);
                    System.out.println("unzipping "+zipFile.toPath().toString()+"\n "+pth.toString());
                    unzip(zipFile.toPath().toString(),pth.toString());

                    //selectedTested = new File(selectedTested.getParent()+"\\temp_manager_mcpack_folder\\");
                }

                input.close();

            }catch(IOException i){
                //
            }
            setting.delete();
            if (!selectedTested.exists()) {
                System.out.println("wrong settings");
                remember.setSelected(false);
            }else if (!new File(selectedTested.getAbsolutePath() + "\\"+customBiomeFilename).exists()) {
                System.out.println("wrong settings");
                customBiomeFilename="";
            }else{
                //data valid
            exitASAP = true;
            }
        }
    }

    ActionListener explorer = e -> {
        final JFileChooser chooser = new JFileChooser(System.getProperty("user.dir"));
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new CustomFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(launcherPanel);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            selectedTested = chooser.getSelectedFile();
            choice.setText(selectedTested.getAbsolutePath());
                if (selectedTested.isDirectory()){
                    if (new File(selectedTested.getAbsolutePath()+"/manifest.json").exists()) {
                        useCreateAboveFileButton.setText("Manage this existing resourcepack folder?");
                    }else{
                        useCreateAboveFileButton.setText("Create a new resourcepack in selected folder?");
                    }
                }else{
                    if (selectedTested.getName().contains(".mcpack")){
                        useCreateAboveFileButton.setText("Manage this existing mcpack file?");
                    }else if (selectedTested.getName().contains("manifest.json")){
                        useCreateAboveFileButton.setText("Manage this existing resourcepack folder?");
                    }else if (selectedTested.getName().contains("biomes_client.json")){
                        useCreateAboveFileButton.setText("Manage this existing biome file or pack?");
                    }
                }

            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
            selectFile.setVisible(false);
            fileSelected.setVisible(true);
        } else {
            //log.append("Open command cancelled by user." + newline);
        }

    };

    private static class CustomFilter  extends FileFilter {
        public boolean accept(File f) {
            if (f.isDirectory()) {
                return true;
            }
            //if (f.getName().matches("biomes_client.json")||
            //        f.getName().matches("manifest.json")){
            //    return true;
            //}
            if (f.getName().matches("fogs")||
                    f.getName().matches("src")||
                    f.getName().matches("out")){
                return false;
            }

            String extension = getExtension(f);
            if (extension != null) {
                if (extension.equals("mcpack")
                      || extension.equals("json")
                ){
                    return true;
                } else {
                    return false;
                }
            }

            return false;
        }

        @Override
        public String getDescription() {
            return "Folder, .mcpack, biomes_client.json, or existing pack manifest.json";
        }

        public String getExtension(File f) {
            String ext = null;
            String s = f.getName();
            int i = s.lastIndexOf('.');

            if (i > 0 &&  i < s.length() - 1) {
                ext = s.substring(i+1).toLowerCase();
            }
            return ext;
        }
    }

    Timer timer = new Timer(1000, new ActionListener() {
        public void actionPerformed(ActionEvent evt) {
            timer.stop();
            initiateMCPackMode();
            BiomeMain.exitLauncher();
        }
    });

    //assumes input has only been correct
    void handleChoice(File f){
        boolean dont = false;
        System.out.println(f.getAbsolutePath() + f.isDirectory());
        if(f.getAbsolutePath().equals(defaultOutputFolder.getAbsolutePath())){
            initiateHereMode();
            BiomeMain.exitLauncher();
        } else if (f.isDirectory()) {
                initiateFolderMode(f.exists());
                BiomeMain.exitLauncher();
        } else if (f.isFile()) {
                if (f.getName().contains("manifest.json")) {
                    selectedTested = selectedTested.getParentFile();
                    initiateFolderMode(true);
                    dont = true;
                    BiomeMain.exitLauncher();
                }else if (f.getName().contains("biomes_client.json")) {
                    customBiomeFilename = selectedTested.getName();
                    selectedTested = selectedTested.getParentFile();
                    initiateFolderMode(true);
                    dont = true;
                    BiomeMain.exitLauncher();
                }else if (utilityTraben.fileContains(f,"biomes")) {
                    customBiomeFilename = selectedTested.getName();
                    selectedTested = selectedTested.getParentFile();
                    initiateFolderMode(true);
                    dont = true;
                    BiomeMain.exitLauncher();
                } else if (f.getName().contains(".mcpack")) {
                    //customBiomeFilename = selectedTested.getName();
                    //selectedTested = selectedTested.getParentFile();
                    //utilityTraben.message(BiomeMain.frame,"unpacking File: this might take a while with big packs!");
                    //JPanel msg = new JPanel();
                    fileSelected.setVisible(false);
                    loadingMessage.setVisible(true);
                    remember.setVisible(false);
                    useCreateAboveFileButton.setVisible(false);
                    //initiateMCPackMode();
                    dont = true;
                    timer.start();
                }

        }else{
            //no choice made
            System.out.println("empty initiating here");
            initiateHereMode();
            BiomeMain.exitLauncher();
        }
    }



    //creat working folder here
    private void createFolder(Path dir, boolean markDelete){
        try {
            // lol what? am i dumb?

            System.out.println("created directory");
            Files.createDirectory(dir);
            if (markDelete) {
                //delete path when jvm closes
                File del = dir.toFile();
                del.deleteOnExit();
            }

            dir = Paths.get(dir.toAbsolutePath()  +"\\fogs\\");
            Files.createDirectory(dir);
        }catch (IOException i){
            System.out.println("utter failure expect crash in later logic");
        }
    }


    private void initiateFolderMode(boolean folderExists){
        //resource pack folder
        //check for manifest
        if (!folderExists){
            createFolder(FileSystems.getDefault().getPath(selectedTested.getAbsolutePath()),false);
        }

        makeManifestCapable();

        // ready to go

        //end of line

    }


    private void initiateHereMode(){
        System.out.println("here mode");
        //selectedTested = new File(System.getProperty("user.dir"));
        initiateFolderMode(defaultOutputFolder.exists());
    }
    private void initiateMCPackMode(){
        zipFile = new File(selectedTested.getAbsolutePath());
        isZip = true;
        selectedTested = new File(selectedTested.getParent()+"\\temp_manager_mcpack_folder\\");


        Path pth = selectedTested.toPath();

        try {
            deleteDirectoryStream(pth);
        }catch(IOException i){
            System.out.println("ignore");

        }
        createFolder(pth, true);
        System.out.println("unzipping "+zipFile.toPath().toString()+"\n "+pth.toString());
        unzip(zipFile.toPath().toString(),pth.toString());

        initiateFolderMode(true);
    }

    void deleteDirectoryStream(Path path) throws IOException {
        Files.walk(path)
                .sorted(Comparator.reverseOrder())
                .map(Path::toFile)
                .forEach(File::delete);
    }


    private void makeManifestCapable(){
        boolean testRTX = false;
        boolean testFormat = true;
        System.out.println("checking manifest at "+selectedTested + "\\manifest.json");

        StringBuilder fileAsString = new StringBuilder();
        try {
            File toRead = new File(selectedTested+"\\manifest.json");
            Scanner input = new Scanner(toRead);
            String line;
            boolean write = true;
            while (input.hasNextLine()) {
                line = input.nextLine();


                //test pack
                if (line.contains("format_version")&&
                        //version not larger or equal to 2
                        !(Integer.parseInt(line.replaceAll("\\D+","")) >=2 )
                ){
                    testFormat =false;
                    fileAsString.append("    \"format_version\": 2,").append("\n");
                    write = false;
                }
                if (line.contains("raytraced")){
                    testRTX = true;
                }
                if (line.matches(" {4}]")){
                    //if end of file
                    if (!testRTX) {
                        fileAsString.append("    ],\n" +
                                "    \"capabilities\" : [\n" +
                                "            \"raytraced\"\n" +
                                "    ]\n");
                        write = false;
                    }
                }
                if (write) fileAsString.append(line).append("\n");
                write = true;
            }
            input.close();



        }catch (FileNotFoundException e){
            System.out.println("file not found\n ");
            try {
                    FileWriter out = new FileWriter(selectedTested + "\\manifest.json");
                    out.write(manifestDefaultFileString);
                    out.close();
            }catch (IOException h){
                System.out.println("dffghfgnfnfgnfghfhfd should never appear ever");
            }
            testFormat = true;
            testRTX = true;

        }
        try {
            //if needs change
            if (!testFormat || !testRTX) {
                FileWriter out = new FileWriter(selectedTested + "\\manifest.json");
                out.write(fileAsString.toString());
                out.close();
            }
        }catch (IOException e){
            System.out.println("dffghfghfhfd should never appear ever");


        }

        //pack now has manifest and is set correctly
    }

    private static void unzip(String zipFilePath, String destDir) {
            //Open the file
            try(ZipFile file = new ZipFile(zipFilePath))
            {
                FileSystem fileSystem = FileSystems.getDefault();
                //Get file entries
                Enumeration<? extends ZipEntry> entries = file.entries();

                //We will unzip files in this folder
                String uncompressedDirectory = destDir+"/";
                //Files.createDirectory(fileSystem.getPath(uncompressedDirectory));

                //Iterate over entries
                while (entries.hasMoreElements())
                {
                    ZipEntry entry = entries.nextElement();
                    //System.out.println(entry.getName());
                    //If directory then create a new directory in uncompressed folder
                    if (entry.isDirectory())
                    {//somehow never reads folder?????
                        System.out.println("Creating Directory:" + uncompressedDirectory + entry.getName());
                        Files.createDirectories(fileSystem.getPath(uncompressedDirectory + entry.getName()));
                    }
                    //Else create the file
                    else
                    {

                        InputStream is = file.getInputStream(entry);
                        BufferedInputStream bis = new BufferedInputStream(is);
                        String uncompressedFileName = uncompressedDirectory + entry.getName();
                        Path uncompressedFilePath = fileSystem.getPath(uncompressedFileName);

                        new File(uncompressedFileName).getParentFile().mkdirs();

                        Files.createFile(uncompressedFilePath);
                        FileOutputStream fileOutput = new FileOutputStream(uncompressedFileName);
                        while (bis.available() > 0)
                        {
                            fileOutput.write(bis.read());
                        }
                        fileOutput.close();
                        System.out.println("Written :" + entry.getName());
                    }
                }
            }
            catch(IOException e)
            {
                e.printStackTrace();
            }
        }


/*    private static void unzip(String zipFilePath, String destDir) {
        File dir = new File(destDir);
        // create output directory if it doesn't exist
        if(!dir.exists()) dir.mkdirs();
        FileInputStream fis;
        //buffer for read and write data to file
        byte[] buffer = new byte[1024];
        try {
            fis = new FileInputStream(zipFilePath);
            ZipInputStream zis = new ZipInputStream(fis);
            ZipEntry ze = zis.getNextEntry();
            while(ze != null){
                String fileName = ze.getName();
                File newFile = new File(destDir + File.separator + fileName);
                System.out.println("Unzipping to "+newFile.getAbsolutePath());
                //create directories for sub directories in zip
                new File(newFile.getParent()).mkdirs();
                FileOutputStream fos = new FileOutputStream(newFile);
                int len;
                while ((len = zis.read(buffer)) > 0) {
                    fos.write(buffer, 0, len);
                }
                fos.close();
                //close this ZipEntry
                zis.closeEntry();
                ze = zis.getNextEntry();
            }
            //close last ZipEntry
            zis.closeEntry();
            zis.close();
            fis.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }*/



    File getChoice(){
        File setting= new File(System.getProperty("user.dir")+"\\rememberLast");
        if (setting.exists()){
            setting.delete();
        }
        if (remember.isSelected()){
                try{
                    FileWriter output = new FileWriter(setting);
                    output.write( selectedTested.getAbsolutePath() +"\n");
                    output.write( customBiomeFilename+"\n");
                    if (isZip){
                        output.write(zipFile.getAbsolutePath()+"\n");
                    }else{
                        output.write("\n");
                    }
                    output.close();
                }catch(IOException i){
                    //
                }
        }
            return new File(selectedTested + "\\" + customBiomeFilename);

    }

    final String manifestDefaultFileString ="{\n" +
            "    \"format_version\": 2,\n" +
            "    \"header\": {\n" +
            "        \"description\": \"Biome & Fog Manager - Pack\",\n" +
            "        \"name\": \"[Fog - pack]\",\n" +
            "        \"uuid\": \"4ef32887-84ed-4172-ad98-45277df17a48\",\n" +
            "        \"version\": [0, 0, 1],\n" +
            "        \"min_engine_version\": [ 1, 14, 0 ]\n" +
            "    },\n" +
            "    \"modules\": [\n" +
            "        {\n" +
            "            \"description\": \"This is a default Biome & Fog Manager created resource pack\",\n" +
            "            \"type\": \"resources\",\n" +
            "            \"uuid\": \"1a20e6b1-5133-4a9c-a3fc-c296992438bc\",\n" +
            "            \"version\": [0, 0, 1]\n" +
            "        }\n" +
            "    ],\n" +
            "    \"capabilities\" : [\n" +
            "            \"raytraced\"\n" +
            "    ]\t\n" +
            "}";
}
