# spring-social-neo4j

[![Build Status](https://travis-ci.org/shanika/spring-social-neo4j.svg)](https://travis-ci.org/shanika/spring-social-neo4j)

Intention of this project is to implement the suport for neo4j in spring social. 

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
    


    
