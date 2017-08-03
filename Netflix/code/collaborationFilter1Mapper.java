package stubs;


import java.io.IOException;

import org.apache.hadoop.io.DoubleWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.mapreduce.Mapper;
import org.apache.hadoop.mapreduce.Mapper.Context;


public class collaborationFilter1Mapper extends
Mapper<LongWritable, Text, Text, Text> {

@Override
public void map(LongWritable key, Text value, Context context)
throws IOException, InterruptedException {

String valueAsString = value.toString().trim();
String[] tokens = valueAsString.split(",");
if (tokens.length < 3) {
return;
}
context.write(new Text(tokens[0]), new Text(tokens[1] + ","+ tokens[2]));
}

}