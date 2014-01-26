/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman;

import com.sun.java.swing.plaf.nimbus.NimbusLookAndFeel;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.sql.DataSource;
import javax.swing.JFrame;
import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import paperman.dialog.UserLoginUbahSistem;
import paperman.menu.MainMenu;
import paperman.model.SecurityConstants;
import paperman.service.MasterService;
import paperman.service.ReportService;
import paperman.service.SistemService;
import paperman.service.TransaksiService;

/**
 *
 * @author i1440ns
 */
public class Main {

    private static MainMenu mainMenu;
    private static ApplicationContext appContext;
    private static MasterService masterService;
    private static TransaksiService transaksiService;
    private static SistemService sistemService;
    private static ReportService reportService;
    private static DataSource dataSource;
    private static UserLoginUbahSistem userLogin;
    private static splash splashMain;

    public static MainMenu getMainMenu() {
        return mainMenu;
    }

    public static MasterService getMasterService() {
        return masterService;
    }

    public static TransaksiService getTransaksiService() {
        return transaksiService;
    }

    public static SistemService getSistemService() {
        return sistemService;
    }

    public static ReportService getReportService() {
        return reportService;
    }

    public static void initApplicationContext() {
        appContext = new ClassPathXmlApplicationContext("classpath:applicationContext.xml");
//        appContext = new ClassPathXmlApplicationContext("classpath:clientContext.xml");
        masterService = (MasterService) appContext.getBean("masterService");
        transaksiService = (TransaksiService) appContext.getBean("transaksiService");
        sistemService = (SistemService) appContext.getBean("sistemService");
        reportService = (ReportService) appContext.getBean("reportService");
//        dataSource = (DataSource) appContext.getBean("dataSource");
    }

    public static void executeMain() {
        java.awt.EventQueue.invokeLater(new Runnable() {

            public void run() {
                try {
                    UIManager.setLookAndFeel(new NimbusLookAndFeel());
                } catch (UnsupportedLookAndFeelException ex) {
                    Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
                }
                mainMenu = new MainMenu();
                mainMenu.setExtendedState(JFrame.MAXIMIZED_BOTH);
                mainMenu.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                mainMenu.IntialSystemState();
                mainMenu.setVisible(true);
                if (Main.getSistemService().sistemRecord() != null) {
                    userLogin = new UserLoginUbahSistem().showMainLoginDialog(SecurityConstants.LOGIN_SYSTEM);
                }
            }
        });
    }

    /**
     * @param args the command line arguments
     */
}
