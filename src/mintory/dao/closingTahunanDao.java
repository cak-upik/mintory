/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.closingTahunan;

/**
 *
 * @author Nurul Chusna
 */
@Repository
public class closingTahunanDao extends BaseDaoHibernate<closingTahunan> {
    public List<closingTahunan> closingTahunanRecord() {
        List<closingTahunan> listClosingTahunan = sessionFactory.getCurrentSession()
                .createQuery("from closingTahunan clt")
                .list();
        return listClosingTahunan;
    }
}
