/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.Supplier;

/**
 *
 * @author i1440ns
 */
public class SupplierTableModel extends AbstractTableModel {
    private List<Supplier> listSupplier;
    private String[] kolom = {"Kode Supplier","Nama Supplier", "Alamat", "No.Telepon"};
    private Supplier supply;

    public SupplierTableModel(List<Supplier> listSuppliers) {
        this.listSupplier = listSuppliers;
    }

    public int getRowCount() {
        return listSupplier.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        Supplier sp = listSupplier.get(row);
        switch(column) {
            case 0 : return sp.getKodeSupplier();
            case 1 : return sp.getNamaSupplier();
            case 2 : return sp.getAlamat();
            case 3 : return sp.getTelp();
            default: return new Object();
        }
    }
}
