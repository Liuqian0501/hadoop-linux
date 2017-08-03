package Recom;

/**
 * The Main class for the Recommendations MapReduce program
 * It sets up and runs 2 MR jobs chained together (output of 1 is input to the other)
 */


import org.apache.hadoop.fs.FileSystem;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.conf.*;
import org.apache.hadoop.io.*;
import org.apache.hadoop.mapreduce.*;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;

public class Recommendations{
    private static final String OUTPUT_PATH = "/test/intermediate_output";
    public static void main(String[] args) throws Exception {
        // --------- first Map1-Reduce1 Job -------------------------
        // For each user, calculates the number of common friends
        // with other users
        Configuration conf = new Configuration();

        FileSystem fs = FileSystem.get(conf);
        if(fs.exists(new Path(OUTPUT_PATH))){
            fs.delete(new Path(OUTPUT_PATH),true);
        }

        Job job = new Job(conf, "FriendRecom-MR1");
        job.setJarByClass(Recommendations.class);
        job.setOutputKeyClass(TextPair.class);
        job.setOutputValueClass(IntWritable.class);

        job.setMapOutputKeyClass(TextPair.class);
        job.setMapOutputValueClass(IntWritable.class);

        job.setMapperClass(Map1.class);
        job.setReducerClass(Reduce1.class);

        FileInputFormat.addInputPath(job, new Path(args[0]));
        FileOutputFormat.setOutputPath(job, new Path(OUTPUT_PATH));

        job.waitForCompletion(true);
        // -------- Second Map1 Reduce1 Job -------
        // For each user, find top recommendations
        Configuration conf2 = new Configuration();

        Job job2 = new Job(conf2, "FriendRecom-MR2");
        job2.setJarByClass(Recommendations.class);
        job2.setOutputKeyClass(Text.class);
        job2.setOutputValueClass(Text.class);

        job2.setMapOutputKeyClass(Text.class);
        job2.setMapOutputValueClass(TextIntPair.class);

        job2.setMapperClass(Map2.class);
        job2.setReducerClass(Reduce2.class);

        FileInputFormat.addInputPath(job2, new Path(OUTPUT_PATH));
        FileOutputFormat.setOutputPath(job2, new Path(args[1]));

        job2.waitForCompletion(true);
    }

}
