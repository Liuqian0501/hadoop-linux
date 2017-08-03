package customPartition;

/**
 * A custom Partitioner class which will assign words with count<10 to 1 reducer and the
 * rest to another reducer
 */


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;

public class countPartitioner extends Partitioner<IntWritable,Text> {
    @Override
    public int getPartition(IntWritable key, Text value, int numReduceTasks) {
        int partition;
        if (key.get()<=10){ partition = 0;}
        else{partition = 1;}
        return partition ;
    }
}
