import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.util.Scanner;

/**
 * Project 05 -- MainMethod
 * <p>
 * Extends the run method from login to organize which class is called
 *
 * @author Tim Chou L09
 * @version May 1,2022
 */

public class MainMethod extends Run {
    public static void main(String[] args) {
        int i = 0;

        try {
            Socket socket = new Socket("localhost", 4242); //SOCKET CONNECTION
            String username = runQuiz(socket); //USER CONNECTS
            if (username.equals("end")) {
                i++;
            } else if (username.equals("")) {
            } else {
                String[] splited = username.split(" ");
                if (splited[0].equals("Teacher")) { //TEACHER IS LOGGED IN
                    Teacher teacher = new Teacher(splited[1]);
                    teacher.createMenuGUI(socket);
                    i++;
                } else if (splited[0].equals("Student")) { //STUDENT IS LOGGED IN
                    Student student = new Student(splited[1], socket);
                    student.mainStudent(socket);
                    i++;
                }
            }


        } catch (Exception e) {
            e.printStackTrace();

        }
    }
}
