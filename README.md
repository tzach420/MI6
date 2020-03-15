# MI6
implementation of a a simple Pub-Sub framework, which will use to implement a system for the MI6, On Her Majesty’s Secret Service. Pub-Sub is shorthand for Publish-Subscribe messaging, an asynchronous communication method in which messages are exchanged between applications without knowing the identity of the sender or recipient.
## Framework details
- MessageBroker- A shared object used for communication between Publishers and Subscribers.</br>
- Topic – An intermediary channel that maintains a list of subscribers to relay messages that are received from publishers.</br>
- Message – Serialized messages are sent to a topic by a publisher which has no knowledge of the subscribers.</br>
- Publisher – The application that publishes a message to a topic.</br>
- Subscriber – An application that registers itself with the desired topic in order to receivethe appropriate messages.</br>
## Program active objects
- Intelligence - creates a mission to be executed. (publisher)</br>
- Q - this guy has an access to the inventory, where he keeps the gadgets he invented for the 00 agents.(subscriber)</br>
- M - she’s the boss. She handles the missions sent from the Intelligence (note that might be several instances of M).(subscriber)</br>
- Moneypenny - she checks the availability of 00 agents(note that might be several instances of MoneyPenny).(subscriber)</br>
Only Q knows about the inventory, only Moneypenny-instances know about the agents, only M-instances know about the missions.
## program flow
Different intelligence sources will deliver info and ask for a agent (or a couple of them) and a gadget that is needed for some mission. One of the M-subscribers will process this event, it must know whether the 00 agent is available in the squad (M cannot perform that check on its own. She will send an event that will be handled by one of the Moneypenny-subscribers), and whether a fit gadget in his Inventory (as before, M cannot perform that check on its own. She will send an event that will be handled by Q). After those checks are made, M fills a report to her diary in case all conditions are met. She also increments a counter of total received missions (this counter is incremented whether the mission was executed or not).
##  PROGRAM EXECUTION
When started, it should accept as command line argument the name of the json input file and the names of the two output files - the first file is the output file for the gadgets, and the second is for the List<Report> - the order of the parameters should be:<br/>
  1.“inputFile.json”- data file with all the data required(missions to execute, agent details, gadget details, number of M          instances, number of Intelligence Instances, etc...)<br/>
  2.“inventoryOutputFile.json”- output file. Shows to gadgets that left in the inventory after executing all the missions.<br/>
  3.“diaryOutputFile.json”- output file. Shows all the missions that executed.
  
## Compilation
Make sure you have maven installed.<br/>
1.clone the project.<br/>
2.open the terminal in the local directory.<br/>
3. type "mvn compile"<br/>
4. type mvn exec:java -Dexec.mainClass="bgu.spl.mics.application.MI6Runner" -Dexec.args="inputFile.json inventoryOutputFile.json diaryOutputFile.json"
  
