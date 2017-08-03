package stubs;

import java.io.IOException;

import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

/* receive (_, (MovieId, UserId, Rating))
 * emit (UserId, (MovieId,Rating) */

public class predictionMapper extends Mapper<Object, Text, Text, Text> {


	@Override
	public void map(Object key, Text value, Context context)
			throws IOException, InterruptedException {

		String valueAsString = value.toString().trim();
		String[] tokens = valueAsString.split(",");
		if (tokens.length != 3) {
			return;
		}

		String movieId = tokens[0];
		String userId = tokens[1];
		String rating = tokens[2];
		context.write(new Text(userId), new Text(movieId + "," + rating));
	}

}