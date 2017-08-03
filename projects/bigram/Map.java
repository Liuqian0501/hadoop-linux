package bigram;

/**
 * The Mapper class for Bigram counting - given a line, it outputs (bigram, 1) for each
 * of the bigrams in the line
 */

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable,Text,TextPair,IntWritable> {
    private  Text lastWord = null;
    private  Text currentWord = new Text();


    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String line = value.toString();
        line = line.replace(",", "");
        line = line.replace(".", "");

        for(String word: line.split(" "))
        {
            if(lastWord == null)
            {
                lastWord = new Text(word);
            }
            else
            {
                currentWord.set(word);
                context.write(new TextPair(lastWord, currentWord), new IntWritable(1));
                lastWord.set(currentWord.toString());
            }
        }
    }
}
