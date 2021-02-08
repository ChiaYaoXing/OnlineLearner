package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Course;
import de.unidue.inf.is.domain.Submission;
import de.unidue.inf.is.domain.Submit;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;


public final class SubmitStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public SubmitStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }



    public boolean isSubmitted(short uid, short kid, short aid) throws StoreException {

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Select * from dbp019.einreichen WHERE bnummer = ? AND kid = ? AND anummer = ?");
            preparedStatement.setShort(1, uid);
            preparedStatement.setShort(2, kid);
            preparedStatement.setShort(3, aid);
            ResultSet r = preparedStatement.executeQuery();
            return r.next();
        } catch (SQLException e) {
            throw new StoreException(e);
        }

    }

    public void addSubmit(short uid, short kid, short aid, Submission submission) throws StoreException {

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("Insert into dbp019.einreichen(bnummer, kid, anummer, aid) values(?, ?, ?, ?)");
            preparedStatement.setShort(1, uid);
            preparedStatement.setShort(2, kid);
            preparedStatement.setShort(3, aid);
            preparedStatement.setShort(1, submission.getsid());
            preparedStatement.executeUpdate();
        } catch (SQLException e) {
            throw new StoreException(e);
        }

    }

    public Submit getRandomSubmit(short uid) throws StoreException, IOException{
        try(RegisterStore registerStore = new RegisterStore()){
            List<Course> courses = registerStore.getRegisteredCourses(uid);
            if(courses.size() == 0) return null;
            StringBuilder queryString = new StringBuilder();
            queryString.append("Select * FROM dbp019.einreichen WHERE bnummer != ? AND kid = ?");
            for(int i = courses.size();i > 1;--i ){
                queryString.append(" or kid = ?");
            }
            queryString.append(" order by rand() fetch first rows only");
            PreparedStatement preparedStatement = connection.prepareStatement(queryString.toString());
            preparedStatement.setShort(1, uid);

            int i = 2;
            for(Course c: courses){
                preparedStatement.setShort(i, c.getKid());
                i += 1;
            }
            ResultSet r = preparedStatement.executeQuery();
            Submit submit = null;
            if(r.next()){
                submit = new Submit(r.getShort(1), r.getShort(2), r.getShort(3), r.getShort(4));
            }
            return submit;

    }catch(SQLException e){
            throw new StoreException(e);
        }
    }

    public List<Submit> getSubmit(short uid, short kid) throws StoreException{
        try{
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("SELECT au.anummer, au.name, au.beschreibung, a.aid, a.abgabetext, b.note, b.komentar, b.bnummer FROM " +
//                            "((dbp019.einreichen e join dbp019.abgabe a on a.aid = e.aid) join dbp019.aufgabe au on " +
//                            "e.kid = au.kid AND e.anummer = au.anummer) join dbp019.bewerten b on " +
//                            "b.aid = b.aid WHERE e.bnummer = ? AND e.kid = ? order by au.anummer");
//            preparedStatement.setShort(1, uid);
//            preparedStatement.setShort(2, kid);
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.einreichen e WHERE e.bnummer = ? AND e.kid = ? order by e.anummer");
            preparedStatement.setShort(1, uid);
            preparedStatement.setShort(2, kid);
            ResultSet r = preparedStatement.executeQuery();
            List<Submit> submits = new ArrayList<>();
            while(r.next()){
                submits.add(new Submit(uid, kid, r.getShort(3), r.getShort(4)));
//                submissions.add(new Submission(r.getShort(4),r.getString(5), new Assignment(kid, r.getShort(1), r.getString(2), r.getString(3)), new Rate(r.getShort(8), r.getShort(4), r.getShort(6), r.getString(7))));
            }
            return submits;
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