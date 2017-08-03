package join;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class NamesMapper extends Mapper<LongWritable,Text,TextIntPair,Text> {
    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

        String line = value.toString();
        String[] data = line.split(",");


        if(data[0].equals("SYMBOL")){
            return;
        }

        TextIntPair keyOut = new TextIntPair(new Text (data[0]), new IntWritable(0));

        String output = data[1];

        context.write(keyOut, new Text(output));
    }

}

