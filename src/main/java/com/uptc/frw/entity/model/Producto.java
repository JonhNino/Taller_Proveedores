package com.uptc.frw.entity.model;

import jakarta.persistence.*;

import java.util.List;

@Entity
@Table(name = "productos")
@SequenceGenerator(name = "PRODUCTOS_SEQ_GEN", sequenceName = "PRODUCTOS_SEQ", allocationSize = 1)
public class Producto {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PRODUCTOS_SEQ_GEN")
    private Long id;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "precio_unitario")
    private Double precioUnitario;

    @ManyToMany(mappedBy = "productos", cascade = CascadeType.ALL)
    private List<Persona> personas;

    @OneToMany(mappedBy = "producto",cascade = CascadeType.ALL)
    private List<Detalle> detalles;

    public Producto() {
    }

    public Producto(String nombre, Double precioUnitario) {
        this.nombre = nombre;
        this.precioUnitario = precioUnitario;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Double getPrecioUnitario() {
        return precioUnitario;
    }

    public void setPrecioUnitario(Double precioUnitario) {
        this.precioUnitario = precioUnitario;
    }

    public List<Persona> getPersonas() {
        return personas;
    }

    public void setPersonas(List<Persona> personas) {
        this.personas = personas;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precioUnitario=" + precioUnitario +
                '}';
    }
}

