/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mintory.dao.MasterKendaraanDao;
import mintory.dao.MasterKendaraanPutihDao;
import mintory.dao.MasterPengemudiDao;
import mintory.dao.MasterPengemudiPutihDao;
import mintory.model.kendaraan;
import mintory.model.kendaraanPutih;
import mintory.model.pengemudi;
import mintory.model.pengemudiPutih;
import mintory.service.MasterService;

/**
 *
 * @author i1440ns
 */
@Service("masterService")
@Transactional
public class MasterServiceImpl implements MasterService{
    @Autowired private MasterKendaraanDao mkDao;
    @Autowired private MasterPengemudiDao pgDao;
    @Autowired private MasterKendaraanPutihDao mkPutihDao;
    @Autowired private MasterPengemudiPutihDao pgPutihDao;

    @Autowired
    public void setDao(MasterKendaraanDao mkDao, MasterPengemudiDao pgDao) {
        this.mkDao = mkDao;
        this.pgDao = pgDao;
    }

    public void save(kendaraan kend) {
        mkDao.save(kend);
    }

    public void delete(kendaraan kend) {
        mkDao.delete(kend);
    }

    public List<kendaraan> kendaraanRecord() {
        return mkDao.semuaData();
    }

    public void save(pengemudi kemudi) {
        pgDao.save(kemudi);
    }

    public void delete(pengemudi kemudi) {
        pgDao.delete(kemudi);
    }

    public List<pengemudi> kemudiRecord() {
        return pgDao.semuaData();
    }

    public void save(kendaraanPutih kendPutih) {
        mkPutihDao.save(kendPutih);
    }

    public void delete(kendaraanPutih kendPutih) {
        mkPutihDao.delete(kendPutih);
    }

    public List<kendaraanPutih> kendaraanPutihRecord() {
        return mkPutihDao.semuaData();
    }

    public void save(pengemudiPutih kemudiPutih) {
        pgPutihDao.save(kemudiPutih);
    }

    public void delete(pengemudiPutih kemudPutih) {
        pgPutihDao.delete(kemudPutih);
    }

    public List<pengemudiPutih> kemudiPutihRecord() {
        return pgPutihDao.semuaData();
    }

    public List<kendaraan> findKendaraanByLambung(Integer lambung) {
        return mkDao.findKendaraanByLambung(lambung);
    }

    public List<kendaraanPutih> findKendaraanPutihByLambung(Integer lambung) {
        return mkPutihDao.findKendaraanByLambung(lambung);
    }
}