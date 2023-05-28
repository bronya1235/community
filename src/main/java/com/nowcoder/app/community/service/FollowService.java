package com.nowcoder.app.community.service;

import com.nowcoder.app.community.pojo.User;
import com.nowcoder.app.community.util.CommunityConstant;
import com.nowcoder.app.community.util.RedisKeyUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.RedisOperations;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.SessionCallback;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-14:00
 * @Description com.nowcoder.app.community.service
 * @Function
 */
@Service
public class FollowService implements CommunityConstant {
	@Autowired
	private RedisTemplate<String, Object> redisTemplate;
	@Autowired
	private UserService userService;

	//关注方法
	public void addFollow(int userId, int entityType, int entityId) {

		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				operations.multi();
				operations.opsForZSet().add(followeeKey, entityId, System.currentTimeMillis());
				operations.opsForZSet().add(followerKey, userId, System.currentTimeMillis());
				return operations.exec();
			}
		});
	}


	//取消关注方法
	public void unFollow(int userId, int entityType, int entityId) {
		redisTemplate.execute(new SessionCallback() {
			@Override
			public Object execute(RedisOperations operations) throws DataAccessException {
				String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
				String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
				operations.multi();
				operations.opsForZSet().remove(followeeKey, entityId);
				operations.opsForZSet().remove(followerKey, userId);
				return operations.exec();
			}
		});
	}
	//查询关注的实体数量
	public long findFolloweeCount(int userId,int entityType){
		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
		return redisTemplate.opsForZSet().zCard(followeeKey);
	}

	//查询实体的粉丝数量
	public long findFollowerCount(int entityId,int entityType){
		String followerKey = RedisKeyUtil.getFollowerKey(entityType, entityId);
		return redisTemplate.opsForZSet().zCard(followerKey);
	}

	//查询当前用户是否已经关注某个实体
	public boolean findFolloweeStatus(int userId,int entityType,int entityId){
		String followeeKey = RedisKeyUtil.getFolloweeKey(entityType, userId);
		return redisTemplate.opsForZSet().score(followeeKey, entityId)!=null;
	}

	//查询关注的实体,支持分页
	public List<Map<String,Object>> findFollowee(int userId,int offset,int limit){
		String followeeKey = RedisKeyUtil.getFolloweeKey(ENTITY_TYPE_USER, userId);
		Set<Object> followeeIds = redisTemplate.opsForZSet().reverseRange(followeeKey, offset, offset+limit-1);
		if(followeeIds==null){
			return null;
		}
		List<Map<String,Object>> list = new ArrayList<>();
		Integer targetId;
		for (Object id : followeeIds) {
			Map<String,Object> map = new HashMap<>();
			targetId = (Integer)id;
			User user = userService.findUserById(targetId);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followeeKey, targetId);
			map.put("followTime",new Date(score.longValue()));
			list.add(map);
		}
		return list;
	}


	//查询实体的粉丝,支持分页
	public List<Map<String,Object>> findFollower(int userId,int offset,int limit){
		String followerKey = RedisKeyUtil.getFollowerKey(ENTITY_TYPE_USER, userId);
		Set<Object> followeeIds = redisTemplate.opsForZSet().reverseRange(followerKey, offset, offset+limit-1);
		if(followeeIds==null){
			return null;
		}
		List<Map<String,Object>> list = new ArrayList<>();
		Integer targetId;
		for (Object id : followeeIds) {
			Map<String,Object> map = new HashMap<>();
			targetId = (Integer)id;
			User user = userService.findUserById(targetId);
			map.put("user", user);
			Double score = redisTemplate.opsForZSet().score(followerKey, targetId);
			map.put("followTime",new Date(score.longValue()));
			list.add(map);
		}
		return list;
	}
}
