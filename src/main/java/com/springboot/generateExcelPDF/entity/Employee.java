package com.springboot.generateExcelPDF.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name="employee")
public class Employee {
    @Id
    @Column(name="id")
    private String employeeId;
    @Column(name="name")
    private String name;
    @Column(name="address")
    private String address;
    @Column(name="city")
    private String city;
    @Column(name="mobile")
    private long mobile;
    @Column(name="mail")
    private String mail;

}
