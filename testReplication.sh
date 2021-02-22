echo -e  \n "switch to no-replica config \n"
cp ./src/main/docker/datasource-no-replica.yml ./src/main/docker/datasources.yml
curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"

echo -e "\n start 1 minute read load test \n"
./src/test/wrk/test.sh 1  findProfiles.lua  > /tmp/readTest.log &

echo -e "\n wait 25 sec \n"
sleep 25

echo -e "\n switch to replica config \n"
cp ./src/main/docker/datasources-1replica.yml ./src/main/docker/datasources.yml
curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"

echo -e "\n wait 25 sec \n"
sleep 25

echo -e "\n start 1 minute write load test \n"
./src/test/wrk/test.sh 1  addProfiles.lua  > /tmp/writeTest.log &

echo -e "\n wait 10 sec\n"
sleep 10

echo -e "\n stop master \n"
docker stop mysql-master

echo -e "\n wait 10 sec\n"
sleep 10

echo -e "\n switch slave2 to slave1 \n"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "STOP SLAVE"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "CHANGE MASTER TO  MASTER_HOST='mysql-storage-1',  MASTER_USER='root', MASTER_PASSWORD='password'"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "START SLAVE"

echo -e "\n switch to replica config 2 \n"
cp ./src/main/docker/datasources-2replica.yml ./src/main/docker/datasources.yml
curl localhost:8080/actuator/refresh -d {} -H "Content-Type: application/json"
