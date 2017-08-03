package Recom;

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.Text;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;
import java.util.*;

/* for each user, find the 10 IDs that have most common friends with the user */
public  class Reduce2 extends Reducer<Text, TextIntPair, Text, Text> {

    public void reduce(Text key, Iterable<TextIntPair> values, Context context) throws IOException, InterruptedException {


        List<TextIntPair> common  = new ArrayList<>();
        for (TextIntPair val:values){
            Text first = new Text(val.getFirst());
            IntWritable second = new IntWritable(val.getSecond().get());

            if (common.size()<10){
                common.add(new TextIntPair(first, second));
                continue;
            }

            for (int i=0; i<10; i++){
                if(common.get(i).getSecond().get()<second.get()){
                    common.set(i, new TextIntPair(first, second));
                    break;
                }
            }
        }


        StringBuilder stringBuilder = new StringBuilder();

        int j;
        for (j=0 ; j < Math.min(common.size(),11) ;j++) {

            stringBuilder.append(common.get(j).getFirst()) ;

            if (j < Math.min(common.size(),11)-1){
                stringBuilder.append("|");}

        };

        context.write(key, new Text(stringBuilder.toString()));
    }
}