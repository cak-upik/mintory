/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package mintory.transaksi;

import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import javax.swing.JLabel;
import javax.swing.Renderer;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.TableCellRenderer;
import mintory.TextPrompt;

/**
 *
 * @author cak-upik
 */
public class TransaksiPemakaianUnit extends javax.swing.JInternalFrame {

    public static TransaksiPemakaianUnit transPemakaianUnit;

    public static void inisialisasi() {
        transPemakaianUnit = new TransaksiPemakaianUnit();
    }

    public static void destroy() {
        transPemakaianUnit.dispose();
        transPemakaianUnit = null;
    }

    public static TransaksiPemakaianUnit getTransaksiPemakaianUnit() {
        return transPemakaianUnit;
    }

    /**
     * Creates new form TransaksiPemakaianUnit
     */
    public TransaksiPemakaianUnit() {
        initComponents();
        initColumnBarang();
        initColumnUnit();
        TextPrompt hintstxtSearch = new TextPrompt("Pencarian Data Barang", txtSearchBarang);
        hintstxtSearch.changeAlpha(0.5f);
        hintstxtSearch.changeStyle(Font.ITALIC);
        TextPrompt hintstxtTrans = new TextPrompt("Masukkan No. Transaksi", txtNoTransaksi);
        hintstxtTrans.changeAlpha(0.5f);
        hintstxtTrans.changeStyle(Font.ITALIC);
        TextPrompt hintstxtPengemudi = new TextPrompt("Masukkan NRP Pengemudi", txtNRP);
        hintstxtPengemudi.changeAlpha(0.5f);
        hintstxtPengemudi.changeStyle(Font.ITALIC);
        TextPrompt hintstxtNama = new TextPrompt("Masukkan Nama Pengemudi", txtNama);
        hintstxtNama.changeAlpha(0.5f);
        hintstxtNama.changeStyle(Font.ITALIC);
        TextPrompt hintstxtNoLB = new TextPrompt("Masukkan No Lambung", txtNoLambung);
        hintstxtNoLB.changeAlpha(0.5f);
        hintstxtNoLB.changeStyle(Font.ITALIC);
        TextPrompt hintstxtNoNota = new TextPrompt("Masukkan No Nota", txtNota);
        hintstxtNoNota.changeAlpha(0.5f);
        hintstxtNoNota.changeStyle(Font.ITALIC);
     }
    
    private void initTableHeaderAlignment() {
        // -- Tabel Barang Renderer -- //
        TableCellRenderer brgRenderer = tblBarang.getTableHeader().getDefaultRenderer();
        JLabel labelBrg = (JLabel) brgRenderer;
        labelBrg.setHorizontalAlignment(JLabel.CENTER);
        
        // -- Tabel Item Renderer -- //
        TableCellRenderer itemRenderer = tblBarang.getTableHeader().getDefaultRenderer();
        JLabel labelItem = (JLabel) itemRenderer;
        labelItem.setHorizontalAlignment(JLabel.CENTER);
        
    }
    
    private void initButtonListener() {
        toolbarButtonTransaksi.getBtnTambah().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiTambah();
            }
        });
        toolbarButtonTransaksi.getBtnUbah().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiUbah();
            }
        });
        toolbarButtonTransaksi.getBtnSimpan().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiSimpan();
            }
        });
        toolbarButtonTransaksi.getBtnHapus().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiHapus();
            }
        });
        toolbarButtonTransaksi.getBtnCetak().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiCetak();
            }
        });
        toolbarButtonTransaksi.getBtnBatal().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiBatal();
            }
        });
        toolbarButtonTransaksi.getBtnKeluar().addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                aksiKeluar();
            }
        });
    }

    private void initColumnBarang() {
        tblBarang.getColumnModel().getColumn(0).setMinWidth(100);
        tblBarang.getColumnModel().getColumn(0).setMaxWidth(100);
        tblBarang.getColumnModel().getColumn(1).setMinWidth(150);
        tblBarang.getColumnModel().getColumn(1).setMaxWidth(200);
        tblBarang.getColumnModel().getColumn(2).setMinWidth(60);
        tblBarang.getColumnModel().getColumn(2).setMaxWidth(60);
        tblBarang.getColumnModel().getColumn(3).setMinWidth(130);
        tblBarang.getColumnModel().getColumn(3).setMaxWidth(130);
    }

    private void initColumnUnit() {
        tblItem.getColumnModel().getColumn(0).setMinWidth(100);
        tblItem.getColumnModel().getColumn(0).setMaxWidth(100);
        tblItem.getColumnModel().getColumn(1).setMinWidth(150);
        tblItem.getColumnModel().getColumn(1).setMaxWidth(200);
        tblItem.getColumnModel().getColumn(2).setMinWidth(65);
        tblItem.getColumnModel().getColumn(2).setMaxWidth(65);
        tblItem.getColumnModel().getColumn(3).setMinWidth(40);
        tblItem.getColumnModel().getColumn(3).setMaxWidth(40);
        tblItem.getColumnModel().getColumn(4).setMinWidth(100);
        tblItem.getColumnModel().getColumn(4).setMaxWidth(100);
        tblItem.getColumnModel().getColumn(5).setMinWidth(100);
        tblItem.getColumnModel().getColumn(5).setMaxWidth(100);
    }
    
    private void aksiTambah() {
        enableForm(true);
        houseKeeping();
        txtNoTransaksi.requestFocus();
        toolbarButtonTransaksi.editMode();
    }
    private void aksiUbah() {
        enableForm(true);
        txtNoTransaksi.requestFocus();
        toolbarButtonTransaksi.editMode();
    }
    private void aksiSimpan() {}
    private void aksiHapus() {}
    private void aksiCetak() {}
    private void aksiBatal() {}
    private void aksiKeluar() {}
    private void aksiCari() {}
    
    private void enableForm(boolean e){}
    private void houseKeeping(){}
    private void LoadDataToBarangTable() {}
    private void LoadDataToUnitTable() {}
    private void LoadFormToDatabase() {}
    private void LoadDatabaseToForm() {}
    private boolean validateForm() {
        return false;
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        buttonGroup1 = new javax.swing.ButtonGroup();
        jSplitPane1 = new javax.swing.JSplitPane();
        toolbarButtonTransaksi = new mintory.transaksi.toolbarButtonTransaksi();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel2 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        radioInventory = new javax.swing.JRadioButton();
        radioMaintenance = new javax.swing.JRadioButton();
        txtNoTransaksi = new javax.swing.JTextField();
        txtNRP = new javax.swing.JTextField();
        txtNoLambung = new javax.swing.JTextField();
        txtNota = new javax.swing.JTextField();
        datePemakaian = new com.toedter.calendar.JDateChooser();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        btnSearchNRP = new javax.swing.JButton();
        jSeparator1 = new javax.swing.JSeparator();
        jSeparator2 = new javax.swing.JSeparator();
        jPanel3 = new javax.swing.JPanel();
        jLabel6 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        txtNama = new javax.swing.JTextField();
        jSplitPane3 = new javax.swing.JSplitPane();
        jPanel4 = new javax.swing.JPanel();
        jScrollPane4 = new javax.swing.JScrollPane();
        tblBarang = new javax.swing.JTable();
        txtSearchBarang = new javax.swing.JTextField();
        jPanel5 = new javax.swing.JPanel();
        jScrollPane5 = new javax.swing.JScrollPane();
        tblItem = new javax.swing.JTable();
        jButton1 = new javax.swing.JButton();
        jButton2 = new javax.swing.JButton();

        setIconifiable(true);
        setMaximizable(true);
        setTitle("Transaksi Pemakaian SparePart");

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        toolbarButtonTransaksi.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED), javax.swing.BorderFactory.createEtchedBorder()));
        jSplitPane1.setLeftComponent(toolbarButtonTransaksi);

        jSplitPane2.setDividerLocation(215);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jPanel1.setBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED));

        jLabel1.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel1.setText("No. Transaksi");

        jLabel2.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel2.setText("NRP Pengemudi");

        jLabel3.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel3.setText("No. Lambung");

        jLabel4.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel4.setText("No. Nota");

        jLabel5.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel5.setText("Tanggal Pemakaian");

        jLabel7.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel7.setText("Keterangan");

        jPanel2.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Status", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("DejaVu Sans", 1, 12))); // NOI18N

        buttonGroup1.add(radioInventory);
        radioInventory.setText("Inventory       ");
        radioInventory.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                radioInventoryActionPerformed(evt);
            }
        });

        buttonGroup1.add(radioMaintenance);
        radioMaintenance.setText("Maintenance");

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(radioMaintenance)
                .addGap(18, 18, 18)
                .addComponent(radioInventory)
                .addContainerGap(110, Short.MAX_VALUE))
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {radioInventory, radioMaintenance});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                .addComponent(radioInventory)
                .addComponent(radioMaintenance))
        );

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane3.setViewportView(txtKeterangan);

        btnSearchNRP.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintory/images/mnuFind.png"))); // NOI18N

        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jSeparator2.setOrientation(javax.swing.SwingConstants.VERTICAL);

        jPanel3.setBackground(new java.awt.Color(0, 132, 255));
        jPanel3.setBorder(javax.swing.BorderFactory.createCompoundBorder(javax.swing.BorderFactory.createEtchedBorder(), javax.swing.BorderFactory.createBevelBorder(javax.swing.border.BevelBorder.RAISED)));

        jLabel6.setFont(new java.awt.Font("DejaVu Sans", 1, 14)); // NOI18N
        jLabel6.setForeground(new java.awt.Color(254, 254, 254));
        jLabel6.setText("Data Detail Pemakaian SparePart");

        javax.swing.GroupLayout jPanel3Layout = new javax.swing.GroupLayout(jPanel3);
        jPanel3.setLayout(jPanel3Layout);
        jPanel3Layout.setHorizontalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel3Layout.createSequentialGroup()
                .addGap(339, 339, 339)
                .addComponent(jLabel6)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );
        jPanel3Layout.setVerticalGroup(
            jPanel3Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel6, javax.swing.GroupLayout.DEFAULT_SIZE, 27, Short.MAX_VALUE)
        );

        jLabel8.setFont(new java.awt.Font("DejaVu Sans", 1, 12)); // NOI18N
        jLabel8.setText("Nama Pengemudi");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel8)
                    .addComponent(jLabel1)
                    .addComponent(jLabel2)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(txtNoTransaksi)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addComponent(txtNRP, javax.swing.GroupLayout.DEFAULT_SIZE, 246, Short.MAX_VALUE)
                        .addGap(7, 7, 7)
                        .addComponent(btnSearchNRP, javax.swing.GroupLayout.PREFERRED_SIZE, 39, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addComponent(txtNama)
                    .addComponent(txtNoLambung)
                    .addComponent(txtNota))
                .addGap(26, 26, 26)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 14, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 15, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jLabel7, javax.swing.GroupLayout.PREFERRED_SIZE, 122, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 20, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(datePemakaian, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE)))
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 0, Short.MAX_VALUE)
                        .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                .addGap(22, 22, 22))
            .addComponent(jPanel3, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );
        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel3, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel1Layout.createSequentialGroup()
                        .addGap(0, 6, Short.MAX_VALUE)
                        .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addGroup(jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel1)
                                    .addComponent(txtNoTransaksi, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(btnSearchNRP)
                                    .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                        .addComponent(jLabel2)
                                        .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(jLabel8)
                                    .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel3))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                                    .addComponent(txtNota, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel4)))
                            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                                    .addComponent(datePemakaian, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                    .addComponent(jLabel5, javax.swing.GroupLayout.PREFERRED_SIZE, 25, javax.swing.GroupLayout.PREFERRED_SIZE))
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addComponent(jPanel2, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                                    .addComponent(jLabel7)
                                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 65, javax.swing.GroupLayout.PREFERRED_SIZE))))
                        .addContainerGap())
                    .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jSeparator2, javax.swing.GroupLayout.PREFERRED_SIZE, 165, javax.swing.GroupLayout.PREFERRED_SIZE)))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jSeparator1, jSeparator2});

        jSplitPane2.setLeftComponent(jPanel1);

        jSplitPane3.setDividerLocation(440);
        jSplitPane3.setDividerSize(0);

        jPanel4.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Persediaan Barang", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        tblBarang.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Barang", "Nama Barang", "Stock", "Harga"
            }
        ));
        tblBarang.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblBarang.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane4.setViewportView(tblBarang);
        if (tblBarang.getColumnModel().getColumnCount() > 0) {
            tblBarang.getColumnModel().getColumn(0).setMinWidth(100);
            tblBarang.getColumnModel().getColumn(0).setMaxWidth(100);
            tblBarang.getColumnModel().getColumn(1).setMinWidth(150);
            tblBarang.getColumnModel().getColumn(1).setMaxWidth(200);
            tblBarang.getColumnModel().getColumn(2).setMinWidth(60);
            tblBarang.getColumnModel().getColumn(2).setMaxWidth(60);
            tblBarang.getColumnModel().getColumn(3).setMinWidth(130);
            tblBarang.getColumnModel().getColumn(3).setMaxWidth(130);
        }

        txtSearchBarang.setToolTipText("Masukkan Pencarian Barang");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 428, Short.MAX_VALUE)
            .addComponent(txtSearchBarang, javax.swing.GroupLayout.Alignment.TRAILING)
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(txtSearchBarang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane4, javax.swing.GroupLayout.DEFAULT_SIZE, 195, Short.MAX_VALUE))
        );

        jSplitPane3.setLeftComponent(jPanel4);

        jScrollPane5.setBorder(javax.swing.BorderFactory.createTitledBorder(null, "Data Pemakaian Item", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Arial", 1, 12))); // NOI18N

        tblItem.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Kode Barang", "Nama Barang", "Satuan", "Qty", "SubTotal", "Total"
            }
        ));
        tblItem.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblItem.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane5.setViewportView(tblItem);
        if (tblItem.getColumnModel().getColumnCount() > 0) {
            tblItem.getColumnModel().getColumn(0).setMinWidth(100);
            tblItem.getColumnModel().getColumn(0).setMaxWidth(100);
            tblItem.getColumnModel().getColumn(1).setMinWidth(150);
            tblItem.getColumnModel().getColumn(1).setMaxWidth(200);
            tblItem.getColumnModel().getColumn(2).setMinWidth(65);
            tblItem.getColumnModel().getColumn(2).setMaxWidth(65);
            tblItem.getColumnModel().getColumn(3).setMinWidth(40);
            tblItem.getColumnModel().getColumn(3).setMaxWidth(40);
            tblItem.getColumnModel().getColumn(4).setMinWidth(100);
            tblItem.getColumnModel().getColumn(4).setMaxWidth(100);
            tblItem.getColumnModel().getColumn(5).setMinWidth(100);
            tblItem.getColumnModel().getColumn(5).setMaxWidth(100);
        }

        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintory/images/PHNEXT.png"))); // NOI18N

        jButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/mintory/images/PHBACK.png"))); // NOI18N

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGroup(jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(jButton2, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(jButton1, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                .addGap(0, 0, 0)
                .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 442, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jScrollPane5, javax.swing.GroupLayout.DEFAULT_SIZE, 249, Short.MAX_VALUE)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addGap(64, 64, 64)
                .addComponent(jButton1, javax.swing.GroupLayout.PREFERRED_SIZE, 70, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jButton2, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        jPanel5Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {jButton1, jButton2});

        jSplitPane3.setRightComponent(jPanel5);

        jSplitPane2.setBottomComponent(jSplitPane3);

        jSplitPane1.setBottomComponent(jSplitPane2);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void radioInventoryActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_radioInventoryActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_radioInventoryActionPerformed


    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnSearchNRP;
    private javax.swing.ButtonGroup buttonGroup1;
    private com.toedter.calendar.JDateChooser datePemakaian;
    private javax.swing.JButton jButton1;
    private javax.swing.JButton jButton2;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel3;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JScrollPane jScrollPane4;
    private javax.swing.JScrollPane jScrollPane5;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSeparator jSeparator2;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JSplitPane jSplitPane3;
    private javax.swing.JRadioButton radioInventory;
    private javax.swing.JRadioButton radioMaintenance;
    private javax.swing.JTable tblBarang;
    private javax.swing.JTable tblItem;
    private mintory.transaksi.toolbarButtonTransaksi toolbarButtonTransaksi;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtNRP;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoLambung;
    private javax.swing.JTextField txtNoTransaksi;
    private javax.swing.JTextField txtNota;
    private javax.swing.JTextField txtSearchBarang;
    // End of variables declaration//GEN-END:variables
}
