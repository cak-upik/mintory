/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman.service.impl;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import paperman.dao.TransaksiSetoranDao;
import paperman.dao.TransaksiSetoranDetailDao;
import paperman.dao.TransaksiSetoranDetailPutihDao;
import paperman.dao.TransaksiSetoranPutihDao;
import paperman.dao.closingBulananDao;
import paperman.dao.closingBulananPutihDao;
import paperman.dao.closingTahunanDao;
import paperman.dao.closingTahunanPutihDao;
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
import paperman.service.TransaksiService;

/**
 *
 * @author i1440ns
 */
@Service("transaksiService")
@Transactional
public class TransaksiServiceImpl implements TransaksiService {

    @Autowired
    private TransaksiSetoranDao stoDao;
    @Autowired
    private TransaksiSetoranDetailDao stDetDao;
    @Autowired
    private TransaksiSetoranPutihDao stPutihDao;
    @Autowired
    private TransaksiSetoranDetailPutihDao stDetailPutihDao;
    @Autowired
    private closingBulananDao clbDao;
    @Autowired
    private closingTahunanDao cltDao;
    @Autowired
    private closingBulananPutihDao clbPutihDao;
    @Autowired
    private closingTahunanPutihDao cltPutihDao;

    @Autowired
    public TransaksiServiceImpl(TransaksiSetoranDao stoDao, TransaksiSetoranPutihDao stPutihDao, TransaksiSetoranDetailDao stDetDao, TransaksiSetoranDetailPutihDao stDetailPutihDao, closingBulananDao clbDao, closingTahunanDao cltDao) {
        this.stoDao = stoDao;
        this.stDetDao = stDetDao;
        this.stPutihDao = stPutihDao;
        this.stDetailPutihDao = stDetailPutihDao;
        this.clbDao = clbDao;
        this.cltDao = cltDao;
    }

    public void save(setoran sto) {
        stoDao.save(sto);
    }

    public void delete(setoran sto) {
        stoDao.delete(sto);
    }

    public setoran findByCode(Integer id) {
        return stoDao.findByCode(id);
    }

    public setoran findByKodeSetoran(String kode) {
        return stoDao.findByKodeSetoran(kode);
    }

    public List<setoran> getUpdatedSetoranInRange(Integer lambung, Date spoAwal, Date spoAkhir) {
        return stoDao.getUpdatedSetoranInRange(lambung, spoAwal, spoAkhir);
    }

    public List<setoranPutih> getUpdatedSetoranPutihInRange(Integer lambung, Date spoAwal, Date spoAkhir) {
        return stPutihDao.getUpdatedSetoranInRange(lambung, spoAwal, spoAkhir);
    }

    public Integer getTotalSetoranCount(komposisiSetoran ks) {
        return stoDao.getTotalSetoranCount(ks);
    }

    public Integer getTotalSetoranPutihCount() {
        return stPutihDao.getTotalSetoranCount();
    }

    public List<setoranDetail> findSetoranNoLambungReturned(Date thisMonth, komposisiSetoran tagArmada) {
        return stoDao.findSetoranNoLambungReturned(thisMonth, tagArmada);
    }

    public List<setoranDetailPutih> findSetoranPutihNoLambungReturned(Date thisMonth) {
        return stPutihDao.findSetoranNoLambungReturned(thisMonth);
    }

    public List<setoran> setoranRecord() {
        return stoDao.setoranRecord();
    }

    public List<setoran> setoranRecordInCustomRange(Date periodTime) {
        return stoDao.setoranRecordInCustomRange(periodTime);
    }

    public List<setoran> setoranRecordInCustomRange(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.setoranRecordInCustomRange(startPeriodTime, endPeriodTime);
    }

    public List<setoran> setoranRecordInCustomRange(Date whenMonth, komposisiSetoran whatArmada) {
        return stoDao.setoranRecordInCustomRange(whenMonth, whatArmada);
    }

    public List<setoran> getLatestSetoranCount() {
        return stoDao.getLatestSetoranCount();
    }

    public List<setoranDetail> getLatestSetoranCount(Integer lambung) {
        return stDetDao.getLatestSetoranCount(lambung);
    }

    public List<setoranDetailPutih> getLatestSetoranPutihCount(Integer lambung) {
        return stDetailPutihDao.getLatestSetoranCount(lambung);
    }

    public void save(setoranDetail stoDetail) {
        stDetDao.save(stoDetail);
    }

    public void delete(setoranDetail stoDetail) {
        stDetDao.delete(stoDetail);
    }

    public List<setoranDetail> setoranDetailRecord() {
        return stDetDao.setoranDetailRecord();
    }

    public setoran findBySpecific(Integer lambung, Date tglSetoran, Integer setoranKe) {
        return stoDao.findBySpecific(lambung, tglSetoran, setoranKe);
    }

    public setoran findHutangByNoLambung(Integer lambung, TransaksiStatus status) {
        return stoDao.findByHutangNoLambung(lambung, status);
    }

    public setoranPutih findHutangPutihByNoLambung(Integer lambung, TransaksiStatus status) {
        return stPutihDao.findByHutangNoLambung(lambung, status);
    }

    public BigDecimal sumHutang(Integer lambung, TransaksiStatus status) {
        return stoDao.sumHutang(lambung, status);
    }

    public BigDecimal sumSetoran(Date periodTime) {
        return stoDao.sumSetoran(periodTime);
    }

    public List<setoran> findAnyLeftClosingBulananValid(Date thisMonth, isClosedStatus closedStatus) {
        return stoDao.findAnyLeftClosingBulananValid(thisMonth, closedStatus);
    }

    public List<setoranPutih> findAnyLeftClosingBulananPutihValid(Date thisMonth, isClosedStatus closedStatus) {
        return stPutihDao.findAnyLeftClosingBulananPutihValid(thisMonth, closedStatus);
    }

    public List<setoran> findAllStatusSetoran(TransaksiStatus status) {
        return stoDao.findAllStatusSetoran(status);
    }

    public List<setoran> findStatusSetoranByLambung(Integer lambung, TransaksiStatus status) {
        return stoDao.findStatusSetoranByLambung(lambung, status);
    }

    public List<setoran> findAvailableSetoran(isClosedStatus closedStatus) {
        return stoDao.findAvailableSetoran(closedStatus);
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada) {
        return stoDao.findAvailableSetoran(closedStatus, periodeBulan, periodeTahun, tagArmada);
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada) {
        return stoDao.findAvailableSetoran(closedStatus, valAngsuran, valTabungan, tagArmada);
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada, Integer offset, Integer limit) {
        return stoDao.findAvailableSetoran(closedStatus, periodeBulan, periodeTahun, tagArmada, offset, limit);
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada, Integer offset, Integer limit) {
        return stoDao.findAvailableSetoran(closedStatus, valAngsuran, valTabungan, tagArmada, offset, limit);
    }

    public List<setoranPutih> findAvailableSetoranPutih(isClosedStatus closedStatus) {
        return stPutihDao.findAvailableSetoranPutih(closedStatus);
    }

    public List<setoranDetailPutih> findAvailableSetoranPutih(isClosedStatus closedStatus, BigDecimal angs, BigDecimal tab, Integer offset, Integer limit) {
        return stPutihDao.findAvailableSetoranPutih(closedStatus, angs, tab, offset, limit);
    }

    public List<setoranDetailPutih> findAvailableSetoranPutih(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun) {
        return stPutihDao.findAvailableSetoranPutih(closedStatus, periodeBulan, periodeTahun);
    }

    public List<setoran> findAllAvailableSetoranByLambung(Integer lambung, Date tglSpo, isClosedStatus closedStatus) {
        return stoDao.findAllAvailableSetoranByLambung(lambung, tglSpo, closedStatus);
    }

    public List<setoranPutih> findAllAvailableSetoranPutihByLambung(Integer lambung, Date tglSpo, isClosedStatus closedStatus) {
        return stPutihDao.findAllAvailableSetoranPutihByLambung(lambung, tglSpo, closedStatus);
    }

    public void save(setoranPutih stPutih) {
        stPutihDao.save(stPutih);
    }

    public void delete(setoranPutih stPutih) {
        stPutihDao.delete(stPutih);
    }

    public setoranPutih findByCodePutih(Integer id) {
        return stPutihDao.findByCode(id);
    }

    public List<setoranPutih> getLatestSetoranPutihCount() {
        return stPutihDao.getLatestSetoranCount();
    }

    public List<setoranPutih> setoranPutihRecord() {
        return stPutihDao.setoranPutihRecord();
    }

    public List<setoranDetail> findByNoLambung(Integer lambung, Date whenMonth) {
        return stoDao.findByNoLambung(lambung, whenMonth);
    }

    public List<setoranDetail> findSetoranDetailByLambung(Integer lambung, Date tglSetoran) {
        return stDetDao.findSetoranDetailByLambung(lambung, tglSetoran);
    }

    public List<setoranDetailPutih> findSetoranDetailPutihByLambung(Integer lambung, Date tglSetoran) {
        return stDetailPutihDao.findSetoranDetailByLambung(lambung, tglSetoran);
    }

    public List<setoranDetail> findLastSetoranDetailByLambung(Integer lambung) {
        return stDetDao.findLastSetoranDetailByLambung(lambung);
    }

    public List<setoranDetailPutih> findLastSetoranDetailPutihByLambung(Integer lambung) {
        return stDetailPutihDao.findLastSetoranDetailByLambung(lambung);
    }

    public setoranPutih findBySpecificPutih(Integer lambung, Date tglSetoran, Integer setoranKe) {
        return stPutihDao.findBySpecific(lambung, tglSetoran, setoranKe);
    }

    public setoranPutih findByNoLambungPutih(Integer lambung) {
        return stPutihDao.findByNoLambung(lambung);
    }

    public BigDecimal sumHutangPutih(Integer lambung, TransaksiStatus status) {
        return stPutihDao.sumHutang(lambung, status);
    }

    public BigDecimal sumAngsuran(Date periodTime) {
        return stoDao.sumAngsuran(periodTime);
    }

    public BigDecimal sumAngsuran(Integer lambung, Date periodTime, Date periodYear) {
        return stoDao.sumAngsuran(lambung, periodTime, periodYear);
    }

    public BigDecimal sumTabungan(Integer lambung, Date periodTime, Date periodYear) {
        return stoDao.sumTabungan(lambung, periodTime, periodYear);
    }

    public BigDecimal sumTabungan(Date periodTime) {
        return stoDao.sumTabungan(periodTime);
    }

    public BigDecimal sumKasbon(Date periodTime) {
        return stoDao.sumKasbon(periodTime);
    }

    public BigDecimal sumBayarKasbon(Date periodTime) {
        return stoDao.sumBayarKasbon(periodTime);
    }

    public BigDecimal sumOvertime(Integer lambung, Date periodTime, Date periodYear) {
        return stoDao.sumOvertime(lambung, periodTime, periodYear);
    }

    public BigDecimal sumOvertime(Date periodTime) {
        return stoDao.sumOvertime(periodTime);
    }

    public BigDecimal sumCicilan(Date periodTime) {
        return stoDao.sumCicilan(periodTime);
    }

    public BigDecimal sumAngsuran(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumAngsuran(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumTabungan(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumTabungan(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumSetoran(Integer lambung, Date periodTime, Date periodYear) {
        return stoDao.sumSetoran(lambung, periodTime, periodYear);
    }

    public BigDecimal sumSetoran(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumSetoran(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumKasbon(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumKasbon(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumKasbon(Integer lambung, Date startDate, Date endDate) {
        return stoDao.sumKasbon(lambung, startDate, endDate);
    }

    public BigDecimal sumBayarKasbon(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumBayarKasbon(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumBayarKasbon(Integer lambung, Date startDate, Date endDate) {
        return stoDao.sumBayarKasbon(lambung, startDate, endDate);
    }

    public BigDecimal sumOvertime(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumOvertime(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumCicilan(Date startPeriodTime, Date endPeriodTime) {
        return stoDao.sumCicilan(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumCicilan(Integer lambung, Date startDate, Date endDate) {
        return stoDao.sumCicilan(lambung, startDate, endDate);
    }

    public void save(setoranDetailPutih stoDetailPutih) {
        stDetailPutihDao.save(stoDetailPutih);
    }

    public void delete(setoranDetailPutih stDetailPutih) {
        stDetailPutihDao.delete(stDetailPutih);
    }

    public List<setoranPutih> setoranPutihRecordInCustomRange(Date periodTime) {
        return stPutihDao.setoranPutihRecordInCustomRange(periodTime);
    }

    public List<setoranPutih> setoranPutihRecordInCustomRange(Date periodTime, Date endPeriodTime) {
        return stPutihDao.setoranPutihRecordInCustomRange(endPeriodTime, endPeriodTime);
    }

    public List<setoranDetailPutih> setoranDetailPutihRecord() {
        return stDetailPutihDao.setoranPutihDetailRecord();
    }

    public List<setoranPutih> findAllStatusSetoranPutih(TransaksiStatus status) {
        return stPutihDao.findAllStatusSetoran(status);
    }

    public List<setoranPutih> findStatusSetoranPutihByLambung(Integer lambung, TransaksiStatus status) {
        return stPutihDao.findStatusSetoranByLambung(lambung, status);
    }

    public List<setoran> hutangRecord(TransaksiStatus status) {
        return stoDao.hutangRecordDetailPiutang(status);
    }

    public List<setoranPutih> hutangRecordPutih(TransaksiStatus status) {
        return stPutihDao.hutangRecordPutihForPiutang(status);
    }

    public List<setoran> findLastTglJatuhTempo(JalanStatus status) {
        return stoDao.findLastTglJatuhTempo(status);
    }

    public List<setoranPutih> findLastTglJatuhTempoPutih(JalanStatus status) {
        return stPutihDao.findLastTglJatuhTempo(status);
    }

    public List<setoran> isClosedTransaction(isClosedStatus closedStatus) {
        return stoDao.isClosedTransaction(closedStatus);
    }

    public void save(closingBulanan clb) {
        clbDao.save(clb);
    }

    public void delete(closingBulanan clb) {
        clbDao.delete(clb);
    }

    public List<closingBulanan> closingBulananRecord() {
        return clbDao.closingBulananRecord();
    }

    public List<closingBulanan> closingBulananFirstRecord() {
        return clbDao.closingBulananFirstRecord();
    }

    public List<closingBulanan> closingBulananFirstRecord(Integer lambung) {
        return clbDao.closingBulananFirstRecord(lambung);
    }

    public List<closingBulanan> closingBulananInCustomRange(Date thisMonth) {
        return clbDao.closingBulananInCustomRange(thisMonth);
    }

    public void save(closingTahunan clt) {
        cltDao.save(clt);
    }

    public void delete(closingTahunan clt) {
        cltDao.delete(clt);
    }

    public List<closingTahunan> closingTahunanRecord() {
        return cltDao.closingTahunanRecord();
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal() {
        return clbDao.closingBulananRecordForSaldoAwal();
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month) {
        return clbDao.closingBulananRecordForSaldoAwal(idKomposisi, month);
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwalAll(Date thisMonth, komposisiSetoran ks) {
        return clbDao.closingBulananRecordForSaldoAwalAll(thisMonth, ks);
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal(aksiClosing act) {
        return clbDao.closingBulananRecordForSaldoAwal(act);
    }

    public closingBulanan findClosingSaldoAwalByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbDao.findClosingSaldoAwalByRefNoLambung(refLambung, whenMonth);
    }

    public closingBulanan findClosingByRefNoLambung(Integer refLambung) {
        return clbDao.findClosingByRefNoLambung(refLambung);
    }

    public closingBulanan findClosingByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbDao.findClosingByRefNoLambung(refLambung, whenMonth);
    }
    
    public closingBulanan findNewClosingByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbDao.findNewClosingByRefNoLambung(refLambung, whenMonth);
    }

    public closingBulanan findAnyClosingBulananValid(Date thisMonth, isClosedFor closedFor) {
        return clbDao.findAnyClosingBulananValid(thisMonth, closedFor);
    }

    public void save(closingBulananPutih clbPutih) {
        clbPutihDao.save(clbPutih);
    }

    public void delete(closingBulananPutih clbPutih) {
        clbPutihDao.delete(clbPutih);
    }

    public List<closingBulananPutih> closingBulananPutihRecord() {
        return clbPutihDao.closingBulananPutihRecord();
    }

    public List<closingBulananPutih> closingBulananPutihFirstRecord() {
        return clbPutihDao.closingBulananPutihFirstRecord();
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal() {
        return clbPutihDao.closingBulananPutihRecordForSaldoAwal();
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(Date whenMonth) {
        return clbPutihDao.closingBulananPutihRecordForSaldoAwal(whenMonth);
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month) {
        return clbPutihDao.closingBulananPutihRecordForSaldoAwal(idKomposisi, month);
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwalAll(komposisiSetoran idKomposisi, Date month) {
        return clbPutihDao.closingBulananPutihRecordForSaldoAwalAll(idKomposisi, month);
    }

    public List<closingBulananPutih> closingBulananPutihInCustomRange(Date thisMonth) {
        return clbPutihDao.closingBulananPutihInCustomRange(thisMonth);
    }

    public closingBulananPutih findClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbPutihDao.findClosingPutihByRefNoLambung(refLambung, whenMonth);
    }

    public closingBulananPutih findClosingSaldoAwalPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbPutihDao.findClosingSaldoAwalPutihByRefNoLambung(refLambung, whenMonth);
    }

    public closingBulananPutih findNewClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        return clbPutihDao.findNewClosingPutihByRefNoLambung(refLambung, whenMonth);
    }

    public void save(closingTahunanPutih cltPutih) {
        cltPutihDao.save(cltPutih);
    }

    public void delete(closingTahunanPutih cltPutih) {
        cltPutihDao.delete(cltPutih);
    }

    public List<closingTahunanPutih> closingTahunanPutihRecord() {
        return cltPutihDao.closingTahunanPutihRecord();
    }

    public BigDecimal sumAngsuranPutih(Date periodTime) {
        return stPutihDao.sumAngsuran(periodTime);
    }

    public BigDecimal sumAngsuranPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumAngsuran(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumAngsuranPutih(Integer lambung, Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumAngsuran(lambung, startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumTabunganPutih(Date periodTime) {
        return stPutihDao.sumTabungan(periodTime);
    }

    public BigDecimal sumTabunganPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumTabungan(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumTabunganPutih(Integer lambung, Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumTabungan(lambung, startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumSetoranPutih(Date periodTime) {
        return stPutihDao.sumSetoran(periodTime);
    }

    public BigDecimal sumSetoranPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumSetoran(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumSetoranPutih(Integer lambung, Date periodTime, Date periodYear) {
        return stPutihDao.sumSetoran(lambung, periodTime, periodYear);
    }

    public BigDecimal sumKasbonPutih(Date periodTime) {
        return stPutihDao.sumKasbon(periodTime);
    }

    public BigDecimal sumKasbonPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumKasbon(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumKasbonPutih(Integer lambung, Date startDate, Date endDate) {
        return stPutihDao.sumKasbon(lambung, startDate, endDate);
    }

    public BigDecimal sumBayarKasbonPutih(Date periodTime) {
        return stPutihDao.sumBayarKasbon(periodTime);
    }

    public BigDecimal sumBayarKasbonPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumBayarKasbon(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumBayarKasbonPutih(Integer lambung, Date startDate, Date endDate) {
        return stPutihDao.sumBayarKasbon(lambung, startDate, endDate);
    }

    public BigDecimal sumOvertimePutih(Date periodTime) {
        return stPutihDao.sumOvertime(periodTime);
    }

    public BigDecimal sumOvertimePutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumOvertime(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumOvertimePutih(Integer lambung, Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumOvertime(lambung, startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumCicilanPutih(Date periodTime) {
        return stPutihDao.sumCicilan(periodTime);
    }

    public BigDecimal sumCicilanPutih(Date startPeriodTime, Date endPeriodTime) {
        return stPutihDao.sumCicilan(startPeriodTime, endPeriodTime);
    }

    public BigDecimal sumCicilanPutih(Integer lambung, Date startDate, Date endDate) {
        return stPutihDao.sumCicilan(lambung, startDate, endDate);
    }
}
