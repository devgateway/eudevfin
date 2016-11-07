/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.devgateway.eudevfin.projects.common.entities;

import java.io.Serializable;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 *
 * @author alcr
 */
@Entity
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class ProjectResult implements Serializable {

    private static final long serialVersionUID = -2917246983336492166L;

    @ManyToOne
    @JoinColumn(name = "project_id")
    public Project project;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    protected Long id = null;

    protected String result;
    protected String status;
    
    @Column(length = 4096)
    protected String description;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }
    
    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "\nProjectResult{"
                + "\n   id=" + id
                + "\n   result=" + result
                + "\n   status=" + status
                + "\n   description=" + description
                + '}';
    }
}
