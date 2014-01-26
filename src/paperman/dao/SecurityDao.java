/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman.dao;

import java.util.List;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import paperman.model.SecurityUser;

/**
 *
 * @author Nurul Chusna
 */
@Repository
public class SecurityDao extends BaseDaoHibernate<SecurityUser> {
    public List<SecurityUser> SecurityRecord() {
        List<SecurityUser> listSecurity = sessionFactory.getCurrentSession()
                .createCriteria(SecurityUser.class)
                .list();
        return listSecurity;
        }

    public SecurityUser findByUsername(String username) {
        SecurityUser su = (SecurityUser) sessionFactory.getCurrentSession()
                .createCriteria(SecurityUser.class)
                .add(Restrictions.eq("namaLogin", username))
                .uniqueResult();
        return su;
    }
}
