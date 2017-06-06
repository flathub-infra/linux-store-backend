# Flathub backend (proof of concept)

**Note: at this time this just is a personal proof-of-concept not endorsed by the Flathub project**

This is a simple Spring Boot App implementing a backend for [flathub-store-frontend](https://github.com/jgarciao/flathub-store-frontend):
* Implements a system to get the list of apps and runtimes published in [Flathub](http://flathurg.org)
* Stores the information in a relational database
* Offers a simple REST API to get this information:
   
# Requirements

For the backend:
* Java 7+ (java-8-oracle recommended)
* maven
* appstream-java compiled and present in your local maven repository

For the script appstream-extractor (info below):
* flatpak
* ostree
* tar
* unzip

# How Flathub information is obtained

The list of Flathub's apps and runtimes is obtained from the ostree repository, extracting the contents of the appstream branch.

This is done using the provided script appstream-extractor. It can be run like this:

```
# Add flathub repo
flatpak remote-add --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Run the extractor script as root manually. 
# Once it works it can be run periodically in a cron job
 ./appstream-extractor 
 ```
The script will:
* Extract the appstream data to /var/lib/appstream-extractor/export-data 
* Update the file /var/lib/appstream-extractor/appstream-extractor.info
with information about the last exported data (ostree commit, ...)

Flathub-backend will parse this information periodically and store it in the database.

# Testing flathub-backend

You can run the backend using an in-memory-database from the command line:

```
mvn install
mvn spring-boot:run -Dspring.profiles.active=TEST 
```

Once the service is running go to http://localhost:8080/v1/apps/update to import the appstream data exported with the script appdata-extractor. 

Finally, fetch the data at http://localhost:8080/v1/apps


# Developing flathub-backend

This is just a proof of concept implemented with Spring Boot. Look at ApiController.java and follow the code to undertand how it works.

TODO:
* Appdata translations
* Search apps by name, keywords, catetories, ...
* Search themes
* Search runtimes
* Pagination
* Activity: events, ...
* Integration with GitHub for extra info (summiter name and photo, ...)
* Improve performance (ehcache, ...)
* Implement Rate Limiting