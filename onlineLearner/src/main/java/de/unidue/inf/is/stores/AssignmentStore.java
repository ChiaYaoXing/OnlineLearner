package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Assignment;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class AssignmentStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public AssignmentStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }



//    public void addCourse(Course course) throws StoreException {
//
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("insert into dbp019.kurs (name, beschreibungstext, einschreibeschluessel," +
//                            " freiePlaetze, ersteller) values (?, ?, ?, ?, ?)");
//            preparedStatement.setString(1, course.getName());
//            preparedStatement.setString(2, course.getDescription());
//            preparedStatement.setString(3, course.getPasswort());
//            preparedStatement.setShort(4, course.getFreeplace());
//            preparedStatement.setShort(5, course.getCid());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new StoreException(e);
//        }
//
//    }

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

    public List<Assignment> getAssignment(short kid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.aufgabe a WHERE a.kid = ? order by a.anummer");
            preparedStatement.setShort(1, kid);
            ResultSet r = preparedStatement.executeQuery();
            List<Assignment> assignments = new ArrayList<>();
            while(r.next()){
                assignments.add(new Assignment(kid,r.getShort(2),r.getString(3),r.getString(4) ));
            }
            return assignments;
        }
        catch (SQLException e){
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

