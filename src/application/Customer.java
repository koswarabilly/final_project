/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package application;

import java.util.Objects;

/**
 *
 * @author DMI3
 */
public class Customer {

    private String name, checkindt, checkoutdt;
    private Integer room;

    public Customer(String name, String checkin, String checkout, Integer room) {
        this.name = name;
        this.checkindt = checkin;
        this.checkoutdt = checkout;
        this.room = room;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getCheckindt() {
        return checkindt;
    }

    public void setCheckindt(String checkindt) {
        this.checkindt = checkindt;
    }

    public String getCheckoutdt() {
        return checkoutdt;
    }

    public void setCheckoutdt(String checkoutdt) {
        this.checkoutdt = checkoutdt;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, checkindt, checkoutdt, room);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Customer customer = (Customer) obj;
        return Objects.equals(name, customer.name) && Objects.equals(checkindt, customer.checkindt) && Objects.equals(name, customer.name)
                && Objects.equals(room, customer.room);
    }

    @Override
    public String toString() {
        return "Customer{" + "name=" + name + ", checkindt=" + checkindt + ", checkoutdt=" + checkoutdt + ", room=" + room + '}';
    }

}
