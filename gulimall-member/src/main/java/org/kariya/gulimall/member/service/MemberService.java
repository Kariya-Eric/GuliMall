package org.kariya.gulimall.member.service;

import com.baomidou.mybatisplus.extension.service.IService;
import org.kariya.common.utils.PageUtils;
import org.kariya.gulimall.member.entity.MemberEntity;

import java.util.Map;

/**
 * 会员
 *
 * @author kariya
 * @email eric15195016307@gmail.com
 * @date 2022-06-16 11:03:58
 */
public interface MemberService extends IService<MemberEntity> {

    PageUtils queryPage(Map<String, Object> params);
}

