package com.uptc.frw.entity.model;


import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "facturas")
@SequenceGenerator(name = "FACTURAS_SEQ_GEN", sequenceName = "FACTURAS_SEQ", allocationSize = 1)
public class Factura {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "FACTURAS_SEQ_GEN")
    private Long id;

    @Column(name = "fecha")
    private Date fecha;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "cliente_id", nullable = false)
    private Persona cliente;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "vendedor_id", nullable = false)
    private Persona vendedor;

    @OneToMany(mappedBy = "factura", cascade = CascadeType.ALL)
    private List<Detalle> detalles;

    public Factura() {
    }

    public Factura(Date fecha, Persona cliente, Persona vendedor) {
        this.fecha = fecha;
        this.cliente = cliente;
        this.vendedor = vendedor;
    }

    public Factura(Date fechaFact, Long id, Long id1) {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Date getFecha() {
        return fecha;
    }

    public void setFecha(Date fecha) {
        this.fecha = fecha;
    }

    public Persona getCliente() {
        return cliente;
    }

    public void setCliente(Persona cliente) {
        this.cliente = cliente;
    }

    public Persona getVendedor() {
        return vendedor;
    }

    public void setVendedor(Persona vendedor) {
        this.vendedor = vendedor;
    }

    public List<Detalle> getDetalles() {
        return detalles;
    }

    public void setDetalles(List<Detalle> detalles) {
        this.detalles = detalles;
    }

    @Override
    public String toString() {
        return "Factura{" +
                "id=" + id +
                ", fecha=" + fecha +
                '}';
    }
}
