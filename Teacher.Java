import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.*;
import java.net.Socket;

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
                try {
                    newCreateQuiz();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == editQuizButton) {

                writer.write("4");
                writer.println();
                writer.flush();
                try {
                    newEditQuiz();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
            }

            if (e.getSource() == deleteQuizButton) {
                writer.write("5");
                writer.println();
                writer.flush();

                try {
                    newDeleteQuiz();
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
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
        if (this.socket == null) {
            this.socket = socket;
            try {
                reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                writer = new PrintWriter(socket.getOutputStream());
            } catch (IOException e) {

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
        createQuizButton.addActionListener(actionListener);
        editQuizButton = new JButton("Edit Quiz");
        editQuizButton.addActionListener(actionListener);
        deleteQuizButton = new JButton("Delete Quiz");
        deleteQuizButton.addActionListener(actionListener);
        viewSubmissionButton = new JButton("View Student Submission");
        viewSubmissionButton.addActionListener(actionListener);
        gradeSubmissionButton = new JButton("Grade Student Submission");
        gradeSubmissionButton.addActionListener(actionListener);
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
        c.insets = new Insets(10, 0, 0, 0);
        createCoursePanel.add(enterCourseName, c);

        JTextField courseText = new JTextField("");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 100;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        createCoursePanel.add(courseText, c);

        JButton enterButton = new JButton("Enter");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!courseText.getText().equals("")) {
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
        while (line != null) {
            if (!line.equals("")) {
                dlm.add(i, line);
                line = bfr.readLine();
                i++;
            } else {
                break;
            }
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
        c.insets = new Insets(10, 0, 0, 0);
        deleteCoursePanel.add(deleteCourseName, c);


        JList list = new JList(dlm);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        if (dlm.size() >= 8) {
            JScrollPane scroll = new JScrollPane(list);
            deleteCoursePanel.add(scroll, c);
        } else {
            deleteCoursePanel.add(list, c);
        }


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
                if (index > -1) {
                    String courseDeleted = (String) dlm.get(index);
                    writer.write(courseDeleted);
                    writer.println();
                    writer.flush();
                    JOptionPane.showMessageDialog(null, "Course Deleted Successfully",
                            "Success", JOptionPane.INFORMATION_MESSAGE);
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
                writer.write("");
                writer.println();
                writer.flush();
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

    public void newCreateQuiz() throws IOException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.write(name);
        writer.println();
        writer.flush();
        String line = bfr.readLine();
        DefaultListModel dlm = new DefaultListModel();
        int i = 0;
        while (line != null) {
            if (!line.equals("")) {
                dlm.add(i, line);
                line = bfr.readLine();
                i++;
            } else {
                break;
            }
        }


        menuFrame.dispose();
        createQuizFrame = new JFrame("Create Quiz");
        Container content = createQuizFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel createQuizPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel createQuizName = new JLabel("Select the course the quiz is in");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        createQuizPanel.add(createQuizName, c);


        JList list = new JList(dlm);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        if (dlm.size() >= 8) {
            JScrollPane scroll = new JScrollPane(list);
            createQuizPanel.add(scroll, c);
        } else {
            createQuizPanel.add(list, c);
        }


        JButton enterButton = new JButton("Enter");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 5;
        c.ipadx = 50;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;

        Container content2 = createQuizFrame.getContentPane();
        content2.setLayout(new GridBagLayout());

        JPanel createQuizPanel2 = new JPanel(new GridBagLayout());

        JLabel enterQuizName = new JLabel("Enter the name of the quiz you would like to create");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        createQuizPanel2.add(enterQuizName, c);

        JTextField quizText = new JTextField("");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 100;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        createQuizPanel2.add(quizText, c);

        JButton enterButton2 = new JButton("Enter");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        enterButton2.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!quizText.getText().equals("")) {
                    String quizName = quizText.getText();
                    writer.write(quizName);
                    writer.println();
                    writer.flush();
                    createQuizFrame.dispose();
                    int option = JOptionPane.showConfirmDialog(null, "Do you want to import a file?",
                            "Create Quiz", JOptionPane.YES_NO_OPTION);
                    writer.write("" + option);
                    writer.println();
                    writer.flush();
                    if (option == JOptionPane.NO_OPTION) {
                        createQuizFrame.remove(createQuizPanel2);
                        createQuizFrame.dispose();
                        int randomized = JOptionPane.showConfirmDialog(null, "Do you want the questions to be randomized?",
                                "Create Quiz", JOptionPane.YES_NO_OPTION);
                        if (randomized == JOptionPane.YES_OPTION) {
                            writer.write("" + 0);
                            writer.println();
                            writer.flush();
                        } else {
                            writer.write("" + 1);
                            writer.println();
                            writer.flush();
                        }
                        createQuizQuestions();

                    } else if (option == JOptionPane.YES_OPTION) {
                        createQuizFrame.remove(createQuizPanel2);
                        createQuizFrame.dispose();
                        askForImport();
                    } else {
                        createQuizFrame.dispose();
                        createMenuGUI(socket);
                    }
                }
            }
        });
        createQuizPanel2.add(enterButton2, c);

        JButton cancelButton2 = new JButton("Cancel");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;      //make this component tall
        c.weightx = 0;
        c.gridwidth = 1;
        c.gridx = 1;
        c.gridy = 2;
        cancelButton2.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                writer.write("");
                writer.println();
                writer.flush();
                writer.write("");
                writer.println();
                writer.flush();
                createQuizFrame.dispose();
                createMenuGUI(socket);
            }
        });
        createQuizPanel2.add(cancelButton2, c);

        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                int index = list.getSelectedIndex();
                if (index > -1) {
                    String courseSelected = (String) dlm.get(index);
                    writer.write(courseSelected);
                    writer.println();
                    writer.flush();
                    createQuizFrame.remove(createQuizPanel);
                    createQuizFrame.dispose();
                    content2.add(createQuizPanel2);
                    createQuizFrame.setSize(680, 420);
                    createQuizFrame.setLocationRelativeTo(null);
                    createQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
                    createQuizFrame.setVisible(true);
                }
            }
        });
        createQuizPanel.add(enterButton, c);

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
                writer.write("");
                writer.println();
                writer.flush();
                createQuizFrame.dispose();
                createMenuGUI(socket);
            }
        });
        createQuizPanel.add(cancelButton, c);

        content.add(createQuizPanel);
        createQuizFrame.setSize(680, 420);
        createQuizFrame.setLocationRelativeTo(null);
        createQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createQuizFrame.setVisible(true);


    }

    public void newEditQuiz() throws IOException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.write(name);
        writer.println();
        writer.flush();
        String line = bfr.readLine();
        DefaultListModel dlm = new DefaultListModel();
        int i = 0;
        while (line != null) {
            if (!line.equals("")) {
                dlm.add(i, line);
                line = bfr.readLine();
                i++;
            } else {
                break;
            }
        }


        menuFrame.dispose();
        editQuizFrame = new JFrame("Edit Quiz");
        Container content = editQuizFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel editQuizPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel selectCourseName = new JLabel("Select the course the quiz is in");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        editQuizPanel.add(selectCourseName, c);


        JList list = new JList(dlm);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        if (dlm.size() >= 8) {
            JScrollPane scroll = new JScrollPane(list);
            editQuizPanel.add(scroll, c);
        } else {
            editQuizPanel.add(list, c);
        }


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
                if (index > -1) {
                    String courseSelected = (String) dlm.get(index);
                    writer.write(courseSelected);
                    writer.println();
                    writer.flush();
                    editQuizFrame.remove(editQuizPanel);
                    editQuizFrame.dispose();
                    askForQuiz();
                }
            }
        });
        editQuizPanel.add(enterButton, c);

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
                writer.write("");
                writer.println();
                writer.flush();
                editQuizFrame.dispose();
                createMenuGUI(socket);
            }
        });
        editQuizPanel.add(cancelButton, c);

        content.add(editQuizPanel);
        editQuizFrame.setSize(680, 420);
        editQuizFrame.setLocationRelativeTo(null);
        editQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        editQuizFrame.setVisible(true);
    }

    public void newDeleteQuiz() throws IOException {
        BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
        PrintWriter writer = new PrintWriter(socket.getOutputStream());
        writer.write(name);
        writer.println();
        writer.flush();
        String line = bfr.readLine();
        DefaultListModel dlm = new DefaultListModel();
        int i = 0;
        while (line != null) {
            if (!line.equals("")) {
                dlm.add(i, line);
                line = bfr.readLine();
                i++;
            } else {
                break;
            }
        }


        menuFrame.dispose();
        deleteQuizFrame = new JFrame("Delete Quiz");
        Container content = deleteQuizFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel deleteQuizPanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel deleteQuizName = new JLabel("Select the course the quiz is in");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        deleteQuizPanel.add(deleteQuizName, c);


        JList list = new JList(dlm);
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        if (dlm.size() >= 8) {
            JScrollPane scroll = new JScrollPane(list);
            deleteQuizPanel.add(scroll, c);
        } else {
            deleteQuizPanel.add(list, c);
        }


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
                if (index > -1) {
                    String courseSelected = (String) dlm.get(index);
                    writer.write(courseSelected);
                    writer.println();
                    writer.flush();
                    deleteQuizFrame.remove(deleteQuizPanel);
                    deleteQuizFrame.dispose();
                    askForQuiz2();

                }
            }
        });
        deleteQuizPanel.add(enterButton, c);

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
                writer.write("");
                writer.println();
                writer.flush();
                deleteQuizFrame.dispose();
                createMenuGUI(socket);
            }
        });
        deleteQuizPanel.add(cancelButton, c);

        content.add(deleteQuizPanel);
        deleteQuizFrame.setSize(680, 420);
        deleteQuizFrame.setLocationRelativeTo(null);
        deleteQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        deleteQuizFrame.setVisible(true);
    }

    public void newViewSubmission() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write(name);
            writer.println();
            writer.flush();
            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }


            menuFrame.dispose();
            viewSubmissionFrame = new JFrame("View Student Submission");
            Container content = viewSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel viewSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel viewSubmissionName = new JLabel("Select the student whose submission you would like to view");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            viewSubmissionPanel.add(viewSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                viewSubmissionPanel.add(scroll, c);
            } else {
                viewSubmissionPanel.add(list, c);
            }


            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 2;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index = list.getSelectedIndex();
                    if (index > -1) {
                        String studentSelected = (String) dlm.get(index);
                        writer.write(studentSelected);
                        writer.println();
                        writer.flush();
                        viewSubmissionFrame.remove(viewSubmissionPanel);
                        viewSubmissionFrame.dispose();
                        askForCourse();

                    }
                }
            });
            viewSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.gridy = 2;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    viewSubmissionFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            viewSubmissionPanel.add(cancelButton, c);

            content.add(viewSubmissionPanel);
            viewSubmissionFrame.setSize(680, 420);
            viewSubmissionFrame.setLocationRelativeTo(null);
            viewSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void newGradeSubmission() {
        try{
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write(name);
            writer.println();
            writer.flush();
            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }


            menuFrame.dispose();
            gradeSubmissionFrame = new JFrame("Grade Student Submission");
            Container content = gradeSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel gradeSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel gradeSubmissionName = new JLabel("Select the student whose submission you would like to grade");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            gradeSubmissionPanel.add(gradeSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                gradeSubmissionPanel.add(scroll, c);
            } else {
                gradeSubmissionPanel.add(list, c);
            }


            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 2;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index = list.getSelectedIndex();
                    if (index > -1) {
                        String studentSelected = (String) dlm.get(index);
                        writer.write(studentSelected);
                        writer.println();
                        writer.flush();
                        gradeSubmissionFrame.remove(gradeSubmissionPanel);
                        gradeSubmissionFrame.dispose();
                        askForCourse2();

                    }
                }
            });
            gradeSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.gridy = 2;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    gradeSubmissionFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            gradeSubmissionPanel.add(cancelButton, c);

            content.add(gradeSubmissionPanel);
            gradeSubmissionFrame.setSize(680, 420);
            gradeSubmissionFrame.setLocationRelativeTo(null);
            gradeSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gradeSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void askForImport() {
        Container content = createQuizFrame.getContentPane();
        content.setLayout(new GridBagLayout());

        JPanel fileNamePanel = new JPanel(new GridBagLayout());
        GridBagConstraints c = new GridBagConstraints();

        JLabel enterFileName = new JLabel("Enter the filepath for your file");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 2;
        c.ipadx = 2;
        c.weightx = 0.5;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 0;
        c.insets = new Insets(10, 0, 0, 0);
        fileNamePanel.add(enterFileName, c);

        JTextField fileText = new JTextField("");
        c.fill = GridBagConstraints.NONE;
        c.ipady = 1;
        c.ipadx = 100;
        c.weightx = 0.0;
        c.gridwidth = 3;
        c.gridx = 0;
        c.gridy = 1;
        fileNamePanel.add(fileText, c);

        JButton enterButton = new JButton("Enter");
        c.fill = GridBagConstraints.HORIZONTAL;
        c.ipady = 5;
        c.weightx = 0.0;
        c.gridwidth = 1;
        c.gridx = 0;
        c.gridy = 2;
        enterButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent ae) {
                if (!fileText.getText().equals("")) {
                    String filePath = fileText.getText();
                    File f = new File(filePath);
                    if (f.exists()) {
                        try {
                            writer.write("Go");
                            writer.println();
                            writer.flush();
                            BufferedReader bfr = new BufferedReader(new FileReader(f));
                            String line = bfr.readLine();
                            while (line != null) {
                                writer.write(line);
                                writer.println();
                                writer.flush();
                                line = bfr.readLine();
                            }
                            writer.write("///");
                            writer.println();
                            writer.flush();
                            bfr.close();
                        } catch (FileNotFoundException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        JOptionPane.showMessageDialog(null, "Quiz Created Successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        createQuizFrame.dispose();
                        createMenuGUI(socket);
                    } else {
                        JOptionPane.showMessageDialog(null, "That file does not exist",
                                "Error", JOptionPane.ERROR_MESSAGE);
                        fileText.setText("");
                    }

                }
            }
        });
        fileNamePanel.add(enterButton, c);

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
                createQuizFrame.dispose();
                createMenuGUI(socket);
            }
        });
        fileNamePanel.add(cancelButton, c);


        content.add(fileNamePanel);
        createQuizFrame.setSize(680, 420);
        createQuizFrame.setLocationRelativeTo(null);
        createQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        createQuizFrame.setVisible(true);
    }

    public void createQuizQuestions() {

        int option = JOptionPane.showConfirmDialog(null, "Do you want to add a question?",
                "Create Quiz", JOptionPane.YES_NO_OPTION);

        if (option == JOptionPane.NO_OPTION) {
            writer.write("No");
            writer.println();
            writer.flush();
            JOptionPane.showMessageDialog(null, "Quiz Created Successfully",
                    "Success", JOptionPane.INFORMATION_MESSAGE);
            createQuizFrame.dispose();
            createMenuGUI(socket);
            return;
        } else {
            writer.write("Yes");
            writer.println();
            writer.flush();
            Container content = createQuizFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel questionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel questionName = new JLabel("Enter the question");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            questionPanel.add(questionName, c);

            JTextField questionText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            questionPanel.add(questionText, c);

            JLabel answerAName = new JLabel("Enter answer choice A");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            c.insets = new Insets(10, 0, 0, 0);
            questionPanel.add(answerAName, c);

            JTextField answerAText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 3;
            questionPanel.add(answerAText, c);

            JLabel answerBName = new JLabel("Enter answer choice B");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 4;
            c.insets = new Insets(10, 0, 0, 0);
            questionPanel.add(answerBName, c);

            JTextField answerBText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 5;
            questionPanel.add(answerBText, c);

            JLabel answerCName = new JLabel("Enter answer choice C");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 6;
            c.insets = new Insets(10, 0, 0, 0);
            questionPanel.add(answerCName, c);

            JTextField answerCText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 7;
            questionPanel.add(answerCText, c);

            JLabel answerDName = new JLabel("Enter answer choice D");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 8;
            c.insets = new Insets(10, 0, 0, 0);
            questionPanel.add(answerDName, c);

            JTextField answerDText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 9;
            questionPanel.add(answerDText, c);

            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 10;

            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    if (!questionText.getText().equals("") && !answerAText.getText().equals("") && !answerBText.getText().equals("") && !answerCText.getText().equals("") && !answerDText.getText().equals("")) {
                        writer.write("Continue");
                        writer.println();
                        writer.flush();

                        writer.write(questionText.getText());
                        writer.println();
                        writer.flush();

                        writer.write(answerAText.getText());
                        writer.println();
                        writer.flush();

                        writer.write(answerBText.getText());
                        writer.println();
                        writer.flush();

                        writer.write(answerCText.getText());
                        writer.println();
                        writer.flush();

                        writer.write(answerDText.getText());
                        writer.println();
                        writer.flush();
                        createQuizFrame.remove(questionPanel);
                        createQuizQuestions();

                    } else {
                        JOptionPane.showMessageDialog(null, "One or more fields have not been filled out",
                                "Error", JOptionPane.ERROR_MESSAGE);
                    }
                }
            });
            questionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;      //make this component tall
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 10;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    createQuizFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            questionPanel.add(cancelButton, c);


            content.add(questionPanel);
            createQuizFrame.setSize(680, 420);
            createQuizFrame.setLocationRelativeTo(null);
            createQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            createQuizFrame.setVisible(true);
        }
    }

    public void askForQuiz() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }

            Container content = editQuizFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel editQuizPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel quizName = new JLabel("Select the quiz you would like to edit");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            editQuizPanel.add(quizName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                editQuizPanel.add(scroll, c);
            } else {
                editQuizPanel.add(list, c);
            }


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
                    if (index > -1) {
                        String quizSelected = (String) dlm.get(index);
                        writer.write(quizSelected);
                        writer.println();
                        writer.flush();
                        editQuizFrame.remove(editQuizPanel);
                        editQuizFrame.dispose();
                        editQuizGui();
                    }
                }
            });
            editQuizPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.gridy = 2;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    editQuizFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            editQuizPanel.add(cancelButton, c);

            content.add(editQuizPanel);
            editQuizFrame.setSize(680, 420);
            editQuizFrame.setLocationRelativeTo(null);
            editQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            editQuizFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void editQuizGui() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            writer.write("Continue");
            writer.println();
            writer.flush();

            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = editQuizFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel editQuizPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel editQuizName = new JLabel("Select the line to change and enter the new text for the line");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            editQuizPanel.add(editQuizName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                editQuizPanel.add(scroll, c);
            } else {
                editQuizPanel.add(list, c);
            }

            JTextField newText = new JTextField("");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 100;
            c.weightx = 0.0;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 2;
            editQuizPanel.add(newText, c);

            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            //c.ipadx = 50;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index;
                    index = list.getSelectedIndex();
                    if (index > -1 && !newText.getText().equals("")) {
                        String lineText = newText.getText();
                        writer.write(lineText);
                        writer.println();
                        writer.flush();
                        writer.write("" + index);
                        writer.println();
                        writer.flush();
                        newText.setText("");
                        dlm.set(index, lineText);
                        editQuizPanel.removeAll();
                        editQuizFrame.dispose();
                        editQuizGui();

                    }
                }
            });
            editQuizPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Done");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;      //make this component tall
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("///");
                    writer.println();
                    writer.flush();
                    editQuizFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            editQuizPanel.add(cancelButton, c);

            content.add(editQuizPanel);
            editQuizFrame.setSize(680, 420);
            editQuizFrame.setLocationRelativeTo(null);
            editQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            editQuizFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void askForQuiz2() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = deleteQuizFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel deleteQuizPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel deleteQuizName = new JLabel("Select the quiz to be deleted");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            deleteQuizPanel.add(deleteQuizName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                deleteQuizPanel.add(scroll, c);
            } else {
                deleteQuizPanel.add(list, c);
            }

            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            //c.ipadx = 50;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index;
                    index = list.getSelectedIndex();
                    if (index > -1) {
                        String quizSelected = (String) dlm.get(index);
                        writer.write(quizSelected);
                        writer.println();
                        writer.flush();
                        deleteQuizFrame.dispose();
                        JOptionPane.showMessageDialog(null, "Quiz Deleted Successfully",
                                "Success", JOptionPane.INFORMATION_MESSAGE);
                        createMenuGUI(socket);

                    }
                }
            });
            deleteQuizPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;      //make this component tall
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    deleteQuizFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            deleteQuizPanel.add(cancelButton, c);

            content.add(deleteQuizPanel);
            deleteQuizFrame.setSize(680, 420);
            deleteQuizFrame.setLocationRelativeTo(null);
            deleteQuizFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            deleteQuizFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void askForCourse() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }

            viewSubmissionFrame = new JFrame("View Student Submission");
            Container content = viewSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel viewSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel selectCourseName = new JLabel("Select the course the quiz is in");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            viewSubmissionPanel.add(selectCourseName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                viewSubmissionPanel.add(scroll, c);
            } else {
                viewSubmissionPanel.add(list, c);
            }


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
                    if (index > -1) {
                        String courseSelected = (String) dlm.get(index);
                        writer.write(courseSelected);
                        writer.println();
                        writer.flush();
                        viewSubmissionFrame.remove(viewSubmissionPanel);
                        viewSubmissionFrame.dispose();
                        askForQuiz3();
                    }
                }
            });
            viewSubmissionPanel.add(enterButton, c);

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
                    writer.write("");
                    writer.println();
                    writer.flush();
                    viewSubmissionFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            viewSubmissionPanel.add(cancelButton, c);

            content.add(viewSubmissionPanel);
            viewSubmissionFrame.setSize(680, 420);
            viewSubmissionFrame.setLocationRelativeTo(null);
            viewSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSubmissionFrame.setVisible(true);
        } catch(IOException e) {

        }
    }

    public void askForQuiz3() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = viewSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel viewSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel viewSubmissionName = new JLabel("Select the quiz to be viewed");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            viewSubmissionPanel.add(viewSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                viewSubmissionPanel.add(scroll, c);
            } else {
                viewSubmissionPanel.add(list, c);
            }

            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            //c.ipadx = 50;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index;
                    index = list.getSelectedIndex();
                    if (index > -1) {
                        String quizSelected = (String) dlm.get(index);
                        writer.write(quizSelected);
                        writer.println();
                        writer.flush();
                        try {
                            String exists = bfr.readLine();
                            if(exists.equals("Yes")){
                                viewSubmissionFrame.remove(viewSubmissionPanel);
                                viewSubmissionFrame.dispose();
                                showSubmission();
                            } else {
                                JOptionPane.showMessageDialog(null, "That file does not exist",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                viewSubmissionFrame.dispose();
                                createMenuGUI(socket);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            });
            viewSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;      //make this component tall
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    viewSubmissionFrame.dispose();
                    createMenuGUI(socket);

                }
            });
            viewSubmissionPanel.add(cancelButton, c);

            content.add(viewSubmissionPanel);
            viewSubmissionFrame.setSize(680, 420);
            viewSubmissionFrame.setLocationRelativeTo(null);
            viewSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void showSubmission() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = viewSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel viewSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel viewSubmissionName = new JLabel("View the submission and press done when you are done");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            viewSubmissionPanel.add(viewSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                viewSubmissionPanel.add(scroll, c);
            } else {
                viewSubmissionPanel.add(list, c);
            }

            JButton cancelButton = new JButton("Done");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 5;
            c.ipadx = 50;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    viewSubmissionFrame.dispose();
                    createMenuGUI(socket);

                }
            });
            viewSubmissionPanel.add(cancelButton, c);

            content.add(viewSubmissionPanel);
            viewSubmissionFrame.setSize(680, 420);
            viewSubmissionFrame.setLocationRelativeTo(null);
            viewSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            viewSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }
    public void askForCourse2() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());
            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }

            gradeSubmissionFrame = new JFrame("Grade Student Submission");
            Container content = gradeSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel gradeSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel selectCourseName = new JLabel("Select the course the quiz is in");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            gradeSubmissionPanel.add(selectCourseName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                gradeSubmissionPanel.add(scroll, c);
            } else {
                gradeSubmissionPanel.add(list, c);
            }


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
                    if (index > -1) {
                        String courseSelected = (String) dlm.get(index);
                        writer.write(courseSelected);
                        writer.println();
                        writer.flush();
                        gradeSubmissionFrame.remove(gradeSubmissionPanel);
                        gradeSubmissionFrame.dispose();
                        askForQuiz4();
                    }
                }
            });
            gradeSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 2;
            c.gridy = 2;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    gradeSubmissionFrame.dispose();
                    createMenuGUI(socket);
                }
            });
            gradeSubmissionPanel.add(cancelButton, c);

            content.add(gradeSubmissionPanel);
            gradeSubmissionFrame.setSize(680, 420);
            gradeSubmissionFrame.setLocationRelativeTo(null);
            gradeSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gradeSubmissionFrame.setVisible(true);
        } catch(IOException e) {

        }
    }

    public void askForQuiz4() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line = bfr.readLine();
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = gradeSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel gradeSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel gradeSubmissionName = new JLabel("Select the quiz to be graded");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            gradeSubmissionPanel.add(gradeSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            if (dlm.size() >= 8) {
                JScrollPane scroll = new JScrollPane(list);
                gradeSubmissionPanel.add(scroll, c);
            } else {
                gradeSubmissionPanel.add(list, c);
            }

            JButton enterButton = new JButton("Enter");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            //c.ipadx = 50;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    int index;
                    index = list.getSelectedIndex();
                    if (index > -1) {
                        String quizSelected = (String) dlm.get(index);
                        writer.write(quizSelected);
                        writer.println();
                        writer.flush();
                        try {
                            String exists = bfr.readLine();
                            if(exists.equals("Yes")){
                                String grade = reader.readLine();
                                if(grade.equals("Graded")) {
                                    JOptionPane.showMessageDialog(null, "That quiz has been graded",
                                            "Error", JOptionPane.ERROR_MESSAGE);
                                    gradeSubmissionFrame.dispose();
                                    createMenuGUI(socket);
                                } else {
                                    gradeSubmissionFrame.remove(gradeSubmissionPanel);
                                    gradeSubmissionFrame.dispose();
                                    gradeSubmission();
                                }


                            } else {
                                JOptionPane.showMessageDialog(null, "That file does not exist",
                                        "Error", JOptionPane.ERROR_MESSAGE);
                                gradeSubmissionFrame.dispose();
                                createMenuGUI(socket);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }

                    }
                }
            });
            gradeSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("Cancel");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("");
                    writer.println();
                    writer.flush();
                    gradeSubmissionFrame.dispose();
                    createMenuGUI(socket);

                }
            });
            gradeSubmissionPanel.add(cancelButton, c);

            content.add(gradeSubmissionPanel);
            gradeSubmissionFrame.setSize(680, 420);
            gradeSubmissionFrame.setLocationRelativeTo(null);
            gradeSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gradeSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }

    public void gradeSubmission() {
        try {
            BufferedReader bfr = new BufferedReader(new InputStreamReader(socket.getInputStream()));    //Reads from server
            PrintWriter writer = new PrintWriter(socket.getOutputStream());

            String line = bfr.readLine();
            if(line.equals("///")) {
                createMenuGUI(socket);
                return;
            }
            DefaultListModel dlm = new DefaultListModel();
            int i = 0;
            while (line != null) {
                if (!line.equals("///")) {
                    dlm.add(i, line);
                    line = bfr.readLine();
                    i++;
                } else {
                    break;
                }
            }
            Container content = gradeSubmissionFrame.getContentPane();
            content.setLayout(new GridBagLayout());

            JPanel gradeSubmissionPanel = new JPanel(new GridBagLayout());
            GridBagConstraints c = new GridBagConstraints();

            JLabel gradeSubmissionName = new JLabel("Is this answer correct?");
            c.fill = GridBagConstraints.NONE;
            c.ipady = 2;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 0;
            c.insets = new Insets(10, 0, 0, 0);
            gradeSubmissionPanel.add(gradeSubmissionName, c);


            JList list = new JList(dlm);
            c.fill = GridBagConstraints.NONE;
            c.ipady = 1;
            c.ipadx = 2;
            c.weightx = 0.5;
            c.gridwidth = 3;
            c.gridx = 0;
            c.gridy = 1;
            gradeSubmissionPanel.add(list, c);


            JButton enterButton = new JButton("Yes");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            //c.ipadx = 50;
            c.weightx = 0.0;
            c.gridwidth = 1;
            c.gridx = 0;
            c.gridy = 3;
            enterButton.addActionListener(new ActionListener() {
                public void actionPerformed(ActionEvent ae) {
                    writer.write("Yes");
                    writer.println();
                    writer.flush();
                    gradeSubmissionFrame.remove(gradeSubmissionPanel);
                    gradeSubmissionFrame.dispose();
                    gradeSubmission();

                }
            });
            gradeSubmissionPanel.add(enterButton, c);

            JButton cancelButton = new JButton("No");
            c.fill = GridBagConstraints.HORIZONTAL;
            c.ipady = 5;
            c.weightx = 0;
            c.gridwidth = 1;
            c.gridx = 1;
            c.gridy = 3;
            cancelButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    writer.write("No");
                    writer.println();
                    writer.flush();
                    gradeSubmissionFrame.remove(gradeSubmissionPanel);
                    gradeSubmissionFrame.dispose();
                    gradeSubmission();

                }
            });
            gradeSubmissionPanel.add(cancelButton, c);

            content.add(gradeSubmissionPanel);
            gradeSubmissionFrame.setSize(680, 420);
            gradeSubmissionFrame.setLocationRelativeTo(null);
            gradeSubmissionFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            gradeSubmissionFrame.setVisible(true);
        } catch (IOException e) {

        }
    }
}
