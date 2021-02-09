package de.unidue.inf.is;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Register;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.stores.RegisterStore;
import de.unidue.inf.is.stores.StoreException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Objects;


public final class newEnrollServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        request.setAttribute("course", request.getAttribute("course"));
        request.getRequestDispatcher("/new_enroll.ftl").forward(request, response);

    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        String kid = request.getParameter("kid");
        String isRegister = request.getParameter("isRegister");
        User user =(User) request.getSession().getAttribute("user");
        StringBuilder res = new StringBuilder();
        Course course;


        try (RegisterStore registerStore = new RegisterStore(); CourseStore courseStore = new CourseStore()) {
            course = courseStore.getCourse(Short.parseShort(kid));
            request.setAttribute("course", course);
            if(course.getPasswort() != null){
                res.append("<h2> Einschreibeschlüssel: <input type=\"password\" name=\"password\"></h2>");
            } else{
                res.append("<h2>No Password needed</h2>");
            }
            request.setAttribute("res", res.toString());

            if (null != isRegister && !isRegister.isEmpty()) {
                course = courseStore.getCourse(Short.parseShort(kid));
                request.setAttribute("course", course);
                if (Objects.equals("false", isRegister)) {
                    registerStore.deregister(user.getUid(), Short.parseShort(kid));
                    response.sendRedirect("view_main");
                }
                else {
                    doGet(request, response);
                }

            } else{
                String password = request.getParameter("password");
//                course = courseStore.getCourse(Short.parseShort(kid));
//                request.setAttribute("course", course);



                if((course.validate(password) || course.getPasswort() == null) && course.isAvailable()){
                    Register register = new Register(user.getUid(), course.getKid());
                    registerStore.addRegister(register);
                    courseStore.decrementFreePlace(course.getKid());
                    response.sendRedirect("view_course?kid="+course.getKid()+"&isRegistered=true");
                }
                else{
                    doGet(request, response);
                }

            }
            registerStore.complete();
            courseStore.complete();

        }catch (Exception e){
            throw new StoreException(e);
        }

    }
}