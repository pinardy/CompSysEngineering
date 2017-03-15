/* Programming Assignment 1
 * Authors : Pinardy Yang (1001520), Loh Wei Quan (1001505)
 * Date: 07/03/2017 */

import java.io.File;
import java.util.ArrayList;

public class ProcessManagement {

    // set the working directory
    private static File currentDirectory = new File("/home/osboxes/Documents/ProgAssignment1/src");

    // set the instructions file
    private static File instructionSet = new File("graph-file1.txt");

    private static int count;
    private static boolean allExecuted;
    private static ArrayList<ProcessGraphNode> parents;

    public static void main(String[] args) throws InterruptedException {
        // Parse the instruction file and construct a data structure, stored inside ProcessGraph class
        ParseFile.generateGraph(new File(currentDirectory + "/" + instructionSet));

        // Print the graph information
        ProcessGraph.printGraph();

        // Using index of ProcessGraph, loop through each ProcessGraphNode, to check whether it is ready to run
        try {
            ProcessBuilder pb = new ProcessBuilder();
	    pb.directory(currentDirectory);
            count = 0;
            allExecuted = false;
	System.out.println("Loading... Please be patient");
            // While all nodes are not executed, loop through the nodes
            while (allExecuted == false) {
                for (ProcessGraphNode node : ProcessGraph.nodes) {
                    if (node.isExecuted()) {
                        count++;
                    }
                    else {
                        parents = node.getParents();

                        // int i is a counter to keep track of the number of child processes executed.
                        int i = 0;
                        for (ProcessGraphNode parent : parents) {
                            if (parent.isExecuted()) {
                                i++;
                            }
                        }
                        // When all child processes are executed
                        if (i == parents.size()) {
                            // mark the node as runnable
                            node.setRunnable();
                            // execute runnable node
                            ExecuteProcess(pb, node);
                            // mark node as executed
                            node.setExecuted();
                            count++;
                        } else if (node.isRunnable()) {
                            ExecuteProcess(pb, node);
                            node.setExecuted();
                            count++;
                        }
                    }
                }
                // When all nodes are executed
                if (count == ProcessGraph.nodes.size()) {
                    allExecuted = true;
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        System.out.println("All process finished successfully");
    }

    public static void ExecuteProcess(ProcessBuilder pb, ProcessGraphNode node) {
        try {
            // Obtain string format of input and output
            String input = node.getInputFile().toString();
            String output = node.getOutputFile().toString();

            // Check if there is a need to redirect input/output
            if (!input.equals("stdin")) {
                pb.redirectInput(node.getInputFile());
            }
            if (!output.equals("stdout")) {
                pb.redirectOutput(node.getOutputFile());
            }
            // Obtain program and arguments
            String[] command = {"bash","-c",node.getCommand().toString()};
            pb.command(command);

            // Start process and wait for the process to finish before other processes can execute
            Process process = pb.start();
            process.waitFor();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
