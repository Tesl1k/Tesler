package entitys;


import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import javax.persistence.*;
import java.util.List;
import java.util.Map;

@Entity
@Table(name = "result")
public class Result {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;
    @Column(name = "result", columnDefinition = "json")
    private String resultJson;

    public Result(){}
    public Result(User user, Test test, Map<String, List<String>> result) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.user = user;
        this.test = test;
        this.resultJson = mapper.writeValueAsString(result);
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Test getTest() {
        return test;
    }

    public void setTest(Test test) {
        this.test = test;
    }

    public String getResultJson() {
        return resultJson;
    }

    public void setResultJson(String resultJson) {
        this.resultJson = resultJson;
    }

    public Map<String, List<String>> getResult() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> resultMap = mapper.readValue(resultJson, new TypeReference<Map<String, List<String>>>() {});

        return resultMap;
    }

    @Override
    public String toString() {
        return "Result{" +
                "id=" + id +
                ", user=" + user +
                ", test=" + test +
                ", resultJson='" + resultJson + '\'' +
                '}';
    }
}
