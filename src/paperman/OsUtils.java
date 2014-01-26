/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman;

/**
 *
 * @author cak-upik
 */
public class OsUtils {

    private static String OS = null;

    public static String getOsName() {
        if (OS == null) {
            OS = System.getProperty("os.name");
        }
        return OS;
    }

    public static boolean isWindows() {
        return getOsName().startsWith("Windows");
    }

    public static boolean isUnix() {
        return getOsName().startsWith("Linux");
    }
}
