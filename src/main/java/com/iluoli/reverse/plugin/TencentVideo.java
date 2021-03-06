package com.iluoli.reverse.plugin;

import java.net.URLEncoder;
import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;

import cn.hutool.core.util.RandomUtil;
import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Configuration
public class TencentVideo extends BaseExecute{
	
	String appVersion = "1.0.143";
	String platform = "10201";
	String v = "3.5.57";
	String encryptVer = "9.1";
	String cKeyServer = "http://62.234.27.108:9797/";

	public TencentVideo(String url) {
		super(url);
	}

	@Override
	public String execute() {
		String start = "<link rel=\"canonical\" href=";
		HttpResponse response = requestNoHeader(url);
		String body = response.body();
		int indexOf = body.indexOf(start);
		body = body.substring(indexOf + start.length() + 1);
		int index = body.indexOf("\" />");
		body = body.substring(0,index);
		String[] split = body.split("/");
		String vid = split[split.length - 1].replaceAll(".html","");
		String[] split2 = url.split("/");
		String coverid = split2[split2.length - 1].replaceAll(".html","");
		
		String flowid = RandomUtil.randomString(32) + "_" + platform;
		String tm = String.valueOf(System.currentTimeMillis()).substring(0,10);
		String rfid = RandomUtil.randomString(32) + "_" + tm;
		String cKey = HttpUtil.get(cKeyServer + "?vid="+ vid +"&tm=" + tm);
		
		log.info("vid:{}",vid);
		log.info("coverid:{}",coverid);
		log.info("flowid:{}",flowid);
		log.info("tm:{}",tm);
		log.info("rfid:{}",rfid);
		log.info("url:{}",url);
		log.info("cKey:{}",cKey);
		
		String cookieStr = "eas_sid=c1W5J8g1C155s5J1a2Y834r4K0; pgv_pvid=6229304748; pgv_pvi=4063204352; tvfe_boss_uuid=cf6705d65812ef49; RK=FXJwrAcnef; ptcz=afb3ad2c1963b13ee5d6571a17735200c7d29971bf63d6b24044ef4c37618887; ts_uid=807040940; login_remember=qq; tvfe_search_uid=1e965db9-111e-496c-8c65-f96f1aac19f7; uin_cookie=o0724555508; ied_qq=o0724555508; LOLWebSet_AreaBindInfo_724555508=%257B%2522areaid%2522%253A%25229%2522%252C%2522areaname%2522%253A%2522%25E5%25BC%2597%25E9%259B%25B7%25E5%25B0%2594%25E5%258D%2593%25E5%25BE%25B7%2520%25E7%25BD%2591%25E9%2580%259A%2522%252C%2522sRoleId%2522%253A0%252C%2522roleid%2522%253A%2522724555508%2522%252C%2522rolename%2522%253A%2522AutoCnM%25E4%25B8%25BF%25E6%259D%259C%25E8%2595%25BE%25E6%2596%25AF%2522%252C%2522checkparam%2522%253A%2522lol%257Cyes%257C724555508%257C9%257C724555508*%257C%257C%257C%257CAutoCnM%2525E4%2525B8%2525BF%2525E6%25259D%25259C%2525E8%252595%2525BE%2525E6%252596%2525AF*%257C%257C%257C1587133113%2522%252C%2522md5str%2522%253A%2522E863FAC83B6B0F1D93A100AC15891CE4%2522%252C%2522roleareaid%2522%253A%25229%2522%252C%2522sPartition%2522%253A%25229%2522%257D; XWINDEXGREY=0; o_cookie=724555508; pac_uid=1_724555508; ptui_loginuin=814197449; pgv_info=ssid=s7236231633; ts_last=v.qq.com/; ts_refer=www.baidu.com/link; bucket_id=9231005; video_platform=2; video_guid=671143db9d110738; qv_als=//7oIEy4Vsv8olukA11602161655iWCKOQ==; Hm_lvt_eaa57ca47dacb4ad4f5a257001a3457c=1602161661; Hm_lpvt_eaa57ca47dacb4ad4f5a257001a3457c=1602161661; ptag=www_baidu_com|顶部导航区:头像; _qpsvr_localtk=0.6518428337708462; pgv_si=s8005358592; main_login=qq; vqq_vuserid=149223809; vqq_vusession=8AROSnd3MEsPWF2sLmfU1Q..; vqq_openid=338A829D4F3BA819F72613540A0D3EBA; vqq_appid=101483052; vqq_access_token=BDC0D68FED34EB836366DA64C204DB3C; qq_nick=%E7%88%B1%E8%90%9D%E8%8E%89%E7%9C%9F%E6%98%AF%E5%A4%AA%E5%A5%BD%E4%BA%86; qq_head=http://thirdqq.qlogo.cn/g?b=oidb&k=JaEiafVv6UXLCwDibu14dCPw&s=640&t=1555626538; lw_nick=%E7%88%B1%E8%90%9D%E8%8E%89%E7%9C%9F%E6%98%AF%E5%A4%AA%E5%A5%BD%E4%BA%86|0|http://thirdqq.qlogo.cn/g?b=oidb&k=JaEiafVv6UXLCwDibu14dCPw&s=640&t=1555626538|0; uid=226280444; ad_play_index=37";
//		String cookieStr = "";
		String[] split3 = cookieStr.split("; ");
		Map<String, String> cookieMap = new HashMap<String, String>();
		if(cookieStr != "" && cookieStr != null) {
			for(String str : split3) {
				cookieMap.put(str.split("=")[0], str.split("=")[1]);
			}
		}
		
		HttpRequest post = HttpUtil.createPost("https://vd.l.qq.com/proxyhttp");
		post.header("Referer" , url);
		String buildParam = buildParam(coverid, flowid, url, rfid, vid, cKey, tm , cookieMap);
		post.body(buildParam);
		//执行
		String res = post.execute().body();
		log.info(res);
		JSONArray ul = JSONObject.parseObject(res).getJSONObject("vinfo").getJSONObject("vl").getJSONArray("vi").getJSONObject(0).getJSONObject("ul").getJSONArray("ui");
		//这里拿数组最后一个，清晰度最高的
		JSONObject ui = ul.getJSONObject(ul.size()-1);
		log.info(ui + "");
		if(cookieStr == "") {
			//没有登录使用这种方式获取，但是如果是vip电影获取出来的只有5分钟
			return ui.getString("url") + ui.getJSONObject("hls").getString("pt");
		}
		//如果登录了可以使用这种方式获取
		return ui.getString("url");		
	}

	public static String get(String address) {
		return new TencentVideo(address).execute();
	}
	
	public static void main(String[] args) {
		System.out.println(TencentVideo.get("https://v.qq.com/x/cover/mzc00200ekacrrt.html"));
	}
	
	
	public String buildParam(String coverid,String flowid,String url,String rfid , String vid , String cKey , String tm , Map<String, String> cookieMap) {
		
		boolean haveCookie = false;
		if(!cookieMap.isEmpty()) {
			haveCookie = true;
		}
		
		
		StringBuffer adparam = new StringBuffer();
		adparam.append("ad_type=LD|KB|PVL").append("&");
		adparam.append("adaptor=2").append("&");
		adparam.append("appversion="+ appVersion).append("&");
		adparam.append("chid=0").append("&");
		adparam.append("coverid=" + coverid).append("&");
		adparam.append("dtype=1").append("&");
		adparam.append("flowid=" + flowid).append("&");
		adparam.append("from=0").append("&");
		adparam.append("guid=" + "d1384f3028935f6465a473a5a07113b5").append("&");
		adparam.append("live=0").append("&");
		adparam.append("pf=in").append("&");
		adparam.append("platform=" + platform).append("&");
		adparam.append("pf_ex=pc").append("&");
		adparam.append("plugin=1.0.0").append("&");
		adparam.append("pt=").append("&");
		adparam.append("pu=0").append("&");
		adparam.append("refer=" + url).append("&");
		adparam.append("req_type=1").append("&");
		adparam.append("resp_type=json").append("&");
		adparam.append("rfid=" + rfid).append("&");
		adparam.append("tpid=1").append("&");
		adparam.append("ty=web").append("&");
		adparam.append("url=" + url).append("&");
		adparam.append("v=" + v).append("&");
		adparam.append("vid=" + vid).append("&");
		adparam.append("vptag=www_baidu_com");
		if(haveCookie) {
			String uid = cookieMap.get("vqq_vuserid");
			String tkn = cookieMap.get("vqq_vusession");
			String opid = cookieMap.get("vqq_openid");
			String lt = cookieMap.get("main_login");
			String atkn = cookieMap.get("vqq_access_token");
			String appid = cookieMap.get("vqq_appid");
			
			adparam.append("&");
			adparam.append("uid=" + uid).append("&");
			adparam.append("tkn=" + tkn).append("&");
			adparam.append("lt=" + lt).append("&");
			adparam.append("opid=" + opid).append("&");
			adparam.append("atkn=" + atkn).append("&");
			adparam.append("appid=" + appid);
		}
			
		
		
		
		StringBuffer vinfoparam = new StringBuffer();
		vinfoparam.append("appVer=" + v).append("&");
		vinfoparam.append("cKey=" + cKey).append("&");
		vinfoparam.append("charge=0").append("&");
		vinfoparam.append("defaultfmt=auto").append("&");
		vinfoparam.append("defn=fhd").append("&");//取值：fhd（蓝光） shd（超清）   hd（高清）  sd（标清）
		vinfoparam.append("defnpayver=1").append("&");
		vinfoparam.append("defsrc=1").append("&");
		vinfoparam.append("dlver=2").append("&");
		vinfoparam.append("drm=32").append("&");
		vinfoparam.append("dtype=3").append("&");
		vinfoparam.append("ehost=" + url).append("&");
		vinfoparam.append("encryptVer=" + encryptVer).append("&");
		vinfoparam.append("fhdswitch=0").append("&");
		vinfoparam.append("flowid=" + flowid).append("&");
		vinfoparam.append("fp2p=1").append("&");
		vinfoparam.append("guid=" + "d1384f3028935f6465a473a5a07113b5").append("&");
		vinfoparam.append("hdcp=0").append("&");
		vinfoparam.append("host=v.qq.com").append("&");
		vinfoparam.append("isHLS=1").append("&");
		
		if(haveCookie) {
			String uid = cookieMap.get("vqq_vuserid");
			String tkn = cookieMap.get("vqq_vusession");
			String opid = cookieMap.get("vqq_openid");
			String lt = cookieMap.get("main_login");
			String atkn = cookieMap.get("vqq_access_token");
			String appid = cookieMap.get("vqq_appid");
			
			String logintoken = "{\"main_login\":\""+ lt +"\",\"openid\":\""+ opid +"\",\"appid\":\""+ appid +"\",\"access_token\":\""+atkn+"\",\"vuserid\":\""+uid+"\",\"vusession\":\""+tkn+"\"}";
			vinfoparam.append("logintoken=" + URLEncoder.encode(logintoken)).append("&");
		}
		
		
		vinfoparam.append("otype=ojson").append("&");
		vinfoparam.append("platform=" + platform).append("&");
		vinfoparam.append("refer=v.qq.com").append("&");
		vinfoparam.append("sdtfrom=v1010").append("&");
		vinfoparam.append("show1080p=1").append("&");
		vinfoparam.append("spadseg=1").append("&");
		vinfoparam.append("spau=1").append("&");
		vinfoparam.append("spaudio=15").append("&");
		vinfoparam.append("spgzip=1").append("&");
		vinfoparam.append("sphls=2").append("&");
		vinfoparam.append("sphttps=1").append("&");
		vinfoparam.append("spwm=4").append("&");
		vinfoparam.append("tm=" + tm).append("&");
		vinfoparam.append("unid=cdbb5f0a6aad11ea981ca042d48ad00a").append("&");
		vinfoparam.append("vid=" + vid);
		
		
		//构造参数时buid可取值
		//1.vinfoad（包含视频信息和广告）
		//2.onlyvinfo（只包含视频信息）
		return "{\"buid\":\"vinfoad\",\"adparam\":\"" + adparam.toString() + "\",\"vinfoparam\":\"" + vinfoparam.toString() + "\"}";
	}
}
