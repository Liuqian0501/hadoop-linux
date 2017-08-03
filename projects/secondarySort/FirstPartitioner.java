package secondarySort;
/**
 * This class partitions the Map output keys.
 * The partitioning is based only the first key
 * You'll set the PartitionerClass of the Job to this
 */

import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Partitioner;

public class FirstPartitioner extends Partitioner<TextIntPair,NullWritable>{
    @Override
    public int getPartition(TextIntPair key, NullWritable value, int numReduceTasks){
     return (key.getFirst().hashCode()*Integer.MAX_VALUE) % numReduceTasks;
    }
}
