package cn.cherish.blog.utils;

import com.alibaba.fastjson.JSON;
import lombok.ToString;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * Created by Cherish on 2017/1/12.
 */
@ToString
public class WeatherUtil {

    /**
     * aqi : {"city":{"aqi":"65","co":"1","no2":"39","o3":"13","pm10":"58","pm25":"46","qlty":"良","so2":"7"}}
     * basic : {"city":"广州","cnty":"中国","id":"CN101280101","lat":"23.108000","lon":"113.265000","update":{"loc":"2017-01-12 10:52","utc":"2017-01-12 02:52"}}
     * daily_forecast : [{"astro":{"sr":"07:10","ss":"18:00"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2017-01-12","hum":"73","pcpn":"0.7","pop":"78","pres":"1018","tmp":{"max":"15","min":"11"},"uv":"1","vis":"10","wind":{"deg":"9","dir":"北风","sc":"3-4","spd":"16"}},{"astro":{"sr":"07:10","ss":"18:01"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2017-01-13","hum":"76","pcpn":"0.9","pop":"100","pres":"1018","tmp":{"max":"14","min":"10"},"uv":"3","vis":"10","wind":{"deg":"4","dir":"无持续风向","sc":"微风","spd":"2"}},{"astro":{"sr":"07:10","ss":"18:01"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2017-01-14","hum":"76","pcpn":"1.5","pop":"92","pres":"1019","tmp":{"max":"15","min":"12"},"uv":"4","vis":"5","wind":{"deg":"24","dir":"无持续风向","sc":"微风","spd":"0"}},{"astro":{"sr":"07:10","ss":"18:02"},"cond":{"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"},"date":"2017-01-15","hum":"78","pcpn":"1.5","pop":"94","pres":"1021","tmp":{"max":"13","min":"10"},"uv":"5","vis":"10","wind":{"deg":"27","dir":"无持续风向","sc":"微风","spd":"1"}},{"astro":{"sr":"07:10","ss":"18:03"},"cond":{"code_d":"104","code_n":"101","txt_d":"阴","txt_n":"多云"},"date":"2017-01-16","hum":"71","pcpn":"2.4","pop":"98","pres":"1022","tmp":{"max":"17","min":"12"},"uv":"5","vis":"10","wind":{"deg":"27","dir":"无持续风向","sc":"微风","spd":"6"}},{"astro":{"sr":"07:10","ss":"18:04"},"cond":{"code_d":"101","code_n":"100","txt_d":"多云","txt_n":"晴"},"date":"2017-01-17","hum":"63","pcpn":"0.0","pop":"0","pres":"1022","tmp":{"max":"19","min":"12"},"uv":"-999","vis":"10","wind":{"deg":"114","dir":"无持续风向","sc":"微风","spd":"3"}},{"astro":{"sr":"07:10","ss":"18:04"},"cond":{"code_d":"100","code_n":"100","txt_d":"晴","txt_n":"晴"},"date":"2017-01-18","hum":"63","pcpn":"0.0","pop":"0","pres":"1022","tmp":{"max":"20","min":"13"},"uv":"-999","vis":"10","wind":{"deg":"126","dir":"无持续风向","sc":"微风","spd":"4"}}]
     * hourly_forecast : [{"date":"2017-01-12 13:00","hum":"77","pop":"58","pres":"1019","tmp":"18","wind":{"deg":"4","dir":"北风","sc":"3-4","spd":"17"}},{"date":"2017-01-12 16:00","hum":"76","pop":"58","pres":"1017","tmp":"18","wind":{"deg":"2","dir":"北风","sc":"3-4","spd":"17"}},{"date":"2017-01-12 19:00","hum":"77","pop":"60","pres":"1018","tmp":"17","wind":{"deg":"4","dir":"北风","sc":"微风","spd":"14"}},{"date":"2017-01-12 22:00","hum":"78","pop":"81","pres":"1019","tmp":"15","wind":{"deg":"2","dir":"北风","sc":"微风","spd":"13"}}]
     * now : {"cond":{"code":"104","txt":"阴"},"fl":"12","hum":"79","pcpn":"0","pres":"1019","tmp":"14","vis":"7","wind":{"deg":"30","dir":"东北风","sc":"3-4","spd":"14"}}
     * status : ok
     * suggestion : {"air":{"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"},"comf":{"brf":"较舒适","txt":"白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"},"cw":{"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"},"drsg":{"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"},"flu":{"brf":"极易发","txt":"将有一次强降温过程，天气寒冷，且空气湿度较大，极易发生感冒，请特别注意增加衣服保暖防寒。"},"sport":{"brf":"较不宜","txt":"有降水，且风力较强，推荐您在室内进行各种健身休闲运动；若坚持户外运动，请注意防风保暖。"},"trav":{"brf":"适宜","txt":"有降水，虽然风稍大，但温度适宜，适宜旅游，可不要错过机会呦！"},"uv":{"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}}
     */

    private AqiBean aqi;
    private BasicBean basic;
    private NowBean now;
    private String status;
    private SuggestionBean suggestion;
    private List<DailyForecastBean> daily_forecast;
    private List<HourlyForecastBean> hourly_forecast;

    private static final String httpUrl = "http://apis.baidu.com/heweather/weather/free";

    /*public static void main(String[] args) {
        //城市名称，国内城市支持中英文，国际城市支持英文
        String content = "天气广州";
        String city = content.replaceAll("^天气", "").trim();
        System.out.println("city=" + city);
        System.out.println(queryByCity(city));
    }*/

    public static String queryByCity(String city) {
        String result = "不好意思，查无此城！";
        //城市名称，国内城市支持中英文，国际城市支持英文
        String httpArg = "city="+city.trim();
        System.out.println("httpArg:" + httpArg);
        try {
            String jsonResult = doQuery(httpUrl, httpArg);

            if(jsonResult.indexOf("ok") > 0){

                String s = jsonResult.substring(jsonResult.indexOf("[")+1, jsonResult.lastIndexOf("]"));

                WeatherUtil weatherUtil = JSON.parseObject(s, WeatherUtil.class);

                StringBuilder sb = new StringBuilder(512);
                sb.append("城市："+weatherUtil.basic.getCity()+","+weatherUtil.basic.getCnty());
                if (weatherUtil.aqi != null) {
                    AqiBean.CityBean cityBean = weatherUtil.aqi.getCity();
                    sb.append(",空气质量指数"+cityBean.getAqi()+"·"+cityBean.getQlty()+",Pm2.5为"+cityBean.getPm25()+"微克每立方米\r\n");
                }else{
                    sb.append("\r\n");
                }

                sb.append("天气："+weatherUtil.now.getWind().getDir()+","+weatherUtil.now.getCond().getTxt()+","+weatherUtil.now.getTmp() +"度\r\n");
                if (weatherUtil.suggestion != null) {
                    sb.append("空气："+weatherUtil.suggestion.getAir().getTxt() +"\r\n");
                    sb.append(weatherUtil.suggestion.getComf().getBrf()+"："+weatherUtil.suggestion.getComf().getTxt() +"\r\n");
    //            sb.append(weatherUtil.suggestion.getCw().getBrf()+"："+weatherUtil.suggestion.getCw().getTxt() +"\r\n");

                    sb.append(weatherUtil.suggestion.getDrsg().getBrf()+"："+weatherUtil.suggestion.getDrsg().getTxt() +"\r\n");
                    sb.append(weatherUtil.suggestion.getFlu().getBrf()+"流感："+weatherUtil.suggestion.getFlu().getTxt() +"\r\n");
                    sb.append(weatherUtil.suggestion.getSport().getBrf()+"运动："+weatherUtil.suggestion.getSport().getTxt() +"\r\n");
                    sb.append(weatherUtil.suggestion.getUv().getBrf()+"光线："+weatherUtil.suggestion.getUv().getTxt() +"\r\n");

                }

                result = sb.toString();
            }
        }catch (IOException e){
            e.printStackTrace();
        }
        return result;
    }

    /**
     * @param httpUrl   :请求接口
     * @param httpArg  :参数
     * @return 返回结果
     */
    public static String doQuery(String httpUrl, String httpArg) throws IOException {
        BufferedReader reader = null;
        StringBuffer sbf = new StringBuffer();
        httpUrl = httpUrl + "?" + httpArg;

        URL url = new URL(httpUrl);
        HttpURLConnection connection = (HttpURLConnection) url.openConnection();
        connection.setRequestMethod("GET");
        // 填入apikey到HTTP header
        connection.setRequestProperty("apikey", "152803548f559e7b96201aed944590a4");
        connection.connect();
        InputStream is = connection.getInputStream();
        reader = new BufferedReader(new InputStreamReader(is, "UTF-8"));
        String strRead = null;
        while ((strRead = reader.readLine()) != null) {
            sbf.append(strRead);
            sbf.append("\r\n");
        }
        reader.close();
        return sbf.toString();
    }

    public AqiBean getAqi() {
        return aqi;
    }

    public void setAqi(AqiBean aqi) {
        this.aqi = aqi;
    }

    public BasicBean getBasic() {
        return basic;
    }

    public void setBasic(BasicBean basic) {
        this.basic = basic;
    }

    public NowBean getNow() {
        return now;
    }

    public void setNow(NowBean now) {
        this.now = now;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public SuggestionBean getSuggestion() {
        return suggestion;
    }

    public void setSuggestion(SuggestionBean suggestion) {
        this.suggestion = suggestion;
    }

    public List<DailyForecastBean> getDaily_forecast() {
        return daily_forecast;
    }

    public void setDaily_forecast(List<DailyForecastBean> daily_forecast) {
        this.daily_forecast = daily_forecast;
    }

    public List<HourlyForecastBean> getHourly_forecast() {
        return hourly_forecast;
    }

    public void setHourly_forecast(List<HourlyForecastBean> hourly_forecast) {
        this.hourly_forecast = hourly_forecast;
    }

    @ToString
    public static class AqiBean {
        /**
         * city : {"aqi":"65","co":"1","no2":"39","o3":"13","pm10":"58","pm25":"46","qlty":"良","so2":"7"}
         */

        private CityBean city;

        public CityBean getCity() {
            return city;
        }

        public void setCity(CityBean city) {
            this.city = city;
        }

        @ToString
        public static class CityBean {
            /**
             * aqi : 65
             * co : 1
             * no2 : 39
             * o3 : 13
             * pm10 : 58
             * pm25 : 46
             * qlty : 良
             * so2 : 7
             */

            private String aqi;
            private String co;
            private String no2;
            private String o3;
            private String pm10;
            private String pm25;
            private String qlty;
            private String so2;

            public String getAqi() {
                return aqi;
            }

            public void setAqi(String aqi) {
                this.aqi = aqi;
            }

            public String getCo() {
                return co;
            }

            public void setCo(String co) {
                this.co = co;
            }

            public String getNo2() {
                return no2;
            }

            public void setNo2(String no2) {
                this.no2 = no2;
            }

            public String getO3() {
                return o3;
            }

            public void setO3(String o3) {
                this.o3 = o3;
            }

            public String getPm10() {
                return pm10;
            }

            public void setPm10(String pm10) {
                this.pm10 = pm10;
            }

            public String getPm25() {
                return pm25;
            }

            public void setPm25(String pm25) {
                this.pm25 = pm25;
            }

            public String getQlty() {
                return qlty;
            }

            public void setQlty(String qlty) {
                this.qlty = qlty;
            }

            public String getSo2() {
                return so2;
            }

            public void setSo2(String so2) {
                this.so2 = so2;
            }
        }
    }

    @ToString
    public static class BasicBean {
        /**
         * city : 广州
         * cnty : 中国
         * id : CN101280101
         * lat : 23.108000
         * lon : 113.265000
         * update : {"loc":"2017-01-12 10:52","utc":"2017-01-12 02:52"}
         */

        private String city;
        private String cnty;
        private String id;
        private String lat;
        private String lon;
        private UpdateBean update;

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getCnty() {
            return cnty;
        }

        public void setCnty(String cnty) {
            this.cnty = cnty;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public UpdateBean getUpdate() {
            return update;
        }

        public void setUpdate(UpdateBean update) {
            this.update = update;
        }

        @ToString
        public static class UpdateBean {
            /**
             * loc : 2017-01-12 10:52
             * utc : 2017-01-12 02:52
             */

            private String loc;
            private String utc;

            public String getLoc() {
                return loc;
            }

            public void setLoc(String loc) {
                this.loc = loc;
            }

            public String getUtc() {
                return utc;
            }

            public void setUtc(String utc) {
                this.utc = utc;
            }
        }
    }

    @ToString
    public static class NowBean {
        /**
         * cond : {"code":"104","txt":"阴"}
         * fl : 12
         * hum : 79
         * pcpn : 0
         * pres : 1019
         * tmp : 14
         * vis : 7
         * wind : {"deg":"30","dir":"东北风","sc":"3-4","spd":"14"}
         */

        private CondBean cond;
        private String fl;
        private String hum;
        private String pcpn;
        private String pres;
        private String tmp;
        private String vis;
        private WindBean wind;

        public CondBean getCond() {
            return cond;
        }

        public void setCond(CondBean cond) {
            this.cond = cond;
        }

        public String getFl() {
            return fl;
        }

        public void setFl(String fl) {
            this.fl = fl;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public WindBean getWind() {
            return wind;
        }

        public void setWind(WindBean wind) {
            this.wind = wind;
        }

        @ToString
        public static class CondBean {
            /**
             * code : 104
             * txt : 阴
             */

            private String code;
            private String txt;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class WindBean {
            /**
             * deg : 30
             * dir : 东北风
             * sc : 3-4
             * spd : 14
             */

            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }

    @ToString
    public static class SuggestionBean {
        /**
         * air : {"brf":"中","txt":"气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。"}
         * comf : {"brf":"较舒适","txt":"白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。"}
         * cw : {"brf":"不宜","txt":"不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。"}
         * drsg : {"brf":"较冷","txt":"建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。"}
         * flu : {"brf":"极易发","txt":"将有一次强降温过程，天气寒冷，且空气湿度较大，极易发生感冒，请特别注意增加衣服保暖防寒。"}
         * sport : {"brf":"较不宜","txt":"有降水，且风力较强，推荐您在室内进行各种健身休闲运动；若坚持户外运动，请注意防风保暖。"}
         * trav : {"brf":"适宜","txt":"有降水，虽然风稍大，但温度适宜，适宜旅游，可不要错过机会呦！"}
         * uv : {"brf":"最弱","txt":"属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。"}
         */

        private AirBean air;
        private ComfBean comf;
        private CwBean cw;
        private DrsgBean drsg;
        private FluBean flu;
        private SportBean sport;
        private TravBean trav;
        private UvBean uv;

        public AirBean getAir() {
            return air;
        }

        public void setAir(AirBean air) {
            this.air = air;
        }

        public ComfBean getComf() {
            return comf;
        }

        public void setComf(ComfBean comf) {
            this.comf = comf;
        }

        public CwBean getCw() {
            return cw;
        }

        public void setCw(CwBean cw) {
            this.cw = cw;
        }

        public DrsgBean getDrsg() {
            return drsg;
        }

        public void setDrsg(DrsgBean drsg) {
            this.drsg = drsg;
        }

        public FluBean getFlu() {
            return flu;
        }

        public void setFlu(FluBean flu) {
            this.flu = flu;
        }

        public SportBean getSport() {
            return sport;
        }

        public void setSport(SportBean sport) {
            this.sport = sport;
        }

        public TravBean getTrav() {
            return trav;
        }

        public void setTrav(TravBean trav) {
            this.trav = trav;
        }

        public UvBean getUv() {
            return uv;
        }

        public void setUv(UvBean uv) {
            this.uv = uv;
        }

        @ToString
        public static class AirBean {
            /**
             * brf : 中
             * txt : 气象条件对空气污染物稀释、扩散和清除无明显影响，易感人群应适当减少室外活动时间。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class ComfBean {
            /**
             * brf : 较舒适
             * txt : 白天会有降雨，这种天气条件下，人们会感到有些凉意，但大部分人完全可以接受。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class CwBean {
            /**
             * brf : 不宜
             * txt : 不宜洗车，未来24小时内有雨，如果在此期间洗车，雨水和路上的泥水可能会再次弄脏您的爱车。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class DrsgBean {
            /**
             * brf : 较冷
             * txt : 建议着厚外套加毛衣等服装。年老体弱者宜着大衣、呢外套加羊毛衫。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class FluBean {
            /**
             * brf : 极易发
             * txt : 将有一次强降温过程，天气寒冷，且空气湿度较大，极易发生感冒，请特别注意增加衣服保暖防寒。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class SportBean {
            /**
             * brf : 较不宜
             * txt : 有降水，且风力较强，推荐您在室内进行各种健身休闲运动；若坚持户外运动，请注意防风保暖。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class TravBean {
            /**
             * brf : 适宜
             * txt : 有降水，虽然风稍大，但温度适宜，适宜旅游，可不要错过机会呦！
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }

        @ToString
        public static class UvBean {
            /**
             * brf : 最弱
             * txt : 属弱紫外线辐射天气，无需特别防护。若长期在户外，建议涂擦SPF在8-12之间的防晒护肤品。
             */

            private String brf;
            private String txt;

            public String getBrf() {
                return brf;
            }

            public void setBrf(String brf) {
                this.brf = brf;
            }

            public String getTxt() {
                return txt;
            }

            public void setTxt(String txt) {
                this.txt = txt;
            }
        }
    }

    @ToString
    public static class DailyForecastBean {
        /**
         * astro : {"sr":"07:10","ss":"18:00"}
         * cond : {"code_d":"305","code_n":"305","txt_d":"小雨","txt_n":"小雨"}
         * date : 2017-01-12
         * hum : 73
         * pcpn : 0.7
         * pop : 78
         * pres : 1018
         * tmp : {"max":"15","min":"11"}
         * uv : 1
         * vis : 10
         * wind : {"deg":"9","dir":"北风","sc":"3-4","spd":"16"}
         */

        private AstroBean astro;
        private CondBeanX cond;
        private String date;
        private String hum;
        private String pcpn;
        private String pop;
        private String pres;
        private TmpBean tmp;
        private String uv;
        private String vis;
        private WindBeanX wind;

        public AstroBean getAstro() {
            return astro;
        }

        public void setAstro(AstroBean astro) {
            this.astro = astro;
        }

        public CondBeanX getCond() {
            return cond;
        }

        public void setCond(CondBeanX cond) {
            this.cond = cond;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPcpn() {
            return pcpn;
        }

        public void setPcpn(String pcpn) {
            this.pcpn = pcpn;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public TmpBean getTmp() {
            return tmp;
        }

        public void setTmp(TmpBean tmp) {
            this.tmp = tmp;
        }

        public String getUv() {
            return uv;
        }

        public void setUv(String uv) {
            this.uv = uv;
        }

        public String getVis() {
            return vis;
        }

        public void setVis(String vis) {
            this.vis = vis;
        }

        public WindBeanX getWind() {
            return wind;
        }

        public void setWind(WindBeanX wind) {
            this.wind = wind;
        }

        @ToString
        public static class AstroBean {
            /**
             * sr : 07:10
             * ss : 18:00
             */

            private String sr;
            private String ss;

            public String getSr() {
                return sr;
            }

            public void setSr(String sr) {
                this.sr = sr;
            }

            public String getSs() {
                return ss;
            }

            public void setSs(String ss) {
                this.ss = ss;
            }
        }

        @ToString
        public static class CondBeanX {
            /**
             * code_d : 305
             * code_n : 305
             * txt_d : 小雨
             * txt_n : 小雨
             */

            private String code_d;
            private String code_n;
            private String txt_d;
            private String txt_n;

            public String getCode_d() {
                return code_d;
            }

            public void setCode_d(String code_d) {
                this.code_d = code_d;
            }

            public String getCode_n() {
                return code_n;
            }

            public void setCode_n(String code_n) {
                this.code_n = code_n;
            }

            public String getTxt_d() {
                return txt_d;
            }

            public void setTxt_d(String txt_d) {
                this.txt_d = txt_d;
            }

            public String getTxt_n() {
                return txt_n;
            }

            public void setTxt_n(String txt_n) {
                this.txt_n = txt_n;
            }
        }

        @ToString
        public static class TmpBean {
            /**
             * max : 15
             * min : 11
             */

            private String max;
            private String min;

            public String getMax() {
                return max;
            }

            public void setMax(String max) {
                this.max = max;
            }

            public String getMin() {
                return min;
            }

            public void setMin(String min) {
                this.min = min;
            }
        }

        @ToString
        public static class WindBeanX {
            /**
             * deg : 9
             * dir : 北风
             * sc : 3-4
             * spd : 16
             */

            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }

    @ToString
    public static class HourlyForecastBean {
        /**
         * date : 2017-01-12 13:00
         * hum : 77
         * pop : 58
         * pres : 1019
         * tmp : 18
         * wind : {"deg":"4","dir":"北风","sc":"3-4","spd":"17"}
         */

        private String date;
        private String hum;
        private String pop;
        private String pres;
        private String tmp;
        private WindBeanXX wind;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getHum() {
            return hum;
        }

        public void setHum(String hum) {
            this.hum = hum;
        }

        public String getPop() {
            return pop;
        }

        public void setPop(String pop) {
            this.pop = pop;
        }

        public String getPres() {
            return pres;
        }

        public void setPres(String pres) {
            this.pres = pres;
        }

        public String getTmp() {
            return tmp;
        }

        public void setTmp(String tmp) {
            this.tmp = tmp;
        }

        public WindBeanXX getWind() {
            return wind;
        }

        public void setWind(WindBeanXX wind) {
            this.wind = wind;
        }

        @ToString
        public static class WindBeanXX {
            /**
             * deg : 4
             * dir : 北风
             * sc : 3-4
             * spd : 17
             */

            private String deg;
            private String dir;
            private String sc;
            private String spd;

            public String getDeg() {
                return deg;
            }

            public void setDeg(String deg) {
                this.deg = deg;
            }

            public String getDir() {
                return dir;
            }

            public void setDir(String dir) {
                this.dir = dir;
            }

            public String getSc() {
                return sc;
            }

            public void setSc(String sc) {
                this.sc = sc;
            }

            public String getSpd() {
                return spd;
            }

            public void setSpd(String spd) {
                this.spd = spd;
            }
        }
    }
}
