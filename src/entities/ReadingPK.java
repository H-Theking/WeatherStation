/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Embeddable;

/**
 *
 * @author harvey
 */
@Embeddable
public class ReadingPK implements Serializable {
    @Basic(optional = false)
    @Column(name = "Id")
    private int id;
    @Basic(optional = false)
    @Column(name = "Sensor_Id")
    private int sensorId;

    public ReadingPK() {
    }

    public ReadingPK(int id, int sensorId) {
        this.id = id;
        this.sensorId = sensorId;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSensorId() {
        return sensorId;
    }

    public void setSensorId(int sensorId) {
        this.sensorId = sensorId;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (int) id;
        hash += (int) sensorId;
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof ReadingPK)) {
            return false;
        }
        ReadingPK other = (ReadingPK) object;
        if (this.id != other.id) {
            return false;
        }
        if (this.sensorId != other.sensorId) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "entities.ReadingPK[ id=" + id + ", sensorId=" + sensorId + " ]";
    }
    
}
