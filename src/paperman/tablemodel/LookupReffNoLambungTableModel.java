/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.kendaraan;
import paperman.model.pengemudi;
import paperman.model.setoranDetail;

/**
 *
 * @author i1440ns
 */
public class LookupReffNoLambungTableModel extends AbstractTableModel {
    private List<setoranDetail> listSetoranDetail;
    private String[] kolom = {"No. Lambung", "NRP", "Nama", "Tgl. Jatuh Tempo", "Keterangan"};
    private setoranDetail stoDet;

    public LookupReffNoLambungTableModel(List<setoranDetail> listSetoranDetail) {
        this.listSetoranDetail= listSetoranDetail;
    }

    public int getRowCount() {
        return listSetoranDetail.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        setoranDetail d = listSetoranDetail.get(row);
        switch(column) {
            case 0 : return d.getKend().getNoLambung();
            case 1 : return d.getKemudi().getNrp();
            case 2 : return d.getKemudi().getNama();
            case 3 : return d.getKend().getTglJatuhTempo();
            case 4 : return d.getKend().getKeterangan();
            default : return new Object();
        }
    }
}
