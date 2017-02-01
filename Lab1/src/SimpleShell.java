import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** STEPS:
  1. Parse the input to obtain the command (and any parameters)
  2. Create a ProcessBuilder object
  3. Start the process
  4. Obtain the output stream
  5. Output contents returned by the command
 */

public class SimpleShell {

public static void main(String[] args) throws java.io.IOException{
		
		String commandLine;
		BufferedReader console = new BufferedReader
				(new InputStreamReader(System.in));
		
		ProcessBuilder pb = new ProcessBuilder();
		ArrayList<String> history = new ArrayList<String>();

		// Break out with <ctrl c>
		while(true){
			// read what the user enters
			System.out.print("jsh>");
			commandLine = console.readLine();
			
			// input parsed into array of strings(commands)
			String[] commands = commandLine.split(" ");
			ArrayList<String> list = new ArrayList<String>();
			
			
			// loop through to see if parsing worked
			for(int i = 0; i < commands.length; i++){
//				System.out.println(commands[i]); // check to see if parsing/split works
				list.add(commands[i]);
			}

//			System.out.print(list); // check to see if list was added correctly
            if (isInteger(commandLine)) {
			    try {
                    history.add(history.get(Integer.parseInt(commandLine)));
                } catch (IndexOutOfBoundsException e){
                    System.out.println("Invalid command history search!");
                }
            } else {
                history.add(commandLine);
            }


			try{
				// display history of shell with index
				if(list.get(list.size()-1).equals("history")){

				    //this removes the command history from the list
                    history.remove(history.size()-1);

                    // Loop through history list
                    for (int i = 0; i < history.size(); i++) {
                        System.out.println(i + " " + history.get(i));
                    }
                    continue;
				}

			    // change directory
                if(list.contains("cd")) {
                    File home = new File(System.getProperty("user.home"));


                    if(list.get(list.size()-1).equals("cd")){
                        //test to see what user.home changes to
                        System.out.println("The home directory is " + home);
                        pb.directory(home);
                        continue;
                    }

                    // if user enters "cd .."
                     else if(list.get(list.size()-1).equals("..")) {
                        String currentDirectory = System.getProperty("user.dir");
                        File currentFile = new File(currentDirectory);
                        File parent = currentFile.getParentFile();

                        // test to see directories (comment if not needed)
    //                    System.out.println("-------------------------");
    //                    System.out.println("home: " + home);
    //                    System.out.println("Current directory:  " + currentDirectory);
    //                    System.out.println("Parent directory: " + parent);
    //                    System.out.println("-------------------------" + "\n");


                        // does the parent exist?
                        boolean parentExists = parent.isDirectory();

                        if (parentExists) {
                            System.out.println(parent);
                            pb.directory(parent);
                            continue;
                        } else {   		// if parent doesn't exist
                            System.out.print("Path ");
                        }
                    }

                    else{
                        String dir = list.get(1);
                        File currentFile2 = new File(pb.directory() + File.separator + dir);

                        // test to see what directory was passed (comment if not needed)
    //					System.out.println("-------------------------");
    //					System.out.println("The directory passed is " + dir + "\n");
    //					System.out.println("home: " + home);
    //					System.out.println("current directory: " + currentFile2);
    //					System.out.println("-------------------------" + "\n");

                        // does the directory exist?
                        boolean dirExists = currentFile2.isDirectory();

                        if(dirExists){
                        System.out.println("/" + dir); //added the "/" for cleaner output
                        pb.directory(currentFile2);
                        continue;
                        }	else{   		//if directory doesn't exist
                            System.out.print("Path ");
                        }
                    }
                }


                // !! command returns the last command in history
                if(list.get(list.size()-1).equals("!!")){
    //                System.out.println("Previous command: " + history.get(history.size()-2));

                    // try and see if there is a previous command
                    try {
                        // if there is a previous command
    //                    System.out.println(history.get(history.size() - 2));
                        pb.command(history.get(history.size() - 2));
                        history.remove(history.size()-1);
                    } catch (Exception e){
                        // if no previous command
                        System.out.println("No previous command found");
                    }
                }

                // <integer value i> command
                else if (isInteger(commandLine)) {
                    int index = Character.getNumericValue(list.get(list.size() - 1).charAt(0));
                    // if integer entered isn't bigger than history size
                    if (index <= history.size()) {
                        pb.command(history.get(index));
                    }
                    //TODO: handle ArrayIndexOutOfBoundsException
                    // if integer is bigger than history size
                    else {
                        pb.command(list);
                    }
                }

                else{
                    pb.command(list);
                }


                try {
                    Process process = pb.start();


                //obtain the input stream
                InputStream is = process.getInputStream();
                InputStreamReader isr = new InputStreamReader(is);
                BufferedReader br = new BufferedReader(isr);

                //read output of the process
                String line;
                while((line = br.readLine()) != null)
                    System.out.println(line);
                br.close();


                // if the user entered a return, just loop again
                if (commandLine.equals(" ")) {
                    continue;
                }

                } catch (ArrayIndexOutOfBoundsException e){
                    // do nothing
                }

			}

			// catches IOException, output appropriate message, resume waiting for input
			catch (IOException e){
				System.out.println("Input Error, Please try again!");
			}
		}

	}

	// this method is used for Q3.3
	public static boolean isInteger(String s) {
        try {
            Integer.parseInt(s);
        } catch (NumberFormatException e) {
            return false;
        } catch (NullPointerException e) {
            return false;
        }
        return true;
    }

}
