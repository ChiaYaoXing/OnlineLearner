package de.unidue.inf.is;

import de.unidue.inf.is.stores.UserStore;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;



/**
 * Einfaches Beispiel, das die Verwendung des {@link UserStore}s zeigt.
 */
public final class UserServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;


    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {

//        // mach was
//        User userToAdd = new User("Manfred", "Mustermann");
//
//        try (UserStore userStore = new UserStore()) {
//            userStore.addUser(userToAdd);
//            // userStore.somethingElse();
//            userStore.complete();
//        }
//
//        // mach noch mehr

    }

}
