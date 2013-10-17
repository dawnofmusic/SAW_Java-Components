SAW_Java-Components
===================

SAW_Java-Components is a framework for creating Java Swing Components that are extended by several interactive capabilities like dragging, support of selection rectangles and others.

Current Version
---------------

0.1.11 Change Log: added listmodel support for tablemodel


Download / Integrate
--------------------

You can simply download or integrate this project via Maven. Just add the following lines to your pom.xml:

	...
	<dependencies>
		...
		<dependency>
			<groupId>de.wsdevel</groupId>
			<artifactId>SAW_Java-Components</artifactId>
			<version>0.1.11</version>
		</dependency>
		...
	</dependencies>
	...
	
	<repositories>
		...
		<repository>
			<id>sebastian-weiss-snapshot</id>
			<name>Sebastian's Snapshot Repository</name>
			<url>http://www.sebastian-weiss.de/mvn-repo/snapshots</url>
			<releases>
				<enabled>false</enabled>
			</releases>
			<snapshots>
				<enabled>true</enabled>
				<updatePolicy>always</updatePolicy>
			</snapshots>
		</repository>
		<repository>
			<id>sebastian-weiss-release</id>
			<name>Sebastian's Release Repository</name>
			<url>http://www.sebastian-weiss.de/mvn-repo/releases</url>
			<releases>
				<enabled>true</enabled>
				<updatePolicy>daily</updatePolicy>
			</releases>
			<snapshots>
				<enabled>false</enabled>
			</snapshots>
		</repository>
		...
	</repositories>
	...
