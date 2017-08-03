package customPartition;
/**
 * The Mapper class - it just reverses the key value pair
 */

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable,Text,IntWritable,Text> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String line = value.toString();
        String[] data = line.split("\t");

        IntWritable keyOut = new IntWritable(Integer.parseInt(data[1]));
        Text valueOut = new Text(data[0]);

        context.write(keyOut, valueOut);
    }
}