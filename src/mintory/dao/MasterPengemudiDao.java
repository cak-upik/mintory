/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.pengemudi;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterPengemudiDao extends BaseDaoHibernate<pengemudi>{
    public List<pengemudi> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from pengemudi order by nrp").list();
    }

}
