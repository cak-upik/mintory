/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

package paperman.model;

import java.io.Serializable;
import java.math.BigDecimal;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

/**
 *
 * @author i1440ns
 */
@Entity
@Table(name="komposisi_setoran")
public class komposisiSetoran implements Serializable {
    @Id
    @GeneratedValue
    private Integer id;

    @Column(name="nama_komposisi")
    private String namaKomposisi;

    @Column(name="nilai_angsuran")
    private BigDecimal angsuran;

    @Column(name="nilai_tabungan")
    private BigDecimal tabungan;

    @Column(name="status")
    private Boolean status;

    @ManyToOne
    @JoinColumn(name="id_sistem")
    private sistem idSistem;

    @ManyToOne
    @JoinColumn(name="id_genCode")
    private codeGenerator genCodeRef;

    public sistem getIdSistem() {
        return idSistem;
    }

    public void setIdSistem(sistem idSistem) {
        this.idSistem = idSistem;
    }

    public codeGenerator getGenCodeRef() {
        return genCodeRef;
    }

    public void setGenCodeRef(codeGenerator genCodeRef) {
        this.genCodeRef = genCodeRef;
    }

    public BigDecimal getAngsuran() {
        return angsuran;
    }

    public void setAngsuran(BigDecimal angsuran) {
        this.angsuran = angsuran;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getNamaKomposisi() {
        return namaKomposisi;
    }

    public void setNamaKomposisi(String namaKomposisi) {
        this.namaKomposisi = namaKomposisi;
    }

    public BigDecimal getTabungan() {
        return tabungan;
    }

    public void setTabungan(BigDecimal tabungan) {
        this.tabungan = tabungan;
    }

    public Boolean getStatus() {
        return status;
    }

    public void setStatus(Boolean status) {
        this.status = status;
    }
}
