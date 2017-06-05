# Flathub backend (proof of concept)

This is a simple Spring Boot App implementing a backend for flathub-frontend:
* Implements a system to obtain the information about apps and runtimes in Flathub and to store it in a database.
* Offers a simple API to get this information:
   
*Note: at this time this just is a personal proof-of-concept not endorsed by the Flathub project*

# Requirements

In order to run this web service you need to install: 

* Java 7+ (java-8-oracle recommended)
* maven
* appstream-java compiled and present in your local maven repository

# How Flathub information is obtained

The information about Flathub's apps and runtimes is obtained from the ostree repository, extracting the appstream branch.

This is done using the provided script appstream-extractor script:

```
# Add flathub repo
flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Run the extractor script as root manually
# Once it works run it in a cron job
 ./appstream-extractor 
 ```
The script will:
* Extract the appstream data in /var/lib/appstream-extractor/export-data 
* Update the file /var/lib/appstream-extractor/appstream-extractor.info
with information about the last exported data (ostree commit, ...)

Flathub-backend will parse this information periodically and store it in the database.

# Testing flathub-backend

To test this web service you can run the backend using an in-memory-database from the command line:

```
mvn install
mvn spring-boot:run -Dspring.profiles.active=TEST 
```

Once the service is running tell the service to gather the appstream data going to http://localhost:8080/v1/apps/update

Finally, fetch the data at http://localhost:8080/v1/apps


# Developing flathub-backend

This is just a proof of concept implemented with Spring Boot. Look at ApiController.java and follow the code to undertand how it works.
