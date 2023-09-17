package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class ViewBill {
    private final EntityManager entityManager;
    private final Scanner scanner;

    public ViewBill(EntityManager entityManager, Scanner scanner) {
        this.entityManager = entityManager;
        this.scanner = scanner;
    }

    public void viewBill() {
        List<Long> facturaIds = mostrarFacturas();
        Long idFactura = obtenerIdFactura(facturaIds);
        mostrarDetalleFactura(idFactura);
    }

    private void mostrarDetalleFactura(Long idFactura) {
        entityManager.getTransaction().begin();
        Factura factura = entityManager.find(Factura.class, idFactura);
        imprimirDetalleFactura(factura);
        entityManager.getTransaction().commit();
    }

    private void imprimirDetalleFactura(Factura factura) {
        System.out.println("Factura #  " + factura.getId());
        System.out.println();
        System.out.println(String.format("Cliente: %-30s Fecha Compra: %s", factura.getCliente().getNombres() + " " + factura.getCliente().getApellidos(), factura.getFecha()));
        System.out.println("Direccion del Cliente: " + factura.getCliente().getDireccion());
        System.out.println();

        System.out.println(String.format("%-8s %-20s %-10s %-25s %-10s", "Id Item", "Nombre Producto", "Cantidad", "Valor Unitario de Venta", "Valor"));
        System.out.println(String.format("%-8s %-20s %-10s %-25s %-10s", "-------", "----------------", "--------", "-----------------------", "-----"));
        TypedQuery<Detalle> queryDetalle = entityManager.createQuery("SELECT d FROM Detalle d WHERE d.factura.id = :facturaId", Detalle.class);
        queryDetalle.setParameter("facturaId", factura.getId());
        List<Detalle> detalles = queryDetalle.getResultList();

        double total = 0;
        for (Detalle detalle : detalles) {
            System.out.println(String.format("%-8d %-20s %-10d %-25.2f %-10.2f", detalle.getId(), detalle.getProducto().getNombre(), detalle.getCantidad(), detalle.getPrecioVenta(), (detalle.getPrecioVenta() * detalle.getCantidad())));
            total += detalle.getPrecioVenta() * detalle.getCantidad();
        }

        System.out.println();
        System.out.println(String.format("%65s %-10.2f", "TOTAL", total));
        System.out.println();
        System.out.println("Atendido Por : " + factura.getVendedor().getNombres() + " " + factura.getVendedor().getApellidos());
    }


    private Long obtenerIdFactura(List<Long> facturaIds) {
        Long idFactura;
        do {
            System.out.println("Ingrese el Id de la Factura a Consultar");
            idFactura = scanner.nextLong();

            if (!facturaIds.contains(idFactura)) {
                System.out.println("El ID ingresado no es valido. Por favor, intente nuevamente.");
            } else {
                break;
            }
        } while (true);

        return idFactura;
    }

    private List<Long> mostrarFacturas() {
        TypedQuery<Long> query = entityManager.createQuery(
                "SELECT DISTINCT f.id FROM Factura f JOIN Detalle d ON f.id = d.factura.id order by f.id ",
                Long.class
        );

        List<Long> facturaIds = query.getResultList();

        for (Long facturaId : facturaIds) {
            Factura factura = entityManager.find(Factura.class, facturaId);

            if (factura != null) {
                System.out.println("ID: " + factura.getId());
                System.out.println("Fecha de Factura: " + factura.getFecha());

                Persona cliente = factura.getCliente();
                if (cliente != null) {
                    System.out.println("Id del Cliente Asociado a esta Factura: " + cliente.getId());
                    System.out.println("Nombre del Cliente " + cliente.getNombres() + " " + cliente.getApellidos());
                }

                Persona vendedor = factura.getVendedor();
                if (vendedor != null) {
                    System.out.println("Id del Vendedor Asociado a esta Factura: " + vendedor.getId());
                    System.out.println("Nombre del Vendedor " + vendedor.getNombres() + " " + vendedor.getApellidos());
                }

                System.out.println("-----------");
            }
        }

        return facturaIds;
    }


}
