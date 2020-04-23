package cn.kgc.entity;

import java.util.Set;
import java.util.HashSet;
public class Customer {

    private Integer customerId;
    private String name;



    //映射多的一方的集合
    //一定要实例化HashSet,不然如果没有给值,会报空指针异常
    private Set<Order> orders=new HashSet<>();

    public Integer getCustomerId() {
        return customerId;
    }

    public void setCustomerId(Integer customerId) {
        this.customerId = customerId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Order> getOrders() {
        return orders;
    }

    public void setOrders(Set<Order> orders) {
        this.orders = orders;
    }
    @Override
    public String toString() {
        return "Customer{" +
                "customerId=" + customerId +
                ", name='" + name + '\'' +
                ", orders=" + orders +
                '}';
    }
}
