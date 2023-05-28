package com.nowcoder.app.community.service;

import com.nowcoder.app.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-14:00
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class LikeService {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;

	//点赞方法
	public void addLike(int userId, int entityType, int entityId, int targetId) {
		//String key = RedisKeyUtil.getKey(entityType, entityId);
		////判断是否已经点过赞
		//Boolean isMember = redisTemplate.opsForSet().isMember(key, userId);
		//if(isMember){
		//	redisTemplate.opsForSet().remove(key, userId);
		//}else {
		//	redisTemplate.opsForSet().add(key, userId);
		//}
		//处理redis的事务
		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String entityKey = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
				String targetUserKey = RedisKeyUtil.getLikeUserKey(targetId);
				Boolean isMember = operations.opsForSet().isMember(entityKey, userId);
				operations.multi();
				if (isMember) {
					operations.opsForSet().remove(entityKey, userId);
					operations.opsForValue().decrement(targetUserKey);
				} else {
					operations.opsForSet().add(entityKey, userId);
					operations.opsForValue().increment(targetUserKey);
				}
				return operations.exec();
			}
		});
	}

	//查询实体点赞的数量
	public long findEntityLikeCount(int entityType, int entityId) {
		String key = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
		return redisTemplate.opsForSet().size(key);
	}

	//查询是否已经点过赞
	public int findEntityLikeStatus(int userId, int entityType, int entityId) {
		String key = RedisKeyUtil.getLikeEntityKey(entityType, entityId);
		//判断是否已经点过赞
		return redisTemplate.opsForSet().isMember(key, userId) ? 1 : 0;
	}

	//查询某个用户获得的赞数量
	public int findUserLikeCount(int userId) {
		String userKey = RedisKeyUtil.getLikeUserKey(userId);
		Integer integer = (Integer) redisTemplate.opsForValue().get(userKey);
		return integer == null ? 0 : integer;
	}
}
