package net.ideahut.admin.central.object;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Getter;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.object.StorageKeyParam;
import net.ideahut.springboot.serializer.BinarySerializer;

@Getter
public class Redis {

	private final RedisTemplate<String, byte[]> template;
	private final String prefix;
	private final BinarySerializer serializer;
	
	private Redis(RedisTemplate<String, byte[]> template, String prefix, BinarySerializer serializer) {
		this.template = template;
		this.prefix = prefix;
		this.serializer = serializer;
	}
	
	public static Redis of(
		RedisTemplate<String, byte[]> template, 
		StorageKeyParam storageKeyParam, 
		ApplicationContext applicationContext,
		BinarySerializer serializer
	) {
		String prefix = FrameworkHelper.createStorageKeyPrefix(storageKeyParam, applicationContext);
		return new Redis(template, prefix, serializer);
	}
	
}
