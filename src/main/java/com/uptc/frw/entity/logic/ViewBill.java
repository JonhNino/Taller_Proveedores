package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Factura;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class ViewBill {

    public static void viewBill(EntityManager entityManager) {
        Scanner scanner = new Scanner(System.in);
        List<Long> facturaIds = mostrarFacturas(entityManager);
        Long idFactura = null;

        do {
            System.out.println("Ingrese el Id de la Factura a Consultar ");
            idFactura = scanner.nextLong();

            if (!facturaIds.contains(idFactura)) {
                System.out.println("El ID ingresado no es valido. Por favor, intente nuevamente.");
            } else {
                break;
            }
        } while (true);

        queryBill(entityManager, idFactura);
    }


    private static void queryBill(EntityManager entityManager, Long idfactura) {
        entityManager.getTransaction().begin();
        Factura factura = entityManager.getReference(Factura.class, idfactura);
        createFactura(entityManager, factura);
        entityManager.getTransaction().commit();
    }

    private static void createFactura(EntityManager entityManager, Factura factura) {
        System.out.println("Factura #  " + factura.getId());
        System.out.println();
        System.out.println(String.format("Cliente: %-30s Fecha Compra: %s", factura.getCliente().getNombres() + " " + factura.getCliente().getApellidos(), factura.getFecha()));
        System.out.println("Direccion del Cliente: " );
        System.out.println();

        System.out.println(String.format("%-8s %-20s %-10s %-25s %-10s", "Id Item", "Nombre Producto", "Cantidad", "Valor Unitario de Venta", "Valor"));
        System.out.println(String.format("%-8s %-20s %-10s %-25s %-10s", "-------", "----------------", "--------", "-----------------------", "-----")); List<Detalle> detalles =factura.getDetalles();
        double total = 0;
        for (Detalle detalle : detalles) {
            System.out.println(String.format("%-8d %-20s %-10d %-25.2f %-10.2f", detalle.getId(), detalle.getProducto().getNombre(), detalle.getCantidad(), detalle.getPrecioVenta(), (detalle.getPrecioVenta() * detalle.getCantidad())));
            total += detalle.getPrecioVenta() * detalle.getCantidad();
        }

        System.out.println();
        System.out.println(String.format("%65s %-10.2f", "TOTAL", total));
        System.out.println();
        System.out.println("Atendido Por : " + factura.getVendedor().getNombres()+" "+factura.getVendedor().getApellidos());
    }

    private static List<Long> mostrarFacturas(EntityManager entityManager) {
        TypedQuery<Factura> query = entityManager.createQuery("SELECT p FROM Factura p", Factura.class);
        List<Factura> facturas = query.getResultList();
        List<Long> facturaIds = new ArrayList<>();

        for (Factura factura : facturas) {
            facturaIds.add(factura.getId());

            System.out.println("ID: " + factura.getId());
            System.out.println("Fecha de Factura: " + factura.getFecha());
            System.out.println("Id del Cliente Asociado a esta Factura: " + factura.getCliente().getId());
            System.out.println("Nombre del Cliente " + factura.getCliente().getNombres() + " " + factura.getCliente().getApellidos());
            System.out.println("Id del Vendedor Asociado a esta Factura: " + factura.getVendedor().getId());
            System.out.println("Nombre del Vendedor " + factura.getVendedor().getNombres() + " " + factura.getVendedor().getApellidos());
            System.out.println("-----------");
        }

        return facturaIds;
    }

}
