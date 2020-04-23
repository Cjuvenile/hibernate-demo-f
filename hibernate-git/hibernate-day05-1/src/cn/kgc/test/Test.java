package cn.kgc.test;

import cn.kgc.entity.Department1;
import cn.kgc.entity.Manager1;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

public class Test {
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
        Department1 department = new Department1();
        department.setName("dept_aa");

        Manager1 manager = new Manager1();
        manager.setName("mana_aa");
        //映射关联关系
        department.setManager(manager);
        manager.setDepartment(department);
//        /**
//         * 若先保存引用主键的一方,再保存被引用的一方,2条insert
//         */
//        session.save(department);
//        session.save(manager);

        /**
         * 若先保存被引用主键的一方,再保存引用的一方,2条insert
         */
        session.save(manager);
        session.save(department);
    }

    /**
     * 延迟(懒)加载:只有查询的对象被调用的时候,才回去执行查询语句,
     * 否则就不会去执行查询语句
     * mybatis默认就支持懒加载
     * 引用生成主键的查询
     */
@org.junit.Test
      public void testGet(){
    /**
     * 懒加载
     * 若只查询引用主键的一方的属性,不会用左外连接
     * 查询两端属性，两条select，一条内部查询一条左外查询
     */
        Department1 department = (Department1) session.get(Department1.class,1);
        System.out.println("department.getName() = " + department.getName());
    /**
     * 若查询关联的另一端属性,会用到左外连接
     */
        System.out.println("department.getManager().getName() = " + department.getManager().getName());
}
/*
* 被引用主键的查询
*/
    @org.junit.Test
    public void testGet02(){

        Manager1 manager = (Manager1) session.get(Manager1.class,1);
        /**
         * 若只查询被引用主键的属性,会用左外连接
         * 两个同时存在只会出现一条左外链接查询
         */
        System.out.println("manager.getName() = " + manager.getName());
        /**
         * 若只查询关联的另一端的属性,会用左外连接
         */
        System.out.println(manager.getDepartment().getName());
    }


}
