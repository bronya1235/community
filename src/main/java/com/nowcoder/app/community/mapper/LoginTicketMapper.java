package com.nowcoder.app.community.mapper;

import com.nowcoder.app.community.pojo.LoginTicket;
import org.apache.ibatis.annotations.*;

/**
 * @Author: Bao
 * @Date: 2022/7/13-07-13-16:32
 * @Description com.nowcoder.app.community.mapper
 * @Function
 */
@Mapper
@Deprecated
public interface LoginTicketMapper {
	//插入数据
	@Insert({
			"insert into login_ticket(user_id,ticket,status,expired) ",
			"values(#{userId},#{ticket},#{status},#{expired})"
	})
	@Options(useGeneratedKeys = true, keyProperty = "id")
	int insertLoginTicket(LoginTicket ticket);

	//根据ticket查询
	@Select({
			"select id,user_id,ticket,status,expired ",
			"from login_ticket where ticket = #{ticket}"
	})
	LoginTicket selectByTicket(String ticket);

	//更新状态
	@Update({
			"update login_ticket set status = #{status} ",
			"where ticket = #{ticket}"
	})
	int updateLoginTicket(String ticket, int status);
}
