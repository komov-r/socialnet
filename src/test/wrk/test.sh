docker run --rm -v /media/roman/projects/otus/hl/social-network/src/test/wrk:/tmp/ --network host  williamyeh/wrk \
-t$1 -c$1 -d60 -s /tmp/$2 --latency \
http://localhost:8080
#http://localhost:8080/api/profiles
