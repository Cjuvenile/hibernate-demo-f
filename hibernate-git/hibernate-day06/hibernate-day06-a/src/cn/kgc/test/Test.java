package cn.kgc.test;
//双向多对多
import cn.kgc.entity.Category;
import cn.kgc.entity.Item;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

import java.util.Set;

public class Test {
    //1.创建一个sessionFactory对象
    private SessionFactory sessionFactory;
    //2.创建session对象
    private Session session;
    //3.开启事务
    private Transaction transaction;

    /**
     * 在方法执行之前先创建sessionFactory,session,transaction
     */
    @Before
    public void init() {
        //1.1创建Configuration对象
        Configuration configuration = new Configuration().configure();
        //1.2创建ServiceRegistry
        ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(registry);
        //2.创建session对象
        session = sessionFactory.openSession();
        //3.开启事务
        transaction = session.beginTransaction();
    }

    /**
     * 在其他方法执行之后关闭资源
     */
    @After
    public void destroy() {
        transaction.commit();
        session.close();
        sessionFactory.close();
    }
//先删表  双向多对多的插入前后顺序没有区别  都是8条insert
    @org.junit.Test
    public void testSave(){
        Category category1 = new Category();
        category1.setName("c-aa");

        Category category2 = new Category();
        category2.setName("c-bb");


        Item item1 = new Item();
        item1.setName("a-aa");

        Item item2 = new Item();
        item2.setName("a-bb");

        //设定关联关系
        category1.getItems().add(item1);
        category1.getItems().add(item2);

        category2.getItems().add(item1);
        category2.getItems().add(item2);

        item1.getCategories().add(category1);
        item1.getCategories().add(category2);

        item2.getCategories().add(category1);
        item2.getCategories().add(category2);


        //执行保持
        session.save(category1);
        session.save(category2);

        session.save(item1);
        session.save(item2);
    }


    @org.junit.Test
    public void testGest1(){
        Category category= (Category) session.get(Category.class,1);
        System.out.println("category.getName() = " + category.getName());
        //需要连接中间表
        for(Item item:category.getItems()){
            System.out.println("item.getName() = " + item.getName());
        }

        System.out.println("category.getItems().size() = " + category.getItems().size());

    }

    @org.junit.Test
    public  void testGet2(){
        Item item= (Item) session.get(Item.class,1);
      for( Category category: item.getCategories()){
           System.out.println("category.getName() = " + category.getName());
//          for(Item item1:category.getItems()){
//              System.out.println("item1.getName() = " + item1.getName());
//          }
      }
    }
}
