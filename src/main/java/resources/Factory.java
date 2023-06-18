package resources;

import entitys.Result;
import entitys.Test;
import entitys.User;
import entitys.UserTest;
import org.hibernate.SessionFactory;
import org.hibernate.cfg.Configuration;

public class Factory {

    private static SessionFactory factory;

    static {
        factory = new Configuration()
                .configure("/hibernate.cfg.xml")
                .addAnnotatedClass(User.class)
                .addAnnotatedClass(Test.class)
                .addAnnotatedClass(UserTest.class)
                .addAnnotatedClass(Result.class)
                .buildSessionFactory();
    }

    public static SessionFactory getFactory(){
        return factory;
    }
}
