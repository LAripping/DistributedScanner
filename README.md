
# Distributed Scanner
A three-fold application that allows information gathering from deep
network scans ran by distributed agents. 
Also comes with an Android app for the admin!


### Installation Guide

###### Part 1) _AggregatorManager_
* Clone the repository and descend into `AgrregatorManager` dir.
  ```bash
  $ git clone https://github.com/LAripping/DistributedScanner.git
  $ cd DistributedScanner/AggregatorManager
  ```

* Create the database and insert the admin user.
   ```bash
   $ mysql < DSschema.sql
   $ mysql> INSERT INTO users(username,password) VALUES ('admin','admin');
    ```

* Build and Run  with Maven (install if needed). Default configuration
  used is described in __Under the Hood__ section. 
  ```bash
     $ sudo apt-get install maven
     $ mvn clean install -DskipTests
     $ mvn exec:java -Dexec.args='amprop.conf' #Single, optional argument = .conf file path
   ```
###### Part 2) _SoftwareAgent(s)_
* Clone the repository and descend into `SoftwareAgent` dir.
  ```bash
  $ git clone https://github.com/LAripping/DistributedScanner.git
  $ cd DistributedScanner/SoftwareAgent
  ```

* Build and Run  with Maven.
  ```bash
     $ mvn clean install
     $ sudo mvn exec:java \
     $  > -Dexec.mainClass='dsSA.softwareAgent.Main'
     $  > -Dexec.args='saprop.conf'  \
     #sudo used to enable executing more 'sophisticated' nmap commands ;-) 
   ```


### Quick Start Guide
After launching the AggregatorManager application the user is presented the GUI.
Here they can log-in using the admin credentials and make use of the full
functionality of the dsAM app.

Eventually, exit the application by closing the GUI window -for the dsAM app- 
and by killing any dsSA apps listening for incoming jobs or waiting to re-execute 
periodic ones.


### Project Structure
By running the SoftwareAgent module locally, multiple nodes gather
information for the network, after executing [nmap](https://nmap.org) commands.
Sets of __nmap jobs__ are assigned to each agent from the *Manager modules, and 
these in turn receive input from the user. All in all nmap jobs can be defined:

1. Directly, passed as input for the **SoftwareAgent** (or dsSA)
   Java application from a terminal.
2. Remotely, assigning them a REST API to selected SoftwareAgents
   that are connected with the **AggregatorManager** (or dsAM) Java application, 
   through a GUI.
3. On the go! Using the **MobileManager** (or dsMM) Android app.

Each agent stores these __nmap jobs__ in a queue and executes them synchronously
or asynchronoysly, since they take one of two forms:

1. __One-off__ jobs, where the command is executed when it's turn comes.
   Results are immediately sent to the AggregatorManager and the job 
   is poped from the queue.
2. __Periodic__ jobs, which are first executed in new threads spawned,
   when their turn comes, so that a timer can be set to re-execute 
   independently from the main thread dealing with "one offs".



### Data Handling
While the server module (dsAM) is mandatory, the two clients are not.
This means that any or no information can be gathered, sent to the 
server and stored in a MySQL database.

The schema designed is defined in [DSschema.sql](https://raw.github.com/LAripping/NetManager/master/AggregatorManager/DSscript.sql) and a MySQL Workbench Model is available in [DSmodel.mwb](https://raw.github.com/LAripping/NetManager/master/AggregatorManager/DSmodel.mwb).

For more info take a look at the __Under the Hood__ section 


### REST API
As mentioned above, the entities communicate over a simple and
coprehensive HTTP protocol.
In detail, the services exposed are the following:
(API visualization coming soon!)


| Method | URL                          | URL Params      | Body                     |  Description                                                |
| :----: | :-------------               | :----------     | :-------                 | :------------                                               | 
| PUT    | <AMurl>`/softwareagent`      |                 | "name\|password"         | Register the Agent in the Manager's Database*.              | 
| GET    | <AMurl>`/nmapjobs`           |`?hash=<SA_hash>`|                          | Request for NmapJobs from agent, identified by its hash**.  | 
| POST   | <AMurl>`/nmapjobs/<job_id>`  |                 |  "Starting Nmap 6.40..." | Send results from nmap identified by <job_id>*.             | 

  *Returns `200 OK` in success or `400 Bad Request` otherwise, 
  both with an empty response body.
  
 **Returns a list ( `[<job>,<job>,...]` )  of `toString()`'ed 
 nmap jobs ( `"id.param,periodic,period"` ). 
 
 __NOTE__ that:
- `Agent Termination` commands and
- `Periodic Job Halt` commands

can also be encoded in this format


### Dependencies
The [Maven](https://maven.apache.org/) tool is used to fetch 
external Java libraries needed so each project comes with
each own `pom.xml` file that specifies them.




### Under the Hood
- Obviously, more than one Agents can run in one node, but
  _only one_  Manager. To avoid duplicate hashes, the generated
  number is XOR'ed with a random number, to maintain large entropy.
- The SoftwareAgent app can even run __without a NIC__ in the 
  system host. This case was added for testing purposes in 
  combination with the fact that this situation arised in development :)
    + If no network interface is found, the SA is registered with
      the following conventions:
        | Interface - MAC | Interface IP | AM URL           |
        | :-------------: | :----------: | :--------------: |
        | `NO-NIF`        | `127.0.0.0`  | `127.0.0.0:9998` |

- The original description strictly defined that the `Agent Termination`
  command should be encoded in an nmap_job with id = -1. Thus only one 
  of this command can currently be stored in the database. This is 
  recognized as an issue (#1) and awaits a fix.
 	 
- Supported parameters for dsSA's property file are: 
  (see [/SoftwareAgent/saprop.conf]() for an exapmle )
    | Key  | Type | Description | 
    | ---: | :--: | :--------- | 
    | `AMexists` | true/false | Allows "master-less" execution. |
    | `AMurl` | ip:port | Points to the Manager host. |
    | `Jobs File` | /path/to/jobs.file | Read jobs from file instead of AM. |
    | `Lines per Read` | int | How many job-lines to be imported per atomic read. |
    | `Pool Size` | int | Count of threads to be pre-spawned to handle one-off jobs. |
    | `Verbose` | true/false | False means silent. True recommended. |
    | `RegisterRequestInterval` | int | Seconds between registration requests in case of failure. |
    | `JobRequestInterval` | int | Seconds between Job requests in case of failure. |

- Supported parameters for dsAM's property file are: 
  (see [/AggregatorManager/amprop.conf]() for an exapmle )
    | Key  | Type | Description | 
    | ---: | :--: | :--------- | 
    | `DBuser` | string | Database username. |
    | `DBpass` | string | Database password. |\
    | `Verbose` | true/false | False means silent. True recommended. |
    | `RegisterRequestInterval` | int | Seconds between registration requests in case of failure. |
    | `JobRequestInterval` | int | Seconds between Job requests from Agents |

### Credits
The project was developed by 
* Petros Kaltzias
* Apostolis Kourtogiannis 
* Leonidas Tsaousis ( @LAripping )

as a semester-long assignment in the course _Software Development 
for Networks and Telecommunications_. 	
				