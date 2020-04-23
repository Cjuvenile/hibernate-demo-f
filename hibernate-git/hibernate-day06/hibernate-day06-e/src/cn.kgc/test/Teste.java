package cn.kgc.test;

import cn.kgc.entity.Person1;
import cn.kgc.entity.Person3;
import cn.kgc.entity.Student1;
import cn.kgc.entity.Student3;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import java.util.List;

public class Teste {
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
//先删表 单向多对多的插入前后顺序没有区别  都是8条insert
    @org.junit.Test
    public void testSave(){
        Person3 person = new Person3();
        person.setName("p-aa");
        person.setAge(21);
        session.save(person);


        Student3 student = new Student3();
        student.setSchool("beida");
        student.setAge(28);
        student.setName("华大职业教育");
        session.save(student);
    }
    /**
     * 查询:
     *  1.查询父类记录,只需要查询一张数据表
     *  2.查询子类记录,也只需要一张数据表
     *  缺点:
     *      1.使用了辨别者列
     *      2.子类独有的字段不能添加非空约束
     *      3.若继承的层次较深,则数据表的字段也会较多
     */
@org.junit.Test
    public void testGet(){
        List<Person3> personList=session.createQuery("From Person3").list();
        System.out.println("personList.size() = " + personList.size());
        System.out.println("personList.get(1).getAge() = " + personList.get(1).getAge());

    List<Student3> studentList=session.createQuery("From Student3").list();
        System.out.println("studentList.size() = " + studentList.size());
    }
    @Test
    public void testUpdate() {
        String hql = "update Person3 p set p.age = 34";
        session.createQuery(hql).executeUpdate();
    }
}
