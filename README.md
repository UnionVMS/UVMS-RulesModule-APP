# RulesModule

####Module description/purpose

This module is responsible for data validation. 
The first step is to validate incoming messages from the Exchange module before passing it further into the system. It verifies that the data is complete enough for processing. If a message breaks any of these rules the message is put aside in a Holding Table. From here it is possible to reprocess it. This means that the rules are run again on the message. The message itself cannot be changed, but the surrounding circumstances can (e.g. a necessary Mobile Terminal could have been added to the system since the message arrived). If the message is deemed correct it is passed to the Movement module for processing. When finished it will return to the Rules module for the next step, which is the user defined rules. These rules are created by the user and will generate a Notification when triggered. For instance, a user might be interested in all messages from a specific asset, or messages from all assets in a specific area. Multiple users can subscribe to these rules and get access to the Notifications they create. Users can also choose to receive email when a rule triggers.
In order to validate the various rules this module interacts with multiple other modules to get the information it needs.

## Module dependencies

|Name           |Description                                                                                             |Repository                                           |
|---------------|--------------------------------------------------------------------------------------------------------|-----------------------------------------------------|
|Asset          |Gets information needed for rules validation.                                                           |https://github.com/UnionVMS/UVMS-AssetModule         |
|Audit          |Log all operations which have been executed in all UVMS-modules                                         |https://github.com/UnionVMS/UVMS-AuditModule         |
|Config         |Used by all modules in the system. As of now there are no specific configurations the Rules Module uses.|https://github.com/UnionVMS/UVMS-ConfigModule        |
|Exchange       |Gateway provides communication ability with other modules or artifacts beside UVMS. This is the main entry and exit point of the system and also for the Rules Module.|https://github.com/UnionVMS/UVMS-ExchangeModule|
|Mobile Terminal|Gets information needed for rules validation.                                                           |https://github.com/UnionVMS/UVMS-MobileTerminalModule|
|Movement       |The Rules module provides verified messages to the Movement module, and also receives processed positions from it in order to run user defined rules which generates notifications.|https://github.com/UnionVMS/UVMS-MovementModule|
|User           |Mainly used to get correct email addresses and organization information.                                |https://github.com/UnionVMS/UVMS-User                |

## JMS Queue Dependencies
The jndi name example is taken from wildfly8.2 application server

|Name                   |JNDI name example                      |Description                                   |
|-----------------------|---------------------------------------|----------------------------------------------|
|UVMSRulesEvent         |java:/jms/queue/UVMSRulesEvent         |Request queue to Rules service module         |
|UVMSRules              |java:/jms/queue/UVMSRules              |Response queue to Rules module                |
|UVMSMobileTerminalEvent|java:/jms/queue/UVMSMobileTerminalEvent|Request queue to MobileTerminal service module|
|UVMSAssetEvent         |java:/jms/queue/UVMSAssetEvent         |Request queue to Asset service module         |
|UVMSAuditEvent         |java:/jms/queue/UVMSAuditEvent         |Request queue to Audit service module         |
|UVMSUserEvent          |java:/jms/queue/UVMSUserEvent          |Request queue to User service module          |
|UVMSExchangeEvent      |java:/jms/queue/UVMSExchangeEvent      |Request queue to Exchange service module      |
|UVMSMovementEvent      |java:/jms/queue/UVMSMovementEvent      |Request queue to Movement service module      |
|UVMSConfigEvent        |java:/jms/queue/UVMSConfigEvent        |Request queue to Config service module        |

## Datasources
The jndi name example is taken from wildfly8.2 application server

|Name      |JNDI name example                |
|----------|---------------------------------|
|uvms_rules|java:jboss/datasources/uvms_rules|

## Related Repositories

* https://github.com/UnionVMS/UVMS-RulesModule-DB
* https://github.com/UnionVMS/UVMS-RulesModule-MODEL

## Maven 

To speedup building with maven, sometimes it is better only to build changed modules. 
To only build a module and it's dependant modules, you can add "-pl MODULE_NAME -amd" which will start build from module instead of all modules.   

1. Build from domain "mvn clean install -pl domain  -amd"

2. Build from service "mvn clean install -pl service  -amd"

3. Build from rest "mvn clean install -pl rest  -amd"
