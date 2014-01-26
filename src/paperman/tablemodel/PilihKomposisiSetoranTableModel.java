/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import paperman.model.komposisiSetoran;

/**
 *
 * @author Nurul Chusna
 */
public class PilihKomposisiSetoranTableModel extends AbstractTableModel {
    private List<komposisiSetoran> listKomposisiSetoran;
    private komposisiSetoran komposisiModel;
    private String[] kolom = {"Nama Komposisi","Nilai Angsuran","Nilai Tabungan"};

    public PilihKomposisiSetoranTableModel(List<komposisiSetoran> listKomposisiSetoran) {
        this.listKomposisiSetoran = listKomposisiSetoran;
    }

    public int getRowCount() {
        return listKomposisiSetoran.size();
    }

    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        komposisiSetoran ks = listKomposisiSetoran.get(rowIndex);
        switch(columnIndex) {
            case 0 : return ks.getNamaKomposisi();
            case 1 : return ks.getAngsuran();
            case 2 : return ks.getTabungan();
            default : return new Object();
        }
    }
}
