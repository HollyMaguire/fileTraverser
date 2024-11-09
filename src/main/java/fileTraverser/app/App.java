package fileTraverser.app;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Hello world!
 */
public class App {
    public static void main(String[] args) {
        List<String> directories = listAllFiles(args[0]);
        if(args.length == 1){
            writeDates(directories);
        }
        else{
            String[] actions = Arrays.copyOfRange(args, 1, args.length);
            for (String action : actions) {
                try {
                    // Try to find the method with the name that matches the argument
                    Method method = App.class.getMethod(action, List.class);
                    // Call the method
                    method.invoke(null, directories);  // 'null' since we're calling a static method
                } catch (Exception e) {
                    System.out.println("No such function: " + action);
                    e.printStackTrace();
                }
            }
        }
    }

    public static void writeDates(List<String> filePaths) {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        
        try (FileWriter writer = new FileWriter("file_list.txt")) {
            for (String path : filePaths) {
                File file = new File(path);
                
                if (file.exists()) {
                    String lastModified = sdf.format(file.lastModified());
                    String line = file.getAbsolutePath() + "," + lastModified;
                    writer.write(line + "\n");
                } else {
                    System.out.println("File: " + path + " does not exist.");
                }
            }
            System.out.println("File list with last modified dates has been written to: file_list.txt");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void countLines(List<String> filePaths) {
        int totalLines = 0;
        String outputFilePath = "total_lines.txt";
        // Loop through each file path in the list
        for (String filePath : filePaths) {
            File file = new File(filePath);

            // Check if the file exists and is a file (not a directory)
            if (file.exists() && file.isFile()) {
                try (BufferedReader reader = new BufferedReader(new FileReader(file))) {
                    String line;
                    // Read each line in the file
                    while ((line = reader.readLine()) != null) {
                        totalLines++;
                    }
                } catch (IOException e) {
                    System.err.println("Error reading file: " + filePath);
                    e.printStackTrace();
                }
            } else {
                System.out.println("Skipping invalid file: " + filePath);
            }
        }

        // Write the total line count to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write("Total lines in all files: " + totalLines);
            System.out.println("Total line count written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + outputFilePath);
            e.printStackTrace();
        }
    }

    public static List<String> listAllFiles(String directoryPath) {
        List<String> fileList = new ArrayList<>();
        File directory = new File(directoryPath);

        if (directory.exists() && directory.isDirectory()) {
            addFilesRecursively(directory, fileList);
        } else {
            System.out.println("The specified path is not a directory or does not exist.");
        }

        return fileList;
    }

    private static void addFilesRecursively(File directory, List<String> fileList) {
        File[] files = directory.listFiles();

        if (files != null) {
            for (File file : files) {
                if (file.isDirectory()) {
                    // Recursive call for subdirectory
                    addFilesRecursively(file, fileList);
                } else {
                    // Add file path to the list
                    fileList.add(file.getAbsolutePath());
                }
            }
        }
    }

    public static void calculateAverageFileSize(List<String> fileNames) {
        long totalSize = 0;
        int fileCount = 0;
        String outputFilePath = "average_file_size.txt";
        // Loop through each file name in the list
        for (String fileName : fileNames) {
            File file = new File(fileName);

            // Check if the file exists and is a file (not a directory)
            if (file.exists() && file.isFile()) {
                totalSize += file.length();
                fileCount++;
            } else {
                System.out.println("Skipping invalid file: " + fileName);
            }
        }

        // If no valid files were found, handle the error and write a message
        String resultMessage;
        if (fileCount == 0) {
            resultMessage = "No valid files found.";
        } else {
            // Calculate the average size
            double averageSize = (double) totalSize / fileCount;
            resultMessage = "Average file size: " + averageSize + " bytes.";
        }

        // Write the result to the output file
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFilePath))) {
            writer.write(resultMessage);
            System.out.println("Average file size written to: " + outputFilePath);
        } catch (IOException e) {
            System.err.println("Error writing to output file: " + outputFilePath);
            e.printStackTrace();
        }
    }


}
