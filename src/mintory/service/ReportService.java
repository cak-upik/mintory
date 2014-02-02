/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.service;

import java.util.Date;
import net.sf.jasperreports.engine.JasperPrint;
import mintory.model.isClosedFor;
import mintory.model.komposisiSetoran;

/**
 *
 * @author Nurul Chusna
 */
public interface ReportService {
    // ---- Armada BIRU ---- //
    public JasperPrint getDailyTransactionReport(Date dateSPO, komposisiSetoran kompos, String OS);
    public JasperPrint getTandaTerimaReport(Integer lambung, Integer setKe, String OS);
    public JasperPrint getMonthlyTransactionReport(Date dateSPO, String[] kompos, String OS);
    public JasperPrint getKartuPembayaranReport(Integer lambung, Date dateAwal, Date dateAkhir, isClosedFor optionalClosingStatus, Integer monthClosing, String SldAwalState, String OS);
    public JasperPrint getDataLambungReport(Date awalSPO, Date dateSPO, String[] kompos, String OS);

    // ---- Armada PUTIH ---- //
    public JasperPrint getDailyTransactionWhiteReport(Date dateSPO, String OS);
    public JasperPrint getTandaTerimaWhiteReport(Integer lambung, Integer setKe, String OS);
    public JasperPrint getMonthlyTransactionWhiteReport(Date dateSPO, String OS);
    public JasperPrint getKartuPembayaranWhiteReport(Integer lambung, Date dateAwal, Date dateAkhir, isClosedFor optionalClosingStatus, Integer monthClosing, String SldAwalState, String OS);
    public JasperPrint getDataLambungWhiteReport(Date dateAwal, Date dateSPO, String OS);

    // ---- MANAJEMEN PIUTANG ---- //
    public JasperPrint getDataLambungForPiutangReport(Integer lambung, Date dateAwal, Date dateSPO, String OS);
    public JasperPrint getDataLambungForPiutangWhiteReport(Integer lambung, Date dateAwal, Date dateSPO, String OS);
}
