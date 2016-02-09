/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author harvey
 */
@Entity
@Table(name = "Reading")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Reading.findAll", query = "SELECT r FROM Reading r"),
    @NamedQuery(name = "Reading.findById", query = "SELECT r FROM Reading r WHERE r.readingPK.id = :id"),
    @NamedQuery(name = "Reading.findBySensorId", query = "SELECT r FROM Reading r WHERE r.readingPK.sensorId = :sensorId"),
    @NamedQuery(name = "Reading.findByReadValue", query = "SELECT r FROM Reading r WHERE r.readValue = :readValue"),
    @NamedQuery(name = "Reading.findByDate", query = "SELECT r FROM Reading r WHERE r.date = :date")})
public class Reading implements Serializable {
    private static final long serialVersionUID = 1L;
    @EmbeddedId
    protected ReadingPK readingPK;
    @Basic(optional = false)
    @Column(name = "ReadValue")
    private float readValue;
    @Basic(optional = false)
    @Column(name = "Date")
    private String date;
    @JoinColumn(name = "Sensor_Id", referencedColumnName = "Id", insertable = false, updatable = false)
    @ManyToOne(optional = false)
    private Sensor sensor;

    public Reading() {
    }

    public Reading(ReadingPK readingPK) {
        this.readingPK = readingPK;
    }

    public Reading(ReadingPK readingPK, float readValue, String date) {
        this.readingPK = readingPK;
        this.readValue = readValue;
        this.date = date;
    }

    public Reading(int id, int sensorId) {
        this.readingPK = new ReadingPK(id, sensorId);
    }

    public ReadingPK getReadingPK() {
        return readingPK;
    }

    public void setReadingPK(ReadingPK readingPK) {
        this.readingPK = readingPK;
    }

    public float getReadValue() {
        return readValue;
    }

    public void setReadValue(float readValue) {
        this.readValue = readValue;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
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
        hash += (readingPK != null ? readingPK.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Reading)) {
            return false;
        }
        Reading other = (Reading) object;
        if ((this.readingPK == null && other.readingPK != null) || (this.readingPK != null && !this.readingPK.equals(other.readingPK))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.Reading[ readingPK=" + readingPK + " ]";
    }
    
}
