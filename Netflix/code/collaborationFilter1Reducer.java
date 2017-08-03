package stubs;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/*
 * Input (UserId,list of (MovieId,Rating))
 * emit (MivieId_1, MovieId_2, Rating_1,Rating_2)
 */

public class collaborationFilter1Reducer extends Reducer<Text, Text, Text, Text> {

	static final Comparator<String>  myComparator  = new Comparator<String>() {
		public int compare(String s1, String s2) {
			return Integer.parseInt(s1.split(",")[0])
					- Integer.parseInt(s2.split(",")[0]);
		}
	};

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		List<String> moviesList = new ArrayList<String>();

		for (Text value : values) {
			moviesList.add(value.toString().trim());
		}
		

		Collections.sort(moviesList, myComparator );

		for (int i = 0; i < moviesList.size(); i++) {
			for (int j = i + 1; j < moviesList.size(); j++) {
				String[] movie1Tokens = moviesList.get(i).split(",");
				String[] movie2Tokens = moviesList.get(j).split(",");
				context.write(new Text(movie1Tokens[0] + "," + movie2Tokens[0]), new Text(movie1Tokens[1] + "," + movie2Tokens[1]));
			}
		}
	}
}