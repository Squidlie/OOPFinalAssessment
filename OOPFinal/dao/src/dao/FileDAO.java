package dao;

import bo.File;
import java.util.List;

public interface FileDAO {
    public File getFileById(int idFile);
    public List<File> getFileList();

    public boolean crudFile(File file, int queryId);
}
