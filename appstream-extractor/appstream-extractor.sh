#!/bin/bash
#
# Copyright (C) 2017 Jorge García Oncins <jgarciao@gmail.com>
#
# Script to extract appstream data from flathub repo

APPSTREAM_EXTRACTOR_HOME=/var/lib/appstream-extractor

APPSTREAM_EXTRACTOR_INFO=$APPSTREAM_EXTRACTOR_HOME/appstream-extractor.info
APPSTREAM_EXTRACTOR_DEST_FOLDER=$APPSTREAM_EXTRACTOR_HOME/export-data

FLATPAK_OSTREE_REPO_PATH=/var/lib/flatpak/repo/
FLATPAK_REMOTE_NAME=flathub
FLATPAK_REF_APPSTREAM_X86_64=flathub/appstream/x86_64 

function check-required-programs {
	command -v flatpak >/dev/null 2>&1 || { echo >&2 "I require flatpak but it's not installed.  Aborting."; exit 1; }
	command -v ostree >/dev/null 2>&1 || { echo >&2 "I require ostree but it's not installed.  Aborting."; exit 1; }
	command -v tar >/dev/null 2>&1 || { echo >&2 "I require tar but it's not installed.  Aborting."; exit 1; }
	command -v gunzip >/dev/null 2>&1 || { echo >&2 "I require gunzip but it's not installed.  Aborting."; exit 1; }
}

function create-required-folders {

	mkdir -p $APPSTREAM_EXTRACTOR_DEST_FOLDER;
	if [ $? -ne 0 ]; then
		exit
	fi;

}


function update-appstream {
	echo "Updating appstream info for remote: $FLATPAK_REMOTE_NAME"
	/usr/bin/timeout 300s /usr/bin/flatpak update $FLATPAK_REMOTE_NAME --appstream
}

function extract-appstream {

	COMMIT_DATE_TEMP=`/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH show $FLATPAK_REF_APPSTREAM_X86_64 | grep "Date:"`
	COMMIT_DATE_PREFIX="Date:  "
	COMMIT_DATE=${COMMIT_DATE_TEMP#$COMMIT_DATE_PREFIX}
	DATE_WITHOUT_WHITESPACES=${COMMIT_DATE// /.}

	APPSTREAM_EXTRACT_FILE="$APPSTREAM_EXTRACTOR_DEST_FOLDER/appstream-$FLATPAK_REMOTE_NAME-x86_64-$DATE_WITHOUT_WHITESPACES.tar.gz"
	APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER=${APPSTREAM_EXTRACT_FILE%".tar.gz"}

	echo "REPO=$FLATPAK_REMOTE_NAME"                         > $APPSTREAM_EXTRACTOR_INFO
	echo "COMMIT=$COMMIT"                                   >> $APPSTREAM_EXTRACTOR_INFO
	echo "DATE=$COMMIT_DATE"                                >> $APPSTREAM_EXTRACTOR_INFO
	echo "FILE=$APPSTREAM_EXTRACT_FILE"                     >> $APPSTREAM_EXTRACTOR_INFO
	echo "EXPORT_DATA=$APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER" >> $APPSTREAM_EXTRACTOR_INFO

	echo "Extracting appstream info $APPSTREAM_EXTRACT_FILE"
	/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH  export $FLATPAK_REF_APPSTREAM_X86_64 | /bin/gzip > $APPSTREAM_EXTRACT_FILE

	#echo "Uncopressing $APPSTREAM_EXTRACT_FILE"
	mkdir $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER
	/bin/tar -xf $APPSTREAM_EXTRACT_FILE -C $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER
	
	#echo "Uncopressing $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER/appstream.xml.gz"
	/bin/gunzip --force $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER/appstream.xml.gz
}

#######################################################################################################################################

# Check requirements
check-required-programs

# Create folders required by this script
if [ ! -d "$APPSTREAM_EXTRACTOR_HOME" ]; then
 	 
	echo "This script requires to create this folder $APPSTREAM_EXTRACTOR_HOME"
	echo "Do you want to continue?"
	select yn in "Yes" "No"; do
	    case $yn in
		Yes ) create-required-folders;
		      break;;
		No ) exit;;
	    esac
	done
fi

# Update appstream info from repo
update-appstream

# Get current commit from appstream ostree log
COMMIT_TEMP=`/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH show $FLATPAK_REF_APPSTREAM_X86_64 | grep "commit"`
COMMIT_PREFIX="commit "
COMMIT=${COMMIT_TEMP#$COMMIT_PREFIX}

# Extract appstream-data if first run or current commit != previous commit
if [ ! -f $APPSTREAM_EXTRACTOR_INFO ]
then
	extract-appstream
else
	PREVIOUS_COMMIT_TEMP=`grep COMMIT= $APPSTREAM_EXTRACTOR_INFO`
	PREVIOUS_COMMIT_PREFIX="COMMIT="
	PREVIOUS_COMMIT=${PREVIOUS_COMMIT_TEMP#$PREVIOUS_COMMIT_PREFIX}

	if [ "$COMMIT" != "$PREVIOUS_COMMIT" ]
	then
		extract-appstream
	fi
fi




