package entitys;

import DAO.UserDAO;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javafx.scene.image.Image;

import javax.persistence.*;
import java.io.*;
import java.util.*;

@Entity
@Table(name = "user")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "name")
    private String name;
    @Column(name = "surname")
    private String surname;
    @Column(name = "email")
    private String email;
    @Column(name = "password")
    private String password;
    @Lob
    @JsonIgnore
    @Column(name = "avatar")
    private byte[] avatar;
    @JsonIgnore
    @OneToMany(fetch = FetchType.EAGER, mappedBy = "user", cascade = CascadeType.ALL, orphanRemoval = true)
    private Set<UserTest> userTests = new HashSet<>();

    public User(){
    }

    public User(String name, String surname, String email, String password) {
        this.name = name;
        this.surname = surname;
        this.email = email;
        this.password = password;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Image getAvatar() {
        if (avatar != null) {
            InputStream avatarStream = new ByteArrayInputStream(avatar);
            Image avatarImage = new Image(avatarStream);
            return avatarImage;
        } else {
            return null;
        }
    }

    public void setAvatar(Image image) throws IOException {
        File file = new File(image.getUrl().substring(6));
        byte[] bAvatar = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(bAvatar);
        this.avatar = bAvatar;
    }

    public Set<UserTest> getUserTests() {
        return userTests;
    }

    public void setUserTests(Set<UserTest> userTests) {
        this.userTests = userTests;
    }

    public List<Test> getTests() {
        List<Test> tests = new ArrayList<>();
        for (UserTest userTest : userTests) {
            tests.add(userTest.getTest());
        }
        return tests;
    }

    public void removeTest(Test test) {
        UserTest userTestToRemove = null;
        for (UserTest userTest : userTests) {
            if (userTest.getTest().getTitle().equals(test.getTitle())) {
                userTestToRemove = userTest;
                break;
            }
        }
        if (userTestToRemove != null) {
            userTestToRemove.setTest(null);
            userTests.remove(userTestToRemove);
        }
    }

    public void addUserTest(UserTest userTest){
        userTests.add(userTest);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", surname='" + surname + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}
