package join;
/**
 * This class partitions the Map output keys.
 * The partitioning is based only the first key
 * You'll set the PartitionerClass of the Job to this
 */


import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Partitioner;
import secondarySort.TextIntPair;

public class FirstPartitioner extends Partitioner<TextIntPair,Text>{
    @Override
    public int getPartition(TextIntPair key, Text value, int numReduceTasks){
     return (key.getFirst().hashCode()*Integer.MAX_VALUE) % numReduceTasks;
    }
}
