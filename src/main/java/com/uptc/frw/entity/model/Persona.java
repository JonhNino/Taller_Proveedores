package com.uptc.frw.entity.model;

import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Producto;
import jakarta.persistence.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "personas")
@SequenceGenerator(name = "PERSONAS_SEQ_GEN", sequenceName = "PERSONAS_SEQ", allocationSize = 1)
public class Persona {

    @Id
    @Column(name = "id")
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "PERSONAS_SEQ_GEN")
    private Long id;

    @Column(name = "nombres")
    private String nombres;

    @Column(name = "apellidos")
    private String apellidos;

    @Column(name = "fecha_nacimiento")
    private Date fechaNacimiento;

    @Column(name = "tipo_documento")
    private String tipoDocumento;

    @Column(name = "numero_documento")
    private String numeroDocumento;

    @Column(name = "direccion")
    private String direccion;

    @OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL)
    private List<Factura> facturasComoCliente;

    @OneToMany(mappedBy = "vendedor", cascade = CascadeType.ALL)
    private List<Factura> facturasComoVendedor;

    @ManyToMany(cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    @JoinTable(
            name = "productos_personas",
            joinColumns = @JoinColumn(name = "persona_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> productos;

    public Persona() {
    }

    public Persona(String nombres, String apellidos, Date fechaNacimiento, String tipoDocumento, String numeroDocumento, String direccion) {
        this.nombres = nombres;
        this.apellidos = apellidos;
        this.fechaNacimiento = fechaNacimiento;
        this.tipoDocumento = tipoDocumento;
        this.numeroDocumento = numeroDocumento;
        this.direccion = direccion;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getNombres() {
        return nombres;
    }

    public void setNombres(String nombres) {
        this.nombres = nombres;
    }

    public String getApellidos() {
        return apellidos;
    }

    public void setApellidos(String apellidos) {
        this.apellidos = apellidos;
    }

    public Date getFechaNacimiento() {
        return fechaNacimiento;
    }

    public void setFechaNacimiento(Date fechaNacimiento) {
        this.fechaNacimiento = fechaNacimiento;
    }

    public String getTipoDocumento() {
        return tipoDocumento;
    }

    public void setTipoDocumento(String tipoDocumento) {
        this.tipoDocumento = tipoDocumento;
    }

    public String getNumeroDocumento() {
        return numeroDocumento;
    }

    public void setNumeroDocumento(String numeroDocumento) {
        this.numeroDocumento = numeroDocumento;
    }

    public String getDireccion() {
        return direccion;
    }

    public void setDireccion(String direccion) {
        this.direccion = direccion;
    }

    public List<Factura> getFacturasComoCliente() {
        return facturasComoCliente;
    }

    public void setFacturasComoCliente(List<Factura> facturasComoCliente) {
        this.facturasComoCliente = facturasComoCliente;
    }

    public List<Factura> getFacturasComoVendedor() {
        return facturasComoVendedor;
    }

    public void setFacturasComoVendedor(List<Factura> facturasComoVendedor) {
        this.facturasComoVendedor = facturasComoVendedor;
    }

    public List<Producto> getProductos() {
        return productos;
    }

    public void setProductos(List<Producto> productos) {
        this.productos = productos;
    }

    @Override
    public String toString() {
        return "Persona{" +
                "id=" + id +
                ", nombres='" + nombres + '\'' +
                ", apellidos='" + apellidos + '\'' +
                ", fechaNacimiento=" + fechaNacimiento +
                ", tipoDocumento='" + tipoDocumento + '\'' +
                ", numeroDocumento='" + numeroDocumento + '\'' +
                ", direccion='"+direccion + '\'' +
                '}';
    }
}
