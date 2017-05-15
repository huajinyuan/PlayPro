package cn.gtgs.base.playpro.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CheckUtil {
	public static boolean IsPhone(String phonestr) {

		boolean flag;
		// String phoneStr = phone.getText ( ).toString ( ).trim ( );
		if (StringUtils.isEmpty(phonestr)) {
			flag = false;
		} else {
			Pattern p = Pattern.compile("^1[3,4,5,7,8]+\\d{9}");
			Matcher m = p.matcher(phonestr);
			m.matches();
			if (m.matches()) {
				flag = true;
			} else {
				flag = false;
			}
		}

		return flag;
	}

	public static boolean IsEmail(String email) {
		Pattern pattern = Pattern.compile("^([a-zA-Z0-9_\\-\\.]+)@((\\[[0-9]{1,3}\\.[0-9]{1,3}\\.[0-9]{1,3}\\.)|(([a-zA-Z0-9\\-]+\\.)+))([a-zA-Z]{2,4}|[0-9]{1,3})(\\]?)$");
		Matcher matcher = pattern.matcher(email);
		return matcher.matches();
	}

	public static boolean IstruePWD(String pwd) {
		boolean flag = false;
		Pattern p = Pattern.compile("^[^\\s^\u4e00-\u9fa5]{6,20}$");
		Matcher m = p.matcher(pwd);
		m.matches();
		if (m.matches()) {
			flag = true;

		} else {
			flag = false;
		}
		return flag;
	}

	public static boolean IsStartWithLetter(String str) {
		if ((65 <= str.getBytes()[0] && str.getBytes()[0] <= 90) || (97 <= str.getBytes()[0] && str.getBytes()[0] <= 122)) {
			return true;
		} else {
			return false;
		}
	}

	/**
	 * 校验银行卡卡号
	 *
	 * @param cardId 卡号
	 * @return 是否有效卡号
	 */
	public static boolean checkBankCard(String cardId) {
		char bit = getBankCardCheckCode(cardId
				.substring(0, cardId.length() - 1));
		return bit != 'N' && cardId.charAt(cardId.length() - 1) == bit;
	}

	/**
	 * 从不含校验位的银行卡卡号采用 Luhm 校验算法获得校验位
	 *
	 * @param nonCheckCodeCardId 不含校验位的银行卡
	 * @return 校验结果
	 */
	public static char getBankCardCheckCode(String nonCheckCodeCardId) {
		if (nonCheckCodeCardId == null
				|| nonCheckCodeCardId.trim().length() == 0
				|| !nonCheckCodeCardId.matches("\\d+")) {
			// 如果传的不是数据返回N
			return 'N';
		}
		char[] chs = nonCheckCodeCardId.trim().toCharArray();
		int luhmSum = 0;
		for (int i = chs.length - 1, j = 0; i >= 0; i--, j++) {
			int k = chs[i] - '0';
			if (j % 2 == 0) {
				k *= 2;
				k = k / 10 + k % 10;
			}
			luhmSum += k;
		}
		return (luhmSum % 10 == 0) ? '0' : (char) ((10 - luhmSum % 10) + '0');
	}

}
