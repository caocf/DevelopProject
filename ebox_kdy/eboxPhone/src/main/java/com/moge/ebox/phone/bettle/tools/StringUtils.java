package com.moge.ebox.phone.bettle.tools;

import android.annotation.SuppressLint;
import android.text.Layout;
import android.view.ViewTreeObserver;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.widget.TextView;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressLint({ "SimpleDateFormat" })
public class StringUtils {
	private static final Pattern emailer = Pattern
			.compile("\\w+([-+.]\\w+)*@\\w+([-.]\\w+)*\\.\\w+([-.]\\w+)*");
	private static final SimpleDateFormat dateFormater = new SimpleDateFormat(
			"yyyy-MM-dd HH:mm:ss");
	public static final SimpleDateFormat dateFormater2 = new SimpleDateFormat(
			"yyyy-MM-dd");
	static final String PLEASE_SELECT = "请选择...";

	public static String phpLongtoDate(String sdate) {
		try {
			Date date = new Date(Long.valueOf(sdate).longValue() * 1000L);
			return dateFormater.format(date);
		} catch (Exception e) {
		}
		return null;
	}

	public static String phpLongtoDate(String sdate, SimpleDateFormat format) {
		try {
			Date date = new Date(Long.valueOf(sdate).longValue() * 1000L);
			return format.format(date);
		} catch (Exception e) {
		}
		return null;
	}

	public static Date toDate(String sdate) {
		try {
			return dateFormater.parse(sdate);
		} catch (ParseException e) {
		}
		return null;
	}

	public static String friendly_time(String sdate) {
		Date time = toDate(sdate);
		if (time == null) {
			return "Unknown";
		}
		String ftime = "";
		Calendar cal = Calendar.getInstance();

		String curDate = dateFormater2.format(cal.getTime());
		String paramDate = dateFormater2.format(time);
		if (curDate.equals(paramDate)) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000L);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000L, 1L)
						+ "分钟前";
			else
				ftime = hour + "小时前";
			return ftime;
		}

		long lt = time.getTime() / 86400000L;
		long ct = cal.getTimeInMillis() / 86400000L;
		int days = (int) (ct - lt);
		if (days == 0) {
			int hour = (int) ((cal.getTimeInMillis() - time.getTime()) / 3600000L);
			if (hour == 0)
				ftime = Math.max(
						(cal.getTimeInMillis() - time.getTime()) / 60000L, 1L)
						+ "分钟前";
			else
				ftime = hour + "小时前";
		} else if (days == 1) {
			ftime = "昨天";
		} else if (days == 2) {
			ftime = "前天";
		} else if ((days > 2) && (days <= 10)) {
			ftime = days + "天前";
		} else if (days >= 1) {
			ftime = dateFormater.format(time);
		}
		return ftime;
	}

	public static boolean isToday(String sdate) {
		boolean b = false;
		Date time = toDate(sdate);
		Date today = new Date();
		if (time != null) {
			String nowDate = dateFormater2.format(today);
			String timeDate = dateFormater2.format(time);
			if (nowDate.equals(timeDate)) {
				b = true;
			}
		}
		return b;
	}

	public static boolean isURL(String url) {
		if (empty(url)) {
			return false;
		}
		Pattern urlPattern = Pattern
				.compile("[http|https]+[://]+[0-9A-Za-z:/[-]_#[?][=][.][&]]*");
		return urlPattern.matcher(url).matches();
	}

	public static boolean isEmail(String email) {
		if ((email == null) || (email.trim().length() == 0))
			return false;
		return emailer.matcher(email).matches();
	}

	public static String spaceEdit(String str) {
		int start = 0;
		int end = str.length() - 1;
		do {
			start++;

			if (start > end)
				break;
		} while (str.charAt(start) == ' ');

		while ((start <= end) && (str.charAt(end) == ' '))
			end--;
		return str.substring(start, end + 1);
	}

	public static boolean isMobileNO(String mobiles) {
		String regex = "^(((86|\\+86|0|)1[3458][0-9]{9})|(\\d{3,4}-(\\d{7,8})))$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(mobiles);
		return m.matches();
	}

	public static boolean isAddress(String address) {
		String regex = "^[一-龥]{2}[\\w]*";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(address);
		return m.matches();
	}

	public static boolean isChinese(String text) {
		String regex = "^[一-龥]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(text);
		if (m.matches()) {
			return true;
		}
		return false;
	}

	public static boolean isChineseName(String name) {
		String regex = "^[一-龥]+$";
		Pattern p = Pattern.compile(regex);
		Matcher m = p.matcher(name);
		if (m.matches()) {
			String surnames = "赵钱孙李周吴郑王冯陈褚卫蒋沈韩杨朱秦尤许何吕施张孔曹严华金魏陶姜戚谢邹喻柏水窦章云苏潘葛奚范彭郎鲁韦昌马苗凤花方俞任袁柳酆鲍史唐费廉岑薛雷贺倪汤滕殷罗毕郝邬安常乐于时傅皮卞齐康伍余元卜顾孟平黄和穆萧尹姚邵湛汪祁毛禹狄米贝明臧计伏成戴谈宋茅庞熊纪舒屈项祝董梁杜阮蓝闵席季麻强贾路娄危江童颜郭梅盛林刁锺徐邱骆高夏蔡田樊胡凌霍虞万支柯昝管卢莫经房裘缪干解应宗丁宣贲邓郁单杭洪包诸左石崔吉钮龚程嵇邢滑裴陆荣翁荀羊於惠甄麴家封芮羿储靳汲邴糜松井段富巫乌焦巴弓牧隗山谷车侯宓蓬全郗班仰秋仲伊宫宁仇栾暴甘钭历戎祖武符刘景詹束龙叶幸司韶郜黎蓟溥印宿白怀蒲邰从鄂索咸籍赖卓蔺屠蒙池乔阳郁胥能苍双闻莘党翟谭贡劳逄姬申扶堵冉宰郦雍却璩桑桂濮牛寿通边扈燕冀僪浦尚农温别庄晏柴瞿阎充慕连茹习宦艾鱼容向古易慎戈廖庾终暨居衡步都耿满弘匡国文寇广禄阙东欧殳沃利蔚越夔隆师巩厍聂晁勾敖融冷訾辛阚那简饶空曾毋沙乜养鞠须丰巢关蒯相查后荆红游竺权逮盍益桓公万俟司马上官欧阳夏侯诸葛闻人东方赫连皇甫尉迟公羊澹台公冶宗政濮阳淳于单于太叔申屠公孙仲孙轩辕令狐钟离宇文长孙慕容司徒司空召有舜叶赫那拉丛岳寸贰皇侨彤竭端赫实甫集象翠狂辟典良函芒苦其京中夕之章佳那拉冠宾香果依尔根觉罗依尔觉罗萨嘛喇赫舍里额尔德特萨克达钮祜禄他塔喇喜塔腊讷殷富察叶赫那兰库雅喇瓜尔佳舒穆禄爱新觉罗索绰络纳喇乌雅范姜碧鲁张廖张简图门太史公叔乌孙完颜马佳佟佳富察费莫蹇称诺来多繁戊朴回毓税荤靖绪愈硕牢买但巧枚撒泰秘亥绍以壬森斋释奕姒朋求羽用占真穰翦闾漆贵代贯旁崇栋告休褒谏锐皋闳在歧禾示是委钊频嬴呼大威昂律冒保系抄定化莱校么抗祢綦悟宏功庚务敏捷拱兆丑丙畅苟随类卯俟友答乙允甲留尾佼玄乘裔延植环矫赛昔侍度旷遇偶前由咎塞敛受泷袭衅叔圣御夫仆镇藩邸府掌首员焉戏可智尔凭悉进笃厚仁业肇资合仍九衷哀刑俎仵圭夷徭蛮汗孛乾帖罕洛淦洋邶郸郯邗邛剑虢隋蒿茆菅苌树桐锁钟机盘铎斛玉线针箕庹绳磨蒉瓮弭刀疏牵浑恽势世仝同蚁止戢睢冼种涂肖己泣潜卷脱谬蹉赧浮顿说次错念夙斯完丹表聊源姓吾寻展出不户闭才无书学愚本性雪霜烟寒少字桥板斐独千诗嘉扬善揭祈析赤紫青柔刚奇拜佛陀弥阿素长僧隐仙隽宇祭酒淡塔琦闪始星南天接波碧速禚腾潮镜似澄潭謇纵渠奈风春濯沐茂英兰檀藤枝检生折登驹骑貊虎肥鹿雀野禽飞节宜鲜粟栗豆帛官布衣藏宝钞银门盈庆喜及普建营巨望希道载声漫犁力贸勤革改兴亓睦修信闽北守坚勇汉练尉士旅五令将旗军行奉敬恭仪母堂丘义礼慈孝理伦卿问永辉位让尧依犹介承市所苑杞剧第零谌招续达忻六鄞战迟候宛励粘萨邝覃辜初楼城区局台原考妫纳泉老清德卑过麦曲竹百福言第五佟爱年笪谯哈墨南宫赏伯佴佘牟商西门东门左丘梁丘琴后况亢缑帅微生羊舌海归呼延南门东郭百里钦鄢汝法闫楚晋谷梁宰父夹谷拓跋壤驷乐正漆雕公西巫马端木颛孙子车督仉司寇亓官鲜于锺离盖逯库郏逢阴薄厉稽闾丘公良段干开光操瑞眭泥运摩伟铁迮付";
			if (surnames.indexOf(name.substring(0, 1)) != -1) {
				return true;
			}
		}
		return false;
	}

	public static boolean isPN(String pn) {
		pn = pn.replace(" ", "");
		Pattern p = Pattern.compile("\\d+");
		Matcher m = p.matcher(pn);
		return m.matches();
	}

	public static String ToDBC(String input) {
		char[] c = input.toCharArray();
		for (int i = 0; i < c.length; i++) {
			if (c[i] == '　') {
				c[i] = ' ';
			} else if ((c[i] > 65280) && (c[i] < 65375))
				c[i] = ((char) (c[i] - 65248));
		}
		return new String(c);
	}

	public static int toInt(String str, int defValue) {
		try {
			return Integer.parseInt(str);
		} catch (Exception localException) {
		}
		return defValue;
	}

	public static int toInt(Object obj) {
		if (obj == null)
			return 0;
		return toInt(obj.toString(), 0);
	}

	public static long toLong(String obj) {
		try {
			return Long.parseLong(obj);
		} catch (Exception localException) {
		}
		return 0L;
	}

	public static boolean toBool(String b) {
		try {
			return Boolean.parseBoolean(b);
		} catch (Exception localException) {
		}
		return false;
	}


	public static String getAlpha(String str) {
		if (str == null) {
			return "~";
		}
		if (str.trim().length() == 0) {
			return "~";
		}
		char c = str.trim().substring(0, 1).charAt(0);
		String str2 = String.valueOf(c);
		Pattern pattern = Pattern.compile("^[A-Za-z]+$");
		if (pattern.matcher(str2).matches()) {
			return str2.toUpperCase();
		}
		return "~";
	}

	public static String doEmpty(String str) {
		return doEmpty(str, "");
	}

	public static String doEmpty(String str, String defaultValue) {
		if ((str == null) || (str.equalsIgnoreCase("null"))
				|| (str.trim().equals("")) || (str.trim().equals("－请选择－")))
			str = defaultValue;
		else if (str.startsWith("null")) {
			str = str.substring(4, str.length());
		}
		return str.trim();
	}

	public static boolean notEmpty(Object o) {
		return (o != null) && (!"".equals(o.toString().trim()))
				&& (!"null".equalsIgnoreCase(o.toString().trim()))
				&& (!"undefined".equalsIgnoreCase(o.toString().trim()))
				&& (!"请选择...".equals(o.toString().trim()));
	}

	public static boolean empty(Object o) {
		return (o == null) || ("".equals(o.toString().trim()))
				|| ("null".equalsIgnoreCase(o.toString().trim()))
				|| ("undefined".equalsIgnoreCase(o.toString().trim()))
				|| ("请选择...".equals(o.toString().trim()));
	}

	public static boolean num(Object o) {
		int n = 0;
		try {
			n = Integer.parseInt(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0) {
			return true;
		}
		return false;
	}

	public static boolean decimal(Object o) {
		double n = 0.0D;
		try {
			n = Double.parseDouble(o.toString().trim());
		} catch (NumberFormatException e) {
			e.printStackTrace();
		}
		if (n > 0.0D) {
			return true;
		}
		return false;
	}

	public static String getUserNameByJid(String Jid) {
		if (empty(Jid)) {
			return null;
		}
		if (!Jid.contains("@")) {
			return Jid;
		}
		return Jid.split("@")[0];
	}

	public static String getJidByName(String userName, String jidFor) {
		if ((empty(jidFor)) || (empty(jidFor))) {
			return null;
		}
		return userName + "@" + jidFor;
	}

	public static String getJidByName(String userName) {
		String jidFor = "ahic.com.cn";
		return getJidByName(userName, jidFor);
	}

	public static String getMonthTomTime(String allDate) {
		return allDate.substring(5, 19);
	}

	public static String getMonthTime(String allDate) {
		return allDate.substring(5, 16);
	}
}
