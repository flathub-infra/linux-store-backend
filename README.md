#  Note: this backend has been replaced in flathub.org by [this one](https://github.com/flathub/backend/) (Sept. 2020) 


# Linux Store backend


This is a simple Spring Boot App implementing a backend for [linux-store-frontend](https://github.com/jgarciao/linux-store-frontend):
* Implements a system to get the list of apps and runtimes published in flatpak repos like [Flathub](http://flathub.org) and others 
* Stores the information in a relational database
* Offers a simple REST API to get this information
   
# Requirements

For the backend:
* Java 7+ (java-8-oracle recommended)
* maven
* [appstream-appdata-java](https://github.com/jgarciao/appstream-appdata-java) compiled and present in your local maven repository

For the script appstream-extractor (info below):
* flatpak
* ostree
* tar
* unzip

# How appstream information is obtained

The list of apps and runtimes published in a flatpak repo is obtained extracting the contents of the appstream branch.

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

linux-store-backend will parse this information periodically and store it in the database.

# Testing linux-store-backend

You can run the backend using an in-memory database from the command line:

```
mvn install
mvn spring-boot:run -Dspring.profiles.active=TEST 
```

Once the service is running go to http://localhost:8080/v1/apps/update to import the appstream data exported with the script appdata-extractor. 

Finally, fetch the data at http://localhost:8080/v1/apps


# Improving linux-store-backend

This is just a proof of concept implemented with Spring Boot. Look at ApiController.java and follow the code to undertand how it works.

TODO:
* Improve appstream-extractor to manage multiple repos
* Import and store appdata translations
* Import and store appdata for all arches (arm, ...)
* Search apps by name, keywords, categories, ...
* Search themes
* Search runtimes
* Pagination
* Activity: events, ...
* Integration with GitHub to obtain extra info (publisher name and photo, ...)
* Improve performance (ehcache, ...)
* Rate Limiting
