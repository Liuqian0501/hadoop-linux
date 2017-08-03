package join;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reduce extends Reducer<TextIntPair, Text,Text,Text> {
    @Override
    public void reduce(TextIntPair key, Iterable<Text> values, Context context) throws IOException, InterruptedException{
        String keyOut = new String("");

        for (Text value:values)
        {
            if (key.getSecond().get()==0){
                keyOut = key.getFirst().toString()+"\t"+value.toString();
                continue;
            }

            context.write(new Text(keyOut) , value);
        }


    }
}