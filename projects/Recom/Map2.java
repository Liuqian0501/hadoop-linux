package Recom;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Mapper;

import java.io.IOException;
import java.util.List;

/* Use the first ID as key to map each line to a key value pair */
public class Map2 extends Mapper<LongWritable, Text, Text, TextIntPair> {
    public void map(LongWritable key, Text value, Context context) throws IOException, InterruptedException {
        String line = value.toString();
        String[] data = line.split("\t");
       

        Text user_id = new Text(data[0]);
        Text friend_id = new Text(data[1]);
        Integer commonFriend = Integer.parseInt(data[2]);
        context.write(user_id, new TextIntPair(friend_id, new IntWritable(commonFriend)));
    }
}