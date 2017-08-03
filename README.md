# set-hadoop-linux-Google Compute engine

# 0. New Linux user
```
adduser hadoop

su hadoop

```
### 0.1 修改主机名
由于创建的是伪分布式即单机版，修改主机名：修改hostname文件：`vim /etc/hostname` ，修改内容如下:

```
hadoop-alone
```
在 /etc/hosts 文件里注册修改的主机名称，如果此处不进行修改操作，Hadoop服务启动会报错，使用命令 vim /etc/hosts修改hosts文件，修改完成之后，重启主机使修改配置文件生效，可以使用 reboot 命令。
```
127.0.0.1       localhost
127.0.1.1       localhost
0.0.0.0       hadoop-alone
```

# 1. set hadoop

## 1.1 Install Java JDK

```
gcloud compute ssh hadoop@deployer
```
download jdk
```
$cd Downloads/
$wget http://ftp.osuosl.org/pub/funtoo/distfiles/oracle-java/jdk-7u80-linux-x64.tar.gz
$tar zxf jdk-7u80-linux-x64.tar.gz 
$mv jdk1.7.0_80 jdk
$mv jdk /usr/local/
```

in `~/.bashrc`
```
export JAVA_HOME=/usr/local/jdk
export PATH=$PATH:$JAVA_HOME/bin:
```
`source ~/.bashrc`

## 1.2 Install Hadoop

download Hadoop

```
wget https://archive.apache.org/dist/hadoop/core/hadoop-2.7.3/hadoop-2.7.3.tar.gz
tar xzf hadoop-2.7.3.tar.gz
mv hadoop-2.7.3 hadoop
mv hadoop /usr/local/
```

in `~/.bashrc`
```
export JAVA_HOME=/usr/local/jdk
export HADOOP_HOME=/usr/local/hadoop
export PATH=$PATH:$JAVA_HOME/bin:$HADOOP_HOME/bin:$HADOOP_HOME/sbin
```

Hadoop依赖于Java的开发包（JAVA_HOME），虽然现在已经在profile文件里面定义了JAVA_HOME，但是很多时候Hadoop找不到这个配置，所以建议在整个的配置之中手工配置一下要使用的JDK。
vim /usr/local/hadoop/etc/hadoop/hadoop-env.sh
手工配置要使用的JAVA_HOME环境：
```
#export JAVA_HOME={JAVA_HOME}
export JAVA_HOME=/usr/local/jdk
```

`source ~/.bashrc`

# 2. Wordcount example

## 2.1 data to input folder
```
mkdir Workspace
cd Workspace/
mkdir wordcount
cd wordcount/
mkdir input
##mkdir output
cd input/
cp $HADOOP_HOME/*.txt .
```

## 2.2 run wordcount

for statistic, use hadoop build in jar

`/usr/local/hadoop/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.8.0-sources.jar`

and program

`org.apache.hadoop.examples.WordCount`

run
```
hadoop jar $HADOOP_HOME/share/hadoop/mapreduce/sources/hadoop-mapreduce-examples-2.7.3-sources.jar  org.apache.hadoop.examples.WordCount input output
```
show result
```
cat output/*
```

# 3. Hadoop Configuration
#### 3.1  Hadoop所有的配置文件目录都保存在:`/usr/local/hadoop/etc/hadoop`
#### 3.2 修改`core-site.xml`配置文件，这个配置文件为整个Hadoop运行中的核心配置文件：

  - 官方文档：http://hadoop.apache.org/docs/r2.8.0/hadoop-project-dist/hadoop-common/SingleCluster.html

> 注意：Hadoop默认启动的时候使用的是系统的 /tmp 目录，但是这个目录每一次重新启动之后都会自动清空，也就是说如果你现在不配置好一个临时的存储目录，那么下一次你的Hadoop就无法启动了。

建立一个保存临时目录的路径：`mkdir -p ~/data/hadoop/tmp`

编辑`core-site.xml`配置文件：`vim /usr/local/hadoop/etc/hadoop/core-site.xml`

```xml
<configuration>
  <property>
      <name>hadoop.tmp.dir</name>
      <value>/home/hadoop/data/hadoop/tmp</value>
      <description>Abase for other temporary directories.</description>
  </property>
  <property>
      <name>fs.defaultFS</name>
      <value>hdfs://hadoop-alone:9000</value>
  </property>
</configuration>

```
#### 3.3 修改`hdfs-site.xml`文件，该配置文件主要的功能是进行`HDFS`分布式存储的配置
>注意：如果你现在Hadoop网络环境发生了改变，这两个目录一定要清空，否则Hadoop就启动不了了。

- 建立namenode进程的保存路径：mkdir -p /home/hadoop/data/hadoop/dfs/name
- 建立datanode进程的保存路径：mkdir -p /home/hadoop/data/hadoop/dfs/data 
- 编辑配置文件：vim /usr/local/hadoop/etc/hadoop/hdfs-site.xml

```xml
<configuration>
  <property>
      <name>dfs.replication</name>
      <value>1</value>
  </property>
  <property>
      <name>dfs.namenode.name.dir</name>
      <value>file:///home/hadoop/data/hadoop/dfs/name</value>
  </property>
  <property>
      <name>dfs.datanode.data.dir</name>
      <value>file:///home/hadoop/data/hadoop/dfs/data</value>
  </property>
<!--
  <property>
      <name>dfs.namenode.http-address</name>
      <value>hadoop-alone:50070</value>
  </property>
  <property>
      <name>dfs.namenode.secondary.http-address</name>
      <value>hadoop-alone:50090</value>
  </property>
-->
  <property> 
      <name>dfs.permissions</name>
      <value>false</value>
   </property>
</configuration>
```

#### 3.4 修改`yarn-site.xml`配置文件，这个是进行`yarn`分析结构使用的

修改配置文件：vim /usr/local/hadoop/etc/hadoop/yarn-site.xml

```
<configuration>
    <property>
        <name>yarn.resourcemanager.admin.address</name>
        <value>hadoop-alone:8033</value>
        </property>
    <property>
        <name>yarn.nodemanager.aux-services</name>
        <value>mapreduce_shuffle</value>
    </property>
    <property>
        <name>yarn.nodemanager.aux-services.mapreduce_shuffle.class</name>
        <value>org.apache.hadoop.mapred.ShuffleHandler</value>
      </property>
    <property>
        <name>yarn.resourcemanager.resource-tracker.address</name>
        <value>hadoop-alone:8025</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>hadoop-alone:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.address</name>
        <value>hadoop-alone:8050</value>
    </property>
    <property>
        <name>yarn.resourcemanager.scheduler.address</name>
        <value>hadoop-alone:8030</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.address</name>
        <value>hadoop-alone:8088</value>
    </property>
    <property>
        <name>yarn.resourcemanager.webapp.https.address</name>
        <value>hadoop-alone:8090</value>
    </property>
</configuration>


```

#### 3.5 edit /usr/local/hadoop/etc/hadoop/mapred-site.xml
```
<configuration>
<property>

<name>mapreduce.framework.name</name>
<value>yarn</value>

</property>
</configuration>
```

#### 3.5 由于现在属于单主机伪分布式运行方式，所以还需要修改一下从节点的配置文件
修改配置文件：`nano /usr/local/hadoop/etc/hadoop/slaves`

```
hadoop-alone
```
#### 3,6.现在对于数据的保存保存在了/home/hadoop/data/hadoop/{name,data}目录下，所以如果要想使用，则必须针对于这两个目录进行格式化的处理操作：
`hdfs namenode -format`

# 4 启动Hadoop相关进程
（不建议使用此启动命令）：/usr/local/hadoop/sbin/start-all.sh

`permission denied`
```
1.ssh-keygen
2.It will ask for folder location where it will copy the keys, I entered /home/hadoop/.ssh/id_rsa

3.it will ask for pass phrase, keep it empty for simplicity.

4.cp /home/hadoop/.ssh/id_rsa.pub .ssh/authorized_keys (To copy the newly generated public key to auth file in your users home/.ssh directiry

ssh localhost
start-dfs.sh (Now it should work!)
```
启动完成之后，查看进行：jps

```
hadoop@dockers-container:~$ jps
2793 NodeManager
1924 NameNode
3069 Jps
2698 ResourceManager
2176 SecondaryNameNode
2045 DataNode
```
# 5.Verifying Hadoop Installation

The following steps are used to verify the Hadoop installation.

Step 1: Name Node Setup
Set up the namenode using the command “hdfs namenode -format” as follows.
```
$ cd ~ 
$ hdfs namenode -format 
```

Step 2: Verifying Hadoop dfs
The following command is used to start dfs. Executing this command will start your Hadoop file system.

$ start-dfs.sh 

Step 3: Verifying Yarn Script
The following command is used to start the yarn script. Executing this command will start your yarn daemons.

$ start-yarn.sh 

Step 4: Accessing Hadoop on Browser
The default port number to access Hadoop is 50070. Use the following url to get Hadoop services on browser.

http://localhost:50070/

Step 5: Verify All Applications for Cluster
The default port number to access all applications of cluster is 8088. Use the following url to visit this service.

http://localhost:8088/


# Run wordcount
Uploading Files to HFDS
```
$hadoop fs -put /home/hadoop/Workspace/wordcount/input /
$hadoop fs -ls /
Found 1 items
drwxr-xr-x   - hadoop supergroup          0 2017-07-28 03:31 /input
$hadoop fs -ls /input
Found 3 items
-rw-r--r--   1 hadoop supergroup      84854 2017-07-28 03:31 /input/LICENSE.txt
-rw-r--r--   1 hadoop supergroup      14978 2017-07-28 03:31 /input/NOTICE.txt
-rw-r--r--   1 hadoop supergroup       1366 2017-07-28 03:31 /input/README.txt
```

# Run hadoop

**Compile!the!three!Java!classes:!**
```
javac -classpath `hadoop classpath` stubs/*.java
```
**Collect!your!compiled!Java!files!into!a!JAR!file:!**
```
$ jar cvf wc.jar stubs/*.class
```
**run hadoop**
```
hadoop jar wc.jar stubs.WordCount /input /output
```
**show result**
```
hadoop fs -cat /output/* | less
```


# Run hadoop streaming API python
```
$ hadoop jar $HADOOP_HOME/share/hadoop/tools/lib/hadoop-streaming-*.jar -D mapred.reduce.tasks=4 \
-files /home/hadoop/Workspace/wordcount/set-hadoop-linux/ForPython/wordMapper.py,/home/hadoop/Workspace/wordcount/set-hadoop-linux/ForPython/wordReducer.py \
-input /input \
-output /output7 \
-mapper "python /home/hadoop/Workspace/wordcount/set-hadoop-linux/ForPython/wordMapper.py" \
-combiner "python /home/hadoop/Workspace/wordcount/set-hadoop-linux/ForPython/wordReducer.py" \
-reducer "python /home/hadoop/Workspace/wordcount/set-hadoop-linux/ForPython/wordReducer.py"
```


# Run hadoop with ToolRunner
```
$javac -classpath `hadoop classpath` wordCount/*.java
$jar cvf wordCount.jar wordCount/*.class
$hadoop jar wordcount.jar wordCount.WordCountwithTool -D reducer.minWordCount=5 /input /output
```




