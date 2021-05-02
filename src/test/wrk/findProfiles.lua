wrk.headers["Content-Type"] = "application/json"
wrk.headers["Authorization"] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxMDA1OTc3ODEiLCJleHAiOjIyMTc0OTExMDN9.uRsNK9rx_EXdFoV-118pXomhXMGGMiVzrGy3BteiDMAR68WFbXL94Y-gJPsQuty4XOSlZC7psOUIdEOgAiMoTg"
wrk.headers["Accept"] = "*/*"
wrk.path   = "/api/profiles"


names = {}
for line in io.lines("/tmp/names.csv") do
    local firstName, surname = line:match("%s*(.-),%s*(.-)")
    names[#names + 1] = { firstName = firstName, surname = surname }
end

request = function()
    row = names[math.random(1, #names)]
    fn = row.firstName
    sn = row.surname

    return wrk.format(nil, wrk.path .. "?firstName=" .. fn .. "&surname" .. sn)
end

response = function(status, header, body)
--      print("status:" .. status .. "\n" .. body .. "\n-------------------------------------------------\n");
end


done = function(summary, latency, requests)
--    io.write("------------------------------\n")
--
--    io.write(latency.min.."\n")
--    io.write(latency.max.."\n")
--    io.write(latency.mean.."\n")
--    io.write(#latency.."\n")
--    io.write("\n")
--    for cnt = 1, #latency do
--      print(latency(cnt))
--    end
end