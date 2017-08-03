package stubs;


import org.apache.log4j.Logger;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.KeyValueTextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

/*
 *  Input (UserId,MovieId,Rating)
 * 	items that are rated by the same user are emitted together with their ratings
 *  Output (MivieId_1, MovieId_2, Rating_1; Rating_2;)
 */

public class collaborationFilter1Driver extends Configured implements Tool {

	private static Logger THE_LOGGER = Logger.getLogger(collaborationFilter1Driver.class);

	@Override
	public int run(String[] args) throws Exception {
		Job job = new Job(getConf());
		job.setJarByClass(collaborationFilter1Driver.class);
		job.setJobName("collaborationFilter1");


		job.setMapOutputKeyClass(Text.class);
		job.setMapOutputValueClass(Text.class);

		job.setOutputKeyClass(Text.class);
		job.setOutputValueClass(Text.class);	

		job.setMapperClass(collaborationFilter1Mapper.class);
		job.setReducerClass(collaborationFilter1Reducer.class);

		FileInputFormat.setInputPaths(job, new Path(args[0]));
		FileOutputFormat.setOutputPath(job, new Path(args[1]));

		boolean status = job.waitForCompletion(true);
		THE_LOGGER.info("run(): status=" + status);
		return status ? 0 : 1;
	}

	public static void main(String[] args) throws Exception {
		if (args.length < 2) {
			THE_LOGGER.warn("usage collaborationFilter1Driver <input> <output>");
			System.exit(1);
		}

		THE_LOGGER.info("inputDir=" + args[0]);
		THE_LOGGER.info("outputDir=" + args[1]);
		int returnStatus = ToolRunner.run(new collaborationFilter1Driver(), args);
		System.exit(returnStatus);
	}

}