/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.setoranDetail;

/**
 *
 * @author i1440ns
 */
public class TransaksiSetoranTableModel extends AbstractTableModel{
    private List<setoranDetail> listSetoranDetail = new ArrayList<setoranDetail>();
    private String[] header = {"Kode Setoran", "Setoran Ke", "No. Lambung", "Tgl. Jatuh Tempo", "Tgl. SPO", "Total Setoran", "Total Hutang"};
    private DecimalFormat totalFormatter = new DecimalFormat("Rp ###,###.##");

//    public TransaksiSetoranTableModel(List<setoran> listSetoran, List<setoranDetail> listSetoranDetail) {
//        this.listSetoran = listSetoran;
//        this.listSetoranDetail = listSetoranDetail;
//    }

    public TransaksiSetoranTableModel(List<setoranDetail> listSetoranDetail) {
//        this.listSetoran = listSetoran;
        this.listSetoranDetail = listSetoranDetail;
    }

    public int getRowCount() {
        return listSetoranDetail.size();
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    public int getColumnCount() {
        return header.length;
    }



    public Object getValueAt(int row, int column) {
        setoranDetail std = listSetoranDetail.get(row);
        switch(column) {
            case 0 : return std.getSetor_map().getKode();
            case 1 : return std.getSetor_map().getCounter_setoran();
//            case 2 : return std.getKemudi().getKend().getNoLambung();
            case 3 : return std.getSetor_map().getTglJatuhTempo();
            case 4 : return std.getSetor_map().getTglSPO();
            case 5 : return totalFormatter.format(std.getSetor_map().getTotalSetoran());
            case 6 : return totalFormatter.format(std.getSetor_map().getTotalHutang());
            default : return new Object();
        }
    }
}
