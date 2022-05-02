Project 5: Learning management System Option 2 (Online quiz)
---------------------------------------------------

Submitters
---------------------------------------------------

Kacey - Submitted Report and Presentation

Chase - Submitted Vocareum Workspace

Instructions:
--------------------------------------------------

Run "MainServer"
Run "ManMethod" as many times as you want


Teacher.java:
---------------------------
When the teacher logs in with the correct username and password, they are provided with an interface. They have 8 different options. They can create a quiz, edit a quiz, delete a quiz, grade a quiz, view a student’s submission, create a new course, delete a course, and an exit option. They can also maximize or minimize the screen and can press the X button at the top anytime to close the program. Multiple teachers can be connected at once.

When the teacher object is created, a new, empty file is created that will hold the courses the teacher creates. In order to actually get anything done with the teacher class, a course must be created first. When a course is successfully created, this empty file will now contain the new courses’ name. A new file will also be created which holds the quizzes within that course, which will be empty when first created. With a new course created, the teacher has several more options. They have two new options, creating a quiz and deleting a course. 

If a teacher decides to delete a course, the course is removed from the text file which holds the teachers course. The text file that holds the quizzes in that course is deleted. Finally, all of the quizzes within that course are deleted. The student submissions are not deleted, however

If a teacher creates a quiz successfully, the previously empty text file that is supposed to contain the quizzes will now contain this quizzes name. A new file is also created when a quiz is made. This file is the actual quiz with the contents that the teacher provided. This file must be written in a certain way. The very first line will either contain true or false, which will determine if the teacher wants the quiz to be randomized. If there is no true or false, then it is assumed the quiz is already grade. The next line will be the question the quiz is asking. The next four lines are answer choices A, B, C, and D with their respective options. A new text file is also created with the correct answer choices that the teacher gives. While creating a file, the teacher is given an option if they want to import a file for the quiz. If they say yes, they must provide the file path to that file.. The quiz file must be formatted in the way that was just mentioned for it to work.

With a quiz created, the teacher has several more options. The teacher can decide to edit the quiz. If this option is chosen, the entire quiz file is printed out. The teacher may click on a line they want to edit and enter the new text that will replace that line. They then press enter and the line will be changed to the new line of text.

Another action the teacher can perform is the delete quiz option. If the teacher does this, the quiz file is deleted and the quiz name located in the course file that holds the quizzes is removed.

The last two options will only be performed correctly if a student has submitted a quiz. If a quiz has been submitted, a teacher can choose to view a student’s submission. This just prints out the students' submission of that quiz.

The last option is the ability to grade a quiz. If this option is chosen, the students' quiz will be printed one question at a time. The teacher can decide if the question is right or wrong. The grade is then calculated and written on the student submission file so the student can see their grade and what they missed. If the teacher tries to grade a quiz for a student who has not taken it yet, they will be returned to the options menu. Similarly, if no student has been added to this teacher’s course and the teacher selects one of these options, there will be no option to select a student.
In almost all of the GUIs, there is an option to cancel and go back to either the main menu, or exit the program entirely. All content will be refreshed if the user presses the cancel button and goes back to where they were. In some cases, such as edit quiz, the content will be updated when the enter button is clicked with content inside the text box.

Student.java:
----------------------------------
The Student class requires only the students username as a parameter so that it can later be used when writing the students file to pass back to the teacher for grading. The student class should be called after the login so that this parameter can be passed to it. The student class begins by prompting the user to input the username of their teacher. It will check if the teacher username they inputted exists by reading the TeacherAccounts.txt file to see if the username is stored there. If not it will print that the teacher does not exist. If it does exist then next the user will be prompted to choose a course out of the courses that the teacher has previously created. These courses are stored in a file named “teachersUsername_Courses.txt”. If an invalid choice is inputted it will print an error and loop back to the course selection. Once a course is selected the student will be prompted to choose a quiz that the teacher has created. These quizzes are stored in a file called “teachersUsername_courseName.txt”. Once a student selects a quiz the student class will check whether or not the student has taken the quiz before. This is done by checking for whether or not a file already exists called “studentUsername_teacherUsername_courseName_quizName.txt”. This is the file that is created when a student completes a quiz. If the quiz has already been taken then the student will be prompted between returning to the course selection or viewing the grade on their quiz. If they choose to view their quiz then if the teacher has graded the quiz it will print the percent given by the teacher and if it has not been graded it will print that the quiz has not yet been graded. If the quiz has not been taken yet then the selection quiz will be printed into the terminal for the user to see. This is done by reading the quiz file and the printing with System.out.println(readLine()). The student will then have the option to import a file with answers or to answer manually. When the quiz is finished a file will be created called “studentUsername_teacherUsername_courseName_quizName.txt” which will store a copy of the quiz taken, the students username, the students answers, and a timestamp of when they finished. The new student class fixed the problem from Project 4 where the code is exiting after a quiz.




Run.java
-------------------------------------------
For login, it will first print out a welcome prompt and ask if the user is a student or a teacher.. After that, the system will print out a message to ask what action the user will like to proceed. Which includes making an account, editing an account, login, and delete account. If the user decides to make an account, the system will prompt and ask them to enter their username, then the system will compare the username to the corresponding user type and see if the username already exists. If so, the system will return the user to the previous step. Or if the username contains a space, the system will deny the user from making an account. If the user decides to make an account and they pass all the requirements, the system will ask them for their password. Then the system will store the username in either teacherAccount.txt or studentAccount.txt based on their type, and the system will do the same for password. If the user already has an account and plans to edit their account, the system will first ask for their username, then compare it to the text file of their corresponding type. If the username does not exist, the system will prompt them back to the previous step. If the username exists, the system will ask for their password. Then the system will get the index of the account txt file and find the password in the password txt file using the index then compare it to the password the user typed in. If it matched, then the system will ask if the user wants to edit their username or their password, and using bufferedreader and bufferedwriter, the system will update the corresponding txt file. If the user decides to login, then similar to editing their account, the system will ask for their username, compare to the corresponding file to check if the username exist, then ask for password if the username exist, and compare the password the user typed in with what was stored before using the method mentioned above. If the password matches the username, then based on the type of the user, they’ll be able to use either functions from the teacher class or functions from the student class. Lastly, if the user decides to delete an account, the program will ask the client which account they would like to delete. If the username exists, it will ask for the password. If the passwords match, the account will be deleted.


MainMethod.java
-----------------------------------------
The MainMethod class is the class that the client runs if they want to use the program. This class creates a connection with the main server. MainMethod will then call runQuiz in the Run class. runQuiz will return a username. If the username is an empty string, it will do runQuiz again. This means that the user did not login to an account, they performed one of the other actions. Once an actual username is returned, a teacher or student object is created depending on the username that was returned. Then, the methods to run the teacher/student class are called. This fixed one of the problems we had in Project 4, where our program would not loop after creating an account, editing, or deleting an account.







MainServer.java
----------------------------------------
The purpose of the MainServer class is to act as the server that the client connects to. The server runs constantly. When  the socket accepts a connection and a connection is established with a client, a new ServerThread thread is created and is started.





ServerThread.java
--------------------------------------
The main purpose of our ServerThread is to store information that our user inputs. It is a thread that is created when a new client connects to the server. This allows the server to be multi-threaded. It also serves the information of communicating with the client. The ServerThread will wait to receive the action that it is supposed to perform by reading the line for the client. The possible actions are create, edit, delete and login. If it reads create from the client, it will read the next line to determine if the user is a teacher or a student. Then it will check the username and send it back to the client indicating if the username is taken or not, then it will wait from the client and save the account information and password if supposed to and write to a file. 
If it reads edit from the client, it will read the next line to determine if the user is a teacher or a student. Then it will check if the username exists and send it back to the client, then it’ll receive the password from the client and check if the password matches the username in our record. If it matches it’ll send success to the client. Then it’ll wait for the server to send the new username that the user wants and check if it exists, then it’ll wait for the new password that the user wants and write it to the corresponding file. 
If it reads delete from the client, it will read the next line to determine if the user is a teacher or a student. Then it will check if the username exists and send it back to the client, then it’ll receive the password from the client and check if the password matches the username in our record. If it matches it’ll send success to the client. Then it will delete the username and the password from the corresponding file. 
If it reads login from the client, it will read the next line to determine if the user is a teacher or a student. Then it will check if the username exists and sends it back to the client, then it’ll receive the password from the client and check if the password matches the username in our record. If it matches it’ll send success to the client. Then it will proceed to login and the server will run what the teacher or the student class will need. 
