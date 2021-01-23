docker run --rm -v $(pwd):/tmp/ --network host  williamyeh/wrk \
-t$1 -c$1 -d25 -s /tmp/reqConf.lua --latency \
http://localhost:8080/api/profiles
