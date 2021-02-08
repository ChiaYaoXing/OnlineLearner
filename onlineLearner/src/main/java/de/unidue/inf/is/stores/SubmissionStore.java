package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.Submission;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


public final class SubmissionStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public SubmissionStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }



    public Submission addSubmission(String text) throws StoreException {

        try {
            PreparedStatement preparedStatement = connection
                    .prepareStatement("insert into dbp019.abgabe(abgabetext) values(?)");
            preparedStatement.setString(1, text);
            preparedStatement.executeUpdate();
            return addsid(text);

        } catch (SQLException e) {
            throw new StoreException(e);
        }

    }

    public Submission addsid(String text) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.abgabe WHERE abgabetext = ?");
            preparedStatement.setString(1, text);

            ResultSet r = preparedStatement.executeQuery();
            Submission submission = null;
            if(r.next()){
                submission = new Submission(r.getShort(1), text);
            }
            return submission;
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }

    public Submission getSubmission(short sid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.abgabe WHERE aid = ?");
            preparedStatement.setShort(1, sid);
            ResultSet r = preparedStatement.executeQuery();
            Submission submission = null;
            if(r.next()){
                submission =  new Submission(sid, r.getString(2));
            }
            return submission;
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
