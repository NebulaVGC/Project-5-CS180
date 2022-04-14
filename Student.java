import javax.swing.*;
import java.io.*;
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
    private Scanner scan;
    private String userName;

    public Student(String userName) { //creates a new student obj with the username from login
        this.userName = userName;
    }

    /*
    method that runs the quiz and gives the user an option to attach a file to the quiz
     */
    public void runQuizNew() {
        String[] options = {"Active", "Import File"};
        int quizType = JOptionPane.showOptionDialog(null, "Select how you would like to take the quiz",
                "Quiz Type", JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE, null, options, null);
    }

    public void runQuiz(String quizName, String username, String teacherName, String courseName, String plainQuizName,
                        Scanner scan)
            throws IOException {
        this.scan = scan;
        BufferedReader br = new BufferedReader(new FileReader(quizName));
        File f = new File(username + "_" + teacherName + "_" + courseName + "_" + plainQuizName + ".txt");
        BufferedWriter bw = new BufferedWriter(new FileWriter(f));
        String nextLine = "";
        String shuffleStatus = br.readLine(); //reads the first line, determines whether it will be shuffled
        String copy = "";
        String totalCopy = "";
        String importedFile = "";
        String copyQuiz = ""; //copy of the quiz
        int counter = 0; //counts lines
        int loop = 0; //used for infinite loops
        int number = 1;
        String firstLine; // stores answer a
        String secondLine; //stores answer b
        String thirdLine; //stores answer c
        String fourthLine; //stores answer d
        while ((nextLine = br.readLine()) != null) { //reads line until null
            firstLine = br.readLine();
            secondLine = br.readLine();
            thirdLine = br.readLine();
            fourthLine = br.readLine();
            System.out.println(number + ". " + nextLine + "\n" + firstLine + "\n" + secondLine +
                    "\n" + thirdLine + "\n" + fourthLine);
            copyQuiz = copyQuiz + nextLine + "\n" + firstLine + "\n" + secondLine +
                    "\n" + thirdLine + "\n" + fourthLine + "\n"; //copies the whole quiz
            counter++;
            number++;
        }
        br.close();
        System.out.println("Would you like to attach a file with answers or answer in the quiz?");
        //gives user the option
        System.out.println("1. attach a file\n2. answer in the quiz"); //gives student the choice to attach a file
        while (loop == 0) {
            String option = scan.nextLine();
            if (option.equals("1")) { //import file
                System.out.println("Enter the name of the file you would like to import:");
                importedFile = scan.nextLine();
                BufferedReader bufferedReader3 = new BufferedReader(new FileReader(importedFile));
                while ((copy = bufferedReader3.readLine()) != null) {
                    totalCopy = totalCopy + copy + "\n";
                }
                loop++;
            } else if (option.equals("2")) { //take quiz on terminal
                for (int i = 1; i <= counter; i++) {
                    System.out.println("What is the answer to question " + i + ":");
                    totalCopy = totalCopy + scan.nextLine().toUpperCase() + "\n";
                }
                loop++;
            } else {
                System.out.println("Invalid Input: Try Again"); //input validation
            }
        }
        System.out.println("Thanks for taking the quiz!");
        String timeStamp = new SimpleDateFormat("MM/dd/yyyy_HH:mm:ss").format(Calendar.getInstance().getTime());
        //timestamp
        totalCopy = shuffleStatus + "\n" + copyQuiz + username + "\n" + totalCopy + timeStamp;
        bw.write(totalCopy); //writes to a file for the teacher to grade
        bw.close();

    }

    /*
    method that returns the course the student picks
     */
    public String pickCourse(String filename, Scanner scan) {
        this.scan = scan;
        String courseName = null; //the coursename
        try {
            int loop = 0;
            File course = new File(filename);
            FileReader frcourse = new FileReader(course);
            BufferedReader bfrCourse = new BufferedReader(frcourse);
            BufferedReader br = new BufferedReader(new FileReader(filename));
            courseName = "";
            br.close();
            String line = bfrCourse.readLine(); //read first line
            ArrayList<String> courseList = new ArrayList<>();
            while (line != null) { //prints out each course option
                courseList.add(line);
                line = bfrCourse.readLine();
            }
            String[] courseOptions = new String[courseList.size()];
            for (int i = 0; i < courseList.size(); i++) {
                courseOptions[i] = courseList.get(i);
            }
            courseName = (String) JOptionPane.showInputDialog(null, "Please select which course you would like to take?",
                    "Course Selection", JOptionPane.QUESTION_MESSAGE, null, courseOptions, courseOptions[0]);
            if (courseName.equals("-1")) {
                courseName = "end";
            }
        } catch (IOException io) {
            io.printStackTrace();
        }
        return courseName;
    }

    /*
    method that allows the student to choose which quiz to take. If they have already taken the quiz, it
    will say already taken and give them the option to see their grades and response for each answer
     */
    public String pickQuiz(String filename, Scanner scan) {
        this.scan = scan;
        String courseName = null;
        try {
            File course = new File(filename);
            FileReader frcourse = new FileReader(course);
            BufferedReader bfrCourse = new BufferedReader(frcourse);
            courseName = ""; //name of course
            String line = bfrCourse.readLine();
            ArrayList<String> quizList = new ArrayList<>();
            while (line != null) {
                quizList.add(line); //prints a number in front of each quiz
                line = bfrCourse.readLine();
            }
            String[] quizOptions = new String[quizList.size()];
            for (int i = 0; i < quizOptions.length; i++) {
                quizOptions[i] = quizList.get(i);
            }
            courseName = (String) JOptionPane.showInputDialog(null, "Please select which quiz you would like to take?",
                    "Quiz Selection", JOptionPane.QUESTION_MESSAGE, null, quizOptions, quizOptions[0]);
        } catch (IOException io) {
            io.printStackTrace();
        }
        return courseName;
    }


    public void mainStudent(Scanner scanner) throws IOException {
        scan = scanner;
        //takes place after login
        try {
            Student student = new Student(this.userName); //creates Student object
            int n = 0;
            String teacher = null; //initiates string to store teachers username
            while (n == 0) {
                BufferedReader br = new BufferedReader(new FileReader("TeacherAccount.txt"));
                String newLine = "";
                ArrayList<String> teacherList = new ArrayList<>();
                while ((newLine = br.readLine()) != null) {
                    teacherList.add(newLine);
                }
                String[] teacherOptions = new String[teacherList.size()];
                for (int i = 0; i < teacherList.size(); i++) {
                    teacherOptions[i] = teacherList.get(i);
                }
                teacher = (String) JOptionPane.showInputDialog(null, "What is the username of your teacher?"
                        , "Teacher Selection", JOptionPane.QUESTION_MESSAGE, null, teacherOptions, teacherOptions[0]);
                if (teacher.equals("-1")) {
                    return;
                }
                BufferedReader bufferedReader = new BufferedReader(new FileReader(teacher + "_Courses.txt"));
                String checkLine = bufferedReader.readLine();
                bufferedReader.close();
                if (!new File(teacher + "_Courses.txt").exists()) {
                    JOptionPane.showMessageDialog(null, "Your teacher has not created any courses",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if (checkLine == null) {
                    JOptionPane.showMessageDialog(null, "Your teacher has not created any courses",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else {
                    n++;
                }
            }
            int loop = 0; //initiates looping variable

            do {
                String course = student.pickCourse(teacher + "_Courses.txt", scanner);
                //runs method to have student pick a course
                String checkLine = null;
                try {
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(teacher + "_" + course + ".txt"));
                    checkLine = bufferedReader.readLine();
                } catch (Exception exception) {
                    JOptionPane.showMessageDialog(null, "No quizzes exist in this course",
                            "Error", JOptionPane.ERROR_MESSAGE);
                }
                if (checkLine == null && new File(teacher + "_" + course + ".txt").exists()) {
                    JOptionPane.showMessageDialog(null, "No quizzes exist in this course",
                            "Error", JOptionPane.ERROR_MESSAGE);
                } else if (checkLine != null && new File(teacher + "_" + course + ".txt").exists()) { //CONTINUE HERE
                    String quiz = student.pickQuiz(teacher + "_" + course + ".txt", scanner);

                    try {
                        BufferedReader br = new BufferedReader(new FileReader(teacher + "_" + course + "_" + quiz + ".txt"));
                        checkLine = br.readLine();
                    } catch (Exception e) {
                        JOptionPane.showMessageDialog(null, "Quiz is empty",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                    if (checkLine == null && new File(teacher + "_" + course + "_" + quiz + ".txt").exists()) {
                        JOptionPane.showMessageDialog(null, "Quiz is empty",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    } else if (checkLine != null && new File(teacher + "_" + course + "_" + quiz + ".txt").exists()) {
                        FileReader fr = new FileReader(teacher + "_" + course + "_" + quiz + ".txt");
                        BufferedReader bufferedReader2 = new BufferedReader(fr);
                        String shuffleStatus = bufferedReader2.readLine(); //whether or not to shuffle the quiz
                        if (shuffleStatus.equalsIgnoreCase("True")) {
                            student.shuffle(teacher + "_" + course + "_" + quiz + ".txt", scanner);
                            //shuffles quiz
                        }
                        if (!new File(student.userName + "_" + teacher + "_"
                                + course + "_" + quiz + ".txt").exists()) {
                            //checks if quiz has already been taken. if not then runs the quiz
                            student.runQuiz(teacher + "_" + course + "_" + quiz + ".txt",
                                    student.userName, teacher, course, quiz, scanner);
                        } else {
                            System.out.println("Quiz already taken");
                            int optionLoop = 0;
                            while (optionLoop == 0) {
                                System.out.println("1. View Quiz Grade\n2. Exit Back to Courses");
                                String viewGrade = scan.nextLine();
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
            } while (loop == 0);
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    public void shuffle(String filename, Scanner scan) throws IOException {
        this.scan = scan;
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

    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        Student student = new Student("username");
        try {
            student.mainStudent(scanner);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
