/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package workshop2.interfacelayer.controller;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import static java.time.LocalDateTime.now;
import java.util.List;
import java.util.Optional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import workshop2.domain.Account;
import workshop2.domain.Address;
import workshop2.domain.Customer;
import workshop2.domain.Order;
import workshop2.domain.OrderItem;
import workshop2.domain.Product;
import workshop2.interfacelayer.dao.AccountDao;
import workshop2.interfacelayer.dao.CustomerDao;
import workshop2.interfacelayer.dao.DaoFactory;
import workshop2.interfacelayer.dao.OrderDao;
import workshop2.interfacelayer.dao.OrderItemDao;
import workshop2.interfacelayer.dao.ProductDao;
import workshop2.interfacelayer.view.OrderItemView;
import workshop2.interfacelayer.view.OrderView;

/**
 *
 * @author thoma
 */
public class OrderController {
    private static final Logger log = LoggerFactory.getLogger(OrderController.class);
    private OrderView orderView;
    private OrderItemView orderItemView;
    private final OrderDao orderDao;
    private final OrderItemDao orderItemDao;
    private final CustomerDao customerDao;
    private final ProductDao productDao;
    private final AccountDao accountDao;

    OrderController(OrderView orderView, OrderItemView orderItemView) {
        this.orderView = orderView;
        this.orderItemView = orderItemView;
        orderDao = DaoFactory.getDaoFactory().createOrderDao();
        orderItemDao = DaoFactory.getDaoFactory().createOrderItemDao();
        customerDao = DaoFactory.getDaoFactory().createCustomerDao();
        productDao = DaoFactory.getDaoFactory().createProductDao();
        accountDao = DaoFactory.getDaoFactory().createAccountDao();
    }

    public void createOrderEmployee(CustomerController customerController) {
        orderView.showConstructOrderEmployeeStartScreen();
        Integer customerId = customerController.selectCustomerIdByUser();
        if (customerId == null) {
            // No customer selected so we skip creating the order
            return;
        }
        
        List<Product> productList = productDao.getAllProductsAsList();
        List<OrderItem> orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice(orderItemList);
        LocalDateTime dateTime = LocalDateTime.now();
        Order order = new Order(price, customerId, dateTime, 1); 
        
        //Get full productList, the one loaded above is emptied in the order creation process
        productList = productDao.getAllProductsAsList();
        orderView.showOrderToBeCreated(orderItemList, order, productList); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            //insert order, retreive the database-generated orderId
            int orderId = orderDao.insertOrder(order);
            log.debug("The orderId generated by the database is " + orderId);
            
            //insert the orderItems for orderItemList after setting the orderId
            for(OrderItem orderItem: orderItemList) {
                orderItem.setOrderId(orderId);
                orderItemDao.insertOrderItem(orderItem);
            }
            
            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
    
    public void createOrderCustomer(String username) {
        orderView.showConstructOrderCustomerStartScreen();
        
        Account customerAccount = accountDao.findAccountByUserName(username).get();

        int customerId = customerDao.findCustomerByAccountId(customerAccount.getId()).get().getId();

        List<Product> productList = productDao.getAllProductsAsList();
        List<OrderItem> orderItemList = orderItemView.createOrderItemListForNewOrder(productList);
        
        BigDecimal price = calculateOrderPrice(orderItemList);
        LocalDateTime dateTime = LocalDateTime.now();
        Order order = new Order(price, customerId, dateTime, 1);
        
        //Get full productList, the one loaded above is emptied in the order creation process
        productList = productDao.getAllProductsAsList();
        orderView.showOrderToBeCreated(orderItemList, order, productList); //For now only price, date and order status
        Integer confirmed = orderView.requestConfirmationToCreate();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            //insert order, retreive the database-generated orderId
            int orderId = orderDao.insertOrder(order);
            log.debug("The orderId generated by the database is " + orderId);
            
            //insert the orderItems for orderItemList after setting the orderId
            for(OrderItem orderItem: orderItemList) {
                orderItem.setOrderId(orderId);
                orderItemDao.insertOrderItem(orderItem);
            }
            
            //Update the stock after placing the order
            updateProductStockAfterCreatingOrder(orderItemList);
            
        }
    }
    
    public void showOrderToCustomer(String username) {
        orderView.showOrderListCustomerStartScreen();
        
        Account customerAccount = accountDao.findAccountByUserName(username).get();
        int customerId = customerDao.findCustomerByAccountId(customerAccount.getId()).get().getId();

        List<Order> orderList = orderDao.getAllOrdersAsListByCustomerId(customerId);
        List<Product> productList = productDao.getAllProductsAsList();
        
        if(orderList.size() == 0) {
            orderView.showCustomerNoOrdersWereFound();
        }
        else if(orderList.size() == 1) {
            Order order = orderList.get(0);
            List<OrderItem> orderItemList = orderItemDao.findAllOrderItemsAsListByOrderId(order.getId());
            orderView.showOneOrderWasFound();
            orderView.showOrderToCustomer(order, orderItemList, productList);
        }
        else {
            orderView.showCustomerListOfFoundOrders(orderList);
            int index = orderView.requestOrderIdToSelectFromList(orderList);
            Order order = orderList.get(index);
            List<OrderItem> orderItemList = orderItemDao.findAllOrderItemsAsListByOrderId(order.getId());
            orderView.showCustomerThatOrderWasSelected();
            orderView.showOrderToCustomer(order, orderItemList, productList);
        }
        
        
    }
        
    void updateProductStockAfterCreatingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
                Optional<Product> optionalProduct = productDao.findProductById(orderItem.getProductId());
                Product product = optionalProduct.get();
                int amount = orderItem.getAmount();
                int stock = product.getStock();
                product.setStock(stock - amount);
                productDao.updateProduct(product);
        }
    }

    public void deleteOrderEmployee(CustomerController customerController) {
        List<Order> orderList = orderDao.getAllOrdersAsList();
        
        List<Customer> customerList = customerDao.getAllCustomersAsList();
        
        //obtain the id of order to delete
        orderView.showDeleteOrderEmployeeStartScreen();
        orderView.showListToSelectOrderToDelete(orderList, customerList);
        Integer index = orderView.requestOrderIdToSelectFromList(orderList);
        if(index == null)
            return;
        Order selectedOrder = orderList.get(index);
        
        List<OrderItem> orderItemList = orderItemDao.findAllOrderItemsAsListByOrderId(selectedOrder.getId());

        List<Product> productList = productDao.getAllProductsAsList();
        
        orderView.showOrderToBeDeleted(orderItemList, selectedOrder, customerList, productList);
        
        
        Integer confirmed = orderView.requestConfirmationToDelete();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            orderDao.deleteOrder(selectedOrder);
            
            //Update the stock after placing the order
            updateProductStockAfterDeletingOrder(orderItemList);
        }
    }
    
    void updateProductStockAfterDeletingOrder(List<OrderItem> orderItemList) {
        
        for(OrderItem orderItem: orderItemList) {
                Optional<Product> optionalProduct = productDao.findProductById(orderItem.getProductId());
                Product product = optionalProduct.get();
                int amount = orderItem.getAmount();
                int stock = product.getStock();
                product.setStock(stock + amount);
                productDao.updateProduct(product);
        }
    }
    
//    public void deleteOrderCustomer() {
//        
//    }
//
//    public void updateOrderEmployee(CustomerController customerController) {
//        
//    }
//    
//    public void updateOrderCustomer() {
//        
//    }
    
    public void setOrderStatus() {
        List<Order> orderList = orderDao.getAllOrdersAsList();
        
        List<Customer> customerList = customerDao.getAllCustomersAsList();
        
        //obtain the id of order to delete
        orderView.showSetOrderStatusStartScreen();
        orderView.showListToSelectOrderToSetOrderStatus(orderList, customerList);
        Integer index = orderView.requestOrderIdToSelectFromList(orderList);
        if(index == null)
            return;
        Order selectedOrder = orderList.get(index);
        
        List<OrderItem> orderItemList = orderItemDao.findAllOrderItemsAsListByOrderId(selectedOrder.getId());
        List<Product> productList = productDao.getAllProductsAsList();
        
        orderView.showOrderToSetOrderStatus(orderItemList, selectedOrder, customerList, productList);
        Integer newOrderStatusId = orderView.requestInputForNewOrderStatus(selectedOrder);
        if(newOrderStatusId == null)
            return;
        
        
        orderView.showOrderToSetNewOrderStatusId(selectedOrder, newOrderStatusId, customerList, productList);
        
        selectedOrder.setOrderStatusId(newOrderStatusId);
        
        Integer confirmed = orderView.requestConfirmationToSetNewOrderStatusId();
        if (confirmed == null || confirmed == 2){
            return;
        }
        else {
            orderDao.updateOrder(selectedOrder);
        }
    }
    
    private BigDecimal calculateOrderPrice(List<OrderItem> orderItemList) {
        BigDecimal price = new BigDecimal("0.00");
        for(OrderItem orderItem: orderItemList){
            price = price.add(orderItem.getSubTotal());

        }
        return price;
    }
    
    
    
    
    
}
