/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman.dao;

import java.math.BigDecimal;
import java.util.Date;
import java.util.List;
import org.hibernate.Criteria;
import org.hibernate.Hibernate;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;
import org.joda.time.DateTime;
import org.springframework.stereotype.Repository;
import paperman.model.JalanStatus;
import paperman.model.TransaksiStatus;
import paperman.model.isClosedStatus;
import paperman.model.komposisiSetoran;
import paperman.model.setoran;
import paperman.model.setoranDetail;

/**
 *
 * @author i1440ns
 */
@Repository
public class TransaksiSetoranDao extends BaseDaoHibernate<setoran> {

    public setoran findByCode(Integer id) {
        setoran st = (setoran) sessionFactory.getCurrentSession()
                .createQuery("from setoran st where st.id=:id")
                .setInteger("id", id)
                .uniqueResult();
        Hibernate.initialize(st.getId());
        Hibernate.initialize(st.getDetail());
        return st;
    }

    public setoran findByKodeSetoran(String kode) {
        setoran sto = (setoran) sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .add(Restrictions.like("kode", kode, MatchMode.END))
                .uniqueResult();
        return sto;
    }
    
    public List<setoranDetail> findByNoLambung(Integer lambung, Date whenMonth) {
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail sd where sd.kemudi.kend.noLambung=:lambung and month(sd.setor_map.tglSPO)=month(:thisMonth)")
                .setInteger("lambung", lambung)
                .setDate("thisMonth", whenMonth)
                .setMaxResults(1)
                .list();
        return listSetoranDetail;
    }

    public List<setoran> getUpdatedSetoranInRange(Integer lambung, Date spoAwal, Date spoAkhir) {
        List<setoran> listSto = sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .add(Restrictions.gt("tglSPO", spoAwal))
                .add(Restrictions.le("tglSPO", spoAkhir))
                .createCriteria("detail", Criteria.INNER_JOIN)
                .createCriteria("kemudi")
                .createCriteria("kend")
                .add(Restrictions.eq("noLambung", lambung))
                .list();
        return listSto;
    }

    public List<setoranDetail> findSetoranNoLambungReturned(Date thisMonth, komposisiSetoran tagArmada) {
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createCriteria(setoranDetail.class)
                .createCriteria("setor_map", Criteria.INNER_JOIN)
                .add(Restrictions.eq("idKomposisi", tagArmada))
                .add(Restrictions.and(
                        Restrictions.ge("tglSPO", new DateTime(thisMonth).withDayOfMonth(new DateTime(thisMonth).dayOfMonth().getMinimumValue()).toDate()),
                        Restrictions.le("tglSPO", new DateTime(thisMonth).withDayOfMonth(new DateTime(thisMonth).dayOfMonth().getMaximumValue()).toDate())))

                .createCriteria("detail", "det", Criteria.INNER_JOIN)
                .createCriteria("kemudi", "kem", Criteria.INNER_JOIN)
                .createCriteria("kend", "ken", Criteria.INNER_JOIN)
                .setProjection(Projections.distinct(Projections.property("ken.noLambung")))
                .list();
        return listSetoranDetail;
    }

    public List<setoran> findAnyLeftClosingBulananValid(Date thisMonth, isClosedStatus closedStatus) {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran sto where sto.closedStatus=:status and month(sto.tglSPO)=month(:bulan) and sto.idClosing is null")
                .setParameter("status", closedStatus)
                .setDate("bulan", thisMonth)
                .list();
        return listSetoran;

    }

    public List<setoran> findAllStatusSetoran(TransaksiStatus status) {
        List<setoran> listStatus = sessionFactory.getCurrentSession()
                .createQuery("from setoran where payedStatus=:status")
                .setParameter("status", status)
                .list();
        return listStatus;
    }

    public List<setoran> findAllAvailableSetoranByLambung(Integer lambung, Date tglSpo, isClosedStatus status) {
        List<setoran> listAvailableSetoran = sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .add(Restrictions.eq("tglSPO", tglSpo))
                .add(Restrictions.eq("closedStatus", status))
                .createCriteria("detail", Criteria.INNER_JOIN)
                .createCriteria("kemudi", Criteria.INNER_JOIN)
                .createCriteria("kend", Criteria.INNER_JOIN)
                .add(Restrictions.eq("noLambung", lambung))
                .list();
        for(setoran st : listAvailableSetoran) {
            Hibernate.initialize(st.getDetail());
        }
        return listAvailableSetoran;
    }

    public List<setoran> findStatusSetoranByLambung(Integer lambung, TransaksiStatus status){
        List<setoran> listStatus = sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .add(Restrictions.eq("payedStatus", status))
                .createCriteria("detail", Criteria.INNER_JOIN)
                .createCriteria("kemudi", Criteria.INNER_JOIN)
                .createCriteria("kend", Criteria.INNER_JOIN)
                .add(Restrictions.eq("noLambung", lambung))
                .list();
        return listStatus;
    }

    public List<setoran> findAvailableSetoran(isClosedStatus closedStatus){
        List<setoran> listStatus = sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .add(Restrictions.eq("closedStatus", closedStatus))
                .list();
        return listStatus;
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada){
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail std where std.setor_map.closedStatus =:status and std.setor_map.idKomposisi.namaKomposisi=:armada and month(std.setor_map.tglSPO)=month(:bln) and year(std.setor_map.tglSPO)=year(:thn) order by std.kemudi.kend.noLambung, std.setor_map.counter_setoran asc")
                .setParameter("status", closedStatus)
                .setString("armada", tagArmada)
                .setDate("bln", periodeBulan)
                .setDate("thn", periodeTahun)
                .list();
        return listSetoranDetail;
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, Date periodeBulan, Date periodeTahun, String tagArmada, Integer offset, Integer limit){
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail std where std.setor_map.closedStatus =:status and std.setor_map.idKomposisi.namaKomposisi=:armada and month(std.setor_map.tglSPO)=month(:bln) and year(std.setor_map.tglSPO)=year(:thn) order by std.kemudi.kend.noLambung, std.setor_map.counter_setoran asc")
                .setParameter("status", closedStatus)
                .setString("armada", tagArmada)
                .setDate("bln", periodeBulan)
                .setDate("thn", periodeTahun)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();
        return listSetoranDetail;
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada){
         List<setoranDetail> listStatus = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail stDet where stDet.setor_map.closedStatus=:closed and stDet.angsuran=:angsur and stDet.tabungan=:tabungan and stDet.setor_map.idKomposisi.namaKomposisi=:armada " +
                "order by stDet.kemudi.kend.noLambung, stDet.setor_map.counter_setoran, stDet.setor_map.tglSPO asc")
                .setParameter("closed", closedStatus)
                .setBigDecimal("angsur", valAngsuran)
                .setBigDecimal("tabungan", valTabungan)
                .setString("armada", tagArmada)
                .list();

        return listStatus;
    }

    public List<setoranDetail> findAvailableSetoran(isClosedStatus closedStatus, BigDecimal valAngsuran, BigDecimal valTabungan, String tagArmada, Integer offset, Integer limit){
//        List<setoran> listStatus = sessionFactory.getCurrentSession()
//                .createCriteria(setoran.class)
//                .add(Restrictions.eq("closedStatus", closedStatus))
//                .createCriteria("detail", Criteria.INNER_JOIN)
//                .add(Restrictions.and(Restrictions.eq("angsuran", valAngsuran), Restrictions.eq("tabungan", valTabungan)))
//                .list();
         List<setoranDetail> listStatus = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail stDet where stDet.setor_map.closedStatus=:closed and stDet.angsuran=:angsur and stDet.tabungan=:tabungan and stDet.setor_map.idKomposisi.namaKomposisi=:armada " +
                "order by stDet.kemudi.kend.noLambung, stDet.setor_map.counter_setoran, stDet.setor_map.tglSPO asc")
                .setParameter("closed", closedStatus)
                .setBigDecimal("angsur", valAngsuran)
                .setBigDecimal("tabungan", valTabungan)
                .setString("armada", tagArmada)
                .setFirstResult(offset)
                .setMaxResults(limit)
                .list();

        return listStatus;
    }

    public List<setoran> findLastTglJatuhTempo(JalanStatus statusJalan) {
        List<setoran> ListSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran s where s.jalanStatus=:status order by s.tglJatuhTempo desc")
                .setParameter("status", statusJalan)
                .list();
        return ListSetoran;
    }

//    public List<setoran> findStatusSetoranByLambung(Integer lambung, TransaksiStatus status, TransaksiStatus newStatus) {
//        List<setoran> listStatus = sessionFactory.getCurrentSession()
//                .createQuery("from setoran s inner join s.detail det where det.kemudi.kend.noLambung=:lambung and det.setor_map.payedStatus=:status")
//                .setInteger("lambung", lambung)
//                .setParameter("status", status)
//                .list();
//        for(int i=0; i<listStatus.size();i++) {
//            if(listStatus.get(i).getPayedStatus() == newStatus) {
//                listStatus.get(i).setPayedStatus(TransaksiStatus.L);
//                sessionFactory.getCurrentSession().flush();
//            }
//        }
//        return listStatus;
//    }

    public setoran findBySpecific(Integer lambung, Date tglSetoran, Integer setoran) {
        setoran st = (setoran) sessionFactory.getCurrentSession()
                .createQuery("from setoran s where s.detail.kemudi.kend.noLambung=:lambung and s.tglSetoran=:tglSetoran and s.counter_setoran=:setoran")
                .setInteger("lambung", lambung)
                .setDate("tglSetoran", tglSetoran)
                .setInteger("setoran", setoran)
                .uniqueResult();
        Hibernate.initialize(st.getDetail());
        return st;
    }

    public List<setoran> hutangRecordDetailPiutang(TransaksiStatus status) {
        List<setoran> listSto = sessionFactory.getCurrentSession()
                .createQuery("from setoran s where s.payedStatus=:status")
                .setParameter("status", status)
                .list();
        for(setoran s : listSto) {
            Hibernate.initialize(s.getDetail());
        }
        return listSto;
    }

    public BigDecimal sumHutang(Integer lambung, TransaksiStatus status) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(s.totalHutang) from setoran s inner join s.detail det where det.kemudi.kend.noLambung=:lambung and det.setor_map.payedStatus=:status")
                .setInteger("lambung", lambung)
                .setParameter("status", status)
                .uniqueResult();
    }

    public BigDecimal sumSetoran(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(s.totalSetoran) from setoran s where month(s.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumSetoran(Integer lambung, Date periodTime, Date periodYear) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.setor_map.totalSetoran) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO)=month(:datePeriod) and year(std.setor_map.tglSPO)=year(:dateYear)")
                .setInteger("lb", lambung)
                .setDate("datePeriod", periodTime)
                .setDate("dateYear", periodYear)
                .uniqueResult();
    }

    public BigDecimal sumSetoran(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(s.totalSetoran) from setoran s where year(s.tglSPO)>=year(:startPeriod) and year(s.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumAngsuran(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.angsuran) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumAngsuran(Integer lambung, Date periodTime, Date periodYear) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.angsuran) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO)=month(:datePeriod) and year(std.setor_map.tglSPO)=year(:dateYear)")
                .setInteger("lb", lambung)
                .setDate("datePeriod", periodTime)
                .setDate("dateYear", periodYear)
                .uniqueResult();
    }

    public BigDecimal sumAngsuran(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.angsuran) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumTabungan(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.tabungan) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumTabungan(Integer lambung, Date periodTime, Date periodYear) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.tabungan) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO)=month(:datePeriod) and year(std.setor_map.tglSPO)=year(:dateYear)")
                .setInteger("lb", lambung)
                .setDate("datePeriod", periodTime)
                .setDate("dateYear", periodYear)
                .uniqueResult();
    }

    public BigDecimal sumTabungan(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.tabungan) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumKasbon(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.kasbon) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumKasbon(Integer lambung, Date startDate, Date endDate) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.kasbon) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO)= month(:start) and year(std.setor_map.tglSPO) = year(:end)")
                .setInteger("lb", lambung)
                .setDate("start", startDate)
                .setDate("end", endDate)
                .uniqueResult();
    }

    public BigDecimal sumKasbon(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.kasbon) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumBayarKasbon(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.bayar) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumBayarKasbon(Integer lambung, Date startDate, Date endDate) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.bayar) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO) = month(:start) and year(std.setor_map.tglSPO) = year(:end)")
                .setInteger("lb", lambung)
                .setDate("start", startDate)
                .setDate("end", endDate)
                .uniqueResult();
    }

    public BigDecimal sumBayarKasbon(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.bayar) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumOvertime(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.ovtime) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumOvertime(Integer lambung, Date startDate, Date endDate) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.ovtime) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO) = month(:start) and year(std.setor_map.tglSPO) = year(:end)")
                .setInteger("lb", lambung)
                .setDate("start", startDate)
                .setDate("end", endDate)
                .uniqueResult();
    }

    public BigDecimal sumOvertime(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.ovtime) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public BigDecimal sumCicilan(Date periodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.KS) from setoranDetail std where month(std.setor_map.tglSPO)=month(:datePeriod)")
                .setDate("datePeriod", periodTime)
                .uniqueResult();
    }

    public BigDecimal sumCicilan(Integer lambung, Date startDate, Date endDate) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.KS) from setoranDetail std where std.kemudi.kend.noLambung=:lb and month(std.setor_map.tglSPO) = month(:start) and year(std.setor_map.tglSPO) = year(:end)")
                .setInteger("lb", lambung)
                .setDate("start", startDate)
                .setDate("end", endDate)
                .uniqueResult();
    }

    public BigDecimal sumCicilan(Date startPeriodTime, Date endPeriodTime) {
        return (BigDecimal) sessionFactory.getCurrentSession()
                .createQuery("select sum(std.KS) from setoranDetail std where year(std.setor_map.tglSPO)>=year(:startPeriod) and year(std.setor_map.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .uniqueResult();
    }

    public List<setoran> isClosedTransaction(isClosedStatus closedStatus) {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran st where st.closedStatus=:status DESC")
                .setParameter("status", closedStatus)
                .list();
        return listSetoran;
    }

    public setoran findByHutangNoLambung(Integer lambung, TransaksiStatus status) {
        setoran st = (setoran) sessionFactory.getCurrentSession()
//                .createQuery("from setoran st inner join st.detail det where det.kemudi.kend.noLambung=:lb and det.setor_map.payedStatus=:stats group by det.kemudi.kend.noLambung")
//                .setInteger("lb", lambung)
//                .setParameter("stats", status)
//                .uniqueResult();
                .createCriteria(setoran.class)
                .add(Restrictions.eq("payedStatus", status))
                .createCriteria("detail", Criteria.INNER_JOIN)
                .createCriteria("kemudi", Criteria.INNER_JOIN)
                .createCriteria("kend", Criteria.INNER_JOIN)
                .add(Restrictions.eq("noLambung", lambung))
                .uniqueResult();
        Hibernate.initialize(st.getDetail());
        return st;
    }

    public List<setoran> setoranRecord() {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createCriteria(setoran.class)
                .list();

        for (setoran str : listSetoran) {
            Hibernate.initialize(str.getDetail());
        }
        return listSetoran;
    }

    public List<setoran> setoranRecordInCustomRange(Date periodTime) {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran str where month(str.tglSPO)=month(:period)")
                .setDate("period", periodTime)
                .list();

        for (setoran str : listSetoran) {
            Hibernate.initialize(str.getDetail());
        }
        return listSetoran;
    }

    public List<setoran> setoranRecordInCustomRange(Date startPeriodTime, Date endPeriodTime) {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran str where year(str.tglSPO)>=year(:startPeriod) and year(str.tglSPO)<=year(:endPeriod)")
                .setDate("startPeriod", startPeriodTime)
                .setDate("endPeriod", endPeriodTime)
                .list();

        for (setoran str : listSetoran) {
            Hibernate.initialize(str.getDetail());
        }
        return listSetoran;
    }

    public List<setoran> setoranRecordInCustomRange(Date whenMonth, komposisiSetoran whatArmada) {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran str where month(str.tglSPO)=month(:when) and str.idKomposisi=:armada")
                .setDate("when", whenMonth)
                .setParameter("armada", whatArmada)
                .list();

        for (setoran str : listSetoran) {
            Hibernate.initialize(str.getDetail());
        }
        return listSetoran;
    }

    public List<setoran> getLatestSetoranCount() {
        List<setoran> listSetoran = sessionFactory.getCurrentSession()
                .createQuery("from setoran order by counter_setoran desc")
                .setMaxResults(1)
                .list();
        return listSetoran;
    }

    public Integer getTotalSetoranCount(komposisiSetoran ks) {
        return (Integer) sessionFactory.getCurrentSession()
                .createQuery("from setoran st where st.idKomposisi=:dataKs")
                .setParameter("dataKs", ks)
                .list()
                .size();
    }
}
