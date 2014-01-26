/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import paperman.model.kendaraanPutih;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterKendaraanPutihDao extends BaseDaoHibernate<kendaraanPutih>{
    public List<kendaraanPutih> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from kendaraanPutih order by noLambung").list();
    }
    public List<kendaraanPutih> findKendaraanByLambung(Integer noLambung) {
        String query = "from kendaraanPutih kend where kend.noLambung like '%";
        query += noLambung + "%' order by kend.noLambung";
        List<kendaraanPutih> listKendaraanPutih = sessionFactory.getCurrentSession()
                .createQuery(query)
                .list();
        return listKendaraanPutih;
    }
}
