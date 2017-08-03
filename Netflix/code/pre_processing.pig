data = LOAD 'netflix/TrainingRatings.txt' USING PigStorage(',') AS (movie_id:int,

             user_id:int,

             rating:float);

grouped = group data by user_id;

user1 = foreach grouped generate group as user_id, AVG(data.rating) as mean;

joined = Join data by user_id, user1 by user_id;

store joined into 'netflix/joined';



data2 = LOAD 'netflix/joined' AS (movie_id:int,

             user_id:int,

             rating:float, extra:int, mean:float);

new_data = foreach data2 generate movie_id, user_id, rating-mean; 



store new_data into 'netflix/pre_processing';

store user1 into 'netflix/u_mean';

grouped2 = group data by movie_id;

movie = foreach grouped2 generate group as movie_id, AVG(data.rating) as mean;

store movie into 'netflix/m_mean';
