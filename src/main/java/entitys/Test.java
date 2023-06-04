package entitys;

import javax.persistence.*;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import java.util.*;

@Entity
@Table(name = "test")
public class Test {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private int id;
    @Column(name = "title")
    private String title;
    @Column(name = "description")
    private String description;
    @Column(name = "questions", columnDefinition = "json")
    private String questionsJson;
    @Column(name = "answer", columnDefinition = "json")
    private String answerJson;
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    public Test(){
    }

    public Test(String title, String description, Map<String, List<String>> questions, Map<String, List<String>> answers, User user) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        this.title = title;
        this.description = description;
        this.questionsJson = mapper.writeValueAsString(questions);
        this.answerJson = mapper.writeValueAsString(answers);
        this.user = user;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Map<String, List<String>> getQuestions() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> questionsMap = mapper.readValue(questionsJson, new TypeReference<Map<String, List<String>>>() {});

        return questionsMap;
    }

    public Map<String, List<String>> getAnswers() throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        Map<String, List<String>> answersList = mapper.readValue(answerJson, new TypeReference<Map<String, List<String>>>() {});

        return answersList;
    }

    public String getQuestionsJson() {
        return questionsJson;
    }

    public void setQuestionsJson(String questionsJson) {
        this.questionsJson = questionsJson;
    }

    public String getAnswerJson(){
        return answerJson;
    }

    public void setAnswerJson(String answerJson) {
        this.answerJson = answerJson;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {
        return "Test{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", description='" + description + '\'' +
                ", questionsJson='" + questionsJson + '\'' +
                ", answerJson='" + answerJson + '\'' +
                '}';
    }

}
