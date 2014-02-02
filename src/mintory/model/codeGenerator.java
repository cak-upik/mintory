/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package mintory.model;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 *
 * @author Nurul Chusna
 */
@Entity
@Table(name="number_factory")
public class codeGenerator implements Serializable {
    @Id
    private Integer id;
    @Column(name = "TransactionType")
    private String TagTransaction;
    @Column(name = "ArmadaType")
    private String TagArmada;
    @Column(name = "tanggal")
    @Temporal(TemporalType.DATE)
    private Date tanggal;
    @Column(name = "last_number")
    private  Integer lastnum;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }
    
    public String getTagTransaction() {
        return TagTransaction;
    }

    public void setTagTransaction(String TagTransaction) {
        this.TagTransaction = TagTransaction;
    }

    public String getTagArmada() {
        return TagArmada;
    }

    public void setTagArmada(String TagArmada) {
        this.TagArmada = TagArmada;
    }

    public Integer getLastnum() {
        return lastnum;
    }

    public void setLastnum(Integer lastnum) {
        this.lastnum = lastnum;
    }

    public Date getTanggal() {
        return tanggal;
    }

    public void setTanggal(Date tanggal) {
        this.tanggal = tanggal;
    }
}
