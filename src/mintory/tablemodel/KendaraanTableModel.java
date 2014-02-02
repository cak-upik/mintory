/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.kendaraan;

/**
 *
 * @author i1440ns
 */
public class KendaraanTableModel extends AbstractTableModel {
    private List<kendaraan> listKendaraan;
    private String[] kolom = {"No. Lambung", "No. Polisi", "No. Mesin", "No. Rangka", "STNK", "Jatuh Tempo", "Keterangan"};
    private kendaraan kend;

    public KendaraanTableModel(List<kendaraan> listKendaraan) {
        this.listKendaraan = listKendaraan;
    }

    public int getRowCount() {
        return listKendaraan.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        kendaraan k = listKendaraan.get(row);
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
