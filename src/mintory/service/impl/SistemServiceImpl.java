/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mintory.service.impl;

import java.util.Date;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mintory.dao.GeneratorCodeDao;
import mintory.dao.KomposisiSetoranDao;
import mintory.dao.SecurityDao;
import mintory.dao.SistemDao;
import mintory.model.SecurityUser;
import mintory.model.codeGenerator;
import mintory.model.komposisiSetoran;
import mintory.model.sistem;
import mintory.service.SistemService;

/**
 *
 * @author i1440ns
 */
@Service("sistemService")
@Transactional
public class SistemServiceImpl implements SistemService {

    @Autowired private SistemDao sistemDao;
    @Autowired private KomposisiSetoranDao komposisiDao;
    @Autowired private GeneratorCodeDao generator;
    @Autowired private SecurityDao secUserDao;

    public void save(sistem sys) {
        sistemDao.save(sys);
    }

    public void update(sistem sys) {
        sistemDao.update(sys);
    }

    public void delete(sistem sys) {
        sistemDao.delete(sys);
    }

    public sistem sistemRecord() {
        return sistemDao.sistemRecord();
    }

    public void save(codeGenerator genCode) {
        generator.save(genCode);
    }

    public void delete(codeGenerator genCode) {
        generator.delete(genCode);
    }

    public List<codeGenerator> generateCode(String transactionType, String armadaType, Date now_date) {
        return generator.generateCode(transactionType, armadaType, now_date);
    }

    public List<codeGenerator> codeRecord() {
        return generator.codeRecord();
    }

    public codeGenerator findBySpecific(String tagTransaction, String tagArmada) {
        return generator.findBySpecific(tagTransaction, tagArmada);
    }

    public void save(komposisiSetoran komposisi) {
        komposisiDao.save(komposisi);
    }

    public void delete(komposisiSetoran komposisi) {
        komposisiDao.delete(komposisi);
    }

    public List<komposisiSetoran> komposisiRecord() {
        return komposisiDao.komposisiRecord();
    }

    public List<komposisiSetoran> komposisiBonusRecord() {
        return komposisiDao.komposisiBonusRecord();
    }

    public List<komposisiSetoran> findBonusBulanan() {
        return komposisiDao.findBonusBulanan();
    }

    public List<komposisiSetoran> findBonusBulanan(String komposisi) {
        return komposisiDao.findBonusBulanan(komposisi);
    }

    public komposisiSetoran findBonusBulananPutih() {
        return komposisiDao.findBonusBulananPutih();
    }

    public void save(SecurityUser secUser) {
        secUserDao.save(secUser);
    }

    public void delete(SecurityUser secUser) {
        secUserDao.delete(secUser);
    }

    public List<SecurityUser> SecurityRecord() {
        return secUserDao.SecurityRecord();
    }

    public SecurityUser findByUsername(String username) {
        return secUserDao.findByUsername(username);
    }

    public komposisiSetoran findKomposisiByName(String namaKomposisi) {
        return komposisiDao.findKomposisiByName(namaKomposisi);
    }

    public List<komposisiSetoran> findKomposisiStartsWithName(String namaKomposisi) {
        return komposisiDao.findKomposisiStartsWithName(namaKomposisi);
    }

    public List<komposisiSetoran> findKomposisiLikeName(String namaKomposisi) {
        return komposisiDao.findKomposisiLikeName(namaKomposisi);
    }
}
