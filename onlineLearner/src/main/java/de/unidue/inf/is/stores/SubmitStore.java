package de.unidue.inf.is.stores;

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



//    public void addSubmission(User user, Submission submission) throws StoreException {
//
//        try {
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("insert into dbp019.einreichen (bnummer, kid, anummer," +
//                            " aid) values (?, ?, ?, ?)");
//            preparedStatement.setShort(1, user.getUid());
//            preparedStatement.setShort(2, submission.getAssignment().getKid());
//            preparedStatement.setShort(3, submission.getAssignment().getAid());
//            preparedStatement.setShort(4, submission.getsid());
//            preparedStatement.executeUpdate();
//        } catch (SQLException e) {
//            throw new StoreException(e);
//        }
//
//    }

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