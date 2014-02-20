/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.dao;

import java.util.List;
import org.springframework.stereotype.Repository;
import mintory.model.Barang;

/**
 *
 * @author i1440ns
 */
@Repository
public class MasterBarangDao extends BaseDaoHibernate<Barang>{
    public List<Barang> semuaData() {
        return sessionFactory.getCurrentSession().createQuery("from Barang order by kodeBarang").list();
    }
    public List<Barang> findByKodeBarang(String kodeBrg) {
        String query = "from Barang brg where brg.kodeBarang like '%";
        query += kodeBrg + "%' order by brg.kodeBarang";
        List<Barang> listBarang = sessionFactory.getCurrentSession()
                .createQuery(query)
                .list();
        return listBarang;
    }
}
