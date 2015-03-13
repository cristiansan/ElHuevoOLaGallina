package app.eggorchicken.eggorchicken;

/**
 * Created by horaceayala on 15-03-12.
 */
public class Question {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public boolean isCorrect() {
        return correct;
    }

    public void setCorrect(boolean correct) {
        this.correct = correct;
    }

    private long id;
    private String question;
    private boolean correct;
}
