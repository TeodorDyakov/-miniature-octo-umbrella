import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Scanner;
import java.util.Set;
import java.util.stream.Collectors;

public class TestPractice {

    static class Question{
        String question;
        String answer;

        public String getQuestion() {
            return question;
        }

        @Override
        public String toString() {
            return
                "question:\n" + question+
                "answer:\n" + answer +"\n";
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Question question1 = (Question) o;
            return Objects.equals(question, question1.question) && Objects.equals(answer, question1.answer);
        }

        @Override
        public int hashCode() {
            return Objects.hash(question, answer);
        }

        public String getAnswer() {
            return answer;
        }

        public Question(String question, String answer) {
            this.question = question;
            this.answer = answer;
        }

    }

    public static List<String> parse(String text){
        var l = new ArrayList<String>();
        int idx;
        text = text.substring(text.indexOf("1. "));
        for(int i = 2;; i++){
            String num = i + ". ";
            idx = text.indexOf(num);
            if(idx < 0){
                break;
            }
            l.add(text.substring(0, idx));
            text = text.substring(idx);
        }
        return l;
    }

    public static void main(String[] args) throws IOException {
        Path fileName = Path.of(args[0]);
        String actual = Files.readString(fileName);

        List<String>que = parse(actual);
        List<String> ans = Files.lines(Path.of(args[1])).collect(Collectors.toList());
        List<Question> questions = new ArrayList<>();

        for(int i = 0; i < que.size() && i < ans.size(); i++){
            questions.add(new Question(que.get(i), ans.get(i)));
        }
        File f = new File("wrong");
        FileWriter fileWriter = new FileWriter(f, true);
        BufferedWriter bw = new BufferedWriter(fileWriter);

        Scanner scanner = new Scanner(System.in);
        int right_answers = 0;
        Collections.shuffle(questions);
        int answers = 0;
        Set<Question> gotWrong = new HashSet<>();
        for(Question q : questions){
            System.out.printf("    %d of %d correct\n\n", right_answers, answers++);
            System.out.println(q.getQuestion());
            System.out.println("You answer:");
            String answer = scanner.nextLine();
            if(answer.equals(q.getAnswer())){
                System.out.println("RIGHT");
                right_answers++;
            }else{
                bw.write(q.toString() + System.lineSeparator());
                bw.flush();
                gotWrong.add(q);
                System.out.println("WRONG answer is: " + q.getAnswer());
            }
        }
        bw.close();
        while(!gotWrong.isEmpty()){
            List<Question> l = new ArrayList<>(gotWrong);
            Collections.shuffle(l);
            gotWrong = new HashSet<>(l);
            Question random = l.get(0);
            System.out.printf("    %d of %d correct\n\n", right_answers, answers++);
            System.out.println(random.getQuestion());
            System.out.println("You answer:");
            String answer = scanner.nextLine();
            if(answer.equals(random.getAnswer())){
                System.out.println("RIGHT");
                right_answers++;
                gotWrong.remove(random);
            }else{
                System.out.println("WRONG answer is: " + random.getAnswer());
            }
        }
    }

}
