package de.unidue.inf.is;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.CourseStore;
import de.unidue.inf.is.stores.StoreException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

public final class newCourseServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

        request.getRequestDispatcher("/new_course.ftl").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
            IOException {

        User user = (User) request.getSession().getAttribute("user");
        Course course;
        String name = request.getParameter("courseName");
        String password = request.getParameter("coursePassword");
        String freeplace = request.getParameter("courseFreeplace");
        String description = request.getParameter("courseDescription");
        course = new Course(name, description, password, Short.parseShort(freeplace), user.getUid());
        CourseStore courseStore = null;



        try {
            courseStore = new CourseStore();

            courseStore.addCourse(course);
            courseStore.decrementFreePlace(course.getKid());
            courseStore.complete();
            courseStore.close();
            courseStore = null;

            response.sendRedirect("view_main");

        } catch(Exception e){
            throw new StoreException(e);
        } finally {

            if(courseStore != null){
                courseStore.close();
            }
        }
    }
}
