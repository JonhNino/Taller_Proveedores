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
                DataService dataService = new DataService(entityManager);
                dataService.insertData(entityManager);
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
}
