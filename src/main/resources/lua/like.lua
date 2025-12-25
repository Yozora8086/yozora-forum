redis.replicate_commands()
-- KEYS[1] = like:forum:{forumId}
-- KEYS[2] = like:count:{forumId}  (计数)
-- ARGV[1] = userId
-- ARGV[2] = TTL 秒
local t = redis.call("TIME")
local ts = t[1] * 1000 + math.floor(t[2] / 1000)

local old = redis.call("HGET", KEYS[1], ARGV[1])
local oldStatus = 0

if old then
    oldStatus = cjson.decode(old).status
end

local status

if oldStatus == 1 then
    --取消点赞，点赞数-1
    status = 0
    redis.call("DECR", KEYS[2])
else
    --执行点赞，点赞数+1
    status = 1
    redis.call("INCR", KEYS[2])
end
--点赞状态和时间戳包装成json形式
local value = cjson.encode({status=status,ts=ts})

--写入数据
redis.call("HSET", KEYS[1], ARGV[1], value)

-- 设置 TTL（过期时间3天）
--if redis.call("PTTL", KEYS[1]) == -1 then
--    redis.call("PEXPIRE", KEYS[1], tonumber(ARGV[2]) * 1000)
--end
--if redis.call("PTTL", KEYS[2]) == -1 then
--    redis.call("PEXPIRE", KEYS[2], tonumber(ARGV[2]) * 1000)
--end
return status
