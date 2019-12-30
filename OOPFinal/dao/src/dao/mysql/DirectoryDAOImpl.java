package dao.mysql;

import bo.Directory;
import dao.DirectoryDAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class DirectoryDAOImpl extends MySQL implements DirectoryDAO {

    @Override
    public Directory getDirectoryById(int idDirectory) {
        Connect();
        Directory directory = null;
        try {
            String sp = "{call GetDirectory(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);

            cStmt.setInt(1, GET_BY_ID);
            cStmt.setInt(2, idDirectory);
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()) {
                directory = HydrateDirectory(rs);
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return directory;
    }

    @Override
    public List<Directory> getDirectoryList() {
        Connect();
        List<Directory> directoryList = new ArrayList<Directory>();
        try{
            String sp = "{call GetDirectory(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);

            cStmt.setInt(1, GET_COLLECTION);
            cStmt.setInt(2, 0);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()) {
                directoryList.add(HydrateDirectory(rs));
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return directoryList;
    }

    public boolean crudDirectory(Directory directory, int queryId){
        Connect();
        int id = 0;
        try{
            String sp = "{call ExecuteDirectory(?,?,?,?,?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            if(queryId == INSERT){
                cStmt.setInt(1, INSERT);
                cStmt.setInt(2, 0);
                confirm = "New Directory Record Inserted - ";
            }
            else if(queryId == UPDATE){
                cStmt.setInt(1, UPDATE);
                cStmt.setInt(2, directory.getIdDirectory());
                confirm = "Directory Record Updated - ";
            }
            else if(queryId == DELETE){
                cStmt.setInt(1, DELETE);
                cStmt.setInt(2, directory.getIdDirectory());
                confirm =  "Directory Record Deleted - ";
            }
            else {
                return false;
            }
            cStmt.setString(3, directory.getDirName());
            cStmt.setDouble(4, directory.getDirSize());
            cStmt.setInt(5, directory.getNumberOfFiles());
            cStmt.setString(6, directory.getPath());
            ResultSet rs = cStmt.executeQuery();
            if (rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return id > 0;
    }


    public static Directory HydrateDirectory(ResultSet rs) throws SQLException{
        Directory directory = new Directory();
        directory.setIdDirectory(rs.getInt(1));
        directory.setDirName(rs.getString(2));
        directory.setDirSize(rs.getFloat(3));
        directory.setNumberOfFiles(rs.getInt(4));
        directory.setPath(rs.getString(5));

        return directory;
    }
}
