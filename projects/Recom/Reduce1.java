package Recom;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

/* count number of common friends for each pair of user */
public  class Reduce1 extends Reducer<TextPair, IntWritable, TextPair, IntWritable> {
    public void reduce(TextPair key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException {
        int sum = 0, prod = 1;
        for (IntWritable val : values) {
            sum += val.get();
            prod *= val.get();
        }
        if( prod!=0 )
            context.write(key, new IntWritable(sum));
    }
}