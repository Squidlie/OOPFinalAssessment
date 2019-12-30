package console;

import bo.Directory;
import dao.DirectoryDAO;
import dao.FileDAO;
import dao.mysql.DirectoryDAOImpl;
import dao.mysql.FileDAOImpl;
import dao.mysql.MySQL;
import org.apache.log4j.Logger;
import java.io.File;
import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Scanner;

import static dao.mysql.DirectoryDAOImpl.HydrateDirectory;
import static dao.mysql.FileDAOImpl.HydrateFile;

public class Main extends MySQL{

    final static Logger logger = Logger.getLogger(MySQL.class);

    public static void main(String[] args){
        javaOOPFinalAssessment();
    }

    private static void javaOOPFinalAssessment(){
        deleteFromDatabase();
        System.out.println("==============================");
        System.out.println("==     Input Directory      ==");
        System.out.println("==============================");
        Scanner scanner = new Scanner(System.in);
        String firstInput = scanner.nextLine();
        RecursiveFileScan(new File (firstInput));
        MenuSelection();
    }

    private static void MenuSelection(){
        Scanner scanner = new Scanner(System.in);
        String selection = null;
        System.out.println("============================================");
        System.out.println("==            Make A Selection:           ==");
        System.out.println("============================================");
        System.out.println("== 1) Display Directory with most files   ==");
        System.out.println("== 2) Display Directory largest in size   ==");
        System.out.println("== 3) Display 5 largest files in size     ==");
        System.out.println("== 4) Display all Files of a certain type ==");
        System.out.println("== 5) Clear the database and start over   ==");
        System.out.println("== 6) Exit                                ==");
        System.out.println("============================================");
        selection = scanner.nextLine();
        switch (selection) {
            case "1":
                Directory directoryOutput = DirectoryWithMostFiles();
                System.out.println("Directory with most Files:  ");
                System.out.println("Name:                   " + directoryOutput.getDirName());
                System.out.println("Number of Files:        " + directoryOutput.getNumberOfFiles());
                System.out.println("Path:                   " + directoryOutput.getPath());
                Pause();
                MenuSelection();
                break;
            case "2":
                Directory directoryOutput2 = DirectoryLargest();
                System.out.println("Directory with Largest Size:  ");
                System.out.println("Name:              " + directoryOutput2.getDirName());
                System.out.println("Size:              " + directoryOutput2.getDirSize() + " (MB)");
                System.out.println("Path:              " + directoryOutput2.getPath());
                Pause();
                MenuSelection();
                break;
            case "3":
                List<bo.File> fileListOutput = fileLargestFive();
                System.out.println("Five Largest Files:");
                    for (bo.File file : fileListOutput){
                        System.out.println(file.getFileName());
                    }
                Pause();
                MenuSelection();
                break;
            case "4":
                System.out.print("Input file type: ");
                HashSet<String> fileTypeSet = new HashSet<>();
                FileDAO fileDAO = new FileDAOImpl();
                List<bo.File> allFiles = fileDAO.getFileList();
                for (bo.File file : allFiles) {
                    fileTypeSet.add(file.getFileType());
                    }
                System.out.println(fileTypeSet);
                String typeInput = scanner.nextLine();
                List<bo.File> resultList = filesByType(typeInput);
                for(bo.File singleFile : resultList){
                    System.out.println(singleFile.getFileName());
                }
                Pause();
                MenuSelection();
                break;
            case "5":
                System.out.println("Deleting from Database...");
                deleteFromDatabase();
                System.out.println("Deleted");
                Pause();
                javaOOPFinalAssessment();
                break;
            case "6":
                break;
            default:
                System.out.println("Invalid Input");
                Pause();
                MenuSelection();
                break;
        }
    }

    private static void RecursiveFileScan(File dir){
        try {
            File[] files = dir.listFiles();
            int i = 1;
            int j = 1;
            String extension = "";
            Directory dirScan = new Directory();
            bo.File fileScan = new bo.File();
            for (File file : files) {
                if (file.isDirectory()) {
                    dirScan.setIdDirectory(i);
                    dirScan.setDirName(file.getName());
                    dirScan.setDirSize(folderSize(file)/1048576);
                    dirScan.setNumberOfFiles(file.list().length);
                    dirScan.setPath(file.getCanonicalPath());
                    i = i + 1;
                    DirectoryDAO directoryDAO = new DirectoryDAOImpl();
                    boolean id = directoryDAO.crudDirectory(dirScan, INSERT);
                    logger.error(confirm + id);
                    RecursiveFileScan(file);
                } else {
                    fileScan.setIdFile(j);
                    fileScan.setFileName(file.getName());
                    fileScan.setFileSize(file.length());
                    int k = file.getName().lastIndexOf('.');
                    if (k > 0) {
                        extension = file.getName().substring(k+1); }
                    fileScan.setFileType(extension);
                    fileScan.setPath(file.getCanonicalPath());
                    j = j + 1;
                    FileDAO fileDAO = new FileDAOImpl();
                    boolean id = fileDAO.crudFile(fileScan, INSERT);
                    logger.error(confirm + id);
                }
            }
        } catch (IOException ioEx){
            logger.error(ioEx);
        }
    }

    public static double folderSize(File directory) {
        double length = 0;
        for (File file : directory.listFiles()) {
            if (file.isFile())
                length += file.length();
            else
                length += folderSize(file);
        }
        return length;
    }

    private static Directory DirectoryWithMostFiles(){
    Connect();
    Directory directoryMost = null;
      try {
        String sp = "{call GetDirectory(100, 0)}";
        CallableStatement cStmt = connection.prepareCall(sp);

        ResultSet rs = cStmt.executeQuery();

        if(rs.next()) {
            directoryMost = HydrateDirectory(rs);
        }
    } catch (SQLException sqlEx){
        logger.error(sqlEx);
    }
            return directoryMost;
    }

    private static Directory DirectoryLargest(){
        Connect();
        Directory directoryLargest = null;
        try {
            String sp = "{call GetDirectory(200,0)}";
            CallableStatement cStmt = connection.prepareCall(sp);

            ResultSet rs = cStmt.executeQuery();

            if(rs.next()) {
                directoryLargest = HydrateDirectory(rs);
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return directoryLargest;
    }

    private static List<bo.File> fileLargestFive(){
        Connect();
        List<bo.File> fileLargest = new ArrayList<>();
        try {
            String sp = "{call GetFile(100, 0)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()) {
                fileLargest.add(HydrateFile(rs));
            }
        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return fileLargest;
    }

    private static List<bo.File> filesByType(String fileType){
        Connect();
        List<bo.File> fileListByType = new ArrayList<>();
        try {
            String sp = "{call GetFileByType(10,?)}";
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.setString(1, fileType);
            ResultSet rs = cStmt.executeQuery();

            while(rs.next()){
                fileListByType.add(HydrateFile(rs));
            }

        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
        return fileListByType;
    }

    private static void deleteFromDatabase(){
        Connect();
        try{
            String sp = "{call ExecuteDirectory (100, 0, '', '', 0, '')}";
            String sp2 = "{call ExecuteFile(100, 0, '', '', '', '')}";
            CallableStatement cStmt2 = connection.prepareCall(sp2);
            CallableStatement cStmt = connection.prepareCall(sp);
            cStmt.executeQuery();
            cStmt2.executeQuery();

        } catch (SQLException sqlEx){
            logger.error(sqlEx);
        }
    }

    private static void Pause(){
        try{
            Thread.sleep(2000);
        } catch (InterruptedException iExc){
            logger.error(iExc);
        }
    }
}
