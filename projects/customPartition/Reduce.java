package customPartition;

/**
 * The Reducer class for custom partiioning - its just an identity function
 */


import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reduce extends Reducer<IntWritable,Text,IntWritable,Text> {
    @Override
    public void reduce(final IntWritable key, final Iterable<Text> values,
                       final Context context) throws IOException, InterruptedException {


        for (Text value : values) {
            context.write(key, value);
        }


    }
}