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
public class Worker {

    private String name, id, position,since;

    public Worker(String id, String name, String position, String since) {
        this.name = name;
        this.id = id;
        this.position = position;
        this.since = since;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getPosition() {
        return position;
    }

    public void setPosition(String position) {
        this.position = position;
    }

    public String getSince() {
        return since;
    }

    public void setSince(String since) {
        this.since = since;
    }


    @Override
    public int hashCode() {
        return Objects.hash(id,name,position, since);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (obj == null || getClass() != obj.getClass()) {
            return false;
        }
        Worker worker = (Worker) obj;
        return Objects.equals(id, worker.id) && Objects.equals(name, worker.name) && Objects.equals(position, worker.position)
                && Objects.equals(since, worker.since);
    }

    @Override
    public String toString() {
        return "Worker{" + "name=" + name + ", id=" + id + ", position=" + position + ", since=" + since + '}';
    }

    

}
