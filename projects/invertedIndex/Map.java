package invertedIndex;



import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.lib.input.FileSplit;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.StringTokenizer;

public class Map extends Mapper<LongWritable,Text,Text,Text> {

    private Text word = new Text();
    private Text filename = new Text();

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException{

            FileSplit currentSplit = ((FileSplit) context.getInputSplit());
            String filenameStr = currentSplit.getPath().getName();
            filename = new Text(filenameStr);

            String line = value.toString();
            StringTokenizer tokenizer = new StringTokenizer(line);

            while (tokenizer.hasMoreTokens()) {
                word.set(tokenizer.nextToken());
                context.write(word, filename);
            }

    }

}
