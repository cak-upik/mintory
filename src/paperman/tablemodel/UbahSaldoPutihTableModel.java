/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.closingBulanan;
import paperman.model.closingBulananPutih;

/**
 *
 * @author Nurul Chusna
 */
public class UbahSaldoPutihTableModel extends AbstractTableModel {
    private List<closingBulananPutih> listClbPth;
    private String[] kolom = {"Jenis Armada","No Lambung","Setoran Ke","Total Angsuran","Total Tabungan","Total Kasbon", "Total Bayar Kas", "Total Overtime", "Total Cicilan", "Total Setoran"};

    public UbahSaldoPutihTableModel(List<closingBulananPutih> listClbPth) {
        this.listClbPth = listClbPth;
    }

    public int getRowCount() {
        return listClbPth.size();
    }

    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        closingBulananPutih clbPth = listClbPth.get(rowIndex);
        switch(columnIndex) {
            case 0 : return clbPth.getIdKomposisiRefSaldoAwal().getNamaKomposisi();
            case 1 : return clbPth.getRefNoLambung();
            case 2 : return clbPth.getRefSetoranKe();
            case 3 : return clbPth.getTotalAngsuran();
            case 4 : return clbPth.getTotalTabungan();
            case 5 : return clbPth.getTotalKas();
            case 6 : return clbPth.getTotalBayarKas();
            case 7 : return clbPth.getTotalOvertime();
            case 8 : return clbPth.getTotalCicilan();
            case 9 : return clbPth.getTotalSetor();
            default : return new Object();
        }
    }
}
