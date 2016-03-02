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

import java.util.HashMap;
import org.junit.Test;
import static org.junit.Assert.*;
import org.junit.Ignore;

/**
 *
 * @author harvey
 */
public class SettingsTest {

    /**
     * Test of openFile method, of class Settings.
     */
    @Test
    @Ignore
    public void testOpenFile() {
        System.out.println("openFile");
        Settings.openFile("read");
    }

    /**
     * Test of addRecords method, of class Settings.
     */
    @Test
    public void testAddRecords() {
        System.out.println("addRecords");
        HashMap<String, String> settings = new HashMap<>();
        settings.put("interval", "4000");
        settings.put("humidity", "Harvey");
        settings.put("pressure", "Sama");
        settings.put("temperature", "Me");
        settings.put("wind", "Again");
        boolean expResult = true;
        boolean result = Settings.addRecords(settings);
        assertEquals(expResult, result);
    }

    /**
     * Test of readRecords method, of class Settings.
     */
    @Test
//    @Ignore
    public void testReadRecords() {
        System.out.println("readRecords");
//        HashMap<String, String> expResult = null;
        HashMap<String, String> result = Settings.readRecords();
//        assertEquals(expResult, result);
        System.out.println("interval " + result.get("interval"));
        System.out.println("humidity " + result.get("humidity"));
        System.out.println("pressure " + result.get("pressure"));
        System.out.println("temperature " + result.get("temperature"));
        System.out.println("wind " + result.get("wind"));
    }

    /**
     * Test of closeFile method, of class Settings.
     */
    @Test
    @Ignore
    public void testCloseFile() {
        System.out.println("closeFile");
        Settings.closeFile();
        // TODO review the generated test code and remove the default call to fail.
        fail("The test case is a prototype.");
    }

}
