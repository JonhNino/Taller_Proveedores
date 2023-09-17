package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class TotalVentas {
    public static void totalVentas(EntityManager entityManager) {
        Scanner scanner = new Scanner(System.in);
        List<Long> vendedorIds = mostrarVendedores(entityManager);

        long idVendedor = -1;
        boolean idValido = false;

        while (!idValido) {
            System.out.println("Ingrese el Id del Vendedor al que se le va a Consultar el total de sus Ventas");
            idVendedor = scanner.nextLong();

            if (vendedorIds.contains(idVendedor)) {
                idValido = true;
            } else {
                System.out.println("El ID ingresado no pertenece a un vendedor. Por favor, intente de nuevo.");
            }
        }

        System.out.println(queryVentas(entityManager,idVendedor));
        System.out.println(queryDetalles(entityManager,queryVentas(entityManager,idVendedor)));
    }

    private static double queryDetalles(EntityManager entityManager, List<Long> facturaIds) {
        double totalVentas = 0.0;
        try {
            entityManager.getTransaction().begin();

            for(Long facturaId : facturaIds) {
                TypedQuery<Detalle> query = entityManager.createQuery(
                        "SELECT d FROM Detalle d WHERE d.factura.id = :facturaId", Detalle.class);
                query.setParameter("facturaId", facturaId);

                List<Detalle> detalles = query.getResultList();

                for (Detalle detalle : detalles) {
                    System.out.println("ID Factura: " + facturaId);
                    System.out.println("ID Detalle: " + detalle.getId());
                    System.out.println("Precio Venta: " + detalle.getPrecioVenta());
                    totalVentas += detalle.getPrecioVenta();  // Sumamos el precio de venta a totalVentas
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
        return totalVentas;  // Retornamos el total de las ventas
    }



    private static List<Long> queryVentas(EntityManager entityManager, long idVendedor) {
        List<Long> facturaIds = new ArrayList<>();
        try {
            entityManager.getTransaction().begin();

            TypedQuery<Long> query = entityManager.createQuery(
                    "SELECT f.id FROM Factura f WHERE f.vendedor.id = :idVendedor", Long.class);
            query.setParameter("idVendedor", idVendedor);

            facturaIds = query.getResultList();

            entityManager.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
            if (entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().rollback();
            }
        }

        return facturaIds;
    }



    private static List<Long> mostrarVendedores(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        List<Long> vendedorIds = new ArrayList<>();

        try {
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
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.getTransaction().commit();
        }

        return vendedorIds;
    }


}
