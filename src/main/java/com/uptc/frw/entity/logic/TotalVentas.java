package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TotalVentas {
    private final Scanner scanner;

    public TotalVentas(Scanner scanner) {
        this.scanner = scanner;
    }

    public void mostrarTotalVentas(EntityManager entityManager) {
        List<Long> vendedorIds = obtenerVendedorIds(entityManager);

        long idVendedor;
        while (true) {
            System.out.println("Ingrese el ID del vendedor para consultar el total de sus ventas:");
            idVendedor = scanner.nextLong();

            if (vendedorIds.contains(idVendedor)) {
                break;
            } else {
                System.out.println("El ID ingresado no pertenece a un vendedor. Por favor, intente de nuevo.");
            }
        }

        List<Long> facturaIds = obtenerFacturaIdsPorVendedor(entityManager, idVendedor);
        Persona vendedor = entityManager.getReference(Persona.class, idVendedor);
        System.out.println("El vendedor " + vendedor.getNombres() + " " + vendedor.getApellidos()
                + " ha realizado " + facturaIds.size() + " ventas, con un total de: " +
                calcularTotalVentas(entityManager, facturaIds));
    }

    private double calcularTotalVentas(EntityManager entityManager, List<Long> facturaIds) {
        double totalVentas = 0.0;

        try {
            entityManager.getTransaction().begin();

            for (Long facturaId : facturaIds) {
                TypedQuery<Detalle> query = entityManager.createQuery(
                        "SELECT d FROM Detalle d WHERE d.factura.id = :facturaId", Detalle.class);
                query.setParameter("facturaId", facturaId);

                List<Detalle> detalles = query.getResultList();

                for (Detalle detalle : detalles) {
                    System.out.println("ID Factura: " + facturaId);
                    System.out.println("ID Detalle: " + detalle.getId());
                    System.out.println("Precio Venta: " + detalle.getPrecioVenta());
                    totalVentas += detalle.getPrecioVenta();
                    System.out.println("-----------");
                }
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return totalVentas;
    }

    private List<Long> obtenerFacturaIdsPorVendedor(EntityManager entityManager, long idVendedor) {
        List<Long> facturaIds = new ArrayList<>();

        try {
            entityManager.getTransaction().begin();

            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT f.id FROM Factura f WHERE f.vendedor.id = :idVendedor", Long.class);
            query.setParameter("idVendedor", idVendedor);

            facturaIds = query.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            gestionarExcepcionTransaccion(entityManager, e);
        }

        return facturaIds;
    }

    private List<Long> obtenerVendedorIds(EntityManager entityManager) {
        List<Long> vendedorIds = new ArrayList<>();

        try {
            entityManager.getTransaction().begin();

            TypedQuery<Persona> query = entityManager.createQuery(
                    "SELECT DISTINCT p FROM Persona p INNER JOIN p.facturasComoVendedor f", Persona.class);

            List<Persona> resultados = query.getResultList();

            if (resultados.isEmpty()) {
                System.out.println("No se encontraron vendedores.");
            } else {
                for (Persona persona : resultados) {
                    vendedorIds.add(persona.getId());
                    System.out.print("ID: " + persona.getId());
                    System.out.print(" Nombre: " + persona.getNombres());
                    System.out.println(" Apellido: " + persona.getApellidos());
                }
            }

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            gestionarExcepcionTransaccion(entityManager, e);
        }

        return vendedorIds;
    }

    private void gestionarExcepcionTransaccion(EntityManager entityManager, Exception e) {
        e.printStackTrace();
        if (entityManager.getTransaction().isActive()) {
            entityManager.getTransaction().rollback();
        }
    }
}

