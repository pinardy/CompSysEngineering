/* Programming Assignment 1 
 * Authors : Pinardy Yang (1001520), Loh Wei Quan (1001505)
 * Date: 07/03/2017 */

-=-=-Purpose of program -=-=-

The purpose of this program is to construct a directed acyclic graph (DAG) of 
user programs from an input text file. The user will then traverse through this 
DAG and execute the processes which have control and data dependencies between
each other.



-=-=- How to compile program -=-=-

1) Download the code and put it in your favourite folder

2) In ProcessManagement.java, change the file path for currentDirectory to 
where you placed the code in. For example, the file path name could be 
"C:\Pinardy\SUTD\Term_5\50.005 - Computer Systems Engineering\ProgAssignment1\src"

3) In ProcessManagement.java, change the file path for instructionSet to whichever file
that you want to use for the test cases. 
These test cases include:
-> graph-file.txt
-> graph-file1.txt

4a) For IDE:
Click run under ProcessManagement.java to start the program.

4b) For Linux shell:
- Change directory to where you placed the program using the "cd" command
For example:
$ cd "/home/osboxes/Documents/ProgAssignment1/src"

- Compile the java file using the following command:
$ javac ProcessManagement.java

- Run the java file using the following command:
$ java ProcessManagement



-=-=-What exactly does the program do? -=-=-

1) The user parses in a file to generate a ProcessGraph

2) Information of the ProcessGraph is displayed to the user

3) Each line in the input file represents a node. The line is of format:
<program name with arguments :list of children ID's : input file : output file>

4) We recursively check each node to see if they have been executed. While all the
nodes are still not executed, we check if the nodes are runnable. 

5) Execute the runnable nodes and repeat step 4 until all nodes are executed.



