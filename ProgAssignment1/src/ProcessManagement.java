import java.io.File;

public class ProcessManagement {

    // set the working directory
    private static File currentDirectory = new File("C:\\Pinardy\\SUTD\\Term_5\\50.005 - Computer Systems Engineering\\ProgAssignment1\\ProgAssignment1\\src/");
    // set the instructions file
    private static File instructionSet = new File("testproc.txt");
    public static Object lock = new Object();

    public static void main(String[] args) throws InterruptedException {

        // parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/"+instructionSet));

        // Print the graph information
	// WRITE YOUR CODE


        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
                // check if all the nodes are executed
                // WRITE YOUR CODE

                //mark all the runnable nodes
	        // WRITE YOUR CODE

                //run the node if it is runnable
	        // WRITE YOUR CODE

        System.out.println("All process finished successfully");
    }

}