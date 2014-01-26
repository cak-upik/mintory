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
public class KomposisiSetoranTableModel extends AbstractTableModel {
    private List<komposisiSetoran> listKomposisiSetoran;
    private komposisiSetoran komposisiModel;
    private String[] kolom = {"Default","Nama Komposisi","Nilai Angsuran","Nilai Tabungan"};

    public KomposisiSetoranTableModel(List<komposisiSetoran> listKomposisiSetoran) {
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
            case 0 : return ks.getStatus();
            case 1 : return ks.getNamaKomposisi();
            case 2 : return ks.getAngsuran();
            case 3 : return ks.getTabungan();
            default : return new Object();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0 : return Boolean.class;
            case 1 : return String.class;
            case 2 : return String.class;
            case 3 : return String.class;
            default: return Object.class;
        }
    }
}
