package groupBy;

/**
 * The Mapper class for an SQL groupby query.
 * The Grouping key becomes the key for map output
 */

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable,Text,Text,DoubleWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String line = value.toString();
        String[] data = line.split(",");

        if(data[0].equals("SYMBOL")){
            return;
        }


        if ( data[1].equals("EQ")){

            String symbol = data[0];
            Double openPrice = Double.parseDouble(data[2]);
            context.write(new Text(symbol), new DoubleWritable(openPrice));
        }


            }
        }