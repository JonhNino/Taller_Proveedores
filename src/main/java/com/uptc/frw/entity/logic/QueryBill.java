package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.List;
import java.util.Scanner;

public class QueryBill {
    private EntityManager entityManager;

    public QueryBill(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void queryBills(EntityManager entityManager) {
        Scanner scanner = new Scanner(System.in);
        mostrarClientes(entityManager);
        System.out.print("¿Ingrese el Id del Cliente al que se le va a Consultar sus Facturas? ");
        int idCliente = scanner.nextInt();
        mostrarFacturas(entityManager, idCliente);

    }

    private static void mostrarFacturas(EntityManager entityManager, int idCliente) {
        entityManager.getTransaction().begin();

        try {
            TypedQuery<Factura> query = entityManager.createQuery(
                    "SELECT f FROM Factura f WHERE f.cliente.id = :idCliente", Factura.class);
            query.setParameter("idCliente", idCliente);

            List<Factura> facturas = query.getResultList();

            if (facturas.isEmpty()) {
                System.out.println("No se encontraron facturas para el cliente con ID: " + idCliente);
            } else {
                for (Factura factura : facturas) {
                    System.out.println("El Vendedor " + factura.getVendedor().getNombres()
                            + factura.getVendedor().getApellidos() + " tiene las siguientes facturas Asociadas");
                    System.out.println("ID Factura: " + factura.getId());
                    System.out.println("Fecha: " + factura.getFecha());

                    System.out.println("---------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.getTransaction().commit();
        }
    }


    private static void mostrarClientes(EntityManager entityManager) {
        entityManager.getTransaction().begin();

        try {
            TypedQuery<Persona> query = entityManager.createQuery(
                    "SELECT p FROM Persona p INNER JOIN Factura f ON p.id = f.cliente", Persona.class);

            List<Persona> resultados = query.getResultList();

            for (Persona persona : resultados) {
                System.out.print("ID: " + persona.getId());
                System.out.print(" Nombre: " + persona.getNombres());
                System.out.println("Apellido: " + persona.getApellidos());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.getTransaction().commit();
        }
    }

}
