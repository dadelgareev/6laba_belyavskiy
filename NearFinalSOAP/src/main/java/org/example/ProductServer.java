package org.example;

import javax.jws.WebMethod;
import javax.jws.WebService;
import javax.xml.ws.Endpoint;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;

@WebService(endpointInterface = "org.example.ProductInterface")
public class ProductServer implements ProductInterface {
    private Product[] products;
    private List<Order> orders = new ArrayList<>();
    private List<Customer> customerList = new ArrayList<>();
    private List<String> accountNumbers = new ArrayList<>();
    private List<String> customerNames = new ArrayList<>();
    //private Map<String, Double> accounts = new HashMap<>();
    private int productCount;
    private int orderIdCounter = 1;
    private List<String> news = new ArrayList<>();


    public ProductServer() {
        products = new Product[10]; // Максимальное количество товаров
        productCount = 0;
    }

    @WebMethod
    public Product[] getProducts() {
        System.out.println(formatTime() + " Client: " + "get information about all products");
        return products;
    }

    public void addProduct(String name, int quantity, double price) {
        if (productCount < products.length) {
            Product newProduct = new Product(name, quantity, price);
            products[productCount++] = newProduct;
            System.out.println(formatTime() + " Server: " + "product: " + " Name: " + products[productCount - 1].getName()+ " Quanity: "+ products[productCount - 1].getQuantity() + " Price: " + products[productCount - 1].getPrice());
        } else {
            System.out.println(formatTime() + " Server: " + " Cannot add product. Product list is full.");
        }
    }
    @WebMethod
    public void placeOrder(String productName, int quantity, String customerName, String address) {
        // Проверяем наличие товара в каталоге
        System.out.println(formatTime() + "Client: " + "try add order: "+" Product name: " + productName +" quanity: "+ quantity + " customer Name: " + customerName + " address: "+ address);
        Product orderedProduct = null;
        for (Product product : products) {
            if (product != null && product.getName().equals(productName)) {
                orderedProduct = product;
                break;
            }
        }

        if (orderedProduct == null) {
            System.out.println(formatTime() + " Client: " + "Товар с именем " + productName + " не найден в каталоге.");
            return;
        }

        // Проверяем наличие товара на складе
        if (orderedProduct.getQuantity() < quantity) {
            System.out.println(formatTime() + " Client: " + "Недостаточное количество товара " + productName + " на складе.");
            return;
        }
        // Создаем заказную позицию
        OrderItem orderItem = new OrderItem(orderedProduct, quantity);
        System.out.println(formatTime() + " Client: " + "succesfull add order: "+" Product name: " + productName +" quanity: "+ quantity + " customer Name: " + customerName + " address: "+ address);
        // Создаем заказ
        Order order = new Order(orderIdCounter++, customerName, address);
        order.addOrderItem(orderItem);
        System.out.println(formatTime() + " Client: " + "succesfull add order: "+" Product name: " + productName +" quanity: "+ quantity + " customer Name: " + customerName + " address: "+ address);
        // Обновляем количество товара на складе
        orderedProduct.setQuantity(orderedProduct.getQuantity() - quantity);

        // Добавляем заказ в список заказов
        orders.add(order);
        System.out.println(formatTime() + " Client: " + "update product quanity new value: "+ orderedProduct.getQuantity());
        //System.out.println("Заказ успешно размещен.");
    }
    @WebMethod
    public Order viewOrder(String customerName) {
        System.out.println(formatTime() + " Client: " + "try to view order: "+ "customer name: " + customerName);
        for (Order order : orders) {
            if (order.getCustomerName().equals(customerName)) {
                return order;
            }
        }
        System.out.println(formatTime() + " Client: " + "failed to view order: "+ "customer name: " + customerName);
        // Если заказ не найден, возвращаем null или генерируем исключение
        return null;
    }


    @WebMethod
    public String getOrderStatus(int orderId) {
        System.out.println(formatTime() + " Client: " + "try to get order status: "+" order ID: " + orderId);
        for (Order order : orders) {
            if (order.getId() == orderId) {
                return order.getStatus();
            }
        }
        return "Заказ с указанным ID не найден.";
    }

    @WebMethod
    public Product[] viewProducts() {
        return products;
    }
    @WebMethod
    public List<String> getNews() {
        System.out.println(formatTime() + " Client: " + "try to get news");
        return news;
    }



    public String cancelOrderDueToInsufficientFunds(int orderId, double accountBalance) {
        for (Order order : orders) {
            if (order != null && order.getId() == orderId) {
                double totalPrice = order.getTotalPrice();
                if (accountBalance < totalPrice) {
                    order.setStatus("Отменен из-за нехватки средств");
                    return "Заказ успешно отменен из-за нехватки средств.";
                } else {
                    order.setStatus("Оплачен");
                    return "У клиента достаточно средств для оплаты заказа.";
                }
            }
        }
        return "Заказ с указанным ID не найден.";
    }

    public void createAccount(String customerName, String accountNumber, double balance) {
        // Проверяем, существует ли уже счет для данного клиента
        if (customerNames.contains(customerName)) {
            System.out.println(formatTime() + " Server: Данный клиент " + customerName + " уже существует.");
            return;
        }
        if (accountNumbers.contains(accountNumber)) {
            System.out.println(formatTime() + " Server: Счет для клиента " + customerName + " уже существует.");
            return;
        }
        // Создаем новый счет для клиента
        Customer newCustomer = new Customer(customerName, accountNumber, balance);
        customerList.add(newCustomer);
        customerNames.add(customerName);
        accountNumbers.add(accountNumber);
        System.out.println(formatTime() + " Server: Создан счет для клиента " + customerName + " с номером " + accountNumber + " и балансом " + balance);

    }
    public static String formatTime() {
        // Получаем текущее системное время
        LocalDateTime now = LocalDateTime.now();

        // Определяем формат вывода
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM/dd HH:mm:ss");

        // Форматируем время в соответствии с заданным форматом
        String formattedTime = now.format(formatter);

        return formattedTime;
    }
    public void addNews(String news) {
        this.news.add(news);
        System.out.println(formatTime() + " Server: Сервер добавил новую новость: " + news);
    }
    public void viewCustomers() {
        System.out.println(formatTime() + " Server: Viewing all accounts:");
        for (Customer customer : customerList) {
            System.out.println("Customer: " + customer.getCustomerName() + ", Account Number: " + customer.getAccountNumber() + ", Balance: " + customer.getBalance());
        }
    }

    public void viewOrders(List<Order> orders) {
        if (orders.isEmpty()) {
            System.out.println("No orders available.");
        } else {
            System.out.println("List of orders:");
            for (Order order : orders) {
                System.out.println("Order ID: " + order.getId());
                System.out.println("Customer: " + order.getCustomerName());
                System.out.println("Address: " + order.getAddress());
                System.out.println("Status: " + order.getStatus());
                System.out.println("Ordered Items:");
                List<OrderItem> orderItems = order.getOrderItems();
                for (OrderItem orderItem : orderItems) {
                    Product product = orderItem.getProduct();
                    System.out.println("   - Product: " + product.getName());
                    System.out.println("     Quantity: " + orderItem.getQuantity());
                    System.out.println("     Price: " + product.getPrice());
                }
                System.out.println("--------------------");
            }
        }
    }
    public List<Order> getOrdersList(){
        return orders;
    }
    public void processOrders(List<Order> orders) {
        for (Order order : orders) {
            // Получаем имя клиента из заказа
            String customerName = order.getCustomerName();

            // Проверяем, есть ли у клиента счет и достаточно ли на нем средств для оплаты заказа
            for (Customer customer : customerList) {
                if (customer.getCustomerName().equals(customerName)) {
                    double accountBalance = customer.getBalance();
                    double totalPrice = order.getTotalPrice();

                    // Если средств достаточно для оплаты заказа, меняем статус заказа на "Оплачен"
                    if (accountBalance >= totalPrice) {
                        order.setStatus("Оплачен");
                        System.out.println(formatTime() + " Server: Заказ с ID " + order.getId() + " оплачен для клиента " + customerName);
                    } else {
                        // Если средств недостаточно, меняем статус заказа на "Отменен из-за нехватки средств"
                        order.setStatus("Отменен из-за нехватки средств");
                        System.out.println(formatTime() + " Server: Заказ с ID " + order.getId() + " отменен для клиента " + customerName + " из-за нехватки средств");
                    }
                    break;
                }
            }
        }
    }


    public static void main(String[] args) {
        String url = "http://localhost:8080/product-server";
        ProductServer server = new ProductServer();
        Endpoint.publish(url, server);
        System.out.println("SOAP Server is published at " + url + "?wsdl");

        // Пример добавления товаров через командную строку
        server.addProduct("Товар 1", 10, 100.0);
        server.addProduct("Товар 2", 20, 200.0);
        server.createAccount("Danil Obramov", "000000000001",15000);
        server.createAccount("Ilya Bodin", "000000000002",25000);
        server.createAccount("Vladimir Dolgiy", "000000000003",1000);
        server.createAccount("Maksim Bobov", "000000000004",100);
        server.addNews("First news");
        server.addNews("Second news");
        Scanner scanner = new Scanner(System.in);

        while (true) {
            System.out.println("Menu:");
            System.out.println("1. Add a new product");
            System.out.println("2. Add a new customer");
            System.out.println("3. View all customers and their balances");
            System.out.println("4. View all orders");
            System.out.println("5. Process orders");
            System.out.println("6. Exit");
            System.out.print("Enter your choice: ");

            int choice = scanner.nextInt();
            scanner.nextLine(); // Consume newline character

            switch (choice) {
                case 1:
                    System.out.print("Enter product name: ");
                    String productName = scanner.nextLine();
                    System.out.print("Enter product quantity: ");
                    int productQuantity = scanner.nextInt();
                    System.out.print("Enter product price: ");
                    double productPrice = scanner.nextDouble();
                    server.addProduct(productName, productQuantity, productPrice);
                    break;
                case 2:
                    System.out.print("Enter customer name: ");
                    String customerName = scanner.nextLine();
                    System.out.print("Enter customer account: ");
                    String customerAccount = scanner.nextLine();
                    System.out.print("Enter customer balance: ");
                    double customerBalance = scanner.nextDouble();
                    server.createAccount(customerName, customerAccount,customerBalance);
                    break;
                case 3:
                    server.viewCustomers();
                    break;
                case 4:
                    server.viewOrders(server.getOrdersList());
                    break;
                case 5:
                    server.processOrders(server.getOrdersList());
                    break;
                case 6:
                    System.out.println("Exiting...");
                    System.exit(0);
                default:
                    System.out.println("Invalid choice. Please enter a number between 1 and 5.");
            }
        }
    }
}
