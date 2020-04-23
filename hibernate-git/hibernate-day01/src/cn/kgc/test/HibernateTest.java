package cn.kgc.test;


import cn.kgc.entity.News;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.Test;

import java.util.Date;

public class HibernateTest {

    @Test
    public void test01(){
        //1.创建一个sessionFactory对象
        SessionFactory sessionFactory = null;
        //1.1创建Configuration对象
        Configuration configuration=new Configuration().configure();
        //1.2创建ServiceRegistry
        ServiceRegistry registry = new ServiceRegistryBuilder().applySettings(configuration.getProperties())
                .buildServiceRegistry();
        sessionFactory = configuration.buildSessionFactory(registry);
        //2.创建session对象
        Session session = sessionFactory.openSession();
        //3.开启事务
        Transaction transaction = session.beginTransaction();
        //4.执行保存
        News news = new News("C#", "教育", new Date());
        session.save(news);
        //执行查询 orm思想
        System.out.println("news = " + news);
        //5.事务提交
        transaction.commit();
        //6.关闭session
        session.close();
        //7.关闭sessionFactory
        sessionFactory.close();
    }


}
