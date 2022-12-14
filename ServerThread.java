import javax.swing.*;
import java.io.*;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Collections;

public class ServerThread extends Thread {
    Socket socket = null;
    int i = 0;

    ArrayList<String> teacherAccountStore = new ArrayList<>();
    ArrayList<String> teacherPasswordStore = new ArrayList<>();
    ArrayList<String> studentAccountStore = new ArrayList<>();
    ArrayList<String> studentPasswordStore = new ArrayList<>();

    int usernameStatus = 0;

    public ServerThread(Socket socket) {
        this.socket = socket;
    }

    @Override
    public void run() {
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String action = reader.readLine();
            int k = 0;
            String username1 = null;

            if (action == null) {
                return;
            } else if (action.equals("create")) {// either teacher or student creating account
                String type = reader.readLine();
                if (type.equals("teacher")) { // teacher creating account
                    while (k == 0) {
                        username1 = reader.readLine();

                        if (username1 == null) {
                            break;
                        }
                        int check = checkTeacherUsername(username1);
                        if (check != 2) {
                            writer.write("success");
                            writer.println();
                            writer.flush();
                            k++;
                        } else {
                            writer.write("fail");
                            writer.println();
                            writer.flush();
                        }
                    }

                    if (username1 == null) {
                        return;
                    }

                    try {
                        String teacherPassword = reader.readLine();
                        if (teacherPassword == null) {
                            return;
                        }

                        teacherAccountStore.add(username1);
                        teacherPasswordStore.add(teacherPassword);

                        File teacherAccountFile = new File("TeacherAccount.txt");
                        FileOutputStream fos = new FileOutputStream(teacherAccountFile, true);
                        PrintWriter pw = new PrintWriter(fos);
                        pw.println(username1);
                        File teacherPasswordFile = new File("TeacherPassword.txt");
                        FileOutputStream fos2 = new FileOutputStream(teacherPasswordFile, true);
                        PrintWriter pw2 = new PrintWriter(fos2);
                        pw2.println(teacherPassword);
                        pw.close();
                        pw2.close();
                        run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else if (type.equals("student")) {// student creating account
                    while (k == 0) {
                        username1 = reader.readLine();

                        if (username1 == null) {
                            break;
                        }
                        int check = checkStudentUsername(username1);
                        if (check == 1) {
                            writer.write("success");
                            writer.println();
                            writer.flush();
                            k++;
                        } else {
                            writer.write("fail");
                            writer.println();
                            writer.flush();
                        }
                    }

                    if (username1 == null) {
                        return;
                    }

                    try {
                        String studentPassword = reader.readLine();
                        if (studentPassword == null) {
                            return;
                        }

                        studentAccountStore.add(username1);
                        studentPasswordStore.add(studentPassword);

                        File studentAccountFile = new File("StudentAccount.txt");
                        FileOutputStream fos = new FileOutputStream(studentAccountFile, true);
                        PrintWriter pw = new PrintWriter(fos);
                        pw.println(username1);
                        File studentPasswordFile = new File("StudentPassword.txt");
                        FileOutputStream fos2 = new FileOutputStream(studentPasswordFile, true);
                        PrintWriter pw2 = new PrintWriter(fos2);
                        pw2.println(studentPassword);
                        pw.close();
                        pw2.close();
                        run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (action.equals("edit")) {// either teacher or student edit account
                k = 0;
                String OldUsername = null;
                String type = reader.readLine();
                if (type.equals("teacher")) { // teacher editing account
                    try {
                        while (k == 0) {
                            OldUsername = reader.readLine();
                            if (OldUsername == null) {
                                break;
                            }
                            int status = checkTeacherUsername(OldUsername);
                            if (status == 2) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k++;
                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                            }
                        }

                        ArrayList<String> accountList = new ArrayList<>();
                        ArrayList<String> passwordList = new ArrayList<>();

                        String OldPassword = reader.readLine();
                        if (OldPassword == null) {
                            return;
                        }

                        File f = new File("TeacherAccount.txt");
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        String line = bfr.readLine();
                        usernameStatus = 1;
                        while (line != null) {
                            accountList.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();

                        File f2 = new File("TeacherPassword.txt");
                        FileReader fr2 = new FileReader(f2);
                        BufferedReader bfr2 = new BufferedReader(fr2);
                        String line2 = bfr2.readLine();

                        while (line2 != null) {
                            passwordList.add(line2);
                            line2 = bfr2.readLine();
                        }
                        bfr2.close();

                        int l = 0;
                        while (l == 0) {
                            if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k = 0;
                                String newUsername = null;
                                while (k == 0) {
                                    newUsername = reader.readLine();
                                    if (newUsername == null) {
                                        break;
                                    }

                                    int check = checkTeacherUsername(newUsername);

                                    if (check == 1) {
                                        writer.write("success");
                                        writer.println();
                                        writer.flush();
                                        k++;
                                    } else {
                                        writer.write("fail");
                                        writer.println();
                                        writer.flush();
                                    }
                                }

                                String newPassword = reader.readLine();
                                if (newPassword == null) {
                                    break;
                                }

                                editTeacherInformation(OldUsername, OldPassword, newUsername, newPassword);
                                l++;
                                break;

                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                                break;
                            }
                        }
                        run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (type.equals("student")) {// student editing account
                    try {
                        while (k == 0) {
                            OldUsername = reader.readLine();
                            if (OldUsername == null) {
                                break;
                            }
                            int status = checkStudentUsername(OldUsername);
                            if (status == 2) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k++;
                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                            }
                        }

                        ArrayList<String> accountList = new ArrayList<>();
                        ArrayList<String> passwordList = new ArrayList<>();

                        String OldPassword = reader.readLine();
                        if (OldPassword == null) {
                            return;
                        }

                        File f = new File("StudentAccount.txt");
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        String line = bfr.readLine();
                        usernameStatus = 1;
                        while (line != null) {
                            accountList.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();

                        File f2 = new File("StudentPassword.txt");
                        FileReader fr2 = new FileReader(f2);
                        BufferedReader bfr2 = new BufferedReader(fr2);
                        String line2 = bfr2.readLine();

                        while (line2 != null) {
                            passwordList.add(line2);
                            line2 = bfr2.readLine();
                        }
                        bfr2.close();

                        int l = 0;
                        while (l == 0) {
                            if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k = 0;
                                String newUsername = null;
                                while (k == 0) {
                                    newUsername = reader.readLine();
                                    if (newUsername == null) {
                                        break;
                                    }
                                    int check = checkStudentUsername(newUsername);

                                    if (check == 1) {
                                        writer.write("success");
                                        writer.println();
                                        writer.flush();
                                        k++;
                                    } else {
                                        writer.write("fail");
                                        writer.println();
                                        writer.flush();
                                    }
                                }

                                String newPassword = reader.readLine();
                                if (newPassword == null) {
                                    break;
                                }

                                editStudentInformation(OldUsername, OldPassword, newUsername, newPassword);
                                l++;
                                break;

                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                            }

                        }
                        run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            } else if (action.equals("delete")) { // either teacher or student deleting account
                k = 0;
                String OldUsername = null;
                String type = reader.readLine();
                if (type.equals("teacher")) { //teacher deleting account
                    try {
                        while (k == 0) {
                            OldUsername = reader.readLine();
                            if (OldUsername == null) {
                                break;
                            }
                            int status = checkTeacherUsername(OldUsername);
                            if (status == 2) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k++;
                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                            }
                        }

                        ArrayList<String> accountList = new ArrayList<>();
                        ArrayList<String> passwordList = new ArrayList<>();

                        String OldPassword = reader.readLine();
                        if (OldPassword == null) {
                            return;
                        }

                        File f = new File("TeacherAccount.txt");
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        String line = bfr.readLine();
                        usernameStatus = 1;
                        while (line != null) {
                            accountList.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();

                        File f2 = new File("TeacherPassword.txt");
                        FileReader fr2 = new FileReader(f2);
                        BufferedReader bfr2 = new BufferedReader(fr2);
                        String line2 = bfr2.readLine();

                        while (line2 != null) {
                            passwordList.add(line2);
                            line2 = bfr2.readLine();
                        }
                        bfr2.close();

                        int l = 0;
                        while (l == 0) {
                            if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {

                                writer.write("success");
                                writer.println();
                                writer.flush();
                                deleteTeacherInformation(OldUsername, OldPassword);
                                break;

                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                                break;
                            }
                        }
                        run();

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (type.equals("student")) {//student deleting account
                    try {
                        while (k == 0) {
                            OldUsername = reader.readLine();
                            if (OldUsername == null) {
                                break;
                            }
                            int status = checkStudentUsername(OldUsername);
                            if (status == 2) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                k++;
                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                            }
                        }

                        ArrayList<String> accountList = new ArrayList<>();
                        ArrayList<String> passwordList = new ArrayList<>();

                        String OldPassword = reader.readLine();
                        if (OldPassword == null) {
                            return;
                        }

                        File f = new File("StudentAccount.txt");
                        FileReader fr = new FileReader(f);
                        BufferedReader bfr = new BufferedReader(fr);
                        String line = bfr.readLine();
                        usernameStatus = 1;
                        while (line != null) {
                            accountList.add(line);
                            line = bfr.readLine();
                        }
                        bfr.close();

                        File f2 = new File("StudentPassword.txt");
                        FileReader fr2 = new FileReader(f2);
                        BufferedReader bfr2 = new BufferedReader(fr2);
                        String line2 = bfr2.readLine();

                        while (line2 != null) {
                            passwordList.add(line2);
                            line2 = bfr2.readLine();
                        }
                        bfr2.close();

                        int l = 0;
                        while (l == 0) {
                            if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {
                                writer.write("success");
                                writer.println();
                                writer.flush();
                                deleteStudentInformation(OldUsername, OldPassword);
                                break;

                            } else {
                                writer.write("fail");
                                writer.println();
                                writer.flush();
                                break;
                            }
                        }
                        run();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            } else if (action.equals("login")) {// either teacher or student login
                k = 0;
                String OldUsername = null;
                String type = reader.readLine();
                if (type.equals("teacher")) {//teacher loging in
                    try {
                        int u = 0;
                        while (u == 0) {
                            while (k == 0) {
                                OldUsername = reader.readLine();
                                if (OldUsername == null) {
                                    break;
                                }
                                int status = checkTeacherUsername(OldUsername);
                                if (status == 2) {
                                    writer.write("success");
                                    writer.println();
                                    writer.flush();
                                    k++;
                                } else {
                                    writer.write("fail");
                                    writer.println();
                                    writer.flush();
                                }
                            }

                            ArrayList<String> accountList = new ArrayList<>();
                            ArrayList<String> passwordList = new ArrayList<>();

                            String OldPassword = reader.readLine();
                            if (OldPassword == null) {
                                break;
                            }

                            File f = new File("TeacherAccount.txt");
                            FileReader fr = new FileReader(f);
                            BufferedReader bfr = new BufferedReader(fr);
                            String line = bfr.readLine();
                            usernameStatus = 1;
                            while (line != null) {
                                accountList.add(line);
                                line = bfr.readLine();
                            }
                            bfr.close();

                            File f2 = new File("TeacherPassword.txt");
                            FileReader fr2 = new FileReader(f2);
                            BufferedReader bfr2 = new BufferedReader(fr2);
                            String line2 = bfr2.readLine();

                            while (line2 != null) {
                                passwordList.add(line2);
                                line2 = bfr2.readLine();
                            }
                            bfr2.close();

                            int l = 0;
                            while (l == 0) {
                                if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {
                                    writer.write("success");
                                    writer.println();
                                    writer.flush();
                                    writer.write(OldUsername);
                                    writer.println();
                                    writer.flush();

                                    int choice = 0;
                                    while (choice != 8) {
                                        try {
                                            choice = Integer.parseInt(reader.readLine());
                                        } catch (IOException e) {
                                            break;
                                        }
                                        if (choice == 1) {
                                            createCourse(reader, writer);
                                        } else if (choice == 2) {
                                            deleteCourse(reader, writer);
                                        } else if (choice == 3) {
                                            createQuiz(reader, writer);
                                        } else if (choice == 4) {
                                            editQuiz(reader, writer);
                                        } else if (choice == 5) {
                                            deleteQuiz(reader, writer);
                                        } else if (choice == 6) {
                                            viewSubmission(reader, writer);
                                        } else if (choice == 7) {
                                            gradeSubmission(reader, writer);
                                        }
                                    }
                                    u++;
                                    break;
                                } else {
                                    writer.write("fail");
                                    writer.println();
                                    writer.flush();
                                    l++;
                                    k = 0;
                                }
                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else if (type.equals("student")) {//student logging in
                    try {
                        int u = 0;
                        while (u == 0) {
                            while (k == 0) {
                                OldUsername = reader.readLine();
                                System.out.println(OldUsername);
                                if (OldUsername == null) {
                                    break;
                                }
                                int status = checkStudentUsername(OldUsername);
                                if (status == 2) {
                                    writer.write("success");
                                    writer.println();
                                    writer.flush();
                                    k++;
                                } else {
                                    writer.write("fail");
                                    writer.println();
                                    writer.flush();
                                }
                            }

                            ArrayList<String> accountList = new ArrayList<>();
                            ArrayList<String> passwordList = new ArrayList<>();

                            String OldPassword = reader.readLine();
                            if (OldPassword == null) {
                                break;
                            }

                            File f = new File("StudentAccount.txt");
                            FileReader fr = new FileReader(f);
                            BufferedReader bfr = new BufferedReader(fr);
                            String line = bfr.readLine();
                            usernameStatus = 1;
                            while (line != null) {
                                accountList.add(line);
                                line = bfr.readLine();
                            }
                            bfr.close();

                            File f2 = new File("StudentPassword.txt");
                            FileReader fr2 = new FileReader(f2);
                            BufferedReader bfr2 = new BufferedReader(fr2);
                            String line2 = bfr2.readLine();

                            while (line2 != null) {
                                passwordList.add(line2);
                                line2 = bfr2.readLine();
                            }
                            bfr2.close();

                            int l = 0;
                            while (l == 0) {
                                if (passwordList.get(accountList.indexOf(OldUsername)).equals(OldPassword)) {
                                    writer.write("success");
                                    writer.println();
                                    writer.flush();
                                    writer.write(OldUsername);
                                    writer.println();
                                    writer.flush();

                                    int looping = 0;
                                    String teacher = null;
                                    String username = reader.readLine();
                                    String newLine = null;
                                    while (looping == 0) {
                                        if (reader.readLine().equals("get teacher")) {
                                            BufferedReader bufferedReader = new BufferedReader(new FileReader("TeacherAccount.txt"));
                                            newLine = "";
                                            ArrayList<String> teacherFileCopy = new ArrayList<>();
                                            int n = 0;
                                            while ((newLine = bufferedReader.readLine()) != null) {
                                                teacherFileCopy.add(newLine);
                                                n++;
                                            }
                                            bufferedReader.close();
                                            if (n == 0) {
                                                writer.write("0");
                                                writer.println();
                                                writer.flush();
                                            } else {
                                                String nString = String.valueOf(n);
                                                writer.write(nString);
                                                writer.println();
                                                writer.flush();
                                                for (int j = 0; j < teacherFileCopy.size(); j++) {
                                                    writer.write(teacherFileCopy.get(j));
                                                    writer.println();
                                                    writer.flush();
                                                }

                                                String checkLine = "";
                                                teacher = reader.readLine();
                                                try {
                                                    BufferedReader bufferedReader2 = new BufferedReader(new FileReader(teacher + "_Courses.txt"));
                                                    checkLine = bufferedReader2.readLine();
                                                    bufferedReader2.close();
                                                } catch (Exception e) {
                                                    writer.write("0");
                                                    writer.println();
                                                    writer.flush();
                                                }
                                                if (checkLine == null && new File(teacher + "_Courses.txt").exists()) {
                                                    writer.write("0");
                                                    writer.println();
                                                    writer.flush();
                                                } else if (checkLine != null && new File(teacher + "_Courses.txt").exists()) {
                                                    ArrayList<String> coursesList = new ArrayList<>();
                                                    BufferedReader bufferedReader1 = new BufferedReader(new FileReader(teacher + "_Courses.txt"));
                                                    int coursesCounter = 0;
                                                    while ((newLine = bufferedReader1.readLine()) != null) {
                                                        coursesList.add(newLine);
                                                        coursesCounter++;
                                                    }
                                                    bufferedReader1.close();
                                                    String coursesAmount = String.valueOf(coursesCounter);
                                                    writer.write(coursesAmount);
                                                    writer.println();
                                                    writer.flush();
                                                    for (int j = 0; j < coursesList.size(); j++) {
                                                        writer.write(coursesList.get(j));
                                                        writer.println();
                                                        writer.flush();
                                                    }

                                                    String courseSelection = reader.readLine(); //retrieves course name from client
                                                    checkLine = "";
                                                    try {
                                                        BufferedReader bufferedReader3 = new BufferedReader(new FileReader(teacher + "_" + courseSelection + ".txt"));
                                                        checkLine = bufferedReader3.readLine();
                                                        bufferedReader3.close();
                                                    } catch (Exception e) {
                                                        writer.write("0"); //tells client theres an error
                                                        writer.println();
                                                        writer.flush();
                                                    }
                                                    if (checkLine == null && new File(teacher + "_" + courseSelection + ".txt").exists()) {
                                                        writer.write("0"); //tells client theres an error
                                                        writer.println();
                                                        writer.flush();
                                                    } else if (checkLine != null && new File(teacher + "_" + courseSelection + ".txt").exists()) {
                                                        ArrayList<String> quizList = new ArrayList<>();
                                                        BufferedReader bufferedReader4 = new BufferedReader(new FileReader(teacher + "_" + courseSelection + ".txt"));
                                                        int quizzesCounter = 0;
                                                        String newLine3 = "";
                                                        while ((newLine3 = bufferedReader4.readLine()) != null) {
                                                            quizList.add(newLine3);
                                                            quizzesCounter++;
                                                        }
                                                        bufferedReader4.close();
                                                        String quizAmount = String.valueOf(quizzesCounter);
                                                        writer.write(quizAmount); //writes size of list and list of qizzes
                                                        writer.println();
                                                        writer.flush();

                                                        for (int j = 0; j < quizList.size(); j++) {
                                                            writer.write(quizList.get(j));
                                                            writer.println();
                                                            writer.flush();
                                                        }
                                                        String quizSelection = reader.readLine();
                                                        String check = null;
                                                        try {
                                                            BufferedReader bufferedReader5 = new BufferedReader(new FileReader(teacher + "_" + courseSelection + "_" + quizSelection + ".txt"));
                                                            check = bufferedReader5.readLine();
                                                            bufferedReader5.close();
                                                        } catch (Exception e) {
                                                            writer.write("Empty Quiz"); //tells client theres an error
                                                            writer.println();
                                                            writer.flush();
                                                        }

                                                        if (check == null && new File(teacher + "_" + courseSelection + "_" + quizSelection + ".txt").exists()) {
                                                            writer.write("Empty Quiz"); //tells client theres an error
                                                            writer.println();
                                                            writer.flush();
                                                        } else if (check != null && new File(teacher + "_" + courseSelection + "_" + quizSelection + ".txt").exists()) {
                                                            writer.write("Quiz");
                                                            writer.println();
                                                            writer.flush();
                                                            BufferedReader bufferedReader6 = new BufferedReader(new FileReader(teacher + "_" + courseSelection + "_" + quizSelection + ".txt"));
                                                            String shuffleStatus = bufferedReader6.readLine();
                                                            if (shuffleStatus.equals("True")) {
                                                                //MainServer mainServer = new MainServer();
                                                                shuffle(teacher + "_" + courseSelection + "_" + quizSelection + ".txt");
                                                            }
                                                            if (!new File(username + "_" + teacher + "_"
                                                                    + courseSelection + "_" + quizSelection + ".txt").exists()) {
                                                                writer.write("run quiz");
                                                                writer.println();
                                                                writer.flush();

                                                                int questionCounter = 0;
                                                                String nextLine = "";
                                                                ArrayList<String> quizContents = new ArrayList<>();
                                                                while ((nextLine = bufferedReader6.readLine()) != null) {
                                                                    quizContents.add(nextLine);
                                                                    questionCounter++;
                                                                }
                                                                questionCounter = questionCounter / 5;
                                                                String questionAmount = String.valueOf(questionCounter);
                                                                writer.write(questionAmount);
                                                                writer.println();
                                                                writer.flush();
                                                                writer.write(shuffleStatus);
                                                                writer.println();
                                                                writer.flush();

                                                                for (int j = 0; j < quizContents.size(); j++) {
                                                                    writer.write(quizContents.get(j));
                                                                    writer.println();
                                                                    writer.flush();
                                                                }
                                                                int quizSize = Integer.parseInt(reader.readLine());
                                                                String completeQuizString = "";
                                                                for (int j = 0; j < quizSize; j++) {
                                                                    completeQuizString = completeQuizString + reader.readLine() + "\n";
                                                                }
                                                                completeQuizString = completeQuizString.substring(0, completeQuizString.length() - 1);
                                                                File files = new File((username + "_" + teacher + "_" + courseSelection + "_" + quizSelection + ".txt"));
                                                                BufferedWriter bw = new BufferedWriter(new FileWriter(files));
                                                                bw.write(completeQuizString);
                                                                bw.close();


                                                            } else {
                                                                writer.write("dont run");
                                                                writer.println();
                                                                writer.flush();

                                                                int viewGrade = Integer.parseInt(reader.readLine());

                                                                if (viewGrade == JOptionPane.YES_OPTION) {
                                                                    File fileGrade = new File(username + "_" + teacher +
                                                                            "_" + courseSelection + "_" + quizSelection + ".txt");
                                                                    BufferedReader brquiz = new BufferedReader(new FileReader(fileGrade));
                                                                    String quizLine = brquiz.readLine();
                                                                    int cline = 0;
                                                                    while (quizLine != null) {
                                                                        cline++;
                                                                        quizLine = brquiz.readLine(); //reads through all line
                                                                    }
                                                                    BufferedReader brquiz2 = new BufferedReader(new FileReader(fileGrade));
                                                                    //reads the same quiz with new bfr
                                                                    BufferedReader gradeSetter = new BufferedReader(new FileReader(fileGrade));
                                                                    String lastLine = "";
                                                                    String grade = "";
                                                                    for (int j = 0; j < cline; j++) {
                                                                        lastLine = brquiz2.readLine();
                                                                    }
                                                                    brquiz.close();
                                                                    String nextLine1 = "";
                                                                    nextLine1 = gradeSetter.readLine();
                                                                    while (!(nextLine1).equals(username)) {
                                                                        nextLine1 = gradeSetter.readLine();
                                                                    }


                                                                    if (lastLine.endsWith("%")) {
                                                                        ArrayList<String> gradeList1 = new ArrayList<>();
                                                                        ArrayList<String> gradeList = new ArrayList<>();
                                                                        while (nextLine1 != null) {
                                                                            gradeList1.add(nextLine1);
                                                                            nextLine1 = gradeSetter.readLine();
                                                                        }
                                                                        gradeSetter.close();
                                                                        for (int j = 1; j < gradeList1.size() - 2; j++) {
                                                                            String problem = gradeList1.get(j);
                                                                            problem = problem.substring(2);
                                                                            if (problem.equalsIgnoreCase("correct")) {
                                                                                problem = (j) + ". " + problem + " 1/1";
                                                                            } else {
                                                                                problem = (j) + ". " + problem + " 0/1";
                                                                            }
                                                                            gradeList.add(problem);
                                                                        }
                                                                        gradeList.add(gradeList1.get(gradeList1.size() - 1));
                                                                        String size = String.valueOf(gradeList.size());
                                                                        writer.write(size);
                                                                        writer.println();
                                                                        writer.flush();
                                                                        for (int j = 0; j < gradeList.size(); j++) {
                                                                            System.out.println(gradeList.get(j));
                                                                            writer.write(gradeList.get(j));
                                                                            writer.println();
                                                                            writer.flush();
                                                                        }
                                                                    } else {
                                                                        grade = "Grade not yet entered";
                                                                        writer.write("0");
                                                                        writer.println();
                                                                        writer.flush();
                                                                        writer.write(grade);
                                                                        writer.println();
                                                                        writer.flush();
                                                                    }
                                                                }
                                                            }
                                                        }

                                                    }

                                                }

                                            }
                                        }
                                    } //end
                                    u++;
                                    break;
                                } else {
                                    writer.write("fail");
                                    writer.println();
                                    writer.flush();
                                    l++;
                                    k = 0;
                                }
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (IOException e) {
            //e.printStackTrace();
        }
    }

    private static void checkFile() {
        File tAccount = new File("TeacherAccount.txt");
        try {
            if (tAccount.exists()) {

            } else {
                tAccount.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File tPassword = new File("TeacherPassword.txt");
        try {
            if (tPassword.exists()) {

            } else {
                tPassword.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File sAccount = new File("StudentAccount.txt");
        try {
            if (sAccount.exists()) {

            } else {
                sAccount.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        File sPassword = new File("StudentPassword.txt");
        try {
            if (sPassword.exists()) {

            } else {
                sPassword.createNewFile();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static int editTeacherInformation(String editUsername, String editPassword, String
            newUsername, String newPassword) {
        ArrayList<String> accountList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        int status = 0;
        try {
            File f = new File("TeacherAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                accountList.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            File f2 = new File("TeacherPassword.txt");
            FileReader fr2 = new FileReader(f2);
            BufferedReader bfr2 = new BufferedReader(fr2);
            String line2 = bfr2.readLine();

            while (line2 != null) {
                passwordList.add(line2);
                line2 = bfr2.readLine();
            }
            bfr2.close();

            accountList.set(accountList.indexOf(editUsername), newUsername);
            passwordList.set(passwordList.indexOf(editPassword), newPassword);

            File teacherAccountFile = new File("TeacherAccount.txt");
            FileOutputStream fos = new FileOutputStream(teacherAccountFile,
                    false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < accountList.size(); i++) {
                pw.println(accountList.get(i));
            }
            pw.close();

            File teacherPasswordFile = new File("TeacherPassword.txt");
            FileOutputStream fos2 = new FileOutputStream(teacherPasswordFile,
                    false);
            PrintWriter pw2 = new PrintWriter(fos2);
            for (int i = 0; i < accountList.size(); i++) {
                pw2.println(passwordList.get(i));
            }
            pw2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }


    public static int editStudentInformation(String editUsername, String editPassword, String
            newUsername, String newPassword) {
        ArrayList<String> accountList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        int status = 0;
        try {
            File f = new File("StudentAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                accountList.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            File f2 = new File("StudentPassword.txt");
            FileReader fr2 = new FileReader(f2);
            BufferedReader bfr2 = new BufferedReader(fr2);
            String line2 = bfr2.readLine();

            while (line2 != null) {
                passwordList.add(line2);
                line2 = bfr2.readLine();
            }
            bfr2.close();

            accountList.set(accountList.indexOf(editUsername), newUsername);
            passwordList.set(passwordList.indexOf(editPassword), newPassword);

            File studentAccountFile = new File("StudentAccount.txt");
            FileOutputStream fos = new FileOutputStream(studentAccountFile,
                    false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < accountList.size(); i++) {
                pw.println(accountList.get(i));
            }
            pw.close();

            File studentPasswordFile = new File("StudentPassword.txt");
            FileOutputStream fos2 = new FileOutputStream(studentPasswordFile,
                    false);
            PrintWriter pw2 = new PrintWriter(fos2);
            for (int i = 0; i < accountList.size(); i++) {
                pw2.println(passwordList.get(i));
            }
            pw2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static int deleteTeacherInformation(String username1, String password1) {
        ArrayList<String> accountList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        int status = 0;
        try {
            File f = new File("TeacherAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                accountList.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            File f2 = new File("TeacherPassword.txt");
            FileReader fr2 = new FileReader(f2);
            BufferedReader bfr2 = new BufferedReader(fr2);
            String line2 = bfr2.readLine();

            while (line2 != null) {
                passwordList.add(line2);
                line2 = bfr2.readLine();
            }
            bfr2.close();

            accountList.remove(username1);
            passwordList.remove(password1);

            File teacherAccountFile = new File("TeacherAccount.txt");
            FileOutputStream fos = new FileOutputStream(teacherAccountFile,
                    false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < accountList.size(); i++) {
                pw.println(accountList.get(i));
            }
            pw.close();

            File teacherPasswordFile = new File("TeacherPassword.txt");
            FileOutputStream fos2 = new FileOutputStream(teacherPasswordFile,
                    false);
            PrintWriter pw2 = new PrintWriter(fos2);
            for (int i = 0; i < accountList.size(); i++) {
                pw2.println(passwordList.get(i));
            }
            pw2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }

    public static int deleteStudentInformation(String username1, String password1) {
        ArrayList<String> accountList = new ArrayList<>();
        ArrayList<String> passwordList = new ArrayList<>();
        int status = 0;
        try {
            File f = new File("StudentAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            while (line != null) {
                accountList.add(line);
                line = bfr.readLine();
            }
            bfr.close();

            File f2 = new File("StudentPassword.txt");
            FileReader fr2 = new FileReader(f2);
            BufferedReader bfr2 = new BufferedReader(fr2);
            String line2 = bfr2.readLine();

            while (line2 != null) {
                passwordList.add(line2);
                line2 = bfr2.readLine();
            }
            bfr2.close();

            accountList.remove(username1);
            passwordList.remove(password1);

            File studentAccountFile = new File("StudentAccount.txt");
            FileOutputStream fos = new FileOutputStream(studentAccountFile,
                    false);
            PrintWriter pw = new PrintWriter(fos);
            for (int i = 0; i < accountList.size(); i++) {
                pw.println(accountList.get(i));
            }
            pw.close();

            File studentPasswordFile = new File("StudentPassword.txt");
            FileOutputStream fos2 = new FileOutputStream(studentPasswordFile,
                    false);
            PrintWriter pw2 = new PrintWriter(fos2);
            for (int i = 0; i < accountList.size(); i++) {
                pw2.println(passwordList.get(i));
            }
            pw2.close();


        } catch (Exception e) {
            e.printStackTrace();
        }
        return status;
    }


    private static void createCourse(BufferedReader newReader, PrintWriter newWriter) {
        try {
            String courseName = newReader.readLine();
            String teacherName = newReader.readLine();
            if (courseName.equals("")) {
                return;
            }
            File f1 = new File(teacherName + "_Courses.txt");  //The courses file for the teacher
            PrintWriter pw = new PrintWriter(new FileOutputStream(f1, true));   //Printer writer for the f1
            File f2 = new File(teacherName + "_" + courseName + ".txt");   //File that holds the quizzes for the
            //given course
            f2.createNewFile();
            pw.println(courseName);
            pw.close();
        } catch (IOException e) {

        }
    }

    private static void deleteCourse(BufferedReader newReader, PrintWriter newWriter) {

        try {
            String teacherName = newReader.readLine();
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            String line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseDeleted = newReader.readLine();
            if (courseDeleted.equals("")) {
                return;
            }
            File f = new File(teacherName + "_Courses.txt");   //File that holds the courses for the teacher
            ArrayList<String> list = new ArrayList<>();
            try {
                BufferedReader bfr = new BufferedReader(new FileReader(f));  //Buffered reader for the courses file
                line = bfr.readLine();   //Reads the line in the reader
                while (line != null) {
                    if (!line.equals(courseDeleted)) {
                        list.add(line);
                        line = bfr.readLine();
                    } else {
                        line = bfr.readLine();
                    }
                }
                BufferedReader bfr2 = new BufferedReader(new FileReader(teacherName + "_" + courseDeleted + ".txt"));
                line = bfr2.readLine();
                while (line != null) {
                    File quizFile = new File(teacherName + "_" + courseDeleted + "_" + line + ".txt");
                    quizFile.delete();
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
            File f3 = new File(teacherName + "_" + courseDeleted + ".txt");  //File that holds the quizzes for the course
            f3.delete();

        } catch (IOException e) {

        }
    }

    private static void createQuiz(BufferedReader newReader, PrintWriter newWriter) {
        ArrayList<String> questions = new ArrayList<>();
        ArrayList<String> answersA = new ArrayList<>();
        ArrayList<String> answersB = new ArrayList<>();
        ArrayList<String> answersC = new ArrayList<>();
        ArrayList<String> answersD = new ArrayList<>();
        PrintWriter pw3 = null; //Print writer that writes the quiz name into the file that holds the quizzes
        PrintWriter pw = null;
        try {
            String teacherName = newReader.readLine();
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            String line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseSelected = newReader.readLine();
            if (courseSelected.equals("")) {
                return;
            }
            String quizName = newReader.readLine();
            if (quizName.equals("")) {
                return;
            }
            int option = Integer.parseInt(newReader.readLine());
            int randomChoice;
            if (option == 1) {
                randomChoice = Integer.parseInt(newReader.readLine());
                String addQuestion = newReader.readLine();
                while (addQuestion.equals("Yes")) {
                    String cancelled = newReader.readLine();
                    if (!cancelled.equals("")) {
                        String question = newReader.readLine();
                        String answerA = newReader.readLine();
                        String answerB = newReader.readLine();
                        String answerC = newReader.readLine();
                        String answerD = newReader.readLine();
                        questions.add(question);
                        answersA.add(answerA);
                        answersB.add(answerB);
                        answersC.add(answerC);
                        answersD.add(answerD);
                        addQuestion = newReader.readLine();
                    } else {
                        return;
                    }
                }

                File courseQuiz = new File(teacherName + "_" + courseSelected + ".txt");   //The file that holds the quizzes
                try {
                    pw3 = new PrintWriter(new FileOutputStream(courseQuiz, true));
                    pw3.println(quizName);
                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }
                try {
                    File f = new File(teacherName + "_" + courseSelected + "_" + quizName + ".txt");   //File for the quiz
                    pw = new PrintWriter(new FileOutputStream(f));


                } catch (FileNotFoundException e) {
                    e.printStackTrace();
                }

                if (randomChoice == 1) {
                    pw.println("False");
                } else {
                    pw.println("True");
                }

                for (int i = 0; i < answersA.size(); i++) {
                    pw.println(questions.get(i));
                    pw.println("A. " + answersA.get(i));
                    pw.println("B. " + answersB.get(i));
                    pw.println("C. " + answersC.get(i));
                    pw.println("D. " + answersD.get(i));
                }
            } else if (option == 0) {
                ArrayList<String> importList = new ArrayList<>();
                String go = newReader.readLine();
                if (!go.equals("")) {
                    line = newReader.readLine();
                    while (!line.equals("///")) {
                        importList.add(line);
                        line = newReader.readLine();

                    }

                    File f = new File(teacherName + "_" + courseSelected + "_" + quizName + ".txt");
                    File f2 = new File(teacherName + "_" + courseSelected + ".txt");
                    pw = new PrintWriter(new FileOutputStream(f));
                    pw3 = new PrintWriter(new FileOutputStream(f2, true));
                    pw3.println(teacherName + "_" + courseSelected + "_" + quizName + ".txt");

                    for (int i = 0; i < importList.size(); i++) {
                        pw.println(importList.get(i));
                    }
                } else {
                    return;
                }

            } else {
                return;
            }
            pw.close();
            pw3.close();

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void editQuiz(BufferedReader newReader, PrintWriter newWriter) {
        try {
            String teacherName = newReader.readLine();
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            String line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseSelected = newReader.readLine();
            if (courseSelected.equals("")) {
                return;
            }

            BufferedReader fileReader2 = new BufferedReader(new FileReader(teacherName + "_" + courseSelected + ".txt"));
            line = fileReader2.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader2.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader2.close();
            String quizSelected = newReader.readLine();
            if (quizSelected.equals("")) {
                return;
            }
            String continueEdit = newReader.readLine();
            PrintWriter pw = null;
            BufferedReader quizReader = null;
            while (!continueEdit.equals("///")) {
                File quizFile = new File(teacherName + "_" + courseSelected + "_" + quizSelected + ".txt");
                quizReader = new BufferedReader(new FileReader(quizFile));
                line = quizReader.readLine();
                ArrayList<String> quizList = new ArrayList<>();
                while (line != null) {
                    quizList.add(line);
                    newWriter.write(line);
                    newWriter.println();
                    newWriter.flush();
                    line = quizReader.readLine();
                }
                newWriter.write("///");
                newWriter.println();
                newWriter.flush();


                String newLineText = newReader.readLine();
                if (newLineText.equals("///")) {
                    continueEdit = "///";
                } else {
                    int index = Integer.parseInt(newReader.readLine());
                    quizList.set(index, newLineText);

                    pw = new PrintWriter(new FileOutputStream(quizFile));
                    for (int i = 0; i < quizList.size(); i++) {
                        pw.println(quizList.get(i));
                    }
                    continueEdit = newReader.readLine();
                    quizReader.close();
                    pw.close();
                }
            }

        } catch (IOException e) {

        }
    }

    private static void deleteQuiz(BufferedReader newReader, PrintWriter newWriter) {
        try {
            String teacherName = newReader.readLine();
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            String line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseSelected = newReader.readLine();
            if (courseSelected.equals("")) {
                return;
            }

            BufferedReader fileReader2 = new BufferedReader(new FileReader(teacherName + "_" + courseSelected + ".txt"));
            line = fileReader2.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader2.readLine();
            }

            newWriter.write("///");
            newWriter.println();
            newWriter.flush();
            fileReader2.close();
            String quizSelected = newReader.readLine();
            if (quizSelected.equals("")) {
                return;
            }
            File f = new File(teacherName + "_" + courseSelected + "_" + quizSelected + ".txt");
            File f2 = new File(teacherName + "_" + courseSelected + ".txt");
            f.delete();
            BufferedReader bfr = new BufferedReader(new FileReader(f2));
            ArrayList<String> courseList = new ArrayList<>();
            line = bfr.readLine();
            while (line != null) {
                if (!line.equals(quizSelected)) {
                    courseList.add(line);
                    line = bfr.readLine();
                } else {
                    line = bfr.readLine();
                }
            }

            PrintWriter pw = new PrintWriter(new FileOutputStream(f2));
            for (int i = 0; i < courseList.size(); i++) {
                pw.println(courseList.get(i));

            }
            pw.close();
            bfr.close();


        } catch (IOException e) {

        }
    }

    private static void viewSubmission(BufferedReader newReader, PrintWriter newWriter) {
        ArrayList<String> studentNames = new ArrayList<>(); //List that holds the current students names
        File studentFile = new File("StudentAccount.txt");  //The file that holds the student names
        try {
            String teacherName = newReader.readLine();

            BufferedReader bfr = new BufferedReader(new FileReader(studentFile));   //Buffered reader for student file
            String line = bfr.readLine();   //Reads the next line in the buffered reader
            while (line != null) {
                studentNames.add(line);

                newWriter.write(line);
                newWriter.println();
                newWriter.flush();

                line = bfr.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();

            String studentSelected = newReader.readLine();
            if (studentSelected.equals("")) {
                return;
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseSelected = newReader.readLine();
            if (courseSelected.equals("")) {
                return;
            }
            BufferedReader fileReader2 = new BufferedReader(new FileReader(teacherName + "_" + courseSelected + ".txt"));
            line = fileReader2.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader2.readLine();
            }

            newWriter.write("///");
            newWriter.println();
            newWriter.flush();
            fileReader2.close();

            String quizSelected = newReader.readLine();
            if (quizSelected.equals("")) {
                return;
            }
            File f = new File(studentSelected + "_" + teacherName + "_" + courseSelected + "_" + quizSelected + ".txt");
            if (f.exists()) {
                newWriter.write("Yes");
                newWriter.println();
                newWriter.flush();
            } else {
                newWriter.write("No");
                newWriter.println();
                newWriter.flush();
                return;
            }

            BufferedReader studentReader = new BufferedReader(new FileReader(f));
            line = studentReader.readLine();
            ArrayList<String> quizList = new ArrayList<>();
            while (line != null) {
                quizList.add(line);
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = studentReader.readLine();
            }
            newWriter.write("///");
            newWriter.println();
            newWriter.flush();

            String done = newReader.readLine();
            if (done.equals("")) {
                return;
            }
        } catch (IOException e) {

        }
    }

    private static void gradeSubmission(BufferedReader newReader, PrintWriter newWriter) {
        ArrayList<String> studentNames = new ArrayList<>(); //List that holds the current students names
        File studentFile = new File("StudentAccount.txt");  //The file that holds the student names
        try {
            String teacherName = newReader.readLine();

            BufferedReader bfr = new BufferedReader(new FileReader(studentFile));   //Buffered reader for student file
            String line = bfr.readLine();   //Reads the next line in the buffered reader
            while (line != null) {
                studentNames.add(line);

                newWriter.write(line);
                newWriter.println();
                newWriter.flush();

                line = bfr.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();

            String studentSelected = newReader.readLine();
            if (studentSelected.equals("")) {
                return;
            }
            BufferedReader fileReader = new BufferedReader(new FileReader(teacherName + "_Courses.txt"));
            line = fileReader.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader.readLine();
            }
            newWriter.write("");
            newWriter.println();
            newWriter.flush();
            fileReader.close();

            String courseSelected = newReader.readLine();
            if (courseSelected.equals("")) {
                return;
            }
            BufferedReader fileReader2 = new BufferedReader(new FileReader(teacherName + "_" + courseSelected + ".txt"));
            line = fileReader2.readLine();
            while (line != null) {
                newWriter.write(line);
                newWriter.println();
                newWriter.flush();
                line = fileReader2.readLine();
            }

            newWriter.write("///");
            newWriter.println();
            newWriter.flush();
            fileReader2.close();

            String quizSelected = newReader.readLine();
            if (quizSelected.equals("")) {
                return;
            }
            File f = new File(studentSelected + "_" + teacherName + "_" + courseSelected + "_" + quizSelected + ".txt");
            if (f.exists()) {
                newWriter.write("Yes");
                newWriter.println();
                newWriter.flush();
                BufferedReader bfr2 = new BufferedReader(new FileReader(f));
                line = bfr2.readLine();
                if (line.equals("True") || line.equals("False")) {
                    newWriter.write("Not");
                    newWriter.println();
                    newWriter.flush();
                } else {
                    newWriter.write("Graded");
                    newWriter.println();
                    newWriter.flush();
                    return;
                }
            } else {
                newWriter.write("No");
                newWriter.println();
                newWriter.flush();
                return;
            }

            BufferedReader bfrQuestions = new BufferedReader(new FileReader(f));  //Buffered reader for
            //the student quiz and reading the questions
            BufferedReader bfrAnswers = new BufferedReader(new FileReader(f));    //Buffered reader for
            //the student quiz and reading the answer
            int numCorrect = 0;
            ArrayList<String> entireFile = new ArrayList<>();   //List to hold the files entire contents
            ArrayList<String> correct = new ArrayList<>();  //List to hold the teachers yes or no answers
            String questionLine = bfrQuestions.readLine();  //Reads the next line in the question buffered reader
            if (questionLine.equals("True") || questionLine.equals("False")) {
                questionLine = bfrQuestions.readLine();
                String answerLine = bfrAnswers.readLine();  //Reads the next line in the answer buffered reader
                while (!answerLine.equals("A") && !answerLine.equals("B") && !answerLine.equals("C") &&
                        !answerLine.equals("D")) {
                    answerLine = bfrAnswers.readLine();
                }
                while (answerLine.equals("A") || answerLine.equals("B") || answerLine.equals("C")
                        || answerLine.equals("D")) {
                    newWriter.write(questionLine);
                    newWriter.println();
                    newWriter.flush();
                    questionLine = bfrQuestions.readLine();
                    newWriter.write(questionLine);
                    newWriter.println();
                    newWriter.flush();
                    questionLine = bfrQuestions.readLine();
                    newWriter.write(questionLine);
                    newWriter.println();
                    newWriter.flush();
                    questionLine = bfrQuestions.readLine();
                    newWriter.write(questionLine);
                    newWriter.println();
                    newWriter.flush();
                    questionLine = bfrQuestions.readLine();
                    newWriter.write(questionLine);
                    newWriter.println();
                    newWriter.flush();

                    newWriter.write("Student Answer: " + answerLine);
                    newWriter.println();
                    newWriter.flush();
                    answerLine = bfrAnswers.readLine();
                    questionLine = bfrQuestions.readLine();
                    newWriter.write("///");
                    newWriter.println();
                    newWriter.flush();

                    String yesOrNo = newReader.readLine();
                    correct.add(yesOrNo);
                    if (yesOrNo.equals("Yes")) {
                        numCorrect++;
                    }

                }
                newWriter.write("///");
                newWriter.println();
                newWriter.flush();

                BufferedReader bfr3 = new BufferedReader(new FileReader(f));  //Buffered reader for the
                //student quiz
                line = bfr3.readLine();  //Reads the next line in the buffered reader
                line = bfr3.readLine();
                while (line != null) {
                    entireFile.add(line);
                    line = bfr3.readLine();
                }
                double percentage = (double) numCorrect / correct.size() * 100;     //Holds the grade percentage
                PrintWriter pw = new PrintWriter(new FileOutputStream(f));    //Print writer for the
                //student quiz
                int j = 0;  //Counter for the answers
                for (int i = 0; i < entireFile.size(); i++) {

                    if (entireFile.get(i).equals("A") || entireFile.get(i).equals("B") ||
                            entireFile.get(i).equals("C") || entireFile.get(i).equals("D")) {
                        if (correct.get(j).equals("Yes")) {
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
            }

        } catch (IOException e) {


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
            if (lineNum == (2 + 5 * mult) && mult <= numq && line != null) {
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

    public static int checkTeacherUsername(String username1) {
        String tUsername;
        try {
            File f = new File("TeacherAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            int usernameStatus = 1;

            while (line != null) { //checks if username already exists
                if (username1.equals(line)) {
                    return 2;
                }
                line = bfr.readLine();
            }
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }

    public static int checkStudentUsername(String username1) {
        String tUsername;
        try {
            File f = new File("StudentAccount.txt");
            FileReader fr = new FileReader(f);
            BufferedReader bfr = new BufferedReader(fr);
            String line = bfr.readLine();
            int usernameStatus = 1;

            while (line != null) { //checks if username already exists
                if (username1.equals(line)) {
                    return 2;
                }
                line = bfr.readLine();
            }
            return 1;

        } catch (Exception e) {
            e.printStackTrace();
        }
        return 1;
    }
}
