/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.util.Date;
import java.util.List;
import org.hibernate.Hibernate;
import org.springframework.stereotype.Repository;
import paperman.model.setoranDetail;

/**
 *
 * @author i1440ns
 */
@Repository
public class TransaksiSetoranDetailDao extends BaseDaoHibernate<setoranDetail>{

    public List<setoranDetail> getLatestSetoranCount(Integer lambung) {
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail st where st.kemudi.kend.noLambung = :lb order by st.setor_map.counter_setoran desc")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        return listSetoranDetail;
    }

    public List<setoranDetail> findSetoranDetailByLambung(Integer lambung) {
        List<setoranDetail> listStd = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail sd where sd.kemudi.kend.noLambung= :lb")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        for(setoranDetail stDet : listStd) {
            Hibernate.initialize(stDet.getSetor_map().getDetail());
        }
        return listStd;
    }

    public List<setoranDetail> findSetoranDetailByLambung(Integer lambung, Date tglSetoran) {
        List<setoranDetail> listStd = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail sd where sd.kemudi.kend.noLambung= :lb and day(sd.setor_map.tglSPO)= day(:tgl) order by sd.setor_map.counter_setoran desc")
                .setInteger("lb", lambung)
                .setDate("tgl", tglSetoran)
                .setMaxResults(1)
                .list();
        for(setoranDetail stDet : listStd) {
            Hibernate.initialize(stDet.getSetor_map().getDetail());
        }
        return listStd;
    }

    public List<setoranDetail> findLastSetoranDetailByLambung(Integer lambung) {
        List<setoranDetail> listStd = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail sd where sd.kemudi.kend.noLambung= :lb order by sd.setor_map.counter_setoran desc")
                .setInteger("lb", lambung)
                .setMaxResults(1)
                .list();
        for(setoranDetail stDet : listStd) {
            Hibernate.initialize(stDet.getSetor_map().getDetail());
        }
        return listStd;
    }

    public List<setoranDetail> setoranDetailRecord() {
        List<setoranDetail> listSetoranDetail = sessionFactory.getCurrentSession()
                .createQuery("from setoranDetail stDetail")
                .list();

        for(setoranDetail stDet : listSetoranDetail) {
            Hibernate.initialize(stDet.getSetor_map().getDetail());
        }
        return listSetoranDetail;
    }
}
