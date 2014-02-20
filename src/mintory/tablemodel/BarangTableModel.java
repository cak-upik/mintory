/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.Barang;

/**
 *
 * @author i1440ns
 */
public class BarangTableModel extends AbstractTableModel {
    private List<Barang> listBarang;
    private String[] kolom = {"Kode Barang", "Nama Barang", "Satuan", "Tgl Masuk", "Keterangan"};
    private Barang barang;

    public BarangTableModel(List<Barang> listBarangs) {
        this.listBarang = listBarangs;
    }

    public int getRowCount() {
        return listBarang.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        Barang k = listBarang.get(row);
        switch(column) {
            case 0 : return k.getKodeBarang();
            case 1 : return k.getNamaBarang();
            case 2 : return k.getSatuan();
            case 3 : return k.getTglMasuk();
            case 4 : return k.getKeterangan();
            default : return new Object();
        }
    }

}
