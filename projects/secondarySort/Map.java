package secondarySort;



import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;

public class Map extends Mapper<LongWritable,Text,TextIntPair,NullWritable> {

    @Override
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException
    {
        String line = value.toString();
        String[] data = line.split(",");


        Text city = new Text(data[0]);
        IntWritable sale = new IntWritable(Integer.parseInt(data[1]));

            context.write(new TextIntPair(city,sale),  NullWritable.get());
        }


            }
