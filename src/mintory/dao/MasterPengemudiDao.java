/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.Pengemudi;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterPengemudiDao extends BaseDaoHibernate<Pengemudi>{
    public List<Pengemudi> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from Pengemudi order by nrp").list();
    }

}
