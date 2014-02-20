/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.service;

import java.util.List;
import mintory.model.Barang;
import mintory.model.kendaraanPutih;
import mintory.model.Supplier;
import mintory.model.Pengemudi;

/**
 *
 * @author i1440ns
 */
public interface MasterService {
    public void save(Barang kend);
    public void delete(Barang kend);
    public List<Barang> barangRecord();
    public List<Barang> findByKodeBarang(String kode);

    public void save(Supplier supply);
    public void save(Pengemudi kemudi);
    public void delete(Supplier supply);
    public void delete(Pengemudi kemudi);
    public List<Supplier> supplierRecord();
    public List<Pengemudi> kemudiRecord();
}
