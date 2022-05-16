package io.github.marbys.myrealworldapp.repository;

import io.github.marbys.myrealworldapp.user.UserRepository;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.context.event.ApplicationReadyEvent;
import org.springframework.context.event.EventListener;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import javax.persistence.EntityManagerFactory;

@Component
public class LoaderClass {

    private PasswordEncoder encoder;
    private UserRepository repository;

    public LoaderClass(UserRepository repository, PasswordEncoder encoder) {
        this.repository = repository;
        this.encoder = encoder;
    }

    @Autowired
    private EntityManagerFactory entityManagerFactory;


    @EventListener(ApplicationReadyEvent.class)
    public void insert() {
        Session session = entityManagerFactory.unwrap(SessionFactory.class).openSession();
        Transaction transaction = session.beginTransaction();

        session.createSQLQuery("insert into USERS(name, bio, image, email, password) VALUES('user', 'bio', 'image', 'user@gmail.com', '$2a$10$.lGCcIXo5uxAO/IU33Op2eCcouwPdCakjMcWcT9kl3dd/tHydUHza')").executeUpdate();
        session.createSQLQuery("insert into USERS(name, bio, image, email, password) VALUES('user2', 'bio', 'image', 'user2@gmail.com', '$2a$10$6Jkod4stSqec4b9j59kBB.9.BaLkxF6F0RCH8IX3hbe98Y0s9PXwO')").executeUpdate();
        session.createSQLQuery("insert into user_followings(follower_id, followee_id) VALUES(1, 2)").executeUpdate();

        transaction.commit();
        session.close();
    }
}
