package de.unidue.inf.is;

import de.unidue.inf.is.domain.Assignment;
import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Submission;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.AssignmentStore;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.stores.SubmissionStore;
import de.unidue.inf.is.stores.SubmitStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class NewAssignmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

//    private static List<User> userList = new ArrayList<>();
//
//    // Just prepare static data to display on screen
//    static {
//        userList.add(new User("Bill", "Gates@uni-due.de", (short) 1));
//        userList.add(new User("Steve", "Jobs@uni-due.de", (short) 2));
//        userList.add(new User("Larry", "Page@uni-due.de", (short) 3));
//        userList.add(new User("Sergey", "Brin@uni-due.de", (short) 4));
//        userList.add(new User("Larry", "Ellison@stud.uni-due.de", (short) 5));
//    }


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        // Put the user list in request and let freemarker paint it.
//        request.setAttribute("users", userList);
        short aid = Short.parseShort(request.getParameter("aid"));
        short kid = Short.parseShort(request.getParameter("kid"));

        try(CourseStore courseStore =  new CourseStore(); AssignmentStore assignmentStore = new AssignmentStore()){
            Course course = courseStore.getCourse(kid);
            Assignment assignment = assignmentStore.getAssignment(kid, aid);

            request.setAttribute("course", course);
            request.setAttribute("assignment", assignment);
            courseStore.complete();
            assignmentStore.complete();
        }
        request.getRequestDispatcher("/new_assignment.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String text = request.getParameter("text");
        short kid = Short.parseShort(request.getParameter("kid"));
        short aid = Short.parseShort(request.getParameter("aid"));
        User user = (User) request.getSession().getAttribute("user");


        try(SubmitStore submitStore = new SubmitStore(); SubmissionStore submissionStore = new SubmissionStore()){
            if(submitStore.isSubmitted(user.getUid(), kid, aid)){
                request.setAttribute("isError", "true");
                request.setAttribute("errorText","Aufgabe ist schon eingereicht!");
            }
            else{
                Submission submission = submissionStore.addSubmission(text);
                submitStore.addSubmit(user.getUid(), kid, aid, submission);

            }
            request.getRequestDispatcher("view_course?kid=" + kid + "&isRegistered=true").forward(request, response);
            submitStore.complete();
            submissionStore.complete();
        }


//        request.getRequestDispatcher("onlineLearner").forward(request, response);
//        doGet(request, response);
    }
}