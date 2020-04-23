package cn.kgc.test;

import cn.kgc.entity.Department;
import cn.kgc.entity.Manager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

public class Test1 {
        private SessionFactory sessionFactory;
        private Session session;
        private Transaction transaction;

        @Before
     public void init(){
            Configuration configuration=new Configuration().configure();
            ServiceRegistry serviceRegistry=new ServiceRegistryBuilder().applySettings(configuration.getProperties()).buildServiceRegistry();
            sessionFactory=configuration.buildSessionFactory(serviceRegistry);
            session=sessionFactory.openSession();
            transaction=session.beginTransaction();
        }
        @After
     public void destoty(){
        transaction.commit();
        session.close();
        sessionFactory.close();
    }

    @org.junit.Test
    public void testSave(){
        Department department = new Department();
        department.setName("dept_aa");

        Manager manager = new Manager();
        manager.setName("mana_aa");
        //映射关联关系
        department.setManager(manager);
        manager.setDepartment(department);
        /**
         * 若先保存多的一方,再保存一的一方,2条insert,1条update
         */
//        session.save(department);
//        session.save(manager);

        /**
         * 若先保存一的一方,再保存多的一方,2条insert
         */
        session.save(manager);
        session.save(department);
    }

    /**
     * 延迟(懒)加载:只有查询的对象被调用的时候,才回去执行查询语句,
     * 否则就不会去执行查询语句
     * mybatis默认就支持懒加载
     */
@org.junit.Test
      public void testGet(){
    /**
     * 懒加载
     * 若只查询多的本端的属性,会用左外连接
     */
        Department department = (Department) session.get(Department.class,1);
        System.out.println("department.getName() = " + department.getName());
    /**
     * 若查询关联的另一端属性,会用到左外连接
     */
        System.out.println("department.getManager().getName() = " + department.getManager().getName());
}

@org.junit.Test
     public void testGet02(){

         Manager manager = (Manager) session.get(Manager.class,1);
    /**
     * 若只查询一的本端的属性,会用左外连接
     * 两个同时存在只会出现一条左外链接查询
     */
         System.out.println("manager.getName() = " + manager.getName());
    /**
     * 若只查询关联的另一端的属性,会用左外连接
     */
         System.out.println(manager.getDepartment().getName());
     }
}
