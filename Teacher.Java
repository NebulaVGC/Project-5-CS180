import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;
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
public class Teacher implements Runnable {
    private final String name;      //The teachers name
    private JButton createCourseButton;
    private JButton deleteCourseButton;
    private JButton createQuizButton;
    private JButton editQuizButton;
    private JButton deleteQuizButton;
    private JButton viewSubmissionButton;
    private JButton gradeSubmissionButton;
    private JButton exitButton;

    private JFrame menuFrame;
    private JFrame createCourseFrame;
    private JFrame deleteCourseFrame;
    private JFrame createQuizFrame;
    private JFrame editQuizFrame;
    private JFrame deleteQuizFrame;
    private JFrame viewSubmissionFrame;
    private JFrame gradeSubmissionFrame;

    private Socket socket;
    private BufferedReader reader;
    private PrintWriter writer;

    /**
     * Constructor for Teacher. THe constructor creates a new empty file that is used to hold the courses
     *
     * @param name The name of the teacher
     */
    public Teacher(String name) {
        this.name = name;
    }
    public void run() {
            createMenuGUI(new Socket());

    }

    public static void main(String[] args) {
        Teacher teacher = new Teacher("Joe");
        SwingUtilities.invokeLater(teacher);
    }

    ActionListener actionListener = new ActionListener() {
        @Override
        public void actionPerformed(ActionEvent e) {

            if (e.getSource() == createCourseButton) {
                writer.write("1");
                writer.println();
                writer.flush();
                newCreateCourse();
            }

            if (e.getSource() == deleteCourseButton) {
                writer.write("2");
                writer.println();
                writer.flush();
                try {
                    newDeleteCourse();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == createQuizButton) {
                writer.write("3");
                writer.println();
                writer.flush();
                newCreateQuiz();
            }

            if (e.getSource() == editQuizButton) {

                writer.write("4");
                writer.println();
                writer.flush();
                newEditQuiz();
            }

            if (e.getSource() == deleteQuizButton) {
                writer.write("5");
                writer.println();
                writer.flush();

                newDeleteQuiz();
            }

            if (e.getSource() == viewSubmissionButton) {

                writer.write("6");
                writer.println();
                writer.flush();
                newViewSubmission();
            }

            if (e.getSource() == gradeSubmissionButton) {

                writer.write("7");
                writer.println();
                writer.flush();
                newGradeSubmission();
            }

            if (e.getSource() == exitButton) {
                writer.write("8");
                writer.println();
                writer.flush();
                menuFrame.dispose();
            }


        }
    };

    public void createMenuGUI(Socket socket) {
        if(this.socket == null) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch(IOException e) {

            }
        }
        menuFrame = new JFrame("Teacher");
        Container content = menuFrame.getContentPane();
        content.setLayout(new BorderLayout());

        createCourseButton = new JButton("Create Course");
        createCourseButton.addActionListener(actionListener);
        deleteCourseButton = new JButton("Delete Course");
        deleteCourseButton.addActionListener(actionListener);
        createQuizButton = new JButton("Create Quiz");
        editQuizButton = new JButton("Edit Quiz");
        deleteQuizButton = new JButton("Delete Course");
        viewSubmissionButton = new JButton("View Student Submission");
        gradeSubmissionButton = new JButton("Grade Student Submission");
        exitButton = new JButton("Exit");
        exitButton.addActionListener(actionListener);

        JPanel menuPanel = new JPanel(new GridLayout(8, 1));
        menuPanel.add(createCourseButton);
        menuPanel.add(deleteCourseButton);
        menuPanel.add(createQuizButton);
        menuPanel.add(editQuizButton);
        menuPanel.add(deleteQuizButton);
        menuPanel.add(viewSubmissionButton);
        menuPanel.add(gradeSubmissionButton);
        menuPanel.add(exitButton);

        content.add(menuPanel);

        menuFrame.setSize(680, 420);
        menuFrame.setLocationRelativeTo(null);
        menuFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        menuFrame.setVisible(true);


    }

    public void newCreateCourse() {
        menuFrame.dispose();
        createCourseFrame = new JFrame("Create Course");
        Container content = createCourseFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel createCoursePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel enterCourseName = new JLabel("Enter the course you would like to create");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0,0);
        createCoursePanel.add(enterCourseName,c);

        JTextField courseText = new JTextField("");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 100;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        createCoursePanel.add(courseText,c);

        JButton enterButton = new JButton("Enter");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if(!courseText.getText().equals("")) {
                    String courseCreated = courseText.getText();
                    writer.write(courseCreated);
                    writer.println();
                    writer.flush();
                    writer.write(name);
                    writer.println();
                    writer.flush();
                    JOptionPane.showMessageDialog(null, "Course Added Successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    createCourseFrame.dispose();
                    createMenuGUI(socket);
                }
            }
        });
        createCoursePanel.add(enterButton, c);

        JButton cancelButton = new JButton("Cancel");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;      //make this component tall
        c.weightx = 0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.write("");
                writer.println();
                writer.flush();
                writer.write("");
                writer.println();
                writer.flush();
                createCourseFrame.dispose();
                createMenuGUI(socket);
            }
        });
        createCoursePanel.add(cancelButton, c);



        content.add(createCoursePanel);
        createCourseFrame.setSize(680, 420);
        createCourseFrame.setLocationRelativeTo(null);
        createCourseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createCourseFrame.setVisible(true);
    }

    public void newDeleteCourse() throws IOException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.write(name);
        writer.println();
        writer.flush();
        String line = bfr.readLine();
        DefaultListModel dlm = new DefaultListModel();
        int i = 0;
        while(line != null) {
        dlm.add(i,line);
        line = bfr.readLine();
        i++;
        }

        menuFrame.dispose();
        deleteCourseFrame = new JFrame("Delete Course");
        Container content = deleteCourseFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel deleteCoursePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel deleteCourseName = new JLabel("Select the course you would like to delete");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0,0);
        deleteCoursePanel.add(deleteCourseName,c);


        JList list = new JList(dlm);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        deleteCoursePanel.add(new JScrollPane(list), c);


        JButton enterButton = new JButton("Enter");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 5;
        c.ipadx = 50;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int index = list.getSelectedIndex();
                if(index > -1) {
                    String courseDeleted = (String) dlm.get(index);
                    JOptionPane.showMessageDialog(null, "Course Deleted Successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
                    //TODO Send deleted course name to server
                    deleteCourseFrame.dispose();
                    createMenuGUI(socket);
                }
            }
        });
        deleteCoursePanel.add(enterButton, c);

        JButton cancelButton = new JButton("Cancel");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 5;      //make this component tall
        c.weightx = 0;
        c.gridwidth = 1;
        c.gridx = 2;
        c.gridy = 2;
        cancelButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                deleteCourseFrame.dispose();
                createMenuGUI(socket);
            }
        });
        deleteCoursePanel.add(cancelButton, c);

        content.add(deleteCoursePanel);
        deleteCourseFrame.setSize(680, 420);
        deleteCourseFrame.setLocationRelativeTo(null);
        deleteCourseFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteCourseFrame.setVisible(true);

    }

    public void newCreateQuiz() {

    }

    public void newEditQuiz() {

    }

    public void newDeleteQuiz() {

    }

    public void newViewSubmission() {

    }

    public void newGradeSubmission() {

    }

}
