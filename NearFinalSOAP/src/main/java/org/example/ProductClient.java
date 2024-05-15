package org.example;

import javax.xml.namespace.QName;
import javax.xml.ws.Service;
import java.net.URL;
import java.util.List;
import java.util.Scanner;

public class ProductClient {
    public static void main(String[] args) throws Exception {
        URL url = new URL("http://localhost:8080/product-server?wsdl");
        QName qname = new QName("http://example.org/", "ProductServerService");

        Service service = Service.create(url, qname);
        ProductInterface productInterface = service.getPort(ProductInterface.class);


        /*
        // Получаем массив товаров от сервера
        Product[] products = productInterface.getProducts();
        for (Product product : products) {
            if (product != null) {
                System.out.println("Name: " + product.getName());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("Price: " + product.getPrice());
                System.out.println("--------------------");
            }
        }

        // Добавляем новый товар
        productInterface.addProduct("Door", 15, 250);

        // Повторно получаем массив товаров от сервера, чтобы увидеть обновленный список
        products = productInterface.getProducts();
        for (Product product : products) {
            if (product != null) {
                System.out.println("Name: " + product.getName());
                System.out.println("Quantity: " + product.getQuantity());
                System.out.println("Price: " + product.getPrice());
                System.out.println("--------------------");
            }
        }
        */

        /*
        productInterface.viewProducts();
        productInterface.placeOrder("Товар 1",2,"Danil Obramov","Magnitogorsck");
        productInterface.viewOrder("Danil Obramov");
        System.out.println("Статус заказа с номером: "+ "1" + " Статус:"+productInterface.getOrderStatus(1));
        */
        Scanner scanner = new Scanner(System.in);
        int choice;
        do {
            System.out.println("Menu:");
            System.out.println("1. Place an order");
            System.out.println("2. View products");
            System.out.println("3. Check order status");
            System.out.println("4. Check order");
            System.out.println("5. Check news");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");
            choice = scanner.nextInt();
            scanner.nextLine(); // consume newline

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter quantity: ");
                    int quantity = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter address: ");
                    String address = scanner.nextLine();
                    productInterface.placeOrder(productName, quantity, customerName, address);
                    break;
                case 2:
                    System.out.println("Products available:");
                    Product[] products = productInterface.getProducts();
                    for (Product product : products) {
                        if (product != null) {
                            System.out.println("Name: " + product.getName());
                            System.out.println("Quantity: " + product.getQuantity());
                            System.out.println("Price: " + product.getPrice());
                            System.out.println("--------------------");
                        }
                    }
                    break;
                case 3:
                    System.out.print("Enter order ID: ");
                    int orderId = scanner.nextInt();
                    scanner.nextLine(); // consume newline
                    System.out.println("Order status: " + productInterface.getOrderStatus(orderId));
                    break;
                case 4:
                    System.out.print("Enter customer name: ");
                    String customer = scanner.nextLine();
                    Order order = productInterface.viewOrder(customer);
                    if (order != null) {
                        System.out.println("Order ID: " + order.getId());
                        System.out.println("Customer: " + order.getCustomerName());
                        System.out.println("Address: " + order.getAddress());
                        System.out.println("Ordered Items:");

                        List<OrderItem> orderItems = order.getOrderItems();
                        for (OrderItem orderItem : orderItems) {
                            Product product = orderItem.getProduct();
                            System.out.println("   - Name: " + product.getName());
                            System.out.println("     Quantity: " + orderItem.getQuantity());
                            System.out.println("     Price: " + product.getPrice());
                        }
                    }
                    else{
                        System.out.println("No orders found for customer: " + customer);
                    }
                    break;
                case 5:
                    List<String> news = productInterface.getNews();
                    System.out.println("News:");
                    for (String newsItem : news) {
                        System.out.println(newsItem);
                    }

                    break;
                case 6:
                    System.out.println("Exiting...");
                    break;
                default:
                    System.out.println("Invalid choice. Please enter a number from 1 to 6.");
            }
        } while (choice != 6);
    }

}
