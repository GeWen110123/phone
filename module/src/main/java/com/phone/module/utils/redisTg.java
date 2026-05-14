package com.phone.module.utils;

import io.lettuce.core.RedisClient;
import io.lettuce.core.RedisURI;
import io.lettuce.core.api.StatefulRedisConnection;

import java.time.Duration;
import java.time.temporal.ChronoUnit;

public class redisTg {
   public static RedisClient client;
   public static StatefulRedisConnection<String, String> connection;
   static {
	   RedisURI redisUri = RedisURI.builder()
               .withHost("165.232.168.134").withPort(6388).withPassword("uBsR2ipTrfBv2vtL")
               .withTimeout(Duration.of(10, ChronoUnit.SECONDS))
               .build();
       client = RedisClient.create(redisUri);
        connection = client.connect();
   }
}
