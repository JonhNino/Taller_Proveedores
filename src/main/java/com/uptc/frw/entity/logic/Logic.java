package com.uptc.frw.entity.logic;


import com.uptc.frw.entity.conf.PersistenceUtil;
import jakarta.persistence.EntityManager;

import java.text.ParseException;
import java.util.Scanner;

public class Logic {

    private static final Scanner scanner = new Scanner(System.in);
    private final EntityManager entityManager;

    public Logic() throws ParseException {
        this.entityManager = PersistenceUtil.getEntityManager();
        init();
    }

    private void init() throws ParseException {
        System.out.println("Connexion Mysql OK!");
        System.out.println("Bienvenido al Aplicativo MAPEO PROVEEDORES");
        System.out.println("Agregaremos data a la BD Taller_Proveedores");
        DataService.insertData(entityManager);
        System.out.println("***********FIN INSERCION DATOS****************");
        mostrarMenu();
    }

    private void mostrarMenu() {
        while (true) {
            mostrarOpcionesMenu();
            int seleccion = obtenerSeleccionMenu();
            procesarSeleccionMenu(seleccion);
        }
    }

    private void mostrarOpcionesMenu() {
        System.out.println("Seleccione una opcion del menu:");
        System.out.println("1. Consultar todas las facturas de un Cliente.");
        System.out.println("2. Consultar los productos que provee un Proveedor.");
        System.out.println("3. Mostar el valor total de las ventas de un Vendedor.");
        System.out.println("4. Mostrar Factura.");
        System.out.println("0. Salir del Aplicativo.");
    }

    private int obtenerSeleccionMenu() {
        return scanner.nextInt();
    }

    private void procesarSeleccionMenu(int seleccion) {
        boolean repetir;
        do {
            repetir = false;
            switch (seleccion) {
                case 1:
                    System.out.println("Seleccionaste la opcion 1.");
                    System.out.println("Metodo que permite consultar todas las facturas de un cliente");
                    QueryBill queryBill = new QueryBill(entityManager, scanner);
                    queryBill.queryBills();
                    System.out.println("-----------");
                    break;
                case 2:
                    System.out.println("Seleccionaste la opcion 2.");
                    System.out.println("Metodo que permite consultar los productos que provee un proveedor.");
                    QueryProvedor queryProvedor = new QueryProvedor(scanner);
                    queryProvedor.queryProduct(entityManager);
                    System.out.println("-----------");
                    break;
                case 3:
                    System.out.println("Seleccionaste la opcion 3.");
                    System.out.println("Metodo que a partir del id del vendedor muestre el valor total de las ventas de este.");
                    TotalVentas totalVentas = new TotalVentas(scanner);
                    totalVentas.mostrarTotalVentas(entityManager);
                    System.out.println("-----------");
                    break;
                case 4:
                    System.out.println("Seleccionaste la opcion 4.");
                    System.out.println("Metodo que  al ingresar un identificador de factura muestre por consola.");
                    ViewBill viewBillInstance = new ViewBill(entityManager, scanner);
                    viewBillInstance.viewBill();
                    System.out.println("-----------");
                    break;
                case 0:
                    System.out.println("Saliendo del aplicativo.");
                    System.exit(0);
                    return;
                default:
                    System.out.println("Seleccion no valida, por favor intenta de nuevo.");
                    repetir = true;
            }
            repetir = preguntarSiRepetir();
        } while (repetir);
    }


    private boolean preguntarSiRepetir() {
        System.out.println("¿Desea repetir esta tarea? (s/n): ");
        String respuesta = scanner.next();
        return respuesta.equalsIgnoreCase("s");
    }

    public static void main(String[] args) throws ParseException {
        new Logic();
    }

}
