/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import mintory.model.setoranDetailPutih;

/**
 *
 * @author i1440ns
 */
@Repository
public class TransaksiSetoranDetailPutihDao extends BaseDaoHibernate<setoranDetailPutih>{

    public List<setoranDetailPutih> getLatestSetoranCount(Integer lambung) {
        List<setoranDetailPutih> listSetoranDetailPth = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetailPutih sp where sp.kemudiPutih.kendPutih.noLambung = :lb order by sp.setor_map_pth.counter_setoran desc")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        return listSetoranDetailPth;
    }

    public List<setoranDetailPutih> setoranPutihDetailRecord() {
        List<setoranDetailPutih> listSetoranPutihDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetailPutih stDetail").list();

        for(setoranDetailPutih stDet : listSetoranPutihDetail) {
            Hibernate.initialize(stDet.getSetor_map_putih().getDetailPutih());
        }
        return listSetoranPutihDetail;
    }

    public List<setoranDetailPutih> findLastSetoranDetailByLambung(Integer lambung) {
        List<setoranDetailPutih> listStdPth = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetailPutih sd where sd.kemudiPutih.kendPutih.noLambung= :lb order by sd.setor_map_pth.counter_setoran desc")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        for(setoranDetailPutih stDetPth : listStdPth) {
            Hibernate.initialize(stDetPth.getSetor_map_putih().getDetailPutih());
        }
        return listStdPth;
    }

    public List<setoranDetailPutih> findSetoranDetailByLambung(Integer lambung, Date tglSetoran) {
        List<setoranDetailPutih> listStdPth = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetailPutih sd where sd.kemudiPutih.kendPutih.noLambung= :lb and day(sd.setor_map_pth.tglSPO)= day(:tgl) order by sd.setor_map_pth.counter_setoran desc")
                .setInteger("lb", lambung)
                .setDate("tgl", tglSetoran)
                .setMaxResults(1)
                .list();
        for(setoranDetailPutih stDetPth : listStdPth) {
            Hibernate.initialize(stDetPth.getSetor_map_putih().getDetailPutih());
        }
        return listStdPth;
    }
}
