package join;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.conf.Configured;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mapreduce.lib.input.TextInputFormat;
import org.apache.hadoop.mapreduce.Job;

import org.apache.hadoop.mapreduce.lib.input.MultipleInputs;
import org.apache.hadoop.mapreduce.lib.output.FileOutputFormat;
import org.apache.hadoop.util.Tool;
import org.apache.hadoop.util.ToolRunner;


/**
 * The Main class for an MR job that performs and SQL join
 */

public class Join extends Configured implements Tool{
    @Override
    public int run(String[] args) throws Exception{

        if(args.length !=3){
            System.err.println("Invalid Command");
            System.err.println("Usage: <input path1> <input path2> <output path>");
            return -1;
        }

        Configuration conf = new Configuration();

        Job job = new Job(conf, "Join");

        job.setJarByClass(getClass());


        Path namesPath = new Path(args[0]);
        Path tradesPath = new Path(args[1]);

        FileOutputFormat.setOutputPath(job, new Path(args[2]));

        MultipleInputs.addInputPath(job, namesPath, TextInputFormat.class, NamesMapper.class);
        MultipleInputs.addInputPath(job, tradesPath, TextInputFormat.class, TradesMapper.class);
        job.setReducerClass(Reduce.class);

        job.setMapOutputKeyClass(TextIntPair.class);
        job.setMapOutputValueClass(Text.class);
        job.setPartitionerClass(FirstPartitioner.class);
        job.setGroupingComparatorClass(GroupComparator.class);

        job.setOutputKeyClass(Text.class);
        job.setOutputValueClass(Text.class);

        return job.waitForCompletion(true)?0:1;
    }

    public static void main(String[] args) throws Exception {
        int exitCode = ToolRunner.run(new Join(), args);
        System.exit(exitCode);
    }
}


