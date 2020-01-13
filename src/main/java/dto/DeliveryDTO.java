/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Cargo;
import entities.Delivery;
import entities.Truck;
import java.util.Date;
import java.util.List;

/**
 *
 * @author rasmu
 */
public class DeliveryDTO {
    private Long id;
    private Date shippingDate;
    private String fromLocation;
    private String toLocation;
    private List<Cargo> cargoList;
    private Truck truck;

    public DeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.shippingDate = delivery.getShippingDate();
        this.fromLocation = delivery.getFromLocation();
        this.toLocation = delivery.getToLocation();
        this.cargoList = delivery.getCargoList();
        this.truck = delivery.getTruck();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
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

//    public List<Cargo> getCargoList() {
//        return cargoList;
//    }
//
//    public void setCargoList(List<Cargo> cargoList) {
//        this.cargoList = cargoList;
//    }
//
//    public Truck getTruck() {
//        return truck;
//    }

//    public void setTruck(Truck truck) {
//        this.truck = truck;
//    }
    
    
}
