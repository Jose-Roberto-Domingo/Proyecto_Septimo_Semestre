package com.example.e_commerce;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class FirestoreSetup {

    public static void poblarFirestore() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();

        // Lista de productos originales (Walmart)
        Map<String, Object>[] productos = new Map[20];
        productos[0] = crearProducto("1", "Laptop HP 15", "Electrónica", 500.99, "https://th.bing.com/th/id/OIP.Tub0dCPlAlsupb7aQvi1lQHaHa?w=500&h=500&rs=1&pid=ImgDetMain", "Walmart");
        productos[1] = crearProducto("2", "Samsung TV 50\"", "Electrónica", 400.50, "https://th.bing.com/th/id/OIP.7alX37U0UZjLJLjkC8KqZwAAAA?rs=1&pid=ImgDetMain", "Walmart");
        productos[2] = crearProducto("3", "Audífonos Sony WH-1000XM4", "Electrónica", 300.00, "https://th.bing.com/th/id/R.078669461024b334bf96ad520c57c0f6?rik=vNMJZyvs3lJOow&pid=ImgRaw&r=0", "Walmart");
        productos[3] = crearProducto("4", "iPhone 14", "Celulares", 799.99, "https://th.bing.com/th/id/OIP.VjmMFF8dzwAWsq3J2DmiVwHaKI?rs=1&pid=ImgDetMain", "Walmart");
        productos[4] = crearProducto("5", "Fritura Pringles", "Alimentos", 2.50, "https://th.bing.com/th/id/R.86311134091b84529a025360b3e6d5b1?rik=flOoDcld7pMmnw&pid=ImgRaw&r=0", "Walmart");
        productos[5] = crearProducto("6", "Camiseta Adidas", "Ropa", 25.00, "https://th.bing.com/th/id/R.b2e2270e97a93340da814340960eb89d?rik=HrsW3Y4XHyT5tw&pid=ImgRaw&r=0", "Walmart");
        productos[6] = crearProducto("7", "Zapatos Nike Air Max", "Ropa", 120.00, "https://i.ebayimg.com/images/g/S8MAAOSwHRZnIV0V/s-l1600.webp", "Walmart");
        productos[7] = crearProducto("8", "Batidora Oster", "Hogar", 45.99, "https://th.bing.com/th/id/R.dbc0ed184d05b1ae225842b0c45867a5?rik=j1mkRuPLG7ZPbA&pid=ImgRaw&r=0", "Walmart");
        productos[8] = crearProducto("9", "Licuadora Ninja", "Hogar", 60.00, "https://th.bing.com/th/id/OIP.iWOZb8ID5zepkaREhWaw-AAAAA?rs=1&pid=ImgDetMain", "Walmart");
        productos[9] = crearProducto("10", "Colchón Memory Foam", "Hogar", 300.00, "https://th.bing.com/th/id/OIP.MEqlL883oG-w26O8rD4p_wAAAA?rs=1&pid=ImgDetMain", "Walmart");
        productos[10] = crearProducto("11", "Set de cuchillos Tramontina", "Cocina", 50.00, "https://th.bing.com/th/id/OIP.bjmzA2YUXDSH19qeMu4e2QHaHa?rs=1&pid=ImgDetMain", "Walmart");
        productos[11] = crearProducto("12", "Juego de ollas T-fal", "Cocina", 120.00, "https://th.bing.com/th/id/OIP.Rd5u7uC7TBtTybz6kMZkYgHaHa?rs=1&pid=ImgDetMain", "Walmart");
        productos[12] = crearProducto("13", "Mouse Logitech MX Master 3", "Electrónica", 99.99, "https://th.bing.com/th/id/OIP.eRbukzQIStCfW2MPS0BkFAHaDX?rs=1&pid=ImgDetMain", "Walmart");
        productos[13] = crearProducto("14", "Teclado Mecánico Razer", "Electrónica", 120.00, "https://media.gamestop.com/i/gamestop/11155920_ALT01/Razer-BlackWidow-V3-Mini-HyperSpeed-65-Percent-Wireless-Green-Switch-Mechanical-Gaming-Keyboard-Black-with-Chroma-RGB?fmt=auto", "Walmart");
        productos[14] = crearProducto("15", "Cafetera Nespresso", "Hogar", 180.00, "https://th.bing.com/th/id/R.13f70c7d6daa78299f62adfa100c5a7c?rik=t%2f1AnYEoR%2bSXFw&pid=ImgRaw&r=0", "Walmart");
        productos[15] = crearProducto("16", "Cámara Canon EOS Rebel", "Fotografía", 500.00, "https://www.bhphotovideo.com/images/images2000x2000/canon_9126b003_eos_a_rebel_t5_dslr_1030209.jpg", "Walmart");
        productos[16] = crearProducto("17", "Smartwatch Fitbit", "Accesorios", 99.00, "https://pisces.bbystatic.com/image2/BestBuy_US/images/products/6514/6514033_rd.jpg", "Walmart");
        productos[17] = crearProducto("18", "Smartwatch Samsung Galaxy", "Accesorios", 250.00, "https://th.bing.com/th/id/OIP.2KSHgZbV80hG2doNiTe-xwHaHa?rs=1&pid=ImgDetMain", "Walmart");
        productos[18] = crearProducto("19", "Sombrilla para exteriores", "Jardín", 35.00, "https://th.bing.com/th/id/OIP.g6ejh7WMAiiFOjG5G9pU1wHaHZ?rs=1&pid=ImgDetMain", "Walmart");
        productos[19] = crearProducto("20", "Cortacésped eléctrico", "Jardín", 150.00, "https://th.bing.com/th/id/R.94dd20ddb5feb71a50d705133f7f7c11?rik=uoP7bg9mkJw5Pg&pid=ImgRaw&r=0", "Walmart");

        // Añadir productos originales (Walmart) a Firestore
        for (int i = 0; i < productos.length; i++) {
            db.collection("productos").add(productos[i])
                    .addOnSuccessListener(documentReference -> System.out.println("Producto añadido: " + documentReference.getId()))
                    .addOnFailureListener(e -> System.err.println("Error al añadir producto: " + e.getMessage()));
        }

        // Crear versiones duplicadas (eBay) con precios modificados
        for (int i = 0; i < productos.length; i++) {
            Map<String, Object> productoEbay = new HashMap<>(productos[i]); // Copiar datos del producto
            double precioOriginal = (double) productoEbay.get("precio");
            double nuevoPrecio = precioOriginal + (Math.random() * 20 - 10); // Precio ajustado aleatoriamente (+/- $10)
            productoEbay.put("precio", Math.round(nuevoPrecio * 100.0) / 100.0); // Redondear a 2 decimales
            productoEbay.put("tienda", "eBay");
            db.collection("productos").add(productoEbay)
                    .addOnSuccessListener(documentReference -> System.out.println("Producto añadido: " + documentReference.getId()))
                    .addOnFailureListener(e -> System.err.println("Error al añadir producto: " + e.getMessage()));
        }
    }

    private static Map<String, Object> crearProducto(String id, String nombre, String categoria, double precio, String imagenUrl, String tienda) {
        Map<String, Object> producto = new HashMap<>();
        producto.put("id", id);
        producto.put("nombre", nombre);
        producto.put("categoria", categoria);
        producto.put("precio", precio);
        producto.put("imagenUrl", imagenUrl);
        producto.put("tienda", tienda);
        return producto;
    }
}
