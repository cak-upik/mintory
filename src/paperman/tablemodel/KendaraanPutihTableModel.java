/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.kendaraan;
import paperman.model.kendaraanPutih;

/**
 *
 * @author i1440ns
 */
public class KendaraanPutihTableModel extends AbstractTableModel {
    private List<kendaraanPutih> listKendaraanPutih;
    private String[] kolom = {"No. Lambung", "No. Polisi", "No. Mesin", "No. Rangka", "STNK", "Jatuh Tempo", "Keterangan"};
    private kendaraanPutih kendPutih;

    public KendaraanPutihTableModel(List<kendaraanPutih> listKendaraanPutih) {
        this.listKendaraanPutih = listKendaraanPutih;
    }

    public int getRowCount() {
        return listKendaraanPutih.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        kendaraanPutih k = listKendaraanPutih.get(row);
        switch(column) {
            case 0 : return k.getNoLambung();
            case 1 : return k.getNoPolisi();
            case 2 : return k.getNoMesin();
            case 3 : return k.getNoRangka();
            case 4 : return k.getNoSTNK();
            case 5 : return k.getTglJatuhTempo();
            case 6 : return k.getKeterangan();
            default : return new Object();
        }
    }

}
