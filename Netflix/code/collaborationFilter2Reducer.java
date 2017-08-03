package stubs;


import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

/* 
 * receives ((MivieId_1, MovieId_2), list of (Ratings_1, Ratings_2))
 * emits ((MovieId_1, MovieId_2), similarity)
 * */

public class collaborationFilter2Reducer extends Reducer<Text, Text, Text, Text> {

	@Override
	public void reduce(Text key, Iterable<Text> values, Context context)
			throws IOException, InterruptedException {
		double NdotProd = 0;
		double Nrating1squaredSum = 0;
		double Nrating2squaredSum = 0;
		double similarity = 0;
		int n = 0;

		for (Text value : values) {
			n++;
			String[] tokens = value.toString().split(",");
			double rating1 = Double.parseDouble(tokens[0]);
			double rating2 = Double.parseDouble(tokens[1]);
			NdotProd += (rating1) * (rating2);
			Nrating1squaredSum += Math.pow(rating1, 2);
			Nrating2squaredSum += Math.pow(rating2, 2);
		
		}
		if (Nrating1squaredSum == 0 || Nrating2squaredSum == 0 || n == 1) {
			context.write(key, new Text("0"));
			return;
		}

		similarity = NdotProd/ (Math.sqrt(Nrating1squaredSum) * Math.sqrt(Nrating2squaredSum));
		context.write(key, new Text(String.valueOf(similarity)));
	}
}