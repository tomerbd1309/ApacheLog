# Apache Log File Process :page_facing_up:

## Purpose :dart:
Process an Apache log file and perform statistical analyzes on the information found in the log file.

## Open Source Java Libraries :computer:
**Reddison** - Redis based In-Memory Data Grid for Java. <br />
from :[Redis](https://github.com/MicrosoftArchive/redis/releases) version 3.2.100

**GeoTools** - GeoIP2 API and GeoLite2 database from MaxMind. <br />
from :[MaxMind](https://www.maxmind.com/en/home?gclid=CjwKCAjwuqfoBRAEEiwAZErCsjaLrhOjFW3PlBOJ_QoGmwfnAVpeeaujecSD4q0cy_vTZtlTqHA0uBoCVYoQAvD_BwE&rId=google)

## Compile & Run :running: :walking:
#### Expected command-line arguments:
1.	Log file path<br /><br />
_**Possible:**_<br />
2.	Reports output file path. (If not mentioned, the file will be created relatively.

## Pseudo Work Flow :surfer:
[Work_Flow](PseudoWorkFlow.pdf)

## Caught By A Thread :closed_lock_with_key:
#### Thoughts about concurrency :thought_balloon:

* While planning how to make my program support multithreading process I had few options.
I decided to make the parsing and updating of the relevant data structures a parallel process,
while reading the file will be exacuted by a single thread. In order to support concurrent reading 
of the file it is needed to iterate over the file at list twice, which may cost a lot in case it is a big file.<br /><br />

* I chose Using a fixed size thread pool. At first I thought using a cached thread pool which means that the OS is
creating new thread for every task required while it can, I assumed this can work well since my ApacheLogThread
task is short (when the task is heavy creating more and more threads for new tasks that arrive will burden the OS even more).
but when i tested this approach the result was major slowdown.My assumption is that the OS created large amount of threads 
(for each line read from the log file), and while trying to distribute the 
CPU fairly among all the threads, each thread got a really small part of the CPU, to small to 
finish it's task, and the OS was occupied performing context switches. Using fixed size thread pool 
allowed me to have more control on the way the work is distributed and done.<br />


