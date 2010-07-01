To use this plugin, place the following snippet into your pom.

BPELUnit test suites are expected in ${basedir}/src/test/bpelunit
Reports will be written to ${project.build.directory}/bpelunit-reports

	<build>
		<plugins>
			<plugin>
				<groupId>org.apache.maven.plugins</groupId>
				<artifactId>maven-plugin-plugin</artifactId>
				<version>2.5.1</version>
				<configuration>
					<goalPrefix>org.bpelunit.framework.client.maven</goalPrefix>
				</configuration>
				<executions>
					<execution>
						<id>generated-helpmojo</id>
						<goals>
							<goal>helpmojo</goal>
						</goals>
					</execution>
				</executions>
			</plugin>
			<plugin>
				<groupId>net.bpelunit</groupId>
				<artifactId>maven-bpelunit-plugin</artifactId>
				<version>1.4.1-SNAPSHOT</version>
				<configuration>
					<includes>
						<include>**/*.bpts</include>
					</includes>
				</configuration>
			</plugin>
		</plugins>
	</build>
