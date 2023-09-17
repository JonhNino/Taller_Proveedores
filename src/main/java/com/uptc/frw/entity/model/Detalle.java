package com.uptc.frw.entity.model;


import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Producto;
import jakarta.persistence.*;

@Entity
@Table(name = "detalles")
@SequenceGenerator(name = "DETALLES_SEQ_GEN", sequenceName = "DETALLES_SEQ", allocationSize = 1)
public class Detalle {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "DETALLES_SEQ_GEN")
    private Long id;

    @Column(name = "cantidad")
    private Integer cantidad;

    @Column(name = "precio_venta")
    private Double precioVenta;

    @ManyToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "factura_id", nullable = false)
    private Factura factura;

    @ManyToOne(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinColumn(name = "producto_id", nullable = false)
    private Producto producto;

    public Detalle() {
    }

    public Detalle(Integer cantidad, Double precioVenta, Factura factura, Producto producto) {
        this.cantidad = cantidad;
        this.precioVenta = precioVenta;
        this.factura = factura;
        this.producto = producto;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    public Double getPrecioVenta() {
        return precioVenta;
    }

    public void setPrecioVenta(Double precioVenta) {
        this.precioVenta = precioVenta;
    }

    public Factura getFactura() {
        return factura;
    }

    public void setFactura(Factura factura) {
        this.factura = factura;
    }

    public Producto getProducto() {
        return producto;
    }

    public void setProducto(Producto producto) {
        this.producto = producto;
    }

    @Override
    public String toString() {
        return "Detalle{" +
                "id=" + id +
                ", cantidad=" + cantidad +
                ", precioVenta=" + precioVenta +
                '}';
    }
}

