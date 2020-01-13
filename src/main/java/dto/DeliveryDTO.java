/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Delivery;
import java.util.Date;

/**
 *
 * @author rasmu
 */
public class DeliveryDTO {
    private Long id;
    private Date shippingDate;
    private String fromLocation;
    private String toLocation;

    public DeliveryDTO(Delivery delivery) {
        this.id = delivery.getId();
        this.shippingDate = delivery.getShippingDate();
        this.fromLocation = delivery.getFromLocation();
        this.toLocation = delivery.getToLocation();
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
    
    
}
