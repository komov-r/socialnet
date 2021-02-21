
# export data from master

docker exec -it mysql-master  mysqldump -uroot -ppassword --databases socialnet --master-data > /tmp/dbdump.db

# import data to slave

docker exec -i mysql-slave-1  mysql -uroot -ppassword < /tmp/dbdump.db
docker exec -i mysql-slave-2  mysql -uroot -ppassword < /tmp/dbdump.db

# setup slave without gtid
#docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "CHANGE MASTER TO  MASTER_HOST='mysql-storage',  MASTER_USER='root', MASTER_PASSWORD='password',  MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=915"

#docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "CHANGE MASTER TO  MASTER_HOST='mysql-storage',  MASTER_USER='root', MASTER_PASSWORD='password',  MASTER_LOG_FILE='mysql-bin.000001', MASTER_LOG_POS=915"

# setup slave with  gtid

docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "CHANGE MASTER TO  MASTER_HOST='mysql-storage',  MASTER_USER='root', MASTER_PASSWORD='password'"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "CHANGE MASTER TO  MASTER_HOST='mysql-storage',  MASTER_USER='root', MASTER_PASSWORD='password'"

# start slave

docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "start slave;"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "start slave;"

# show slave status

docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "show slave status\G"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "show slave status\G"