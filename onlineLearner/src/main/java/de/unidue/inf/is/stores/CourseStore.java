package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class CourseStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public CourseStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }



    public void addCourse(Course course) throws StoreException {

            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into dbp019.kurs (name, beschreibungstext, einschreibeschluessel," +
                                " freiePlaetze, ersteller) values (?, ?, ?, ?, ?)");
                preparedStatement.setString(1, course.getName());
                preparedStatement.setString(2, course.getDescription());
                preparedStatement.setString(3, course.getPasswort());
                preparedStatement.setShort(4, course.getFreeplace());
                preparedStatement.setShort(5, course.getCid());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new StoreException(e);
            }

    }

    public void addCid(Course course) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.kurs WHERE name = ? AND beschreibungstext = ? " +
                            "AND einschreibeschluessel = ? AND freiePlaetze = ? AND ersteller = ?");
            preparedStatement.setString(1, course.getName());
            preparedStatement.setString(2, course.getDescription());
            preparedStatement.setString(3, course.getPasswort());
            preparedStatement.setShort(4, course.getFreeplace());
            preparedStatement.setShort(5, course.getCid());
            ResultSet r = preparedStatement.executeQuery();
            if(r.next()){
                course.setKid(r.getShort(1));
            }
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }

    public Course getCourse(short kid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.kurs WHERE kid = ?");
            preparedStatement.setShort(1, kid);
            ResultSet r = preparedStatement.executeQuery();
            Course course = null;
            if(r.next()){
                course =  new Course(kid, r.getString(2), r.getString(3), r.getString(4), r.getShort(5), r.getShort(6));
            }
            return course;
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }

    public void incrementFreePlace(short kid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("Update dbp019.kurs SET freiePlaetze = freiePlaetze + 1 WHERE kid = ?");
            preparedStatement.setShort(1, kid);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new StoreException(e);
        }
    }

    public void decrementFreePlace(short kid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("Update dbp019.kurs SET freiePlaetze = freiePlaetze - 1 WHERE kid = ?");
            preparedStatement.setShort(1, kid);
            preparedStatement.executeUpdate();
        }catch(SQLException e){
            throw new StoreException(e);
        }
    }




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

