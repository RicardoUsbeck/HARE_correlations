package test;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;

import org.apache.commons.math3.stat.correlation.KendallsCorrelation;

public class CalculateHAREKendall {
	public static void main(String[] args) throws IOException {
		File dir = new File("RankedLists");
		File[] directoryListing = dir.listFiles();

		for (int topn : new int[] { 10, 20 }) {
			double intersection_average = 0;
			double corr = 0;
			double max = 0;
			for (int i = 0; i < directoryListing.length; i = i + 2) {

				ArrayList<String> HARE = new ArrayList<String>();
				ArrayList<String> PAGERANK = new ArrayList<String>();

				BufferedReader br_h = new BufferedReader(new FileReader(directoryListing[i]));
				BufferedReader br_pr = new BufferedReader(new FileReader(directoryListing[i + 1]));

				for (int j = 0; j < topn; j++) {
					String h = br_h.readLine().split(" ")[0];
					String pr = br_pr.readLine().split(" ")[0];
					HARE.add(h);
					PAGERANK.add(pr);
				}
				HARE.retainAll(PAGERANK);
				intersection_average += HARE.size();
				// System.out.println(directoryListing[i]);
				// System.out.println(directoryListing[i + 1]);
				// System.out.println("Intersection " + (HARE.size()));
				// System.out.println("---");

				double[] pagerank = new double[topn];
				double[] hare = new double[topn];
				for (int d = 0; d < hare.length; d++) {
					hare[d] = d + 1;
				}

				for (int x = 0; x < PAGERANK.size(); x++) {
					boolean found = false;
					for (int rank = 0; rank < HARE.size(); rank++) {
						if (PAGERANK.get(x).equals(HARE.get(rank))) {
							pagerank[x] = rank + 1;
							found = true;
						}
					}
					if (!found) {
						pagerank[x] = 11;
					}
				}
				corr += new KendallsCorrelation().correlation(hare, pagerank);
				if (new KendallsCorrelation().correlation(hare, pagerank) > max) {
					max = new KendallsCorrelation().correlation(hare, pagerank);
					System.out.println("corr on the way to max: "+ new KendallsCorrelation().correlation(hare, pagerank) + " : " + HARE);

				}
			}
			System.out.println("max " + max);
			System.out.println("Average correlation at topn:  " + topn + " => " + corr / (directoryListing.length / 2));
			System.out.println("Average intersection at topn: " + topn + " => " + intersection_average / (directoryListing.length / 2));
		}
	}
}
