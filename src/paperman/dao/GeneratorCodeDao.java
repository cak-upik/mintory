/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package paperman.dao;

import java.util.Date;
import java.util.List;
import javax.swing.JOptionPane;
import org.hibernate.criterion.Restrictions;
import org.springframework.stereotype.Repository;
import paperman.Main;
import paperman.model.codeGenerator;

/**
 *
 * @author Nurul Chusna
 */
@Repository
public class GeneratorCodeDao extends BaseDaoHibernate<codeGenerator> {
    public List<codeGenerator> generateCode(String transactionType, String armadaType, Date now_date) {
        List<codeGenerator> listCode = sessionFactory.getCurrentSession()
                .createCriteria(codeGenerator.class)
                .add(Restrictions.eq("TagTransaction", transactionType))
                .add(Restrictions.eq("TagArmada", armadaType))
                .add(Restrictions.eq("tanggal", now_date))
                .list();
        if(listCode.isEmpty()) {
            JOptionPane.showMessageDialog(Main.getMainMenu(), "Data Kode Setoran Tidak Ditemukan\nPeriksa Lagi Konfigurasi Nomor Kode! !", "Peringatan Sistem", JOptionPane.ERROR_MESSAGE);
            return null;
        }
        return listCode;
    }

    public List<codeGenerator> codeRecord() {
        List<codeGenerator> listCodeGenerator = sessionFactory.getCurrentSession()
                .createCriteria(codeGenerator.class)
                .list();
        return listCodeGenerator;
    }

    public codeGenerator findBySpecific(String tagTransaction, String tagArmada) {
        codeGenerator cg = (codeGenerator) sessionFactory.getCurrentSession()
                .createQuery("from codeGenerator cg where cg.TagTransaction=:tagTransaction and cg.TagArmada=:tagArmada")
                .setString("tagTransaction", tagTransaction)
                .setString("tagArmada", tagArmada)
                .uniqueResult();
        return cg;
    }
}
