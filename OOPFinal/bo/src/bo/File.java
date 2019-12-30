package bo;

public class File extends Directory{
    private int idFile;
    private String fileName;
    private String fileType;
    private float fileSize;
    private  String path;


    public int getIdFile() {
        return idFile;
    }
    public void setIdFile(int idFile) {
        this.idFile = idFile;
    }

    public String getFileName() {
        return fileName;
    }
    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getFileType() {
        return fileType;
    }
    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public float getFileSize() {
        return fileSize;
    }
    public void setFileSize(float fileSize) {
        this.fileSize = fileSize;
    }

    public String getPath() {
        return path;
    }
    public void setPath(String path) {
        this.path = path;
    }
}
