/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import paperman.model.pengemudiPutih;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterPengemudiPutihDao extends BaseDaoHibernate<pengemudiPutih>{
    public List<pengemudiPutih> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from pengemudiPutih order by nrp").list();
    }

}
