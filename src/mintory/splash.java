package mintory;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.LineBorder;
import mintory.dialog.UserLoginUbahSistem;
import mintory.menu.MainMenu;

/**
 *
 * @author Admin
 */
public class splash extends JWindow implements Runnable {

    private MainMenu mainMenu;
    private UserLoginUbahSistem userLogin;

    public void run() {
        JPanel pnl = new JPanel();
        ImageIcon srcImages = new ImageIcon(getClass().getResource("/paperman/images/splash.png"));
        final JProgressBar pb_1 = new JProgressBar();
        final JLabel lbl_a = new JLabel("");
//        final JLabel lbl_b = new JLabel("");
        final JLabel image = new JLabel(srcImages);
        lbl_a.setForeground(Color.RED);
        lbl_a.setFont(new Font("Verdana", Font.PLAIN, 11));
//        lbl_b.setForeground(Color.BLACK);
//        lbl_b.setFont(new Font("Verdana", Font.PLAIN, 10));
        Dimension screen = Toolkit.getDefaultToolkit().getScreenSize();
        pnl.setBorder(null);
        pnl.setLayout(null);
        pb_1.setBorder(new LineBorder(Color.black, 1));
        pnl.add(lbl_a);
//        pnl.add(lbl_b);
        pnl.add(pb_1);
        pnl.add(image);
        pnl.setBackground(null);
        pnl.setOpaque(false);
        pnl.setBounds(new Rectangle(image.getBounds()));
        lbl_a.setBounds(10, 210, 344, 20);
//        lbl_b.setBounds(10, 232, 344, 20);
        image.setBounds(0, 0, srcImages.getIconWidth(), srcImages.getIconHeight());
        pb_1.setBounds(10, 230, 380, 7);
        getContentPane().add(pnl);
        pb_1.setValue(0);
        pb_1.setMinimum(0);
        pb_1.setMaximum(100);
        int delay = 100; // milliseconds
        ActionListener progressUpdater = new ActionListener() {

            public void actionPerformed(ActionEvent evt) {
                int val = pb_1.getValue();
                val += 2;
                pb_1.setValue(val);
                pb_1.setStringPainted(false);
                pb_1.setBackground(Color.black);
                for (int i = 0; i <= val; i++) {
                    lbl_a.setText("Starting...");
//                    lbl_b.setText("Initializing Main Program");
                }
                if (val == 100) {
                    dispose();
                    JDialog.setDefaultLookAndFeelDecorated(true);
                    Main.executeMain();
                }
            }
        };
        new Timer(delay, progressUpdater).start();
        Dimension sd = Toolkit.getDefaultToolkit().getScreenSize();
        setSize(image.getSize());
        setAlwaysOnTop(true);
        setLocation((screen.width - getSize().width) / 2, (screen.height - getSize().height) / 2);
        dispose();
        show();
    }

    public static void main(String args[]) {
        new Main();
        Main.initApplicationContext();
        splash er = new splash();
        er.run();
    }
}
