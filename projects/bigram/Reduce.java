package bigram;

/**
 * The Reducer class for Bigram counting
 */

import java.io.IOException;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

public class Reduce extends Reducer<TextPair, IntWritable, Text, IntWritable>{
    @Override
    public void reduce(TextPair key,  Iterable<IntWritable> values, Context context) throws IOException, InterruptedException
    {
        int count=0;
        for(IntWritable value: values)
        {
            count += value.get();
        }

        context.write(new Text(key.toString()), new IntWritable(count));
    }
}