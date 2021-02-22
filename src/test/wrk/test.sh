docker run --rm -v $(pwd):/tmp/ --network host  williamyeh/wrk \
-t$1 -c$1 -d120 -s /tmp/$2 --latency \
http://localhost:8080
#http://localhost:8080/api/profiles
