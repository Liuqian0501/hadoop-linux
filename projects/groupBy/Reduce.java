package groupBy;

/**
 * The Reducer class for an SQL groupby query.
 * The if condition works like a having construct.
 *
 */


import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reduce extends Reducer<Text,DoubleWritable,Text,DoubleWritable> {
    @Override
    public void reduce(final Text key, final Iterable<DoubleWritable> values,
                       final Context context) throws IOException, InterruptedException {

        Double maxValue = Double.MIN_VALUE;
        for (DoubleWritable value : values) {
            maxValue = Math.max(maxValue,value.get());
        };


        if (maxValue>20) {
            context.write(key, new DoubleWritable(maxValue));

        }
    }
}