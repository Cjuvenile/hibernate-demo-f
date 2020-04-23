package cn.kgc.test;

import cn.kgc.entity.News;
import cn.kgc.entity.Pay;
import cn.kgc.entity.Worker;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;
import org.hibernate.jdbc.Work;
import org.hibernate.service.ServiceRegistry;
import org.hibernate.service.ServiceRegistryBuilder;
import org.junit.After;
import org.junit.Before;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.sql.Blob;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Date;
import java.util.Map;

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

    @org.junit.Test
    public void test() {
        News news = (News) session.get(News.class, 2);
        System.out.println("news = " + news);

        News news2 = (News) session.get(News.class, 2);
        System.out.println("news2 = " + news2);
    }

    /**
     * flush: 使数据表中的记录和 Session 缓存中的对象的状态保持一致. 为了保持一致, 则可能会发送对应的 SQL 语句.
     * 1. 在 Transaction 的 commit() 方法中: 先调用 session 的 flush 方法, 再提交事务
     * 2. flush() 方法会可能会发送 SQL 语句, 但不会提交事务.
     * 3. 注意: 在未提交事务或显式的调用 session.flush() 方法之前, 也有可能会进行 flush() 操作.
     * 1). 执行 HQL 或 QBC 查询, 会先进行 flush() 操作, 以得到数据表的最新的记录
     * 2). 若记录的 ID 是由底层数据库使用自增的方式生成的, 则在调用 save() 方法时, 就会立即发送 INSERT 语句.
     * 因为 save 方法后, 必须保证对象的 ID 是存在的!
     */

    @org.junit.Test
    public void testFlush() {
        News news = (News) session.get(News.class, 2);
//        news.setTitle("222");
        System.out.println("news = " + news);
        news.setTitle("123");
        session.flush();
        News news2 = (News) session.get(News.class, 2);
        System.out.println("news2 = " + news2);
    }

    /**
     * get VS load:
     * <p>
     * 1. 执行 get 方法: 会立即加载对象.
     * 执行 load 方法, 若不适用该对象, 则不会立即执行查询操作,
     * 而返回一个代理对象
     * <p>
     * get 是 立即检索, load 是延迟检索.
     * <p>
     * 2. load 方法可能会抛出 LazyInitializationException 异常: 在需要初始化
     * 代理对象之前已经关闭了 Session
     * <p>
     * 3. 若数据表中没有对应的记录, Session 也没有被关闭.
     * get 返回 null
     * load 若不使用该对象的任何属性, 没问题; 若需要初始化了, 抛出异常.
     */
    @org.junit.Test
    public void testGet() {
        News news1 = (News) session.get(News.class, 3);
        System.out.println("news1 = " + news1);
    }

    /**
     * 延迟(懒)加载:只有查询的对象被调用的时候,才回去执行查询语句,
     * 否则就不会去执行查询语句
     * mybatis默认就支持懒加载
     */
    @org.junit.Test
    public void testLoad() {
        News news2 = (News) session.load(News.class, 3);
        System.out.println("news2 = " + news2);
    }

    /**
     * 1. save() 方法
     * 1). 使一个临时对象变为持久化对象
     * 2). 为对象分配 ID.
     * 3). 在 flush 缓存时会发送一条 INSERT 语句.
     * 4). 在 save 方法之前的 id 是无效的
     * 5). 持久化对象的 ID 是不能被修改的!
     */

    @org.junit.Test
    public void testSave() {
        News news = new News("C#", "计算机语言", new Date());
        //news.setId(222);
        session.save(news);
        //在持久化之后
        news.setId(333);
        System.out.println("news = " + news);
    }

    @org.junit.Test
    public void testPersist() {
        News news = new News("java", "计算机语言", new Date());
        //news.setId(222);
        session.persist(news);
        //在持久化之后
        //news.setId(333);
        System.out.println("news = " + news);
    }

    @org.junit.Test
    public void testClear() {
        News news = (News) session.get(News.class, 2);
        System.out.println("news = " + news);
        //清理缓存
        session.clear();
        News news2 = (News) session.get(News.class, 2);
        news2.setTitle("555");
        System.out.println("news2 = " + news2);
    }


    @org.junit.Test
    public void testUpdate() {
        News news = new News();
        news.setTitle("JAVA");
        news.setId(2);
        session.update(news);
//        System.out.println("news = " + news);
    }

    /*
     * 1.saveOrUpdate()方法，如果传入参数是瞬时状态的对象，就调用save()方法；
     * 2.如果传入参数是游离状态的对象，就调用update()方法；
     */
    @org.junit.Test
    public void testSaveOrUpdate() {
        News news = new News("html", "计算机语言", new Date());
        //news.setId(2);
        //news.setId(22);
        session.saveOrUpdate(news);
        System.out.println("news = " + news);
    }

    /*
     * 1.merge()方法，执行更新或插入操作并返回持久化的对象，并不影响传入的对象的状态
     * 2.merge()方法根据当前对象主键是否存在决定执行新增操作还是更新操作，
     * 如果该对象主键存在且没有字段更改则不执行任何操作。
     * 3.SaveOrUpdate()和Merge()的区别 就在于前者没有返回持久化对象，后者有。
     */
    @org.junit.Test
    public void testMerge() {
        News news = new News("jdk", "计算机语言", new Date());
        //news.setId(2);
        news.setId(22);
        session.merge(news);
        System.out.println("news = " + news);
    }

    /**
     * delete:执行删除操作:只要OID在数据表中有对应的数据,就会准备执行delete操作
     * 若没有,就会抛出异常
     * 可以通过配置文件hibernate.use_identifier_rollback为true
     * 是删除对象后把OID置为null
     */
    @org.junit.Test
    public void testDelete() {

        News news = (News) session.get(News.class, 1);
        session.delete(news);

        System.out.println("news = " + news);
    }

    @org.junit.Test
    public void testDowork() {
        //匿名内部类
        session.doWork(new Work() {
            @Override
            public void execute(Connection connection) throws SQLException {
                System.out.println("connection = " + connection);
            }
        });
    }

    @org.junit.Test
    public void testDoWork() {
        session.doWork(connection -> System.out.println("connection = " + connection));
    }
@org.junit.Test
    public void testBlob() throws IOException, SQLException {
      //插入图片以及文本数据
        News news = new News();
        news.setAuthor("AAAA");
        news.setTitle("BBBB");
        news.setCreateDate(new Date());
        news.setContent("sdnfijshfsdhfisdfoisadasdadasdasdasdasdafasgsehgga");

        InputStream stream = new FileInputStream("1.jpg");
        Blob image = Hibernate.getLobCreator(session)
                .createBlob(stream, stream.available());
        news.setImage(image);

        session.save(news);
       //查询图片大小
//    News news = (News) session.get(News.class, 1);
//    Blob image=news.getImage();
//    InputStream stream=image.getBinaryStream();
//    System.out.println("stream.available() = " + stream.available());
}
  @org.junit.Test
    public void testWorker() {
        Worker worker = new Worker();
        Pay pay = new Pay();

        pay.setMonthlyPay(8000);
        pay.setYearPay(200000);

        worker.setName("迪丽热巴");
        worker.setPay(pay);

        session.save(worker);
    }
    /*映射派生属性*/
    @org.junit.Test
    public void testFormula(){
        News news = (News) session.get(News.class,1);
        System.out.println("news.getDesc() = " + news.getDesc());
    }

}
