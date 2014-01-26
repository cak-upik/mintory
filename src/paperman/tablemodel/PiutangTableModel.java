/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.setoran;

/**
 *
 * @author i1440ns
 */
public class PiutangTableModel extends AbstractTableModel {
    private List<setoran> listSetoran;
    private String[] kolom = {"No. Lambung","NRP","Nama", "Total Setoran", "Total Hutang", "Set.Ke", "Tgl Jatuh Tempo", "Tgl SPO"};

    public PiutangTableModel(List<setoran> listSetoran) {
        this.listSetoran = listSetoran;
    }

    public int getRowCount() {
        return listSetoran.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        setoran st = listSetoran.get(row);
        switch(column) {
            case 0 : return st.getDetail().get(0).getKemudi().getKend().getNoLambung();
            case 1 : return st.getDetail().get(0).getKemudi().getNrp();
            case 2 : return st.getDetail().get(0).getKemudi().getNama();
            case 3 : return st.getTotalSetoran();
            case 4 : return st.getTotalHutang();
            case 5 : return st.getCounter_setoran();
            case 6 : return st.getTglJatuhTempo();
            case 7 : return st.getTglSPO();
            default : return new Object();
        }
    }

}
