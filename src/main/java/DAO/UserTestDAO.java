package DAO;

import entitys.Result;
import entitys.Test;
import entitys.User;
import entitys.UserTest;
import org.hibernate.FlushMode;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;
import resources.Factory;

import java.util.List;
import java.util.Set;

@Component("userTestDAO")
public class UserTestDAO {

    private static List<UserTest> userTests;
    private SessionFactory factory = Factory.getFactory();

    public void check(){

        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            userTests = session.createQuery("from UserTest").getResultList();

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void addUserTest(UserTest userTest){


        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.save(userTest);

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        finally {
            check();
        }

    }

    public void delete(UserTest userTest){


        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.delete(userTest);

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }

        finally {
            check();
        }


    }

    public List<UserTest> getUserTests() {
        return userTests;
    }

    public void setUserTests(List<UserTest> userTests) {
        this.userTests = userTests;
    }

    public void close(){
        factory.close();
    }

}