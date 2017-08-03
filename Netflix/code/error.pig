data = LOAD 'res1' USING PigStorage('\t') AS (pairs:chararray,
             true_rating:float,
             pre_rating:float);
errors = FOREACH data GENERATE (true_rating - pre_rating)*(true_rating - pre_rating) as s_err, ABS(true_rating - pre_rating) as a_err;
grouped = GROUP errors all;
mean_errors = foreach grouped generate AVG(errors.a_err), AVG(errors.s_err);
dump mean_errors;
