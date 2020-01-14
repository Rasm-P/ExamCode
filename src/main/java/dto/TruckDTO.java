/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Truck;

/**
 *
 * @author rasmu
 */
public class TruckDTO {

    private Long id;
    private String name;
    private int capacity;

    public TruckDTO(Truck truck) {
        this.name = truck.getName();
        this.capacity = truck.getCapacity();
        this.id = truck.getId();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

}
