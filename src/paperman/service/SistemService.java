/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.service;

import java.util.Date;
import java.util.List;
import paperman.model.SecurityUser;
import paperman.model.codeGenerator;
import paperman.model.komposisiSetoran;
import paperman.model.sistem;

/**
 *
 * @author i1440ns
 */
public interface SistemService {
    public void save(sistem sys);
    public void update(sistem sys);
    public void delete(sistem sys);
//    public sistem getSistemDefault();
    public sistem sistemRecord();

    public void save(codeGenerator genCode);
    public void delete(codeGenerator genCode);
    public List<codeGenerator> generateCode(String transactionType, String armadaType, Date now_date);
    public codeGenerator findBySpecific(String tagTransaction, String tagArmada);
    public List<codeGenerator> codeRecord();

    public void save(komposisiSetoran komposisi);
    public void delete(komposisiSetoran komposisi);
    public List<komposisiSetoran> komposisiRecord();
    public List<komposisiSetoran> komposisiBonusRecord();
    public List<komposisiSetoran> findBonusBulanan();
    public List<komposisiSetoran> findBonusBulanan(String komposisi);
    public komposisiSetoran findBonusBulananPutih();
    public komposisiSetoran findKomposisiByName(String namaKomposisi);
    public List<komposisiSetoran> findKomposisiStartsWithName(String namaKomposisi);
    public List<komposisiSetoran> findKomposisiLikeName(String namaKomposisi);

    public void save(SecurityUser secUser);
    public void delete(SecurityUser secUser);
    public List<SecurityUser> SecurityRecord();
    public SecurityUser findByUsername(String username);
}
