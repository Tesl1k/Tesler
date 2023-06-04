package DAO;

import entitys.Test;
import entitys.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;

@Component("testDAO")
public class TestDAO {

    private static List<Test> tests;

    private SessionFactory factory;

    public void connection(){

        factory = new Configuration()
                .configure("/hibernate.cfg.xml")
                .addAnnotatedClass(Test.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        check();

    }

    private void check(){

        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            tests = session.createQuery("from Test").getResultList();

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void addTest(Test test){

        connection();

        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.save(test);

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        finally {
            check();
            close();
        }

    }

    public void deleteTest(Test test){

        connection();

        try {
            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.delete(test);

            session.getTransaction().commit();
        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        finally {
            check();
            close();
        }


    }


    public List<Test> getTests(){

        return tests;
    }

    public Test getTest(int id){
        return tests.stream()
                .filter(test -> test.getId() == id)
                .findFirst()
                .orElse(null);
    }

    public Test getTest(String title){
        return tests.stream()
                .filter(test -> test.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }
    public void close(){
        factory.close();
    }

}
