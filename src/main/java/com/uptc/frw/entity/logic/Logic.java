package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.bdmysql.Persona;
import com.uptc.frw.entity.conf.PersistenceUtil;
import jakarta.persistence.EntityManager;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Scanner;

public class Logic {

    static Scanner scanner = new Scanner(System.in);

    public Logic() throws ParseException {

        EntityManager entityManager = PersistenceUtil.getEntityManager();
        System.out.println("Conexion Mysql OK!");
        System.out.println("Bienvenido al Aplicativo MAPEO PROVEEDORES");
        mostrarMenu(entityManager);
        scanner.close();

    }

    public static void mostrarMenu(EntityManager entityManager) throws ParseException {

        while (true) {
            menu();
            int seleccion = scanner.nextInt();
            switchMenu(seleccion,entityManager);
        }

    }

    private static void menu() {

        System.out.println("Seleccione una opcion del menu:");
        System.out.println("1. Insertar en la base de datos N cantidad de registros en cada tabla");
        System.out.println("2. Consultar todas las facturas de un Cliente");
        System.out.println("3. Consultar los productos que provee un Proveedor");
        System.out.println("4. Mostar el valor total de las ventas de un Vendedor");
        System.out.println("5. Mostrar Factura");
        System.out.println("0. Salir del Aplicativo");

    }

    private static void switchMenu(int seleccion, EntityManager entityManager) throws ParseException {

        switch (seleccion) {
            case 1:
                System.out.println("Seleccionaste la opcion 1, Vamos a crear nuevos registros a la base MAPEO_PROVEEDORES");
                insertData(entityManager);
                break;
            case 2:
                System.out.println("Seleccionaste la opcion 2");
                // Llama aquí a la función para la opción 2
                break;
            case 3:
                System.out.println("Seleccionaste la opcion 3");
                // Llama aquí a la función para la opción 3
                break;
            case 4:
                System.out.println("Seleccionaste la opcion 4");
                // Llama aquí a la función para la opción 4
                break;
            case 5:
                System.out.println("Seleccionaste la opcion 5");
                // Llama aquí a la función para la opción 5
                break;
            case 0:
                System.out.println("Saliendo del aplicativo");
                return;
            default:
                System.out.println("Seleccion no valida, por favor intenta de nuevo.");
        }
    }

    private static void insertData(EntityManager entityManager) throws ParseException {

        System.out.print("¿Cuantos registros deseas ingresar en cada tabla? ");
        int numeroDeRegistros = scanner.nextInt();
        System.out.print("Vamos a Crear " + numeroDeRegistros + " registos en Cada tabla de la BD MAPEO_PROVEEDORES\n");
        System.out.print("Iniciamos Creando los registros para la tabla Personas\n");
        createPerson(entityManager,numeroDeRegistros);
        System.out.print("Ahora Crearemos los registros para la tabla Productos\n");


    }

    private static void createPerson(EntityManager entityManager, int numeroDeRegistros) throws ParseException {

        entityManager.getTransaction().begin();

        SimpleDateFormat formatoFecha = new SimpleDateFormat("yyyy/MM/dd");

        for(int i = 0; i < numeroDeRegistros; i++) {

            System.out.println("Registro de persona " + (i+1));

            System.out.print("Ingrese el nombre: ");
            String nombre = scanner.nextLine();

            System.out.print("Ingrese los apellidos: ");
            String apellidos = scanner.nextLine();

            Date fechaNacimiento = null;
            while(fechaNacimiento == null) {

                System.out.print("Ingrese la fecha de nacimiento (YYYY/MM/DD): ");
                String fechaStr = scanner.nextLine();
                try {
                    fechaNacimiento = formatoFecha.parse(fechaStr);
                } catch(ParseException e) {
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

        entityManager.getTransaction().commit();

    }
}
