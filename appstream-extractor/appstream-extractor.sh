#!/bin/bash
#
# Copyright (C) 2017-2018 Jorge García Oncins <jgarciao@gmail.com>
#
# Script to extract appstream data from flathub repo

APPSTREAM_EXTRACTOR_HOME=/var/lib/appstream-extractor
APPSTREAM_EXTRACTOR_DEST_FOLDER=$APPSTREAM_EXTRACTOR_HOME/export-data
FLATPAK_OSTREE_REPO_PATH=$HOME/.local/share/flatpak/repo/


function check-required-programs {
	command -v flatpak >/dev/null 2>&1 || { echo >&2 "I require flatpak but it's not installed.  Aborting."; exit 1; }
	command -v ostree >/dev/null 2>&1 || { echo >&2 "I require ostree but it's not installed.  Aborting."; exit 1; }
	command -v tar >/dev/null 2>&1 || { echo >&2 "I require tar but it's not installed.  Aborting."; exit 1; }
	command -v gunzip >/dev/null 2>&1 || { echo >&2 "I require gunzip but it's not installed.  Aborting."; exit 1; }
	command -v find >/dev/null 2>&1 || { echo >&2 "I require find but it's not installed.  Aborting."; exit 1; }
}

function check-required-folders {

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

	if [ ! -d "$APPSTREAM_EXTRACTOR_DEST_FOLDER" ]; then
		
		echo "This script requires to create this folder $APPSTREAM_EXTRACTOR_DEST_FOLDER"
		echo "Do you want to continue?"
		select yn in "Yes" "No"; do
			case $yn in
			Yes ) create-required-folders;
				break;;
			No ) exit;;
			esac
		done
	fi
}

function create-required-folders {
	mkdir -p $APPSTREAM_EXTRACTOR_DEST_FOLDER;
	if [ $? -ne 0 ]; then
		exit
	fi;
}

function extract-files-from-ostree {

	COMMIT_DATE_TEMP=`/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH show $FLATPAK_APPSTREAM_REF | grep "Date:"`
	COMMIT_DATE_PREFIX="Date:  "
	COMMIT_DATE=${COMMIT_DATE_TEMP#$COMMIT_DATE_PREFIX}
	DATE_WITHOUT_WHITESPACES=${COMMIT_DATE// /.}

	APPSTREAM_EXTRACT_FILE="$APPSTREAM_EXTRACTOR_DEST_FOLDER/appstream-$FLATPAK_REMOTE_NAME-$ARCH-$DATE_WITHOUT_WHITESPACES.tar.gz"
	APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER=${APPSTREAM_EXTRACT_FILE%".tar.gz"}
	FLATPAK_REMOTE_INFO_FILE="$APPSTREAM_EXTRACTOR_DEST_FOLDER/appstream-$FLATPAK_REMOTE_NAME-$ARCH-$DATE_WITHOUT_WHITESPACES-remote-info.txt"

	echo "REPO=$FLATPAK_REMOTE_NAME"                         > $REMOTE_INFO_DESCRIPTOR
	echo "COMMIT=$COMMIT"                                   >> $REMOTE_INFO_DESCRIPTOR
	echo "DATE=$COMMIT_DATE"                                >> $REMOTE_INFO_DESCRIPTOR
	echo "FILE=$APPSTREAM_EXTRACT_FILE"                     >> $REMOTE_INFO_DESCRIPTOR
	echo "EXPORT_DATA=$APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER" >> $REMOTE_INFO_DESCRIPTOR
	echo "REMOTE_INFO=$FLATPAK_REMOTE_INFO_FILE"            >> $REMOTE_INFO_DESCRIPTOR

	echo "Extracting appstream info $APPSTREAM_EXTRACT_FILE"
	/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH  export $FLATPAK_APPSTREAM_REF | /bin/gzip > $APPSTREAM_EXTRACT_FILE

	mkdir $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER
	/bin/tar -xf $APPSTREAM_EXTRACT_FILE -C $APPSTREAM_EXTRACT_FILE_UNZIP_FOLDER

	# Create file with last ostree commit per app and other info
	flatpak remote-ls --user -d $FLATPAK_REMOTE_NAME  --arch $ARCH > $FLATPAK_REMOTE_INFO_FILE

	echo "Cleaning up unused files in $APPSTREAM_EXTRACTOR_DEST_FOLDER"
	find $APPSTREAM_EXTRACTOR_DEST_FOLDER/appstream-$FLATPAK_REMOTE_NAME-$ARCH* -mtime +1 -exec rm -fr {} \; 2> /dev/null

}


function extract-appstream-by-arch {

	FLATPAK_REMOTE_NAME=$1
	ARCH=$2

	FLATPAK_APPSTREAM_REF=flathub/appstream2/$ARCH
	REMOTE_INFO_DESCRIPTOR=$APPSTREAM_EXTRACTOR_HOME/$FLATPAK_REMOTE_NAME-$ARCH.info

	echo "Getting info for remote $FLATPAK_REMOTE_NAME and arch $ARCH ..."

	# Update appstream info from repo
	/usr/bin/timeout 300s /usr/bin/flatpak --user update --appstream --arch=$ARCH

	# Get current commit from appstream ostree log
	COMMIT_TEMP=`/usr/bin/ostree --repo=$FLATPAK_OSTREE_REPO_PATH show $FLATPAK_APPSTREAM_REF | grep "commit"`
	COMMIT_PREFIX="commit "
	COMMIT=${COMMIT_TEMP#$COMMIT_PREFIX}

	# Extract appstream-data if first run or current commit != previous commit
	# and update REMOTE_INFO_DESCRIPTOR file
	if [ ! -f $REMOTE_INFO_DESCRIPTOR ]
	then
		extract-files-from-ostree
	else
		PREVIOUS_COMMIT_TEMP=`grep COMMIT= $REMOTE_INFO_DESCRIPTOR`
		PREVIOUS_COMMIT_PREFIX="COMMIT="
		PREVIOUS_COMMIT=${PREVIOUS_COMMIT_TEMP#$PREVIOUS_COMMIT_PREFIX}

		if [ "$COMMIT" != "$PREVIOUS_COMMIT" ]
		then
			extract-files-from-ostree
		fi
	fi

	echo

}


#######################################################################################################################################

# Check requirements
check-required-programs
check-required-folders

# Configure remote
flatpak remote-add --user --if-not-exists flathub https://flathub.org/repo/flathub.flatpakrepo

# Extract appstream info
extract-appstream-by-arch flathub x86_64
extract-appstream-by-arch flathub i386
extract-appstream-by-arch flathub arm
extract-appstream-by-arch flathub aarch64

# Clean old appstream data
ostree --repo=$HOME/.local/share/flatpak/repo/ prune --refs-only --depth=0


