package org.example;

import javax.jws.WebMethod;
import javax.jws.WebService;
import java.util.List;

@WebService
public interface ProductInterface {
    @WebMethod
    Product[] getProducts();
    @WebMethod
    Product[] viewProducts();

    @WebMethod
    void placeOrder(String productName, int quantity, String customerName, String address);
    @WebMethod
    public Order viewOrder(String customerName);
    @WebMethod
    public String getOrderStatus(int orderId);
    @WebMethod
    public List<String> getNews();
}
