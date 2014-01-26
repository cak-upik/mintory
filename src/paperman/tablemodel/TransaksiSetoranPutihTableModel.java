/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.setoranDetailPutih;
import paperman.model.setoranPutih;

/**
 *
 * @author i1440ns
 */
public class TransaksiSetoranPutihTableModel extends AbstractTableModel{
    private List<setoranDetailPutih> listSetoranDetailPutih = new ArrayList<setoranDetailPutih>();
    private String[] header = {"Kode Setoran", "Setoran Ke", "No. Lambung", "Tgl. Jatuh Tempo","Tgl. SPO", "Total Setoran", "Total Hutang"};
    private setoranPutih sto;
    private DecimalFormat totalFormatter = new DecimalFormat("Rp ###,###.##");

    public TransaksiSetoranPutihTableModel(List<setoranDetailPutih> listSetoranDetailPutih) {
        this.listSetoranDetailPutih = listSetoranDetailPutih;
    }

    public int getRowCount() {
        return listSetoranDetailPutih.size();
    }

    @Override
    public String getColumnName(int column) {
        return header[column];
    }

    public int getColumnCount() {
        return header.length;
    }

    public Object getValueAt(int row, int column) {
        setoranDetailPutih std = listSetoranDetailPutih.get(row);
        switch(column) {
            case 0 : return std.getSetor_map_putih().getKode();
            case 1 : return std.getSetor_map_putih().getCounter_setoran();
            case 2 : return std.getKemudiPutih().getKendPutih().getNoLambung();
            case 3 : return std.getSetor_map_putih().getTglJatuhTempo();
            case 4 : return std.getSetor_map_putih().getTglSPO();
            case 5 : return totalFormatter.format(std.getSetor_map_putih().getTotalSetoran());
            case 6 : return totalFormatter.format(std.getSetor_map_putih().getTotalHutang());
            default : return new Object();
        }
    }
}
