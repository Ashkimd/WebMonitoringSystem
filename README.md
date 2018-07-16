# WebMonitoringSystem
Tracks changes in a group of websites
There are two HashMaps where URLs of websites are keys and html codes of their pages are values.
First map represents the yesterday's state of websites and the second one - current state.
The program tracks what websites have been removed, added of modified and sends e-mail with a report via Gmail SMPT and TLS protocol.
Mail settings are in the src/main/resources/config.properties file.
