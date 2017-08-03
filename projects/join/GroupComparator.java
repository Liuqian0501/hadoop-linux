package join;

/**
 * This class groups the Map output keys. You'll set the GroupComparator of the Job to this
 */

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;
import secondarySort.TextIntPair;

public class GroupComparator extends WritableComparator {
    protected GroupComparator(){
        super(secondarySort.TextIntPair.class, true);
    }

    @Override
    public int compare(WritableComparable w1, WritableComparable w2){

        secondarySort.TextIntPair p1 = (secondarySort.TextIntPair) w1;
        secondarySort.TextIntPair p2 = (TextIntPair) w2;

      return p1.getFirst().compareTo(p1.getFirst());
    }

}