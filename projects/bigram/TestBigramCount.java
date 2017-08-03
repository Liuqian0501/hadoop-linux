package bigram;

/**
 * Unit test to count bigrams
 */

import org.apache.hadoop.io.IntWritable;
import org.apache.hadoop.io.LongWritable;
import org.apache.hadoop.io.Text;

import org.apache.hadoop.mrunit.mapreduce.MapDriver;
import org.apache.hadoop.mrunit.mapreduce.MapReduceDriver;
import org.apache.hadoop.mrunit.mapreduce.ReduceDriver;
import org.junit.Before;
import org.junit.Test;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TestBigramCount {
    MapDriver<LongWritable, Text, TextPair, IntWritable> mapDriver;
    ReduceDriver<TextPair, IntWritable, Text, IntWritable> reduceDriver;
    MapReduceDriver<LongWritable, Text, TextPair, IntWritable, Text, IntWritable> mapReduceDriver;

    @Before
    public void setUp()
    {
        Map mapper = new Map();
        mapDriver = new MapDriver<LongWritable, Text, TextPair, IntWritable>();
        mapDriver.setMapper(mapper);

        Reduce reducer = new Reduce();
        reduceDriver = new ReduceDriver<TextPair, IntWritable, Text, IntWritable>();
        reduceDriver.setReducer(reducer);

        mapReduceDriver = new MapReduceDriver<LongWritable, Text, TextPair, IntWritable, Text, IntWritable>();
        mapReduceDriver.setMapper(mapper);
        mapReduceDriver.setReducer(reducer);
    }

    @Test
    public void testMapper() throws IOException {
        mapDriver.withInput(new LongWritable(1), new Text("this is the bigram this is"));
        mapDriver.withOutput(new TextPair("this", "is"), new IntWritable(1));
        mapDriver.withOutput(new TextPair("is", "the"), new IntWritable(1));
        mapDriver.withOutput(new TextPair("the", "bigram"), new IntWritable(1));
        mapDriver.withOutput(new TextPair("bigram", "this"), new IntWritable(1));
        mapDriver.withOutput(new TextPair("this", "is"), new IntWritable(1));
        mapDriver.runTest();
    }

    @Test
    public void testReducer() throws IOException
    {
        List values = new ArrayList();
        values.add(new IntWritable(1));
        values.add(new IntWritable(1));
        reduceDriver.withInput(new TextPair("this","is"), values);
        reduceDriver.withOutput(new Text("this" +"\t" +"is"), new IntWritable(2));
        reduceDriver.runTest();
    }

    @Test
    public void testMapperReducer() throws IOException
    {
        mapReduceDriver.addInput(new LongWritable(1), new Text("this is the bigram this is"));
        mapReduceDriver.addOutput(new Text("bigram" + "\t" + "this"), new IntWritable(1));
        mapReduceDriver.addOutput(new Text("is" + "\t" + "the"), new IntWritable(1));
        mapReduceDriver.addOutput(new Text("the" +"\t" +"bigram"), new IntWritable(1));
        mapReduceDriver.addOutput(new Text("this" + "\t" + "is"), new IntWritable(2));
        mapReduceDriver.runTest();
    }


}
