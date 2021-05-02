SCRIPT=`realpath $0`
SCRIPTPATH=`dirname $SCRIPT`

docker run --rm -v $SCRIPTPATH:/tmp/ --network host  williamyeh/wrk \
-t$1 -c$1 -d90 -s /tmp/$2 --latency \
http://localhost:8080
#http://localhost:8080/api/profiles
