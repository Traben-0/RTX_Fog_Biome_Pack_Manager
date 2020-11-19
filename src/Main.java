/*
words cannot express how bad i was at coding here
the entire editor is the worst code i have every written
as i hadn't coded in years

BiomeMain is so much better pls no judge

*/


import javafx.scene.control.SpinnerValueFactory;

import javax.imageio.ImageIO;
import javax.swing.*; //imports Swing package which creates form and button
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.*; //imports Event package which listens for button press
import java.awt.image.BufferedImage;
import java.io.*;
import java.math.BigDecimal;
import java.math.MathContext;
import java.nio.file.*;
import java.text.DecimalFormat;
import java.util.Scanner;


public class Main {

    private final JMenuBar menuBar = new JMenuBar();
    private final JMenu menu = new JMenu("File");
    private final JMenuItem helpM = new JMenuItem("Help");
    private final JMenuItem importExisting = new JMenuItem("copy from...");
    private final JMenuItem newFog = new JMenuItem("New");
    private final JMenuItem newFogOpen = new JMenuItem("Open");
    private final JMenuItem saveMenu = new JMenuItem("Save Fog File");
    private final JMenuItem saveAsMenu = new JMenuItem("Save as");

    private final JMenuItem saveMessage = new JMenuItem("* Unsaved changes (go to File/Save)");



    private final settingPage distancePage= new settingPage();
    private final volumetric volumetricPage= new volumetric();
    private final coefficients coefficiantPage = new coefficients();
    private final fog_wrapper wrapperF = new fog_wrapper();
    private final JFrame frame =new JFrame();;

    public String indentifier = "namespace:test_fog";
    public String FILENAME = "Fog_Edit_Output.json";
    private File FILE;


    private final ItemListener action = e -> {
            saveMessage.setVisible(true);
            Object source = e.getItemSelectable();

            if (source == distancePage.airFogCheckBox) {
                distancePage.air_fog.setVisible(
                        !distancePage.air_fog.isVisible()
                );
                render();
            } else if (source == distancePage.waterFogCheckBox) {
                distancePage.water_fog.setVisible(
                        !distancePage.water_fog.isVisible()
                );
                render();
            } else if (source == distancePage.weatherFogCheckBox) {
                distancePage.weather_fog.setVisible(
                        !distancePage.weather_fog.isVisible()
                );
                render();
            } else if (source == distancePage.lavaFogCheckBox) {
                distancePage.lava_fog.setVisible(
                        !distancePage.lava_fog.isVisible()
                );
                render();
            }else if (source == distancePage.lava_ResistanceCheckBox) {
                distancePage.lava_resistance_fog.setVisible(
                        !distancePage.lava_resistance_fog.isVisible()
                );
                render();
            } else if (source == volumetricPage.airFogCheckBox) {
                volumetricPage.air_vfog.setVisible(
                        !volumetricPage.air_vfog.isVisible()
                );
                render();
            } else if (source == volumetricPage.waterFogCheckBox) {
                volumetricPage.water_vfog.setVisible(
                        !volumetricPage.water_vfog.isVisible()
                );
                render();
            } else if (source == volumetricPage.weatherFogCheckBox) {
                volumetricPage.weather_vfog.setVisible(
                        !volumetricPage.weather_vfog.isVisible()
                );
                render();

            } else if (source == volumetricPage.lavaFogCheckBox) {
                volumetricPage.lava_vfog.setVisible(
                        !volumetricPage.lava_vfog.isVisible()
                );
                render();
            }else if (source == volumetricPage.lava_ResistanceCheckBox) {
                volumetricPage.lava_resistance_vfog.setVisible(
                        !volumetricPage.lava_resistance_vfog.isVisible()
                );
                render();
            } else if (source == coefficiantPage.airCheckBox) {
                coefficiantPage.air_c.setVisible(
                        !coefficiantPage.air_c.isVisible()
                );
                render();
            } else if (source == coefficiantPage.waterCheckBox) {
                coefficiantPage.water_c.setVisible(
                        !coefficiantPage.water_c.isVisible()
                );
                render();
            } else if (source == coefficiantPage.cloudCheckBox) {
                coefficiantPage.cloud_c.setVisible(
                        !coefficiantPage.cloud_c.isVisible()
                );
                render();
            }




            if (e.getStateChange() == ItemEvent.DESELECTED)
            //...make a note of it...

                render();

    };

    private final ActionListener saveEvt = e -> {
        saveMessage.setVisible(false);
        save();
        BiomeMain.saveBiomeFile(new File(BiomeMain.activeDirectory+"\\biomes_client.json"));
        BiomeMain.saveMessage.setVisible(false);
    };
    private void save(){
        if (volumetricPage.weatherFogCheckBox.isSelected()) {
            error("WARNING: in the current RTX beta Volumetric Fog (Weather) is currently bugged.\n " +
                    "If you do not uncheck this box the pack will not work in-game!!\n\n" +
                    " This program will still output the file incase this is fixed in future");
        }
        if (
                ((doubleReturn(distancePage.air_fog_1.getText()) >= doubleReturn(distancePage.air_fog_2.getText()))) ||
                        ((doubleReturn(distancePage.water_fog_1.getText()) >= doubleReturn(distancePage.water_fog_2.getText()))) ||
                        ((doubleReturn(distancePage.weather_fog_1.getText()) >= doubleReturn(distancePage.weather_fog_2.getText()))) ||
                        ((doubleReturn(distancePage.lava_fog_1.getText()) >= doubleReturn(distancePage.lava_fog_2.getText()))) ||
                        ((doubleReturn(distancePage.lava_resistance_fog_1.getText()) >= doubleReturn(distancePage.lava_resistance_fog_2.getText())))
        ){
            error("WARNING: Distance fog end CANNOT! be lower than the start value.\n" +
                    "\n This file will still save.\n but it shouldn't work ingame...");
        }

        printJson();
    }

    private final FocusListener updateOthers = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            Object source = e.getSource();
            saveMessage.setVisible(true);
            if (source == coefficiantPage.a_a_r_1) {
                coSliderConvert(coefficiantPage.a_a_r_1,coefficiantPage.a_a_r_2,true);
            }else if (source == coefficiantPage.a_a_g_1) {
                coSliderConvert(coefficiantPage.a_a_g_1,coefficiantPage.a_a_g_2,true);
            }else if (source == coefficiantPage.a_a_b_1) {
                coSliderConvert(coefficiantPage.a_a_b_1,coefficiantPage.a_a_b_2,true);
            }else if (source == coefficiantPage.a_s_r_1) {
                coSliderConvert(coefficiantPage.a_s_r_1,coefficiantPage.a_s_r_2,true);
            }else if (source == coefficiantPage.a_s_g_1) {
                coSliderConvert(coefficiantPage.a_s_g_1,coefficiantPage.a_s_g_2,true);
            }else if (source == coefficiantPage.a_s_b_1) {
                coSliderConvert(coefficiantPage.a_s_b_1,coefficiantPage.a_s_b_2,true);
                //water
            }else if (source == coefficiantPage.w_a_r_1) {
                coSliderConvert(coefficiantPage.w_a_r_1,coefficiantPage.w_a_r_2,true);
            }else if (source == coefficiantPage.w_a_g_1) {
                coSliderConvert(coefficiantPage.w_a_g_1,coefficiantPage.w_a_g_2,true);
            }else if (source == coefficiantPage.w_a_b_1) {
                coSliderConvert(coefficiantPage.w_a_b_1,coefficiantPage.w_a_b_2,true);
            }else if (source == coefficiantPage.w_s_r_1) {
                coSliderConvert(coefficiantPage.w_s_r_1,coefficiantPage.w_s_r_2,true);
            }else if (source == coefficiantPage.w_s_g_1) {
                coSliderConvert(coefficiantPage.w_s_g_1,coefficiantPage.w_s_g_2,true);
            }else if (source == coefficiantPage.w_s_b_1) {
                coSliderConvert(coefficiantPage.w_s_b_1,coefficiantPage.w_s_b_2,true);
                //cloud
            }else if (source == coefficiantPage.car1) {
                coSliderConvert(coefficiantPage.car1,coefficiantPage.car2,true);
            }else if (source == coefficiantPage.cag1) {
                coSliderConvert(coefficiantPage.cag1,coefficiantPage.cag2,true);
            }else if (source == coefficiantPage.cab1) {
                coSliderConvert(coefficiantPage.cab1,coefficiantPage.cab2,true);
            }else if (source == coefficiantPage.csr1) {
                coSliderConvert(coefficiantPage.csr1,coefficiantPage.csr2,true);
            }else if (source == coefficiantPage.csg1) {
                coSliderConvert(coefficiantPage.csg1,coefficiantPage.csg2,true);
            }else if (source == coefficiantPage.csb1) {
                coSliderConvert(coefficiantPage.csb1,coefficiantPage.csb2,true);
            }
        }
    };
    private final ActionListener updateOthersa = e -> {
        saveMessage.setVisible(true);
        Object source = e.getSource();

        if (source == coefficiantPage.a_a_r_1) {
            coSliderConvert(coefficiantPage.a_a_r_1,coefficiantPage.a_a_r_2,true);
        }else if (source == coefficiantPage.a_a_g_1) {
            coSliderConvert(coefficiantPage.a_a_g_1,coefficiantPage.a_a_g_2,true);
        }else if (source == coefficiantPage.a_a_b_1) {
            coSliderConvert(coefficiantPage.a_a_b_1,coefficiantPage.a_a_b_2,true);
        }else if (source == coefficiantPage.a_s_r_1) {
            coSliderConvert(coefficiantPage.a_s_r_1,coefficiantPage.a_s_r_2,true);
        }else if (source == coefficiantPage.a_s_g_1) {
            coSliderConvert(coefficiantPage.a_s_g_1,coefficiantPage.a_s_g_2,true);
        }else if (source == coefficiantPage.a_s_b_1) {
            coSliderConvert(coefficiantPage.a_s_b_1,coefficiantPage.a_s_b_2,true);
            //water
        }else if (source == coefficiantPage.w_a_r_1) {
            coSliderConvert(coefficiantPage.w_a_r_1,coefficiantPage.w_a_r_2,true);
        }else if (source == coefficiantPage.w_a_g_1) {
            coSliderConvert(coefficiantPage.w_a_g_1,coefficiantPage.w_a_g_2,true);
        }else if (source == coefficiantPage.w_a_b_1) {
            coSliderConvert(coefficiantPage.w_a_b_1,coefficiantPage.w_a_b_2,true);
        }else if (source == coefficiantPage.w_s_r_1) {
            coSliderConvert(coefficiantPage.w_s_r_1,coefficiantPage.w_s_r_2,true);
        }else if (source == coefficiantPage.w_s_g_1) {
            coSliderConvert(coefficiantPage.w_s_g_1,coefficiantPage.w_s_g_2,true);
        }else if (source == coefficiantPage.w_s_b_1) {
            coSliderConvert(coefficiantPage.w_s_b_1,coefficiantPage.w_s_b_2,true);
            //cloud
        }else if (source == coefficiantPage.car1) {
            coSliderConvert(coefficiantPage.car1,coefficiantPage.car2,true);
        }else if (source == coefficiantPage.cag1) {
            coSliderConvert(coefficiantPage.cag1,coefficiantPage.cag2,true);
        }else if (source == coefficiantPage.cab1) {
            coSliderConvert(coefficiantPage.cab1,coefficiantPage.cab2,true);
        }else if (source == coefficiantPage.csr1) {
            coSliderConvert(coefficiantPage.csr1,coefficiantPage.csr2,true);
        }else if (source == coefficiantPage.csg1) {
            coSliderConvert(coefficiantPage.csg1,coefficiantPage.csg2,true);
        }else if (source == coefficiantPage.csb1) {
            coSliderConvert(coefficiantPage.csb1,coefficiantPage.csb2,true);
        }
    };
    private final ChangeListener updateOthersSlider = e -> {
        saveMessage.setVisible(true);
        Object source = e.getSource();

        if (source == coefficiantPage.a_a_r_2) {
            coSliderConvert(coefficiantPage.a_a_r_1,coefficiantPage.a_a_r_2,false);
        }else if (source == coefficiantPage.a_a_g_2) {
            coSliderConvert(coefficiantPage.a_a_g_1,coefficiantPage.a_a_g_2,false);
        }else if (source == coefficiantPage.a_a_b_2) {
            coSliderConvert(coefficiantPage.a_a_b_1,coefficiantPage.a_a_b_2,false);
        }else if (source == coefficiantPage.a_s_r_2) {
            coSliderConvert(coefficiantPage.a_s_r_1,coefficiantPage.a_s_r_2,false);
        }else if (source == coefficiantPage.a_s_g_2) {
            coSliderConvert(coefficiantPage.a_s_g_1,coefficiantPage.a_s_g_2,false);
        }else if (source == coefficiantPage.a_s_b_2) {
            coSliderConvert(coefficiantPage.a_s_b_1,coefficiantPage.a_s_b_2,false);
            //water
        }else if (source == coefficiantPage.w_a_r_2) {
            coSliderConvert(coefficiantPage.w_a_r_1,coefficiantPage.w_a_r_2,false);
        }else if (source == coefficiantPage.w_a_g_2) {
            coSliderConvert(coefficiantPage.w_a_g_1,coefficiantPage.w_a_g_2,false);
        }else if (source == coefficiantPage.w_a_b_2) {
            coSliderConvert(coefficiantPage.w_a_b_1,coefficiantPage.w_a_b_2,false);
        }else if (source == coefficiantPage.w_s_r_2) {
            coSliderConvert(coefficiantPage.w_s_r_1,coefficiantPage.w_s_r_2,false);
        }else if (source == coefficiantPage.w_s_g_2) {
            coSliderConvert(coefficiantPage.w_s_g_1,coefficiantPage.w_s_g_2,false);
        }else if (source == coefficiantPage.w_s_b_2) {
            coSliderConvert(coefficiantPage.w_s_b_1,coefficiantPage.w_s_b_2,false);
            //cloud
        }else if (source == coefficiantPage.car2) {
            coSliderConvert(coefficiantPage.car1,coefficiantPage.car2,false);
        }else if (source == coefficiantPage.cag2) {
            coSliderConvert(coefficiantPage.cag1,coefficiantPage.cag2,false);
        }else if (source == coefficiantPage.cab2) {
            coSliderConvert(coefficiantPage.cab1,coefficiantPage.cab2,false);
        }else if (source == coefficiantPage.csr2) {
            coSliderConvert(coefficiantPage.csr1,coefficiantPage.csr2,false);
        }else if (source == coefficiantPage.csg2) {
            coSliderConvert(coefficiantPage.csg1,coefficiantPage.csg2,false);
        }else if (source == coefficiantPage.csb2) {
            coSliderConvert(coefficiantPage.csb1,coefficiantPage.csb2,false);
        }

    };

    //            coSliderConvert(coefficiantPage.a_s_r_1,coefficiantPage.a_s_r_2,true);
    private void coSliderConvert(JTextField text, JSlider slider, boolean textToSlider){

        if (textToSlider){
            try {
               int value = (int)(Double.parseDouble(text.getText()) *10000);
                if (value > 10000){
                    value =10000;
                    text.setText("1.0");
                }
                if (value < 0) {
                    value =0;
                    text.setText("0.0");
                }


                slider.setValue( value);

                //double dub = (double)text.getValue();
                //double dub2 = Double.parseDouble(text.toString());
                //System.out.println(dub);



               //BigDecimal dub = BigDecimal.valueOf((double) text.getValue());
                //dub = dub.movePointRight(4);

                //System.out.println(dub);
                //slider.setValue();

            } catch (Exception e){
                System.out.println(e.toString());
                error("please type a value between 0.0000 and 1.0 please " +e.toString());
            }
        }else{
/*            Double value = slider.getValue() / 10000d;
            DecimalFormat df = new DecimalFormat("#.####");
            String str = df.format(value);
            if (str.matches("1")) str = "1.0";
            if (str.matches("0")) str = "0.0";*/

            DecimalFormat dc = new DecimalFormat("0.0000");
            //String formattedText = dc.format(yourDouble);
            text.setText(dc.format(slider.getValue()/10000d));
        }


        render();
    }


    private final ItemListener uniformDisable = e -> {
        saveMessage.setVisible(true);

        Object source = e.getItemSelectable();

        if (source == volumetricPage.air_vfog_2) {
            volumetricPage.air_vfog_3.setEnabled(
                    !volumetricPage.air_vfog_3.isEnabled(
            ));
            volumetricPage.air_vfog_4.setEnabled(
                    !volumetricPage.air_vfog_4.isEnabled(
                    ));

        } else if (source == volumetricPage.water_vfog_2) {
            volumetricPage.water_vfog_3.setEnabled(
                    !volumetricPage.water_vfog_3.isEnabled(
                    ));
            volumetricPage.water_vfog_4.setEnabled(
                    !volumetricPage.water_vfog_4.isEnabled(
                    ));

        } else if(source == volumetricPage.weather_vfog_2) {
            volumetricPage.weather_vfog_3.setEnabled(
                    !volumetricPage.weather_vfog_3.isEnabled(
                    ));
            volumetricPage.weather_vfog_4.setEnabled(
                    !volumetricPage.weather_vfog_4.isEnabled(
                    ));

        } else if(source == volumetricPage.lava_vfog_2) {
            volumetricPage.lava_vfog_3.setEnabled(
                    !volumetricPage.lava_vfog_3.isEnabled(
                    ));
            volumetricPage.lava_vfog_4.setEnabled(
                    !volumetricPage.lava_vfog_4.isEnabled(
                    ));

        }else if(source == volumetricPage.lava_resistance_vfog_2) {
            volumetricPage.lava_resistance_vfog_3.setEnabled(
                    !volumetricPage.lava_resistance_vfog_3.isEnabled(
                    ));
            volumetricPage.lava_resistance_vfog_4.setEnabled(
                    !volumetricPage.lava_resistance_vfog_4.isEnabled(
                    ));

        }
        render();
    };

    private final FocusListener updateColour = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            Object source = e.getSource();
            saveMessage.setVisible(true);
            if (source == distancePage.ar) {
                disSliderConvert(distancePage.ar,distancePage.air_fog_3_r,true);
            }else if (source == distancePage.ag) {
                disSliderConvert(distancePage.ag,distancePage.air_fog_3_g,true);
            }else if (source == distancePage.ab) {
                disSliderConvert(distancePage.ab,distancePage.air_fog_3_b,true);
            }else if (source == distancePage.wr) {
                disSliderConvert(distancePage.wr,distancePage.water_fog_3_r,true);
            }else if (source == distancePage.wg) {
                disSliderConvert(distancePage.wg,distancePage.water_fog_3_g,true);
            }else if (source == distancePage.wb) {
                disSliderConvert(distancePage.wb,distancePage.water_fog_3_b,true);
            }else if (source == distancePage.er) {
                disSliderConvert(distancePage.er,distancePage.weather_fog_3_r,true);
            }else if (source == distancePage.eg) {
                disSliderConvert(distancePage.eg,distancePage.weather_fog_3_g,true);
            }else if (source == distancePage.eb) {
                disSliderConvert(distancePage.eb,distancePage.weather_fog_3_b,true);
            }else if (source == distancePage.lr) {
                disSliderConvert(distancePage.lr,distancePage.lava_fog_3_r,true);
            }else if (source == distancePage.lg) {
                disSliderConvert(distancePage.lg,distancePage.lava_fog_3_g,true);
            }else if (source == distancePage.lb) {
                disSliderConvert(distancePage.lb,distancePage.lava_fog_3_b,true);
            }else if (source == distancePage.lrr) {
                disSliderConvert(distancePage.lrr,distancePage.lava_resistance_fog_3_r,true);
            }else if (source == distancePage.lrg) {
                disSliderConvert(distancePage.lrg,distancePage.lava_resistance_fog_3_g,true);
            }else if (source == distancePage.lrb) {
                disSliderConvert(distancePage.lrb,distancePage.lava_resistance_fog_3_b,true);
            }
            render();
        }

    };
    private final ChangeListener updateColoura = e -> {
        Object source = e.getSource();
        saveMessage.setVisible(true);
        if (source == distancePage.ar) {
            disSliderConvert(distancePage.ar,distancePage.air_fog_3_r,true);
        }else if (source == distancePage.ag) {
            disSliderConvert(distancePage.ag,distancePage.air_fog_3_g,true);
        }else if (source == distancePage.ab) {
            disSliderConvert(distancePage.ab,distancePage.air_fog_3_b,true);
        }else if (source == distancePage.wr) {
            disSliderConvert(distancePage.wr,distancePage.water_fog_3_r,true);
        }else if (source == distancePage.wg) {
            disSliderConvert(distancePage.wg,distancePage.water_fog_3_g,true);
        }else if (source == distancePage.wb) {
            disSliderConvert(distancePage.wb,distancePage.water_fog_3_b,true);
        }else if (source == distancePage.er) {
            disSliderConvert(distancePage.er,distancePage.weather_fog_3_r,true);
        }else if (source == distancePage.eg) {
            disSliderConvert(distancePage.eg,distancePage.weather_fog_3_g,true);
        }else if (source == distancePage.eb) {
            disSliderConvert(distancePage.eb,distancePage.weather_fog_3_b,true);
        }else if (source == distancePage.lr) {
            disSliderConvert(distancePage.lr,distancePage.lava_fog_3_r,true);
        }else if (source == distancePage.lg) {
            disSliderConvert(distancePage.lg,distancePage.lava_fog_3_g,true);
        }else if (source == distancePage.lb) {
            disSliderConvert(distancePage.lb,distancePage.lava_fog_3_b,true);
        }else if (source == distancePage.lrr) {
            disSliderConvert(distancePage.lrr,distancePage.lava_resistance_fog_3_r,true);
        }else if (source == distancePage.lrg) {
            disSliderConvert(distancePage.lrg,distancePage.lava_resistance_fog_3_g,true);
        }else if (source == distancePage.lrb) {
            disSliderConvert(distancePage.lrb,distancePage.lava_resistance_fog_3_b,true);
        }
        render();
    };
    private final ChangeListener updateColourSlider = e -> {
        Object source = e.getSource();
        saveMessage.setVisible(true);
        if (source == distancePage.air_fog_3_r) {
            disSliderConvert(distancePage.ar,distancePage.air_fog_3_r,false);
        }else if (source == distancePage.air_fog_3_g) {
            disSliderConvert(distancePage.ag,distancePage.air_fog_3_g,false);
        }else if (source == distancePage.air_fog_3_b) {
            disSliderConvert(distancePage.ab,distancePage.air_fog_3_b,false);
        }else if (source == distancePage.water_fog_3_r) {
            disSliderConvert(distancePage.wr,distancePage.water_fog_3_r,false);
        }else if (source == distancePage.water_fog_3_g) {
            disSliderConvert(distancePage.wg,distancePage.water_fog_3_g,false);
        }else if (source == distancePage.water_fog_3_b) {
            disSliderConvert(distancePage.wb,distancePage.water_fog_3_b,false);
        }else if (source == distancePage.weather_fog_3_r) {
            disSliderConvert(distancePage.er,distancePage.weather_fog_3_r,false);
        }else if (source == distancePage.weather_fog_3_g) {
            disSliderConvert(distancePage.eg,distancePage.weather_fog_3_g,false);
        }else if (source == distancePage.weather_fog_3_b) {
            disSliderConvert(distancePage.eb,distancePage.weather_fog_3_b,false);
        }else if (source == distancePage.lava_fog_3_r) {
            disSliderConvert(distancePage.lr,distancePage.lava_fog_3_r,false);
        }else if (source == distancePage.lava_fog_3_g) {
            disSliderConvert(distancePage.lg,distancePage.lava_fog_3_g,false);
        }else if (source == distancePage.lava_fog_3_b) {
            disSliderConvert(distancePage.lb,distancePage.lava_fog_3_b,false);
        }else if (source == distancePage.lava_resistance_fog_3_r) {
            disSliderConvert(distancePage.lrr,distancePage.lava_resistance_fog_3_r,false);
        }else if (source == distancePage.lava_resistance_fog_3_g) {
            disSliderConvert(distancePage.lrg,distancePage.lava_resistance_fog_3_g,false);
        }else if (source == distancePage.lava_resistance_fog_3_b) {
            disSliderConvert(distancePage.lrb,distancePage.lava_resistance_fog_3_b,false);
        }
        render();
    };

    private  void disSliderConvert(JSpinner text, JSlider slider, boolean textToSlider){

        if (textToSlider){
/*            int num;
            try {
                num = ;
            } catch (Exception e){
                error("please enter number between 0 and 255");
                num = 0;
            }
            if (num >255) num=255;
            if (num <0) num=0;*/
            slider.setValue((int)text.getValue());
        }else{;
            text.setValue(slider.getValue());
        }

        render();
    }

    private final FocusListener doubleInputTest = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            saveMessage.setVisible(true);
            JTextField source = (JTextField) e.getSource();
            double test;
            try {
                test = Double.parseDouble(source.getText());
                if (test < 0.0) test = 0.0;
                //if (test > 1.0) error("recommend value up to 1.0 but this might work");

            }catch(Exception ex){
                error("was not a number");
                test = 0.0;

            }
            source.setText(test+"");
            render();
        }
    };

    private final ActionListener doubleInputTesta = this::doubleInputTestMethoda;
    private final FocusListener doubleInputTestMax1 = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            saveMessage.setVisible(true);
            JTextField source = (JTextField) e.getSource();
            double test;
            try {
                test = Double.parseDouble(source.getText());
                if (test < 0.0) test = 0.0;
                System.out.println("max 1 running");
                if (test > 1.0) test=1.0;

            }catch(Exception ex){
                error("was not a number");
                test = 0.0;

            }
            if (source == volumetricPage.air_vfog_1){
                volumetricPage.a1s.setValue((int)(test * 100));
            }else if(source == volumetricPage.water_vfog_1){
                volumetricPage.w1s.setValue((int)(test * 100));
            }else if(source == volumetricPage.weather_vfog_1){
                volumetricPage.e1s.setValue((int)(test * 100));
            }else if(source == volumetricPage.lava_vfog_1){
                volumetricPage.l1s.setValue((int)(test * 100));
            }else if(source == volumetricPage.lava_resistance_vfog_1){
                volumetricPage.lr1s.setValue((int)(test * 100));
            }

            source.setText(test+"");
            render();
        }
    };

    private final ActionListener doubleInputTestaMax1 = this::doubleInputTestMethodaMax1;
    private  void doubleInputTestMethodaMax1(ActionEvent e){
        JTextField source = (JTextField) e.getSource();
        saveMessage.setVisible(true);
        double test;
        try {
            test = Double.parseDouble(source.getText());
            if (test < 0) test = 0.0;
            if (test > 1.0) test=1.0;

        }catch(Exception ex){
            //error("was not a number");
            test = 0.0;

        }
        if (source == volumetricPage.air_vfog_1){
            volumetricPage.a1s.setValue((int)(test * 100));
        }else if(source == volumetricPage.water_vfog_1){
            volumetricPage.w1s.setValue((int)(test * 100));
        }else if(source == volumetricPage.weather_vfog_1){
            volumetricPage.e1s.setValue((int)(test * 100));
        }else if(source == volumetricPage.lava_vfog_1){
            volumetricPage.l1s.setValue((int)(test * 100));
        }else if(source == volumetricPage.lava_resistance_vfog_1){
            volumetricPage.lr1s.setValue((int)(test * 100));
        }
        //error("test worked");
        source.setText(test+"");
        render();
    }
    private  void doubleInputTestMethoda(ActionEvent e){
        JTextField source = (JTextField) e.getSource();
        saveMessage.setVisible(true);
        double test;
        try {
            test = Double.parseDouble(source.getText());
            if (test < 0) test = 0.0;


        }catch(Exception ex){
            error("was not a number");
            test = 0.0;

        }
        //error("test worked");
        source.setText(test+"");
        render();
    }

    private final FocusListener integerInputTest = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {}
        public void focusLost(FocusEvent e) {
            saveMessage.setVisible(true);
            JTextField source = (JTextField) e.getSource();
            int test;
            try {
                test = Integer.parseInt(source.getText());
                if (test <= 0) {test = 0;}
                if (test > 128) {
                    if ((e.getSource() == volumetricPage.air_vfog_4) ||
                            (e.getSource() == volumetricPage.water_vfog_4) ||
                            (e.getSource() == volumetricPage.weather_vfog_4) ||
                            (e.getSource() == volumetricPage.lava_vfog_4) ||
                            (e.getSource() == volumetricPage.lava_resistance_vfog_4)) {
                        test = 128;
                        error("max zero height limit of 128! \n\n" +
                                "logic would assume it would be 256 but it isn't ¯\\_(ツ)_/¯");
                    }
                }

            }catch(Exception ex){
                error("was not a whole number");
                test = 0;

            }
            source.setText(test+"");
            render();
        }
    };
    private final ActionListener integerInputTesta = e -> {
        saveMessage.setVisible(true);
        JTextField source = (JTextField) e.getSource();
        int test;
        try {
            test = Integer.parseInt(source.getText());
            if (test <= 0) {test = 0;}
            if (test >= 128) {
                if ((e.getSource() == volumetricPage.air_vfog_4) ||
                        (e.getSource() == volumetricPage.water_vfog_4) ||
                        (e.getSource() == volumetricPage.weather_vfog_4) ||
                        (e.getSource() == volumetricPage.lava_vfog_4) ||
                        (e.getSource() == volumetricPage.lava_resistance_vfog_4)) {
                    test = 128;
                }
            }

        }catch(Exception ex){
            //error("was not a whole number");
            test = 0;

        }
        source.setText(test+"");
        render();
    };

    private final FocusListener namespaceTest = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) { }
        @Override
        public void focusLost(FocusEvent e) {
            saveMessage.setVisible(true);
            JTextField source = (JTextField) e.getSource();
            String str = source.getText();
            str = str.replace(" ","_");
            str = str.trim();
            if (str.contains(":")){
                if (!str.startsWith(":")){
                    if (str.endsWith(":")){
                        error("identifier requires a namespace and intentification name  example \"namespace:id_name\", and cannot end with :");
                        str= "namespace:custom_id";
                    }
                }else{
                    error("identifier requires a namespace and intentification name  example \"namespace:id_name\", and cannot start with :");
                    str= "namespace:custom_id";
                }
            }else{
                error("identifier requires a namespace and intentification name  example \"namespace:id_name\"");
                str= "namespace:custom_id";
            }

            source.setText(str);
            indentifier = str;
            render();
        }
    };
    private final ActionListener namespaceTesta = e -> {
        saveMessage.setVisible(true);
        JTextField source = (JTextField) e.getSource();
        String str = source.getText();
        str = str.replace(" ","_");
        str = str.trim();
        if (str.contains(":")){
            if (!str.startsWith(":")){
                if (str.endsWith(":")){
                    error("identifier requires a namespace and intentification name  example \"namespace:id_name\", and cannot end with :");
                    str= "namespace:custom_id";
                }
            }else{
                error("identifier requires a namespace and intentification name  example \"namespace:id_name\", and cannot start with :");
                str= "namespace:custom_id";
            }
        }else{
            error("identifier requires a namespace and intentification name  example \"namespace:id_name\"");
            str= "namespace:custom_id";
        }

        source.setText(str);
        indentifier = str;
        render();
    };



    public void error(String text){
        //System.out.println("error: " + text);
        JOptionPane.showMessageDialog(frame,
                text,
                "message",
                JOptionPane.PLAIN_MESSAGE);


    }
    private boolean nameManaged;
//////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    Main(String titleAppend, String filePath, String id, boolean closeFullProgramOnExit,boolean isManaged_Etc_UserCantName) {
        loadNew();


      // settingPage distancePage = new settingPage();
     // volumetric volumetricPage = new volumetric();
     // coefficients coefficiantPage = new coefficients();
        nameManaged=isManaged_Etc_UserCantName;
        //wrapperF.fileDrop.setEnabled(!isManaged_Etc_UserCantName);
        saveAsMenu.setEnabled(!isManaged_Etc_UserCantName);
        //newFog.setEnabled(!isManaged_Etc_UserCantName);
        newFogOpen.setEnabled(!isManaged_Etc_UserCantName);


        if (!filePath.equals("")){
            FILE = new File(filePath);
            FILENAME= FILE.getName();

        }
        if (!id.equals("")) indentifier = id;



        initMenu();
        initSpinners();
       //frame.setContentPane(distancePage.distance_content);


        //if (closeFullProgramOnExit)frame.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);


        wrapperF.tabbedPane1.add("<html>[Vanilla]<br> Distance Fog</html>", distancePage.distance_content);
        wrapperF.tabbedPane1.add("<html>[RTX]<br> Volumetric Fog (e.g Godrays)</html>",volumetricPage.volumetric_content);
        wrapperF.tabbedPane1.add("<html>[RTX]<br> Colour values of materials</html>",coefficiantPage.coefficient_content);

        distancePage.scrll.getVerticalScrollBar().setUnitIncrement(20);
        volumetricPage.scrll.getVerticalScrollBar().setUnitIncrement(20);
        coefficiantPage.scrll.getVerticalScrollBar().setUnitIncrement(20);

        frame.setContentPane(wrapperF.mainWindow);
        if (titleAppend.matches("")) titleAppend = "¯\\_(ツ)_/¯";
        frame.setTitle("Fog Editor BETA: " + titleAppend);

        frame.setSize(610,700); //pixel size of frame in width then height
        frame.setPreferredSize(new Dimension(610,700));
        frame.setMaximumSize(new Dimension(610,700));

        setInvisisbles();
        //initMenu();
        initWrapper();
        initRadios();
        initUpdatables();
        initFileDrop();
        initAnswers();
        initDistanceSliders();
        changeDistanceType();
        initVolumeSliders();
        if (isManaged_Etc_UserCantName) hideManagedSettings();

        frame.setDefaultCloseOperation(WindowConstants.DO_NOTHING_ON_CLOSE);

        wrapperF.fileNameField.setText(FILENAME);
        wrapperF.indentifierBox.setText(indentifier);
        render();
        frame.setVisible(true);

        if (!filePath.matches("")){
            loadJson(new File(filePath));
        }


        frame.addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (saveMessage.isVisible()) {
                    int result = JOptionPane.showConfirmDialog(frame, "Unsaved changes detected!\n(you can save in 'File/Save')\n \n Exit without saving?", "Exit?",
                            JOptionPane.YES_NO_OPTION,
                            JOptionPane.QUESTION_MESSAGE);
                    if (result == JOptionPane.YES_OPTION) {
                        closing();
                    } else if (result == JOptionPane.NO_OPTION) {
                        //label.setText("You selected: No");
                    } else {
                        //label.setText("None selected");
                    }
                }else{
                    closing();
                }

            }
        });

    }

    private void closing(){
        System.out.println("exiting");

        frame.dispose();

    }

    private void set10000Spinners(JSpinner s){

        s.setModel(new SpinnerNumberModel(0, 0, 10000, 1));
       // s.setEditor(new JSpinner.NumberEditor(s,"#####"));
    }

    private void initSpinners(){

       // coefficiantPage.fuckingCunt.setEditor(new JSpinner.NumberEditor(coefficiantPage.fuckingCunt, "0.0000"));

        distancePage.ar.setModel(spin255());
        distancePage.ag.setModel(spin255());
        distancePage.ab.setModel(spin255());

        distancePage.wr.setModel(spin255());
        distancePage.wg.setModel(spin255());
        distancePage.wb.setModel(spin255());

        distancePage.er.setModel(spin255());
        distancePage.eg.setModel(spin255());
        distancePage.eb.setModel(spin255());

        distancePage.lr.setModel(spin255());
        distancePage.lg.setModel(spin255());
        distancePage.lb.setModel(spin255());

        distancePage.lrr.setModel(spin255());
        distancePage.lrg.setModel(spin255());
        distancePage.lrb.setModel(spin255());

/*        set10000Spinners(coefficiantPage.a_a_r_1);
        set10000Spinners(coefficiantPage.a_a_g_1);
        set10000Spinners(coefficiantPage.a_a_b_1);
        set10000Spinners(coefficiantPage.a_s_r_1);
        set10000Spinners(coefficiantPage.a_s_g_1);
        set10000Spinners(coefficiantPage.a_s_b_1);
        set10000Spinners(coefficiantPage.w_a_r_1);
        set10000Spinners(coefficiantPage.w_a_g_1);
        set10000Spinners(coefficiantPage.w_a_b_1);
        set10000Spinners(coefficiantPage.w_s_r_1);
        set10000Spinners(coefficiantPage.w_s_g_1);
        set10000Spinners(coefficiantPage.w_s_b_1);
        set10000Spinners(coefficiantPage.car1);
        set10000Spinners(coefficiantPage.cag1);
        set10000Spinners(coefficiantPage.cab1);
        set10000Spinners(coefficiantPage.csr1);
        set10000Spinners(coefficiantPage.csg1);
        set10000Spinners(coefficiantPage.csb1);*/
    }

    private final ActionListener explorerOpen = e -> {
        //System.out.println(System.getProperty("user.dir"));
        final JFileChooser chooser = new JFileChooser(BiomeMain.activeDirectory.getAbsolutePath()+"\\fogs");
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new utilityTraben.fogFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showOpenDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
            File newf;
            JMenuItem source = (JMenuItem) e.getSource();
            if (source == newFogOpen){
                newf = chooser.getSelectedFile();
                loadJson(newf);
                FILE = newf;
                FILENAME = FILE.getName();

            }else if(source == importExisting){
                //keep current name and fog id and file
                File hold = new File(FILE.getAbsolutePath());
                String holdid = indentifier;
                newf = chooser.getSelectedFile();
                loadJson(newf);
                indentifier = holdid;
                newf = hold;
                FILE = newf;
                FILENAME = FILE.getName();
                wrapperF.indentifierBox.setText(indentifier);
                wrapperF.fileNameField.setText(FILENAME);
            }



        } else {
            //log.append("Open command cancelled by user." + newline);

        }
    };



    private final ActionListener explorerSave = e -> {
        System.out.println("saveas");
        String path;
        if(BiomeMain.activeDirectory !=null){
            path = BiomeMain.activeDirectory.getAbsolutePath()+"\\fogs";
        }else{
            path = System.getProperty("user.dir");
        }

        final JFileChooser chooser = new JFileChooser(path);
        chooser.setFileSelectionMode(JFileChooser.FILES_AND_DIRECTORIES);
        chooser.addChoosableFileFilter(new utilityTraben.fogFileFilter());
        chooser.setAcceptAllFileFilterUsed(false);

        int returnVal = chooser.showSaveDialog(frame);

        if (returnVal == JFileChooser.APPROVE_OPTION) {
            //This is where a real application would open the file.
            //log.append("Opening: " + file.getName() + "." + newline);
            if (chooser.getSelectedFile().getAbsolutePath().endsWith(".json")) {
                FILE = chooser.getSelectedFile();
            }else{
                FILE = new File(chooser.getSelectedFile().getAbsolutePath()+".json");
            }
            FILENAME = FILE.getName();
            wrapperF.fileNameField.setText(FILENAME);
            saveMessage.setVisible(false);
            save();

        } else {
            //log.append("Open command cancelled by user." + newline);
            utilityTraben.message(frame, "uknown error");
        }
    };



    private void initMenu() {


        menu.add(newFog);
        menu.add(newFogOpen);
        menu.add(new JSeparator());
        menu.add(importExisting);
        menu.add(new JSeparator());
        menu.add(saveMenu);
        menu.add(saveAsMenu);


        //JSeparator menuSep = new JSeparator(1);
        JLabel sep = new JLabel("|");
        sep.setEnabled(false);

        menuBar.add(menu);
        //menuBar.add(menuSep);
        menuBar.add(sep);
        menuBar.add(helpM);
        helpM.setMaximumSize(new Dimension(40,25));
        menuBar.add(sep);
        frame.setJMenuBar(menuBar);
        menuBar.add(saveMessage);
        saveMessage.setVisible(false);
        saveMessage.setForeground(new Color(180,0,0));

        saveMenu.addActionListener(saveEvt);
        saveAsMenu.addActionListener(explorerSave);
        newFog.addActionListener(e -> loadNew());

        newFogOpen.addActionListener(explorerOpen);
        importExisting.addActionListener(explorerOpen);

        helpM.addActionListener(e -> new help(2));

    }

        private void hideManagedSettings(){
        wrapperF.indentifierBox.setEnabled(false);
        wrapperF.fileNameField.setEnabled(false);



    }

    private void initVolumeSliders(){
        uniqueChanger(volumetricPage.a1s,volumetricPage.air_vfog_1);
        uniqueChanger(volumetricPage.w1s,volumetricPage.water_vfog_1);
        uniqueChanger(volumetricPage.e1s,volumetricPage.weather_vfog_1);
        uniqueChanger(volumetricPage.l1s,volumetricPage.lava_vfog_1);
        uniqueChanger(volumetricPage.lr1s,volumetricPage.lava_resistance_vfog_1);

    }

    private void uniqueChanger(JSlider slider, JTextField field){
        slider.addChangeListener(e -> {
            saveMessage.setVisible(true);
            field.setText(( slider.getValue() / 100d )+"");
        });
    }

    private void initDistanceSliders(){
        distancePage.as1.addChangeListener(distanceSliders);
        distancePage.as2.addChangeListener(distanceSliders);
        distancePage.ws1.addChangeListener(distanceSliders);
        distancePage.ws2.addChangeListener(distanceSliders);
        distancePage.es1.addChangeListener(distanceSliders);
        distancePage.es2.addChangeListener(distanceSliders);
        distancePage.ls1.addChangeListener(distanceSliders);
        distancePage.ls2.addChangeListener(distanceSliders);
        distancePage.lrs1.addChangeListener(distanceSliders);
        distancePage.lrs2.addChangeListener(distanceSliders);
        distancePage.as1.addFocusListener(distanceSlidersF);
        distancePage.as2.addFocusListener(distanceSlidersF);
        distancePage.ws1.addFocusListener(distanceSlidersF);
        distancePage.ws2.addFocusListener(distanceSlidersF);
        distancePage.es1.addFocusListener(distanceSlidersF);
        distancePage.es2.addFocusListener(distanceSlidersF);
        distancePage.ls1.addFocusListener(distanceSlidersF);
        distancePage.ls2.addFocusListener(distanceSlidersF);
        distancePage.lrs1.addFocusListener(distanceSlidersF);
        distancePage.lrs2.addFocusListener(distanceSlidersF);
    }
        //swapListeners(distancePage.air_fog_1,false);
/*    private void swapListeners(JTextField field,boolean toLimitOf1){
        if (toLimitOf1) {

            for (ActionListener a:
            field.getActionListeners()) {
                field.removeActionListener(a);
            }
            for (FocusListener a:
                    field.getFocusListeners()) {
                field.removeFocusListener(a);
            }
            field.addActionListener(doubleInputTestaMax1);
            field.addFocusListener(doubleInputTestMax1);
        }else{

            for (ActionListener a:
                    field.getActionListeners()) {
                field.removeActionListener(a);
            }
            for (FocusListener a:
                    field.getFocusListeners()) {
                field.removeFocusListener(a);
            }
            field.addActionListener(doubleInputTesta);
            field.addFocusListener(doubleInputTest);
        }
    }*/

    private void changeDistanceType(){
        if (distancePage.air_fog_4.getSelectedIndex()==0){
            // do render
            distancePage.at1.setText("<html> Start distance<br/> *Percentage</html>");
            distancePage.at2.setText("<html> End distance<br/> *Percentage</html>");
            distancePage.as1.setVisible(true);
            distancePage.as2.setVisible(true);
            distancePage.as1.setValue((int)(zeroToOne(distancePage.air_fog_1.getText()) * 100));
            distancePage.as2.setValue((int)(zeroToOne(distancePage.air_fog_2.getText()) * 100));
            distancePage.air_fog_1.setEnabled(false);
            distancePage.air_fog_2.setEnabled(false);

        }else{
            //do fixed
            distancePage.at1.setText("<html> Start distance<br/> *Blocks</html>");
            distancePage.at2.setText("<html> End distance<br/> *Blocks</html>");
            distancePage.as1.setVisible(false);
            distancePage.as2.setVisible(false);
            distancePage.air_fog_1.setEnabled(true);
            distancePage.air_fog_2.setEnabled(true);

        }
        if (distancePage.water_fog_4.getSelectedIndex()==0){
            // do render
            distancePage.wt1.setText("<html>Start distance<br/> *Percentage</html>");
            distancePage.wt2.setText("<html>End distance<br/> *Percentage</html>");
            distancePage.ws1.setVisible(true);
            distancePage.ws2.setVisible(true);
            distancePage.ws1.setValue((int)(zeroToOne(distancePage.water_fog_1.getText()) * 100));
            distancePage.ws2.setValue((int)(zeroToOne(distancePage.water_fog_2.getText()) * 100));
            distancePage.water_fog_1.setEnabled(false);
            distancePage.water_fog_2.setEnabled(false);
        }else{
            //do fixed
            distancePage.wt1.setText("<html>Start distance<br/> *Blocks</html>");
            distancePage.wt2.setText("<html>End distance<br/> *Blocks</html>");
            distancePage.ws1.setVisible(false);
            distancePage.ws2.setVisible(false);
            distancePage.water_fog_1.setEnabled(true);
            distancePage.water_fog_2.setEnabled(true);
        }
        if (distancePage.weather_fog_4.getSelectedIndex()==0){
            // do render
            distancePage.et1.setText("<html>Start distance<br/> *Percentage</html>");
            distancePage.et2.setText("<html>End distance<br/> *Percentage</html>");
            distancePage.es1.setVisible(true);
            distancePage.es2.setVisible(true);
            distancePage.es1.setValue((int)(zeroToOne(distancePage.weather_fog_1.getText()) * 100));
            distancePage.es2.setValue((int)(zeroToOne(distancePage.weather_fog_2.getText()) * 100));
            distancePage.weather_fog_1.setEnabled(false);
            distancePage.weather_fog_2.setEnabled(false);
        }else{
            //do fixed
            distancePage.et1.setText("<html>Start distance<br/> *Blocks</html>");
            distancePage.et2.setText("<html>End distance<br/> *Blocks</html>");
            distancePage.es1.setVisible(false);
            distancePage.es2.setVisible(false);
            distancePage.weather_fog_1.setEnabled(true);
            distancePage.weather_fog_2.setEnabled(true);
        }
        if (distancePage.lava_fog_4.getSelectedIndex()==0){
            // do render
            distancePage.lt1.setText("<html>Start distance<br/> *Percentage</html>");
            distancePage.lt2.setText("<html>End distance<br/> *Percentage</html>");
            distancePage.ls1.setVisible(true);
            distancePage.ls2.setVisible(true);
            distancePage.ls1.setValue((int)(zeroToOne(distancePage.lava_fog_1.getText()) * 100));
            distancePage.ls2.setValue((int)(zeroToOne(distancePage.lava_fog_2.getText()) * 100));
            distancePage.lava_fog_1.setEnabled(false);
            distancePage.lava_fog_2.setEnabled(false);
        }else{
            //do fixed
            distancePage.lt1.setText("<html>Start distance<br/> *Blocks</html>");
            distancePage.lt2.setText("<html>End distance<br/> *Blocks</html>");
            distancePage.ls1.setVisible(false);
            distancePage.ls2.setVisible(false);
            distancePage.lava_fog_1.setEnabled(true);
            distancePage.lava_fog_2.setEnabled(true);
        }
        if (distancePage.lava_resistance_fog_4.getSelectedIndex()==0){
            // do render
            distancePage.lrt1.setText("<html>Start distance<br/> *Percentage</html>");
            distancePage.lrt2.setText("<html>End distance<br/> *Percentage</html>");
            distancePage.lrs1.setVisible(true);
            distancePage.lrs2.setVisible(true);
            distancePage.lrs1.setValue((int)(zeroToOne(distancePage.lava_resistance_fog_1.getText()) * 100));
            distancePage.lrs2.setValue((int)(zeroToOne(distancePage.lava_resistance_fog_2.getText()) * 100));
            distancePage.lava_resistance_fog_1.setEnabled(false);
            distancePage.lava_resistance_fog_2.setEnabled(false);
        }else{
            //do fixed
            distancePage.lrt1.setText("<html>Start distance<br/> *Blocks</html>");
            distancePage.lrt2.setText("<html>End distance<br/> *Blocks</html>");
            distancePage.lrs1.setVisible(false);
            distancePage.lrs2.setVisible(false);
            distancePage.lava_resistance_fog_1.setEnabled(true);
            distancePage.lava_resistance_fog_2.setEnabled(true);
        }


    }
    private Double zeroToOne(String str){
        try {
            double bob = Double.parseDouble(str);
            if (bob > 1.0) bob = 1.0;
            if (bob < 0.0) bob = 0.0;
            return bob;
        }catch(NumberFormatException e){
            return 0.0;
        }

    }
    private Double doubleReturn(String str){
        try {
            return Double.parseDouble(str);
        }catch(NumberFormatException e){
            return 0.0;
        }

    }

    private final ChangeListener distanceSliders = e -> {
        Object source= e.getSource();
        saveMessage.setVisible(true);
        if (source == distancePage.as1) {
            String str = ""+(distancePage.as1.getValue()/100d);
            distancePage.air_fog_1.setText(str);
        }else if (source == distancePage.as2) {
            String str = ""+(distancePage.as2.getValue()/100d);
            distancePage.air_fog_2.setText(str);
        }
        if (source == distancePage.ws1) {
            String str = ""+(distancePage.ws1.getValue()/100d);
            distancePage.water_fog_1.setText(str);
        }else if (source == distancePage.ws2) {
            String str = ""+(distancePage.ws2.getValue()/100d);
            distancePage.water_fog_2.setText(str);
        }
        if (source == distancePage.es1) {
            String str = ""+(distancePage.es1.getValue()/100d);
            distancePage.weather_fog_1.setText(str);
        }else if (source == distancePage.es2) {
            String str = ""+(distancePage.es2.getValue()/100d);
            distancePage.weather_fog_2.setText(str);
        }
        if (source == distancePage.ls1) {
            String str = ""+(distancePage.ls1.getValue()/100d);
            distancePage.lava_fog_1.setText(str);
        }else if (source == distancePage.ls2) {
            String str = ""+(distancePage.ls2.getValue()/100d);
            distancePage.lava_fog_2.setText(str);
        }
        if (source == distancePage.lrs1) {
            String str = ""+(distancePage.lrs1.getValue()/100d);
            distancePage.lava_resistance_fog_1.setText(str);
        }else if (source == distancePage.lrs2) {
            String str = ""+(distancePage.lrs2.getValue()/100d);
            distancePage.lava_resistance_fog_2.setText(str);
        }
    };


    private final FocusListener distanceSlidersF = new FocusListener() {
        @Override
        public void focusGained(FocusEvent e) {

        }

        @Override
        public void focusLost(FocusEvent e) {
            saveMessage.setVisible(true);
            if (

                    ((doubleReturn(distancePage.air_fog_1.getText()) >= doubleReturn(distancePage.air_fog_2.getText()))) ||
                            ((doubleReturn(distancePage.water_fog_1.getText()) >= doubleReturn(distancePage.water_fog_2.getText()))) ||
                            ((doubleReturn(distancePage.weather_fog_1.getText()) >= doubleReturn(distancePage.weather_fog_2.getText()))) ||
                            ((doubleReturn(distancePage.lava_fog_1.getText()) >= doubleReturn(distancePage.lava_fog_2.getText()))) ||
                            ((doubleReturn(distancePage.lava_resistance_fog_1.getText()) >= doubleReturn(distancePage.lava_resistance_fog_2.getText())))
            ){
                error("WARNING:End distance CANNOT be smaller than Fog start");
            }
        }
    };

    private final ActionListener renderChange = e -> {
        saveMessage.setVisible(true);
        changeDistanceType();

        render();
    };


    private void initAnswers(){
        distancePage.qr1.addActionListener(renderHelp);
        distancePage.qr2.addActionListener(renderHelp);
        distancePage.qr3.addActionListener(renderHelp);
        distancePage.qr4.addActionListener(renderHelp);
        distancePage.qr5.addActionListener(renderHelp);
    }
    private final ActionListener renderHelp =  new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {
            JFrame bob = new JFrame();
            bob.setTitle("Art by @deku");
            JLabel bob2 = new JLabel();
            ImageIcon back;
            try {
                //BufferedImage bob3 = d(new File("assets/render_help.png"));
                BufferedImage bob3 = ImageIO.read(getClass().getResource("assets/render_help.png"));
                bob3 = resize(bob3,450,700);
                back = new ImageIcon(bob3);
            }catch (NullPointerException e2  ){
                back = new ImageIcon(getClass().getResource("assets/ayy.jpg"));
                System.out.println("null");
            }catch (IOException b){
                back = new ImageIcon(getClass().getResource("assets/ayy.jpg"));
            }
            bob2.setIcon(back);
            bob.add(bob2);
            bob.setSize(450,700); //pixel size of frame in width then height
            bob.setPreferredSize(new Dimension(450,700));
            //bob.setMaximumSize(new Dimension(450,700));
            bob2.setSize(450,700);
            bob.setVisible(true);
        }
    };

    private static BufferedImage resize(BufferedImage img, int newW, int newH) {
        Image tmp = img.getScaledInstance(newW, newH, Image.SCALE_SMOOTH);
        BufferedImage dimg = new BufferedImage(newW, newH, BufferedImage.TYPE_INT_ARGB);

        Graphics2D g2d = dimg.createGraphics();
        g2d.drawImage(tmp, 0, 0, null);
        g2d.dispose();

        return dimg;
    }

    private void initFileDrop(){

     /*   // end filesDropped
        new  FileDrop( wrapperF.fileDrop, files -> {

            // handle file drop

            if (files.length > 1) {
                error("please only drop one file");
            }else {
                if (files[0].getName().contains(".json")){
                    //error(files[0].getName() + " Was loaded succesfully!");
                    loadJson(files[0]);
                }else {
                    error("file is not a \".json\" ABORTING");
                }
            }

        }); // end FileDrop.Listener
*/
    }


/////FUCKING LONG
    private void initUpdatables(){

        distancePage.air_fog_4.addActionListener(renderChange);
        distancePage.water_fog_4.addActionListener(renderChange);
        distancePage.weather_fog_4.addActionListener(renderChange);
        distancePage.lava_fog_4.addActionListener(renderChange);
        distancePage.lava_resistance_fog_4.addActionListener(renderChange);



       // coefficiantPage.a_a_r_1.;

        coefficiantPage.a_a_r_1.addFocusListener(updateOthers);
        coefficiantPage.a_a_r_2.addChangeListener(updateOthersSlider);
        coefficiantPage.a_a_g_1.addFocusListener(updateOthers);
        coefficiantPage.a_a_g_2.addChangeListener(updateOthersSlider);
        coefficiantPage.a_a_b_1.addFocusListener(updateOthers);
        coefficiantPage.a_a_b_2.addChangeListener(updateOthersSlider);
        coefficiantPage.a_s_r_1.addFocusListener(updateOthers);
        coefficiantPage.a_s_r_2.addChangeListener(updateOthersSlider);
        coefficiantPage.a_s_g_1.addFocusListener(updateOthers);
        coefficiantPage.a_s_g_2.addChangeListener(updateOthersSlider);
        coefficiantPage.a_s_b_1.addFocusListener(updateOthers);
        coefficiantPage.a_s_b_2.addChangeListener(updateOthersSlider);

        coefficiantPage.w_a_r_1.addFocusListener(updateOthers);
        coefficiantPage.w_a_r_2.addChangeListener(updateOthersSlider);
        coefficiantPage.w_a_g_1.addFocusListener(updateOthers);
        coefficiantPage.w_a_g_2.addChangeListener(updateOthersSlider);
        coefficiantPage.w_a_b_1.addFocusListener(updateOthers);
        coefficiantPage.w_a_b_2.addChangeListener(updateOthersSlider);
        coefficiantPage.w_s_r_1.addFocusListener(updateOthers);
        coefficiantPage.w_s_r_2.addChangeListener(updateOthersSlider);
        coefficiantPage.w_s_g_1.addFocusListener(updateOthers);
        coefficiantPage.w_s_g_2.addChangeListener(updateOthersSlider);
        coefficiantPage.w_s_b_1.addFocusListener(updateOthers);
        coefficiantPage.w_s_b_2.addChangeListener(updateOthersSlider);

        coefficiantPage.car1.addFocusListener(updateOthers);
        coefficiantPage.car2.addChangeListener(updateOthersSlider);
        coefficiantPage.cag1.addFocusListener(updateOthers);
        coefficiantPage.cag2.addChangeListener(updateOthersSlider);
        coefficiantPage.cab1.addFocusListener(updateOthers);
        coefficiantPage.cab2.addChangeListener(updateOthersSlider);
        coefficiantPage.csr1.addFocusListener(updateOthers);
        coefficiantPage.csr2.addChangeListener(updateOthersSlider);
        coefficiantPage.csg1.addFocusListener(updateOthers);
        coefficiantPage.csg2.addChangeListener(updateOthersSlider);
        coefficiantPage.csb1.addFocusListener(updateOthers);
        coefficiantPage.csb2.addChangeListener(updateOthersSlider);

        volumetricPage.air_vfog_2.addItemListener(uniformDisable);
        volumetricPage.water_vfog_2.addItemListener(uniformDisable);
        volumetricPage.weather_vfog_2.addItemListener(uniformDisable);
        volumetricPage.lava_vfog_2.addItemListener(uniformDisable);
        volumetricPage.lava_resistance_vfog_2.addItemListener(uniformDisable);



        distancePage.ar.addFocusListener(updateColour);
        distancePage.air_fog_3_r.addChangeListener(updateColourSlider);
        distancePage.ag.addFocusListener(updateColour);
        distancePage.air_fog_3_g.addChangeListener(updateColourSlider);
        distancePage.ab.addFocusListener(updateColour);
        distancePage.air_fog_3_b.addChangeListener(updateColourSlider);

        distancePage.wr.addFocusListener(updateColour);
        distancePage.water_fog_3_r.addChangeListener(updateColourSlider);
        distancePage.wg.addFocusListener(updateColour);
        distancePage.water_fog_3_g.addChangeListener(updateColourSlider);
        distancePage.wb.addFocusListener(updateColour);
        distancePage.water_fog_3_b.addChangeListener(updateColourSlider);

        distancePage.er.addFocusListener(updateColour);
        distancePage.weather_fog_3_r.addChangeListener(updateColourSlider);
        distancePage.eg.addFocusListener(updateColour);
        distancePage.weather_fog_3_g.addChangeListener(updateColourSlider);
        distancePage.eb.addFocusListener(updateColour);
        distancePage.weather_fog_3_b.addChangeListener(updateColourSlider);

        distancePage.lr.addFocusListener(updateColour);
        distancePage.lava_fog_3_r.addChangeListener(updateColourSlider);
        distancePage.lg.addFocusListener(updateColour);
        distancePage.lava_fog_3_g.addChangeListener(updateColourSlider);
        distancePage.lb.addFocusListener(updateColour);
        distancePage.lava_fog_3_b.addChangeListener(updateColourSlider);

        distancePage.lrr.addFocusListener(updateColour);
        distancePage.lava_resistance_fog_3_r.addChangeListener(updateColourSlider);
        distancePage.lrg.addFocusListener(updateColour);
        distancePage.lava_resistance_fog_3_g.addChangeListener(updateColourSlider);
        distancePage.lrb.addFocusListener(updateColour);
        distancePage.lava_resistance_fog_3_b.addChangeListener(updateColourSlider);


        ///// all text entries for doubles
        distancePage.air_fog_1.addFocusListener(doubleInputTest);
        distancePage.air_fog_2.addFocusListener(doubleInputTest);
        distancePage.water_fog_1.addFocusListener(doubleInputTest);
        distancePage.water_fog_2.addFocusListener(doubleInputTest);
        distancePage.weather_fog_1.addFocusListener(doubleInputTest);
        distancePage.weather_fog_2.addFocusListener(doubleInputTest);
        distancePage.lava_fog_1.addFocusListener(doubleInputTest);
        distancePage.lava_fog_2.addFocusListener(doubleInputTest);
        distancePage.lava_resistance_fog_1.addFocusListener(doubleInputTest);
        distancePage.lava_resistance_fog_2.addFocusListener(doubleInputTest);

        volumetricPage.air_vfog_1.addFocusListener(doubleInputTestMax1);
        volumetricPage.water_vfog_1.addFocusListener(doubleInputTestMax1);
        volumetricPage.weather_vfog_1.addFocusListener(doubleInputTestMax1);
        volumetricPage.lava_vfog_1.addFocusListener(doubleInputTestMax1);
        volumetricPage.lava_resistance_vfog_1.addFocusListener(doubleInputTestMax1);

        ////all text entried for int

        volumetricPage.air_vfog_3.addFocusListener(integerInputTest);
        volumetricPage.water_vfog_3.addFocusListener(integerInputTest);
        volumetricPage.weather_vfog_3.addFocusListener(integerInputTest);
        volumetricPage.lava_vfog_3.addFocusListener(integerInputTest);
        volumetricPage.lava_resistance_vfog_3.addFocusListener(integerInputTest);

        volumetricPage.air_vfog_4.addFocusListener(integerInputTest);
        volumetricPage.water_vfog_4.addFocusListener(integerInputTest);
        volumetricPage.weather_vfog_4.addFocusListener(integerInputTest);
        volumetricPage.lava_vfog_4.addFocusListener(integerInputTest);
        volumetricPage.lava_resistance_vfog_4.addFocusListener(integerInputTest);


        //dear god action listeners too

        coefficiantPage.a_a_r_1.addActionListener(updateOthersa);
        coefficiantPage.a_a_g_1.addActionListener(updateOthersa);
        coefficiantPage.a_a_b_1.addActionListener(updateOthersa);
        coefficiantPage.a_s_r_1.addActionListener(updateOthersa);
        coefficiantPage.a_s_g_1.addActionListener(updateOthersa);
        coefficiantPage.a_s_b_1.addActionListener(updateOthersa);
        coefficiantPage.w_a_r_1.addActionListener(updateOthersa);
        coefficiantPage.w_a_g_1.addActionListener(updateOthersa);
        coefficiantPage.w_a_b_1.addActionListener(updateOthersa);
        coefficiantPage.w_s_r_1.addActionListener(updateOthersa);
        coefficiantPage.w_s_g_1.addActionListener(updateOthersa);
        coefficiantPage.w_s_b_1.addActionListener(updateOthersa);
        coefficiantPage.car1.addActionListener(updateOthersa);
        coefficiantPage.cag1.addActionListener(updateOthersa);
        coefficiantPage.cab1.addActionListener(updateOthersa);
        coefficiantPage.csr1.addActionListener(updateOthersa);
        coefficiantPage.csg1.addActionListener(updateOthersa);
        coefficiantPage.csb1.addActionListener(updateOthersa);
        distancePage.ar.addChangeListener(updateColoura);
        distancePage.ag.addChangeListener(updateColoura);
        distancePage.ab.addChangeListener(updateColoura);
        distancePage.wr.addChangeListener(updateColoura);
        distancePage.wg.addChangeListener(updateColoura);
        distancePage.wb.addChangeListener(updateColoura);
        distancePage.er.addChangeListener(updateColoura);
        distancePage.eg.addChangeListener(updateColoura);
        distancePage.eb.addChangeListener(updateColoura);
        distancePage.lr.addChangeListener(updateColoura);
        distancePage.lg.addChangeListener(updateColoura);
        distancePage.lb.addChangeListener(updateColoura);
        distancePage.lrr.addChangeListener(updateColoura);
        distancePage.lrg.addChangeListener(updateColoura);
        distancePage.lrb.addChangeListener(updateColoura);
        ///// all text entries for doubles
        distancePage.air_fog_1.addActionListener(doubleInputTesta);
        distancePage.air_fog_2.addActionListener(doubleInputTesta);
        distancePage.water_fog_1.addActionListener(doubleInputTesta);
        distancePage.water_fog_2.addActionListener(doubleInputTesta);
        distancePage.weather_fog_1.addActionListener(doubleInputTesta);
        distancePage.weather_fog_2.addActionListener(doubleInputTesta);
        distancePage.lava_fog_1.addActionListener(doubleInputTesta);
        distancePage.lava_fog_2.addActionListener(doubleInputTesta);
        distancePage.lava_resistance_fog_1.addActionListener(doubleInputTesta);
        distancePage.lava_resistance_fog_2.addActionListener(doubleInputTesta);
        volumetricPage.air_vfog_1.addActionListener(doubleInputTesta);
        volumetricPage.water_vfog_1.addActionListener(doubleInputTesta);
        volumetricPage.weather_vfog_1.addActionListener(doubleInputTesta);
        volumetricPage.lava_vfog_1.addActionListener(doubleInputTesta);
        volumetricPage.lava_resistance_vfog_1.addActionListener(doubleInputTesta);
        ////all text entried for int
        volumetricPage.air_vfog_3.addActionListener(integerInputTesta);
        volumetricPage.water_vfog_3.addActionListener(integerInputTesta);
        volumetricPage.weather_vfog_3.addActionListener(integerInputTesta);
        volumetricPage.lava_vfog_3.addActionListener(integerInputTesta);
        volumetricPage.lava_resistance_vfog_3.addActionListener(integerInputTesta);
        volumetricPage.air_vfog_4.addActionListener(integerInputTesta);
        volumetricPage.water_vfog_4.addActionListener(integerInputTesta);
        volumetricPage.weather_vfog_4.addActionListener(integerInputTesta);
        volumetricPage.lava_vfog_4.addActionListener(integerInputTesta);
        volumetricPage.lava_resistance_vfog_4.addActionListener(integerInputTesta);

    }


    private void loadNew(){

        FILE = new File(BiomeMain.activeDirectory+"\\fogs\\New_Fog_Edit_Output.json");
        indentifier ="new_namespace:new_test_fog";
        FILENAME = FILE.getName();

        wrapperF.fileNameField.setText(FILENAME);
        wrapperF.indentifierBox.setText(indentifier);
        distancePage.airFogCheckBox.setSelected(false);
        distancePage.waterFogCheckBox.setSelected(false);
        distancePage.weatherFogCheckBox.setSelected(false);
        distancePage.lavaFogCheckBox.setSelected(false);
        distancePage.lava_ResistanceCheckBox.setSelected(false);

        distancePage.air_fog.setVisible(false);
        distancePage.water_fog.setVisible(false);
        distancePage.weather_fog.setVisible(false);
        distancePage.lava_fog.setVisible(false);
        distancePage.lava_resistance_fog.setVisible(false);

        distancePage.air_fog_1.setText("0.0"); distancePage.as1.setValue(0);
        distancePage.water_fog_1.setText("0.0"); distancePage.ws1.setValue(0);
        distancePage.weather_fog_1.setText("0.0"); distancePage.es1.setValue(0);
        distancePage.lava_fog_1.setText("0.0"); distancePage.ls1.setValue(0);
        distancePage.lava_resistance_fog_1.setText("0.0"); distancePage.lrs1.setValue(0);

        distancePage.air_fog_2.setText("1.0"); distancePage.as2.setValue(100);
        distancePage.water_fog_2.setText("1.0"); distancePage.ws2.setValue(100);
        distancePage.weather_fog_2.setText("1.0"); distancePage.es2.setValue(100);
        distancePage.lava_fog_2.setText("1.0"); distancePage.ls2.setValue(100);
        distancePage.lava_resistance_fog_2.setText("1.0"); distancePage.lrs2.setValue(100);

        distancePage.air_fog_3_r.setValue(0); distancePage.ar.setValue(0);
        distancePage.air_fog_3_g.setValue(0); distancePage.ag.setValue(0);
        distancePage.air_fog_3_b.setValue(0); distancePage.ab.setValue(0);

        distancePage.water_fog_3_r.setValue(0); distancePage.wr.setValue(0);
        distancePage.water_fog_3_g.setValue(0); distancePage.wg.setValue(0);
        distancePage.water_fog_3_b.setValue(0); distancePage.wb.setValue(0);

        distancePage.weather_fog_3_r.setValue(0); distancePage.er.setValue(0);
        distancePage.weather_fog_3_g.setValue(0); distancePage.eg.setValue(0);
        distancePage.weather_fog_3_b.setValue(0); distancePage.eb.setValue(0);

        distancePage.lava_fog_3_r.setValue(0); distancePage.lr.setValue(0);
        distancePage.lava_fog_3_g.setValue(0); distancePage.lg.setValue(0);
        distancePage.lava_fog_3_b.setValue(0); distancePage.lb.setValue(0);

        distancePage.lava_resistance_fog_3_r.setValue(0); distancePage.lrr.setValue(0);
        distancePage.lava_resistance_fog_3_g.setValue(0); distancePage.lrg.setValue(0);
        distancePage.lava_resistance_fog_3_b.setValue(0); distancePage.lrb.setValue(0);

        distancePage.air_fog_4.setSelectedIndex(0);
        distancePage.water_fog_4.setSelectedIndex(0);
        distancePage.weather_fog_4.setSelectedIndex(0);
        distancePage.lava_fog_4.setSelectedIndex(0);
        distancePage.lava_resistance_fog_4.setSelectedIndex(0);

        volumetricPage.airFogCheckBox.setSelected(false);
        volumetricPage.air_vfog.setVisible(false);
        volumetricPage.air_vfog_1.setText("0.0");
        volumetricPage.a1s.setValue(0);
        volumetricPage.air_vfog_2.setSelected(true);
        volumetricPage.air_vfog_3.setText("64");
        volumetricPage.air_vfog_3.setEnabled(false);
        volumetricPage.air_vfog_4.setText("128");
        volumetricPage.air_vfog_4.setEnabled(false);

        volumetricPage.waterFogCheckBox.setSelected(false);
        volumetricPage.water_vfog.setVisible(false);
        volumetricPage.water_vfog_1.setText("0.0");
        volumetricPage.w1s.setValue(0);
        volumetricPage.water_vfog_2.setSelected(true);
        volumetricPage.water_vfog_3.setText("64");
        volumetricPage.water_vfog_3.setEnabled(false);
        volumetricPage.water_vfog_4.setText("128");
        volumetricPage.water_vfog_4.setEnabled(false);

        volumetricPage.weatherFogCheckBox.setSelected(false);
        volumetricPage.weather_vfog.setVisible(false);
        volumetricPage.weather_vfog_1.setText("0.0");
        volumetricPage.e1s.setValue(0);
        volumetricPage.weather_vfog_2.setSelected(true);
        volumetricPage.weather_vfog_3.setText("64");
        volumetricPage.weather_vfog_3.setEnabled(false);
        volumetricPage.weather_vfog_4.setText("128");
        volumetricPage.weather_vfog_4.setEnabled(false);

        volumetricPage.lavaFogCheckBox.setSelected(false);
        volumetricPage.lava_vfog.setVisible(false);
        volumetricPage.lava_vfog_1.setText("0.0");
        volumetricPage.l1s.setValue(0);
        volumetricPage.lava_vfog_2.setSelected(true);
        volumetricPage.lava_vfog_3.setText("64");
        volumetricPage.lava_vfog_3.setEnabled(false);
        volumetricPage.lava_vfog_4.setText("128");
        volumetricPage.lava_vfog_4.setEnabled(false);

        volumetricPage.lava_ResistanceCheckBox.setSelected(false);
        volumetricPage.lava_resistance_vfog.setVisible(false);
        volumetricPage.lava_resistance_vfog_1.setText("0.0");
        volumetricPage.lr1s.setValue(0);
        volumetricPage.lava_resistance_vfog_2.setSelected(true);
        volumetricPage.lava_resistance_vfog_3.setText("64");
        volumetricPage.lava_resistance_vfog_3.setEnabled(false);
        volumetricPage.lava_resistance_vfog_4.setText("128");
        volumetricPage.lava_resistance_vfog_4.setEnabled(false);


        coefficiantPage.airCheckBox.setSelected(false);
        coefficiantPage.air_c.setVisible(false);
        coefficiantPage.a_a_r_1.setText("0");
        coefficiantPage.a_a_r_2.setValue(0);
        coefficiantPage.a_a_g_1.setText("0");
        coefficiantPage.a_a_g_2.setValue(0);
        coefficiantPage.a_a_b_1.setText("0");
        coefficiantPage.a_a_b_2.setValue(0);
        coefficiantPage.a_s_r_1.setText("0");
        coefficiantPage.a_s_r_2.setValue(0);
        coefficiantPage.a_s_g_1.setText("0");
        coefficiantPage.a_s_g_2.setValue(0);
        coefficiantPage.a_s_b_1.setText("0");
        coefficiantPage.a_s_b_2.setValue(0);

        coefficiantPage.waterCheckBox.setSelected(false);
        coefficiantPage.water_c.setVisible(false);
        coefficiantPage.w_a_r_1.setText("0");
        coefficiantPage.w_a_r_2.setValue(0);
        coefficiantPage.w_a_g_1.setText("0");
        coefficiantPage.w_a_g_2.setValue(0);
        coefficiantPage.w_a_b_1.setText("0");
        coefficiantPage.w_a_b_2.setValue(0);
        coefficiantPage.w_s_r_1.setText("0");
        coefficiantPage.w_s_r_2.setValue(0);
        coefficiantPage.w_s_g_1.setText("0");
        coefficiantPage.w_s_g_2.setValue(0);
        coefficiantPage.w_s_b_1.setText("0");
        coefficiantPage.w_s_b_2.setValue(0);

        coefficiantPage.cloudCheckBox.setSelected(false);
        coefficiantPage.cloud_c.setVisible(false);
        coefficiantPage.car1.setText("0");
        coefficiantPage.car2.setValue(0);
        coefficiantPage.cag1.setText("0");
        coefficiantPage.cag2.setValue(0);
        coefficiantPage.cab1.setText("0");
        coefficiantPage.cab2.setValue(0);
        coefficiantPage.csr1.setText("0");
        coefficiantPage.csr2.setValue(0);
        coefficiantPage.csg1.setText("0");
        coefficiantPage.csg2.setValue(0);
        coefficiantPage.csb1.setText("0");
        coefficiantPage.csb2.setValue(0);


        changeDistanceType();
        setColours();
        render();
    }

    private SpinnerNumberModel spin255(){
        return new SpinnerNumberModel(0, 0, 255, 1);
    }
    private SpinnerNumberModel spin1(){
        return new SpinnerNumberModel(0.00, 0.00, 1.00, 0.01);
    }
    private SpinnerNumberModel spin10000(){
        return new SpinnerNumberModel(0.0000d, 0.0000d, 1.0000d, 0.0001d);
    }


    private void loadJson(java.io.File file){
        //empty all fields first
        loadNew();

        try {
            FILENAME = file.getName();
            FILE = file;
            wrapperF.fileNameField.setText(FILENAME);
            //error(file.getPath());

            //File toRead = new File(file.getAbsolutePath());

            Scanner input = new Scanner(file);
            //System.out.println("here");
            String line;
            boolean i = false;
            boolean d = false;
            boolean v = false;
            boolean c = false;
            while (input.hasNextLine()) {
                 line = input.nextLine();
                //read identifier
                System.out.println(line);

                if (line.contains("identifier") && !i) {
                    line = line.replace(" ", "");
                    line = line.replace("\"", "");
                    line = line.replace("identifier", "");
                    line = line.replaceFirst(":","");
                    indentifier = line;
                    wrapperF.indentifierBox.setText(line);
                    System.out.println("identifier set");
                    i=true;
                }
                if (line.contains("distance") && !d) {
                    loadDistance(input);
                    d=true;
                }
                if (line.contains("density") && !v) {
                    loadVolumetric(input);
                    v=true;
                }
                if (line.contains("media") && !c) {
                    loadCoefficients(input);
                    c=true;
                }
            }

            input.close();
            changeDistanceType();
            render();

        }catch (FileNotFoundException e){
            error("file not found");
        }
        //error("read complete");

        saveMessage.setVisible(false);
    }


    private void loadCoefficients(Scanner input){
        String line;
        boolean cont = true;
        while (input.hasNextLine() && cont) {
            line = input.nextLine();
            System.out.println(line);
            line = line.replace(" ", "");
            line = line.replace(":", "");
            line = line.replace("\"", "");
            if (line.matches("}")) break;
            line = line.replace(",", "");

            if (line.contains("air")) {
                //read air and continue true or false returns
                cont = readOneCoeff(input,
                        coefficiantPage.airCheckBox,
                        coefficiantPage.air_c,
                        coefficiantPage.a_s_r_1,
                        coefficiantPage.a_s_g_1,
                        coefficiantPage.a_s_b_1,
                        coefficiantPage.a_s_r_2,
                        coefficiantPage.a_s_g_2,
                        coefficiantPage.a_s_b_2,
                        coefficiantPage.a_a_r_1,
                        coefficiantPage.a_a_g_1,
                        coefficiantPage.a_a_b_1,
                        coefficiantPage.a_a_r_2,
                        coefficiantPage.a_a_g_2,
                        coefficiantPage.a_a_b_2
                );
            }
            if (line.contains("water")) {
                //read air and continue true or false returns
                cont = readOneCoeff(input,
                        coefficiantPage.waterCheckBox,
                        coefficiantPage.water_c,
                        coefficiantPage.w_s_r_1,
                        coefficiantPage.w_s_g_1,
                        coefficiantPage.w_s_b_1,
                        coefficiantPage.w_s_r_2,
                        coefficiantPage.w_s_g_2,
                        coefficiantPage.w_s_b_2,
                        coefficiantPage.w_a_r_1,
                        coefficiantPage.w_a_g_1,
                        coefficiantPage.w_a_b_1,
                        coefficiantPage.w_a_r_2,
                        coefficiantPage.w_a_g_2,
                        coefficiantPage.w_a_b_2
                );
            }
            if (line.contains("cloud")) {
                //read air and continue true or false returns
                cont = readOneCoeff(input,
                        coefficiantPage.cloudCheckBox,
                        coefficiantPage.cloud_c,
                        coefficiantPage.csr1,
                        coefficiantPage.csg1,
                        coefficiantPage.csb1,
                        coefficiantPage.csr2,
                        coefficiantPage.csg2,
                        coefficiantPage.csb2,
                        coefficiantPage.car1,
                        coefficiantPage.cag1,
                        coefficiantPage.cab1,
                        coefficiantPage.car2,
                        coefficiantPage.cag2,
                        coefficiantPage.cab2
                );
            }
        }
    }







    private boolean readOneCoeff(Scanner input,
                                         JCheckBox checkBox,
                                         JPanel revealPanel,
                                         JTextField sr,
                                 JTextField sg,
                                 JTextField sb,
                                        JSlider SR,
                                        JSlider SG,
                                        JSlider SB,
                                 JTextField ar,
                                 JTextField ag,
                                 JTextField ab,
                                        JSlider AR,
                                        JSlider AG,
                                        JSlider AB
    ){
        //coSliderConvert(coefficiantPage.a_a_b_1,coefficiantPage.a_a_b_2,true);

        checkBox.setSelected(true);
        revealPanel.setVisible(true);
        String line;
        while (input.hasNextLine()){
            line= input.nextLine();
            if (line.contains("}")){
                return (line.contains(","));
            }
            if (line.contains("scattering")){
                line = line.replace(" ", "");
                line = line.replace(":", "");
                line = line.replace("\"", "");
                line = line.replace("[", "");
                line = line.replace("]", "");
                line = line.replace("scattering", "");


                String[] values = line.split(",");
                System.out.println(values[0]);
                //set red
                sr.setText(values[0]);
                coSliderConvert(sr,SR,true);
                //set g
                sg.setText(values[1]);
                coSliderConvert(sg,SG,true);
                //set b
                sb.setText(values[2]);
                coSliderConvert(sb,SB,true);
            }
            if (line.contains("absorption")){
                line = line.replace(" ", "");
                line = line.replace(":", "");
                line = line.replace("\"", "");
                line = line.replace("[", "");
                line = line.replace("]", "");
                line = line.replace("absorption", "");

                String[] values = line.split(",");
                //set red
                ar.setText(values[0]);
                coSliderConvert(ar,AR,true);
                //set g
                ag.setText(values[1]);
                coSliderConvert(ag,AG,true);
                //set b
                ab.setText(values[2]);
                coSliderConvert(ab,AB,true);
            }

        }
        error("distance reader read false");
        return false;
    }





    private void loadVolumetric(Scanner input){
        String line;
        boolean cont = true;
        while (input.hasNextLine() && cont) {
            line = input.nextLine();
            System.out.println(line);
            line = line.replace(" ", "");
            line = line.replace(":", "");
            line = line.replace("\"", "");
            if (line.matches("}")) break;
            line = line.replace(",", "");
            if (line.contains("media")) {
                loadCoefficients(input);

            }
            if (line.contains("air")) {
                //read air and continue true or false returns
                cont = readOneVolume(input,
                        volumetricPage.airFogCheckBox,
                        volumetricPage.air_vfog,
                        volumetricPage.air_vfog_1,
                        volumetricPage.air_vfog_2,
                        volumetricPage.air_vfog_3,
                        volumetricPage.air_vfog_4,
                        volumetricPage.a1s);
            }
            if (line.contains("water")) {
                //read air and continue true or false returns
                cont = readOneVolume(input,
                        volumetricPage.waterFogCheckBox,
                        volumetricPage.water_vfog,
                        volumetricPage.water_vfog_1,
                        volumetricPage.water_vfog_2,
                        volumetricPage.water_vfog_3,
                        volumetricPage.water_vfog_4,
                        volumetricPage.w1s);
            }
            if (line.contains("weather")) {
                //read air and continue true or false returns
                cont = readOneVolume(input,
                        volumetricPage.weatherFogCheckBox,
                        volumetricPage.weather_vfog,
                        volumetricPage.weather_vfog_1,
                        volumetricPage.weather_vfog_2,
                        volumetricPage.weather_vfog_3,
                        volumetricPage.weather_vfog_4,
                        volumetricPage.e1s);
            }
            if (line.contains("lava") && !line.contains("resistance")) {
                //read air and continue true or false returns
                cont = readOneVolume(input,
                        volumetricPage.lavaFogCheckBox,
                        volumetricPage.lava_vfog,
                        volumetricPage.lava_vfog_1,
                        volumetricPage.lava_vfog_2,
                        volumetricPage.lava_vfog_3,
                        volumetricPage.lava_vfog_4,
                        volumetricPage.l1s);
            }
            if (line.contains("lava_resistance")) {
                //read air and continue true or false returns
                cont = readOneVolume(input,
                        volumetricPage.lava_ResistanceCheckBox,
                        volumetricPage.lava_resistance_vfog,
                        volumetricPage.lava_resistance_vfog_1,
                        volumetricPage.lava_resistance_vfog_2,
                        volumetricPage.lava_resistance_vfog_3,
                        volumetricPage.lava_resistance_vfog_4,
                        volumetricPage.lr1s);
            }
        }
    }

    private boolean readOneVolume(Scanner input,
                                           JCheckBox checkBox,
                                           JPanel revealPanel,
                                           JTextField max,
                                           JCheckBox uniform,
                                         JTextField maxH,
                                         JTextField zeroH,
                                  JSlider slider

    ){
        checkBox.setSelected(true);
        revealPanel.setVisible(true);
        String line;
        //set if not uniform
        uniform.setSelected(false);
        maxH.setEnabled(true);
        zeroH.setEnabled(true);

        while (input.hasNextLine()){
            line= input.nextLine();
            if (line.contains("}")){
                return (line.contains(","));
            }
            if (line.contains("max_density") && !line.contains("height")){
                line = clearRead(line,"max_density");
                max.setText(line);
                try {
                    slider.setValue((int) (Double.parseDouble(line) * 100));
                }catch (NumberFormatException e){
                    slider.setValue(50);
                }
            }
            if (line.contains("uniform")){
                line = clearRead(line,"uniform");
                if (line.contains("true")){
                    uniform.setSelected(true);
                    maxH.setEnabled(false);
                    zeroH.setEnabled(false);
                }


            }
            if (line.contains("max_density_height")){
                line = clearRead(line,"max_density_height");
                maxH.setText(line);
            }
            if (line.contains("zero_density_height")){
                line = clearRead(line,"zero_density_height");
                zeroH.setText(line);
            }
        }
        error("distance reader read false");
        return false;
    }


    private void loadDistance(Scanner input){
        String line;
        boolean cont = true;
        while (input.hasNextLine() && cont) {
            line = input.nextLine();
            System.out.println(line);
            line = line.replace(" ", "");
            line = line.replace(":", "");
            line = line.replace("\"", "");
            if (line.matches("}") || line.contains("volumetric")) break;
            line = line.replace(",", "");

            if (line.contains("air")) {
                //read air and continue true or false returns
                cont = readOneDistance(input,
                        distancePage.airFogCheckBox,
                        distancePage.air_fog,
                        distancePage.air_fog_1,
                        distancePage.air_fog_2,
                        distancePage.air_fog_3_r,
                        distancePage.air_fog_3_g,
                        distancePage.air_fog_3_b,
                        distancePage.air_fog_4);
            }
            if (line.contains("water")) {
                //read air and continue true or false returns
                cont = readOneDistance(input,
                        distancePage.waterFogCheckBox,
                        distancePage.water_fog,
                        distancePage.water_fog_1,
                        distancePage.water_fog_2,
                        distancePage.water_fog_3_r,
                        distancePage.water_fog_3_g,
                        distancePage.water_fog_3_b,
                        distancePage.water_fog_4);
            }
            if (line.contains("weather")) {
                //read air and continue true or false returns
                cont = readOneDistance(input,
                        distancePage.weatherFogCheckBox,
                        distancePage.weather_fog,
                        distancePage.weather_fog_1,
                        distancePage.weather_fog_2,
                        distancePage.weather_fog_3_r,
                        distancePage.weather_fog_3_g,
                        distancePage.weather_fog_3_b,
                        distancePage.weather_fog_4);
            }
            if (line.contains("lava") && !line.contains("resistance")) {
                //read air and continue true or false returns
                cont = readOneDistance(input,
                        distancePage.lavaFogCheckBox,
                        distancePage.lava_fog,
                        distancePage.lava_fog_1,
                        distancePage.lava_fog_2,
                        distancePage.lava_fog_3_r,
                        distancePage.lava_fog_3_g,
                        distancePage.lava_fog_3_b,
                        distancePage.lava_fog_4);
            }
            if (line.contains("lava_resistance")) {
                //read air and continue true or false returns
                cont = readOneDistance(input,
                        distancePage.lava_ResistanceCheckBox,
                        distancePage.lava_resistance_fog,
                        distancePage.lava_resistance_fog_1,
                        distancePage.lava_resistance_fog_2,
                        distancePage.lava_resistance_fog_3_r,
                        distancePage.lava_resistance_fog_3_g,
                        distancePage.lava_resistance_fog_3_b,
                        distancePage.lava_resistance_fog_4);
            }
        }
    }

    private boolean readOneDistance(Scanner input,
                                           JCheckBox checkBox,
                                           JPanel revealPanel,
                                           JTextField fogStart,
                                           JTextField fogEnd,
                                           JSlider fogRed,
                                           JSlider fogGreen,
                                           JSlider fogBlue,
                                           JComboBox renderType
                                           ){
        checkBox.setSelected(true);
        revealPanel.setVisible(true);
        String line;
        while (input.hasNextLine()){
            line= input.nextLine();
            if (line.contains("}")){
                return (line.contains(","));
            }
            if (line.contains("fog_start")){
                line = clearRead(line,"fog_start");
                fogStart.setText(line);
            }
            if (line.contains("fog_color")){
                line = clearRead(line,"fog_color");
                Color col = Color.decode(line);
                fogRed.setValue(col.getRed());
                fogGreen.setValue(col.getGreen());
                fogBlue.setValue(col.getBlue());
            }
            if (line.contains("fog_end")){
                line = clearRead(line,"fog_end");
                fogEnd.setText(line);
            }
            if (line.contains("render_distance_type")){
                line = clearRead(line,"render_distance_type");
                if (line.matches("fixed")){
                    renderType.setSelectedIndex(1);
                }else {
                    renderType.setSelectedIndex(0);
                }
            }
        }
        error("distance reader read false");
        return false;
    }

    private  String clearRead(String line, String removeText){
        line = line.replace(" ", "");
        line = line.replace(":", "");
        line = line.replace("\"", "");
        line = line.replace(removeText, "");
        line = line.replace(",", "");
        // dont  line = line.replace("#", "");
        return line;
    }
private void fileNameValidate(){
    FILENAME = FILENAME.replace(" ", "");
    FILENAME = FILENAME.replace(":", "");
    FILENAME = FILENAME.replace("\"", "");
    FILENAME = FILENAME.replace(",", "");
    if (FILENAME.matches(".json")) FILENAME = "Fog_Edit_Output.json";
    if (FILENAME.isEmpty()) FILENAME = "Fog_Edit_Output.json";
    if (!FILENAME.endsWith(".json")) FILENAME = FILENAME + ".json";
    if (FILENAME.matches("biomes_client.json")) FILENAME = "Fog_Edit_Output.json";

    wrapperF.fileNameField.setText(FILENAME);
    render();
}


    private void printJson(){
        saveMessage.setVisible(false);
        //error("start print");
        try {


            File    json = FILE;


            try {
                Path dir;
                if (BiomeMain.activeDirectory !=null) {

                dir =FileSystems.getDefault().getPath(BiomeMain.activeDirectory + "/fogs/");
                }else                {
                   dir = FileSystems.getDefault().getPath(System.getProperty("user.dir"));
                }

                Files.createDirectory(Files.createDirectory(dir));
            }catch (FileAlreadyExistsException e){
                //
            }

            if (json.exists() && json.isFile())
            {
                json.delete();
            }

            if (json.createNewFile()) {
                System.out.println("File created: " + json.getName());
                FileWriter output;


                output = new FileWriter(json);


                output.write("/*\n" +
                        " - Fog Definition Made by Biome & Fog Manager's Fog Editor\n" +
                        " - Coded by @Traben from the Minecraft RTX Discord\n" +
                        "  */\n" +
                        "{\n" +
                        "  \"format_version\": \"1.16.100\",\n" +
                        "  \"minecraft:fog_settings\": {\n" +
                        "    \"description\": {\n" +
                        "      \"identifier\": \"");
                output.write(indentifier);
                output.write("\"\n" +
                        "    },\n" +
                        "    \"distance\": {");

                boolean comma = false;

                if (distancePage.airFogCheckBox.isSelected()) {

                    output.write("\n" +
                            "      \"air\": {\n" +
                            "        \"fog_start\": " + distancePage.air_fog_1.getText() + ",\n" +
                            "        \"fog_end\": " + distancePage.air_fog_2.getText() + ",\n" +
                            "        \"fog_color\": \"" + String.format("#%02X%02X%02X", distancePage.air_fog_3_r.getValue(), distancePage.air_fog_3_g.getValue(), distancePage.air_fog_3_b.getValue()) + "\",\n" +
                            "        \"render_distance_type\": \"");
                    if (distancePage.air_fog_4.getSelectedIndex() == 0){
                        output.write("render");
                    }else{
                        output.write("fixed");
                    }
                            output.write("\"\n" +
                            "      }");
                    comma = true;
                }
                if (distancePage.waterFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "      \"water\": {\n" +
                            "        \"fog_start\": " + distancePage.water_fog_1.getText() + ",\n" +
                            "        \"fog_end\": " + distancePage.water_fog_2.getText() + ",\n" +
                            "        \"fog_color\": \"" + String.format("#%02X%02X%02X", distancePage.water_fog_3_r.getValue(), distancePage.water_fog_3_g.getValue(), distancePage.water_fog_3_b.getValue()) + "\",\n" +
                            "        \"render_distance_type\": \"");
                    if (distancePage.water_fog_4.getSelectedIndex() == 0){
                        output.write("render");
                    }else{
                        output.write("fixed");
                    }
                    output.write("\"\n" +
                            "      }");
                    comma = true;
                }
                if (distancePage.weatherFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "      \"weather\": {\n" +
                            "        \"fog_start\": " + distancePage.weather_fog_1.getText() + ",\n" +
                            "        \"fog_end\": " + distancePage.weather_fog_2.getText() + ",\n" +
                            "        \"fog_color\": \"" + String.format("#%02X%02X%02X", distancePage.weather_fog_3_r.getValue(), distancePage.weather_fog_3_g.getValue(), distancePage.weather_fog_3_b.getValue()) + "\",\n" +
                            "        \"render_distance_type\": \"");
                    if (distancePage.weather_fog_4.getSelectedIndex() == 0){
                        output.write("render");
                    }else{
                        output.write("fixed");
                    }
                    output.write("\"\n" +
                            "      }");
                    comma = true;
                }
                if (distancePage.lavaFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "      \"lava\": {\n" +
                            "        \"fog_start\": " + distancePage.lava_fog_1.getText() + ",\n" +
                            "        \"fog_end\": " + distancePage.lava_fog_2.getText() + ",\n" +
                            "        \"fog_color\": \"" + String.format("#%02X%02X%02X", distancePage.lava_fog_3_r.getValue(), distancePage.lava_fog_3_g.getValue(), distancePage.lava_fog_3_b.getValue()) + "\",\n" +
                            "        \"render_distance_type\": \"");
                    if (distancePage.lava_fog_4.getSelectedIndex() == 0){
                        output.write("render");
                    }else{
                        output.write("fixed");
                    }
                    output.write("\"\n" +
                            "      }");
                    comma = true;
                }
                if (distancePage.lava_ResistanceCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "      \"lava_resistance\": {\n" +
                            "        \"fog_start\": " + distancePage.lava_resistance_fog_1.getText() + ",\n" +
                            "        \"fog_end\": " + distancePage.lava_resistance_fog_2.getText() + ",\n" +
                            "        \"fog_color\": \"" + String.format("#%02X%02X%02X", distancePage.lava_resistance_fog_3_r.getValue(), distancePage.lava_resistance_fog_3_g.getValue(), distancePage.lava_resistance_fog_3_b.getValue()) + "\",\n" +
                            "        \"render_distance_type\": \"");
                    if (distancePage.lava_resistance_fog_4.getSelectedIndex() == 0){
                        output.write("render");
                    }else{
                        output.write("fixed");
                    }
                    output.write("\"\n" +
                            "      }");

                }
                comma = false;
//volumetric
                output.write("\n" +
                        "    },\n" +
                        "    \"volumetric\": {\n" +
                        "      \"density\": {");

                if (volumetricPage.airFogCheckBox.isSelected()) {
                    output.write("\n" +
                            "        \"air\": {\n" +
                            "          \"max_density\": " + volumetricPage.air_vfog_1.getText() + ",\n");
                    if (volumetricPage.air_vfog_2.isSelected()) {
                        output.write("          \"uniform\": true\n"); //uniformas are fucky still
                    } else {
                        output.write("          \"max_density_height\": " + volumetricPage.air_vfog_3.getText() + ",\n" +
                                "          \"zero_density_height\": " + volumetricPage.air_vfog_4.getText() + "\n");
                    }

                    output.write("        }");
                    comma = true;
                }
                if (volumetricPage.waterFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"water\": {\n" +
                            "          \"max_density\": " + volumetricPage.water_vfog_1.getText() + ",\n");
                    if (volumetricPage.water_vfog_2.isSelected()) {
                        output.write("          \"uniform\": true\n");
                    } else {
                        output.write("          \"max_density_height\": " + volumetricPage.water_vfog_3.getText() + ",\n" +
                                "          \"zero_density_height\": " + volumetricPage.water_vfog_4.getText() + "\n");
                    }

                    output.write("        }");
                    comma = true;
                }
                if (volumetricPage.weatherFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"weather\": {\n" +
                            "          \"max_density\": " + volumetricPage.weather_vfog_1.getText() + ",\n");
                    if (volumetricPage.weather_vfog_2.isSelected()) {
                        output.write("          \"uniform\": true\n");
                    } else {
                        output.write("          \"max_density_height\": " + volumetricPage.weather_vfog_3.getText() + ",\n" +
                                "          \"zero_density_height\": " + volumetricPage.weather_vfog_4.getText() + "\n");
                    }


                    output.write("        }");
                    comma = true;
                }
                if (volumetricPage.lavaFogCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"lava\": {\n" +
                            "          \"max_density\": " + volumetricPage.lava_vfog_1.getText() + ",\n");
                    if (volumetricPage.lava_vfog_2.isSelected()) {
                        output.write("          \"uniform\": true\n");
                    } else {
                        output.write("          \"max_density_height\": " + volumetricPage.lava_vfog_3.getText() + ",\n" +
                                "          \"zero_density_height\": " + volumetricPage.lava_vfog_4.getText() + "\n");
                    }


                    output.write("        }");
                    comma = true;
                }
                if (volumetricPage.lava_ResistanceCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"lava_resistance\": {\n" +
                            "          \"max_density\": " + volumetricPage.lava_resistance_vfog_1.getText() + ",\n");
                    if (volumetricPage.lava_resistance_vfog_2.isSelected()) {
                        output.write("          \"uniform\": true\n");
                    } else {
                        output.write("          \"max_density_height\": " + volumetricPage.lava_resistance_vfog_3.getText() + ",\n" +
                                "          \"zero_density_height\": " + volumetricPage.lava_resistance_vfog_4.getText() + "\n");
                    }


                    output.write("        }");

                }
                comma = false;
                output.write("\n" +
                        "      },\n" +
                        "      \"media_coefficients\": {");
                if (coefficiantPage.airCheckBox.isSelected()) {
                    output.write("\n" +
                            "        \"air\": {\n" +
                            "          \"scattering\": [ " + coefficiantPage.a_s_r_1.getText() + ", " + coefficiantPage.a_s_g_1.getText() + ", " + coefficiantPage.a_s_b_1.getText() + " ],\n" +
                            "          \"absorption\": [ " + coefficiantPage.a_a_r_1.getText() + ", " + coefficiantPage.a_a_g_1.getText() + ", " + coefficiantPage.a_a_b_1.getText() + " ]\n" +
                            "        }");
                    comma = true;
                }
                if (coefficiantPage.waterCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"water\": {\n" +
                            "          \"scattering\": [ " + coefficiantPage.w_s_r_1.getText() + ", " + coefficiantPage.w_s_g_1.getText() + ", " + coefficiantPage.w_s_b_1.getText() + " ],\n" +
                            "          \"absorption\": [ " + coefficiantPage.w_a_r_1.getText() + ", " + coefficiantPage.w_a_g_1.getText() + ", " + coefficiantPage.w_a_b_1.getText() + " ]\n" +
                            "        }");
                    comma = true;
                }
                if (coefficiantPage.cloudCheckBox.isSelected()) {
                    if (comma) output.write(",");
                    output.write("\n" +
                            "        \"cloud\": {\n" +
                            "          \"scattering\": [ " + coefficiantPage.csr1.getText() + ", " + coefficiantPage.csg1.getText() + ", " + coefficiantPage.csb1.getText() + " ],\n" +
                            "          \"absorption\": [ " + coefficiantPage.car1.getText() + ", " + coefficiantPage.cag1.getText() + ", " + coefficiantPage.cab1.getText() + " ]\n" +
                            "        }");

                }
                output.write("\n" +
                        "      }\n" +
                        "    }\n" +
                        "  }\n" +
                        "}");


                output.close();
                System.out.println("Successfully wrote to the file: " + FILENAME);


            } else {
                System.out.println("File already exists.");

            }

        } catch (IOException e) {
            System.out.println("An error occurred.");
            e.printStackTrace();
        }catch (InvalidPathException e) {
            System.out.println("the file name is invalid please change it and try again");
            e.printStackTrace();
        }

    }



    private void initRadios(){
        distancePage.airFogCheckBox.addItemListener(action);
        distancePage.waterFogCheckBox.addItemListener(action);
        distancePage.weatherFogCheckBox.addItemListener(action);
        distancePage.lavaFogCheckBox.addItemListener(action);
        distancePage.lava_ResistanceCheckBox.addItemListener(action);

        volumetricPage.airFogCheckBox.addItemListener(action);
        volumetricPage.waterFogCheckBox.addItemListener(action);
        volumetricPage.weatherFogCheckBox.addItemListener(action);
        volumetricPage.lavaFogCheckBox.addItemListener(action);
        volumetricPage.lava_ResistanceCheckBox.addItemListener(action);

        coefficiantPage.airCheckBox.addItemListener(action);
        coefficiantPage.waterCheckBox.addItemListener(action);
        coefficiantPage.cloudCheckBox.addItemListener(action);

    }


    private void setInvisisbles(){

        distancePage.air_fog.setVisible(false);
        distancePage.water_fog.setVisible(false);
        distancePage.weather_fog.setVisible(false);
        distancePage.lava_fog.setVisible(false);
        distancePage.lava_resistance_fog.setVisible(false);

        volumetricPage.air_vfog.setVisible(false);
        volumetricPage.water_vfog.setVisible(false);
        volumetricPage.weather_vfog.setVisible(false);
        volumetricPage.lava_vfog.setVisible(false);
        volumetricPage.lava_resistance_vfog.setVisible(false);

        coefficiantPage.air_c.setVisible(false);
        coefficiantPage.water_c.setVisible(false);
        coefficiantPage.cloud_c.setVisible(false);

    }


    /*private  void initMenu(){
        setButtons( distancePage.volumetricFogButton, 2);
        setButtons( distancePage.coefficientsButton, 3);

        setButtons( volumetricPage.distanceFogButton, 1);
        setButtons( volumetricPage.coefficientsButton, 3);

        setButtons( coefficiantPage.distanceFogButton, 1);
        setButtons( coefficiantPage.volumetricFogButton, 2);

        distancePage.saveExitButton.addActionListener(finish);
        volumetricPage.saveExitButton.addActionListener(finish);
        coefficiantPage.saveExitButton.addActionListener(finish);



    }*/

    private void initWrapper(){

        wrapperF.indentifierBox.addFocusListener(namespaceTest);
        wrapperF.indentifierBox.addActionListener(namespaceTesta);
        wrapperF.fileNameField.addFocusListener(new FocusListener() {
            @Override
            public void focusGained(FocusEvent e) {
            }
            @Override
            public void focusLost(FocusEvent e) {
                saveMessage.setVisible(true);
                FILENAME = wrapperF.fileNameField.getText();
                fileNameValidate();
            }
        });
        wrapperF.fileNameField.addActionListener(e -> {
            saveMessage.setVisible(true);
            FILENAME = wrapperF.fileNameField.getText();
            fileNameValidate();
        });
    }


    private void drawVisuals(){
        setColours();

    }

    private void setColours(){



        distancePage.afcp.setBackground(new Color(
                distancePage.air_fog_3_r.getValue()
                ,distancePage.air_fog_3_g.getValue()
                ,distancePage.air_fog_3_b.getValue()));

        distancePage.wfcp.setBackground(new Color(
            distancePage.water_fog_3_r.getValue()
                ,distancePage.water_fog_3_g.getValue()
                        ,distancePage.water_fog_3_b.getValue()));
        distancePage.efcp.setBackground(new Color(
                distancePage.weather_fog_3_r.getValue()
                ,distancePage.weather_fog_3_g.getValue()
                ,distancePage.weather_fog_3_b.getValue()));
        distancePage.lfcp.setBackground(new Color(
                distancePage.lava_fog_3_r.getValue()
                ,distancePage.lava_fog_3_g.getValue()
                ,distancePage.lava_fog_3_b.getValue()));
        distancePage.lrcp.setBackground(new Color(
                distancePage.lava_resistance_fog_3_r.getValue()
                ,distancePage.lava_resistance_fog_3_g.getValue()
                ,distancePage.lava_resistance_fog_3_b.getValue()));

        coefficiantPage.ascp.setBackground(new Color(
                get255Int( coefficiantPage.a_s_r_2)
                ,get255Int(coefficiantPage.a_s_g_2)
                ,get255Int(coefficiantPage.a_s_b_2)));
        coefficiantPage.aacp.setBackground(new Color(
                255 -get255Int(coefficiantPage.a_a_r_2)
                ,255 -get255Int(coefficiantPage.a_a_g_2)
                ,255 -get255Int(coefficiantPage.a_a_b_2)));

        coefficiantPage.wscp.setBackground(new Color(
                get255Int(coefficiantPage.w_s_r_2)
                ,get255Int(coefficiantPage.w_s_g_2)
                ,get255Int(coefficiantPage.w_s_b_2)));
        coefficiantPage.wacp.setBackground(new Color(
                255 -get255Int(coefficiantPage.w_a_r_2)
                ,255 -get255Int(coefficiantPage.w_a_g_2)
                ,255 -get255Int(coefficiantPage.w_a_b_2)));

        coefficiantPage.cscp.setBackground(new Color(
                get255Int(coefficiantPage.csr2)
                ,get255Int(coefficiantPage.csg2)
                ,get255Int(coefficiantPage.csb2)));
        coefficiantPage.cacp.setBackground(new Color(
                255 -get255Int(coefficiantPage.car2)
                ,255 -get255Int(coefficiantPage.cag2)
                ,255 -get255Int(coefficiantPage.cab2)));
    }

    public int get255Int(JSlider input){
        int num= input.getValue();
        num = num * 256;
        num = num / 10000;
        num =num*2;
        if (num >= 256) num = 255;
        return num;
    }

    public void render(){

        drawVisuals();


        frame.pack();

    }

}
