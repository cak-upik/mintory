/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.service;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import paperman.model.JalanStatus;
import paperman.model.TransaksiStatus;
import paperman.model.aksiClosing;
import paperman.model.closingBulanan;
import paperman.model.closingBulananPutih;
import paperman.model.closingTahunan;
import paperman.model.closingTahunanPutih;
import paperman.model.isClosedFor;
import paperman.model.isClosedStatus;
import paperman.model.komposisiSetoran;
import paperman.model.setoran;
import paperman.model.setoranDetail;
import paperman.model.setoranDetailPutih;
import paperman.model.setoranPutih;

/**
 *
 * @author i1440ns
 */
public interface TransaksiService {
    public void save(setoran sto);
    public void save(setoranPutih stPutih);
    public void delete(setoran sto);
    public void delete(setoranPutih stPutih);
    public setoran findByCode(Integer id);
    public Integer getTotalSetoranCount(komposisiSetoran ks);
    public Integer getTotalSetoranPutihCount();
    public setoran findByKodeSetoran(String kode);
    public List<setoranDetail> findByNoLambung(Integer lambung, Date thisMonth);
    public List<setoran> getUpdatedSetoranInRange(Integer lambung, Date spoAwal, Date spoAkhir);
    public List<setoranPutih> getUpdatedSetoranPutihInRange(Integer lambung, Date spoAwal, Date spoAkhir);
    public List<setoranDetail> findSetoranNoLambungReturned(Date thisMonth, komposisiSetoran tagArmada);
    public List<setoranDetailPutih> findSetoranPutihNoLambungReturned(Date thisMonth);
    public List<setoranDetail> findSetoranDetailByLambung(Integer lambung, Date tglSetoran);
    public List<setoranDetailPutih> findSetoranDetailPutihByLambung(Integer lambung, Date tglSetoran);
    public List<setoranDetail> findLastSetoranDetailByLambung(Integer lambung);
    public List<setoranDetailPutih> findLastSetoranDetailPutihByLambung(Integer lambung);
    public setoranPutih findByCodePutih(Integer id);
    public List<setoran> getLatestSetoranCount();
    public List<setoranDetail> getLatestSetoranCount(Integer lambung);
    public List<setoranDetailPutih> getLatestSetoranPutihCount(Integer lambung);
    public List<setoran> findLastTglJatuhTempo(JalanStatus status);
    public List<setoranPutih> findLastTglJatuhTempoPutih(JalanStatus status);
    public List<setoranPutih> getLatestSetoranPutihCount();
    public List<setoran> setoranRecord();
    public List<setoran> setoranRecordInCustomRange(Date periodTime);
    public List<setoran> setoranRecordInCustomRange(Date startPeriodTime, Date endPeriodTime);
    public List<setoran> setoranRecordInCustomRange(Date whenMonth, komposisiSetoran whatArmada);
    public List<setoranPutih> setoranPutihRecord();
    public List<setoranPutih> setoranPutihRecordInCustomRange(Date periodTime);
    public List<setoranPutih> setoranPutihRecordInCustomRange(Date periodTime, Date endPeriodTime);
    public List<setoran> isClosedTransaction(isClosedStatus closedStatus);
    public setoran findBySpecific(Integer lambung, Date tglSetoran, Integer setoranKe);
    public setoranPutih findBySpecificPutih(Integer lambung, Date tglSetoran, Integer setoranKe);
    public setoran findHutangByNoLambung(Integer lambung, TransaksiStatus status);
    public setoranPutih findHutangPutihByNoLambung(Integer lambung, TransaksiStatus status);
    public setoranPutih findByNoLambungPutih(Integer lambung);
    public List<setoran> findAnyLeftClosingBulananValid(Date thisMonth, isClosedStatus closedStatus);
    public List<setoranPutih> findAnyLeftClosingBulananPutihValid(Date thisMonth, isClosedStatus closedStatus);
    public List<setoran> findAvailableSetoran(isClosedStatus closedStatus);
    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada);
    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada);
    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada, Integer offset, Integer limit);
    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada, Integer offset, Integer limit);
    public List<setoranDetailPutih> findAvailableSetoranPutih(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun);
    public List<setoran> findAllAvailableSetoranByLambung(Integer lambung, Date tglSpo, isClosedStatus status);
    public List<setoranPutih> findAvailableSetoranPutih(isClosedStatus closedStatus);
    public List<setoranDetailPutih> findAvailableSetoranPutih(isClosedStatus closedStatus, BigDecimal angs, BigDecimal tab, Integer offset, Integer limit);
    public List<setoranPutih> findAllAvailableSetoranPutihByLambung(Integer lambung, Date tglSpo, isClosedStatus status);
    public BigDecimal sumHutang(Integer lambung, TransaksiStatus status);
    public BigDecimal sumHutangPutih(Integer lambung, TransaksiStatus status);
    public BigDecimal sumAngsuran(Date periodTime);
    public BigDecimal sumAngsuran(Integer lambung, Date periodTime, Date periodYear);
    public BigDecimal sumAngsuran(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumTabungan(Date periodTime);
    public BigDecimal sumTabungan(Integer lambung, Date periodTime, Date periodYear);
    public BigDecimal sumTabungan(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumSetoran(Date periodTime);
    public BigDecimal sumSetoran(Integer lambung, Date periodTime, Date periodYear);
    public BigDecimal sumSetoran(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumKasbon(Date periodTime);
    public BigDecimal sumKasbon(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumKasbon(Integer lambung, Date startDate, Date endDate);
    public BigDecimal sumBayarKasbon(Date periodTime);
    public BigDecimal sumBayarKasbon(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumBayarKasbon(Integer lambung, Date startDate, Date endDate);
    public BigDecimal sumOvertime(Date periodTime);
    public BigDecimal sumOvertime(Integer lambung, Date periodTime, Date periodYear);
    public BigDecimal sumOvertime(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumCicilan(Date periodTime);
    public BigDecimal sumCicilan(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumCicilan(Integer lambung, Date startDate, Date endDate);
    public BigDecimal sumAngsuranPutih(Date periodTime);
    public BigDecimal sumAngsuranPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumAngsuranPutih(Integer lambung, Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumTabunganPutih(Date periodTime);
    public BigDecimal sumTabunganPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumTabunganPutih(Integer lambung, Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumSetoranPutih(Date periodTime);
    public BigDecimal sumSetoranPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumSetoranPutih(Integer lambung, Date periodTime, Date periodYear);
    public BigDecimal sumKasbonPutih(Date periodTime);
    public BigDecimal sumKasbonPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumKasbonPutih(Integer lambung, Date startDate, Date endDate);
    public BigDecimal sumBayarKasbonPutih(Date periodTime);
    public BigDecimal sumBayarKasbonPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumBayarKasbonPutih(Integer lambung, Date startDate, Date endDate);
    public BigDecimal sumOvertimePutih(Date periodTime);
    public BigDecimal sumOvertimePutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumOvertimePutih(Integer lambung, Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumCicilanPutih(Date periodTime);
    public BigDecimal sumCicilanPutih(Date startPeriodTime, Date endPeriodTime);
    public BigDecimal sumCicilanPutih(Integer lambung, Date startDate, Date endDate);


    public void save(setoranDetail stoDetail);
    public void save(setoranDetailPutih stoDetailPutih);
    public void delete(setoranDetail stoDetail);
    public void delete(setoranDetailPutih stDetailPutih);
    public List<setoranDetail> setoranDetailRecord();
    public List<setoran> hutangRecord(TransaksiStatus status);
    public List<setoranPutih> hutangRecordPutih(TransaksiStatus status);
    public List<setoranDetailPutih> setoranDetailPutihRecord();
    public List<setoran> findAllStatusSetoran(TransaksiStatus status);
    public List<setoranPutih> findAllStatusSetoranPutih(TransaksiStatus status);
    public List<setoran> findStatusSetoranByLambung(Integer lambung, TransaksiStatus status);
    public List<setoranPutih> findStatusSetoranPutihByLambung(Integer lambung, TransaksiStatus status);

    public void save(closingBulanan clb);
    public void delete(closingBulanan clb);
    public List<closingBulanan> closingBulananRecord();
    public List<closingBulanan> closingBulananFirstRecord();
    public List<closingBulanan> closingBulananFirstRecord(Integer lambung);
    public List<closingBulanan> closingBulananInCustomRange(Date thisMonth);
    public List<closingBulanan> closingBulananRecordForSaldoAwal();
    public List<closingBulanan> closingBulananRecordForSaldoAwalAll(Date thisMonth, komposisiSetoran ks);
    public List<closingBulanan> closingBulananRecordForSaldoAwal(aksiClosing act);
    public List<closingBulanan> closingBulananRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month);
    public closingBulanan findClosingByRefNoLambung(Integer refLambung);
    public closingBulanan findClosingByRefNoLambung(Integer refLambung, Date whenMonth);
    public closingBulanan findNewClosingByRefNoLambung(Integer refLambung, Date whenMonth);
    public closingBulanan findAnyClosingBulananValid(Date thisMonth, isClosedFor closedFor);
    public closingBulanan findClosingSaldoAwalByRefNoLambung(Integer refLambung, Date whenMonth);

    public void save(closingBulananPutih clbPutih);
    public void delete(closingBulananPutih clbPutih);
    public List<closingBulananPutih> closingBulananPutihRecord();
    public List<closingBulananPutih> closingBulananPutihFirstRecord();
    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal();
    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(Date whenMonth);
    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month);
    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwalAll(komposisiSetoran idKomposisi, Date month);
    public List<closingBulananPutih> closingBulananPutihInCustomRange(Date thisMonth);
    public closingBulananPutih findClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth);
    public closingBulananPutih findClosingSaldoAwalPutihByRefNoLambung(Integer refLambung, Date whenMonth);
    public closingBulananPutih findNewClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth);

    public void save(closingTahunan clt);
    public void delete(closingTahunan clt);
    public List<closingTahunan> closingTahunanRecord();

    public void save(closingTahunanPutih cltPutih);
    public void delete(closingTahunanPutih cltPutih);
    public List<closingTahunanPutih> closingTahunanPutihRecord();
}
