/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.pengemudiPutih;

/**
 *
 * @author i1440ns
 */
public class PengemudPutihiTableModel extends AbstractTableModel {
    private List<pengemudiPutih> listPengemudiPutih;
    private String[] kolom = {"Lambung","NRP", "Nama", "Alamat", "Kota", "Tgl Masuk", "Masa Kontrak"};
    private pengemudiPutih kemudiPutih;

    public PengemudPutihiTableModel(List<pengemudiPutih> listPengemudiPutih) {
        this.listPengemudiPutih = listPengemudiPutih;
    }

    public int getRowCount() {
        return listPengemudiPutih.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        pengemudiPutih p = listPengemudiPutih.get(row);
        switch(column) {
            case 0 : return p.getKendPutih().getNoLambung();
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
