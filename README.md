![Travis CI](https://api.travis-ci.org/koku/MockMock.png?branch=master)

MockMock :: Mock SMTP Server
============================

MockMock is a cross-platform SMTP server built on Java. It allows you to test if outgoing emails are sent (without actually sending them) and to see what they look like. It provides a web interface that displays which emails were sent and shows you what the contents of those emails are. If you use MockMock you can be sure that your outgoing emails will not reach customers or users by accident. It really just is a mock SMTP server and has no email sending functionality.

Mail overview page
------------------

![Mail overview page](https://raw.github.com/tweakers-dev/MockMock/master/extra/mockmock1.png "The mail overview page shows you all received mails")


Mail detail page
----------------

![Mail detail page](https://raw.github.com/tweakers-dev/MockMock/master/extra/mockmock2.png "The mail detail page shows you all info about a mail, like headers, content, html content, etc")


Requirements
============

MockMock is built using Java and to function you need a Java 7 runtime environment. MockMock works on OSX, Windows and *nix based platforms that have a Java 7 runtime environment. You can download the Java 7 runtime environment [here](http://www.oracle.com/technetwork/java/javase/downloads/java-se-jre-7-download-432155.html).


Installation / setup
====================

The easiest way to install and run MockMock is by downloading the complete package [here](https://github.com/downloads/koku/MockMock/MockMock.zip). Extract it to any place you like and start the server by running:
`java -jar MockMock.jar`

This will run MockMock on the default ports 25000 (for SMTP) and 8282 (the web interface). After it started you can use your internet browser to navigate to [http://localhost:8282] (replace host name and web port if necessary). This will now show you the emails it received (which should be none at the moment). You can now send emails using the built in SMTP server running on port 25000 by default. The emails should be visible via the web interface. To run MockMock on another port, you can start it with the following parameters:
`java -jar MockMock.jar -p 25 -h 80`

This will run MockMock on SMTP port 25 and http port 80. Please note you might need root permissions on some systems.


Running as a Daemon
===================

The package contains an init.ubuntu file which you can use on Debian based systems. Rename the file to mockmock and place it inside the /etc/init.d/ folder. Edit the file and make sure the paths in there are correct. Also give the file execute permission by running:
`chmod +x /etc/init.d/mockmock`

You can now start MockMock as a daemon by running:
`/etc/init.d/mockmock start`
