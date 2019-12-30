package dao;

import bo.Directory;
import java.util.List;

public interface DirectoryDAO {
    public Directory getDirectoryById(int idDirectory);
    public List<Directory> getDirectoryList();

    public boolean crudDirectory(Directory directory, int queryId);
}
