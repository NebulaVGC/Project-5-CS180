import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.*;
/**
 * Project 04 -- Student
 * <p>
 * This class is the student object for the quiz taking system.
 *
 * @author Matt Zlatinksi, Victor Dollosso L09
 * @version April 11, 2022
 */

public class Student {
    private Socket socket;
    private String userName;

    public Student(String userName, Socket socket) { //creates a new student obj with the username from login
        this.userName = userName;
        this.socket = socket;
    }

    /*
    method that runs the quiz and gives the user an option to attach a file to the quiz
     */
    public void runNewQuiz(PrintWriter writer, BufferedReader reader) throws IOException {
        String[] options = {"Active", "Import File"};
        int quizType = JOptionPane.showOptionDialog(null, "Select how you would like to take the quiz",
                "Quiz Type", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        int questionCounter = Integer.parseInt(reader.readLine());
        int currentQ = 0;
        String answer = "";
        ArrayList<String> answerList = new ArrayList<>();
        ArrayList<String> copyQuizContents = new ArrayList<>();
        copyQuizContents.add(reader.readLine());
        if (quizType == 0) {
            while (currentQ < questionCounter) {
                String Question = reader.readLine();
                String A = reader.readLine();
                String B = reader.readLine();
                String C = reader.readLine();
                String D = reader.readLine();
                currentQ++;
                String[] answerOptions = {"A", "B", "C", "D"};
                answer = (String) JOptionPane.showInputDialog(null,
                        Question + "\n" + A + "\n" + B + "\n" + C + "\n" + D,
                        "Question " + currentQ, JOptionPane.QUESTION_MESSAGE, null, answerOptions, answerOptions[0]);
                copyQuizContents.add(Question);
                copyQuizContents.add(A);
                copyQuizContents.add(B);
                copyQuizContents.add(C);
                copyQuizContents.add(D);
                answerList.add(answer);
            }
            copyQuizContents.add(userName);
            for (int i = 0; i < answerList.size(); i++) {
                copyQuizContents.add(answerList.get(i));
            }
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            copyQuizContents.add(timeStamp);
            writer.write(copyQuizContents.size());
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
                    "Enter the name of the file you would like to input", "File Input", JOptionPane.QUESTION_MESSAGE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            String newLine = "";
            while ((newLine = bufferedReader.readLine()) != null) {
                copyQuizContents.add(newLine);
            }
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            copyQuizContents.add(timeStamp);
            writer.write(copyQuizContents.size());
            writer.println();
            writer.flush();
        }
        for (int i = 0; i < copyQuizContents.size(); i++) {
            writer.write(copyQuizContents.get(i));
            writer.println();
            writer.flush();
        }
    }

    public void runQuizNew(String quizName, String username, String teacherName, String courseName, String plainQuizName, Socket socket) throws IOException{
        String[] options = {"Active", "Import File"};
        int quizType = JOptionPane.showOptionDialog(null, "Select how you would like to take the quiz",
                "Quiz Type", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
        System.out.println(quizType);
        String copyQuiz = "";
        PrintWriter printWriter = new PrintWriter(socket.getOutputStream());
        printWriter.write("Add Completed Quiz");
        printWriter.println();
        printWriter.flush();
        if (quizType == 0) {
            BufferedReader br = new BufferedReader(new FileReader(quizName));
            copyQuiz = br.readLine();
            String firstLine = "";
            String firstQ; //stores answer a
            String secondQ; //stores answer b
            String thirdQ; //stores answer c
            String fourthQ; //stores answer d
            String answer = "";
            int questionNum = 1;
            String[] answerOptions = {"A", "B", "C", "D"};
            while ((firstLine = br.readLine()) != null) {
                firstQ = br.readLine();
                secondQ = br.readLine();
                thirdQ = br.readLine();
                fourthQ = br.readLine();
                answer = answer + JOptionPane.showInputDialog(null,
                        firstLine + "\n" + firstQ + "\n" + secondQ + "\n" + thirdQ + "\n" + fourthQ,
                        "Question " + questionNum, JOptionPane.QUESTION_MESSAGE, null, answerOptions, answerOptions[0]) + "\n";
                copyQuiz = copyQuiz + firstLine + "\n" + firstQ + "\n" + secondQ + "\n" + thirdQ + "\n" + fourthQ + "\n";
                questionNum++;
            }
            br.close();
        } else if (quizType == 1) {
            BufferedReader br = new BufferedReader(new FileReader(quizName));
            copyQuiz = br.readLine();
            String firstLine = "";
            String answer = "";
            int questionNum = 1;
            String firstQ; //stores answer a
            String secondQ; //stores answer b
            String thirdQ; //stores answer c
            String fourthQ; //stores answer d
            while ((firstLine = br.readLine()) != null) {
                firstQ = br.readLine();
                secondQ = br.readLine();
                thirdQ = br.readLine();
                fourthQ = br.readLine();
                JOptionPane.showMessageDialog(null, firstLine + "\n" + br.readLine()
                                + "\n" + br.readLine() + "\n" + br.readLine() + "\n" + br.readLine(),
                        "Question " + questionNum, JOptionPane.QUESTION_MESSAGE);
                copyQuiz = copyQuiz + firstLine + "\n" + firstQ + "\n" + secondQ + "\n" + thirdQ + "\n" + fourthQ + "\n";
                questionNum++;
            }
            br.close();
            String fileName = JOptionPane.showInputDialog(null,
                    "Enter the name of the file you would like to input", "File Input", JOptionPane.QUESTION_MESSAGE);
            BufferedReader bufferedReader = new BufferedReader(new FileReader(fileName));
            while ((firstLine = bufferedReader.readLine()) != null) {
                answer = answer + firstLine + "\n";
            }
            answer = answer.substring(0, answer.length()-1);
            bufferedReader.close();
            String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
            String identifier = (username + "_" + teacherName + "_" + courseName + "_" + quizName);
            printWriter.write(identifier + "\n" + copyQuiz + username + "\n" + answer + timeStamp);
            printWriter.println();
            printWriter.flush();
            printWriter.close();
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
        courseName = (String) JOptionPane.showInputDialog(null, "Please select which course you would like to take?",
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
        quizName = (String) JOptionPane.showInputDialog(null, "Please select which quiz you would like to take?",
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
                        JOptionPane.showMessageDialog(null, "There are no existing teachers to choose from",
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
                        teacher = (String) JOptionPane.showInputDialog(null, "What is the username of your teacher?"
                                , "Teacher Selection", JOptionPane.QUESTION_MESSAGE, null, teacherOptions, teacherOptions[0]);
                        if (teacher.equals("-1")) {
                            return;
                        }
                        writer.write(teacher);
                        writer.println();
                        writer.flush();
                        int coursesCounter = Integer.parseInt(reader.readLine());
                        System.out.println(coursesCounter);
                        int currentCourse = 0;
                        if (coursesCounter == 0) {
                            JOptionPane.showMessageDialog(null, "Your teacher has not created any courses",
                                    "Error", JOptionPane.ERROR_MESSAGE);
                        } else {
                            n++;
                            while (currentCourse < coursesCounter) {
                                coursesList.add(reader.readLine());
                                currentCourse++;
                            }

                            String course = student.pickCourse(coursesList, socket);//runs method to have student pick a course
                            writer.write(course); //writes course to server
                            writer.println();
                            writer.flush();
                            String checkLine = null;
                            int quizzesCounter = Integer.parseInt(reader.readLine());
                            int currentQuiz = 0;
                            ArrayList<String> quizzesList = new ArrayList<>();
                            if (quizzesCounter == 0) {
                                JOptionPane.showMessageDialog(null, "No quizzes exist in this course",
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
                                        System.out.println("Quiz already taken");
                                        int optionLoop = 0;
                                        while (optionLoop == 0) {
                                            System.out.println("1. View Quiz Grade\n2. Exit Back to Courses");
                                            String viewGrade = "1";
                                            if (viewGrade.equals("1")) {
                                                File f = new File(student.userName + "_" + teacher +
                                                        "_" + course + "_" + quiz + ".txt");
                                                BufferedReader brquiz = new BufferedReader(new FileReader(f));
                                                String quizLine = brquiz.readLine();
                                                int cline = 0;
                                                while (quizLine != null) {
                                                    cline++;
                                                    quizLine = brquiz.readLine(); //reads through all line
                                                }
                                                BufferedReader brquiz2 = new BufferedReader(new FileReader(f));
                                                //reads the same quiz with new bfr
                                                BufferedReader gradeSetter = new BufferedReader(new FileReader(f));
                                                String lastLine = "";
                                                String grade = "";
                                                for (int i = 0; i < cline; i++) {
                                                    lastLine = brquiz2.readLine();
                                                }
                                                brquiz.close();
                                                String nextLine1 = "";
                                                nextLine1 = gradeSetter.readLine();
                                                while (!(nextLine1).equals(userName)) {
                                                    nextLine1 = gradeSetter.readLine();
                                                }


                                                if (lastLine.substring(lastLine.length() - 1).equals("%")) {
                                                    while (nextLine1 != null) {
                                                        grade = grade + nextLine1 + "\n";
                                                        nextLine1 = gradeSetter.readLine();
                                                    }
                                                } else {
                                                    grade = "Grade not yet entered";
                                                }
                                                System.out.println(grade);
                                                loop++;
                                                optionLoop++;
                                            } else if (viewGrade.equals("2")) {
                                                optionLoop++;
                                            } else {
                                                System.out.println("Invalid Input. Try again.");
                                            }
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

    public void shuffle(String filename) throws IOException {
        //Shuffles the question order and the answer order of the quiz file
        ArrayList<String> questions = new ArrayList<>();   //array that stores full questions with answers attached
        File f = new File(filename);
        BufferedReader br = new BufferedReader(new FileReader(f));
        BufferedReader bfr = new BufferedReader(new FileReader(filename));
        String line = "";       //initiates a string
        int numq = 1;
        String shuffleStatus = bfr.readLine();
        while (bfr.readLine() != null) {
            numq++;           //counts the number of lines in file
        }
        bfr.close();
        numq = numq / 5;   //counts number of questions
        int lineNum = 1;
        int mult = 0;
        br.readLine();
        while (line != null) {
            line = br.readLine();
            ++lineNum;
            if (lineNum == ( 2 + 5 * mult) && mult <= numq && line != null) {
                mult++;
                String q = "";
                String ans = "";
                q = line + "\n";
                lineNum++;
                q += br.readLine() + "\n";
                lineNum++;
                q += br.readLine() + "\n";
                lineNum++;
                q += br.readLine() + "\n";
                lineNum++;
                q += br.readLine() + "\n";
                questions.add(q);
            }
        }
        br.close();
        Collections.shuffle(questions);  //shuffles question order

        ArrayList<String> newQuestions = new ArrayList<>(); //list that new shuffled answers will be stored in
        for (int i = 0; i < questions.size(); i++) {
            String wholeQuestion = questions.get(i); //stores one question
            String q = wholeQuestion.substring(0, wholeQuestion.indexOf("\n")); //question line
            String answerOptions = wholeQuestion.substring(wholeQuestion.indexOf("\n"));
            answerOptions = answerOptions.substring(1);
            String a = answerOptions.substring(0, answerOptions.indexOf("\n"));
            String a1 = a.substring(3);   //answer option A without "A. "
            answerOptions = answerOptions.substring(answerOptions.indexOf("\n"));
            answerOptions = answerOptions.substring(1);
            String b = answerOptions.substring(0, answerOptions.indexOf("\n"));
            String b1 = b.substring(3); //answer option B without "B. "
            answerOptions = answerOptions.substring(answerOptions.indexOf("\n"));
            answerOptions = answerOptions.substring(1);
            String c = answerOptions.substring(0, answerOptions.indexOf("\n"));
            String c1 = c.substring(3);  //answer option C without "C. "
            answerOptions = answerOptions.substring(answerOptions.indexOf("\n"));
            answerOptions = answerOptions.substring(1);
            String d = answerOptions.substring(0, answerOptions.indexOf("\n"));
            String d1 = d.substring(3);  //answer option D without "D. "
            ArrayList<String> answerHolder = new ArrayList<>(); //holds answer options
            answerHolder.add(a1);
            answerHolder.add(b1);
            answerHolder.add(c1);
            answerHolder.add(d1);
            Collections.shuffle(answerHolder); //shuffles answer options
            String answerI = "";
            for (int j = 0; j < 4; j++) { //attaches an "A. ", "B. ", "C. ", or "D. " in front of answer option
                if (j == 0) {
                    answerI += ("\n" + "A. " + answerHolder.get(j));
                } else if (j == 1) {
                    answerI += ("\nB. " + answerHolder.get(j));
                } else if (j == 2) {
                    answerI += ("\nC. " + answerHolder.get(j));
                } else if (j == 3) {
                    answerI += ("\nD. " + answerHolder.get(j));
                }
            }

            String total = q + answerI;
            newQuestions.add(total);  //string of answer options
        }
        BufferedWriter bw = new BufferedWriter(new FileWriter(filename));
        //new buffered writer to write back to quiz file
        bw.write(shuffleStatus); //writes "True" at top of file
        bw.write("\n");
        for (int k = 0; k < newQuestions.size(); k++) { //writes full shuffled quiz back to file
            if (k < newQuestions.size() - 1) {
                bw.write(newQuestions.get(k));
                bw.write("\n");
            } else {
                bw.write(newQuestions.get(k));
            }
        }
        bw.close();
    }

}
