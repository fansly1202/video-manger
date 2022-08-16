package com.fly.www.video.utils;

import cn.hutool.core.map.MapUtil;
import cn.hutool.http.HttpResponse;
import cn.hutool.http.HttpUtil;
import cn.hutool.json.JSONArray;
import cn.hutool.json.JSONObject;
import cn.hutool.json.JSONUtil;
import com.fly.www.video.pojo.VideoInfo;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Connection;
import org.jsoup.Jsoup;


import java.io.*;
import java.net.URL;
import java.net.URLConnection;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class VideoUtils {

    /**
     * 过滤链接，获取http连接地址
     *
     * @param url
     * @return
     */
    public static String decodeHttpUrl(String url) {
        String s = pickURI(url);
        int start = url.indexOf("http");
        int end = url.lastIndexOf("/");
        String decodeurl = s;
        if (url.endsWith("/")) {
            decodeurl = url.substring(start, end);
        }
        return decodeurl;
    }
    /**
     * 下载
     *
     * @param videoAddress
     * @param desc
     */
    public static void downloadVideo(String videoAddress, String author, String desc, String type,String savePath) {
        int byteRead;
        try {
            if (videoAddress == null) {
                return;
            }
            URL url = new URL(videoAddress);
            //获取链接
            URLConnection conn = url.openConnection();
            // 设置通用的请求属性
            conn.setRequestProperty("accept", "*/*");
            conn.setRequestProperty("connection", "Keep-Alive");
            conn.setRequestProperty("Accept-Charset", "utf-8");
            conn.addRequestProperty("User-Agent","Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
            conn.setConnectTimeout(50000);
            conn.setReadTimeout(50000);
            conn.connect();

            //输入流
            InputStream inStream = conn.getInputStream();
            //封装一个保存文件的路径对象
            desc = desc.replaceAll(":", "").replaceAll("\\?", "")
                    .replaceAll("\"", "").replaceAll("\\|", "")
                    .replaceAll("/", "").replaceAll("\\\\", "")
                    .replaceAll("\\*", "").replaceAll("\\<", "")
                    .replaceAll("\\>", "");
            if(!savePath.endsWith("/")) {//保存目录设置为以“/”结尾
                savePath = savePath+"/";
            }
            File fileSavePath = new File(savePath +  author + "/" + desc + type);
            //注:如果保存文件夹不存在,那么则创建该文件夹
            File fileParent = fileSavePath.getParentFile();
            if (!fileParent.exists()) {
                fileParent.mkdirs();
            }
            if (fileSavePath.exists()) { //如果文件存在，则删除原来的文件
                fileSavePath.delete();
            }
            //写入文件
            FileOutputStream fs = new FileOutputStream(fileSavePath);
            BufferedInputStream bin = new BufferedInputStream(inStream);
            BufferedOutputStream bout = new BufferedOutputStream(fs);

            byte[] buffer = new byte[1024 * 3];
            while ((byteRead = bin.read(buffer)) != -1) {
                bout.write(buffer, 0, byteRead);
            }
            bout.close();
            bin.close();
            inStream.close();
            fs.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 根据分享路径获取抖音视频
     *
     * @throws Exception
     */
    public static  List<VideoInfo>  getDyVideoAndAudio(String url) throws Exception {
        final String videoPath = "https://www.iesdouyin.com/web/api/v2/aweme/iteminfo/?item_ids=";
        url = decodeHttpUrl(url); //过滤链接，获取http连接地址
        Connection con = Jsoup.connect(url);
        con.header("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        Connection.Response resp = con.method(Connection.Method.GET).ignoreHttpErrors(true).execute();
        String strUrl = resp.url().toString();
        String itemId = strUrl.substring(strUrl.indexOf("video/"), strUrl.lastIndexOf("/")).replace("video/", "");
        String videoUrl = videoPath + itemId;
        String jsonStr = Jsoup.connect(videoUrl).ignoreContentType(true).execute().body();
        JSONObject json = new JSONObject(jsonStr);
        String videoAddress = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").get(0).toString();
        String width = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getStr("width");
        String height = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getStr("height");
        String ratio = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getStr("ratio");
        String author = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("author").getStr("nickname");
        String desc = json.getJSONArray("item_list").getJSONObject(0).getStr("desc");
        String originCover = json.getJSONArray("item_list").getJSONObject(0).getJSONObject("video").getJSONObject("origin_cover").getJSONArray("url_list").get(0).toString();

        HashMap headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (iPhone; CPU iPhone OS 11_0 like Mac OS X) AppleWebKit/604.1.38 (KHTML, like Gecko) Version/11.0 Mobile/15A372 Safari/604.1");
        String replace = videoAddress.replace("playwm", "play"); //替换成无水印链接
        HttpResponse execute = HttpUtil.createGet(replace).addHeaders(headers).execute();
        String finalVideoAddress = execute.header("Location");
        //下载到指定文件路径
        VideoInfo videoInfo = new VideoInfo();
        videoInfo.setDownloadUrl(finalVideoAddress);
        videoInfo.setOriginCover(originCover);
        videoInfo.setAuthor(author);
        videoInfo.setDesc(desc);
        videoInfo.setWidth(width);
        videoInfo.setHeight(height);
        videoInfo.setRatio(ratio);
        List<VideoInfo> list =  new ArrayList<VideoInfo>();
        list.add(videoInfo);
        return list;

    }
    public static  List<VideoInfo>  getKsVideoAndAudio(String url) throws Exception {
        Map<String, String> headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");

        Map<String,String> coookies = new HashMap<String,String>();
        coookies.put("did","");//里面的值可以去网页上找到，去赋值
        coookies.put("kpf","PC_WEB");
        coookies.put("kpn","KUAISHOU_VISION");
        coookies.put("clientid","3");
        coookies.put("userId","");//里面的值可以去网页上找到，去赋值
        coookies.put("kuaishou.server.web_ph","");//里面的值可以去网页上找到，去赋值
        coookies.put("kuaishou.server.web_st","");//里面的值可以去网页上找到，去赋值
        url = decodeHttpUrl(url); //过滤链接，获取http连接地址
        Connection con = Jsoup.connect(url);
        Connection.Response resp = con.method(Connection.Method.GET).ignoreContentType(true).ignoreHttpErrors(true).execute();
        String strUrl = resp.url().toString();
        JSONObject json11 = new JSONObject(resp.body());
        System.out.println(json11.get("data"));
        Map<String,String> data = new HashMap<String,String>();

        data.put("operationName","visionVideoDetail");
        Map<String,String> variables = new HashMap<String,String>();
        variables.put("page","selected");
        variables.put("photoId",strUrl.substring(strUrl.indexOf("short-video/"),strUrl.indexOf("?")).replace("short-video/",""));
        String s = JSONUtil.toJsonStr(variables);
        System.out.println(variables);
        data.put("variables", s);
        String query = "query visionVideoDetail($photoId: String, $type: String, $page: String) {   visionVideoDetail(photoId: $photoId, type: $type, page: $page) {     status     type     author {       id       name       following       headerUrl       __typename     }     photo {       id       duration       caption       likeCount       realLikeCount       coverUrl       photoUrl       liked       timestamp       expTag       llsid       __typename     }     tags {       type       name       __typename     }     commentLimit {       canAddComment       __typename     }     llsid     __typename   } } ";
        data.put("query",query);

        String videoPath = "https://www.kuaishou.com/graphql";
        headers.put("content-Type","application/json");
        String jsonStr = Jsoup.connect(videoPath).data(data).headers(headers).cookies(coookies).ignoreContentType(true).ignoreHttpErrors(true).execute().body();
        JSONObject json = new JSONObject(jsonStr);
        JSONObject dataJSONObject =   JSONUtil.parseObj( json.get("data"));
        JSONObject visionVideoDetail = JSONUtil.parseObj( dataJSONObject.get("visionVideoDetail"));
        VideoInfo videoInfo = new VideoInfo();
        String regEx="[\\s~·`!！@#￥$%^……&*(())\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《、/？?]";
        String videoAddress = visionVideoDetail.getJSONObject("photo").getStr("photoUrl");
        String ratio = visionVideoDetail.getJSONObject("photo").getStr("videoRatio");
        String author = visionVideoDetail.getJSONObject("author").getStr("name");
        String origin_cover = visionVideoDetail.getJSONObject("photo").getStr("coverUrl");
        String desc = visionVideoDetail.getJSONObject("photo").getStr("caption") ;
        //下载到指定文件路径
        videoInfo.setDownloadUrl(videoAddress);
        videoInfo.setOriginCover(origin_cover);
        videoInfo.setAuthor(Pattern.compile(regEx).matcher(author).replaceAll(""));
        videoInfo.setDesc(Pattern.compile(regEx).matcher(desc).replaceAll(""));
        videoInfo.setRatio(ratio);
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
        videoInfoList.add(videoInfo);
        return videoInfoList;

    }

    public static List<VideoInfo>  getDyVideoAndAudioById(String url) throws Exception {
        String videoPath = "https://www.iesdouyin.com/web/api/v2/aweme/post/?sec_uid=";
        url = decodeHttpUrl(url); //过滤链接，获取http连接地址
        String sec_uid ="";
        if(url.contains("?")){
             sec_uid = url.substring(url.indexOf("user/"),url.indexOf("?")).replace("user/", "");
        }else{
            sec_uid =  url.substring(url.indexOf("user/")).replace("user/", "");
        }
        String videoUrl = videoPath + sec_uid + "&count=20&max_cursor=" + 0 + "&aid=1128&_signature=R6Ub1QAAJ-gQklOOeJfpTEelG8&dytk=";
        String jsonStr = Jsoup.connect(videoUrl).ignoreHttpErrors(true).ignoreContentType(true).execute().body();
        JSONObject json = new JSONObject(jsonStr);
        Boolean has_more = (Boolean) json.get("has_more");
        Object max_cursor = json.get("max_cursor");
        List<JSONArray> list = new ArrayList<JSONArray>();
        JSONArray vlist = json.getJSONArray("aweme_list");
        list.add(vlist);
        while (has_more) {
            videoUrl = videoPath + sec_uid + "&count=20&max_cursor=" + max_cursor + "&aid=1128&_signature=R6Ub1QAAJ-gQklOOeJfpTEelG8&dytk=";
            jsonStr = Jsoup.connect(videoUrl).ignoreHttpErrors(true).ignoreContentType(true).execute().body();
            json = new JSONObject(jsonStr);
            vlist = json.getJSONArray("aweme_list");
            list.add(vlist);
            has_more = (Boolean) json.get("has_more");
            max_cursor = json.get("max_cursor");
        }
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
        if (list.size() > 0) {
            for (JSONArray jsonArray : list) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    VideoInfo videoInfo = new VideoInfo();
                    String videoAddress = jsonArray.getJSONObject(i).getJSONObject("video").getJSONObject("play_addr").getJSONArray("url_list").get(0).toString();
                    String width = jsonArray.getJSONObject(i).getJSONObject("video").getStr("width");
                    String height = jsonArray.getJSONObject(i).getJSONObject("video").getStr("height");
                    String ratio = jsonArray.getJSONObject(i).getJSONObject("video").getStr("ratio");
                    String author = jsonArray.getJSONObject(i).getJSONObject("author").getStr("nickname");
                    String origin_cover = jsonArray.getJSONObject(i).getJSONObject("video").getJSONObject("origin_cover").getJSONArray("url_list").get(0).toString();

                    String desc = jsonArray.getJSONObject(i).getStr("desc") + "_________" + i;
                    String replace = videoAddress.replace("playwm", "play"); //替换成无水印链接
                    //下载到指定文件路径
                    videoInfo.setDownloadUrl(replace);
                    videoInfo.setOriginCover(origin_cover);
                    videoInfo.setAuthor(author);
                    videoInfo.setDesc(desc);
                    videoInfo.setWidth(width);
                    videoInfo.setHeight(height);
                    videoInfo.setRatio(ratio);
                    videoInfoList.add(videoInfo);
                }
            }
        }
        return videoInfoList;
    }

    public static List<VideoInfo>  getKsVideoAndAudioById(String url) throws Exception {
        Map<String, String> headers = MapUtil.newHashMap();
        headers.put("User-Agent", "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/102.0.0.0 Safari/537.36");
        headers.put("content-Type","application/json");

        Map<String,String> coookies = new HashMap<String,String>();
        coookies.put("did",""); //里面的值可以去网页上找到，去赋值
        coookies.put("kpf","PC_WEB");
        coookies.put("kpn","KUAISHOU_VISION");
        coookies.put("clientid","3");
        coookies.put("userId",""); //里面的值可以去网页上找到，去赋值
        coookies.put("kuaishou.server.web_ph","");//里面的值可以去网页上找到，去赋值
        coookies.put("kuaishou.server.web_st","");//里面的值可以去网页上找到，去赋值
        url = decodeHttpUrl(url); //过滤链接，获取http连接地址
        String userId = url.substring(url.indexOf("profile/")).replace("profile/", "");

        Map<String,String> data = new HashMap<String,String>();
        data.put("operationName","visionProfilePhotoList");
        Map<String,String> variables = new HashMap<String,String>();
        variables.put("userId",userId);
        variables.put("pcursor","");
        variables.put("page","profile");
        String s = JSONUtil.toJsonStr(variables);
        data.put("variables", s);
        String query ="fragment photoContent on PhotoEntity {   id   duration   caption   likeCount   viewCount   realLikeCount   coverUrl   photoUrl   photoH265Url   manifest   manifestH265   videoResource   coverUrls {     url     __typename   }   timestamp   expTag   animatedCoverUrl   distance   videoRatio   liked   stereoType   profileUserTopPhoto   __typename }  fragment feedContent on Feed {   type   author {     id     name     headerUrl     following     headerUrls {       url       __typename     }     __typename   }   photo {     ...photoContent     __typename   }   canAddComment   llsid   status   currentPcursor   __typename }  query visionProfilePhotoList($pcursor: String, $userId: String, $page: String, $webPageArea: String) {   visionProfilePhotoList(pcursor: $pcursor, userId: $userId, page: $page, webPageArea: $webPageArea) {     result     llsid     webPageArea     feeds {       ...feedContent       __typename     }     hostName     pcursor     __typename   } } ";
        data.put("query",query);
        String videoPath = "https://www.kuaishou.com/graphql";
        String jsonStr = Jsoup.connect(videoPath).data(data).headers(headers).cookies(coookies).ignoreContentType(true).ignoreHttpErrors(true).execute().body();
        JSONObject json = new JSONObject(jsonStr);
        JSONObject dataJSONObject =   JSONUtil.parseObj( json.get("data"));
        JSONObject visionProfilePhotoList = JSONUtil.parseObj( dataJSONObject.get("visionProfilePhotoList"));
        String pcursor = (String) visionProfilePhotoList.get("pcursor");
        Boolean has_more = false;
        if(pcursor!=null&&!StringUtils.equals("no_more",pcursor)){
            has_more = true;
        }
        List<JSONArray> list = new ArrayList<JSONArray>();
        JSONArray vlist = visionProfilePhotoList.getJSONArray("feeds");
        if(vlist!=null){
            list.add(vlist);
        }
        while (has_more) {
            variables.put("pcursor",pcursor);
            s = JSONUtil.toJsonStr(variables);
            data.put("variables", s);
            jsonStr = Jsoup.connect(videoPath).data(data).headers(headers).cookies(coookies).ignoreContentType(true).ignoreHttpErrors(true).execute().body();
            json = new JSONObject(jsonStr);
            dataJSONObject =   JSONUtil.parseObj( json.get("data"));
            visionProfilePhotoList = JSONUtil.parseObj( dataJSONObject.get("visionProfilePhotoList"));
            pcursor = (String) visionProfilePhotoList.get("pcursor");
            vlist = visionProfilePhotoList.getJSONArray("feeds");
            if(vlist!=null){
                list.add(vlist);
            }
            variables.put("pcursor",pcursor);
            if(pcursor!=null&&!StringUtils.equals("no_more",pcursor)){
                has_more = true;
            }else{
                has_more = false;
            }
        }
        List<VideoInfo> videoInfoList = new ArrayList<VideoInfo>();
        String regEx="[\\s~·`!！@#￥$%^……&*(())\\-——\\-_=+【\\[\\]】｛{}｝\\|、\\\\；;：:‘'“”\"，,《、/？?]";
        if (list.size() > 0) {
            for (JSONArray jsonArray : list) {
                for (int i = 0; i < jsonArray.size(); i++) {
                    VideoInfo videoInfo = new VideoInfo();
                    String videoAddress = jsonArray.getJSONObject(i).getJSONObject("photo").getStr("photoUrl");
                    String ratio = jsonArray.getJSONObject(i).getJSONObject("photo").getStr("videoRatio");
                    String author = jsonArray.getJSONObject(i).getJSONObject("author").getStr("name");
                    String origin_cover = jsonArray.getJSONObject(i).getJSONObject("photo").getStr("coverUrl");
                    String desc = jsonArray.getJSONObject(i).getJSONObject("photo").getStr("caption") + "_________" + i;
                    //下载到指定文件路径
                    videoInfo.setDownloadUrl(videoAddress);
                    videoInfo.setOriginCover(origin_cover);
                    videoInfo.setAuthor(Pattern.compile(regEx).matcher(author).replaceAll(""));
                    videoInfo.setDesc(Pattern.compile(regEx).matcher(desc).replaceAll(""));
                    videoInfo.setRatio(ratio);
                    videoInfoList.add(videoInfo);
                }
            }
        }
        return videoInfoList;
    }

    /**
     * 正则表达式提取 url
     *
     * @param text
     * @return
     */
    public static String pickURI(String text) {
        Pattern pattern = Pattern.compile("https?://[-A-Za-z0-9+&@#/%?=~_|!:,.;]+[-A-Za-z0-9+&@#/%=~_|]");
        Matcher matcher = pattern.matcher(text);
        if (matcher.find()) {
            return matcher.group();
        }
        return "";
    }

}
