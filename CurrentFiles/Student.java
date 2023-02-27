import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * Project 05 -- Student
 * <p>
 * This class is the student object for the quiz taking system.
 *
 * @author Matt Zlatinksi, Victor Dollosso L09
 * @version May 1, 2022
 */

public class Student {
    private Socket socket;
    private String userName;

    public Student(String userName, Socket socket) { //creates a new student obj with the username from login
        this.userName = userName; //username of user currently logged in
        this.socket = socket;
    }

    /*
    method that runs the quiz and gives the user an option to attach a file to the quiz
     */
    public void runNewQuiz(PrintWriter writer, BufferedReader reader) throws IOException {
        String[] options = {"Active", "Import File"};
        int quizType = JOptionPane.showOptionDialog(null,
                "Select how you would like to take the quiz",
                "Quiz Type", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                null, options, null);
        int questionCounter = Integer.parseInt(reader.readLine()); //question count sent from server
        int currentQ = 0;//current question
        String answer = "";
        ArrayList<String> answerList = new ArrayList<>();
        ArrayList<String> copyQuizContents = new ArrayList<>(); //will store a copy of the original quiz file
        copyQuizContents.add(reader.readLine());
        if (quizType == 0) {
            while (currentQ < questionCounter) {
                String Question = reader.readLine();
                String A = reader.readLine(); //stores first answer option
                String B = reader.readLine(); //stores second answer option
                String C = reader.readLine(); //stores third answer option
                String D = reader.readLine(); //stores fourth answer option
                currentQ++;
                String[] answerOptions = {"A", "B", "C", "D"};
                answer = (String) JOptionPane.showInputDialog(null,
                        Question + "\n" + A + "\n" + B + "\n" + C + "\n" + D,
                        "Question " + currentQ, JOptionPane.QUESTION_MESSAGE,
                        null, answerOptions, answerOptions[0]);
                copyQuizContents.add(Question);
                copyQuizContents.add(A);
                copyQuizContents.add(B);
                copyQuizContents.add(C);
                copyQuizContents.add(D);
                answerList.add(answer);
            }
            copyQuizContents.add(userName);
            for (int i = 0; i < answerList.size(); i++) {
                copyQuizContents.add(answerList.get(i)); //adds answer choices in order to the end of the file
            }
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").
                    format(Calendar.getInstance().getTime()); //creates timestamp
            copyQuizContents.add(timeStamp);
            String contentsSize = String.valueOf(copyQuizContents.size());
            writer.write(contentsSize);
            writer.println();
            writer.flush();
        } else if (quizType == 1) {
            while (currentQ < questionCounter) {
                String Question = reader.readLine();
                String A = reader.readLine();
                String B = reader.readLine();
                String C = reader.readLine();
                String D = reader.readLine();
                currentQ++;
                JOptionPane.showMessageDialog(null,
                        Question + "\n" + A + "\n" + B + "\n" + C + "\n" + D,
                        "Question " + currentQ, JOptionPane.QUESTION_MESSAGE);
                copyQuizContents.add(Question);
                copyQuizContents.add(A);
                copyQuizContents.add(B);
                copyQuizContents.add(C);
                copyQuizContents.add(D);
            }
            copyQuizContents.add(userName);
            String fileName = JOptionPane.showInputDialog(null,
                    "Enter the name of the file you would like to input",
                    "File Input", JOptionPane.QUESTION_MESSAGE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String newLine = "";
            while ((newLine = bufferedReader.readLine()) != null) {
                copyQuizContents.add(newLine);
            }
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").
                    format(Calendar.getInstance().getTime());
            copyQuizContents.add(timeStamp);
            String contentsSize = String.valueOf(copyQuizContents.size());
            writer.write(contentsSize);
            writer.println();
            writer.flush();
        }
        for (int i = 0; i < copyQuizContents.size(); i++) { //writes the contents of the final file to the server
            writer.write(copyQuizContents.get(i));
            writer.println();
            writer.flush();
        }
    }


    /*
    method that returns the course the student picks
     */
    public String pickCourse(ArrayList<String> courseList, Socket socket) {
        String courseName = null; //the course name
        String[] courseOptions = new String[courseList.size()];
        for (int i = 0; i < courseList.size(); i++) { //changes array list to array
            courseOptions[i] = courseList.get(i);
        }
        courseName = (String) JOptionPane.showInputDialog(null,
                "Please select which course you would like to take?",
                "Course Selection", JOptionPane.QUESTION_MESSAGE, null, courseOptions, courseOptions[0]);
        if (courseName.equals("-1")) {
            courseName = "end";
        }
        return courseName;
    }

    /*
    method that allows the student to choose which quiz to take. If they have already taken the quiz, it
    will say already taken and give them the option to see their grades and response for each answer
     */
    public String pickQuiz(ArrayList<String> quizList, Socket socket) {
        this.socket = socket;
        String quizName = null;
        String[] quizOptions = new String[quizList.size()];
        for (int i = 0; i < quizOptions.length; i++) {
            quizOptions[i] = quizList.get(i);
        }
        quizName = (String) JOptionPane.showInputDialog(null,
                "Please select which quiz you would like to take?",
                "Quiz Selection", JOptionPane.QUESTION_MESSAGE, null, quizOptions, quizOptions[0]);
        if (quizName.equals("-1")) {
            quizName = "end";
        }
        return quizName;
    }


    public void mainStudent(Socket socket) throws IOException {
        //takes place after login
        int loop = 0; //initiates looping variable
        this.socket = socket;
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.write(this.userName);
        writer.println();
        writer.flush();
        try {
            while (loop == 0) {
                Student student = new Student(this.userName, socket); //creates Student object
                int n = 0;
                String teacher = null; //initiates string to store teachers username

                BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                ArrayList<String> coursesList = new ArrayList<>(); //initializes list of courses
                while (n == 0) {
                    writer.write("get teacher"); //writes to the server to initiate sending the list of teachers
                    writer.println();
                    writer.flush();
                    int listSize = Integer.parseInt(reader.readLine()); //reads size of teacher list
                    if (listSize == 0) {
                        JOptionPane.showMessageDialog(null,
                                "There are no existing teachers to choose from",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        return;
                    } else {
                        int lineCount = 0;
                        ArrayList<String> teacherList = new ArrayList<>();
                        while (lineCount < listSize) {
                            teacherList.add(reader.readLine());
                            lineCount++;
                        }
                        String[] teacherOptions = new String[teacherList.size()];
                        for (int i = 0; i < teacherList.size(); i++) {
                            teacherOptions[i] = teacherList.get(i); //list of teachers
                        }
                        teacher = (String) JOptionPane.showInputDialog(null,
                                "What is the username of your teacher?"
                                , "Teacher Selection", JOptionPane.QUESTION_MESSAGE, null,
                                teacherOptions, teacherOptions[0]);
                        if (teacher.equals("-1")) {
                            return;
                        }
                        writer.write(teacher);
                        writer.println();
                        writer.flush();
                        int coursesCounter = Integer.parseInt(reader.readLine());
                        //receives number of courses from server
                        int currentCourse = 0;
                        if (coursesCounter == 0) { //if there are no created courses
                            JOptionPane.showMessageDialog(null,
                                    "Your teacher has not created any courses",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            n++;
                            while (currentCourse < coursesCounter) {
                                coursesList.add(reader.readLine());
                                currentCourse++;
                            }

                            String course = student.pickCourse(coursesList, socket);
                            //runs method to have student pick a course
                            writer.write(course); //writes course to server
                            writer.println();
                            writer.flush();
                            String checkLine = null;
                            int quizzesCounter = Integer.parseInt(reader.readLine());
                            int currentQuiz = 0;
                            ArrayList<String> quizzesList = new ArrayList<>();
                            if (quizzesCounter == 0) {
                                JOptionPane.showMessageDialog(null,
                                        "No quizzes exist in this course",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                            } else {
                                while (currentQuiz < quizzesCounter) {
                                    quizzesList.add(reader.readLine());
                                    currentQuiz++;
                                }
                                String quiz = student.pickQuiz(quizzesList, socket);
                                writer.write(quiz); //writes course to server
                                writer.println();
                                writer.flush();
                                String canRun = reader.readLine();
                                if (canRun.equals("Empty Quiz")) {
                                    JOptionPane.showMessageDialog(null, "Quiz is empty",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                } else {
                                    String run = reader.readLine();
                                    if (run.equals("run quiz")) {
                                        //checks if quiz has already been taken. if not then runs the quiz
                                        student.runNewQuiz(writer, reader);
                                    } else {
                                        String viewGrade = String.valueOf(JOptionPane.showConfirmDialog
                                                (null, "Would you like to view your quiz grade?",
                                                        "QUIZ ALREADY TAKEN", JOptionPane.YES_NO_OPTION));
                                        if (viewGrade.equals("-1") || viewGrade.equals("1")) {
                                            return;
                                        }
                                        writer.write(viewGrade); //tells the server to retrieve quiz results or not
                                        writer.println();
                                        writer.flush();

                                        int gradeSize = Integer.parseInt(reader.readLine());
                                        int end = 0;

                                        if (gradeSize == 0) {
                                            end = JOptionPane.showConfirmDialog(null,
                                                    reader.readLine(), "Quiz Results", JOptionPane.OK_CANCEL_OPTION);
                                        } else {
                                            String grade = "";
                                            for (int l = 0; l < gradeSize; l++) { //formats info from server to string
                                                grade = grade + reader.readLine() + "\n";
                                            }
                                            grade = grade.substring(0, grade.length() - 1);
                                            end = JOptionPane.showConfirmDialog(null, grade,
                                                    "Quiz Results", JOptionPane.OK_CANCEL_OPTION);
                                        }

                                        if (end == -1 || end == JOptionPane.CANCEL_OPTION) {
                                            return;
                                        }
                                    }
                                }
                            }
                        }
                    }
                }


            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
