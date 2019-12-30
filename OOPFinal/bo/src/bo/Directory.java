package bo;

public class Directory {
    private int idDirectory;
    private String dirName;
    private double dirSize;
    private int numberOfFiles;
    private String path;


    public int getIdDirectory() {
        return idDirectory;
    }
    public void setIdDirectory(int idDirectory) {
        this.idDirectory = idDirectory;
    }

    public String getDirName() {
        return dirName;
    }
    public void setDirName(String dirName) {
        this.dirName = dirName;
    }

    public double getDirSize() {
        return dirSize;
    }
    public void setDirSize(double dirSize) {
        this.dirSize = dirSize;
    }

    public int getNumberOfFiles() {
        return numberOfFiles;
    }
    public void setNumberOfFiles(int numberOfFiles) {
        this.numberOfFiles = numberOfFiles;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
