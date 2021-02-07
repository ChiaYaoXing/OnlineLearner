package de.unidue.inf.is;

import de.unidue.inf.is.domain.*;
import de.unidue.inf.is.stores.*;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;


public final class viewCourseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        try(RegisterStore registerStore = new RegisterStore()){
//            User user = (User) request.getSession().getAttribute("user");
//            List<Course> courses =  registerStore.getRegisteredCourses(user.getUid());
//            List<Course> availableCourses =  registerStore.getNonRegisteredCourses(user.getUid());
//            registerStore.complete();
//            request.setAttribute("numberOfColumns", courses.size());
//            request.setAttribute("availableCourses", availableCourses);
//            request.setAttribute("courses", courses);
//        } catch (Exception e){
//            throw new StoreException(e);
//        }
        request.getRequestDispatcher("/view_course.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String kid = request.getParameter("courseID");
        String registered = request.getParameter("isRegistered");
        User requestUser =(User) request.getSession().getAttribute("user");


        if (null != kid && !kid.isEmpty()) {

            short courseID = Short.parseShort(kid);
            boolean isRegistered = Boolean.parseBoolean(registered);


            try(CourseStore courseStore = new CourseStore(); UserStore userStore = new UserStore();
                SubmitStore submitStore = new SubmitStore(); AssignmentStore assignmentStore = new AssignmentStore();
                SubmissionStore submissionStore = new SubmissionStore(); RateStore rateStore = new RateStore()) {
                Course course = courseStore.getCourse(courseID);
                User user = userStore.getUser(course.getCid());
                request.setAttribute("creator", user);
                request.setAttribute("course",course);
                request.setAttribute("isRegistered", isRegistered);
                StringBuilder res = new StringBuilder();
                res.append("<form action=\"new_enroll\" method=\"post\"><input type=\"hidden\" name=\"kid\" value=\"").append(kid).append("\"><input type=\"hidden\" name=\"isRegister\" value=\"");
                if(!isRegistered){
                    res.append("true").append("\"><button type=\"submit\">Einschreiben</button>");
                    res.append("</form>");
                } else{
                    res.append("false").append("\"><button type=\"submit\">Kurs Löschen</button>");
                    res.append("</form>");
                    res.append("<hr><h1>Liste der Aufgaben</h1><tr><th>Aufgabe</th><th>Meine Abgabe</th><th>Bewertungsnote</th></tr>");
                    List<Submit> submits;
                    List<Assignment> assignments;

                        assignments = assignmentStore.getAssignments(courseID);
                        submits = submitStore.getSubmit(requestUser.getUid(), courseID);

                        for(Assignment a: assignments) {
                            res.append("<tr><td><form action=\"new_assignment\" method=\"get\"><input type=\"hidden\" name=\"aid\" value=\"");
                            res.append(a.getAid());
                            res.append("\"><input type=\"hidden\" name=\"kid\" value=\"").append(courseID).append("\"><button type=\"submit\" class=\"btn\">").append(a.getName()).append("</button></form></td><td>");
                            String result = null;
                            Rate rate = null;
                            for(Submit s:submits){
                                if(s.getAid() == a.getAid()){
                                    Submission submission = submissionStore.getSubmission(s.getSid());
                                    rate = rateStore.getRate(submission.getsid());
                                    result = submission.getText();
                                    break;
                                }
                            }
                            if(result == null){
                                result = "Keine Abgabe";
                                res.append(result);
                                res.append("</td><td></td></tr>");
                            } else {
                                res.append(result);
                                res.append("</td><td>");
                                if (rate != null) {
                                    res.append(rate.getScore());
                                } else {
                                    res.append("Noch keine Bewertung");
                                }
                                res.append("</td></tr>");
                            }
                        }

                }
                submitStore.complete();
                assignmentStore.complete();
                userStore.complete();
                courseStore.complete();
                submissionStore.complete();
                rateStore.complete();
                request.setAttribute("res", res.toString());
            } catch(Exception e){
                throw new StoreException(e);
            }


        }
        doGet(request, response);
    }

}