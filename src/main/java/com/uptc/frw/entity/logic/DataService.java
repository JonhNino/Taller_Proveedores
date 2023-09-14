package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.bdmysql.Persona;
import com.uptc.frw.entity.bdmysql.Producto;
import jakarta.persistence.EntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.InputMismatchException;
import java.util.Scanner;

public class DataService {
    private EntityManager entityManager;
    static Scanner scanner = new Scanner(System.in);


    public DataService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void insertData(EntityManager entityManager) throws ParseException {

        System.out.print("¿Cuantos registros deseas ingresar en cada tabla? ");
        int numeroDeRegistros = scanner.nextInt();
        System.out.print("Vamos a Crear " + numeroDeRegistros + " registos en Cada tabla de la BD MAPEO_PROVEEDORES\n");
        entityManager.getTransaction().begin();
        System.out.print("Iniciamos Creando los registros para la tabla Personas\n");
        createPerson(entityManager, numeroDeRegistros);
        System.out.print("Ahora Crearemos los registros para la tabla Productos\n");
        createProductos(entityManager, numeroDeRegistros);

        entityManager.getTransaction().commit();

    }

    private static void createProductos(EntityManager entityManager, int numeroDeRegistros) {
        Scanner scanner = new Scanner(System.in);
        for(int i = 0; i < numeroDeRegistros; i++) {
            System.out.println("Creacion de producto " + (i+1));

            System.out.print("Ingrese el nombre del producto: ");
            String nombre = scanner.nextLine();

            Double precioUnitario = null;
            while (precioUnitario == null) {
                try {
                    System.out.print("Ingrese el precio unitario del producto: ");
                    precioUnitario = scanner.nextDouble();
                    scanner.nextLine();
                } catch (InputMismatchException e) {
                    System.out.println("Precio no válido. Por favor, inténtelo de nuevo.");
                    scanner.next();
                }
            }

            Producto producto = new Producto(nombre, precioUnitario);
            entityManager.persist(producto);
        }
    }

    private static void createPerson(EntityManager entityManager, int numeroDeRegistros) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");
        for (int i = 0; i < numeroDeRegistros; i++) {

            System.out.println("Registro de persona " + (i + 1));

            System.out.print("Ingrese el nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese los apellidos: ");
            String apellidos = scanner.nextLine();

            Date fechaNacimiento = null;
            while (fechaNacimiento == null) {
                System.out.print("Ingrese la fecha de nacimiento (YYYY/MM/DD): ");
                String fechaStr = scanner.nextLine();
                try {
                    fechaNacimiento = formatoFecha.parse(fechaStr);
                } catch (ParseException e) {
                    System.out.println("Fecha no válida. Por favor, inténtelo de nuevo.");
                }
            }

            System.out.print("Ingrese el tipo de documento: ");
            String tipoDocumento = scanner.nextLine();

            System.out.print("Ingrese el número de documento: ");
            String numeroDocumento = scanner.nextLine();

            Persona persona = new Persona(nombre, apellidos, fechaNacimiento, tipoDocumento, numeroDocumento);
            entityManager.persist(persona);
        }
    }
}
