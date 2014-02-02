/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.aksiClosing;
import mintory.model.closingBulanan;
import mintory.model.isClosedFor;
import mintory.model.komposisiSetoran;

/**
 * PETUNJUK :
 * BEDA CLOSING BULANAN DENGAN CLOSING SALDO AWAL
 * CLOSING BULANAN :
 * 1. PERIODE BULAN VALID
 * 2. TGL CLOSING VALID
 * 3. ID KOMPOSISI NULL
 * 4. USER SET VALID
 * CLOSING SALDO AWAL :
 * 1. PERIODE BULAN VALID
 * 2. TGL CLOSING TIDAK VALID
 * 3. ID KOMPOSISI NOT NULL
 * 4. USER SET TIDAK VALID
 * 
 */
@Repository
public class closingBulananDao extends BaseDaoHibernate<closingBulanan> {
    public List<closingBulanan> closingBulananRecord() {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb")
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananFirstRecord() {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb order by clb.refNoLambung asc")
                .setMaxResults(1)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananFirstRecord(Integer lambung) {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.refNoLambung=:lb order by clb.refNoLambung asc")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal() {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.closedFor= :closedType order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month) {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.closedFor= :closedType and idKomposisi=:ks and month(clb.periodeBulan)= month(:when) and clb.idKomposisi is not null order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setParameter("ks", idKomposisi)
                .setParameter("when", month)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwalAll(Date thisMonth, komposisiSetoran ks) {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.closedFor= :closedType and month(clb.periodeBulan)= month(:thisMonth) and clb.idKomposisi=:id order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setParameter("id", ks)
                .setDate("thisMonth", thisMonth)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananInCustomRange(Date thisMonth) {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where month(clb.periodeBulan)= month(:thisMonth) and clb.idKomposisi is null order by clb.refNoLambung asc")
                .setDate("thisMonth", thisMonth)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulanan> closingBulananRecordForSaldoAwal(aksiClosing act) {
        List<closingBulanan> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.closedFor= :closedType and actClosing= :whatAct order by id desc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setParameter("whatAct", act)
                .setMaxResults(1)
                .list();
        return listClosingBulanan;
    }

    public closingBulanan findAnyClosingBulananValid(Date thisMonth, isClosedFor closedFor) {
        closingBulanan clb = (closingBulanan) sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where month(clb.tglClosing)=month(:dateClosing) and clb.closedFor=:closingParam")
                .setDate("dateClosing", thisMonth)
                .setParameter("closingParam", closedFor)
                .uniqueResult();
        return clb;

    }

    public closingBulanan findClosingByRefNoLambung(Integer refLambung) {
        closingBulanan clb = (closingBulanan) sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.refNoLambung=:no")
                .setInteger("no", refLambung)
                .uniqueResult();
        return clb;
    }

    public closingBulanan findClosingByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulanan clb = (closingBulanan) sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth)")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clb;
    }

    public closingBulanan findClosingSaldoAwalByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulanan clb = (closingBulanan) sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth) and clb.idKomposisi is not null")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clb;
    }

    public closingBulanan findNewClosingByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulanan clb = (closingBulanan) sessionFactory.getCurrentSession()
                .createQuery("from closingBulanan clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth) and clb.idKomposisi is null")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clb;
    }
}
