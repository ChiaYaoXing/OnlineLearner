package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Register;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class RegisterStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public RegisterStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }



    public void addRegister(Register register) throws StoreException, IOException {

        try (CourseStore courseStore = new CourseStore()){
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp019.einschreiben (bnummer, kid) " +
                            "values (?, ?)");
            preparedStatement.setShort(1, register.getUid());
            preparedStatement.setShort(2, register.getKid());
            preparedStatement.executeUpdate();
            courseStore.decrementFreePlace(register.getKid());
            courseStore.complete();
        } catch (SQLException e) {
            throw new StoreException(e);
        }

    }

    public void deregister(short uid, short kid) throws StoreException, IOException{
        String stmt = "DELETE FROM dbp019.einschreiben e WHERE e.bnummer = ? AND e.kid = ?";

        try(CourseStore courseStore = new CourseStore()){
            PreparedStatement preparedStatement = connection.prepareStatement(stmt);
            preparedStatement.setShort(1, uid);
            preparedStatement.setShort(2, kid);
            courseStore.incrementFreePlace(kid);
            courseStore.complete();
            preparedStatement.executeUpdate();

        }catch(SQLException e){
            throw new StoreException(e);
        }

    }

    public List<Course> getRegisteredCourses(short uid) throws StoreException, IOException{

        try(CourseStore courseStore = new CourseStore()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM dbp019.einschreiben e " +
                    "WHERE e.bnummer = ?");
            preparedStatement.setShort(1, uid);
            ResultSet r = preparedStatement.executeQuery();
            List<Course> registeredCourses = new ArrayList<>();
            while (r.next()){
                short kid = r.getShort(2);
                registeredCourses.add(courseStore.getCourse(kid));
            }
            courseStore.complete();
            return registeredCourses;
        }
        catch(SQLException e){
            throw new StoreException(e);
        }
    }

    public List<Course> getNonRegisteredCourses(short uid) throws StoreException, IOException{

        try(CourseStore courseStore = new CourseStore()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT distinct e.kid FROM dbp019.einschreiben e " +
                    "WHERE e.kid not in (select e1.kid from dbp019.einschreiben e1 where e1.bnummer = ?)");
            // change sql to not =
            preparedStatement.setShort(1, uid);
            ResultSet r = preparedStatement.executeQuery();
            List<Course> nonRegisteredCourses = new ArrayList<>();
            while (r.next()){
                short kid = r.getShort(1);
                nonRegisteredCourses.add(courseStore.getCourse(kid));
            }
            courseStore.complete();
            return nonRegisteredCourses;
        }
        catch(SQLException e){
            throw new StoreException(e);
        }
    }

//    public void addCid(Course course) throws StoreException{
//        try{
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("SELECT * FROM dbp019.kurs WHERE name = ? AND beschreibungstext = ? " +
//                            "AND einschreibeschluessel = ? AND freiePlaetze = ? AND ersteller = ?");
//            preparedStatement.setString(1, course.getName());
//            preparedStatement.setString(2, course.getDescription());
//            preparedStatement.setString(3, course.getPasswort());
//            preparedStatement.setShort(4, course.getFreeplace());
//            preparedStatement.setShort(5, course.getCid());
//            ResultSet r = preparedStatement.executeQuery();
//            if(r.next()){
//                course.setKid(r.getShort(1));
//            }
//        }
//        catch (SQLException e){
//            throw new StoreException(e);
//        }
//    }





    public void complete() {
        complete = true;
    }


    @Override
    public void close() throws IOException {
        if (connection != null) {
            try {
                if (complete) {
                    connection.commit();
                }
                else {
                    connection.rollback();
                }
            }
            catch (SQLException e) {
                throw new StoreException(e);
            }
            finally {
                try {
                    connection.close();
                }
                catch (SQLException e) {
                    throw new StoreException(e);
                }
            }
        }
    }

}

