package com.leverx.employeestat.rest.entity;

import javax.persistence.Embeddable;
import javax.persistence.Embedded;
import javax.persistence.ManyToOne;
import java.io.Serializable;
import java.util.Objects;
import java.util.UUID;

@Embeddable
public class WorkId implements Serializable {

    @ManyToOne(targetEntity = Employee.class)
    private Employee employee;

    @ManyToOne(targetEntity = Project.class)
    private Project project;

    public Employee getEmployee() {
        return employee;
    }

    public void setEmployee(Employee employee) {
        this.employee = employee;
    }

    public Project getProject() {
        return project;
    }

    public void setProject(Project project) {
        this.project = project;
    }

    public String toString(){
        return "(employeeId=" + employee.getId() + "; projectId=" + project.getId() + ")";
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        WorkId workId = (WorkId) o;
        return employee.equals(workId.employee) && project.equals(workId.project);
    }

    @Override
    public int hashCode() {
        return Objects.hash(employee, project);
    }
}