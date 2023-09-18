package com.uptc.frw.entity.logic;

import com.uptc.frw.entity.model.Detalle;
import com.uptc.frw.entity.model.Factura;
import com.uptc.frw.entity.model.Persona;
import com.uptc.frw.entity.model.Producto;
import jakarta.persistence.EntityManager;
import jakarta.persistence.TypedQuery;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.*;

public class DataService {

    private EntityManager entityManager;
    private static final String DATE_PATTERN = "yyyy/MM/dd";
    private static final SimpleDateFormat DATE_FORMAT = new SimpleDateFormat(DATE_PATTERN);


    public DataService(EntityManager entityManager) {
        this.entityManager = entityManager;
    }

    public static void insertData(EntityManager entityManager) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        System.out.print("¿Cuantos registros deseas ingresar en cada tabla? ");
        int numeroDeRegistros = scanner.nextInt();
        if (numeroDeRegistros == 0) {
            System.out.println("Has ingresado 0, no se crearan registros. Continuando con el programa...");
            return;
        }
        iniciarInsercionDeDatos(scanner, numeroDeRegistros, entityManager);
    }

    private static void iniciarInsercionDeDatos(Scanner scanner, int numeroDeRegistros, EntityManager entityManager) throws ParseException {
        System.out.print("Vamos a Crear " + numeroDeRegistros + " registros en Cada tabla de la BD MAPEO_PROVEEDORES\n");
        System.out.print("Vamos a Crear " + numeroDeRegistros + " registos en Cada tabla de la BD MAPEO_PROVEEDORES\n");
        System.out.print("Iniciamos Creando los registros para la tabla Productos\n");
        createProductos(entityManager,numeroDeRegistros );
        System.out.println("-----------");
        System.out.print("Ahora Crearemos los registros para la tabla Personas\n");
        createPerson(entityManager,numeroDeRegistros);
        System.out.println("-----------");
        System.out.print("Ahora Crearemos los registros para la tabla Facturas\n");
        createFacturas(entityManager,numeroDeRegistros);
        System.out.println("-----------");
        System.out.print("Por Ultimo Crearemos los registros para la tabla Detalles Facturas\n");
        createDetallesFacturas(entityManager,numeroDeRegistros);
        System.out.println("-----------");
    }

    private static void createDetallesFacturas(EntityManager entityManager, int numeroDeRegistros) {
        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);

        for (int i = 0; i < numeroDeRegistros; i++) {
            boolean agregarOtroDetalle = true;


            System.out.println("Creacion del Detalle " + (i + 1));
            mostrarTodosLosFacturas(entityManager);
            System.out.print("Ingrese el id de la Factura a detallar : ");
            Long idFactrura = scanner.nextLong();
            scanner.nextLine();
            Factura factura = entityManager.find(Factura.class, idFactrura);
            while (agregarOtroDetalle) {
                System.out.print("Ingrese la cantidad de Productos a Cobrar: ");
                Integer cantidad = scanner.nextInt();
                scanner.nextLine();

                Double precioVenta = null;
                Producto producto = null;
                while (precioVenta == null) {
                    try {
                        mostrarTodosLosProductosCliente(entityManager);
                        System.out.print("Ingrese el id del Producto a detallar : ");
                        Long idProducto = scanner.nextLong();
                        scanner.nextLine();
                        producto = entityManager.find(Producto.class, idProducto);
                        precioVenta = cantidad * producto.getPrecioUnitario();
                        System.out.print("El precio de venta del Producto " + producto.getNombre() + " Es: " + precioVenta);
                        scanner.nextLine();
                    } catch (InputMismatchException e) {
                        System.out.println("Precio no válido. Por favor, inténtelo de nuevo.");
                        scanner.next();
                    }
                }

                Detalle detalle = new Detalle(cantidad, precioVenta, factura, producto);
                entityManager.persist(detalle);

                System.out.print("¿Desea agregar otro detalle al detalle " + (i + 1) + "? (s/n): ");
                String respuesta = scanner.nextLine();
                agregarOtroDetalle = respuesta.equalsIgnoreCase("s");
            }
        }
        entityManager.getTransaction().commit();
    }


    private static void mostrarTodosLosProductosCliente(EntityManager entityManager) {
        TypedQuery<Producto> query = entityManager.createQuery(
                "SELECT p FROM Producto p",
                Producto.class
        );
        List<Producto> productos = query.getResultList();
        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getId() +
                    ", Nombre: " + producto.getNombre() +
                    ", Precio Unitario: " + producto.getPrecioUnitario());
        }
    }


    public static List<Long> mostrarTodosLosFacturas(EntityManager entityManager) {
        TypedQuery<Factura> query = entityManager.createQuery("SELECT f FROM Factura f", Factura.class);
        List<Factura> facturas = query.getResultList();

        List<Long> facturaIds = new ArrayList<>();
        for (Factura factura : facturas) {
            facturaIds.add(factura.getId());
            imprimirDetallesDeFactura(factura);
        }
        return facturaIds;
    }

    private static void imprimirDetallesDeFactura(Factura factura) {
        System.out.println("ID: " + factura.getId());
        System.out.println("Fecha de Factura: " + factura.getFecha());
        System.out.println("Id del Cliente Asociado a esta Factura: " + factura.getCliente().getId());
        System.out.println("Nombre del Cliente " + factura.getCliente().getNombres() + " " + factura.getCliente().getApellidos());
        System.out.println("Id del Vendedor Asociado a esta Factura: " + factura.getVendedor().getId());
        System.out.println("Nombre del Vendedor " + factura.getVendedor().getNombres() + " " + factura.getVendedor().getApellidos());
        System.out.println("-----------");
    }

    private static void createFacturas(EntityManager entityManager, int numeroDeRegistros) {
        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("[yyyy/M/d][yyyy/MM/dd]");
        for (int i = 0; i < numeroDeRegistros; i++) {
            int iFact = i + 1;
            System.out.print("Ingrese la fecha de la factura " + iFact);
            LocalDate fechaFact = obtenerFecha(scanner, formatoFecha);
            Persona cliente = obtenerPersonaPorID(entityManager, scanner, "cliente", i + 1);
            Persona vendedor = obtenerPersonaPorID(entityManager, scanner, "vendedor", i + 1);
            Factura factura = new Factura(fechaFact, cliente, vendedor);
            entityManager.persist(factura);
        }
        entityManager.getTransaction().commit();
    }

    public static Persona obtenerPersonaPorID(EntityManager entityManager, Scanner scanner, String tipoPersona, int numeroFactura) {
        Persona persona = null;
        mostrarPersonasConProductos(entityManager, tipoPersona);
        while (persona == null) {
            System.out.print("Ingrese el ID del " + tipoPersona + " para la factura " + numeroFactura + ": ");
            Long idPersona = scanner.nextLong();
            scanner.nextLine();
            persona = entityManager.find(Persona.class, idPersona);

            if (persona == null) {
                System.out.println("No se encontró un " + tipoPersona + " con el ID " + idPersona + ". Intentelo de nuevo.");
            }
        }
        return persona;
    }

    public static void mostrarPersonasConProductos(EntityManager entityManager, String tipoPersona) {

        TypedQuery<Persona> query;


        if (tipoPersona.equalsIgnoreCase("Cliente")) {
            query = entityManager.createQuery("SELECT p FROM Persona p WHERE NOT EXISTS (SELECT 1 FROM p.productos)", Persona.class);
        } else if (tipoPersona.equalsIgnoreCase("Vendedor")) {
            query = entityManager.createQuery("SELECT p FROM Persona p WHERE NOT EXISTS (SELECT 1 FROM p.productos)", Persona.class);
        } else {
            throw new IllegalArgumentException("Tipo de persona no reconocido: " + tipoPersona);
        }

        List<Persona> personas = query.getResultList();

        for (Persona persona : personas) {

            System.out.println("ID: " + persona.getId());
            System.out.println("Nombre: " + persona.getNombres());
            System.out.println("Apellidos: " + persona.getApellidos());
            System.out.println("-----------");

        }

    }


    private static void createProductos(EntityManager entityManager, int numeroDeRegistros) {

        entityManager.getTransaction().begin();
        Scanner scanner = new Scanner(System.in);
        for (int i = 0; i < numeroDeRegistros; i++) {
            System.out.println("Creacion de producto " + (i + 1));

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
        entityManager.getTransaction().commit();
    }

    private static void createPerson(EntityManager entityManager, int numeroDeRegistros) throws ParseException {

        Scanner scanner = new Scanner(System.in);
        DateTimeFormatter formatoFecha = DateTimeFormatter.ofPattern("[yyyy/M/d][yyyy/MM/dd]");
        for (int i = 0; i < numeroDeRegistros; i++) {

            System.out.println("Registro de persona " + (i + 1));
            Persona persona = obtenerInformacionPersona(scanner, formatoFecha);

            if (!entityManager.getTransaction().isActive()) {
                entityManager.getTransaction().begin();
            }

            entityManager.persist(persona);
            agregarProductosAPersona(entityManager, scanner, persona);

        }

        entityManager.getTransaction().commit();
    }

    private static void agregarProductosAPersona(EntityManager entityManager, Scanner scanner, Persona persona) {

        System.out.print("¿Desea marcar a " + persona.getNombres() + " " + persona.getApellidos() + " como proveedor? (s/n): ");
        String respuestaProveedor = scanner.nextLine();

        if (respuestaProveedor.equalsIgnoreCase("s")) {
            while (true) {
                System.out.print("¿Desea agregar un producto al proveedor " + persona.getNombres() + " " + persona.getApellidos() + "? (s/n): ");
                String respuesta = scanner.nextLine();

                if (respuesta.equalsIgnoreCase("n")) {
                    break;
                }

                mostrarTodosLosProductos(entityManager);
                ingresarProductoProveedor(entityManager, scanner, persona);
            }
        } else {
            System.out.println(persona.getNombres() + " " + persona.getApellidos() + " no es marcado como proveedor.");
        }
    }


    public static void ingresarProductoProveedor(EntityManager entityManager, Scanner scanner, Persona persona) {
        System.out.print("Ingrese el ID del Producto para la Persona " + persona.getNombres() + ": ");
        Long idProducto = scanner.nextLong();
        scanner.nextLine();
        Producto producto = entityManager.find(Producto.class, idProducto);
        if (producto != null) {
            if (persona.getProductos() == null) {
                persona.setProductos(new ArrayList<>());
            }
            if (producto.getPersonas() == null) {
                producto.setPersonas(new ArrayList<>());
            }
            persona.getProductos().add(producto);
            producto.getPersonas().add(persona);
            entityManager.persist(persona);
            entityManager.persist(producto);
        } else {
            System.out.println("Producto no encontrado con el ID: " + idProducto);
        }
    }

    public static void mostrarTodosLosProductos(EntityManager entityManager) {
        TypedQuery<Producto> query = entityManager.createQuery("SELECT p FROM Producto p", Producto.class);
        List<Producto> productos = query.getResultList();
        for (Producto producto : productos) {
            System.out.println("ID: " + producto.getId());
            System.out.println("Nombre: " + producto.getNombre());
            System.out.println("Precio Unitario: " + producto.getPrecioUnitario());
            System.out.println("-----------");
        }
    }

    private static Persona obtenerInformacionPersona(Scanner scanner, DateTimeFormatter formatoFecha) {
        System.out.print("Ingrese el nombre: ");
        String nombre = scanner.nextLine();
        System.out.print("Ingrese los apellidos: ");
        String apellidos = scanner.nextLine();
        System.out.print("Ingrese la fecha de nacimiento ");
        LocalDate fechaNacimiento = obtenerFecha(scanner, formatoFecha);
        System.out.print("Ingrese el tipo de documento: ");
        String tipoDocumento = scanner.nextLine();
        System.out.print("Ingrese el numero de documento: ");
        String numeroDocumento = scanner.nextLine();
        System.out.print("Ingrese la Direccion de la Persona : ");
        String direccion = scanner.nextLine();
        return new Persona(nombre, apellidos, fechaNacimiento, tipoDocumento, numeroDocumento, direccion);
    }

    private static LocalDate obtenerFecha(Scanner scanner, DateTimeFormatter formatoFecha) {
        LocalDate fecha = null;
        while (fecha == null) {
            System.out.print("Ingrese la fecha (YYYY/MM/DD): ");
            String fechaStr = scanner.nextLine();
            try {
                fecha = LocalDate.parse(fechaStr, formatoFecha);
            } catch (DateTimeParseException e) {
                System.out.println("Fecha no valida. Por favor, intentelo de nuevo.");
            }
        }
        return fecha;
    }


}
