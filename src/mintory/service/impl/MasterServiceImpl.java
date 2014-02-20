/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.service.impl;

import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import mintory.dao.MasterBarangDao;
import mintory.dao.MasterKendaraanPutihDao;
import mintory.dao.MasterSupplierDao;
import mintory.dao.MasterPengemudiDao;
import mintory.model.Barang;
import mintory.model.kendaraanPutih;
import mintory.model.Supplier;
import mintory.model.Pengemudi;
import mintory.service.MasterService;

/**
 *
 * @author i1440ns
 */
@Service("masterService")
@Transactional
public class MasterServiceImpl implements MasterService{
    @Autowired private MasterBarangDao mstBrgDao;
    @Autowired private MasterSupplierDao supplyDao;
    @Autowired private MasterPengemudiDao kemudiDao;

    @Autowired
    public void setDao(MasterBarangDao mstBrgDao, MasterSupplierDao suppDao) {
        this.mstBrgDao = mstBrgDao;
        this.supplyDao = suppDao;
    }

    public void save(Barang brg) {
        mstBrgDao.save(brg);
    }

    public void delete(Barang brg) {
        mstBrgDao.delete(brg);
    }

    public List<Barang> barangRecord() {
        return mstBrgDao.semuaData();
    }

    public void save(Supplier supply) {
        supplyDao.save(supply);
    }

    public void delete(Supplier supply) {
        supplyDao.delete(supply);
    }

    public List<Supplier> supplierRecord() {
        return supplyDao.semuaData();
    }

    public void save(Pengemudi kemudi) {
        kemudiDao.save(kemudi);
    }

    public void delete(Pengemudi kemudi) {
        kemudiDao.delete(kemudi);
    }

    public List<Pengemudi> kemudiRecord() {
        return kemudiDao.semuaData();
    }

    public List<Barang> findByKodeBarang(String kode) {
        return mstBrgDao.findByKodeBarang(kode);
    }
}
