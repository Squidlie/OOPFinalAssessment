package dao.mysql;

import bo.File;
import dao.FileDAO;

import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class FileDAOImpl extends MySQL implements FileDAO {

    @Override
    public File getFileById(int idFile) {
        Connect();
        File file = null;
        try {
            String sp = "{call GetFile(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);

            cStmt.setInt(1, GET_BY_ID);
            cStmt.setInt(2, idFile);
            ResultSet rs = cStmt.executeQuery();

            if(rs.next()) {
                file = HydrateFile(rs);
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return file;
    }

    @Override
    public List<File> getFileList() {
        Connect();
        List<File> fileList = new ArrayList<File>();
        try{
            String sp = "{call GetFile(?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);

            cStmt.setInt(1, GET_COLLECTION);
            cStmt.setInt(2, 0);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()) {
                fileList.add(HydrateFile(rs));
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return fileList;
    }

    @Override
    public boolean crudFile(File file, int queryId) {
        Connect();
        int id = 0;
        try{
            String sp = "{call ExecuteFile(?,?,?,?,?,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            if(queryId == INSERT){
                cStmt.setInt(1, INSERT);
                cStmt.setInt(2, 0);
                confirm = "New File Record Inserted - ";
            }
            else if(queryId == UPDATE){
                cStmt.setInt(1, UPDATE);
                cStmt.setInt(2, file.getIdFile());
                confirm = "File Record Updated - ";
            }
            else if(queryId == DELETE){
                cStmt.setInt(1, DELETE);
                cStmt.setInt(2, file.getIdFile());
                confirm =  "File Record Deleted - ";
            }
            else {
                return false;
            }
            cStmt.setString(3, file.getFileName());
            cStmt.setString(4, file.getFileType());
            cStmt.setFloat(5, file.getFileSize());
            cStmt.setString(6, file.getPath());
            ResultSet rs = cStmt.executeQuery();
            if (rs.next()){
                id = rs.getInt(1);
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return id > 0;
    }

    public static File HydrateFile(ResultSet rs) throws SQLException{
        File file = new File();
        file.setIdFile(rs.getInt(1));
        file.setFileName(rs.getString(2));
        file.setFileType(rs.getString(3));
        file.setFileSize(rs.getFloat(4));
        file.setPath(rs.getString(5));

        return file;
    }
}
