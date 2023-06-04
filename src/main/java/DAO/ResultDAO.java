package DAO;

import entitys.Result;
import entitys.Test;
import entitys.User;
import org.hibernate.HibernateException;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;
import org.springframework.stereotype.Component;

import java.util.List;


@Component
public class ResultDAO {

    private static List<Result> results;

    private SessionFactory factory;

    public void connection(){

        factory = new Configuration()
                .configure("/hibernate.cfg.xml")
                .addAnnotatedClass(Result.class)
                .addAnnotatedClass(Test.class)
                .addAnnotatedClass(User.class)
                .buildSessionFactory();

        check();

    }

    private void check(){
        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            results = session.createQuery("from Result").getResultList();

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void addResult(Result result){

        connection();

        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.save(result);

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

    public void deleteResult(Result result){

        connection();

        try {
            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.delete(result);

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

    public List<Result> getResults(){

        return results;
    }

    public void close(){
        factory.close();
    }

}
