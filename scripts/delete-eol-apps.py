#!/usr/bin/env python3

import os
import subprocess

import psycopg2
import yaml


def get_eol_apps():
    list_command = "flatpak remote-ls --user --all --columns=ref,options flathub"
    list_run = subprocess.Popen(list_command, shell=True, stdout=subprocess.PIPE, universal_newlines=True)
    output, _ = list_run.communicate()
    eol_indicators = ("eol=", "eol-rebase=")
    eol_apps = []

    for line in output.split('\n'):
        if len(line):
            splitline = line.split('\t')
            if len(splitline) < 2:
                continue
            else:
                ref, options = splitline

            kind, appid, _, _ = ref.split("/")
            
            if kind != "app":
                continue

            if any(x in options for x in eol_indicators):
                eol_apps.append(appid)

    return set(eol_apps)


def main():
    homedir = os.path.expanduser("~")
    store_config_file = os.path.join(homedir, "linux-store-backend.yml")
    with open(store_config_file) as f:
        store_config = yaml.load(f)

    _, _, db_host, db_name = store_config['spring']['datasource']['url'].split('/')
    pgsql_conn = {
            "host": db_host.split(":")[0],
            "port": db_host.split(":")[1],
            "password": store_config['spring']['datasource']['password'],
            "database": db_name,
    }

    conn = psycopg2.connect(**pgsql_conn)
    with conn.cursor() as c:
        c.execute("SELECT flatpak_app_id FROM app;")
        store_apps = {x[0] for x in c.fetchall()}

    eol_apps = get_eol_apps()
    apps_to_delete = store_apps & eol_apps

    for app_id in apps_to_delete:
        print("=> deleting {}".format(app_id))
        with conn.cursor() as c:
            c.execute("SELECT app_id FROM app where flatpak_app_id = %s;", (app_id,))
            db_id = c.fetchone()[0]

            c.execute("DELETE FROM app_category WHERE app_id = %s;", (db_id,))
            c.execute("DELETE FROM app_release WHERE app_id = %s;", (db_id,))
            c.execute("DELETE FROM Screenshot WHERE app_id = %s;", (db_id,))
            c.execute("DELETE FROM app WHERE app_id = %s;", (db_id,))

    conn.commit()
    conn.close()

if __name__ == "__main__":
    main()
