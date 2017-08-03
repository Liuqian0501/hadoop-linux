package wordCount;

/**
 * An example where we pass in a command line option to
 * the Reducer
 *
 */



import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class WordCountReducerWithMin extends Reducer<Text, IntWritable,Text,IntWritable> {
    @Override
    public void reduce(Text key, Iterable<IntWritable> values, Context context) throws IOException, InterruptedException{

        int minCount=0;
        Configuration config = context.getConfiguration();
        try {
            minCount = Integer.parseInt(config.get("reducer.minWordCount"));
        } catch (Exception e){

        }

        int count = 0;
        for (IntWritable value:values)
        {
            count += value.get();
        }

        if (count>=minCount) {

            context.write(key, new IntWritable(count));
        }
    }
}
