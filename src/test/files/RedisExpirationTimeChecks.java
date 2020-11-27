
import redis.clients.jedis.Jedis;

public class RedisTimeoutChecks {
    public static void saveSession(String id, Map session) {
        Jedis jedis = null; // Noncompliant {{Jedis should set the expiration time}}
        try {
            jedis = RedisUtil.getJedis();
            jedis.set(id.getBytes(), serialize(session));
        } catch (Exception e) {
            logger.error("保存session错误：", e);
            e.printStackTrace();
        } finally {
            jedis.close();
            logger.error(session.toString());
        }
    }

    public static void saveSession2(String id, Map session) {
        Jedis jedis = null;
        try {
            jedis =RedisUtil.getJedis();
            jedis.set(id.getBytes(), serialize(session));
            jedis.expire(id.getBytes(), RedisUtil.SESSION_TIME_OUT);
        } catch (Exception e) {
            logger.error("保存session错误：",e);
            e.printStackTrace();

        } finally {
            jedis.close();
            logger.error(session.toString());
        }
    }
}