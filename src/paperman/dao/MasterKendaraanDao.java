/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import paperman.model.kendaraan;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterKendaraanDao extends BaseDaoHibernate<kendaraan>{
    public List<kendaraan> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from kendaraan order by noLambung").list();
    }
    public List<kendaraan> findKendaraanByLambung(Integer noLambung) {
        String query = "from kendaraan kend where kend.noLambung like '%";
        query += noLambung + "%' order by kend.noLambung";
        List<kendaraan> listKendaraan = sessionFactory.getCurrentSession()
                .createQuery(query)
                .list();
        return listKendaraan;
    }
}
