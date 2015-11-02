# spring-social-neo4j
Spring Social UsersConnectionRepository/ConnectionRepository implementation for Neo4j.

[![Build Status](https://travis-ci.org/shanika/spring-social-neo4j.svg)](https://travis-ci.org/shanika/spring-social-neo4j)

Using this library you can easily configure Spring Social for an application that use Neo4j as the database server. 

1. Add repository
    ```xml
    <repositories>
        <repository>
            <id>spring-social-neo4j</id>
            <url>https://raw.github.com/shanika/spring-social-neo4j/mvn-repo/</url>
            <snapshots>
                <enabled>true</enabled>
                <updatePolicy>always</updatePolicy>
            </snapshots>
        </repository>
    </repositories>
    ```

2. Add dependency
    
    ```xml
    <dependency>
        <groupId>org.springframework</groupId>
        <artifactId>spring-social-neo4j</artifactId>
        <version>0.0.1-SNAPSHOT</version>
    </dependency>
    ```    

3. Configure UsersConnectionRepository in spring social config
    
    ```java
    
    @Autowired
    private Neo4jServer server;
    
    @Override
    public UsersConnectionRepository getUsersConnectionRepository(ConnectionFactoryLocator connectionFactoryLocator) {

        return new Neo4jUsersConnectionRepository(server.url(), connectionFactoryLocator, Encryptors.noOpText());
    }
    
    ```
    


    
