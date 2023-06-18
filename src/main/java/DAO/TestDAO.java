package DAO;

import entitys.Result;
import entitys.Test;
import entitys.User;
import entitys.UserTest;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;
import resources.Factory;

import java.util.ArrayList;
import java.util.List;

@Component("testDAO")
public class TestDAO {

    private static List<Test> tests, guestTests;

    private SessionFactory factory = Factory.getFactory();

    public void check(){

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
        }

    }

    public void deleteTest(Test test){

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
        }

    }


    public List<Test> getTests(){

        return tests;
    }

    public List<Test> getGuestTests(){

        return guestTests;
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

    public Test getGuestTest(String title){
        return guestTests.stream()
                .filter(test -> test.getTitle().equals(title))
                .findFirst()
                .orElse(null);
    }

    public void createGuestTests(){

        guestTests = new ArrayList<>();

    }
    public void close(){
        factory.close();
    }

}
