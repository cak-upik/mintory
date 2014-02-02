/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.pengemudi;

/**
 *
 * @author i1440ns
 */
public class PengemudiTableModel extends AbstractTableModel {
    private List<pengemudi> listPengemudi;
    private String[] kolom = {"Lambung","NRP", "Nama", "Alamat", "Kota", "Tgl Masuk", "Masa Kontrak"};
    private pengemudi kemudi;

    public PengemudiTableModel(List<pengemudi> listPengemudi) {
        this.listPengemudi = listPengemudi;
    }

    public int getRowCount() {
        return listPengemudi.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        pengemudi p = listPengemudi.get(row);
        switch(column) {
            case 0 : return p.getKend().getNoLambung();
            case 1 : return p.getNrp();
            case 2 : return p.getNama();
            case 3 : return p.getAlamat();
            case 4 : return p.getKota();
            case 5 : return p.getTglMasuk();
            case 6 : return p.getKontrakHari() + " Hari" + p.getKontrakBulan() + " Bulan";
            default: return new Object();
        }
    }
}
