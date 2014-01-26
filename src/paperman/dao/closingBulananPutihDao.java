/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.util.Date;
import java.util.List;
import org.springframework.stereotype.Repository;
import paperman.model.closingBulananPutih;
import paperman.model.isClosedFor;
import paperman.model.komposisiSetoran;

/**
 *
 * @author Nurul Chusna
 */
@Repository
public class closingBulananPutihDao extends BaseDaoHibernate<closingBulananPutih> {
    public List<closingBulananPutih> closingBulananPutihRecord() {
        List<closingBulananPutih> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb")
                .list();
        return listClosingBulanan;
    }

    public List<closingBulananPutih> closingBulananPutihFirstRecord() {
        List<closingBulananPutih> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb order by clb.refNoLambung asc")
                .setMaxResults(1)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal() {
        List<closingBulananPutih> listClosingBulananPutih = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.closedFor= :closedType order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .list();
        return listClosingBulananPutih;
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(Date monthClosing) {
        List<closingBulananPutih> listClosingBulananPutih = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.closedFor= :closedType and month(clb.periodeBulan)=month(:whenMonth) and clb.idKomposisi is not null order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setDate("whenMonth", monthClosing)
                .list();
        return listClosingBulananPutih;
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwal(komposisiSetoran idKomposisi, Date month) {
        List<closingBulananPutih> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.closedFor= :closedType and idKomposisi=:ks and month(clb.periodeBulan)= month(:when) and clb.idKomposisi is not null order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setParameter("ks", idKomposisi)
                .setParameter("when", month)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulananPutih> closingBulananPutihRecordForSaldoAwalAll(komposisiSetoran idKomposisi, Date month) {
        List<closingBulananPutih> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.closedFor= :closedType and idKomposisi=:ks and month(clb.periodeBulan)= month(:when) order by clb.refNoLambung asc")
                .setParameter("closedType", isClosedFor.CLOSING_SALDO_AWAL)
                .setParameter("ks", idKomposisi)
                .setParameter("when", month)
                .list();
        return listClosingBulanan;
    }

    public List<closingBulananPutih> closingBulananPutihInCustomRange(Date thisMonth) {
        List<closingBulananPutih> listClosingBulanan = sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where month(clb.periodeBulan)= month(:thisMonth) and clb.idKomposisi is null order by clb.refNoLambung asc")
                .setDate("thisMonth", thisMonth)
                .list();
        return listClosingBulanan;
    }

    public closingBulananPutih findClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulananPutih clbPth = (closingBulananPutih) sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth)")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clbPth;
    }

    public closingBulananPutih findClosingSaldoAwalPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulananPutih clb = (closingBulananPutih) sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth) and clb.idKomposisi is not null")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clb;
    }

    public closingBulananPutih findNewClosingPutihByRefNoLambung(Integer refLambung, Date whenMonth) {
        closingBulananPutih clbPth = (closingBulananPutih) sessionFactory.getCurrentSession()
                .createQuery("from closingBulananPutih clb where clb.refNoLambung=:no and month(clb.periodeBulan)=month(:whenMonth) and clb.idKomposisi is null")
                .setInteger("no", refLambung)
                .setDate("whenMonth", whenMonth)
                .uniqueResult();
        return clbPth;
    }
}
