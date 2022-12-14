import javax.swing.*;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/**
 * Project 04 -- Run
 * <p>
 * This class prompts user to sign in as teacher or student
 *
 * @author Tim Chou L09
 * @version April 11, 2022
 */

public class Run {
    //Initiates string prompts
    final static String WELCOMEPROMPT = "\nPlease choose a login type";

    /*
    method that runs the quiz and takes the scanner as a parameter
     */


    public static String runQuiz(Socket socket) {

        int w = 0;
        try {
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            //import reader and writer
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            int usernameStatus;
            int welcome = 0;
            int account;
            int[] loginType = new int[1];
            int[] accountChoice = new int[1];
            int[] login = new int[1];


            String username1 = null; //initiates username of current user
            int welcomeChoice = 0;
            while (welcome == 0) {
                welcomeChoice = showWelcomeMessageDialog();
                try {
                    if (welcomeChoice == 10) {
                        return "end"; // end program
                    } else {
                        welcome++; //breaks out of loop
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
            if (welcomeChoice == 1) { // teacher
                while (login[0] == 0) {
                    account = 0;
                    usernameStatus = 0;
                    int proceed = 0;
                    while (account == 0) {
                        proceed = accountPrompt();
                        try {
                            if (proceed == 5) {
                                return "end"; //end program
                            } else if (proceed != 1 && proceed != 2 && proceed != 3 && proceed != 4) {
                                //throws error if account choice is invalid option
                                break;
                            } else {
                                account++; //breaks out of loop
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                    if (proceed == 1) { // teacher creating account
                        int i = 0;
                        writer.write("create");
                        writer.println();
                        writer.flush();
                        writer.write("teacher");
                        writer.println();
                        writer.flush();
                        while (i == 0) {

                            username1 = makeTeacherUsername(); // checking teacher username
                            if (username1.equals("fail")) {
                                return "end"; //end program
                            }
                            writer.write(username1);
                            writer.println();
                            writer.flush();

                            String status = reader.readLine(); // checking teacher username
                            if (status.equals("success")) {
                                i++;
                            } else {
                                usernameExist();
                            }
                        }
                        try {
                            String teacherPassword = makePassword(); // checking teacher password
                            if (teacherPassword.equals("fail")) {
                                return "end"; //end program
                            }

                            writer.write(teacherPassword);
                            writer.println();
                            writer.flush();
                            return "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (proceed == 2) { // teacher wants to log in to existing account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("login");
                                writer.println();
                                writer.flush();
                                writer.write("teacher");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String teacherLoginUsername = checkTeacherOldName();
                                        // checking teacher username
                                        if (teacherLoginUsername.equals("fail")) {
                                            return "end"; //end program
                                        }
                                        writer.write(teacherLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking teacher username
                                            i++;
                                        } else if (status.equals("fail")) {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    String teacherLoginPassword = checkTeacherOldPassword();
                                    // checking teacher password
                                    if (teacherLoginPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(teacherLoginPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine();
                                    if (status.equals("success")) { // checking teacher information matches record
                                        //run teacher
                                        return "Teacher " + reader.readLine();
                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (proceed == 3) { //if teacher wants to edit account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("edit");
                                writer.println();
                                writer.flush();
                                writer.write("teacher");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String teacherLoginUsername = checkTeacherOldName();
                                        // checking teacher username
                                        if (teacherLoginUsername.equals("fail")) {
                                            return "end"; //end program
                                        }
                                        writer.write(teacherLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking teacher username
                                            i++;
                                        } else if (status.equals("fail")) {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    editPassword = checkTeacherOldPassword(); // checking teacher password
                                    if (editPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(editPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine();
                                    if (status.equals("success")) {
                                        i = 0;
                                        while (i == 0) {
                                            String teacherUsername = makeTeacherUsername();
                                            // ask for new teacher username
                                            if (teacherUsername.equals("fail")) {
                                                return "end"; //end program
                                            }
                                            writer.write(teacherUsername);
                                            writer.println();
                                            writer.flush();

                                            status = reader.readLine();
                                            if (status.equals("success")) { // checking teacher username
                                                i++;
                                            } else {
                                                usernameExist();
                                            }
                                        }

                                        try {
                                            String teacherPassword = makePassword();// checking teacher password
                                            if (teacherPassword.equals("fail")) {
                                                return "end"; //end program
                                            }
                                            writer.write(teacherPassword);
                                            writer.println();
                                            writer.flush();
                                            return "";
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (proceed == 4) { // teacher deleting account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("delete");
                                writer.println();
                                writer.flush();
                                writer.write("teacher");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String teacherLoginUsername = checkTeacherOldName();
                                        // checking teacher username
                                        if (teacherLoginUsername.equals("fail")) {
                                            return "end"; //end program
                                        }
                                        writer.write(teacherLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking teacher username
                                            i++;
                                        } else if (status.equals("fail")) {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    String teacherLoginPassword = checkTeacherOldPassword();
                                    // checking teacher password
                                    if (teacherLoginPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(teacherLoginPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine(); // checking teacher password
                                    if (status.equals("success")) {
                                        return "";

                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                writer.close();
                reader.close();
            } else if (welcomeChoice == 2) { //student
                while (login[0] == 0) {
                    account = 0;
                    usernameStatus = 0;
                    int proceed = 0;
                    while (account == 0) {
                        proceed = accountPrompt();
                        try {
                            if (proceed == 5) {
                                return "end"; //end program
                            }
                            if (proceed != 1 && proceed != 2 && proceed != 3 && proceed != 4) {
                                //throws error if account choice is invalid option
                                break;
                            } else {
                                account++; //breaks out of loop
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    if (proceed == 1) { // student creating account
                        int i = 0;
                        writer.write("create");
                        writer.println();
                        writer.flush();
                        writer.write("student");
                        writer.println();
                        writer.flush();
                        while (i == 0) {

                            username1 = makeStudentUsername();  // checking student username
                            if (username1.equals("fail")) {
                                return "end"; //end program
                            }
                            writer.write(username1);
                            writer.println();
                            writer.flush();

                            String status = reader.readLine(); // checking student username
                            if (status.equals("success")) {
                                i++;
                            } else {
                                usernameExist();
                            }
                        }
                        try {
                            String studentPassword = makePassword(); // checking student password
                            if (studentPassword.equals("fail")) {
                                return "end"; //end program
                            }

                            writer.write(studentPassword);
                            writer.println();
                            writer.flush();
                            return "";
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } else if (proceed == 2) { // student wants to log in to existing account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("login");
                                writer.println();
                                writer.flush();
                                writer.write("student");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String studentLoginUsername = checkStudentOldName();
                                        // checking student username
                                        if (studentLoginUsername.equals("fail")) {
                                            return "end"; //end program
                                        }
                                        writer.write(studentLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking student username
                                            i++;
                                        } else {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    String studentLoginPassword = checkStudentOldPassword();
                                    // checking student username
                                    if (studentLoginPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(studentLoginPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine();
                                    if (status.equals("success")) { // checking student information matches record
                                        //run student
                                        return "Student " + reader.readLine();
                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    } else if (proceed == 3) { //if student wants to edit account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("edit");
                                writer.println();
                                writer.flush();
                                writer.write("student");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String studentLoginUsername = checkStudentOldName();
                                        // checking student username
                                        if (studentLoginUsername.equals("fail")) {
                                            return "end"; //end program
                                        }
                                        writer.write(studentLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking teacher username
                                            i++;
                                        } else if (status.equals("fail")) {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    editPassword = checkStudentOldPassword(); // checking teacher password
                                    if (editPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(editPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine();
                                    if (status.equals("success")) {
                                        i = 0;
                                        while (i == 0) {
                                            String studentUsername = makeStudentUsername();
                                            //  ask for new student username
                                            if (studentUsername.equals("fail")) {
                                                return "end"; //end program
                                            }
                                            writer.write(studentUsername);
                                            writer.println();
                                            writer.flush();

                                            status = reader.readLine();
                                            if (status.equals("success")) { // checking teacher username
                                                i++;
                                            } else {
                                                usernameExist();
                                            }
                                        }


                                        try {
                                            String studentPassword = makePassword(); // checking student password
                                            if (studentPassword.equals("fail")) {
                                                return "end"; //end program
                                            }
                                            writer.write(studentPassword);
                                            writer.println();
                                            writer.flush();
                                            return "";
                                        } catch (Exception e) {
                                            e.printStackTrace();
                                        }

                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }
                                }

                            } catch (Exception e) {
                                e.printStackTrace();

                            }
                        }
                    } else if (proceed == 4) { // student deleting account
                        while (usernameStatus == 0) {
                            try {
                                int i = 0;
                                writer.write("delete");
                                writer.println();
                                writer.flush();
                                writer.write("student");
                                writer.println();
                                writer.flush();
                                int u = 0;
                                while (u == 0) {
                                    while (i == 0) {
                                        String studentLoginUsername = checkStudentOldName();
                                        // checking student username
                                        if (studentLoginUsername.equals("fail")) {
                                            return "end";//end program
                                        }
                                        writer.write(studentLoginUsername);
                                        writer.println();
                                        writer.flush();
                                        String status = reader.readLine();

                                        if (status.equals("success")) { // checking student username
                                            i++;
                                        } else {
                                            usernameDoesNotExist();
                                        }
                                    }

                                    String editPassword = null;

                                    String studentLoginPassword = checkStudentOldPassword();
                                    // checking student password
                                    if (studentLoginPassword.equals("fail")) {
                                        return "end"; //end program
                                    }
                                    writer.write(studentLoginPassword);
                                    writer.println();
                                    writer.flush();

                                    String status = reader.readLine(); // checking teacher password
                                    if (status.equals("success")) {
                                        return "";
                                    } else {
                                        informationMismatch();
                                        i = 0;
                                    }

                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                writer.close();
                reader.close();
            }

        } catch (
                Exception e) {
            e.printStackTrace();
        }

        return "";
    }

    /*
    method that checks the teacher and student account/passwords
     */


    public static int showWelcomeMessageDialog() {

        String[] options = {"Teacher", "Student"};
        int result = 1;
        do {
            result = JOptionPane.showOptionDialog(null, "Welcome!" + WELCOMEPROMPT, "User Type",
                    JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE,
                    null, options, null);
            if (result == JOptionPane.CLOSED_OPTION) {
                return 10;
            } else if (result == JOptionPane.YES_OPTION) {
                return 1;
            } else if (result == JOptionPane.NO_OPTION) {
                return 2;
            }
        } while (result != 0);

        return result;

    }

    public static int accountPrompt() {

        String[] options = {"1. Make a new account", "2. Login with existing account", "3. Edit existing account", "4. Delete existing account"};
        String result = "";
        do {
            result = (String) JOptionPane.showInputDialog(null, "How would you like to proceed?", "Action selection",
                    JOptionPane.QUESTION_MESSAGE, null, options, options[0]);
            if (result == null) {
                return 5;
            }
            if (result.equals("1. Make a new account")) {
                return 1;
            } else if (result.equals("2. Login with existing account")) {
                return 2;
            } else if (result.equals("3. Edit existing account")) {
                return 3;
            } else if (result.equals("4. Delete existing account")) {
                return 4;
            } else {
                return 5;
            }
        } while (result.equals("true"));
    }

    public static String makeTeacherUsername() {
        String userName = null;
        int i = 0;
        while (i == 0) {
            userName = JOptionPane.showInputDialog(null, "Please enter your desired username",
                    "Make username", JOptionPane.QUESTION_MESSAGE);
            //if (Integer.parseInt(userName) == JOptionPane.CLOSED_OPTION || Integer.parseInt(userName) == JOptionPane.CANCEL_OPTION) {
            //  return "exit";
            //}
            if (userName == null) {
                return "fail";
            } else if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else if (userName.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Username cannot contain space!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return userName;
    }

    public static String makeStudentUsername() {
        String userName = null;
        int i = 0;
        while (i == 0) {
            userName = JOptionPane.showInputDialog(null, "Please enter your desired username",
                    "Make username", JOptionPane.QUESTION_MESSAGE);
            //if (Integer.parseInt(userName) == JOptionPane.CLOSED_OPTION || Integer.parseInt(userName) == JOptionPane.CANCEL_OPTION) {
            //  return "exit";
            //}
            if (userName == null) {
                return "fail";
            } else if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else if (userName.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Username cannot contain space!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return userName;
    }

    public static String makePassword() {
        String password = null;
        int i = 0;
        while (i == 0) {
            password = JOptionPane.showInputDialog(null, "Please enter your desired password",
                    "Make username", JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                return "fail";
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Password cannot be empty!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else if (password.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Password cannot contain space!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return password;
    }

    public static String checkTeacherOldName() {
        String userName = null;
        int i = 0;
        while (i == 0) {
            userName = JOptionPane.showInputDialog(null, "Please enter your username",
                    "Verify username", JOptionPane.QUESTION_MESSAGE);
            if (userName == null) {
                return "fail";
            } else if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else if (userName.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Username cannot contain space!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return userName;
    }

    public static String checkTeacherOldPassword() {
        String password = null;
        int i = 0;
        while (i == 0) {
            password = JOptionPane.showInputDialog(null, "Please enter your password",
                    "Verify password", JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                return "fail";
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "password cannot be empty!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else if (password.contains(" ")) {
                JOptionPane.showMessageDialog(null, "password cannot contain space!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return password;
    }

    public static String checkStudentOldName() {
        String userName = null;
        int i = 0;
        while (i == 0) {
            userName = JOptionPane.showInputDialog(null, "Please enter your username",
                    "Verify username", JOptionPane.QUESTION_MESSAGE);
            if (userName == null) {
                return "fail";
            } else if (userName.isEmpty()) {
                JOptionPane.showMessageDialog(null, "Username cannot be empty!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else if (userName.contains(" ")) {
                JOptionPane.showMessageDialog(null, "Username cannot contain space!", "Make username",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return userName;
    }

    public static String checkStudentOldPassword() {
        String password = null;
        int i = 0;
        while (i == 0) {
            password = JOptionPane.showInputDialog(null, "Please enter your password",
                    "Verify password", JOptionPane.QUESTION_MESSAGE);
            if (password == null) {
                return "fail";
            } else if (password.isEmpty()) {
                JOptionPane.showMessageDialog(null, "password cannot be empty!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else if (password.contains(" ")) {
                JOptionPane.showMessageDialog(null, "password cannot contain space!", "Make password",
                        JOptionPane.ERROR_MESSAGE);
            } else {
                i++;
            }
        }
        return password;
    }


    public static void informationMismatch() {
        JOptionPane.showMessageDialog(null, "Your information did not match with our record", "Button Game",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void usernameExist() {
        JOptionPane.showMessageDialog(null, "Username already exist!", "Username error",
                JOptionPane.ERROR_MESSAGE);
    }

    public static void usernameDoesNotExist() {
        JOptionPane.showMessageDialog(null, "Username does not exist!", "password error",
                JOptionPane.ERROR_MESSAGE);
    }

}
