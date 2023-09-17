package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Persona;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueryBill {

    private EntityManager entityManager;
    private Scanner scanner;

    public QueryBill(EntityManager entityManager, Scanner scanner) {
        this.entityManager = entityManager;
        this.scanner = scanner;
    }

    public void queryBills() {
        List<Long> listaIdsClientes = mostrarClientes();

        while (true) {
            System.out.println("Ingrese el Id del Cliente al que se le va a Consultar sus Facturas: ");
            long idCliente = scanner.nextLong();

            if (listaIdsClientes.contains(idCliente)) {
                mostrarFacturas(idCliente);
                break;
            } else {
                System.out.println("Ese no es un Id de cliente válido. Por favor, ingrese otro Id.");
            }
        }
    }


    private void mostrarFacturas(Long idCliente) {
        beginTransaction();

        try {
            TypedQuery<Factura> query = entityManager.createQuery(
                    "SELECT f FROM Factura f WHERE f.cliente.id = :idCliente", Factura.class);
            query.setParameter("idCliente", idCliente);

            Persona cliente = entityManager.find(Persona.class, idCliente);
            System.out.println("El Cliente " + cliente.getNombres() + " " + cliente.getApellidos() + " tiene las siguientes facturas Asociadas:");

            List<Factura> facturas = query.getResultList();

            if (facturas.isEmpty()) {
                System.out.println("No se encontraron facturas para el cliente con ID: " + idCliente);
            } else {
                for (Factura factura : facturas) {
                    System.out.println("ID Factura: " + factura.getId());
                    System.out.println("Fecha: " + factura.getFecha());
                    System.out.println("---------------------------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commitTransaction();
        }
    }

    private List<Long> mostrarClientes() {
        List<Long> listaIdsClientes = new ArrayList<>();
        beginTransaction();

        try {
            TypedQuery<Persona> query = entityManager.createQuery(
                    "SELECT p FROM Persona p INNER JOIN Factura f ON p.id = f.cliente", Persona.class);

            List<Persona> resultados = query.getResultList();

            for (Persona persona : resultados) {
                System.out.print("ID: " + persona.getId());
                System.out.print(" Nombre: " + persona.getNombres());
                System.out.println(" Apellido: " + persona.getApellidos());

                listaIdsClientes.add(persona.getId());
            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            commitTransaction();
        }

        return listaIdsClientes;
    }

    private void beginTransaction() {
        entityManager.getTransaction().begin();
    }

    private void commitTransaction() {
        entityManager.getTransaction().commit();
    }

}
