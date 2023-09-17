package com.uptc.frw.entity.logic;


import com.uptc.frw.entity.conf.PersistenceUtil;
import jakarta.persistence.EntityManager;

import java.text.ParseException;
import java.util.Scanner;

public class Logic {

    static Scanner scanner = new Scanner(System.in);

    public Logic() throws ParseException {

        EntityManager entityManager = PersistenceUtil.getEntityManager();

        System.out.println("Connexion Mysql OK!");
        System.out.println("Bienvenido al Aplicativo MAPEO PROVEEDORES");
        System.out.println("Agregaremos data a la BD Taller_Proveedores");
        DataService.insertData(entityManager);
        System.out.println("***********FIN INSERCION DATOS****************");
        mostrarMenu(entityManager);

        scanner.close();

    }

    private static void mostrarMenu(EntityManager entityManager) throws ParseException {

        while (true) {
            menu();
            int seleccion = scanner.nextInt();
            switchMenu(seleccion, entityManager);
        }

    }

    private static void menu() {

        System.out.println("Seleccione una opcion del menu:");
        System.out.println("1. Consultar todas las facturas de un Cliente.");
        System.out.println("2. Consultar los productos que provee un Proveedor.");
        System.out.println("3. Mostar el valor total de las ventas de un Vendedor.");
        System.out.println("4. Mostrar Factura.");
        System.out.println("0. Salir del Aplicativo.");

    }

    private static void switchMenu(int seleccion, EntityManager entityManager) throws ParseException {
        Scanner scanner = new Scanner(System.in);
        boolean repetir;

        do {
            repetir = false;

            switch (seleccion) {
                case 1:
                    do {
                        System.out.println("Seleccionaste la opcion 1.");
                        System.out.println("Metodo que permite consultar todas las facturas de un cliente");
                        QueryBill.queryBills(entityManager);
                        System.out.println("-----------");
                        repetir = preguntaRepetir();
                    } while (repetir);
                    break;
                case 2:
                    do {
                        System.out.println("Seleccionaste la opcion 2.");
                        System.out.println("Metodo que permite consultar los productos que provee un proveedor.");
                        QueryProvedor.queryProduct(entityManager);
                        System.out.println("-----------");
                        repetir = preguntaRepetir();
                    } while (repetir);
                    break;
                case 3:
                    do {
                        System.out.println("Seleccionaste la opcion 3.");
                        System.out.println("Metodo que a partir del id del vendedor muestre el valor total de las ventas de este.");
                        TotalVentas.totalVentas(entityManager);
                        System.out.println("-----------");
                        repetir = preguntaRepetir();
                    } while (repetir);
                    break;
                case 4:
                    do {
                        System.out.println("Seleccionaste la opcion 4.");
                        System.out.println("Metodo que  al ingresar un identificador de factura muestre por consola.");
                        ViewBill.viewBill(entityManager);
                        System.out.println("-----------");
                        repetir = preguntaRepetir();
                    } while (repetir);
                    break;
                case 0:
                    System.out.println("Saliendo del aplicativo.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Seleccion no valida, por favor intenta de nuevo.");
                    repetir = true;
            }
        } while (repetir);
    }

    private static boolean preguntaRepetir() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("¿Desea repetir esta tarea? (s/n): ");
        String respuesta = scanner.nextLine();
        return respuesta.equalsIgnoreCase("s");
    }

}
