# _ZipLock_

_Description: Pairs Data._

## Project Setup

1. _Java 8_
2. _Local Instance of MySql 5.6+_

## Testing

### Unit Tests

1. `mvn test`

### Integration Tests

1. _Have Databases set up and all integeration points defined in the config file_
2. `mvn it:test`

## Deploying

- _Can deploy via Docker, RPM, or JAR file_
- _Can be used as CLI or API_
- _Monitoring services and logging._

### _How to deploy_

_JAR_

1. `mvn clean package install`
2. `cd ziplock-xl/target`
3. artifact jar will have prefix of `ziplock-xl`
4. to run `java -jar ziplock-xl-x.jar {config_file.json}`

_API_

1. `mvn clean package install`
2. `cd ziplock-api/target`
3. artifact jar will have prefix of `ziplock-api`
4. to run `java -jar ziplock-api-x.jar server {config_file.yml}`

## Troubleshooting & Useful Tools

_Examples of common tasks for CLI_

> e.g.
> 
> - How to ensure my data doesn't suck.
> - Does the Janus workflow work, flow?
> - Streaming through cliques of data for clickstream parity.

_Examples of common tasks for API_

> API End Points
> 
> - GET     /datasets 
> - POST    /datasets 
> - DELETE  /datasets/{id} 
> - GET     /datasets/{id} 
> - PUT     /datasets/{id} 
> - GET     /datasources 
> - POST    /datasources 
> - DELETE  /datasources/{id} 
> - GET     /datasources/{id} 
> - PUT     /datasources/{id} 
> - POST    /fields 
> - GET     /fields/{id} 
> - GET     /relations 
> - POST    /relations 
> - GET     /relations/{id} 
> - GET     /reporters 
> - POST    /reporters 
> - GET     /runs 
> - POST    /runs 
> - GET     /runs/{id} 

## Contributing changes

- _Internal git flow_
- _Pull request guidelines_
- _Jira - DS:TA_
- _"Please open github issues"_

## License