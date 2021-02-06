package de.unidue.inf.is.stores;

import de.unidue.inf.is.domain.User;
import de.unidue.inf.is.utils.DBUtil;

import java.io.Closeable;
import java.io.IOException;
import java.sql.*;


public final class UserStore implements Closeable {

    private Connection connection;
    private boolean complete;


    public UserStore() throws StoreException {
        try {
            connection = DBUtil.getExternalConnection();
            connection.setAutoCommit(false);
        }
        catch (SQLException e) {
            throw new StoreException(e);
        }
    }

    public boolean exists(User user) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.benutzer WHERE email = ? AND name = ?");
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(1, user.getEmail());
            ResultSet r = preparedStatement.executeQuery();
            boolean res = r.next();
            return res;
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }


    public void addUser(User userToAdd) throws StoreException {
        if(!exists(userToAdd)) {
            try {
                PreparedStatement preparedStatement = connection
                        .prepareStatement("insert into dbp019.benutzer (email, name) values (?, ?)");
                preparedStatement.setString(2, userToAdd.getName());
                preparedStatement.setString(1, userToAdd.getEmail());
                preparedStatement.executeUpdate();
            } catch (SQLException e) {
                throw new StoreException(e);
            }
        }

    }

//    public User getUser(User user) throws StoreException{
//        try{
//            PreparedStatement preparedStatement = connection
//                    .prepareStatement("SELECT * FROM dbp019.benutzer WHERE email = ? AND name = ?");
//            preparedStatement.setString(2, user.getName());
//            preparedStatement.setString(1, user.getEmail());
//            ResultSet r = preparedStatement.executeQuery();
//            User returnUser = user;
//            if(r.next()){
//                returnUser = new User(r.getString(2),r.getString(3), r.getShort(1));
//            }
//            return returnUser;
//        }
//        catch (SQLException e){
//            throw new StoreException(e);
//        }
//    }

    public void addUid(User user) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.benutzer WHERE email = ? AND name = ?");
            preparedStatement.setString(2, user.getName());
            preparedStatement.setString(1, user.getEmail());
            ResultSet r = preparedStatement.executeQuery();
            if(r.next()){
                user.setUid(r.getShort(1));
            }
        }
        catch (SQLException e){
            throw new StoreException(e);
        }
    }

    public User getUser(short uid) throws StoreException{
        try{
            PreparedStatement preparedStatement = connection
                    .prepareStatement("SELECT * FROM dbp019.benutzer WHERE bnummer = ?");
            preparedStatement.setShort(1, uid);
            ResultSet r = preparedStatement.executeQuery();
            User user =null;
            if(r.next()){
                user = new User(r.getString(3), r.getString(2), r.getShort(1));
            }
            return user;
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
