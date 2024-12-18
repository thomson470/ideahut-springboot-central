package net.ideahut.admin.central.service;

import java.util.UUID;
import java.util.concurrent.TimeUnit;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.ValueOperations;
import org.springframework.stereotype.Service;

import net.ideahut.admin.central.AppConstants;
import net.ideahut.springboot.helper.ErrorHelper;
import net.ideahut.springboot.mapper.DataMapper;
import net.ideahut.springboot.security.SecurityUser;

@Service
class TokenServiceImpl implements TokenService {
	
	private final DataMapper dataMapper;
	private final RedisTemplate<String, byte[]> redisTemplate;
	
	@Autowired
	TokenServiceImpl(
		DataMapper dataMapper,
		RedisTemplate<String, byte[]> redisTemplate
	) {
		this.dataMapper = dataMapper;
		this.redisTemplate = redisTemplate;
	}

	@Override
	public String create(SecurityUser user) {
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = dataMapper.writeAsBytes(user, DataMapper.JSON);
		String token = UUID.randomUUID().toString();
		operations.set(AppConstants.Prefix.TOKEN + token, value, 300, TimeUnit.SECONDS);
		return token;
	}

	@Override
	public SecurityUser getUser(String token) {
		ValueOperations<String, byte[]> operations = redisTemplate.opsForValue();
		byte[] value = operations.getAndDelete(AppConstants.Prefix.TOKEN + token);
		ErrorHelper.throwIf(value == null, "Token is not found");
		return dataMapper.read(value, SecurityUser.class);
	}

}
