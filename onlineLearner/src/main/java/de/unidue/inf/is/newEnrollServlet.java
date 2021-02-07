package de.unidue.inf.is;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.RegisterStore;
import de.unidue.inf.is.stores.StoreException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;
import java.util.Objects;


public final class newEnrollServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        try(RegisterStore registerStore = new RegisterStore()){
            User user = (User) request.getSession().getAttribute("user");
            List<Course> courses =  registerStore.getRegisteredCourses(user.getUid());
            List<Course> availableCourses =  registerStore.getNonRegisteredCourses(user.getUid());
            registerStore.complete();
            request.setAttribute("numberOfColumns", courses.size());
            request.setAttribute("availableCourses", availableCourses);
            request.setAttribute("courses", courses);
        } catch (Exception e){
            throw new StoreException(e);
        }
        request.getRequestDispatcher("/view_main.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String kid = request.getParameter("kid");
        String isRegister = request.getParameter("isRegister");
        HttpSession session = request.getSession();
        User user = (User) session.getAttribute("user");


        try(RegisterStore registerStore = new RegisterStore()){
            if (null != kid && !kid.isEmpty() && null!=isRegister && !isRegister.isEmpty()) {
                if(Objects.equals("false", isRegister)){
                    registerStore.deregister(user.getUid(), Short.parseShort(kid));

                }

        }

//
            short courseID = Short.valueOf(kid);
            System.out.println(kid);
//
//            try {
//                userStore = new UserStore();
//                userStore.addUser(user);
//                userStore.complete();
//                userStore.addUid(user);
//
//
//            } catch(Exception e){
//                throw new StoreException(e);
//            }
//            finally {
//                if(userStore != null) userStore.close();
//                session.setAttribute("user", user);
//            }

        }
//        request.getRequestDispatcher("onlineLearner").forward(request, response);
        response.sendRedirect("view_main");
//        doGet(request, response);
    }

}