/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package mintory.tablemodel;

import java.util.List;
import javax.swing.table.AbstractTableModel;
import mintory.model.codeGenerator;

/**
 *
 * @author Nurul Chusna
 */
public class RunningNumberTableModel extends AbstractTableModel {
    private List<codeGenerator> listCode;
    private codeGenerator genCode;
    private String[] kolom = {"ID","Jenis Transaksi","Jenis Armada","No Lambung","Tgl Sistem"};

    public RunningNumberTableModel(List<codeGenerator> listCode) {
        this.listCode = listCode;
    }

    public int getRowCount() {
        return listCode.size();
    }

    public int getColumnCount() {
        return kolom.length;
    }

    @Override
    public String getColumnName(int column) {
        return kolom[column];
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
        codeGenerator cg = listCode.get(rowIndex);
        switch(columnIndex) {
            case 0 : return cg.getId();
            case 1 : return cg.getTagTransaction();
            case 2 : return cg.getTagArmada();
            case 3 : return cg.getLastnum();
            case 4 : return cg.getTanggal();
            default : return new Object();
        }
    }
}
