# Getting Started

* Application developed with [openjdk-21.0.1](https://jdk.java.net/21/)
  * ** JAVA_HOME env var must be set **
* Elasticsearch running inside docker for convenience, developed using [docker desktop](https://www.docker.com/products/docker-desktop/)
  * No docker-compose used intentionally. Java installation, env config and App running as much manual as possible according to the requirements
* Developed connecting to [Elasticsearch 8.11.1](https://www.elastic.co/guide/en/elasticsearch/reference/current/release-notes-8.11.1.html)
* Build and run through Maven, maven-wrapper available within the code in github

### Reference Documentation
Create a new docker network, not mandatory:
```docker network create elastic```

Run ES and copy credentials, user 'elastic': 
```docker run --name es01 --net elastic -p 9200:9200 -it docker.elastic.co/elasticsearch/elasticsearch:8.11.1```

Store the created password in your env:
```export ELASTIC_PASSWORD="your_password"```

> To re-generate credentials, run:
```docker exec -it es01 /usr/share/elasticsearch/bin/elasticsearch-reset-password -u elastic```

Now copy the certificate from docker into your local:
> Create `temp` folder if not existing

```docker cp es01:/usr/share/elasticsearch/config/certs/http_ca.crt /temp/.```

Make a REST API call to Elasticsearch to test the container:
```curl --cacert http_ca.crt -u elastic:$ELASTIC_PASSWORD https://localhost:9200```

> Windows 11 (powershell) - Run in an admin terminal
> open CMD as admin and import the cert: ```Import-Certificate -FilePath \temp\http_ca.crt -CertStoreLocation Cert:\LocalMachine\Root | Out-Null```
> open https://localhost:9200 and enter user `elastic` and your generated pwd

Run the app via maven wrapper on the project:
```./mvnw spring-boot:run```

For convenience, you can use SwaggerUI and test in your browser:
http://localhost:8080/swagger-ui/index.html
