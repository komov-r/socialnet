
echo -e "\n start 1 minute read load test \n"
./src/test/wrk/test.sh 1  findProfiles.lua  > /tmp/readTest.log &

sleep 30

echo -e "\n stop one of the slaves \n"
docker kill mysql-slave-1

sleep 30

echo -e "\n stop one of the service instances \n"
docker kill socialnet-2