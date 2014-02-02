/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.closingBulanan;

/**
 *
 * @author Nurul Chusna
 */
public class UbahSaldoTableModel extends AbstractTableModel {
    private List<closingBulanan> listClb;
    private String[] kolom = {"Jenis Armada","No Lambung","Setoran Ke","Total Angsuran","Total Tabungan","Total Kasbon", "Total Bayar Kas", "Total Overtime", "Total Cicilan", "Total Setoran"};

    public UbahSaldoTableModel(List<closingBulanan> listClb) {
        this.listClb = listClb;
    }

    public int getRowCount() {
        return listClb.size();
    }

    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        closingBulanan clb = listClb.get(rowIndex);
        switch(columnIndex) {
            case 0 : return clb.getIdKomposisiRefSaldoAwal().getNamaKomposisi();
            case 1 : return clb.getRefNoLambung();
            case 2 : return clb.getRefSetoranKe();
            case 3 : return clb.getTotalAngsuran();
            case 4 : return clb.getTotalTabungan();
            case 5 : return clb.getTotalKas();
            case 6 : return clb.getTotalBayarKas();
            case 7 : return clb.getTotalOvertime();
            case 8 : return clb.getTotalCicilan();
            case 9 : return clb.getTotalSetor();
            default : return new Object();
        }
    }
}
