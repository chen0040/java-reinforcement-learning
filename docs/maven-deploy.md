To deploy to maven snapshot:

```bash
mvn clean deploy
```

To deploy a release, make sure the version is not ended with "-SNAPSHOT" and run this command

```bash
mvn clean deploy -P release
```

more details can refer to http://www.sonatype.org/nexus/2015/01/08/deploy-to-maven-central-repository/

To check the maven release: https://oss.sonatype.org/
