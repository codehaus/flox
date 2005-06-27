#!/bin/sh
DATABASE=flox
USERNAME=flox_dbo
PASSWORD=$USERNAME

psql -d template1 -U postgres -c "DROP DATABASE $DATABASE"
psql -d template1 -U postgres -c "DROP USER $USERNAME"
psql -d template1 -U postgres -c "CREATE USER $USERNAME WITH PASSWORD '$PASSWORD'"
psql -d template1 -U postgres -c "CREATE DATABASE $DATABASE WITH OWNER $USERNAME"