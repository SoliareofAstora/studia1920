docker run --rm --name postgres-dvd-rental-container -v ${PWD}/dumpfile/:/tmp/dumpfile/ postgres_dvd
docker exec postgres-dvd-rental-container pg_restore -U postgres -d dvdrental /tmp/dumpfile/imdb.tar

