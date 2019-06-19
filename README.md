# Apache Log File Process :page_facing_up:

## Purpose :running:
Process an Apache log file and Perform statistical analyzes on the information found in the log file.

## Open Source Java Libraries :computer:
**Reddison** - Redis based In-Memory Data Grid for Java. <br />
from :[Redis](https://github.com/MicrosoftArchive/redis/releases) version 3.2.100

**GeoTools** - GeoIP2 API and GeoLite2 database from MaxMind. <br />
from :[MaxMind](https://www.maxmind.com/en/home?gclid=CjwKCAjwuqfoBRAEEiwAZErCsjaLrhOjFW3PlBOJ_QoGmwfnAVpeeaujecSD4q0cy_vTZtlTqHA0uBoCVYoQAvD_BwE&rId=google)

## Compile & Run :running: :walking:
#### Expected command-line arguments:
1. Log file path<br /><br />
_**Possible:**_
2.reports output file path. (If not mentioned, the file will be created relatively.

## Pseudo Work Flow
[Work_Flow](PseudoWorkFlow.pdf)

## Caught By A Thread
#### Thoughts a bout concurrency :thought_balloon:

* while planning how to make my program support multithreading process I had few options.<br />
I decided to make the parsing and updating of the relevant data structures a parallel process, <br />
while reading the file will be exacuted by a single thread. In order to support concurrent reading <br />
of the file it is needed to iterate over the file at list twice, which may cost a lot in case it is a big file.<br /><br />

* Using a fixed size thread pool is protecting the program from creating more and more threads<br />
(in case I chose to use cached thread pool) and have the OS operating endless context switch,<br />
and causing a starvation. I used 4 threads (of course this number is configurative) because my PC has quad-core processor.<br /><br />

* _**possible improvement: **_ At the moment threads are created according to the number given<br />
to the Executor, each thread get a line from the log file and process it. the reading <br />
waits until the threads are finished. To make it more efficient once a single thread has<br />
finished his work and available to get a new line the reading continues.



