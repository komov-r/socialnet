wrk.headers["Content-Type"]  = "application/json"
wrk.headers["Authorization"] = "Bearer eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzUxMiJ9.eyJzdWIiOiIxIiwiZXhwIjoxNjE0NDY0Njg1fQ.rSaaZp-j3311Onu1Ua6XKvFzHkTRh6OQ4dFZL7Yiw4oO0pfgbMlZd6Obl_MtWF62PFYNwgjhCCRX7TlzjXGbuw"
wrk.headers["Accept"]        = "*/*"
wrk.path                     = "/api/profile"
wrk.method                   = "POST"


math.randomseed(os.time())

v =  math.random(1,100000)

cnt = 0

request = function()
    i = v + cnt

    cnt = cnt + 1

    return wrk.format(nil,  nil, nil, "{ \"username\": \"test " .. i .. " \",  \"firstName\": \"test" .. i ..  "\", \"surname\": \"test777771239\",   \"password\": \"test777771293\" })")
end

delay = function()
    return 5
end

response = function(status, header, body)
     print("status:" .. status .. "\n" .. body .. "\n-------------------------------------------------\n");
end
