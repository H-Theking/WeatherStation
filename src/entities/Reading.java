/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author harvey
 */
@Entity
@Table(name = "Reading")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reading.findAll", query = "SELECT r FROM Reading r, Sensor s where r.sensor.id = s.id AND s.status <> 'REMOVED'"),
    @NamedQuery(name = "Reading.findById", query = "SELECT r FROM Reading r WHERE r.id = :id"),
    @NamedQuery(name = "Reading.findByReadValue", query = "SELECT r FROM Reading r WHERE r.readValue = :readValue"),
    @NamedQuery(name = "Reading.findBySensorAndRegDate", query = "SELECT r FROM Reading r WHERE "
            + "FUNCTION('Day', FUNCTION('Date', r.regDate)) = :day AND "
            + "FUNCTION('Month', FUNCTION('Date', r.regDate)) = :month AND r.sensor = :sensor"),
    @NamedQuery(name = "Reading.get7DaysReading", query = "SELECT r FROM Reading r WHERE r.sensor = :sensor AND r.regDate BETWEEN :start AND :end")})
public class Reading implements Serializable {
    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "Id")
    private Integer id;
    @Basic(optional = false)
    @Column(name = "ReadValue")
    private float readValue;
    @Basic(optional = false)
    @Column(name = "RegDate")
    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;
    @JoinColumn(name = "Sensor_Id", referencedColumnName = "Id")
    @ManyToOne(optional = false)
    private Sensor sensor;

    public Reading() {
    }

    public Reading(Integer id) {
        this.id = id;
    }

    public Reading(float readValue, Date regDate, Sensor sensor) {
        this.readValue = readValue;
        this.regDate = regDate;
        this.sensor = sensor;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public float getReadValue() {
        return readValue;
    }

    public void setReadValue(float readValue) {
        this.readValue = readValue;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }

    public Sensor getSensor() {
        return sensor;
    }

    public void setSensor(Sensor sensor) {
        this.sensor = sensor;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reading)) {
            return false;
        }
        Reading other = (Reading) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Reading[ id=" + id + " ]";
    }
    
}
