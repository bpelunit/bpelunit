To use this plugin, place the following snippet into your pom.

BPELUnit test suites are expected in ${basedir}/src/test/bpelunit
Reports will be written to ${project.build.directory}/bpelunit-reports

	<build>
		<plugins>
			<plugin>
				<groupId>net.bpelunit</groupId>
				<artifactId>maven-bpelunit-plugin</artifactId>
				<version>1.6.2-SNAPSHOT</version>
				<configuration>
					<includes>
						<include>**/*.bpts</include>
					</includes>
				</configuration>
			</plugin>
		</plugins>
	</build>
