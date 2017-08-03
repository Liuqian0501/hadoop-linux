package stubs;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.PriorityQueue;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* receive (UserId, list of (MovieId,Rating))
 * emit (PredictingUserID,PredictingMovieID, actualRating, predictedRating)
 * */

public class predictionReducer extends Reducer<Text, Text, Text, Text> {
	private String testingDataFileName = "/home/training/workspace/prediction/TestingRatings.txt";
	private String userFileName = "/home/training/workspace/prediction/user_mean.txt";
	private String movieFileName = "/home/training/workspace/prediction/movie_mean.txt";
	private String simFolder = "/home/training/workspace/prediction/similarity/";
	private Map<Integer, List<Entry>> testingEntries = new HashMap<Integer, List<Entry>>();
	private Map<Integer, Map<Integer, Float>> movieSimilarities = new HashMap<Integer, Map<Integer, Float>>();
	private Map<Integer, Float> user_mean = new HashMap<Integer, Float>();
	private Map<Integer, Float> movie_mean = new HashMap<Integer, Float>();

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		List<Entry> ratings = new ArrayList<Entry>();
		for (Text value : values) {
			String[] tokens = value.toString().split(",");
			ratings.add(new Entry(Integer.parseInt(tokens[0]), Float
					.parseFloat(tokens[1])));
		}
		for (Entry e : testingEntries.get(Integer.parseInt(key.toString()))) {
			float s1 = Avg(e.movieId, ratings);
			int userId = Integer.parseInt(key.toString());
			float s2 = user_mean.get(userId);
			float s3 = movie_mean.get(e.movieId);
			float p = (float) (0.95*s1 + 0.0*s2 + 0.05*s3);
			context.write(new Text(key.toString()+","+e.movieId), new Text(String.format("%.1f\t%.1f", e.rating, p)));
		}
	}

	@Override
	protected void setup(Context context) throws IOException,
			InterruptedException {
		initTestingData();
		initSimData();
	}


	static final Comparator<SimRatingPair> myCompara = new Comparator<SimRatingPair>() {
		public int compare(SimRatingPair s1, SimRatingPair s2) {
			return Float.compare(s1.sim, s2.sim);
		}
	};

	private float Avg(int itemId, List<Entry> ratings) {
		float sum = 0, n = 0;
		int N = 3;
		PriorityQueue<SimRatingPair> queue = new PriorityQueue<SimRatingPair>(N, myCompara);

		for (Entry e : ratings) {

			float sim;
			if (movieSimilarities.containsKey(Math.min(itemId, e.movieId))
					&& movieSimilarities.get(Math.min(itemId, e.movieId))
							.containsKey(Math.max(itemId, e.movieId))) {
				sim = movieSimilarities.get(Math.min(itemId, e.movieId)).get(
						Math.max(itemId, e.movieId));
			} else {
				sim = 0;
			}

			if (queue.size() < N) {
				queue.add(new SimRatingPair(sim, e.rating));
			} else if (Float.compare(queue.peek().sim, sim) < 0) {
				queue.remove();
				queue.add(new SimRatingPair(sim, e.rating));
			}
		}

		for (SimRatingPair i : queue) {
			sum += i.rating;
			n ++;
		}
		/*int mid = (queue.size()+1)/2;
		while (mid > 1){
			queue.remove();
			mid--;
		}*/

		return sum / n;
		//return queue.peek().rating;
	}


	private void initTestingData() throws FileNotFoundException, IOException {

		File testingDataFile = new File(testingDataFileName);
		try{
		BufferedReader br = new BufferedReader(new FileReader(testingDataFile));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split(",");
			int userId = Integer.parseInt(tokens[1]), movieId = Integer
					.parseInt(tokens[0]);
			float rating = Float.parseFloat(tokens[2]);

			if (testingEntries.containsKey(userId)) {
				testingEntries.get(userId).add(new Entry(movieId, rating));
			} else {
				List<Entry> temp = new ArrayList<Entry>();
				temp.add(new Entry(movieId, rating));
				testingEntries.put(userId, temp);
			}
		}
		br.close();}
		catch (Exception e) {
			System.out.println("Could not open the Testing file");
			return;
		}
		
		File user_means = new File(userFileName);
		try{
		BufferedReader br = new BufferedReader(new FileReader(user_means));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\t");
			int userId = Integer.parseInt(tokens[0]);
			float rating = Float.parseFloat(tokens[1]);
			user_mean.put(userId, rating);
		}
		br.close();}
		catch (Exception e) {
			System.out.println("Could not open the user_mean file");
			return;
		}
		
		File movie_means = new File(movieFileName);
		try{
		BufferedReader br = new BufferedReader(new FileReader(movie_means));
		String line = null;
		while ((line = br.readLine()) != null) {
			String[] tokens = line.split("\t");
			int movieId = Integer.parseInt(tokens[0]);
			float rating = Float.parseFloat(tokens[1]);
			movie_mean.put(movieId, rating);
		}
		br.close();}
		catch (Exception e) {
			System.out.println("Could not open the movie_mean file");
			return;
		}
		
	}


	private void initSimData() throws FileNotFoundException, IOException {

		File folder = new File(simFolder);
		File[] listOfFiles = folder.listFiles();

		for (File file : listOfFiles) {
		    if (file.isFile()) {
		        File simFile = new File(simFolder + file.getName());
				try {
					BufferedReader br = new BufferedReader(new FileReader(simFile));
					String line = null;
					while ((line = br.readLine()) != null) {
						String[] tokens = line.split("\t");
						int item1 = Integer.parseInt(tokens[0].split(",")[0]), item2 = Integer.parseInt(tokens[0].split(",")[1]);
						float sim = Float.parseFloat(tokens[1]);
						if (item1 > item2) {
							int temp = item1;
							item1 = item2;
							item2 = temp;
						}

						if (movieSimilarities.containsKey(item1)) {
							movieSimilarities.get(item1).put(item2, sim);
						} else {
							Map<Integer, Float> temp = new HashMap<Integer, Float>();
							temp.put(item2, sim);
							movieSimilarities.put(item1, temp);
						}

					}
					br.close();
				} catch (Exception e) {
					System.out.println("Could not open the similarity file");
					return;
				}
				
				
		    }
		}
		
	}
	class Entry {
		int movieId;
		float rating;

		public Entry(int movieId, float rating) {
			this.movieId = movieId;
			this.rating = rating;
		}
	}

	class SimRatingPair {
		float sim;
		float rating;

		SimRatingPair(float sim, float rating) {
			this.sim = sim;
			this.rating = rating;
		}
	}
}