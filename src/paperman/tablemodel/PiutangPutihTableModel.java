/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.setoranPutih;

/**
 *
 * @author i1440ns
 */
public class PiutangPutihTableModel extends AbstractTableModel {
    private List<setoranPutih> listSetoranPutih;
    private String[] kolom = {"No. Lambung","NRP","Nama", "Total Setoran", "Total Hutang", "Set.Ke", "Tgl Jatuh Tempo", "Tgl SPO"};

    public PiutangPutihTableModel(List<setoranPutih> listSetoranPutih) {
        this.listSetoranPutih = listSetoranPutih;
    }

    public int getRowCount() {
        return listSetoranPutih.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        setoranPutih stPth = listSetoranPutih.get(row);
        switch(column) {
            case 0 : return stPth.getDetailPutih().get(0).getKemudiPutih().getKendPutih().getNoLambung();
            case 1 : return stPth.getDetailPutih().get(0).getKemudiPutih().getNrp();
            case 2 : return stPth.getDetailPutih().get(0).getKemudiPutih().getNama();
            case 3 : return stPth.getTotalSetoran();
            case 4 : return stPth.getTotalHutang();
            case 5 : return stPth.getCounter_setoran();
            case 6 : return stPth.getTglJatuhTempo();
            case 7 : return stPth.getTglSPO();
            default : return new Object();
        }
    }
}
