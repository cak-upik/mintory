/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.ArrayList;
import java.util.List;
import javax.swing.table.AbstractTableModel;
import org.hibernate.Hibernate;
import mintory.model.SecurityUser;
import mintory.model.Barang;
import mintory.model.setoran;

/**
 *
 * @author i1440ns
 */
public class SecurityTableModel extends AbstractTableModel {
    private List<SecurityUser> listSecurityModel;
    private String[] kolom = {"Nama","Akses Menu","Akses Closing", "Akses Closed Transaksi", "Akses Ubah User", "Akses Komposisi Transaksi", "Akses Saldo Bonus", "Akses Data Sistem", "Akses Saldo Awal"};

    public SecurityTableModel(List<SecurityUser> listSecurity) {
        this.listSecurityModel = listSecurity;
    }

    public int getRowCount() {
        return listSecurityModel.size();
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }
    public int getColumnCount() {
        return kolom.length;
    }

    public Object getValueAt(int row, int column) {
        SecurityUser su = listSecurityModel.get(row);
        switch(column) {
            case 0 : return su.getNamaLgkp();
            case 1 : return su.getViewAllTransactionMenu();
            case 2 : return su.getDoClosing();
            case 3 : return su.getOpenClosedTrans();
            case 4 : return su.getAddUser();
            case 5 : return su.getChangeKomposisi();
            case 6 : return su.getChangeBonus();
            case 7 : return su.getChangeDataSistem();
            case 8 : return su.getDoSaldoAwal();
            default : return new Object();
        }
    }

    @Override
    public Class<?> getColumnClass(int columnIndex) {
        switch(columnIndex) {
            case 0: return String.class;
            case 1: return Boolean.class;
            case 2: return Boolean.class;
            case 3: return Boolean.class;
            case 4: return Boolean.class;
            case 5: return Boolean.class;
            case 6: return Boolean.class;
            case 7: return Boolean.class;
            case 8: return Boolean.class;
            default:return Object.class;
        }
    }
}
