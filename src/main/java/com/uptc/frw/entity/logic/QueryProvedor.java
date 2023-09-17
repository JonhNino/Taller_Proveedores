package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Persona;
import com.uptc.frw.entity.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class QueryProvedor {

    public static void queryProduct(EntityManager entityManager) {
        Scanner scanner = new Scanner(System.in);
        List<Long> proveedorIds = mostrarProveedores(entityManager);

        while (true) {
            System.out.println("Ingrese el Id del Proveedor al que se le va a Consultar los Productos que Provee: ");
            long idProveedor = scanner.nextLong();

            if (proveedorIds.contains(idProveedor)) {
                mostrarProductos(entityManager,idProveedor);
                break;
            } else {
                System.out.println("Este Id no corresponde a un proveedor valido. Por favor, intente de nuevo.");
            }
        }
    }


    private static List<Long> mostrarProveedores(EntityManager entityManager) {
        List<Long> proveedorIds = new ArrayList<>();
        entityManager.getTransaction().begin();
        System.out.println("Lista De Proveedores.");
        try {
            TypedQuery<Persona> query = entityManager.createQuery(
                    "SELECT DISTINCT p FROM Persona p JOIN FETCH p.productos",
                    Persona.class
            );
            List<Persona> proveedores = query.getResultList();

            if(proveedores.isEmpty()) {
                System.out.println("No se encontraron proveedores.");
            } else {
                for (Persona proveedor : proveedores) {
                    System.out.print("ID: " + proveedor.getId());
                    System.out.print(" Nombre: " + proveedor.getNombres());
                    System.out.println(" Apellidos: " + proveedor.getApellidos());
                    System.out.println("---------------------------");
                    proveedorIds.add(proveedor.getId());
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            entityManager.getTransaction().commit();
        }
        return proveedorIds;
    }



    public static void mostrarProductos(EntityManager entityManager, long idProveedor) {
        try {
            TypedQuery<Producto> query = entityManager.createQuery(
                    "SELECT p FROM Producto p JOIN p.personas pers WHERE pers.id = :idProveedor",
                    Producto.class
            );
            query.setParameter("idProveedor", idProveedor);

            List<Producto> productos = query.getResultList();

            if(productos.isEmpty()) {
                System.out.println("No se encontraron productos para el proveedor con ID: " + idProveedor);
            } else {
                Persona persona =entityManager.getReference(Persona.class,idProveedor);
                System.out.println("El Proveedor "+persona.getNombres()+" "+persona.getApellidos()+" tiene los Siguentes productos asociados:");
                for (Producto producto : productos) {
                    System.out.println("ID: " + producto.getId());
                    System.out.println("Nombre: " + producto.getNombre());
                    System.out.println("-----------");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
