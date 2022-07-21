package com.nowcoder.app.community.filters;

import org.apache.commons.lang3.CharUtils;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.HashMap;
import java.util.Map;

/**
 * @Author: Bao
 * @Date: 2022/7/15-07-15-15:35
 * @Description com.nowcoder.app.community.filters
 * @Function 使用一个前缀树来过滤敏感词
 */
@Component
public class SensitiveFilter {
	//打印日志
	private static final Logger logger = LoggerFactory.getLogger(SensitiveFilter.class);
	//需要有一个根节点
	private TrieNode root = new TrieNode();
	//需要一个常量，作为替换的符合
	private static final String REPLACEMENT = "***";

	@PostConstruct
	public void init() {
		//获取文件输入流读取txt文件
		try (
				InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream("sensitive-words.txt");
				BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream));
		) {
			String keyWord;
			while ((keyWord = reader.readLine()) != null) {
				addKeyWord(keyWord);
			}
		} catch (IOException e) {
			logger.error("加载文件失败：" + e.getMessage());
		}
	}

	/**
	 * 过滤敏感词方法
	 *
	 * @param text 待过滤的字符串
	 * @return 过滤后的字符串
	 */
	public String filter(String text) {
		//先检查text是否为空
		if (StringUtils.isBlank(text)) {
			return null;
		}
		//需要三个指针
		TrieNode temp = root;//1号指针，指向前缀树的根节点
		int begin = 0;//2号指针，指向text的文件中敏感词的开始位置
		int end = 0;//3号指针，指向text的文件中敏感词的结束位置
		StringBuilder stringBuilder = new StringBuilder();//用于输出结果
		char c;
		while (end < text.length()) {
			c = text.charAt(end);

			//需要判断一下是否是特殊符号,逻辑如下
			//如果是特殊字符，当TrieNode为root，说明还没有触发敏感词，把特殊字符记录下来
			//如果TrieNode不为root，说明begin已经触发敏感词了，那就单纯跳过这个字符
			if (isSymbol(c)) {
				if (temp == root) {
					stringBuilder.append(c);
					begin++;
				}
				end++;
				continue;
			}
			temp = temp.getSubNode(c);
			if (temp == null) {
				//如果子节点为空，说明begin这个字符不是敏感词
				stringBuilder.append(text.charAt(begin));
				begin++;
				end = begin;
				temp = root;
			} else if (temp.isKeyWordEnd()) {
				//把从begin到end进行替换
				stringBuilder.append(REPLACEMENT);
				begin = ++end;
				temp = root;
			} else {
				end++;
			}
		}
		stringBuilder.append(text.substring(begin));
		return stringBuilder.toString();

	}

	//判断一下是否是特殊字符
	private boolean isSymbol(Character c) {
		//c<0x2E80||c>0x9FFF 表示东亚范围的文字，不属于特殊字符
		return !CharUtils.isAsciiAlphanumeric(c) && (c < 0x2E80 || c > 0x9FFF);
	}

	//用于添加敏感词
	private void addKeyWord(String keyWord) {
		TrieNode tempNode = root;
		char c;
		for (int i = 0; i < keyWord.length(); i++) {
			c = keyWord.charAt(i);
			TrieNode subNode = tempNode.getSubNode(c);
			if (subNode == null) {
				//说明该字符还没被添加进去
				subNode = new TrieNode();
				tempNode.setSubNode(c, subNode);
			}
			//已经添加过了
			tempNode = subNode;
			//设置循环标志，如果当前字符是最后一个，就设置
			if (i == keyWord.length() - 1) {
				tempNode.setKeyWordEnd(true);
			}
		}
	}


	//新建一个内部类，标识树的节点
	private class TrieNode {
		//标志，标识是否是敏感词的最后一个词
		private boolean keyWordEnd = false;
		//子节点，使用一个map来封装
		private Map<Character, TrieNode> subNodes = new HashMap<>();

		public boolean isKeyWordEnd() {
			return keyWordEnd;
		}

		public void setKeyWordEnd(boolean keyWordEnd) {
			this.keyWordEnd = keyWordEnd;
		}

		public TrieNode getSubNode(Character key) {
			return subNodes.get(key);
		}

		public void setSubNode(Character key, TrieNode node) {
			subNodes.put(key, node);
		}
	}
}
