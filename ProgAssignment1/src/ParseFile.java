import java.io.File;
import java.util.Scanner;

public class ParseFile {

    /*  -------- Sample test file --------
        sleep 10:1:stdin:stdout
        echo "Process P1 running. Dependency to P4 is cleared.":4:stdin:out1.txt
        --------------------------------
     */

    // this method generates a ProcessGraph and store in ProcessGraph Class
    public static void generateGraph(File inputFile) {
        try{
            Scanner fileIn = new Scanner(inputFile);
            int index = 0;
            while(fileIn.hasNext()){
                String line = fileIn.nextLine();
                String[] quartiles = line.split(":");
                if (quartiles.length != 4) {
                    System.out.println("Wrong input format!");
                    throw new Exception();
                }

                // add this node
                ProcessGraph.addNode(index);
                // handle Children
                if (!quartiles[1].equals("none")){
                    String[] childrenStringArray = quartiles[1].split(" ");
                    int[] childrenId=new int[childrenStringArray.length];
                    for (int i = 0; i < childrenId.length; i++) {
                        childrenId[i] = Integer.parseInt(childrenStringArray[i]);
                        ProcessGraph.addNode(childrenId[i]);
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
                for (ProcessGraphNode node:ProcessGraph.nodes) {
                    if (node.getParents().isEmpty()){
                        node.setRunnable();
                    }
                }

                index++;
            }
        } catch (Exception e){
            System.out.println("File not found!");
            e.printStackTrace();
        }
    }


}