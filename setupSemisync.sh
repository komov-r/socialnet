# install semisync plugin

docker exec -it mysql-master  mysql -uroot -ppassword -e "INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_master.so';"
docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so';"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "INSTALL PLUGIN rpl_semi_sync_slave SONAME 'semisync_slave.so';"

# enable semisync
docker exec -it mysql-master  mysql -uroot -ppassword -e "SET GLOBAL rpl_semi_sync_master_enabled=1;"
docker exec -it mysql-slave-1 mysql -uroot -ppassword -e "SET GLOBAL rpl_semi_sync_slave_enabled=1;"
docker exec -it mysql-slave-2 mysql -uroot -ppassword -e "SET GLOBAL rpl_semi_sync_slave_enabled=1;"

# check variables and status
docker exec -it mysql-master  mysql -uroot -ppassword -e "SHOW VARIABLES LIKE 'rpl_semi_sync%';"
docker exec -it mysql-master  mysql -uroot -ppassword -e "SHOW STATUS LIKE 'Rpl_semi_sync%';
