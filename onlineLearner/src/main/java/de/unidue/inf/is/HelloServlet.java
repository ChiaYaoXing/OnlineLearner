package de.unidue.inf.is;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.stores.StoreException;
import de.unidue.inf.is.stores.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;



/**
 * Einfaches Beispiel, das die Vewendung der Template-Engine zeigt.
 */
public final class HelloServlet extends HttpServlet {

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

        request.getRequestDispatcher("/index.ftl").forward(request, response);
    }


    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException,
                    IOException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");


        if (null != name && null != email && !name.isEmpty() && !email.isEmpty()) {

//            synchronized (userList) {
//                userList.add(new User(firstname, lastname));
//            }
            UserStore userStore = null;
            User user = new User(name, email);
            HttpSession session = request.getSession();

            try {
                userStore = new UserStore();
                userStore.addUser(user);
                userStore.complete();
                userStore.addUid(user);


            } catch(Exception e){
                throw new StoreException(e);
            }
            finally {
                if(userStore != null) userStore.close();
                session.setAttribute("user", user);
            }

        }
//        request.getRequestDispatcher("onlineLearner").forward(request, response);
        response.sendRedirect("view_main");
//        doGet(request, response);
    }
}
