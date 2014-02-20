/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.Supplier;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterSupplierDao extends BaseDaoHibernate<Supplier>{
    public List<Supplier> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from Supplier order by kodeSupplier").list();
    }

}
