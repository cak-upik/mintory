/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.Pengemudi;

/**
 *
 * @author i1440ns
 */
public class PengemudiTableModel extends AbstractTableModel {
    private List<Pengemudi> listPengemudi;
    private String[] kolom = {"NRP","No Lambung", "Nama", "Alamat", "Kota","Keterangan"};
    private Pengemudi kemudi;

    public PengemudiTableModel(List<Pengemudi> listPengemudis) {
        this.listPengemudi = listPengemudis;
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
        Pengemudi p = listPengemudi.get(row);
        switch(column) {
            case 0 : return p.getNrp();
            case 1 : return p.getNoLB();
            case 2 : return p.getNama();
            case 3 : return p.getAlamat();
            case 4 : return p.getKota();
            case 5 : return p.getKeterangan();
            default: return new Object();
        }
    }
}
