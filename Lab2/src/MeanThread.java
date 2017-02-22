import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class MeanThread {

	public static void main(String[] args) throws InterruptedException, FileNotFoundException {
        // For IDE
//        String fileLocation = "C:\\Pinardy\\SUTD\\Term_5\\50.005 - Computer Systems Engineering\\Week 3\\Lab 2\\Lab2\\src\\input.txt";
//        int N = 16;

        // For Command Line
        String fileLocation = args[0];
        int N = Integer.valueOf(args[1]);

        ArrayList<Integer> dataList = new ArrayList<Integer>();

        Scanner inFile = new Scanner(new File(fileLocation));

        while(inFile.hasNextInt()){
            dataList.add(inFile.nextInt());
        }
		// Partition the array list into N subArrays, where N is the number of threads

        // size of each sublist
        int numCountPart = dataList.size() / N;

        ArrayList<List<Integer>> listOfSublists = new ArrayList<List<Integer>>();

        for (int i = 0; i < N; i++){
            List<Integer> sublist = dataList.subList(i*numCountPart, (i+1)*numCountPart);
            listOfSublists.add(sublist);
        }

        // Start recording time
        final long startTime = System.currentTimeMillis();

        // We have a large number of threads. So, we create an ArrayList of threads.
        ArrayList<MeanMultiThread> listOfThreads = new ArrayList<>();

        // Create N threads and assign sub ArrayLists to the threads so that
        // each thread computes mean of its respective subarray.
        for (int i = 0; i < N; i++){
            MeanMultiThread thread = new MeanMultiThread(listOfSublists.get(i));
            listOfThreads.add(thread);
            thread.start();
        }

        // List of temp mean values of sublists
        List<Double> tempMean = new ArrayList<Double>();

		// Start each thread to execute your computeMean() function defined under the run() method
        // so that the N mean values can be computed.

        for (int i = 0; i < N; i++){
            try {
                listOfThreads.get(i).join();
                Double sublistMean = listOfThreads.get(i).getMean();
                tempMean.add(sublistMean);
            } catch (InterruptedException e){
                e.printStackTrace();
            }
        }

		// Show the N mean values
		System.out.println("Temporal mean values of threads are ... ");
        System.out.println(tempMean + "\n");


		// Compute the global mean value from N mean values.
        Double globalSum = 0.0;
        Double globalMean;
        for (int i = 0; i < tempMean.size(); i++){
            globalSum +=  tempMean.get(i);
        }
        globalMean = globalSum / tempMean.size();

        System.out.println("The global mean value is ... ");
        System.out.println(globalMean);

		// TODO: stop recording time and compute the elapsed time
        final long endTime = System.currentTimeMillis();
        long runningTime = endTime - startTime;
        System.out.println("\n" + "Running time is " + runningTime + " milliseconds\n");
	}
}

class MeanMultiThread extends Thread {
	private List<Integer> list;
	private double mean;

	MeanMultiThread(List<Integer> array) {
		list = array;
	}

	public double getMean() {
		return mean;
	}

	public void run() {
		mean = computeMean(list);
	}

    public double computeMean(List<Integer> list){
        int size = list.size();
        double sum = 0;
        for (int i = 0; i < size; i++){
            sum += list.get(i);
        }
        double sublistMean =  sum / (double) size;
        return sublistMean;
    }
}


