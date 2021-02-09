package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Rate;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class RateStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public RateStore() throws StoreException {
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

    public Rate getRate(short sid) throws StoreException{
        try{
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("SELECT au.anummer, au.name, au.beschreibung, a.aid, a.abgabetext, b.note, b.komentar, b.bnummer FROM " +
//                            "((dbp019.einreichen e join dbp019.abgabe a on a.aid = e.aid) join dbp019.aufgabe au on " +
//                            "e.kid = au.kid AND e.anummer = au.anummer) join dbp019.bewerten b on " +
//                            "b.aid = b.aid WHERE e.bnummer = ? AND e.kid = ? order by au.anummer");
//            preparedStatement.setShort(1, uid);
//            preparedStatement.setShort(2, kid);
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.bewerten b WHERE b.aid = ?");
            preparedStatement.setShort(1, sid);
            ResultSet r = preparedStatement.executeQuery();
            Rate rate = null;
            if(r.next()){
                rate = new Rate(r.getShort(1), sid, r.getShort(3), r.getString(4));
//                submissions.add(new Submission(r.getShort(4),r.getString(5), new Assignment(kid, r.getShort(1), r.getString(2), r.getString(3)), new Rate(r.getShort(8), r.getShort(4), r.getShort(6), r.getString(7))));
            }
            return rate;
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }

    public void addRate(Rate rate) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection.prepareStatement("Insert into dbp019.bewerten(bnummer, aid, note, kommentar) values(?, ?, ?, ?)");
            preparedStatement.setShort(1,rate.getUid());
            preparedStatement.setShort(2, rate.getSid());
            preparedStatement.setShort(3, rate.getScore());
            preparedStatement.setString(4, rate.getComment());
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