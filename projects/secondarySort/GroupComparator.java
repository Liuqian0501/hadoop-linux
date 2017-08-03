package secondarySort;
/**
 * This class groups the Map output keys. You'll set the GroupComparator of the Job to this
 */

import org.apache.hadoop.io.WritableComparable;
import org.apache.hadoop.io.WritableComparator;

public class GroupComparator extends WritableComparator {
    protected  GroupComparator(){
        super(TextIntPair.class, true);
    }

    @Override
    public int compare(WritableComparable w1, WritableComparable w2){

        TextIntPair p1 = (TextIntPair) w1;
        TextIntPair p2 = (TextIntPair) w2;

      return p1.getFirst().compareTo(p2.getFirst());
    }

}