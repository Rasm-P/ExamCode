/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package dto;

import entities.Driver;

/**
 *
 * @author rasmu
 */
public class DriverDTO {
    private Long id;
    private String name;

    public DriverDTO(Driver driver) {
        this.id = driver.getId();
        this.name = driver.getName();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
    
    
}
