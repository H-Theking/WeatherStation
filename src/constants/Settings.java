/*
 * Copyright (C) 2016 harvey
 *
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <http://www.gnu.org/licenses/>.
 */
package constants;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.Formatter;
import java.util.FormatterClosedException;
import java.util.HashMap;
import java.util.NoSuchElementException;
import java.util.Scanner;

/**
 *
 * @author harvey
 */
public class Settings {

    private static final String userHomeDir = System.getProperty("user.home", ".");
    private static String systemDir = userHomeDir + "/settings.txt";
    private static Formatter output;
    private static Scanner input;

    public static void openFile(String read) {

        try {
            if (System.getProperty("os.name").contains("Windows")) {
                systemDir = userHomeDir + "\\settings.txt";
            }
            if (read.equals("read")) {
                input = new Scanner(Paths.get(systemDir));
            } else {
                output = new Formatter(systemDir);
            }
        } catch (SecurityException securityException) {
            System.err.println("Write permission denied. Terminating.");
            System.exit(1); // terminate the program
        } catch (FileNotFoundException fileNotFoundException) {
            System.err.println("Error opening file. Terminating.");
            System.exit(1); // terminate the program
        } catch (IOException ex) {
            try { //create file if not exist
                output = new Formatter(systemDir);
                output.format("%s %s ", "interval", "5000");
                output.format("%s %s ", "humidity", "%");
                output.format("%s %s ", "pressure", "mB");
                output.format("%s %s ", "temperature", "°c");
                output.format("%s %s ", "wind", "m/s");
            } catch (FileNotFoundException ex1) {
                System.err.println("Error opening file. Terminating.");
                System.exit(1);
            }
        }
    }

    // add records to file
    /**
     * Settings. interval->value humidity->unit pressure->unit temperature->unit
     * windSpeed->unit
     *
     * @param settings
     * @return
     */
    public static boolean addRecords(HashMap<String, String> settings) {

        try {
            openFile("write");
            output.format("%s %s ", "interval", settings.get("interval"));
            output.format("%s %s ", "humidity", settings.get("humidity"));
            output.format("%s %s ", "pressure", settings.get("pressure"));
            output.format("%s %s ", "temperature", settings.get("temperature"));
            output.format("%s %s ", "wind", settings.get("wind"));
            closeFile();
        } catch (FormatterClosedException formatterClosedException) {
            System.err.println("Error writing to file. Terminating.");
            return false;
        }
        return true;
    } // end method a

    public static HashMap<String, String> readRecords() {
        HashMap<String, String> settings = new HashMap<>();
        try {
            openFile("read");
            while (input.hasNext()) // while there is more to read
            {
//                System.out.printf("%s %s\n", input.nextInt(), input.next());
                settings.put(input.next(), input.next());
            }
        } catch (NoSuchElementException elementException) {
            System.err.println("File improperly formed. Terminating.");
        } catch (IllegalStateException stateException) {
            System.err.println("Error reading from file. Terminating.");
        }
        return settings;

    } // end method readRecords

    public static void closeFile() {
        output.close();
    }
}
