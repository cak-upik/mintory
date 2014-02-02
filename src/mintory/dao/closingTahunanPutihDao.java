/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.closingTahunanPutih;

/**
 *
 * @author Nurul Chusna
 */
@Repository
public class closingTahunanPutihDao extends BaseDaoHibernate<closingTahunanPutih> {
    public List<closingTahunanPutih> closingTahunanPutihRecord() {
        List<closingTahunanPutih> listClosingTahunan = sessionFactory.getCurrentSession()
                .createQuery("from closingTahunanPutih clt")
                .list();
        return listClosingTahunan;
    }
}
