/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package entities;

import java.io.Serializable;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.validation.constraints.NotNull;

/**
 *
 * @author rasmu
 */
@Entity
@Table(name = "deliveries")
@NamedQuery(name = "Delivery.deleteAllRows", query = "DELETE from Delivery")
public class Delivery implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @NotNull
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    
    @Column(name = "shippingDate")
    @Temporal(javax.persistence.TemporalType.DATE)
    private Date shippingDate;
    
    private String dateAsString;
    
    @Column(name = "fromLocation")
    private String fromLocation;
    
    @Column(name = "toLocation")
    private String toLocation;
    
    @OneToMany(mappedBy = "dilevery")
    private List<Cargo> cargoList = new ArrayList<>();
    
    @ManyToOne
    private Truck truck;

    public Delivery() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Delivery(Date shippingDate, String fromLocation, String toLocation) {
        this.shippingDate = shippingDate;
        this.fromLocation = fromLocation;
        this.toLocation = toLocation;
        this.dateAsString = shippingDate.toInstant().atZone(ZoneId.systemDefault()).toLocalDate().toString();
    }

    public Date getShippingDate() {
        return shippingDate;
    }

    public void setShippingDate(Date shippingDate) {
        this.shippingDate = shippingDate;
    }

    public String getFromLocation() {
        return fromLocation;
    }

    public void setFromLocation(String fromLocation) {
        this.fromLocation = fromLocation;
    }

    public String getToLocation() {
        return toLocation;
    }

    public void setToLocation(String toLocation) {
        this.toLocation = toLocation;
    }

    public List<Cargo> getCargoList() {
        return cargoList;
    }

    public void setCargoList(List<Cargo> cargoList) {
        this.cargoList = cargoList;
    }

    public Truck getTruck() {
        return truck;
    }

    public void setTruck(Truck truck) {
        this.truck = truck;
    }

    public void addCargo(Cargo cargo) {
        cargoList.add(cargo);
    }
    
    
    
}
