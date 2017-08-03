package secondarySort;


/**
 * The Main class for secondary Sorting
 */

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Job;
import org.apache.hadoop.mapreduce.lib.input.FileInputFormat;
import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


public class Sort extends Configured implements Tool{
    @Override
    public int run(String[] args) throws Exception{

        if(args.length !=2){
            System.err.println("Invalid Command");
            System.err.println("Usage: <input path> <output path>");
            return -1;
        }

        Configuration conf = new Configuration();

        Job job = new Job(conf, "SecondarySort");

        job.setJarByClass(getClass());




        FileInputFormat.addInputPath(job, new Path(args[0]));

        FileOutputFormat.setOutputPath(job, new Path(args[1]));

        job.setMapperClass(Map.class);
        job.setReducerClass(Reduce.class);

        job.setMapOutputKeyClass(TextIntPair.class);
        job.setMapOutputValueClass(NullWritable.class);

        
        job.setPartitionerClass(FirstPartitioner.class);
        job.setGroupingComparatorClass(GroupComparator.class);
        job.setSortComparatorClass(KeyComparator.class);

        job.setOutputKeyClass(TextIntPair.class);
        job.setOutputValueClass(NullWritable.class);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Sort(), args);
        System.exit(exitCode);
    }
}


