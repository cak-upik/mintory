/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/*
 * TransaksiSetoran.java
 *
 * Created on Apr 28, 2012, 3:15:51 PM
 */

/*
KONDISI STANDARD OPERASI PEMBAYARAN SETORAN
1. [PELUNASAN FULL] (Setoran + Hutang)
=> Kursor di posisikan pada kasbon, di isi dengan '0', kemudian Enter
2. [PELUNASAN HUTANG SEPARUH] (TANPA SETORAN HARI INI) Hutang/ kasbon dilunasi, setoran hari ini tidak ada
=> kursor di posisikan pada cicilan, pembayaran kasbon di isi total angsuran hari ini, enter
3. [TIDAK SETOR]
=> Kursor di posisikan pada cicilan, di isi '0', enter
4. [SURPLUS, MENABUNG] (Tidak ada hutang + jumlah setoran lebih) jumlah pembayaran yang dimasukkan melebihi total setor
=> kusor di posisikan pada cicilan, pembayaran kasbon di isi, cicilan di isi, enter
5. [MANUAL] Total seluruh elemen kasbon (bayar kas + overtime + cicilan) , nominal hutang di isi manual
=> kursor di posisikan pada cicilan, CTRL + Enter
=> Hutang di isi manual, pada kolom hutang hari ini, field hutang di klik 2x, edit isinya, enter
 */
package mintory.transaksi;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.KeyEventDispatcher;
import java.awt.KeyboardFocusManager;
import java.awt.TextArea;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.swing.JDialog;
import javax.swing.JOptionPane;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;
import net.sf.jasperreports.engine.JasperPrint;
import net.sf.jasperreports.view.JasperViewer;
import org.hibernate.JDBCException;
import org.joda.time.DateTime;
import org.springframework.transaction.TransactionException;
import mintory.Main;
import mintory.OsUtils;
import mintory.PapermanFocusComponentUtils;
import mintory.TextComponentUtils;
import mintory.dialog.OpenClosedTrans;
import mintory.dialog.PilihKomposisi;
import mintory.model.JalanStatus;
import mintory.model.TransaksiStatus;
import mintory.model.aksiClosing;
import mintory.model.closingBulanan;
import mintory.model.codeGenerator;
import mintory.model.isClosedFor;
import mintory.model.isClosedStatus;
import mintory.model.kendaraan;
import mintory.model.komposisiSetoran;
import mintory.model.pengemudi;
import mintory.model.setoran;
import mintory.model.setoranDetail;
import mintory.model.sistem;
import mintory.tablemodel.TransaksiSetoranTableModel;

/**
 *
 * @author i1440ns
 */
public class TransaksiSetoran extends javax.swing.JInternalFrame implements ListSelectionListener {

    private static TransaksiSetoran transaksiSetoran;
    private List<setoran> listSetoran = new ArrayList<setoran>();
    private List<setoran> listSetoranForUpdate = new ArrayList<setoran>();
    private List<setoranDetail> listSetoranDetail = new ArrayList<setoranDetail>();
    private List<setoranDetail> ListStoDetail = new ArrayList<setoranDetail>();
    private List<kendaraan> listKendaraan = new ArrayList<kendaraan>();
    private List<pengemudi> listPengemudi = new ArrayList<pengemudi>();
    private List<sistem> listSistem;
    private List<closingBulanan> listClosingBulananSaldoAwal;
    private closingBulanan refNoLambung;
    private kendaraan kend;
    private pengemudi drive;
    private sistem sys;
    private setoran sto;
    private setoranDetail stoDet;
    private setoranDetail stoDetUpdate;
    private List<codeGenerator> generateCode;
    private codeGenerator genCode;
    private komposisiSetoran komposisisetoran;
    private List<komposisiSetoran> listKomposisi;
    private static final Integer option = JOptionPane.OK_CANCEL_OPTION;
    private int setoranCounter = 0;
    private int setoranPrevious = 0;
    private DecimalFormat rupiahFormatter = new DecimalFormat("Rp ###,###.00");
    private DecimalFormat totalFormatter = new DecimalFormat("###.00");
    private static final int MIN_FONT_SIZE = 3;
    private static final int MAX_FONT_SIZE = 3;
    private BigDecimal total = new BigDecimal(0);
    private BigDecimal hutang = new BigDecimal(0);
    private BigDecimal sumSetoran = new BigDecimal(0);
    private BigDecimal hutangToday = new BigDecimal(0);
    private SimpleDateFormat formatDate = new SimpleDateFormat("ddMMyy");
    private Date current_date;
    private boolean isEditFire = false;
    private boolean foundKomposisi = false;
    private boolean isChangedPages = false;
    private boolean isPrevPagePressed = false;
    private boolean isNextPagePressed = false;
    private boolean isBonusHooray = false;
    private int maxDay = 0;
    private int currentDay = 0;
    private int offset = 0;
    private int limit = 0;
    private int totalPage = 0;
    private int currentPos = 0;
    private int page = 0;
    private int lastPos = 0;
    Dimension ukuranFrame;
    Dimension ukuranLayar;

    public static void inisialisasi() {
        transaksiSetoran = new TransaksiSetoran();
    }

    public static void destroy() {
        transaksiSetoran.dispose();
        transaksiSetoran = null;
        System.gc();
    }

    public static TransaksiSetoran getTransaksiSetoran() {
        return transaksiSetoran;
    }

    /** Creates new form TransaksiSetoran */
    public TransaksiSetoran() {
        initComponents();
        initButtonListener();
        initButtonHotkeyFunction();
        initComponentFocusOrder();
        sys = Main.getSistemService().sistemRecord();
        listKomposisi = Main.getSistemService().komposisiRecord();
        maxDay = new DateTime(sys.getTglKerja()).dayOfMonth().getMaximumValue();
        currentDay = new DateTime(sys.getTglKerja()).getDayOfMonth();
        if (currentDay == maxDay || currentDay == maxDay - 1) {
            if (Main.getSistemService().findBonusBulanan() == null) {
                JOptionPane.showMessageDialog(this, "Tanggal Hari Ini Menggunakan Komposisi Bonus Bulanan\nKomposisi Bonus Bulanan Kosong, Periksa Lagi", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                InputBonusBulanan.inisialisasi();
                Main.getMainMenu().getDesktopPane().add(InputBonusBulanan.getFormInputBonusBulanan());
                ukuranLayar = Toolkit.getDefaultToolkit().getScreenSize();
                ukuranFrame = InputBonusBulanan.getFormInputBonusBulanan().getSize();
                InputBonusBulanan.getFormInputBonusBulanan().setLocation((ukuranLayar.width - ukuranFrame.width) / 2, (ukuranLayar.height - ukuranFrame.height) / 4);
                InputBonusBulanan.getFormInputBonusBulanan().setVisible(true);
                aksiKeluar();
            } else {
                JOptionPane.showMessageDialog(this, "Tanggal Hari Ini Masuk Komposisi BONUS BULANAN\nSegala Jenis Transaksi Yang Bukan Termasuk Komposisi Bonus Tidak Ditampilkan\nTekan F12 Untuk Melihat Atau Dari Laporan");
                komposisisetoran = new PilihKomposisi().showDialogDataBonus();
                isBonusHooray = true;
            }
        } else {
            for (komposisiSetoran ks : listKomposisi) {
                if (ks.getStatus().booleanValue() == Boolean.TRUE) {
                    komposisisetoran = ks;
                    foundKomposisi = true;
                    isBonusHooray = false;
                    break;
                }
            }
            if (!foundKomposisi) {
                komposisisetoran = new PilihKomposisi().showDialog();
                isBonusHooray = false;
            }
        }
        listPengemudi = Main.getMasterService().kemudiRecord();
//        listClosingBulananSaldoAwal = Main.getTransaksiService().closingBulananRecordForSaldoAwal(aksiClosing.DO_CLOSING_SALDO_AWAL);
        listClosingBulananSaldoAwal = Main.getTransaksiService().closingBulananRecordForSaldoAwal(komposisisetoran, sys.getTglKerja());
        if (!listClosingBulananSaldoAwal.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Input Saldo Awal Telah Dilakukan\nTotal Hutang Akan Otomatis Di Sinkronisasi Dengan Kartu Pembayaran", "Pesan Sistem", JOptionPane.INFORMATION_MESSAGE);
        }else {
            listClosingBulananSaldoAwal = Main.getTransaksiService().closingBulananInCustomRange(new DateTime(sys.getTglKerja()).minusMonths(1).toDate());
        }
        toolbarButtonTransaksi.defaultMode();
        enableForm(false);
        tblSetoran.setAutoCreateColumnsFromModel(false);
        offset = 0;
        limit = 100;
        TextComponentUtils.setNumericTextOnly(txtAngsuran);
        TextComponentUtils.setNumericTextOnly(txtTabungan);
        TextComponentUtils.setNumericTextOnly(txtKasbon);
        TextComponentUtils.setNumericTextOnly(txtBayarKasbon);
        TextComponentUtils.setNumericTextOnly(txtOvertime);
        TextComponentUtils.setNumericTextOnly(txtCicilan);
        TextComponentUtils.setCurrency(txtAngsuran);
        TextComponentUtils.setCurrency(txtTabungan);
        TextComponentUtils.setCurrency(txtKasbon);
        TextComponentUtils.setCurrency(txtBayarKasbon);
        TextComponentUtils.setCurrency(txtOvertime);
        TextComponentUtils.setCurrency(txtCicilan);
        TextComponentUtils.setCurrency(txtUbahHutang);
        LoadDatabaseToTable();
        tblSetoran.getSelectionModel().addListSelectionListener(this);
        lblJalanStatus.setVisible(false);
        checkJalanStatus.setVisible(false);
        txtUbahHutang.setVisible(false);
    }

    private void initComponentFocusOrder() {
        List<Component> listComponentActive = new ArrayList<Component>();
        listComponentActive.add(txtNoLambung);
        listComponentActive.add(checkJalanStatus);
        listComponentActive.add(txtNRP);
        listComponentActive.add(txtNama);
        listComponentActive.add(txtSetoranCounter);
        listComponentActive.add(txtKeterangan);
        listComponentActive.add(txtKodeSetoran);
        listComponentActive.add(txtAngsuran);
        listComponentActive.add(txtTabungan);
        listComponentActive.add(txtKasbon);
        listComponentActive.add(txtBayarKasbon);
        listComponentActive.add(txtOvertime);
        listComponentActive.add(txtCicilan);
        this.setFocusTraversalPolicy(new PapermanFocusComponentUtils(listComponentActive));
    }

    private void initButtonHotkeyFunction() {
        KeyboardFocusManager kfm = KeyboardFocusManager.getCurrentKeyboardFocusManager();
        kfm.addKeyEventDispatcher(new KeyEventDispatcher() {

            public boolean dispatchKeyEvent(KeyEvent ke) {
                /*  Button Tambah  */
                if (ke.getKeyCode() == KeyEvent.VK_F1 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnTambah().doClick();
                    System.out.println("F1 Pressed and GO aksiTambah");
                }
                /*  Button Ubah  */
                if (ke.getKeyCode() == KeyEvent.VK_F2 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnUbah().doClick();
                    System.out.println("F2 Pressed and GO aksiUbah");
                }
                /*  Button Simpan  */
                if (ke.getKeyCode() == KeyEvent.VK_F3 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnSimpan().doClick();
                    System.out.println("F3 Pressed and GO aksiSimpan");
                }
                /*  Button Hapus  */
                if (ke.getKeyCode() == KeyEvent.VK_F4 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnHapus().doClick();
                    System.out.println("F4 Pressed and GO aksiHapus");
                }
                /*  Button Cetak  */
                if (ke.getKeyCode() == KeyEvent.VK_F5 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnCetak().doClick();
                    System.out.println("F5 Pressed and GO aksiCetak");
                }
                /*  Button Batal  */
                if (ke.getKeyCode() == KeyEvent.VK_F6 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnBatal().doClick();
                    System.out.println("F6 Pressed and GO aksiBatal");
                }
                /*  Button Keluar  */
                if (ke.getKeyCode() == KeyEvent.VK_F7 && ke.getID() == KeyEvent.KEY_PRESSED) {
                    toolbarButtonTransaksi.getBtnKeluar().doClick();
                    System.out.println("F7 Pressed and GO aksiKeluar");
                }
                return false;
            }
        });

    }

    private void initButtonListener() {
        toolbarButtonTransaksi.getBtnTambah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiTambah();
            }
        });

        toolbarButtonTransaksi.getBtnUbah().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiUbah();
            }
        });

        toolbarButtonTransaksi.getBtnHapus().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiHapus();
            }
        });

        toolbarButtonTransaksi.getBtnSimpan().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiSimpan();
            }
        });

        toolbarButtonTransaksi.getBtnCetak().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiCetak();
            }
        });

        toolbarButtonTransaksi.getBtnBatal().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiBatal();
            }
        });

        toolbarButtonTransaksi.getBtnKeluar().addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(ActionEvent ae) {
                aksiKeluar();
            }
        });
    }

    private void aksiTambah() {
        listSetoran = Main.getTransaksiService().getLatestSetoranCount();
        if (OpenClosedTrans.isUpdatingClosingBiru) {
            JOptionPane.showMessageDialog(this, "Tidak Dapat Menambah Setoran Ketika Sedang Membuka Closed Transaksi\nHarap Lakukan Closing Terlebih Dahulu ! !", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            return;
        }
        if (listClosingBulananSaldoAwal ==null) {
            listClosingBulananSaldoAwal = Main.getTransaksiService().closingBulananRecordForSaldoAwal(komposisisetoran, sys.getTglKerja());
        }
//        closingBulanan clbValid = Main.getTransaksiService().findAnyClosingBulananValid(new DateTime(listSetoran.get(0).getTglSPO()).withDayOfMonth(new DateTime(listSetoran.get(0).getTglSPO()).getMonthOfYear()).toDate(), isClosedFor.CLOSING_BULAN);
        if (isBonusHooray) {
            genCode = komposisisetoran.getGenCodeRef();
        } else {
            genCode = Main.getSistemService().findBySpecific("SETORAN", komposisisetoran.getNamaKomposisi());
        }
        if (genCode != null && !listSetoran.isEmpty()) {
////            if (clbValid != null) {
//                txtAngsuran.setText(TextComponentUtils.formatNumber(clbValid.getTotalAngsuran()));
//                txtTabungan.setText(TextComponentUtils.formatNumber(clbValid.getTotalTabungan()));
//            } else {
            txtAngsuran.setText(TextComponentUtils.formatNumber(komposisisetoran.getAngsuran()));
            txtTabungan.setText(TextComponentUtils.formatNumber(komposisisetoran.getTabungan()));
//            }
//            if (!listClosingBulananSaldoAwal.isEmpty() && listClosingBulananSaldoAwal.get(0).getClosedFor() == isClosedFor.CLOSING_SALDO_AWAL) {
//                setoranCounter = listClosingBulananSaldoAwal.get(0).getRefSetoranKe();
//            } else {
//            setoranCounter = listSetoran.get(0).getCounter_setoran();
//            }
            generateCode = Main.getSistemService().generateCode(genCode.getTagTransaction(), genCode.getTagArmada(), sys.getTglKerja());
//            setoranCounter = listSetoran.get(0).getCounter_setoran();
//            setoranCounter++;
            txtKodeSetoran.setText(formatDate.format(generateCode.get(0).getTanggal()) + "-" + generateCode.get(0).getTagTransaction().substring(0, 3) + "-" + generateCode.get(0).getTagArmada().substring(0, 1) + "-" + new Integer(generateCode.get(0).getLastnum() + 1));
//            txtSetoranCounter.setText(String.valueOf(setoranCounter));
            enableForm(true);
            enableSearchEngine(false);
            toolbarButtonTransaksi.editMode();
        }
        if (genCode == null && listSetoran.isEmpty()) {
            JOptionPane.showMessageDialog(Main.getMainMenu(), "Nomor Kode Belum Di Konfigurasi\nHarap Setting Nomor Kode Pada Menu File > Konfigurasi");
            enableForm(false);
            enableSearchEngine(false);
            tblSetoran.setEnabled(false);
            toolbarButtonTransaksi.defaultMode();
        }
        if (genCode != null && listSetoran.isEmpty()) {
//            if (!listClosingBulananSaldoAwal.isEmpty() && listClosingBulananSaldoAwal.get(0).getClosedFor() == isClosedFor.CLOSING_SALDO_AWAL) {
//                setoranCounter = listClosingBulananSaldoAwal.get(0).getRefSetoranKe();
//            } else {
            setoranCounter = 1;
//            }
            generateCode = Main.getSistemService().generateCode(genCode.getTagTransaction(), genCode.getTagArmada(), sys.getTglKerja());
//            setoranCounter = 1;
            enableForm(true);
            enableSearchEngine(false);
//            txtSetoranCounter.setText(String.valueOf(setoranCounter));
            txtKodeSetoran.setText(formatDate.format(generateCode.get(0).getTanggal()) + "-" + generateCode.get(0).getTagTransaction().substring(0, 3) + "-" + generateCode.get(0).getTagArmada().substring(0, 1) + "-" + new Integer(generateCode.get(0).getLastnum() + 1));
            txtAngsuran.setText(TextComponentUtils.formatNumber(komposisisetoran.getAngsuran()));
            txtTabungan.setText(TextComponentUtils.formatNumber(komposisisetoran.getTabungan()));
            List<closingBulanan> listClosing = Main.getTransaksiService().closingBulananRecord();
            if (listClosing.isEmpty()) {
                closingBulanan clb = new closingBulanan();
                clb.setActClosing(aksiClosing.NOPE);
                clb.setPeriodeBulan(sys.getTglKerja());
                clb.setTglClosing(sys.getTglKerja());
                clb.setTotalAngsuran(new BigDecimal(0));
                clb.setTotalTabungan(new BigDecimal(0));
                clb.setTotalKas(new BigDecimal(0));
                clb.setTotalBayarKas(new BigDecimal(0));
                clb.setTotalOvertime(new BigDecimal(0));
                clb.setTotalCicilan(new BigDecimal(0));
                clb.setTotalSetor(new BigDecimal(0));
                Main.getTransaksiService().save(clb);
            }
            toolbarButtonTransaksi.firstTransaksiMode();
        }
        if (genCode == null && !listSetoran.isEmpty()) {
            JOptionPane.showMessageDialog(Main.getMainMenu(), "Terjadi Kesalahan Pada Form Transaksi\nPeriksa Lagi Apakah Nomor Kode Sudah Dikonfigurasi Atau Belum? ?", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
            enableForm(false);
            enableSearchEngine(false);
            toolbarButtonTransaksi.defaultMode();
        }
        if (!listSetoran.isEmpty() && listSetoran.get(0).getIdClosing() != null && listSetoran.get(0).getClosedStatus().equals(isClosedStatus.CLOSED)) {
            if (new DateTime(listSetoran.get(0).getIdClosing().getPeriodeBulan().getTime()).getMonthOfYear() == new DateTime(Main.getSistemService().sistemRecord().getTglKerja().getTime()).getMonthOfYear()) {
                JOptionPane.showMessageDialog(this, "Transaksi Sudah Di Closing\nTidak Bisa Melakukan Transaksi Bila Telah Di Closing", "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
                houseKeeping();
                enableForm(false);
                enableSearchEngine(false);
                toolbarButtonTransaksi.defaultMode();
            }
        } else {
            txtNoLambung.requestFocus();
            return;
//            enableForm(true);
//            enableSearchEngine(false);
        }
        System.out.println("stc = " + setoranCounter);
        System.out.println("stp = " + setoranPrevious);
        txtNoLambung.requestFocus();
        tblSetoran.setEnabled(false);
    }

    private void aksiUbah() {
        listClosingBulananSaldoAwal = Main.getTransaksiService().closingBulananRecordForSaldoAwal(komposisisetoran, sys.getTglKerja());
        listSetoranForUpdate = Main.getTransaksiService().getUpdatedSetoranInRange(stoDet.getKemudi().getKend().getNoLambung(), sto.getTglSPO(), Main.getTransaksiService().getLatestSetoranCount(stoDet.getKemudi().getKend().getNoLambung()).get(0).getSetor_map().getTglSPO());
        System.out.println("listSetoranForUpdate size = " + listSetoranForUpdate.size());
        System.out.println("getLatestSetoranCount= " + Main.getTransaksiService().getLatestSetoranCount(stoDet.getKemudi().getKend().getNoLambung()).get(0).getKemudi().getKend().getNoLambung());
        System.out.println("getLatestSetoranCount dateAwal= " + sto.getTglSPO());
        System.out.println("getLatestSetoranCount dateAkhir= " + Main.getTransaksiService().getLatestSetoranCount(stoDet.getKemudi().getKend().getNoLambung()).get(0).getSetor_map().getTglSPO());
        enableForm(true);
        enableSearchEngine(false);
//        setoranPrevious = Integer.parseInt(txtSetoranCounter.getText());
//        System.out.println("stp = " + setoranPrevious);
        this.isEditFire = true;
        txtSetoranCounter.setEnabled(true);
        txtNoLambung.setEnabled(false);
        toolbarButtonTransaksi.editMode();
        tblSetoran.setEnabled(false);
        lblJalanStatus.setEnabled(true);
        checkJalanStatus.setEnabled(true);
    }

    private void aksiHapus() {
//        setoranPrevious = Integer.parseInt(txtSetoranCounter.getText());
//        System.out.println("stp = " + setoranPrevious);
        String noLambung = stoDet.getKend().getNoLambung().toString();
        String nrp = stoDet.getKemudi().getNrp();
        String nama = stoDet.getKemudi().getNama();
        String setoranCount = sto.getCounter_setoran().toString();
        String jatuhTempo = sto.getTglJatuhTempo().toString();
        String tglSPO = sto.getTglSPO().toString();
        String kodeSetoran = sto.getKode();
        String angsuran = stoDet.getAngsuran().toString();
        String tabungan = stoDet.getTabungan().toString();
        String kasbon = stoDet.getKasbon().toString();
        String bayarKasbon = stoDet.getBayar().toString();
        String overtime = stoDet.getOvtime().toString();
        String cicilan = stoDet.getKS().toString();
        int konfirmasi = JOptionPane.showConfirmDialog(this,
                "Anda Yakin Akan Menghapus Data Ini ? ?\n"
                + "Rincian :\n"
                + "Kode Setoran = " + kodeSetoran + "\n"
                + "No.Lambung = " + noLambung + "\n"
                + "NRP =  " + nrp + "\n"
                + "Nama = " + nama + "\n"
                + "Setoran Ke = " + setoranCount + "\n"
                + "Tgl. Jatuh Tempo =  " + jatuhTempo + "\n"
                + "Tgl. SPO = " + tglSPO + "\n"
                + "Angsuran = " + angsuran + "\n"
                + "Tabungan = " + tabungan + "\n"
                + "Kasbon = " + kasbon + "\n"
                + "Bayar Kasbon = " + bayarKasbon + "\n"
                + "Over Time= " + overtime + "\n"
                + "Cicilan= " + cicilan,
                "Konfirmasi Hapus Data", option, JOptionPane.QUESTION_MESSAGE);
        if (konfirmasi == JOptionPane.OK_OPTION) {
            setoranCounter = new Integer(txtNoLambung.getText());
            Main.getTransaksiService().delete(sto);
            LoadDatabaseToTable();
            houseKeeping();
            List<setoranDetail> listStoDet = Main.getTransaksiService().getLatestSetoranCount(setoranCounter);
            if (listStoDet.isEmpty()) {
                setoranCounter = 0;
            } else {
                if (listStoDet.get(0).getSetor_map().getTotalHutang().compareTo(BigDecimal.ZERO) != 0) {
                    setoran st = listStoDet.get(0).getSetor_map();
                    st.setPayedStatus(TransaksiStatus.U);
                    Main.getTransaksiService().save(st);
                }
                setoranCounter = listStoDet.get(0).getSetor_map().getCounter_setoran();
            }
            sto = null;
            stoDet = null;
            listSetoran = null;
            listStoDet = null;
        }
        if (konfirmasi == JOptionPane.CANCEL_OPTION) {
            houseKeeping();
            sto = null;
            stoDet = null;
            listSetoran = null;
        }
        enableForm(false);
        enableSearchEngine(true);
        houseKeeping();
        tblSetoran.getSelectionModel().clearSelection();
        tblSetoran.setEnabled(true);
        toolbarButtonTransaksi.defaultMode();
    }

    private void aksiSimpan() {
        try {
            cekDuplicate();
        } catch (JDBCException jde) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Data Kode Setoran Yang Di Input Sudah Ada\nGanti Kode Setoran ? ? ?", "Konfirmasi", option, JOptionPane.QUESTION_MESSAGE);
            if (konfirmasi == JOptionPane.OK_OPTION) {
                enableForm(true);
                enableSearchEngine(false);
                txtKodeSetoran.requestFocus();
                toolbarButtonTransaksi.editMode();
                tblSetoran.setEnabled(false);
            } else if (konfirmasi == JOptionPane.CANCEL_OPTION) {
                enableForm(false);
                enableSearchEngine(true);
                houseKeeping();
                toolbarButtonTransaksi.defaultMode();
                tblSetoran.setEnabled(true);
            }
        } catch (TransactionException te) {
            String error = te.getMessage();
            TextArea txtError = new TextArea();
            txtError.setText(error);
            Object messageOutput[] = {"Koneksi Database Terputus, Mohon Periksa Koneksi Dengan Server\n", "Rincian Kesalahan :", txtError};
            JOptionPane msg = new JOptionPane();
            msg.setMessage(messageOutput);
            msg.setMessageType(JOptionPane.ERROR_MESSAGE);
            JDialog dialog = msg.createDialog(this, "Terjadi Kesalahan Koneksi Database");
            dialog.setVisible(true);
            houseKeeping();
            enableForm(false);
            tblSetoran.getSelectionModel().clearSelection();
            tblSetoran.setEnabled(true);
            toolbarButtonTransaksi.defaultMode();
            sto = null;
            stoDet = null;
        }
    }

    private void cekDuplicate() {
        if (OpenClosedTrans.isUpdatingClosingBiru) {
            stoDetUpdate = new setoranDetail();
            stoDetUpdate.setId(stoDet.getId());
            stoDetUpdate.setSetor_map(stoDet.getSetor_map());
            stoDetUpdate.setKemudi(stoDet.getKemudi());
            stoDetUpdate.setKend(stoDet.getKend());
            OpenClosedTrans.lastUpdated.add(stoDetUpdate);
            System.out.println("array of last update closing = " + OpenClosedTrans.lastUpdated.size());
        }
        if (validateForm()) {
            closingBulanan closingByLambung = Main.getTransaksiService().findClosingByRefNoLambung(new Integer(txtNoLambung.getText()));
            if(closingByLambung == null) {
                closingBulanan clb = new closingBulanan();
                clb.setActClosing(aksiClosing.NOPE);
                clb.setRefNoLambung(new Integer(txtNoLambung.getText()));
                clb.setClosedFor(isClosedFor.CLOSING_SALDO_AWAL);
                clb.setRefSetoranKe(new Integer(txtSetoranCounter.getText()));
                clb.setPeriodeBulan(sys.getTglKerja());
                clb.setTotalAngsuran(new BigDecimal(0));
                clb.setTotalTabungan(new BigDecimal(0));
                clb.setTotalKas(new BigDecimal(0));
                clb.setTotalBayarKas(new BigDecimal(0));
                clb.setTotalOvertime(new BigDecimal(0));
                clb.setTotalCicilan(new BigDecimal(0));
                clb.setTotalSetor(new BigDecimal(0));
                clb.setIdKomposisiRefSaldoAwal(Main.getSistemService().findKomposisiByName(komposisisetoran.getNamaKomposisi()));
                Main.getTransaksiService().save(clb);
            }
            LoadFormToDatabase();
            Main.getTransaksiService().save(sto);
            Main.getSistemService().save(genCode);
            if (listSetoranForUpdate != null && !listSetoranForUpdate.isEmpty()) {
                for (setoran storan : listSetoranForUpdate) {
                    BigDecimal totHut = storan.getTotalHutang();
                    BigDecimal totKas = TextComponentUtils.parseNumberToBigDecimal(lblNominal.getText().substring(3));
                    BigDecimal totalUpdate = totHut.subtract(totKas);
                    if (totalUpdate.signum() == -1) {
                        totalUpdate = totalUpdate.negate();
                    }
                    storan.setTotalHutang(totalUpdate);
                    Main.getTransaksiService().save(storan);
                    System.out.println("totHutang= " + totHut);
                    System.out.println("totKas= " + totKas);
                }
            }
            LoadDatabaseToTable();
            houseKeeping();
            enableForm(false);
            enableSearchEngine(true);
            tblSetoran.setEnabled(true);
            JOptionPane.showMessageDialog(this, "Proses Penyimpanan Telah Berhasil", "Pemberitahuan", JOptionPane.INFORMATION_MESSAGE);
            tblSetoran.getSelectionModel().clearSelection();
            toolbarButtonTransaksi.defaultMode();
            sto = null;
            stoDet = null;
            ListStoDetail = null;
            listClosingBulananSaldoAwal = null;
            listSetoranForUpdate = null;
            setoranCounter = 0;
        } else {
            JOptionPane.showMessageDialog(this, "Terjadi Kesalahan\nGagal Menyimpan", "Pemberitahuan", JOptionPane.ERROR_MESSAGE);
            kenaTilang();
            enableForm(true);
            enableSearchEngine(false);
            tblSetoran.setEnabled(false);
            toolbarButtonTransaksi.editMode();
        }
    }

    private void aksiBatal() {
//        if (setoranCounter != listSetoran.get(0).getCounter_setoran()) {
//            setoranCounter = listSetoran.get(0).getCounter_setoran();
//        }
//        listSetoran = Main.getTransaksiService().getLatestSetoranCount();
//        setoranCounter = listSetoran.get(0).getCounter_setoran();
//        System.out.println("stc = " + setoranCounter);
//        System.out.println("stp = " + setoranPrevious);
        houseKeeping();
        enableForm(false);
        enableSearchEngine(true);
        tblSetoran.getSelectionModel().clearSelection();
        tblSetoran.setEnabled(true);
        sto = null;
        stoDet = null;
        ListStoDetail = null;
        listClosingBulananSaldoAwal = null;
        setoranCounter = 0;
//        listSetoran = null;
//        listSetoranDetail = null;
        toolbarButtonTransaksi.defaultMode();
    }

    private void aksiCetak() {
        int pil = JOptionPane.YES_NO_OPTION;
        int konfirm = JOptionPane.showConfirmDialog(this, "Pastikan Kertas Dan Printer Telah Terpasang Dengan Baik\nLanjutkan? ?", "Peringatan Sistem", pil, JOptionPane.QUESTION_MESSAGE);
        if (konfirm == JOptionPane.YES_OPTION) {
            JasperPrint loadReport = Main.getReportService().getTandaTerimaReport(new Integer(txtNoLambung.getText()), new Integer(txtSetoranCounter.getText()), OsUtils.getOsName());
            JasperViewer.setDefaultLookAndFeelDecorated(true);
            JasperViewer.viewReport(loadReport, false);
        }
        if (konfirm == JOptionPane.NO_OPTION) {
            toolbarButtonTransaksi.defaultMode();
            return;
        }
    }

    private void aksiKeluar() {
        if (OpenClosedTrans.isUpdatingClosingBiru == true) {
            int konfirmasi = JOptionPane.showConfirmDialog(this, "Masih Terdapat Setoran Yang Terbuka Belum Di Closing\nYakin Akan Menutup ? ? ?", "Pesan Sistem", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);
            if (konfirmasi == JOptionPane.YES_OPTION) {
                enableForm(false);
                enableSearchEngine(true);
                houseKeeping();
                tblSetoran.getSelectionModel().clearSelection();
                tblSetoran.setEnabled(false);
                toolbarButtonTransaksi.keluarMode();
                destroy();
                sto = null;
                stoDet = null;
                listSetoran = null;
                listSetoranDetail = null;
                ListStoDetail = null;
                listClosingBulananSaldoAwal = null;
                setoranCounter = 0;
            }
            if (konfirmasi == JOptionPane.NO_OPTION) {
                return;
            }
        } else {
            enableForm(false);
            enableSearchEngine(true);
            houseKeeping();
            tblSetoran.getSelectionModel().clearSelection();
            tblSetoran.setEnabled(false);
            toolbarButtonTransaksi.keluarMode();
            destroy();
            sto = null;
            stoDet = null;
            listSetoran = null;
            listSetoranDetail = null;
            ListStoDetail = null;
            listClosingBulananSaldoAwal = null;
            setoranCounter = 0;
        }
    }

    private void enableForm(boolean en) {
        txtNoLambung.setEnabled(en);
        txtNRP.setEnabled(en);
        txtNama.setEnabled(en);
        txtSetoranCounter.setEnabled(en);
        dateJatuhTempo.setEnabled(en);
        dateSPO.setEnabled(en);
        txtKeterangan.setEnabled(en);
        txtAngsuran.setEnabled(en);
        txtKodeSetoran.setEnabled(en);
        txtTabungan.setEnabled(en);
        txtKasbon.setEnabled(en);
        txtBayarKasbon.setEnabled(en);
        txtOvertime.setEnabled(en);
        txtCicilan.setEnabled(en);
        pnlTerimaKasir.setEnabled(en);
        pnlHutang.setEnabled(en);
        lblNominal.setEnabled(en);
        lblHutang.setEnabled(en);
        lblRupiahHutang.setEnabled(en);
        lblRupiahKasir.setEnabled(en);
    }

    private void enableSearchEngine(boolean es) {
        txtCariSetoranLambung.setEnabled(es);
        dateCariSetoran.setEnabled(es);
        tblSetoran.setEnabled(es);
        txtCariSetoranLambung.setText("");
        dateCariSetoran.setDate(null);
    }

    private void houseKeeping() {
        txtNoLambung.setText("");
        txtNRP.setText("");
        txtNama.setText("");
        txtSetoranCounter.setText("");
        dateJatuhTempo.setDate(null);
        dateSPO.setDate(null);
        txtKeterangan.setText("");
        txtKodeSetoran.setText("");
        txtAngsuran.setText("");
        txtTabungan.setText("");
        txtKasbon.setText("");
        txtBayarKasbon.setText("");
        txtOvertime.setText("");
        txtCicilan.setText("");
        lblNominal.setText("");
        lblHutang.setText("");
        lblRupiahHutang.setText("");
        lblRupiahKasir.setText("");
        lblJalanStatus.setVisible(false);
        checkJalanStatus.setSelected(false);
        checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.PLAIN, 13));
        checkJalanStatus.setText("Tidak/Rusak");
        checkJalanStatus.setVisible(false);
    }

    private void kenaTilang() {
        if (txtNoLambung.getText().isEmpty()
                && txtKodeSetoran.getText().isEmpty()
                && txtKasbon.getText().isEmpty()
                && txtBayarKasbon.getText().isEmpty()
                && txtOvertime.getText().isEmpty()
                && txtCicilan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Tidak boleh ada field yang kosong \nPeriksa Lagi !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoLambung.setForeground(Color.RED);
            txtNoLambung.setText("TIDAK BOLEH KOSONG...");
            txtKodeSetoran.setForeground(Color.RED);
            txtKodeSetoran.setText("TIDAK BOLEH KOSONG...");
            txtKasbon.setForeground(Color.RED);
            txtKasbon.setText("TIDAK BOLEH KOSONG...");
            txtBayarKasbon.setForeground(Color.RED);
            txtBayarKasbon.setText("TIDAK BOLEH KOSONG...");
            txtOvertime.setForeground(Color.RED);
            txtOvertime.setText("TIDAK BOLEH KOSONG...");
            txtCicilan.setForeground(Color.RED);
            txtCicilan.setText("TIDAK BOLEH KOSONG...");
        } else if (txtNoLambung.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi No. Lambung !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtNoLambung.requestFocus();
            txtNoLambung.setForeground(Color.RED);
            txtNoLambung.setText("TIDAK BOLEH KOSONG...");
        } else if (txtKodeSetoran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Kode Setoran !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtKodeSetoran.requestFocus();
            txtKodeSetoran.setForeground(Color.RED);
            txtKodeSetoran.setText("TIDAK BOLEH KOSONG...");
        } else if (txtAngsuran.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nilai Angsuran!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtAngsuran.requestFocus();
            txtAngsuran.setForeground(Color.RED);
            txtAngsuran.setText("TIDAK BOLEH KOSONG...");
        } else if (txtTabungan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nilai Tabungan !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtTabungan.requestFocus();
            txtTabungan.setForeground(Color.RED);
            txtTabungan.setText("TIDAK BOLEH KOSONG...");
        } else if (txtKasbon.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nilai Kasbon!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtKasbon.requestFocus();
            txtKasbon.setForeground(Color.RED);
            txtKasbon.setText("TIDAK BOLEH KOSONG...");
        } else if (txtBayarKasbon.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Pembayaran Kasbon!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtBayarKasbon.requestFocus();
            txtBayarKasbon.setForeground(Color.RED);
            txtBayarKasbon.setText("TIDAK BOLEH KOSONG...");
        } else if (txtOvertime.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nilai Overtime!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtOvertime.requestFocus();
            txtOvertime.setForeground(Color.RED);
            txtOvertime.setText("TIDAK BOLEH KOSONG...");
        } else if (txtCicilan.getText().isEmpty()) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi Nilai Cicilan!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            txtCicilan.requestFocus();
            txtCicilan.setForeground(Color.RED);
            txtCicilan.setText("TIDAK BOLEH KOSONG...");
        } else if (dateJatuhTempo.getDate() == null) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi tanggal jatuh tempo !!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            dateJatuhTempo.setBackground(Color.RED);
            dateJatuhTempo.requestFocusInWindow();
        } else if (dateSPO.getDate() == null) {
            JOptionPane.showMessageDialog(this, "!!! Harap isi tanggal SPO!!!", "Terjadi Error", JOptionPane.ERROR_MESSAGE);
            dateSPO.setBackground(Color.RED);
            dateSPO.requestFocusInWindow();
        }
    }

    private void LoadFormToDatabase() {
        if (sto == null) {
            sto = new setoran();
            stoDet = new setoranDetail();
        }
        sto.setKode(txtKodeSetoran.getText());
        sto.setTglJatuhTempo(dateJatuhTempo.getDate());
        sto.setTglSPO(dateSPO.getDate());
        sto.setTglSetoran(sys.getTglKerja());
        sto.setCounter_setoran(new Integer(txtSetoranCounter.getText()));
        sto.setTotalSetoran(TextComponentUtils.parseNumberToBigDecimal(lblNominal.getText().substring(3)));
        sto.setIdKomposisi(komposisisetoran);
        sto.setUser(sys.getLastLoginUser().getNamaLogin());
        stoDet.setKemudi(drive);
        stoDet.setKend(kend);
        stoDet.setAngsuran(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText()));
        stoDet.setTabungan(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
        stoDet.setKasbon(TextComponentUtils.parseNumberToBigDecimal(txtKasbon.getText()));
        stoDet.setBayar(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText()));
        stoDet.setOvtime(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText()));
        stoDet.setKS(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
        stoDet.setKet(txtKeterangan.getText());
        stoDet.setSetor_map(sto);
        listSetoranDetail.add(stoDet);
        sto.setDetail(listSetoranDetail);
        sto.setClosedStatus(isClosedStatus.AVAILABLE);
        if (sto.getPayedStatus() == TransaksiStatus.L) {
            List<setoran> listStats = Main.getTransaksiService().findStatusSetoranByLambung(new Integer(txtNoLambung.getText()), TransaksiStatus.U);
            for (setoran st : listStats) {
                st.setPayedStatus(TransaksiStatus.L);
                Main.getTransaksiService().save(st);
                System.out.println("id= " + st.getId());
                System.out.println("status = " + st.getPayedStatus());
            }
        }
        if (sto.getPayedStatus() == TransaksiStatus.U) {
            List<setoran> listStats = Main.getTransaksiService().findStatusSetoranByLambung(new Integer(txtNoLambung.getText()), TransaksiStatus.U);
            for (setoran st : listStats) {
                st.setPayedStatus(TransaksiStatus.L);
                Main.getTransaksiService().save(st);
                System.out.println("id= " + st.getId());
                System.out.println("status = " + st.getPayedStatus());
            }
            sto.setPayedStatus(TransaksiStatus.U);
        }
        if (sto.getJalanStatus() == JalanStatus.J) {
            sto.setJalanStatus(JalanStatus.J);
        } else {
            sto.setJalanStatus(JalanStatus.M);
        }
        System.out.println("jalan status now = " + sto.getJalanStatus());
        if (!lblHutang.getText().isEmpty() && !lblHutang.getText().equalsIgnoreCase("Belum Ada Hutang")) {
            sto.setTotalHutang(TextComponentUtils.parseNumberToBigDecimal(lblHutang.getText().substring(3)));
        } else {
            sto.setTotalHutang(BigDecimal.ZERO);
        }
        if (isEditFire) {
            genCode.setId(genCode.getId());
            genCode.setLastnum(genCode.getLastnum());
        } else {
            genCode.setId(genCode.getId());
            genCode.setLastnum(genCode.getLastnum() + 1);
        }
        if (!listClosingBulananSaldoAwal.isEmpty()) {
//            if (Main.getTransaksiService().findClosingByRefNoLambung(new Integer(txtNoLambung.getText())).getActClosing().equals(aksiClosing.DO_CLOSING_SALDO_AWAL)) {
            closingBulanan clb = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja());
            if (clb != null && clb.getActClosing().equals(aksiClosing.DO_CLOSING_SALDO_AWAL)) {
                clb.setActClosing(aksiClosing.NOPE);
                Main.getTransaksiService().save(clb);
            }
//            clb.setRefSetoranKe(new Integer(txtSetoranCounter.getText()));
//            }
        }
    }

    private void LoadDataListSetoranDetail() {
        for (int i = 0; i < listSetoranDetail.size(); i++) {
            listSetoranDetail.get(i).setAngsuran(new BigDecimal(txtAngsuran.getText()));
            listSetoranDetail.get(i).setTabungan(new BigDecimal(txtTabungan.getText()));
            listSetoranDetail.get(i).setKasbon(new BigDecimal(txtKasbon.getText()));
            listSetoranDetail.get(i).setBayar(new BigDecimal(txtBayarKasbon.getText()));
            listSetoranDetail.get(i).setOvtime(new BigDecimal(txtOvertime.getText()));
            listSetoranDetail.get(i).setKS(new BigDecimal(txtCicilan.getText()));
        }
    }

    private void LoadDatabaseToForm() {
        kend.setId(stoDet.getKemudi().getKend().getId());
        drive.setId(stoDet.getKemudi().getId());
        txtNoLambung.setText(stoDet.getKemudi().getKend().getNoLambung().toString());
        txtNRP.setText(stoDet.getKemudi().getNrp());
        txtNama.setText(stoDet.getKemudi().getNama());
        txtSetoranCounter.setText(stoDet.getSetor_map().getCounter_setoran().toString());
        dateJatuhTempo.setDate(stoDet.getSetor_map().getTglJatuhTempo());
        dateSPO.setDate(stoDet.getSetor_map().getTglSPO());
        txtKeterangan.setText(stoDet.getKet());
        txtKodeSetoran.setText(stoDet.getSetor_map().getKode());
        txtAngsuran.setText(TextComponentUtils.formatNumber(stoDet.getAngsuran()));
        txtTabungan.setText(TextComponentUtils.formatNumber(stoDet.getTabungan()));
        txtKasbon.setText(TextComponentUtils.formatNumber(stoDet.getKasbon()));
        txtBayarKasbon.setText(TextComponentUtils.formatNumber(stoDet.getBayar()));
        txtOvertime.setText(TextComponentUtils.formatNumber(stoDet.getOvtime()));
        txtCicilan.setText(TextComponentUtils.formatNumber(stoDet.getKS()));
        lblNominal.setText(rupiahFormatter.format(stoDet.getSetor_map().getTotalSetoran()));
        lblHutang.setText(rupiahFormatter.format(stoDet.getSetor_map().getTotalHutang()));
        lblJalanStatus.setVisible(true);
        lblJalanStatus.setEnabled(false);
        if (stoDet.getSetor_map().getJalanStatus() == JalanStatus.J) {
            checkJalanStatus.setVisible(true);
            checkJalanStatus.setEnabled(false);
            checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.BOLD, 13));
            checkJalanStatus.setText("Jalan");
            checkJalanStatus.setSelected(true);
        } else {
            checkJalanStatus.setVisible(true);
            checkJalanStatus.setEnabled(false);
            checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.PLAIN, 13));
            checkJalanStatus.setText("Tidak/Rusak");
            checkJalanStatus.setSelected(false);
        }
    }

    private void LoadDatabaseToTable() {
        txtShowPage.setText(String.valueOf(limit));
        lblTotalHal.setText("Total " + String.valueOf(totalPage(limit)) + " Halaman");
        currentPos = limit;
        System.out.println("current pos= " + currentPos);
        System.out.println("limit= " + limit);
        currentPages(currentPos, limit);
        if (OpenClosedTrans.isUpdatingClosingBiru == true) {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, OpenClosedTrans.lastClosing, OpenClosedTrans.lastClosing, OpenClosedTrans.lastSelectedTagArmada);
            tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
            initColumnSize();
            txtShowPage.setEnabled(false);
            btnNext.setEnabled(false);
            btnBack.setEnabled(false);
            txtPages.setEnabled(false);
        } else {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), offset, limit);
//        listSetoranDetail = Main.getTransaksiService().setoranDetailRecord();
            tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
            initColumnSize();
        }
//        lblTotalHal.setText((totalPage(new Integer(txtShowPage.getText()))).toString());
    }

    private boolean validateForm() {
        if (!txtNoLambung.getText().isEmpty() && !txtNoLambung.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtKodeSetoran.getText().isEmpty() && !txtKodeSetoran.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtAngsuran.getText().isEmpty() && !txtAngsuran.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtTabungan.getText().isEmpty() && !txtTabungan.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtKasbon.getText().isEmpty() && !txtKasbon.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtBayarKasbon.getText().isEmpty() && !txtBayarKasbon.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtOvertime.getText().isEmpty() && !txtOvertime.getText().equals("TIDAK BOLEH KOSONG...")
                && !txtCicilan.getText().isEmpty() && !txtCicilan.getText().equals("TIDAK BOLEH KOSONG...")
                && dateSPO.getDate() != null && dateJatuhTempo.getDate() != null) {
            return true;
        }
        return false;
    }

    private boolean validateHutang() {
        if (!txtBayarKasbon.getText().isEmpty()
                //                && !txtBayarKasbon.getText().equalsIgnoreCase("0")
                && !txtOvertime.getText().isEmpty()
                && !txtCicilan.getText().isEmpty()) {
            return true;
        } else if (txtBayarKasbon.getText().isEmpty()
                && txtOvertime.getText().isEmpty()
                && txtCicilan.getText().isEmpty()) {
            return false;
        } else {
            return false;
        }
    }

    private void initColumnSize() {
        tblSetoran.getColumnModel().getColumn(0).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(0).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(1).setMinWidth(100);
        tblSetoran.getColumnModel().getColumn(1).setMaxWidth(100);
        tblSetoran.getColumnModel().getColumn(2).setMinWidth(100);
        tblSetoran.getColumnModel().getColumn(2).setMaxWidth(100);
        tblSetoran.getColumnModel().getColumn(3).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(3).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(4).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(4).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(5).setMinWidth(170);
        tblSetoran.getColumnModel().getColumn(5).setMaxWidth(200);
        tblSetoran.getColumnModel().getColumn(6).setMinWidth(170);
        tblSetoran.getColumnModel().getColumn(6).setMaxWidth(200);
    }

    public void valueChanged(ListSelectionEvent lse) {
        if (tblSetoran.getSelectedRow() >= 0) {
            stoDet = listSetoranDetail.get(tblSetoran.getSelectedRow());
            sto = stoDet.getSetor_map();
            kend = new kendaraan();
            drive = new pengemudi();
            genCode = Main.getSistemService().findBySpecific("SETORAN", "BIRU");
            LoadDatabaseToForm();
            toolbarButtonTransaksi.tableMode();
            enableForm(false);
            enableSearchEngine(true);
        }
    }

    private Integer totalPage(Integer limitSize) {
        Integer count = Main.getTransaksiService().getTotalSetoranCount(komposisisetoran);
        Float totPage = count.floatValue() / limitSize.floatValue();
        Double hasil = Math.ceil(totPage.doubleValue());
        totalPage = hasil.intValue();
        System.out.println("count = " + count);
        System.out.println("totalPage = " + totalPage);
        return totalPage;
    }

    private Integer currentPages(Integer currPos, Integer limits) {
        page = currPos / limits;
        txtPages.setText(String.valueOf(page));
        return page;
    }

    private void nextPageRecord(Integer last) {
        lastPos = last + limit;
        System.out.println("Last Pos = " + lastPos);
        listSetoranDetail = null;
        if (isChangedPages) {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), lastPos - limit, limit);
        } else {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), currentPos, limit);
        }
        tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
        initColumnSize();
        currentPos = lastPos;
        currentPages(currentPos, limit);
        System.out.println("Current Pos = " + currentPos);
        System.out.println("limit = " + limit);
        System.out.println("list Detail Size= " + listSetoranDetail.size());
    }

    private void prevPageRecord(Integer last) {
        lastPos = last - limit;
        System.out.println("Last Pos = " + lastPos);
        listSetoranDetail = null;
        if (isChangedPages) {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), lastPos - limit, limit);
        } else {
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), lastPos - limit, limit);
        }
        tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
        initColumnSize();
        currentPos = lastPos;
        currentPages(currentPos, limit);
        System.out.println("Current Pos = " + currentPos);
        System.out.println("limit = " + limit);
    }

    /** This method is called from within the constructor to
     * initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is
     * always regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        jScrollPane1 = new javax.swing.JScrollPane();
        jTable1 = new javax.swing.JTable();
        jSplitPane1 = new javax.swing.JSplitPane();
        jSplitPane2 = new javax.swing.JSplitPane();
        jPanel1 = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        txtCariSetoranLambung = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        dateCariSetoran = new com.toedter.calendar.JDateChooser();
        jScrollPane2 = new javax.swing.JScrollPane();
        tblSetoran = new javax.swing.JTable();
        jPanel4 = new javax.swing.JPanel();
        jLabel12 = new javax.swing.JLabel();
        btnOpenClosedTransaction = new javax.swing.JButton();
        jLabel19 = new javax.swing.JLabel();
        txtShowPage = new javax.swing.JTextField();
        btnBack = new javax.swing.JButton();
        btnNext = new javax.swing.JButton();
        txtPages = new javax.swing.JTextField();
        lblTotalHal = new javax.swing.JLabel();
        jPanel2 = new javax.swing.JPanel();
        jPanel5 = new javax.swing.JPanel();
        jLabel13 = new javax.swing.JLabel();
        jLabel3 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel6 = new javax.swing.JLabel();
        jLabel7 = new javax.swing.JLabel();
        jLabel8 = new javax.swing.JLabel();
        jLabel9 = new javax.swing.JLabel();
        txtNoLambung = new javax.swing.JTextField();
        txtNRP = new javax.swing.JTextField();
        txtNama = new javax.swing.JTextField();
        txtSetoranCounter = new javax.swing.JTextField();
        dateJatuhTempo = new com.toedter.calendar.JDateChooser();
        dateSPO = new com.toedter.calendar.JDateChooser();
        jLabel10 = new javax.swing.JLabel();
        jLabel11 = new javax.swing.JLabel();
        jLabel14 = new javax.swing.JLabel();
        jLabel15 = new javax.swing.JLabel();
        jLabel16 = new javax.swing.JLabel();
        jLabel17 = new javax.swing.JLabel();
        txtAngsuran = new javax.swing.JTextField();
        txtTabungan = new javax.swing.JTextField();
        txtKasbon = new javax.swing.JTextField();
        txtBayarKasbon = new javax.swing.JTextField();
        txtOvertime = new javax.swing.JTextField();
        txtCicilan = new javax.swing.JTextField();
        pnlTerimaKasir = new javax.swing.JPanel();
        lblNominal = new javax.swing.JLabel();
        lblRupiahKasir = new javax.swing.JLabel();
        pnlHutang = new javax.swing.JPanel();
        lblHutang = new javax.swing.JLabel();
        lblRupiahHutang = new javax.swing.JLabel();
        txtUbahHutang = new javax.swing.JTextField();
        jScrollPane3 = new javax.swing.JScrollPane();
        txtKeterangan = new javax.swing.JTextArea();
        jLabel18 = new javax.swing.JLabel();
        txtKodeSetoran = new javax.swing.JTextField();
        lblJalanStatus = new javax.swing.JLabel();
        checkJalanStatus = new javax.swing.JCheckBox();
        jSeparator1 = new javax.swing.JSeparator();
        toolbarButtonTransaksi = new mintory.transaksi.toolbarButtonTransaksi();

        jTable1.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "Title 1", "Title 2", "Title 3", "Title 4"
            }
        ));
        jScrollPane1.setViewportView(jTable1);

        setIconifiable(true);
        setMaximizable(true);
        setResizable(true);
        setTitle("Transaksi Setoran - Armada Biru");

        jSplitPane1.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jSplitPane2.setDividerLocation(250);
        jSplitPane2.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

        jLabel1.setText("Cari Setoran Lambung");

        txtCariSetoranLambung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCariSetoranLambungKeyReleased(evt);
            }
        });

        jLabel2.setText("Tgl SPO");

        dateCariSetoran.addPropertyChangeListener(new java.beans.PropertyChangeListener() {
            public void propertyChange(java.beans.PropertyChangeEvent evt) {
                dateCariSetoranPropertyChange(evt);
            }
        });

        tblSetoran.setModel(new javax.swing.table.DefaultTableModel(
            new Object [][] {

            },
            new String [] {
                "  Kode Setoran", "  Setoran Ke", "  No. Lambung", "  Tgl. Jatuh Tempo", "  Tgl. SPO", "  Total Setoran", "  Total Hutang"
            }
        ));
        tblSetoran.setAutoResizeMode(javax.swing.JTable.AUTO_RESIZE_OFF);
        tblSetoran.setSelectionMode(javax.swing.ListSelectionModel.SINGLE_SELECTION);
        jScrollPane2.setViewportView(tblSetoran);
        tblSetoran.getColumnModel().getColumn(0).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(0).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(1).setMinWidth(100);
        tblSetoran.getColumnModel().getColumn(1).setMaxWidth(100);
        tblSetoran.getColumnModel().getColumn(2).setMinWidth(100);
        tblSetoran.getColumnModel().getColumn(2).setMaxWidth(100);
        tblSetoran.getColumnModel().getColumn(3).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(3).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(4).setMinWidth(150);
        tblSetoran.getColumnModel().getColumn(4).setMaxWidth(150);
        tblSetoran.getColumnModel().getColumn(5).setMinWidth(170);
        tblSetoran.getColumnModel().getColumn(5).setMaxWidth(200);
        tblSetoran.getColumnModel().getColumn(6).setMinWidth(170);
        tblSetoran.getColumnModel().getColumn(6).setMaxWidth(200);

        jPanel4.setBackground(new java.awt.Color(0, 73, 255));

        jLabel12.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel12.setForeground(new java.awt.Color(254, 254, 254));
        jLabel12.setText("  Data Setoran");

        javax.swing.GroupLayout jPanel4Layout = new javax.swing.GroupLayout(jPanel4);
        jPanel4.setLayout(jPanel4Layout);
        jPanel4Layout.setHorizontalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel4Layout.createSequentialGroup()
                .addComponent(jLabel12)
                .addContainerGap(1011, Short.MAX_VALUE))
        );
        jPanel4Layout.setVerticalGroup(
            jPanel4Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel12, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        btnOpenClosedTransaction.setText("Buka Closed Transaksi");
        btnOpenClosedTransaction.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnOpenClosedTransactionActionPerformed(evt);
            }
        });

        jLabel19.setText("Tampil");

        txtShowPage.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtShowPageKeyReleased(evt);
            }
        });

        btnBack.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/PHBACK.png"))); // NOI18N
        btnBack.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnBackActionPerformed(evt);
            }
        });

        btnNext.setIcon(new javax.swing.ImageIcon(getClass().getResource("/paperman/images/PHNEXT.png"))); // NOI18N
        btnNext.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                btnNextActionPerformed(evt);
            }
        });

        txtPages.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtPagesKeyReleased(evt);
            }
        });

        lblTotalHal.setText("Total 0 halaman ");

        javax.swing.GroupLayout jPanel1Layout = new javax.swing.GroupLayout(jPanel1);
        jPanel1.setLayout(jPanel1Layout);
        jPanel1Layout.setHorizontalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jPanel4, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addContainerGap()
                .addComponent(jLabel1)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.UNRELATED)
                .addComponent(txtCariSetoranLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 94, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(18, 18, 18)
                .addComponent(jLabel2)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(dateCariSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, 129, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED, 13, Short.MAX_VALUE)
                .addComponent(jLabel19)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtShowPage, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(txtPages, javax.swing.GroupLayout.PREFERRED_SIZE, 35, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 10, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblTotalHal)
                .addGap(66, 66, 66)
                .addComponent(btnOpenClosedTransaction)
                .addGap(202, 202, 202))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 1014, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(88, Short.MAX_VALUE))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {btnBack, btnNext});

        jPanel1Layout.setVerticalGroup(
            jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addComponent(jPanel4, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(1, 1, 1)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel19, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtShowPage, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(btnBack, javax.swing.GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(txtPages, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(btnNext, javax.swing.GroupLayout.PREFERRED_SIZE, 26, Short.MAX_VALUE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(jScrollPane2, javax.swing.GroupLayout.PREFERRED_SIZE, 189, javax.swing.GroupLayout.PREFERRED_SIZE))
            .addGroup(jPanel1Layout.createSequentialGroup()
                .addGap(31, 31, 31)
                .addComponent(lblTotalHal, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(195, Short.MAX_VALUE))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addGroup(jPanel1Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addComponent(dateCariSetoran, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(txtCariSetoranLambung, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                    .addComponent(jLabel2, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(jLabel1, javax.swing.GroupLayout.PREFERRED_SIZE, 26, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(195, 195, 195))
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, jPanel1Layout.createSequentialGroup()
                .addContainerGap(31, Short.MAX_VALUE)
                .addComponent(btnOpenClosedTransaction, javax.swing.GroupLayout.DEFAULT_SIZE, 26, Short.MAX_VALUE)
                .addGap(195, 195, 195))
        );

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dateCariSetoran, txtCariSetoranLambung, txtPages, txtShowPage});

        jPanel1Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {btnBack, btnNext, jLabel1, jLabel19, jLabel2, lblTotalHal});

        jSplitPane2.setLeftComponent(jPanel1);

        jPanel5.setBackground(new java.awt.Color(0, 73, 255));

        jLabel13.setFont(new java.awt.Font("BPG Chveulebrivi", 1, 13));
        jLabel13.setForeground(new java.awt.Color(254, 254, 254));
        jLabel13.setText("  Input Setoran");

        javax.swing.GroupLayout jPanel5Layout = new javax.swing.GroupLayout(jPanel5);
        jPanel5.setLayout(jPanel5Layout);
        jPanel5Layout.setHorizontalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel5Layout.createSequentialGroup()
                .addComponent(jLabel13)
                .addContainerGap(1008, Short.MAX_VALUE))
        );
        jPanel5Layout.setVerticalGroup(
            jPanel5Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jLabel13, javax.swing.GroupLayout.DEFAULT_SIZE, 30, Short.MAX_VALUE)
        );

        jLabel3.setText("No. Lambung");

        jLabel4.setText("NRP");

        jLabel5.setText("Nama");

        jLabel6.setText("Setoran Ke");

        jLabel7.setText("Jatuh Tempo");

        jLabel8.setText("Tgl. SPO");

        jLabel9.setText("Keterangan");

        txtNoLambung.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtNoLambungMouseClicked(evt);
            }
        });
        txtNoLambung.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtNoLambungKeyReleased(evt);
            }
        });

        jLabel10.setText("Angsuran Setoran");

        jLabel11.setText("Tabungan");

        jLabel14.setText("Kas Bon");

        jLabel15.setText("Pembayaran Kas Bon");

        jLabel16.setText("Over Time");

        jLabel17.setText("Cicilan KS/BS/KB");

        txtAngsuran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtAngsuranMouseClicked(evt);
            }
        });

        txtTabungan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtTabunganMouseClicked(evt);
            }
        });

        txtKasbon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKasbonMouseClicked(evt);
            }
        });
        txtKasbon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtKasbonKeyReleased(evt);
            }
        });

        txtBayarKasbon.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtBayarKasbonMouseClicked(evt);
            }
        });
        txtBayarKasbon.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtBayarKasbonKeyReleased(evt);
            }
        });

        txtOvertime.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtOvertimeMouseClicked(evt);
            }
        });

        txtCicilan.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtCicilanMouseClicked(evt);
            }
        });
        txtCicilan.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyReleased(java.awt.event.KeyEvent evt) {
                txtCicilanKeyReleased(evt);
            }
        });

        pnlTerimaKasir.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Terima Kasir", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        lblNominal.setFont(new java.awt.Font("Tahoma", 1, 24));
        lblNominal.setText("Nominal");
        lblNominal.setAutoscrolls(true);

        lblRupiahKasir.setText("Rupiah");

        javax.swing.GroupLayout pnlTerimaKasirLayout = new javax.swing.GroupLayout(pnlTerimaKasir);
        pnlTerimaKasir.setLayout(pnlTerimaKasirLayout);
        pnlTerimaKasirLayout.setHorizontalGroup(
            pnlTerimaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(javax.swing.GroupLayout.Alignment.TRAILING, pnlTerimaKasirLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlTerimaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(lblNominal, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .addComponent(lblRupiahKasir, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE))
                .addContainerGap())
        );
        pnlTerimaKasirLayout.setVerticalGroup(
            pnlTerimaKasirLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlTerimaKasirLayout.createSequentialGroup()
                .addComponent(lblNominal, javax.swing.GroupLayout.PREFERRED_SIZE, 54, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addComponent(lblRupiahKasir, javax.swing.GroupLayout.PREFERRED_SIZE, 34, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
        );

        pnlHutang.setBorder(javax.swing.BorderFactory.createTitledBorder(javax.swing.BorderFactory.createEtchedBorder(javax.swing.border.EtchedBorder.RAISED), "Hutang Hari Ini", javax.swing.border.TitledBorder.DEFAULT_JUSTIFICATION, javax.swing.border.TitledBorder.DEFAULT_POSITION, new java.awt.Font("Dialog", 1, 14))); // NOI18N

        lblHutang.setFont(new java.awt.Font("Tahoma", 1, 24));
        lblHutang.setText("Nominal");
        lblHutang.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                lblHutangMouseClicked(evt);
            }
        });

        lblRupiahHutang.setText("Rupiah");

        txtUbahHutang.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyPressed(java.awt.event.KeyEvent evt) {
                txtUbahHutangKeyPressed(evt);
            }
        });

        javax.swing.GroupLayout pnlHutangLayout = new javax.swing.GroupLayout(pnlHutang);
        pnlHutang.setLayout(pnlHutangLayout);
        pnlHutangLayout.setHorizontalGroup(
            pnlHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHutangLayout.createSequentialGroup()
                .addContainerGap()
                .addGroup(pnlHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(lblRupiahHutang, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .addComponent(txtUbahHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 312, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addGroup(pnlHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHutangLayout.createSequentialGroup()
                    .addContainerGap()
                    .addComponent(lblHutang, javax.swing.GroupLayout.DEFAULT_SIZE, 378, Short.MAX_VALUE)
                    .addContainerGap()))
        );
        pnlHutangLayout.setVerticalGroup(
            pnlHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(pnlHutangLayout.createSequentialGroup()
                .addGap(12, 12, 12)
                .addComponent(txtUbahHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 31, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addGap(33, 33, 33)
                .addComponent(lblRupiahHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 27, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addContainerGap(13, Short.MAX_VALUE))
            .addGroup(pnlHutangLayout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                .addGroup(pnlHutangLayout.createSequentialGroup()
                    .addComponent(lblHutang, javax.swing.GroupLayout.PREFERRED_SIZE, 48, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addContainerGap(68, Short.MAX_VALUE)))
        );

        txtKeterangan.setColumns(20);
        txtKeterangan.setRows(5);
        jScrollPane3.setViewportView(txtKeterangan);

        jLabel18.setText("Kode Setoran");

        txtKodeSetoran.addMouseListener(new java.awt.event.MouseAdapter() {
            public void mouseClicked(java.awt.event.MouseEvent evt) {
                txtKodeSetoranMouseClicked(evt);
            }
        });

        lblJalanStatus.setText("Jalan Status :");

        checkJalanStatus.setText("Tidak/Rusak");
        checkJalanStatus.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                checkJalanStatusActionPerformed(evt);
            }
        });

        jSeparator1.setForeground(new java.awt.Color(0, 0, 0));
        jSeparator1.setOrientation(javax.swing.SwingConstants.VERTICAL);

        javax.swing.GroupLayout jPanel2Layout = new javax.swing.GroupLayout(jPanel2);
        jPanel2.setLayout(jPanel2Layout);
        jPanel2Layout.setHorizontalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addContainerGap()
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel3)
                    .addComponent(jLabel4)
                    .addComponent(jLabel5)
                    .addComponent(jLabel6)
                    .addComponent(jLabel7)
                    .addComponent(jLabel9)
                    .addComponent(jLabel8))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, 83, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addGap(6, 6, 6)
                        .addComponent(lblJalanStatus)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(checkJalanStatus))
                    .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, 177, javax.swing.GroupLayout.PREFERRED_SIZE)
                    .addComponent(txtNama, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(txtSetoranCounter, javax.swing.GroupLayout.DEFAULT_SIZE, 240, Short.MAX_VALUE)
                    .addComponent(dateJatuhTempo, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(dateSPO, javax.swing.GroupLayout.DEFAULT_SIZE, 207, Short.MAX_VALUE)
                    .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 240, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addGap(18, 18, 18)
                .addComponent(jSeparator1, javax.swing.GroupLayout.PREFERRED_SIZE, 8, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addComponent(jLabel17)
                    .addComponent(jLabel16)
                    .addComponent(jLabel15)
                    .addComponent(jLabel14)
                    .addComponent(jLabel11)
                    .addComponent(jLabel10)
                    .addComponent(jLabel18))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING, false)
                    .addComponent(txtKodeSetoran)
                    .addComponent(txtAngsuran)
                    .addComponent(txtTabungan)
                    .addComponent(txtKasbon)
                    .addComponent(txtBayarKasbon)
                    .addComponent(txtOvertime, javax.swing.GroupLayout.DEFAULT_SIZE, 217, Short.MAX_VALUE)
                    .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, 217, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.TRAILING)
                    .addComponent(pnlTerimaKasir, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                    .addComponent(pnlHutang, javax.swing.GroupLayout.Alignment.LEADING, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                .addContainerGap())
            .addComponent(jPanel5, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {txtNama, txtSetoranCounter});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {dateJatuhTempo, dateSPO});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.HORIZONTAL, new java.awt.Component[] {pnlHutang, pnlTerimaKasir});

        jPanel2Layout.setVerticalGroup(
            jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(jPanel2Layout.createSequentialGroup()
                .addComponent(jPanel5, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addComponent(pnlTerimaKasir, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addComponent(pnlHutang, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(jLabel3)
                            .addComponent(txtNoLambung, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(lblJalanStatus)
                            .addComponent(checkJalanStatus))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNRP, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel4))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtNama, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel5))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtSetoranCounter, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel6))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(jLabel7, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(dateJatuhTempo, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE))
                        .addGap(12, 12, 12)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING, false)
                            .addComponent(dateSPO, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE)
                            .addComponent(jLabel8, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, Short.MAX_VALUE))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
                            .addComponent(jScrollPane3, javax.swing.GroupLayout.PREFERRED_SIZE, 106, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel9)))
                    .addGroup(jPanel2Layout.createSequentialGroup()
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKodeSetoran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel18))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtAngsuran, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel10))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtTabungan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel11))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel14))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtBayarKasbon, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel15))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtOvertime, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel16))
                        .addPreferredGap(javax.swing.LayoutStyle.ComponentPlacement.RELATED)
                        .addGroup(jPanel2Layout.createParallelGroup(javax.swing.GroupLayout.Alignment.BASELINE)
                            .addComponent(txtCicilan, javax.swing.GroupLayout.PREFERRED_SIZE, javax.swing.GroupLayout.DEFAULT_SIZE, javax.swing.GroupLayout.PREFERRED_SIZE)
                            .addComponent(jLabel17)))
                    .addComponent(jSeparator1, javax.swing.GroupLayout.DEFAULT_SIZE, 314, Short.MAX_VALUE))
                .addContainerGap())
        );

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {txtNRP, txtNama, txtNoLambung, txtSetoranCounter});

        jPanel2Layout.linkSize(javax.swing.SwingConstants.VERTICAL, new java.awt.Component[] {dateJatuhTempo, dateSPO});

        jSplitPane2.setRightComponent(jPanel2);

        jSplitPane1.setRightComponent(jSplitPane2);

        toolbarButtonTransaksi.setBorder(new javax.swing.border.SoftBevelBorder(javax.swing.border.BevelBorder.RAISED));
        jSplitPane1.setLeftComponent(toolbarButtonTransaksi);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.DEFAULT_SIZE, 1106, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(jSplitPane1, javax.swing.GroupLayout.Alignment.TRAILING, javax.swing.GroupLayout.DEFAULT_SIZE, 674, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void txtNoLambungKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtNoLambungKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER || evt.getKeyChar() == '\n') {
            drive = new pengemudi();
            kend = new kendaraan();
            Date spo = new Date();
            int i = 0;
            int a = 0;
            checkJalanStatus.setVisible(true);
            checkJalanStatus.setEnabled(true);
            lblJalanStatus.setVisible(true);
            lblJalanStatus.setEnabled(true);
            for (; i < listPengemudi.size(); i++) {
                if (listPengemudi.get(i).getKend().getNoLambung().toString().startsWith(txtNoLambung.getText())) {
                    txtNRP.setText(listPengemudi.get(i).getNrp());
                    txtNama.setText(listPengemudi.get(i).getNama());
                    txtKeterangan.setText(listPengemudi.get(i).getKend().getKeterangan());
                    drive.setId(listPengemudi.get(i).getId());
                    kend.setId(listPengemudi.get(i).getKend().getId());
                    if (!listSetoran.isEmpty()) {
                        if (Main.getSistemService().findBonusBulanan(komposisisetoran.getNamaKomposisi()) != null) {
                            ListStoDetail = Main.getTransaksiService().findLastSetoranDetailByLambung(new Integer(txtNoLambung.getText()));
                        } else {
                            ListStoDetail = Main.getTransaksiService().findSetoranDetailByLambung(new Integer(txtNoLambung.getText()), Main.getTransaksiService().findLastSetoranDetailByLambung(new Integer(txtNoLambung.getText())).get(0).getSetor_map().getTglSPO());
                        }
                        System.out.println("Setoran Found = " + ListStoDetail.size());
                        spo = Main.getTransaksiService().getLatestSetoranCount().get(0).getTglSPO();
                        List<setoran> listSTO = Main.getTransaksiService().findLastTglJatuhTempo(JalanStatus.J);
                        if (!listSTO.isEmpty()) {
                            dateJatuhTempo.setDate(new DateTime(listSTO.get(0).getTglJatuhTempo().getTime()).plusDays(1).toDate());
                            dateSPO.setDate(sys.getTglKerja());
                        } else {
                            if (listSetoran.isEmpty()) {
                                dateJatuhTempo.setDate(listPengemudi.get(i).getKend().getTglJatuhTempo());
                            } else {
                                dateJatuhTempo.setDate(Main.getTransaksiService().findLastTglJatuhTempo(JalanStatus.M).get(0).getTglJatuhTempo());
                            }
                            dateSPO.setDate(sys.getTglKerja());
                        }
                    } else {
                        dateJatuhTempo.setDate(listPengemudi.get(i).getKend().getTglJatuhTempo());
                        dateSPO.setDate(sys.getTglKerja());
                    }
                    if (listClosingBulananSaldoAwal.isEmpty()) {
                        if (!listSetoran.isEmpty()) {
                            if (!ListStoDetail.isEmpty()) {
                                setoranCounter = ListStoDetail.get(0).getSetor_map().getCounter_setoran();
                                setoranCounter++;
                                txtSetoranCounter.setText(String.valueOf(setoranCounter));
                            } else {
                                setoranCounter = 1;
                                txtSetoranCounter.setText(String.valueOf(setoranCounter));
                            }
                        } else {
                            setoranCounter = 1;
                            txtSetoranCounter.setText(String.valueOf(setoranCounter));
                        }
                    } else {
                        for (; a < listClosingBulananSaldoAwal.size(); a++) {
                            if (listClosingBulananSaldoAwal.get(a).getActClosing().equals(aksiClosing.DO_CLOSING_SALDO_AWAL)) {
                                if (listClosingBulananSaldoAwal.get(a).getRefNoLambung().compareTo(new Integer(txtNoLambung.getText())) == 0) {
                                    refNoLambung = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja());
                                    if (refNoLambung != null) {
                                        setoranCounter = refNoLambung.getRefSetoranKe();
                                        setoranCounter++;
                                        txtSetoranCounter.setText(String.valueOf(setoranCounter));
                                        break;
                                    } else {
                                        if (!listSetoran.isEmpty()) {
                                            if (!ListStoDetail.isEmpty()) {
                                                setoranCounter = ListStoDetail.get(0).getSetor_map().getCounter_setoran();
                                                setoranCounter++;
                                                txtSetoranCounter.setText(String.valueOf(setoranCounter));
                                            } else {
                                                setoranCounter = 1;
                                                txtSetoranCounter.setText(String.valueOf(setoranCounter));
                                            }
                                        } else {
                                            txtSetoranCounter.setText("1");
                                        }
                                        break;
                                    }
                                }
                            } else {
                                if (!listSetoran.isEmpty()) {
                                    if (!ListStoDetail.isEmpty()) {
                                        setoranCounter = ListStoDetail.get(0).getSetor_map().getCounter_setoran();
                                        setoranCounter++;
                                        txtSetoranCounter.setText(String.valueOf(setoranCounter));
                                    } else {
                                        setoranCounter = 1;
                                        txtSetoranCounter.setText(String.valueOf(setoranCounter));
                                    }
                                } else {
                                    txtSetoranCounter.setText("1");
                                }
                            }
                        }
                    }
                }
            }

            if (!listSetoran.isEmpty() && !listClosingBulananSaldoAwal.isEmpty()) {
                BigDecimal tKasbon = BigDecimal.ZERO;
                BigDecimal tBayar = BigDecimal.ZERO;
                BigDecimal tCicilan = BigDecimal.ZERO;
                if (!ListStoDetail.isEmpty()) {
                    if (Main.getTransaksiService().closingBulananRecordForSaldoAwal(komposisisetoran, sys.getTglKerja()).isEmpty()) {
                        tKasbon = Main.getTransaksiService().sumKasbon(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findNewClosingByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalKas());
                        System.out.println("sumKasbon " + spo.getDate() + " = " + Main.getTransaksiService().sumKasbon(new Integer(txtNoLambung.getText()), spo, spo).toString());
                        System.out.println("tKasbon= " + tKasbon);
                        System.out.println("Date First =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMinimumValue()).toDate());
                        System.out.println("Date End =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMaximumValue()).toDate());
                        tBayar = Main.getTransaksiService().sumBayarKasbon(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findNewClosingByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalBayarKas());
                        System.out.println("tBayar= " + tBayar);
                        tCicilan = Main.getTransaksiService().sumCicilan(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findNewClosingByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalCicilan());
                        System.out.println("tCicilan= " + tCicilan);
                    } else {
                        tKasbon = Main.getTransaksiService().sumKasbon(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalKas());
                        System.out.println("sumKasbon " + spo.getDate() + " = " + Main.getTransaksiService().sumKasbon(new Integer(txtNoLambung.getText()), spo, spo).toString());
                        System.out.println("tKasbon= " + tKasbon);
                        System.out.println("Date First =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMinimumValue()).toDate());
                        System.out.println("Date End =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMaximumValue()).toDate());
                        tBayar = Main.getTransaksiService().sumBayarKasbon(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalBayarKas());
                        System.out.println("tBayar= " + tBayar);
                        tCicilan = Main.getTransaksiService().sumCicilan(new Integer(txtNoLambung.getText()), spo, spo).add(Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), new DateTime(sys.getTglKerja()).minusMonths(1).toDate()).getTotalCicilan());
                        System.out.println("tCicilan= " + tCicilan);
                    }
                    hutang = tKasbon.subtract(tBayar).subtract(tCicilan);
                    System.out.println("Hutang= " + hutang);
//                sumSetoran = Main.getTransaksiService().sumSetoran(new Integer(txtNoLambung.getText()));
                    if (hutang != null && hutang.compareTo(BigDecimal.ZERO) <= 0) {
                        hutangToday = BigDecimal.ZERO;
                        System.out.println("Hutang compare = " + hutang);
                    } else if (hutang != null && hutang.compareTo(BigDecimal.ZERO) > 0) {
//                    hutangToday = sumSetoran.subtract(hutang);
                        hutangToday = hutang;
                        if (hutangToday.signum() == -1) {
                            hutangToday = hutangToday.negate();
                            System.out.println("hutang today= " + hutangToday.signum());
                        }
                    } else {
                        hutang = new BigDecimal(0);
                        hutangToday = new BigDecimal(0);
                    }

                    System.out.println("Hutang = " + hutang);
                    System.out.println("SumSetoran = " + sumSetoran);
                    System.out.println("Total Hutang Hari Ini = " + hutangToday);
                    lblHutang.setText(rupiahFormatter.format(hutangToday));
                    lblRupiahHutang.setText("Total Yang Harus Di Bayar = " + rupiahFormatter.format(hutangToday.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()))));
                } else {
                    tKasbon = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalKas();
                    System.out.println("tKasbon= " + tKasbon);
                    System.out.println("Date First =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMinimumValue()).toDate());
                    System.out.println("Date End =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMaximumValue()).toDate());
                    tBayar = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalBayarKas();
                    System.out.println("tBayar= " + tBayar);
                    tCicilan = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalCicilan();
                    System.out.println("tCicilan= " + tCicilan);
                    hutang = tKasbon.subtract(tBayar).subtract(tCicilan);
                    System.out.println("Hutang= " + hutang);
//                sumSetoran = Main.getTransaksiService().sumSetoran(new Integer(txtNoLambung.getText()));
                    if (hutang != null && hutang.compareTo(BigDecimal.ZERO) <= 0) {
                        hutangToday = BigDecimal.ZERO;
                        System.out.println("Hutang compare = " + hutang);
                    } else if (hutang != null && hutang.compareTo(BigDecimal.ZERO) > 0) {
//                    hutangToday = sumSetoran.subtract(hutang);
                        hutangToday = hutang;
                        if (hutangToday.signum() == -1) {
                            hutangToday = hutangToday.negate();
                            System.out.println("hutang today= " + hutangToday.signum());
                        }
                    } else {
                        hutang = new BigDecimal(0);
                        hutangToday = new BigDecimal(0);
                    }

                    System.out.println("Hutang = " + hutang);
                    System.out.println("SumSetoran = " + sumSetoran);
                    System.out.println("Total Hutang Hari Ini = " + hutangToday);
                    lblHutang.setText(rupiahFormatter.format(hutangToday));
                    lblRupiahHutang.setText("Total Yang Harus Di Bayar = " + rupiahFormatter.format(hutangToday.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()))));
                }
            } else if (!listSetoran.isEmpty() && listClosingBulananSaldoAwal.isEmpty()) {
                hutang = Main.getTransaksiService().sumHutang(new Integer(txtNoLambung.getText()), TransaksiStatus.U);
//                sumSetoran = Main.getTransaksiService().sumSetoran(new Integer(txtNoLambung.getText()));
                if (hutang != null && hutang.compareTo(BigDecimal.ZERO) <= 0) {
                    hutangToday = BigDecimal.ZERO;
                    System.out.println("Hutang compare = " + hutang);
                } else if (hutang != null && hutang.compareTo(BigDecimal.ZERO) > 0) {
//                    hutangToday = sumSetoran.subtract(hutang);
                    hutangToday = hutang;
                    if (hutangToday.signum() == -1) {
                        hutangToday = hutangToday.negate();
                        System.out.println("hutang today= " + hutangToday.signum());
                    }
                } else {
                    hutang = new BigDecimal(0);
                    hutangToday = new BigDecimal(0);
                }

                System.out.println("Hutang = " + hutang);
                System.out.println("SumSetoran = " + sumSetoran);
                System.out.println("Total Hutang Hari Ini = " + hutangToday);
                lblHutang.setText(rupiahFormatter.format(hutangToday));
                lblRupiahHutang.setText("Total Yang Harus Di Bayar = " + rupiahFormatter.format(hutangToday.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()))));
            } else if (listSetoran.isEmpty() && !listClosingBulananSaldoAwal.isEmpty()) {
                BigDecimal tKasbon = BigDecimal.ZERO;
                BigDecimal tBayar = BigDecimal.ZERO;
                BigDecimal tCicilan = BigDecimal.ZERO;
                tKasbon = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalKas();
                System.out.println("tKasbon= " + tKasbon);
                System.out.println("Date First =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMinimumValue()).toDate());
                System.out.println("Date End =" + new DateTime(dateSPO.getDate()).withDayOfMonth(new DateTime(dateSPO.getDate()).dayOfMonth().getMaximumValue()).toDate());
                tBayar = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalBayarKas();
                System.out.println("tBayar= " + tBayar);
                tCicilan = Main.getTransaksiService().findClosingSaldoAwalByRefNoLambung(new Integer(txtNoLambung.getText()), sys.getTglKerja()).getTotalCicilan();
                System.out.println("tCicilan= " + tCicilan);
                hutangToday = tKasbon.subtract(tBayar).subtract(tCicilan);
                if (hutangToday.signum() == -1) {
                    hutangToday = hutangToday.negate();
                    System.out.println("hutang today= " + hutangToday.signum());
                }
                lblHutang.setText(rupiahFormatter.format(hutangToday));
                lblRupiahHutang.setText("Total Yang Harus Di Bayar = " + rupiahFormatter.format(hutangToday.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()))));
            } else {
                hutangToday = BigDecimal.ZERO;
            }
        }
        if (txtNoLambung.getText().isEmpty()) {
            txtNRP.setText("");
            txtNama.setText("");
            txtSetoranCounter.setText("");
            dateJatuhTempo.setDate(null);
            dateSPO.setDate(null);
            txtKeterangan.setText("");
            checkJalanStatus.setSelected(false);
            checkJalanStatus.setText("Tidak/Rusak");
            checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.PLAIN, 13));
            return;
        }
    }//GEN-LAST:event_txtNoLambungKeyReleased

    private void txtCicilanKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCicilanKeyReleased
        // TODO add your handling code here:

        // ------- SAAT KONDISI MANUAL YANG BERARTI KOMPONEN BAYAR KASBON, OVERTIME, DAN CICILAN/KS/KB DITOTAL SEMUA, DENGAN KONDISI KASBON MANUAL, DEFAULT KASBON : HASIL SUM ANGSURAN DAN TABUNGAN ------ //
        if (evt.isControlDown()) {
            BigDecimal totalManual = new BigDecimal(0);
            BigDecimal totalAngsuran = new BigDecimal(0);
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                if (txtKasbon.getText().equalsIgnoreCase(TextComponentUtils.formatNumber(totalAngsuran))) {
                    totalManual = totalManual.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
                } else if (txtKasbon.getText().equalsIgnoreCase("0")) {
                    totalManual = totalManual.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText())).add(totalAngsuran);
                }
                lblNominal.setText(rupiahFormatter.format(totalManual));
            }
        } else {
            // ---------- KEADAAN BERHUTANG ---------------//
            if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
                BigDecimal totalKasbon = new BigDecimal(0);
                BigDecimal totalAngsuran = new BigDecimal(0);
                BigDecimal totalHutang = new BigDecimal(0);
                BigDecimal totalSurplus = new BigDecimal(0);
                BigDecimal mustPaid = new BigDecimal(0);
                if (isEditFire) {
                    listSetoran = Main.getTransaksiService().findAllAvailableSetoranByLambung(new Integer(txtNoLambung.getText()), dateSPO.getDate(), isClosedStatus.AVAILABLE);
                    hutangToday = TextComponentUtils.parseNumberToBigDecimal(lblHutang.getText().substring(3));
                }
                if (sto == null) {
                    sto = new setoran();
                    stoDet = new setoranDetail();
                }

                // ------- SAAT JUMLAH TRANSAKSI TIDAK KOSONG DAN HUTANG HARI INI LEBIH DARI 0 ------ //
                if (!listSetoran.isEmpty() && hutangToday.compareTo(BigDecimal.ZERO) > 0) {

                    // ----- CEK APAKAH KOMPONEN HUTANG(KASBON, BAYAR KASBON, OVERTIME, CICILAN) TIDAK KOSONG -------- //
                    if (validateHutang()) {
//            if (lblNominal.getSize().width > labelOverSize) {
//                lblNominal.setFont(new Font("BPG Chveulebrivi", Font.BOLD, 18));
//                lblNominal.repaint();
//                pnlTerimaKasir.updateUI();
//
//            } else {
//                lblNominal.setFont(new Font("BPG Chveulebrivi", Font.BOLD, 24));
//                lblNominal.repaint();
//                pnlTerimaKasir.updateUI();
//            }
//            sto = Main.getTransaksiService().findByNoLambung(new Integer(txtNoLambung.getText()));
                        // -- BILA SEDANG UPDATE TRANSAKSI -- //
                        if (isEditFire) {
                            totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                            mustPaid = hutangToday;
                            System.out.println("must Paid=" + mustPaid);
                            totalKasbon = totalKasbon.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
                            totalHutang = totalHutang.add(totalKasbon.subtract(mustPaid));
                            System.out.println("total kasbon= " + totalKasbon);
                            System.out.println("total hutang= " + totalHutang);
                            if (totalHutang.signum() == -1) {
                                totalHutang = totalHutang.negate();
                            }
                            if (totalHutang.compareTo(BigDecimal.ZERO) == 0) {
                                if (totalAngsuran.add(totalKasbon).compareTo(mustPaid) > 0) {
                                    totalSurplus = totalSurplus.add(totalKasbon).subtract(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
                                    lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
                                    lblRupiahKasir.setVisible(true);
                                    txtKasbon.setText("0");
                                    lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
                                    sto.setPayedStatus(TransaksiStatus.L);
                                } else {
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon));
                                    lblHutang.setText(rupiahFormatter.format(totalHutang));
                                    txtKasbon.setText("0");
                                    sto.setPayedStatus(TransaksiStatus.L);
                                    System.out.println("payed = " + sto.getPayedStatus());
                                }
                            }

                            if (totalHutang.compareTo(BigDecimal.ZERO) > 0) {
                                if (totalAngsuran.add(totalKasbon).compareTo(mustPaid) > 0) {
                                    totalSurplus = totalSurplus.add(totalAngsuran.add(totalKasbon).subtract(mustPaid));
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
                                    lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
                                    lblRupiahKasir.setVisible(true);
                                    txtKasbon.setText("0");
                                    lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
                                    sto.setPayedStatus(TransaksiStatus.L);
                                } else {
//                        if (totalKasbon.compareTo(mustPaid) > 0) {
//                            JOptionPane.showMessageDialog(this, "Pembayaran Tidak Boleh Lebih Besar Dari Jumlah Yang Harus Dibayar = " + mustPaid, "Pesan Sistem", JOptionPane.WARNING_MESSAGE);
//                            txtBayarKasbon.requestFocus();
//                        } else {
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon));
                                    lblHutang.setText(rupiahFormatter.format(totalHutang));
                                    txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
                                    sto.setPayedStatus(TransaksiStatus.U);
                                    System.out.println("payed = " + sto.getPayedStatus());
                                }
                            }
                            // -- BILA TIDAK SEDANG UPDATE TRANSAKSI / KEADAAN MENAMBAH TRANSAKSI BARU -- //
                        } else {
                            totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                            mustPaid = hutangToday.add(totalAngsuran);
                            System.out.println("must Paid=" + mustPaid);
                            totalKasbon = totalKasbon.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
                            if (txtKasbon.getText().equalsIgnoreCase("0")) {
                                totalHutang = totalHutang.add(totalAngsuran.add(totalKasbon)).subtract(mustPaid);
                            } else if (txtKasbon.getText().isEmpty() || txtKasbon.getText().compareTo(TextComponentUtils.formatNumber(totalAngsuran)) == 0) {
                                totalHutang = totalHutang.add(totalKasbon.subtract(mustPaid));
                            }
                            System.out.println("total angsuran = " + totalAngsuran);
                            System.out.println("total kasbon = " + totalKasbon);
                            System.out.println("total hutang = " + totalHutang);
                            if (totalHutang.signum() == -1) {
                                totalHutang = totalHutang.negate();
                            }
                            if (totalHutang.compareTo(BigDecimal.ZERO) == 0) {
                                if (totalAngsuran.add(totalKasbon).compareTo(mustPaid) > 0) {
                                    totalSurplus = totalSurplus.add(totalAngsuran.add(totalKasbon).subtract(mustPaid));
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
                                    lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
                                    lblRupiahKasir.setVisible(true);
                                    txtKasbon.setText("0");
                                    lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
                                    sto.setPayedStatus(TransaksiStatus.L);
                                } else {
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
                                    lblHutang.setText(rupiahFormatter.format(totalHutang));
                                    txtKasbon.setText("0");
                                    sto.setPayedStatus(TransaksiStatus.L);
                                    System.out.println("payed = " + sto.getPayedStatus());
                                }

                            }
                            if (totalHutang.compareTo(BigDecimal.ZERO) > 0) {
                                if (totalAngsuran.add(totalKasbon).compareTo(mustPaid) > 0) {
                                    totalSurplus = totalSurplus.add(totalAngsuran.add(totalKasbon).subtract(mustPaid));
                                    lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
                                    lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
                                    lblRupiahKasir.setVisible(true);
                                    txtKasbon.setText("0");
                                    lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
                                    sto.setPayedStatus(TransaksiStatus.L);
                                } else {
                                    if (txtKasbon.getText().equalsIgnoreCase("0")
                                            && txtBayarKasbon.getText().equalsIgnoreCase("0")
                                            && txtOvertime.getText().equalsIgnoreCase("0")
                                            && txtCicilan.getText().equalsIgnoreCase("0")) {
                                        lblNominal.setText(rupiahFormatter.format(totalAngsuran));
                                        lblHutang.setText(rupiahFormatter.format(totalHutang));
                                        sto.setPayedStatus(TransaksiStatus.U);
                                    } else {
//                        if (totalKasbon.compareTo(mustPaid) > 0) {
//                            JOptionPane.showMessageDialog(this, "Pembayaran Tidak Boleh Lebih Besar Dari Jumlah Yang Harus Dibayar = " + mustPaid, "Pesan Sistem", JOptionPane.WARNING_MESSAGE);
//                            txtBayarKasbon.requestFocus();
//                        } else {
                                        lblNominal.setText(rupiahFormatter.format(totalKasbon));
                                        lblHutang.setText(rupiahFormatter.format(totalHutang));
                                        txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
                                        sto.setPayedStatus(TransaksiStatus.U);
                                        System.out.println("payed = " + sto.getPayedStatus());
                                    }
                                }

//                            } else {
//                        if (totalKasbon.compareTo(mustPaid) > 0) {
//                            JOptionPane.showMessageDialog(this, "Pembayaran Tidak Boleh Lebih Besar Dari Jumlah Yang Harus Dibayar = " + mustPaid, "Pesan Sistem", JOptionPane.WARNING_MESSAGE);
//                            txtBayarKasbon.requestFocus();
//                        } else {
//                                lblNominal.setText(rupiahFormatter.format(totalKasbon));
//                                lblHutang.setText(rupiahFormatter.format(totalHutang));
//                                txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
//                                sto.setPayedStatus(TransaksiStatus.U);
//                                System.out.println("payed = " + sto.getPayedStatus());
                            }

                        }
//                    sto.setJalanStatus(JalanStatus.J);
                        System.out.println("total angsuran = " + totalAngsuran);
                        System.out.println("total kasbon = " + totalKasbon);
                        System.out.println("hutang hari ini = " + hutangToday);
                        System.out.println("total hutang =" + totalHutang);

                        // ----- APABILA KOMPONEN HUTANG KOSONG ----- //
                    } else {
                        totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                        txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
                        lblNominal.setText(rupiahFormatter.format(BigDecimal.ZERO));
                        totalKasbon = totalAngsuran;
                        totalHutang = totalHutang.add(totalKasbon.add(hutangToday));
                        lblHutang.setText(rupiahFormatter.format(totalHutang));
                        sto.setPayedStatus(TransaksiStatus.U);
//                    sto.setJalanStatus(JalanStatus.M);
                        System.out.println("payed status = " + sto.getPayedStatus());
                        txtBayarKasbon.setText("0");
                        txtOvertime.setText("0");
                        txtCicilan.setText("0");
                    }

                    // ------- SAAT JUMLAH TRANSAKSI KOSONG DAN HUTANG HARI INI LEBIH DARI 0 ------ //
//                } else if (listSetoran.isEmpty() && hutangToday.compareTo(BigDecimal.ZERO) > 0) {
//                    totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
//                    totalKasbon = hutangToday.add(totalAngsuran);
//                    lblHutang.setText(rupiahFormatter.format(totalKasbon));
//                    lblNominal.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                    txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
//                    txtBayarKasbon.setText("0");
//                    txtOvertime.setText("0");
//                    txtCicilan.setText("0");
//                    sto.setPayedStatus(TransaksiStatus.U);

                    // ------- SAAT JUMLAH TRANSAKSI TIDAK KOSONG DAN HUTANG HARI INI KURANG DARI ATAU SAMA DENGAN 0 ------ //
//                } else if (listSetoran.isEmpty() && hutangToday.compareTo(BigDecimal.ZERO) <= 0) {
//                    totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
//                    totalKasbon = totalKasbon.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
//                    mustPaid = hutangToday.add(totalAngsuran);
//                    if (validateHutang() && totalKasbon.compareTo(totalAngsuran) > 0) {
//                        txtKasbon.setText("0");
//                        lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
//                        lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                        sto.setPayedStatus(TransaksiStatus.L);
//                    } else if (validateHutang() && totalKasbon.compareTo(totalAngsuran) < 0) {
//                        JOptionPane.showMessageDialog(this, "Jumlah Pembayaran Yang Anda Masukkan Tidak Boleh Kurang Dari " + totalAngsuran, "Pesan Sistem", JOptionPane.ERROR_MESSAGE);
//                        txtBayarKasbon.requestFocus();
//                    } else {
//                        txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
//                        txtBayarKasbon.setText("0");
//                        txtOvertime.setText("0");
//                        txtCicilan.setText("0");
//                        lblNominal.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                        lblHutang.setText(rupiahFormatter.format(totalAngsuran));
//                        sto.setPayedStatus(TransaksiStatus.U);
//                    }
//                    if (isEditFire) {
//                        if (totalAngsuran.add(totalKasbon).compareTo(hutangToday) >= 0) {
//                            totalSurplus = totalSurplus.add(totalKasbon).add(totalAngsuran).subtract(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
//                            lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
//                            lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
//                            lblRupiahKasir.setVisible(true);
//                            txtKasbon.setText("0");
//                            lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                            sto.setPayedStatus(TransaksiStatus.L);
//                        } else {
//                            if (totalAngsuran.add(totalKasbon).compareTo(mustPaid) >= 0) {
//                                totalSurplus = totalSurplus.add(totalAngsuran.add(totalKasbon).subtract(mustPaid));
//                                lblNominal.setText(rupiahFormatter.format(totalKasbon.add(totalAngsuran)));
//                                lblRupiahKasir.setText("Surplus = " + rupiahFormatter.format(totalSurplus));
//                                lblRupiahKasir.setVisible(true);
//                                txtKasbon.setText("0");
//                                lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                                sto.setPayedStatus(TransaksiStatus.L);
//                            }
//                        }
//                    }
//            totalKasbon = totalKasbon.add(TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtOvertime.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtCicilan.getText()));
//            totalHutang = totalHutang.add(totalAngsuran.subtract(totalKasbon));
//                sto.setPayedStatus(TransaksiStatus.L);
//                sto.setJalanStatus(JalanStatus.J);
//                System.out.println("payed = " + sto.getPayedStatus());
//                txtKasbon.setText("0");
//                txtBayarKasbon.setText("0");
//                txtOvertime.setText("0");
//                txtCicilan.setText("0");
//                lblNominal.setText(rupiahFormatter.format(totalAngsuran));
//                lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                System.out.println("total angsuran = " + totalAngsuran);
//                System.out.println("total kasbon = " + totalKasbon);
//                System.out.println("hutang hari ini = " + hutangToday);
//                System.out.println("total hutang =" + totalHutang);
                } else {
                    totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                    txtKasbon.setText(TextComponentUtils.formatNumber(totalAngsuran));
                    lblNominal.setText(rupiahFormatter.format(BigDecimal.ZERO));
                    totalKasbon = totalAngsuran;
                    totalHutang = totalHutang.add(totalKasbon.add(hutangToday));
                    lblHutang.setText(rupiahFormatter.format(totalHutang));
                    sto.setPayedStatus(TransaksiStatus.U);
//                    sto.setJalanStatus(JalanStatus.M);
                    System.out.println("payed status = " + sto.getPayedStatus());
                    txtBayarKasbon.setText("0");
                    txtOvertime.setText("0");
                    txtCicilan.setText("0");
                }
            }
        }
    }//GEN-LAST:event_txtCicilanKeyReleased

    private void txtNoLambungMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtNoLambungMouseClicked
        // TODO add your handling code here:
        if (txtNoLambung.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtNoLambung.setForeground(Color.BLACK);
            txtNoLambung.setText("");
        }
    }//GEN-LAST:event_txtNoLambungMouseClicked

    private void txtKodeSetoranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKodeSetoranMouseClicked
        // TODO add your handling code here:
        if (txtKodeSetoran.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKodeSetoran.setForeground(Color.BLACK);
            txtKodeSetoran.setText("");
        }
    }//GEN-LAST:event_txtKodeSetoranMouseClicked

    private void txtAngsuranMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtAngsuranMouseClicked
        // TODO add your handling code here:
        if (txtAngsuran.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtAngsuran.setForeground(Color.BLACK);
            txtAngsuran.setText("");
        }
    }//GEN-LAST:event_txtAngsuranMouseClicked

    private void txtTabunganMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtTabunganMouseClicked
        // TODO add your handling code here:
        if (txtTabungan.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtTabungan.setForeground(Color.BLACK);
            txtTabungan.setText("");
        }
    }//GEN-LAST:event_txtTabunganMouseClicked

    private void txtKasbonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtKasbonMouseClicked
        // TODO add your handling code here:
        if (txtKasbon.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtKasbon.setForeground(Color.BLACK);
            txtKasbon.setText("");
        }
    }//GEN-LAST:event_txtKasbonMouseClicked

    private void txtBayarKasbonMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtBayarKasbonMouseClicked
        // TODO add your handling code here:
        if (txtBayarKasbon.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtBayarKasbon.setForeground(Color.BLACK);
            txtBayarKasbon.setText("");
        }
    }//GEN-LAST:event_txtBayarKasbonMouseClicked

    private void txtOvertimeMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtOvertimeMouseClicked
        // TODO add your handling code here:
        if (txtOvertime.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtOvertime.setForeground(Color.BLACK);
            txtOvertime.setText("");
        }
    }//GEN-LAST:event_txtOvertimeMouseClicked

    private void txtCicilanMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_txtCicilanMouseClicked
        // TODO add your handling code here:
        if (txtCicilan.getText().equals("TIDAK BOLEH KOSONG...")) {
            txtCicilan.setForeground(Color.BLACK);
            txtCicilan.setText("");
        }
    }//GEN-LAST:event_txtCicilanMouseClicked

    private void txtKasbonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtKasbonKeyReleased
        // TODO add your handling code here:
        // ----- KEADAAN PELUNASAN ----- //
        BigDecimal totalAngsuran = new BigDecimal(0);
        BigDecimal totalKasbon = new BigDecimal(0);
        BigDecimal mustPaid = new BigDecimal(0);

        if (sto == null) {
            sto = new setoran();
            stoDet = new setoranDetail();
        }
//         ----- SAAT JUMLAH TRANSAKSI TIDAK KOSONG DAN HUTANG HARI INI LEBIH DARI 0 ----- //
        if (!listSetoran.isEmpty() && hutangToday.compareTo(BigDecimal.ZERO) > 0) {
            // ----- CEK APABILA PENULISAN DIMULAI DENGAN ANGKA 0, YANG ARTINYA PEMBAYARAN PENUH ----- //
            totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
            if (txtKasbon.getText().startsWith("0") && evt.getKeyCode() == KeyEvent.VK_ENTER) {
                mustPaid = hutangToday.add(totalAngsuran);
                totalKasbon = totalKasbon.add(mustPaid);
                if (totalKasbon.signum() == -1) {
                    totalKasbon = totalKasbon.negate();
                }
//                if (totalAngsuran.compareTo(totalKasbon) > 0) {
//                    sto.setPayedStatus(TransaksiStatus.L);
//                    lblNominal.setText(rupiahFormatter.format(totalKasbon));
//                    lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
//                }else if (totalAngsuran.compareTo(totalKasbon) == 0) {
//                    sto.setPayedStatus(TransaksiStatus.L);
//                    lblNominal.setText(rupiahFormatter.format(totalAngsuran));
//                    lblHutang.setText(rupiahFormatter.format(totalKasbon));
//                }
//
//                else if (totalAngsuran.compareTo(totalKasbon) < 0) {
//                    sto.setPayedStatus(TransaksiStatus.U);
//                    lblNominal.setText(rupiahFormatter.format(totalAngsuran));
//                    lblHutang.setText(rupiahFormatter.format(totalKasbon));
//                }
//                if (totalKasbon.compareTo(BigDecimal.ZERO) != 0) {
//                    lblNominal.setText(rupiahFormatter.format(totalAngsuran));
//                    lblHutang.setText(rupiahFormatter.format(totalKasbon));
//                    sto.setPayedStatus(TransaksiStatus.U);
//                } else {
//                    sto.setPayedStatus(TransaksiStatus.L);
//                    lblNominal.setText(rupiahFormatter.format(totalAngsuran));
//                    lblHutang.setText(rupiahFormatter.format(totalKasbon));
//                }
                txtKasbon.setText("0");
                txtOvertime.setText("0");
                txtBayarKasbon.setText(TextComponentUtils.formatNumber(hutangToday));
                txtCicilan.setText("0");
                lblNominal.setText(rupiahFormatter.format(mustPaid));
                lblHutang.setText(rupiahFormatter.format(BigDecimal.ZERO));
                sto.setPayedStatus(TransaksiStatus.L);
                System.out.println("Total angsuran = " + totalAngsuran);
                System.out.println("hutangToday = " + hutangToday);
                System.out.println("Total kasbon = " + totalKasbon);
                // ----- CEK APABILA KASBON KOSONG MAKA SELURUH NILAI AKAN DIHAPUS ----- //
//            } else if (txtKasbon.getText().isEmpty()) {
//                txtKasbon.setText("");
//                txtBayarKasbon.setText("");
//                txtOvertime.setText("");
//                txtCicilan.setText("");
            } else if (txtKasbon.getText().startsWith(TextComponentUtils.formatNumber(totalAngsuran)) && evt.getKeyCode() == KeyEvent.VK_ENTER) {
                mustPaid = hutangToday;
                txtOvertime.setText("0");
                txtBayarKasbon.setText("0");
                txtCicilan.setText("0");
                lblNominal.setText(rupiahFormatter.format(totalAngsuran));
                lblHutang.setText(rupiahFormatter.format(mustPaid));
                sto.setPayedStatus(TransaksiStatus.U);
            }
//         ----- SAAT JUMLAH TRANSAKSI KOSONG DAN HUTANG HARI INI KURANG DARI 0 ----- //
        } else {
//         ----- CEK APABILA PENULISAN DIMULAI DENGAN ANGKA 0 DAN ENTER, YANG ARTINYA PEMBAYARAN SETORAN TANPA HUTANG ----- //
            if (txtKasbon.getText().startsWith("0") && evt.getKeyCode() == KeyEvent.VK_ENTER) {
                totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
                lblNominal.setText(rupiahFormatter.format(totalAngsuran));
                txtBayarKasbon.setText("0");
                txtOvertime.setText("0");
                txtCicilan.setText("0");
                hutangToday = BigDecimal.ZERO;
                sto.setPayedStatus(TransaksiStatus.L);
                lblHutang.setText("Belum Ada Hutang");
//            } else if (txtKasbon.getText().isEmpty()) {
//                txtKasbon.setText("");
//                txtBayarKasbon.setText("");
//                txtOvertime.setText("");
//                txtCicilan.setText("");
//                lblNominal.setText("");
//                lblHutang.setText("");
            }
        }
    }//GEN-LAST:event_txtKasbonKeyReleased

    private void checkJalanStatusActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_checkJalanStatusActionPerformed
        // TODO add your handling code here:
        if (sto == null) {
            sto = new setoran();
            stoDet = new setoranDetail();
        }
        if (checkJalanStatus.isSelected()) {
            checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.BOLD, 13));
            checkJalanStatus.setText("Jalan");
            sto.setJalanStatus(JalanStatus.J);
            System.out.println("jalan status =" + sto.getJalanStatus());
        } else {
            checkJalanStatus.setFont(new Font("BPG Chveulebrivi", Font.PLAIN, 13));
            checkJalanStatus.setText("Tidak/Rusak");
            sto.setJalanStatus(JalanStatus.M);
            System.out.println("jalan status =" + sto.getJalanStatus());
        }
}//GEN-LAST:event_checkJalanStatusActionPerformed

    private void dateCariSetoranPropertyChange(java.beans.PropertyChangeEvent evt) {//GEN-FIRST:event_dateCariSetoranPropertyChange
        // TODO add your handling code here:
        if (dateCariSetoran.getDate() != null) {
            if (sto == null) {
                sto = new setoran();
                stoDet = new setoranDetail();
            }
            listSetoran = Main.getTransaksiService().findAllAvailableSetoranByLambung(new Integer(txtCariSetoranLambung.getText()), dateCariSetoran.getDate(), isClosedStatus.AVAILABLE);
            for (int i = 0; i <= listSetoran.size(); i++) {
                if (txtCariSetoranLambung.getText().startsWith(listSetoran.get(i).getDetail().get(0).getKemudi().getKend().getNoLambung().toString())) {
                    tblSetoran.removeAll();
                    sto = listSetoran.get(i);
                    stoDet.setSetor_map(sto);
                    listSetoranDetail.add(stoDet);
                    tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
                    initColumnSize();
                }
            }
        }
    }//GEN-LAST:event_dateCariSetoranPropertyChange

    private void txtCariSetoranLambungKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtCariSetoranLambungKeyReleased
        // TODO add your handling code here:
        if (txtCariSetoranLambung.getText().isEmpty() && evt.getKeyChar() == '\n') {
            dateCariSetoran.setDate(null);
            LoadDatabaseToTable();
        }
    }//GEN-LAST:event_txtCariSetoranLambungKeyReleased

    private void txtBayarKasbonKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtBayarKasbonKeyReleased
        // TODO add your handling code here:
        BigDecimal totalAngsuran = BigDecimal.ZERO;
        BigDecimal totalBayar = BigDecimal.ZERO;
        BigDecimal totalHutang = BigDecimal.ZERO;

        if (!txtBayarKasbon.getText().isEmpty() && evt.getKeyChar() == '\n') {
            totalAngsuran = totalAngsuran.add(TextComponentUtils.parseNumberToBigDecimal(txtAngsuran.getText())).add(TextComponentUtils.parseNumberToBigDecimal(txtTabungan.getText()));
            totalBayar = TextComponentUtils.parseNumberToBigDecimal(txtBayarKasbon.getText()).add(totalAngsuran);
            totalHutang = totalHutang.add(hutang).subtract(hutang);
            System.out.println("hutang = " + totalHutang);
            System.out.println("totalAngsuran = " + totalAngsuran);
            System.out.println("totalBayar = " + totalBayar);
            lblNominal.setText(rupiahFormatter.format(totalBayar));
            lblHutang.setText(rupiahFormatter.format(totalHutang));
            lblRupiahHutang.setText("Total Yang Harus Di Bayar = " + rupiahFormatter.format(totalHutang));
            txtKasbon.setText("0");
            txtOvertime.setText("0");
            txtCicilan.setText("0");
        }
    }//GEN-LAST:event_txtBayarKasbonKeyReleased

    private void btnOpenClosedTransactionActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnOpenClosedTransactionActionPerformed
        // TODO add your handling code here:
        listSetoranDetail = new OpenClosedTrans().showDialog();
        if (listSetoranDetail.isEmpty()) {
            LoadDatabaseToTable();
        } else if (!listSetoranDetail.isEmpty()) {
            tblSetoran.setEnabled(true);
            tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
        } else {
            return;
        }
    }//GEN-LAST:event_btnOpenClosedTransactionActionPerformed

    private void lblHutangMouseClicked(java.awt.event.MouseEvent evt) {//GEN-FIRST:event_lblHutangMouseClicked
        // TODO add your handling code here:
        if (evt.getClickCount() == 2) {
            lblHutang.setVisible(false);
            txtUbahHutang.setText(lblHutang.getText().substring(3));
            txtUbahHutang.setVisible(true);
            this.getContentPane().validate();
        }
    }//GEN-LAST:event_lblHutangMouseClicked

    private void txtUbahHutangKeyPressed(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtUbahHutangKeyPressed
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            lblHutang.setText(rupiahFormatter.format(TextComponentUtils.parseNumberToBigDecimal(txtUbahHutang.getText())));
            txtUbahHutang.setVisible(false);
            lblHutang.setVisible(true);
            this.getContentPane().validate();
        }
    }//GEN-LAST:event_txtUbahHutangKeyPressed

    private void btnBackActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnBackActionPerformed
        // TODO add your handling code here:
//        if (currentPos <= limit) {
//            btnBack.setEnabled(false);
//            prevPageRecord(currentPos);
//            return;
//        } else if (currentPos > limit) {
//            btnBack.setEnabled(true);
        isPrevPagePressed = true;
        if (isNextPagePressed) {
            isChangedPages = true;
        }
        prevPageRecord(currentPos);
        isChangedPages = false;
        isNextPagePressed = false;
//        }
    }//GEN-LAST:event_btnBackActionPerformed

    private void btnNextActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_btnNextActionPerformed
        // TODO add your handling code here:
        isNextPagePressed = true;
        if (isPrevPagePressed) {
            isChangedPages = true;
        }
        nextPageRecord(currentPos);
        isChangedPages = false;
        isPrevPagePressed = false;
    }//GEN-LAST:event_btnNextActionPerformed

    private void txtShowPageKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtShowPageKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            limit = new Integer(txtShowPage.getText());
            listSetoranDetail = null;
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), 0, limit);
            tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
            initColumnSize();
            currentPos = limit;
        }
    }//GEN-LAST:event_txtShowPageKeyReleased

    private void txtPagesKeyReleased(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_txtPagesKeyReleased
        // TODO add your handling code here:
        if (evt.getKeyCode() == KeyEvent.VK_ENTER) {
            currentPos = new Integer(txtShowPage.getText()).intValue() * new Integer(txtPages.getText()).intValue();
            limit = new Integer(txtShowPage.getText());
            listSetoranDetail = null;
            listSetoranDetail = Main.getTransaksiService().findAvailableSetoran(isClosedStatus.AVAILABLE, komposisisetoran.getAngsuran(), komposisisetoran.getTabungan(), komposisisetoran.getNamaKomposisi(), currentPos, limit);
            tblSetoran.setModel(new TransaksiSetoranTableModel(listSetoranDetail));
            initColumnSize();
            isChangedPages = true;
        }
    }//GEN-LAST:event_txtPagesKeyReleased
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton btnBack;
    private javax.swing.JButton btnNext;
    private javax.swing.JButton btnOpenClosedTransaction;
    private javax.swing.JCheckBox checkJalanStatus;
    private com.toedter.calendar.JDateChooser dateCariSetoran;
    private com.toedter.calendar.JDateChooser dateJatuhTempo;
    private com.toedter.calendar.JDateChooser dateSPO;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel10;
    private javax.swing.JLabel jLabel11;
    private javax.swing.JLabel jLabel12;
    private javax.swing.JLabel jLabel13;
    private javax.swing.JLabel jLabel14;
    private javax.swing.JLabel jLabel15;
    private javax.swing.JLabel jLabel16;
    private javax.swing.JLabel jLabel17;
    private javax.swing.JLabel jLabel18;
    private javax.swing.JLabel jLabel19;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JLabel jLabel7;
    private javax.swing.JLabel jLabel8;
    private javax.swing.JLabel jLabel9;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JPanel jPanel2;
    private javax.swing.JPanel jPanel4;
    private javax.swing.JPanel jPanel5;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JScrollPane jScrollPane2;
    private javax.swing.JScrollPane jScrollPane3;
    private javax.swing.JSeparator jSeparator1;
    private javax.swing.JSplitPane jSplitPane1;
    private javax.swing.JSplitPane jSplitPane2;
    private javax.swing.JTable jTable1;
    private javax.swing.JLabel lblHutang;
    private javax.swing.JLabel lblJalanStatus;
    private javax.swing.JLabel lblNominal;
    private javax.swing.JLabel lblRupiahHutang;
    private javax.swing.JLabel lblRupiahKasir;
    private javax.swing.JLabel lblTotalHal;
    private javax.swing.JPanel pnlHutang;
    private javax.swing.JPanel pnlTerimaKasir;
    private javax.swing.JTable tblSetoran;
    private mintory.transaksi.toolbarButtonTransaksi toolbarButtonTransaksi;
    private javax.swing.JTextField txtAngsuran;
    private javax.swing.JTextField txtBayarKasbon;
    private javax.swing.JTextField txtCariSetoranLambung;
    private javax.swing.JTextField txtCicilan;
    private javax.swing.JTextField txtKasbon;
    private javax.swing.JTextArea txtKeterangan;
    private javax.swing.JTextField txtKodeSetoran;
    private javax.swing.JTextField txtNRP;
    private javax.swing.JTextField txtNama;
    private javax.swing.JTextField txtNoLambung;
    private javax.swing.JTextField txtOvertime;
    private javax.swing.JTextField txtPages;
    private javax.swing.JTextField txtSetoranCounter;
    private javax.swing.JTextField txtShowPage;
    private javax.swing.JTextField txtTabungan;
    private javax.swing.JTextField txtUbahHutang;
    // End of variables declaration//GEN-END:variables
}
