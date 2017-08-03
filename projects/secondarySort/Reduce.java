package secondarySort;


import org.apache.hadoop.io.NullWritable;
import org.apache.hadoop.mapreduce.Reducer;

import java.io.IOException;

public class Reduce extends Reducer<TextIntPair, NullWritable,TextIntPair,NullWritable> {
    @Override
    public void reduce(TextIntPair key, Iterable<NullWritable> values, Context context) throws IOException, InterruptedException {

      for (NullWritable val:values)
        context.write(key, val.get());

    }
}
