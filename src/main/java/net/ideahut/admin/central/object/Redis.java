package net.ideahut.admin.central.object;

import java.io.Serializable;

import org.springframework.context.ApplicationContext;
import org.springframework.data.redis.core.RedisTemplate;

import lombok.Getter;
import net.ideahut.springboot.helper.FrameworkHelper;
import net.ideahut.springboot.object.StorageKeyParam;
import net.ideahut.springboot.serializer.BinarySerializer;

@Getter
public class Redis implements Serializable {
	private static final long serialVersionUID = -3452080679991960286L;
	
	private final transient RedisTemplate<String, byte[]> template;
	private final String prefix;
	private final transient BinarySerializer serializer;
	
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
