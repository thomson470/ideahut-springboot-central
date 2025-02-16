package net.ideahut.admin.central.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import net.ideahut.admin.central.object.Redis;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.security.SecurityUser;

@Service
class TokenServiceImpl implements TokenService {
	
	private final Redis redis;
	
	@Autowired
	TokenServiceImpl(
		Redis redis
	) {
		this.redis = redis;
	}

	@Override
	public String create(SecurityUser user) {
		ValueOperations<String, byte[]> operations = redis.getTemplate().opsForValue();
		byte[] value = redis.getSerializer().serialize(SecurityUser.class, user);
		String token = UUID.randomUUID().toString();
		operations.set(redis.getPrefix() + "TOKEN-" + token, value, 300, TimeUnit.SECONDS);
		return token;
	}

	@Override
	public SecurityUser getUser(String token) {
		ValueOperations<String, byte[]> operations = redis.getTemplate().opsForValue();
		byte[] value = operations.getAndDelete(redis.getPrefix() + "TOKEN-" + token);
		ErrorHelper.throwIf(value == null, "Token is not found");
		return redis.getSerializer().deserialize(SecurityUser.class, value);
	}

}
