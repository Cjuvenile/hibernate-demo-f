package cn.kgc.test;

import cn.kgc.entity.Customer;
import cn.kgc.entity.Order;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;


public class test {
    private SessionFactory sessionFactory;
    private Session session;
    private Transaction transaction;

    @Before
    public void init() {
        Configuration configuration = new Configuration().configure();
        ServiceRegistry serviceRegistry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                .buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(serviceRegistry);
        session = sessionFactory.openSession();
        transaction = session.beginTransaction();
    }

    @After
    public void destroy() {
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @Test
    public void testManyToOne(){
        Customer customer = new Customer();
        customer.setName("WW");

        Order order1 = new Order();
        order1.setName("order_5");
        order1.setCustomer(customer);

        Order order2 = new Order();
        order2.setName("order_6");
        order2.setCustomer(customer);
        /**
         * 先保存一的一方,再保存多的一方, 3条insert语句(推荐).外键维护
         */
        /*session.save(customer);
        session.save(order1);
        session.save(order2);*/



        /**
         * 先保存多的一方,再保存一的一方,3条insert2条update
         */
        session.save(order1);
        session.save(order2);

        /**
         * 在保存完一方后,session关闭后,会报SessionException: Session is closed!
         * 因为在保存完一方后,突然关闭连接的话,事务就无法提交,并且外键维护就不能
         * 确保一的一方有没有多次被保存的情况,所以就报异常
         */
//        session.close();


        session.save(customer);

    }

    @Test
    public void testManyToOneGet(){
        //延迟加载(懒加载).另一张表的字段只有在使用的时候才去会查询,否则只会查询单表
        Order order= (Order) session.get(Order.class,3);
        System.out.println("order.getName() = " + order.getName());

        System.out.println("order.getCustomer().getName() = " + order.getCustomer().getName());
    }

    /**
     * ConstraintViolationException
     * 删除的时候,若先删除一的一方,会报ConstraintViolationException异常
     * 因为一的一方被多的一方所关联,外键维护不能强制删除
     */
    @Test
    public void testManyToOneDelete(){
//        Customer customer= (Customer) session.get(Customer.class,1);
//        session.delete(customer);
        //因为多条关联所以要删除多条
        Order Order1= (Order) session.get(Order.class,3);
        session.delete(Order1);
        Order Order2= (Order) session.get(Order.class,4);
        session.delete(Order2);

    }

    @Test
    public void testManyToOneUpdate(){
        /*两种写法*/
//        Order order=new Order();
//        order.setName("order_aa");
//        order.setOrderId(5);
//        session.update(order);

//        Order order = (Order) session.get(Order.class, 3);
//        order.setName("DD");
//        session.update(order);
       Customer customer = (Customer) session.get(Customer.class, 2);
        customer.setName("DD");
        session.update(customer);

    }

    /***
     * 先保存一的一的一方,再保存多的一方
     * 双向一对多.3条insert.2条update
     */
    @Test
    public void testOneToManySave() {
        Customer customer = new Customer();
        customer.setName("王五");

        Order order1 = new Order();
        order1.setName("order_7");

        Order order2 = new Order();
        order2.setName("order_7");

        customer.getOrders().add(order1);
        customer.getOrders().add(order2);

        session.save(customer);
        session.save(order1);
        session.save(order2);

//        session.save(order1);
//        session.save(order2);
//        session.save(customer);

    }

    /**
     * 在不设置级联删除的情况下,删除一的一方会只清空表关联的字段,设置了之后
     * 就会自动删除一的一方在多的一方的关联的所有数据
     */
    @Test
    public void testOnToManyDelete() {
        Customer customer = (Customer) session.get(Customer.class, 3);
        session.delete(customer);
    }

    /*order-by:排序*/
    @Test
    public void testOnToManyGet() {
        Customer customer = (Customer) session.get(Customer.class, 1);
        for (Order order : customer.getOrders()) {
            System.out.println(order.getName());
        }
    }
}
