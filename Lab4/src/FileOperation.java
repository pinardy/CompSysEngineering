import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Comparator;
import java.util.Date;

public class FileOperation {
    private static File currentDirectory = new File(System.getProperty("user.dir"));
    private static ArrayList<String> command;

    public static void main(String[] args) throws java.io.IOException {

        String commandLine;

        BufferedReader console = new BufferedReader
                (new InputStreamReader(System.in));

        while (true) {
            // read what the user entered
            System.out.print("jsh>");
            commandLine = console.readLine();

            // clear the space before and after the command line
            commandLine = commandLine.trim();

            // if the user entered a return, just loop again
            if (commandLine.equals("")) {
                continue;
            }
            // if exit or quit
            else if (commandLine.equalsIgnoreCase("exit") | commandLine.equalsIgnoreCase("quit")) {
                System.exit(0);
            }

            // check the command line, separate the words
            String[] commandStr = commandLine.split(" ");
            command = new ArrayList<>();
            for (int i = 0; i < commandStr.length; i++) {
                command.add(commandStr[i]);
            }


            try {
                String fileName = command.get(1);

                // --- create ---
                if (command.get(0).equals("create")) {
                    Java_create(currentDirectory, fileName);
                }
                // --- delete ---
                else if (command.get(0).equals("delete")) {
                    Java_delete(currentDirectory, fileName);
                }
                // --- display ---
                else if (command.get(0).equals("display")) {
                    Java_cat(currentDirectory, fileName);
                }

                //--- list ---
                else if (command.contains("list")) {
                    // if the input is only "list"
                    if (command.get(command.size() - 1).equals("list")) {
                        Java_ls(currentDirectory, "list", "");
                    }

                    // if the input is "list property"
                    else if (command.get(command.size() - 1).equals("property")) {
                        Java_ls(currentDirectory, "property", "");
                    }

                    // if the input is "list property name/size/time"
                    else if (command.get(0).equals("list") && command.get(1).equals("property") &&
                            command.get(command.size() - 1).equals("name")) {
                        Java_ls(currentDirectory, "property", "name");
                    } else if (command.get(0).equals("list") && command.get(1).equals("property") &&
                            command.get(command.size() - 1).equals("size")) {
                        Java_ls(currentDirectory, "property", "size");
                    } else if (command.get(0).equals("list") && command.get(1).equals("property") &&
                            command.get(command.size() - 1).equals("time")) {
                        Java_ls(currentDirectory, "property", "time");
                    }
                }

                // --- find ---
                else if (command.get(0).equals("find")) {
                    String target = command.get(1);
                    Java_find(currentDirectory, target);
                }

                // TODO: Q4) implement code to handle tree here
                // --- tree ---
                else if (command.get(0).equals("tree")) {

                }

//                else {
//                    //TODO: Other commands
//                    System.out.println("Other command entered");
//                }

            } catch (Exception e) {
                System.out.println("Command not recognised. Please try again");
            }


            // other commands
            ProcessBuilder pBuilder = new ProcessBuilder(command);
            pBuilder.directory(currentDirectory);
            try {
                Process process = pBuilder.start();
                // obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                // read what is returned by the command
                String line;
                while ((line = br.readLine()) != null)
                    System.out.println(line);

                // close BufferedReader
                br.close();
            }
            // catch the IOexception and resume waiting for commands
            catch (IOException ex) {
                System.out.println(ex);
                continue;
            }
        }
    }

    /**
     * Create a file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be created
     */
    public static void Java_create(File dir, String name) {
        // TODO: create a file
        File fileToCreate = new File(name);

        // if the file to be created does not exist
        if (!fileToCreate.exists()) {
            try {
                fileToCreate.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("Created new file");
        } else {
            System.out.println("File already exists");
        }

    }

    /**
     * Delete a file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be deleted
     */
    public static void Java_delete(File dir, String name) {
        File fileToDelete = new File(name);

        // if the file to be deleted exists
        if (fileToDelete.exists()) {
            fileToDelete.delete();
            System.out.println("Deleted file");
        } else {
            System.out.println("Cannot delete a non-existent file");
        }
    }

    /**
     * Display the file
     *
     * @param dir  - current working directory
     * @param name - name of the file to be displayed
     */
    public static void Java_cat(File dir, String name) {
        File fileToDisplay = new File(name);

        // if the file to be displayed exists
        if (fileToDisplay.exists()) {
            FileReader fr = null;
            try {
                fr = new FileReader(fileToDisplay);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
            BufferedReader br = new BufferedReader(fr);

            String line;
            try {
                while ((line = br.readLine()) != null) {
                    System.out.println(line);
                }
                br.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Cannot display a non-existent file");
        }
    }

    /**
     * Function to sort the file list
     *
     * @param list        - file list to be sorted
     * @param sort_method - control the sort type
     * @return sorted list - the sorted file list
     */
    private static File[] sortFileList(File[] list, String sort_method) {
        // sort the file list based on sort_method
        // if sort based on name
        if (sort_method.equalsIgnoreCase("name")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return (f1.getName()).compareTo(f2.getName());
                }
            });
        } else if (sort_method.equalsIgnoreCase("size")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.length()).compareTo(f2.length());
                }
            });
        } else if (sort_method.equalsIgnoreCase("time")) {
            Arrays.sort(list, new Comparator<File>() {
                public int compare(File f1, File f2) {
                    return Long.valueOf(f1.lastModified()).compareTo(f2.lastModified());
                }
            });
        }
        return list;
    }

    /**
     * List the files under directory
     *
     * @param dir            - current directory
     * @param display_method - control the list type
     * @param sort_method    - control the sort type
     */
    public static void Java_ls(File dir, String display_method, String sort_method) {
        if (display_method.equals("list")) {
            File[] list = dir.listFiles();
            list = sortFileList(list, sort_method);

            for (File file : list) {
                String name = file.getName();
                System.out.println(String.format("%s", name));
            }
        } else if ((display_method.equals("property")) && sort_method.equals("")) {
            File[] list = dir.listFiles();
            list = sortFileList(list, sort_method);

            for (File file : list) {
                String name = file.getName();
                String size = Long.toString(file.length());
                Date date = new Date(file.lastModified());
                System.out.println(String.format("%s", name) + "    Size: " + size
                        + "    Last Modified: " + date);
            }
        } else if ((display_method.equals("property")) && sort_method.equals("name")) {
            File[] list = dir.listFiles();
            list = sortFileList(list, sort_method);

            for (File file : list) {
                String name = file.getName();
                String size = Long.toString(file.length());
                Date date = new Date(file.lastModified());
                System.out.println(String.format("%s", name) + "    Size: " + size
                        + "    Last Modified: " + date);
            }
        } else if ((display_method.equals("property")) && sort_method.equals("size")) {
            File[] list = dir.listFiles();
            list = sortFileList(list, sort_method);

            for (File file : list) {
                String name = file.getName();
                String size = Long.toString(file.length());
                Date date = new Date(file.lastModified());
                System.out.println(String.format("%s", name) + "    Size: " + size
                        + "    Last Modified: " + date);
            }
        } else if ((display_method.equals("property")) && sort_method.equals("time")) {
            File[] list = dir.listFiles();
            list = sortFileList(list, sort_method);

            for (File file : list) {
                String name = file.getName();
                String size = Long.toString(file.length());
                Date date = new Date(file.lastModified());
                System.out.println(String.format("%s", name) + "    Size: " + size
                        + "    Last Modified: " + date);
            }
        }
    }

    /**
     * Find files based on input string
     *
     * @param dir  - current working directory
     * @param name - input string to find in file's name
     * @return flag - whether the input string is found in this directory and its subdirectories
     */
    public static boolean Java_find(File dir, String name) {
        boolean flag = false;
        File[] files = dir.listFiles();
        for (File file : files) {
            // perform recursion here if the file is a directory
            if (file.isDirectory()) {
                Java_find(file, name);
            } else {
                if (file.getAbsolutePath().contains(name)) {
                    System.out.println(file.getAbsolutePath());
                    flag = true;
                }
            }
        }
        return flag;
    }

    /**
     * Print file structure under current directory in a tree structure
     *
     * @param dir         - current working directory
     * @param depth       - maximum sub-level file to be displayed
     * @param sort_method - control the sort type
     */
    public static void Java_tree(File dir, int depth, String sort_method) {
        // TODO: print file tree
        if (depth != 0) {
            File[] listOfFiles = dir.listFiles();
            sortFileList(listOfFiles, sort_method);
            for (File file : listOfFiles) {
                // Checks all the files in the directory
                if (file.isDirectory()) {
                    // if the file is a directory, recurse call to go deeper into the directory
                    // But print out the name as well
                    System.out.println(file.getName());
                    System.out.println("|-" + file.getName());

                    Java_tree(file, depth - 1, sort_method);
                }
            }
        }

        // TODO: define other functions if necessary for the above functions
    }
}