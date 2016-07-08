# RulesModule

####Module description/purpose

This module is responsible for data validation. 
The first step is to validate incoming messages from the Exchange module before passing it further into the system. It verifies that the data is complete enough for processing. If a message breaks any of these rules the message is put aside in a Holding Table. From here it is possible to reprocess it. This means that the rules are run again on the message. The message itself cannot be changed, but the surrounding circumstances can (e.g. a necessary Mobile Terminal could have been added to the system since the message arrived). If the message is deemed correct it is passed to the Movement module for processing. When finished it will return to the Rules module for the next step, which is the user defined rules. These rules are created by the user and will generate a Notification when triggered. For instance, a user might be interested in all messages from a specific asset, or messages from all assets in a specific area. Multiple users can subscribe to these rules and get access to the Notifications they create. Users can also choose to receive email when a rule triggers.
In order to validate the various rules this module interacts with multiple other modules to get the information it needs.

