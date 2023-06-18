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

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;
import java.util.List;


@Component("userDAO")
public class UserDAO {
    private static List<User> users;

    private SessionFactory factory = Factory.getFactory();

    public void check(){

        try{

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            users = session.createQuery("from User").getResultList();

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
    }

    public void update(User user){


        try {

            Session session = factory.getCurrentSession();

            session.beginTransaction();

            session.update(user);

            session.getTransaction().commit();

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }
        finally {
            check();
        }

    }

    public void addUser(User user){

        try{

            if(isUserMissing(user.getEmail(), user.getPassword())){

                Session session = factory.getCurrentSession();

                session.beginTransaction();

                try{
                    user.setPassword(hashPassword(user.getPassword()));
                    session.save(user);
                }
                catch (NoSuchAlgorithmException e) {
                    throw new RuntimeException(e);
                }



                session.getTransaction().commit();

            }

        }
        catch (HibernateException e) {
            throw new RuntimeException(e);
        }

        finally {
            check();
        }

    }

    public User getUser(String email){

        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst()
                .orElse(null);
    }

    public String hashPassword(String password) throws NoSuchAlgorithmException {
        MessageDigest md = MessageDigest.getInstance("SHA-256");
        md.update(password.getBytes());
        byte[] byteData = md.digest();
        return Base64.getEncoder().encodeToString(byteData);
    }

    public boolean isUserMissing(String email, String password) {

        if (users.stream().filter(w -> w.getEmail().contentEquals(email)).findFirst().orElse(null) == null
                && users.stream().filter(w -> w.getPassword().contentEquals(password)).findFirst().orElse(null) == null)
            return true;

        return false;

    }

    public boolean isUserNotMissing(String email, String password) {

        try {
            String finalPassword = hashPassword(password);
            if (users.stream().filter(user -> user.getEmail().contentEquals(email)).findFirst().orElse(null) != null
                    && users.stream().filter(user -> user.getPassword().contentEquals(finalPassword)).findFirst().orElse(null) != null)
                return true;
        }
        catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }

        return false;

    }


    public List<User> getUsers(){

        return users;
    }

    public void close(){
        factory.close();
    }

}
