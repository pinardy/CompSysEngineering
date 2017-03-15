import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class ParseFile {
    // this method generates a ProcessGraph and store in ProcessGraph Class
    public static void generateGraph(File inputFile) {
        try {
            // Find out the number of nodes and add the nodes to the graph
            int numOfNodes = CalculateNodes(inputFile);
            for (int i = 0; i < numOfNodes; i++) {
                ProcessGraph.addNode(i);
            }

            BufferedReader br = new BufferedReader(new FileReader(inputFile));
            int index = 0;
            String line = br.readLine();

            while (line != null) {
                String[] quartiles = line.split(":");
                if (quartiles.length != 4) {
                    System.out.println("Wrong input format!");
                    throw new Exception();
                }

                // handle Children
                if (!quartiles[1].equals("none")) {
                    String[] childrenStringArray = quartiles[1].split(" ");
                    int[] childrenId = new int[childrenStringArray.length];
                    for (int i = 0; i < childrenId.length; i++) {
                        childrenId[i] = Integer.parseInt(childrenStringArray[i]);
                        ProcessGraph.nodes.get(index).addChild(ProcessGraph.nodes.get(childrenId[i]));
                    }
                }

                // setup command
                ProcessGraph.nodes.get(index).setCommand(quartiles[0]);
                // setup input
                ProcessGraph.nodes.get(index).setInputFile(new File(quartiles[2]));
                // setup output
                ProcessGraph.nodes.get(index).setOutputFile(new File(quartiles[3]));
                // setup parent
                for (ProcessGraphNode node : ProcessGraph.nodes) {
                    for (ProcessGraphNode childNode : node.getChildren()) {
                        ProcessGraph.nodes.get(childNode.getNodeId()).addParent(ProcessGraph.nodes.get(node.getNodeId()));
                    }
                }

                // mark initial runnable
                for (ProcessGraphNode node : ProcessGraph.nodes) {
                    if (node.getParents().isEmpty()) {
                        node.setRunnable();
                    }
                }

                line = br.readLine();
                index++;
            }
        } catch (Exception e) {
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }

    // As each line corresponds to a node, this method calculates how many nodes there are based on the number of lines
    public static int CalculateNodes(File inputFile) throws IOException {
        int noOfLines = 0;
        BufferedReader br = new BufferedReader(new FileReader(inputFile));
        try {
            String line = br.readLine();

            // if there is a line, we increment the line count
            while (line != null) {
                noOfLines += 1;
                line = br.readLine();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        br.close();
        return noOfLines;
    }
}

