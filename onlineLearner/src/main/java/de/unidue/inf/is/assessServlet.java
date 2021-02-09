package de.unidue.inf.is;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.AssignmentStore;
import de.unidue.inf.is.stores.RateStore;
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
public final class assessServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        User user = (User)request.getSession().getAttribute("user");
        short kid = Short.parseShort(request.getParameter("kid"));
        try(SubmitStore submitStore = new SubmitStore(); AssignmentStore assignmentStore = new AssignmentStore();
            SubmissionStore submissionStore = new SubmissionStore()){
            Submit submit = submitStore.getRandomSubmit(user.getUid(), kid);
            if(submit != null){
                Assignment assignment = assignmentStore.getAssignment(submit.getKid(), submit.getAid());
                Submission submission = submissionStore.getSubmission(submit.getSid());
                request.setAttribute("assignment", assignment);
                request.setAttribute("submission", submission);
                request.getRequestDispatcher("assess.ftl").forward(request, response);
            }
            else{
                request.setAttribute("isError", "true");
                request.setAttribute("errorText", "No available assignment to correct.");
                request.getRequestDispatcher("view_course?kid=" + kid + "&isRegistered=true").forward(request, response);
            }


        }

        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        short note = Short.parseShort(request.getParameter("note"));
        String comment = request.getParameter("comment");
        short sid = Short.parseShort(request.getParameter("sid"));
        User user = (User) request.getSession().getAttribute("user");
        short kid = Short.parseShort(request.getParameter("kid"));

        try(RateStore rateStore = new RateStore()){
            rateStore.addRate(new Rate(user.getUid(), sid, note, comment));
            rateStore.complete();
            response.sendRedirect("view_course?kid="+kid+"&isRegistered=true");
        }





//        request.getRequestDispatcher("onlineLearner").forward(request, response);
        response.sendRedirect("view_main");
//        doGet(request, response);
    }
}
