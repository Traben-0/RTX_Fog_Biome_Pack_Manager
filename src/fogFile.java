import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Scanner;

public class fogFile{

    String fileName;
    String ID;

    //filename from either directory search or custom input
    //when file exists but only filename is known
    fogFile(String existingFileName){
        fileName = existingFileName;

        getIdentifier();
    }

    //when file is either known ID like a managed file
    // or when creating new file
    fogFile(String newFileName, String identifier){
        fileName = newFileName;
        ID = identifier;
        //check it exists if not it creates
        checkExists();
    }


    private void getIdentifier(){
        try {

            File toRead = new File(fileName);
            Scanner input = new Scanner(toRead);
            String line;
            while (input.hasNextLine()) {
                line = input.nextLine();
                if (line.contains("identifier")) {
                    line = line.replace(" ", "");
                    line = line.replace("\"", "");
                    line = line.replace("identifier", "");
                    line = line.replaceFirst(":","");
                    ID = line;
                    System.out.println("identifier found for " +fileName);
                    break;
                }
            }
            input.close();


        }catch (FileNotFoundException e){
            System.out.println("file not found\n " +
                    "this object has not been initialized correctly\n " +
                    "the file MUST exist to construct object this way"
            );

        }

    }
    private void checkExists(){
        File check = new File("fogs/"+fileName);
        if (!check.exists()) {
            System.out.println("file not found\n Generating default file");
            generateDefaultFile();
        }


    }

    private void generateDefaultFile(){
        try {
            File newFile = new File("fogs/" + fileName);
            FileWriter output = new FileWriter(newFile);
            output.write("{\n" +
                    "  \"format_version\": \"1.16.100\",\n" +
                    "  \"minecraft:fog_settings\": {\n" +
                    "    \"description\": {\n" +
                    "      \"identifier\": \""+ID+"\"\n" +
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
                    "\t\t\"weather\": {\n" +
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
                    "}");

        output.close();
        }catch(IOException e){
            //
        }
        setDefaultParams();
    }
    private void setDefaultParams(){

    }

}
