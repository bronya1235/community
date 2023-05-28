package com.nowcoder.app.community.util;

/**
 * @Author: Bao
 * @Date: 2022/7/21-07-21-13:54
 * @Description com.nowcoder.app.community.util
 * @Function
 */
public class RedisKeyUtil {
	//分隔符
	public static final String SPLIT = ":";
	//key的前缀
	private static final String PREFIX_ENTITY_LIKE = "like:entity";
	private static final String PREFIX_USER_LIKE = "like:user";
	private static final String PREFIX_FOLLOWEE = "followee";
	private static final String PREFIX_FOLLOWER = "follower";
	private static final String PREFIX_KAPTCHA = "kaptcha";
	private static final String PREFIX_TICKET = "ticket";
	private static final String PREFIX_USER = "user";

	//key的格式 like:entity:entityType:entityId
	public static String getLikeEntityKey(int entityType,int entityId) {
		return PREFIX_ENTITY_LIKE+SPLIT+entityType+SPLIT+entityId;
	}

	//key的格式 like:entity:entityType:entityId
	public static String getLikeUserKey(int userId) {
		return PREFIX_USER_LIKE+SPLIT+userId;
	}
	//我的关注，指的是我关注的实体，格式：followee:userId:entityType—>存入的是关注的实体对象的id和时间
	public static String getFolloweeKey(int entityType,int userId){
		return PREFIX_FOLLOWEE+SPLIT+userId+SPLIT+entityType;
	}
	//某个实体拥有的粉丝：格式：follower:entityType:entityId->followerId
	public static String getFollowerKey(int entityType,int entityId) {
		return PREFIX_FOLLOWER+SPLIT+entityType+SPLIT+entityId;
	}
	//登录验证码
	public static String getKaptchaKey(String owner){
		/*验证码应该和用户进行绑定
		原先使用session实现时，发送了一个sessionId给客户端，以此来实现识别客户端
		现在使用redis来实现，所以需要给用户发一个临时的cookie，用来标识用户
		* */
		return PREFIX_KAPTCHA+SPLIT+owner;
	}

	//登录凭证
	public static String getTicketKey(String ticket){
		return PREFIX_TICKET+SPLIT+ticket;
	}

	//用户
	public static String getUserKey(int userId){
		return PREFIX_USER+SPLIT+userId;
	}
}
