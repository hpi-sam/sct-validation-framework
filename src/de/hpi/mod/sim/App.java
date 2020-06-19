package de.hpi.mod.sim;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import javax.swing.JOptionPane;

import de.hpi.mod.sim.core.model.Setting;
import de.hpi.mod.sim.core.view.DriveSimFrame;
import de.hpi.mod.sim.setting.infinitewarehouses.InfiniteStationsSetting;

/**
 * 
 * @author Tim Cech, Jonas Kordt, Paul Methfessel, Simon Wietheger
 *         (Hasso-Plattner-Institute chair for software analysis and modeling)
 * @version 1.0.0
 *
 *          This classes starts the execution of the program.
 */
public class App {

    private static final String[] POSSIBLE_SETTINGS = {
            "de.hpi.mod.sim.setting.infinitewarehouses.InfiniteStationsSetting",
     };

     private static Setting selectSetting() {
        String chosenName;
        if (POSSIBLE_SETTINGS.length == 1)
            chosenName = POSSIBLE_SETTINGS[0];
        else
            chosenName = (String) JOptionPane.showInputDialog(null, "Choose a setting ...", "Setting selection",
                    JOptionPane.QUESTION_MESSAGE, null, POSSIBLE_SETTINGS, POSSIBLE_SETTINGS[0]);
        try {
            @SuppressWarnings("unchecked")
            Constructor<? extends Setting> con = (Constructor<? extends Setting>) Class.forName(chosenName).getConstructors()[0];
            return con.newInstance();
        } catch (Exception e) {
            System.err.println("Found no such subclass of setting: " + chosenName);
            e.printStackTrace();
            return null;
        }
    }

    public static void main(String[] args) {
        DriveSimFrame.setSystemLookAndFeel();
        Setting setting = selectSetting();
        new DriveSimFrame(setting);
    }
}
