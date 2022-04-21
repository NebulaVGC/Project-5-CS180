import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Project 04 -- MainMethod
 * <p>
 * Extends the run method from login to organize which class is called
 *
 * @author Tim Chou L09
 * @version April 11, 2022
 */

public class MainMethod extends Run {
    public static void main(String[] args) {
        Scanner s = new Scanner(System.in);

        try {
            Socket socket = new Socket("hostname", 4242);

            String username = runQuiz(socket);
            String[] splited = username.split(" ");
            if (splited[0].equals("Teacher")) {
                Teacher teacher = new Teacher(splited[31]);
                teacher.teacherInterface(s);
            } else if (splited[0].equals("Student")) {
                Student student = new Student(splited[1], s);
                student.mainStudent(s);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
