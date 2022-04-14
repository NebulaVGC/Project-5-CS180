import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * Project 04 -- Teacher
 * <p>
 * This class is the teacher object for the quiz making and taking system.
 *
 * @author Chase Thompsion, Kacey Atkins L09
 * @version March 8, 2022
 */
public class Teacher {
    private final String name;      //The teachers name
    private Scanner scan;    //Scanner for this class

    /**
     * Constructor for Teacher. THe constructor creates a new empty file that is used to hold the courses
     *
     * @param name The name of the teacher
     */
    public Teacher(String name) {
        this.name = name;
        File f = new File(name + "_Courses.txt");       //Creates the empty file
        try {
            f.createNewFile();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * This method creates a course with a course name that the teacher enters. The name is then added to the courses
     * text file for that teacher.
     */
    public void createCourse() {
        System.out.println("Enter the name of the new course or press N to exit");
        String temp = scan.next();      //Temp used to fix scanner errors
        String courseName = temp + scan.nextLine(); //The course name that the teacher wants
        if (courseName.equals("N")) {
            return;
        }
        try {
            File f1 = new File(name + "_Courses.txt");  //The courses file for the teacher
            PrintWriter pw = new PrintWriter(new FileOutputStream(f1, true));   //Printer writer for the f1
            File f2 = new File(name + "_" + courseName + ".txt");   //File that holds the quizzes for the
            //given course
            f2.createNewFile();
            pw.println(courseName);
            pw.close();
            System.out.println("Course added successfully");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deletes a course that the teacher enters if it exists. The course is removed from the courses text file
     * that the teacher has and removes the file that holds the quizzes for the course
     */
    public void deleteCourse() {
        String courseName;      //The course to be deleted
        ArrayList<String> list = new ArrayList<>();     //List to hold the file contents
        File f1;    //File to check if the given course exists
        do {
            System.out.println("Enter the name of the course you want to delete or press N to exit");
            String temp = scan.next();
            courseName = temp + scan.nextLine();
            f1 = new File(name + "_" + courseName + ".txt");
            if (courseName.equals("N")) {
                return;
            } else if (!f1.exists()) {
                System.out.print("That course does not exist. ");
            }

        } while (!f1.exists());
        File f = new File(name + "_Courses.txt");   //File that holds the courses for the teacher
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(f));  //Buffered reader for the courses file
            String line = bfr.readLine();   //Reads the line in the reader
            while (line != null) {
                if (!line.equals(courseName)) {
                    list.add(line);
                    line = bfr.readLine();
                } else {
                    line = bfr.readLine();
                }
            }
            BufferedReader bfr2 = new BufferedReader(new FileReader(f1));
            line = bfr2.readLine();
            while (line != null) {
                File quizFile = new File(name + "_" + courseName + "_" + line + ".txt");
                quizFile.delete();
                File answerFIle = new File(name + "_" + courseName + "_" + line + "_correctAnswers.txt");
                answerFIle.delete();
                line = bfr2.readLine();
            }

            PrintWriter pw = new PrintWriter(new FileOutputStream(f));  //Print writer that will go over the courses
            //file and rewrite the file without the given course
            for (int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }

            pw.close();
            bfr.close();
            bfr2.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        File f3 = new File(name + "_" + courseName + ".txt");  //File that holds the quizzes for the course
        f3.delete();
        System.out.println("Course deleted successfully!");




    }

    /**
     * Creates a quiz with a given course name and quiz name. The text file that holds the quiz is specific and must
     * be written in that way in order to work
     */
    public void createQuiz() {
        String courseName;  //The name of the course the quiz is going to be in
        String quizName;    //The name of the quiz to be created
        int contains = 0;   //Used to determine if the course is contained in the courses text file
        PrintWriter pw = null;  //Print writer used to write the quiz file
        PrintWriter pw2 = null;  //Print writer used to write the correct quiz answer file
        boolean randomized = false; // Used to determine if the teacher wants the quiz to be randomized
        File courseFile;    //Used to determine if the course file exists
        String temp;    //Used to help fix scanner issues
        do {
            System.out.println("Enter the course name or press N to exit");
            temp = scan.next();
            courseName = temp + scan.nextLine();
            if (courseName.equals("N")) {
                return;
            }
            courseFile = new File(name + "_" + courseName + ".txt");
            if (courseFile.exists()) {
                contains = 1;
            }
            if (contains == 0) {
                System.out.print("This course does not exist. ");
            }
        } while (contains == 0);
        System.out.println("Enter the name of the quiz or press N to exit");
        temp = scan.next();
        quizName = temp + scan.nextLine();
        if (quizName.equals("N")) {
            return;
        }
        File courseQuiz = new File(name + "_" + courseName + ".txt");   //The file that holds the quizzes
        PrintWriter pw3 = null; //Print writer that writes the quiz name into the file that holds the quizzes
        try {
            pw3 = new PrintWriter(new FileOutputStream(courseQuiz, true));
            pw3.println(quizName);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        int imported = 0;   //Used to determine if the teacher wants to import a file or not
        do {
            System.out.println("Do you want to create a quiz or import a quiz? Press N to exit\n1: Create\n2: Import");
            temp = scan.next();
            String importedString = temp + scan.nextLine();     //Used to determine what the teacher answered
            if (importedString.equals("N")) {
                return;
            }
            try {
                imported = Integer.parseInt(importedString);
                if (imported < 1 || imported > 2) {
                    throw new NumberFormatException();
                }
            } catch (NumberFormatException e) {
                System.out.println("Not an option");
                imported = 0;
            }
        } while (imported < 1 || imported > 2);
        if (imported == 2) {
            System.out.println("What is the filepath of the quiz?");
            temp = scan.next();
            String filePath = temp + scan.nextLine();   //Used to determine what the teacher answered for the filepath
            File f = new File(filePath);    //The file for the filepath the teacher answered
            if (!f.exists()) {
                do {
                    System.out.println("That file does not exist. Enter a new file or press N to exit");
                    temp = scan.next();
                    filePath = temp + scan.nextLine();
                    f = new File(filePath);
                    if (filePath.equals("N")) {
                        return;
                    }
                } while (!f.exists());

            }
            try {
                ArrayList<String> importList = new ArrayList<>();   //List to hold the lines within the import file
                BufferedReader bfr = new BufferedReader(new FileReader(f));   //Buffered reader that reads the
                //import file
                String line = bfr.readLine();   //Reads the next line in the buffered reader
                while (line != null) {
                    importList.add(line);
                    line = bfr.readLine();
                }
                File f2 = new File(name + "_" + courseName + "_" + quizName + ".txt");  //File for the quiz
                f.createNewFile();
                PrintWriter pw4 = new PrintWriter(new FileOutputStream(f2));  //Print writer for the quiz
                for (int i = 0; i < importList.size(); i++) {
                    pw4.println(importList.get(i));
                }
                System.out.println("The quiz has been created");
                pw4.close();
                bfr.close();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            String stringRandom;    //Determines if the teacher wants the quiz randomized or not
            do {
                System.out.println("Do you want the quiz to be randomized? Y/N. Press E to exit.");
                temp = scan.next();
                stringRandom = temp + scan.nextLine();

                if (stringRandom.equals("E")) {
                    return;
                } else if (stringRandom.equals("Y")) {
                    randomized = true;
                } else if (stringRandom.equals("N")) {
                    randomized = false;
                } else {
                    System.out.println("Not an option");
                    stringRandom = null;
                }
            } while (stringRandom == null);


            try {
                File f = new File(name + "_" + courseName + "_" + quizName + ".txt");   //File for the quiz
                pw = new PrintWriter(new FileOutputStream(f));
                File f2 = new File(name + "_" + courseName + "_" + quizName + "_correctAnswers.txt");  //File
                //for the answer sheet
                pw2 = new PrintWriter(new FileOutputStream(f2));


            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            int count = 1;
            int choice = 1;

            do {
                System.out.println("Do you want to add a question?");
                System.out.println("1.Yes");
                System.out.println("2.No");
                choice = scan.nextInt();
                if (choice != 1 && choice != 2) {
                    System.out.println("Not an option");
                }
            } while (choice != 1 && choice != 2);
            pw.println(randomized);
            while (choice == 1) {

                System.out.println("Please type question " + count);
                temp = scan.next();
                String givenQuestion = temp + scan.nextLine();  //Holds the question to be asked
                System.out.println("Please type answer option A");
                temp = scan.next();
                String answerA = temp + scan.nextLine();    //Holds the answer choice A
                System.out.println("Please type answer option B");
                temp = scan.next();
                String answerB = temp + scan.nextLine();    //Holds the answer choice B
                System.out.println("Please type answer option C");
                temp = scan.next();
                String answerC = temp + scan.nextLine();    //Holds the answer choice C
                System.out.println("Please type answer option D");
                temp = scan.next();
                String answerD = temp + scan.nextLine();    //Holds the answer choice D
                String correctAnswer;       //Holds the correct ansewr choice
                do {
                    System.out.println("Please enter the correct answer choice A, B, C or D");
                    temp = scan.next();
                    correctAnswer = temp + scan.nextLine();
                    if (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C")
                            && !correctAnswer.equals("D")) {
                        System.out.println("That is not an option");
                    }
                } while (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C")
                        && !correctAnswer.equals("D"));
                count = count + 1;

                pw.println(givenQuestion);
                pw.println("A. " + answerA);
                pw.println("B. " + answerB);
                pw.println("C. " + answerC);
                pw.println("D. " + answerD);
                pw2.println(correctAnswer);
                do {
                    System.out.println("Do you want to add another question?");
                    System.out.println("1.Yes");
                    System.out.println("2.No");
                    choice = scan.nextInt();
                    if (choice != 1 && choice != 2) {
                        System.out.println("Not an option");
                    }
                } while (choice != 1 && choice != 2);
            }

            System.out.println("The quiz is completed");

            pw.close();
            pw2.close();
            pw3.close();
            pw2.close();
        }
    }

    /**
     * Allows the user to edit the quiz by changing a certain line or adding more questions. If the true or false is
     * removed from the top of the quiz, the quiz will be read as already being graded.
     */
    public void editQuiz() {
        ArrayList<String> list = new ArrayList<>(); //List to help print out the contents of the quiz
        String temp;    //Used to help fix scanner issues
        String courseName;  //The name of the course
        File courseFile;    //The file for the course
        File f2;    //The file for the quiz
        do {
            System.out.println("Enter the course the quiz is in or press N to exit");
            temp = scan.next();
            courseName = temp + scan.nextLine();
            courseFile = new File(name + "_" + courseName + ".txt");
            if (courseName.equals("N")) {
                return;
            }

            if (!courseFile.exists()) {
                System.out.print("That course does not exist. ");
            }
        } while (!courseFile.exists());
        System.out.println("Enter the name of the quiz you want to edit or press N to exit.");
        temp = scan.next();
        String quizName = temp + scan.nextLine();
        if (quizName.equals("N")) {
            return;
        }
        do {
            f2 = new File(name + "_" + courseName + "_" + quizName + ".txt");
            if (quizName.equals("N")) {
                return;
            } else if (!f2.exists()) {
                System.out.println("That quiz does not exist. Enter another quiz or press N to exit");
                temp = scan.next();
                quizName = temp + scan.nextLine();
                f2 = new File(name + "_" + courseName + "_" + quizName);

            }

        } while (!f2.exists());
        try {
            int counter = 1;    //Counter to show the line numbers
            String lineNumber;  //Used to determine what the user wants to do
            String newLine = "";    //Determine what the new line should say
            int newLineNumber = 0;  //Determine which line the teacher wants to edit
            BufferedReader bfr = new BufferedReader(new FileReader(f2));    //Buffered reader for the quiz file
            String line = bfr.readLine();   //Used to read the next line in the buffered reader
            while (line != null) {
                System.out.println(counter + ": " + line);
                list.add(line);
                line = bfr.readLine();
                counter++;
            }

            System.out.println("Enter the number of the line you would like to edit, press Y to add more questions," +
                    " or press N to exit");
            temp = scan.next();
            lineNumber = temp + scan.nextLine();
            do {
                newLine = "";
                if (lineNumber.equals("N")) {
                    return;
                } else if (lineNumber.equals("Y")) {
                    File answers = new File(name + "_" + courseName + "_" + quizName + "_correctAnswers.txt");
                    //The correct answers file
                    PrintWriter pw5 = new PrintWriter(new FileOutputStream(answers, true));     //The print
                    //writer for the answer sheet
                    PrintWriter pw2 = new PrintWriter(new FileOutputStream(f2, true));  //The print writer
                    //for the quiz file
                    int choice = 0; //Determine if the user would like to add anohter question
                    while (choice == 0) {
                        System.out.println("Please type the next question");
                        temp = scan.next();
                        String givenQuestion = temp + scan.nextLine();  //Holds the question string
                        System.out.println("Please type answer option A");
                        temp = scan.next();
                        String answerA = temp + scan.nextLine();        //Holds the answer A option
                        System.out.println("Please type answer option B");
                        temp = scan.next();
                        String answerB = temp + scan.nextLine();        //Holds the answer B option
                        System.out.println("Please type answer option C");
                        temp = scan.next();
                        String answerC = temp + scan.nextLine();        //Holds the answer C option
                        System.out.println("Please type answer option D");
                        temp = scan.next();
                        String answerD = temp + scan.nextLine();        //Holds the answer D option
                        String correctAnswer;       //Holds the correct answer
                        do {
                            System.out.println("Please enter the correct answer choice A, B, C or D");
                            temp = scan.next();
                            correctAnswer = temp + scan.nextLine();
                            if (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C")
                                    && !correctAnswer.equals("D")) {
                                System.out.println("That is not an option");
                            }
                        } while (!correctAnswer.equals("A") && !correctAnswer.equals("B") && !correctAnswer.equals("C")
                                && !correctAnswer.equals("D"));

                        pw2.println(givenQuestion);
                        pw2.println("A. " + answerA);
                        pw2.println("B. " + answerB);
                        pw2.println("C. " + answerC);
                        pw2.println("D. " + answerD);
                        pw5.println(correctAnswer);
                        do {
                            System.out.println("Do you want to add another question?");
                            System.out.println("1.Yes");
                            System.out.println("2.No");
                            choice = scan.nextInt();
                            if (choice != 1 && choice != 2) {
                                System.out.println("Not an option");
                            }
                        } while (choice != 1 && choice != 2);
                    }

                } else {
                    try {
                        newLineNumber = Integer.parseInt(lineNumber);
                        if (newLineNumber < 1 || newLineNumber > list.size()) {
                            throw new NumberFormatException();
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Not an option");
                        System.out.println("Enter the number of the line you would like to edit, press Y to " +
                                "add more questions, or press N to exit");
                        temp = scan.next();
                        lineNumber = temp + scan.nextLine();
                        newLineNumber = 0;

                    }
                }
            } while (newLineNumber < 1 || newLineNumber > list.size());
            System.out.println(list.get(newLineNumber - 1));
            System.out.println("What should the line say now?");
            temp = scan.next();
            newLine = temp + scan.nextLine();

            list.set(newLineNumber - 1, newLine);
            PrintWriter pw = new PrintWriter(new FileOutputStream(f2));    //Print writer for the quiz
            for (int i = 0; i < list.size(); i++) {
                pw.println(list.get(i));
            }
            System.out.println("Quiz edited successfully");
            pw.close();
            bfr.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    /**
     * Deletes a quiz with the given course name and quiz name. The quiz is deleted from the course file that holds
     * the quizzes.
     */
    public void deleteQuiz() {
        String courseName;  //The course name for the quiz
        File courseFile;    //The file for the course
        do {
            System.out.println("Enter the course the quiz is in or press N to exit");
            String temp = scan.next();  //Help fix scanner errors
            courseName = temp + scan.nextLine();
            courseFile = new File(name + "_" + courseName + ".txt");
            if (courseName.equals("N")) {
                return;
            } else if (!courseFile.exists()) {
                System.out.print("That course does not exist. ");
            }
        } while (!courseFile.exists());
        File file;  //File for the quiz
        File file2; //File for the answer sheet
        do {
            System.out.println("Enter the name of the quiz you want to delete or press N to exit");
            String temp = scan.next();      //Helps fix scanner errors
            String quizName = temp + scan.nextLine();   //The quiz name the teacher entered
            file = new File(name + "_" + courseName + "_" + quizName + ".txt");
            file2 = new File(name + "_" + courseName + "_" + quizName + "_correctAnswers.txt");
            if (quizName.equals("N")) {
                return;
            } else if (!file.exists()) {
                System.out.print("That file does not exist. ");
            }
        } while (!file.exists());
        file.delete();
        file2.delete();
        System.out.println("File deleted successfully");
    }

    /**
     * Lets the teacher see the a certain students quiz submission. A student name, a course name, and a quiz name are
     * needed
     */
    public void viewStudentSubmission() {
        String student;     //The student whose submission will be viewed
        String courseName;  //The course name
        String quizName;    //The quiz name
        String temp;    //Helps fix scanner errors
        ArrayList<String> studentNames = new ArrayList<>(); //List that holds the current students names
        File courseFile;    //The file for the course
        File studentFile = new File("StudentAccount.txt");  //The file that holds the student names
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(studentFile));   //Buffered reader for student file
            String line = bfr.readLine();   //Reads the next line in the buffered reader
            while (line != null) {
                studentNames.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("No students have been added");
            return;
        }
        do {
            System.out.println("Enter the name of the student you would like to view or press N to exit");
            temp = scan.next();
            student = temp + scan.nextLine();
            if (student.equals("N")) {
                return;
            } else if (studentNames.indexOf(student) < 0) {
                System.out.print("That student does not exist. ");
            }
        } while (studentNames.indexOf(student) < 0);

        do {
            System.out.println("Enter the course or press N to exit");
            temp = scan.next();
            courseName = temp + scan.nextLine();
            courseFile = new File(name + "_" + courseName + ".txt");
            if (courseName.equals("N")) {
                return;
            } else if (!courseFile.exists()) {
                System.out.print("That course does not exist. ");
            }
        } while (!courseFile.exists());
        File quizFile;  //File for the quiz
        do {
            System.out.println("Enter the name of the quiz or press N to exit");
            temp = scan.next();
            quizName = temp + scan.nextLine();
            if (quizName.equals("N")) {
                return;
            }
            quizFile = new File(student + "_" + name + "_" + courseName + "_" + quizName + ".txt");
            if (!quizFile.exists()) {
                System.out.print("This quiz does not exist. ");
            }
        } while (!quizFile.exists());

        try {
            BufferedReader bfr = new BufferedReader(new FileReader(quizFile)); //Buffered reader for the quiz file
            String line = bfr.readLine();   //Reads the next line in the buffered reader
            while (line != null) {
                System.out.println(line);
                line = bfr.readLine();
            }
            bfr.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Allows the teacher to manually grade a students quiz
     */

    public void manualGrade() {
        String student; //The student whose quiz will be graded
        String courseName;  //The course the quiz is in
        String quizName;    //The quiz name
        String temp;    //Used to help fix scanner issues
        int numCorrect = 0; //Determines the number of correct answers
        ArrayList<String> studentNames = new ArrayList<>(); //Stores the student names
        File courseFile;    //Holds the course file
        File studentFile = new File("StudentAccount.txt");  //File for the student file
        try {
            BufferedReader bfr = new BufferedReader(new FileReader(studentFile));   //Buffered reader for student file
            String line = bfr.readLine();   //Reads the next line
            while (line != null) {
                studentNames.add(line);
                line = bfr.readLine();
            }
        } catch (IOException e) {
            System.out.println("No student has been added");
            return;
        }
        do {
            System.out.println("Enter the name of the student you would like to view or press N to exit");
            temp = scan.next();
            student = temp + scan.nextLine();
            if (student.equals("N")) {
                return;
            } else if (studentNames.indexOf(student) < 0) {
                System.out.print("That student does not exist. ");
            }
        } while (studentNames.indexOf(student) < 0);
        do {
            System.out.println("Enter the quiz course or press N to exit");
            temp = scan.next();
            courseName = temp + scan.nextLine();
            if (courseName.equals("N")) {
                return;
            }
            courseFile = new File(name + "_" + courseName + ".txt");
            if (!courseFile.exists()) {
                System.out.print("That course does not exist. ");
            }
        } while (!courseFile.exists());
        File quizFile;  //The file for the quiz
        do {
            System.out.println("Enter the name of the quiz or press N to exit");
            temp = scan.next();
            quizName = temp + scan.nextLine();
            if (quizName.equals("N")) {
                return;
            }
            quizFile = new File(name + "_" + courseName + "_" + quizName + ".txt");
            if (!quizFile.exists()) {
                System.out.print("That quiz does not exist. ");
            }
        } while (!quizFile.exists());
        File studentQuiz = new File(student + "_" + name + "_" + courseName + "_" + quizName + ".txt");
        //The file for the student's quiz
        if (!studentQuiz.exists()) {
            System.out.println("This student has not taken the quiz");
            return;
        } else {
            try {
                BufferedReader bfrQuestions = new BufferedReader(new FileReader(studentQuiz));  //Buffered reader for
                //the student quiz and reading the questions
                BufferedReader bfrAnswers = new BufferedReader(new FileReader(studentQuiz));    //Buffered reader for
                //the student quiz and reading the answer
                ArrayList<String> entireFile = new ArrayList<>();   //List to hold the files entire contents
                ArrayList<String> correct = new ArrayList<>();  //List to hold the teachers yes or no answers
                String questionLine = bfrQuestions.readLine();  //Reads the next line in the question buffered reader
                if (questionLine.equals("true") || questionLine.equals("false")) {
                    questionLine = bfrQuestions.readLine();
                    String answerLine = bfrAnswers.readLine();  //Reads the next line in the answer buffered reader
                    while (!answerLine.equals("A") && !answerLine.equals("B") && !answerLine.equals("C") &&
                            !answerLine.equals("D")) {
                        answerLine = bfrAnswers.readLine();
                    }
                    while (answerLine.equals("A") || answerLine.equals("B") || answerLine.equals("C")
                            || answerLine.equals("D")) {
                        System.out.println(questionLine);
                        questionLine = bfrQuestions.readLine();
                        System.out.println(questionLine);
                        questionLine = bfrQuestions.readLine();
                        System.out.println(questionLine);
                        questionLine = bfrQuestions.readLine();
                        System.out.println(questionLine);
                        questionLine = bfrQuestions.readLine();
                        System.out.println(questionLine);
                        System.out.println("Student Answer: " + answerLine);
                        answerLine = bfrAnswers.readLine();
                        String yesNo = null;    //Determines if the teacher entered yes or no
                        do {
                            System.out.println("Is this correct? Y/N");
                            temp = scan.next();
                            yesNo = temp + scan.nextLine();
                            if (!yesNo.equals("Y") && !yesNo.equals("N")) {
                                System.out.println("Try again");
                            } else if (yesNo.equals("Y")) {
                                numCorrect++;
                            }
                        } while (!yesNo.equals("Y") && !yesNo.equals("N"));
                        correct.add(yesNo);
                        questionLine = bfrQuestions.readLine();
                    }

                    BufferedReader bfr3 = new BufferedReader(new FileReader(studentQuiz));  //Buffered reader for the
                    //student quiz
                    String line = bfr3.readLine();  //Reads the next line in the buffered reader
                    line = bfr3.readLine();
                    while (line != null) {
                        entireFile.add(line);
                        line = bfr3.readLine();
                    }
                    double percentage = (double) numCorrect / correct.size() * 100;     //Holds the grade percentage
                    PrintWriter pw = new PrintWriter(new FileOutputStream(studentQuiz));    //Print writer for the
                    //student quiz
                    int j = 0;  //Counter for the answers
                    for (int i = 0; i < entireFile.size(); i++) {

                        if (entireFile.get(i).equals("A") || entireFile.get(i).equals("B") ||
                                entireFile.get(i).equals("C") || entireFile.get(i).equals("D")) {
                            if (correct.get(j).equals("Y")) {
                                pw.println(entireFile.get(i) + " Correct");
                            } else {
                                pw.println(entireFile.get(i) + " Incorrect");
                            }
                            j++;
                        } else {
                            pw.println(entireFile.get(i));
                        }

                    }
                    pw.println(percentage + "%");

                    pw.close();
                    bfrQuestions.close();
                    bfrAnswers.close();
                    bfr3.close();
                } else {
                    System.out.println("This quiz has already been graded");
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * The interface the teacher sees when they login. They have 8 choices to choose from.
     */
    public void teacherInterface(Scanner scanner) {
        scan = scanner;
        int choice = 0; //The teachers input
        while (choice != 8) {

            do {
                choice = 0;

                System.out.println("What would you like to do?\n1. Create Quiz\n2. Edit Quiz\n3. Delete Quiz");
                System.out.println("4. Grade Quiz\n5. View Student Submission\n6. Create Course\n7. Delete Course");
                System.out.println("8. Exit");
                try {
                    String temp = scan.next();  //Helps fix scanner issues
                    String stringChoice = temp + scan.nextLine();   //Determines what the teacher wants to do
                    choice = Integer.parseInt(stringChoice);
                } catch (NumberFormatException e) {
                    choice = 0;
                }
                if (choice < 1 || choice > 8) {
                    System.out.println("Not an option\n");
                    choice = 0;
                }

            } while (choice < 1 || choice > 8);

            if (choice == 1) {
                createQuiz();
            } else if (choice == 2) {
                editQuiz();
            } else if (choice == 3) {
                deleteQuiz();
            } else if (choice == 4) {
                manualGrade();
            } else if (choice == 5) {
                viewStudentSubmission();
            } else if (choice == 6) {
                createCourse();
            } else if (choice == 7) {
                deleteCourse();
            }
        }
        System.out.println("Goodbye!");
    }
}
