package moe.nyako.NIMS;

import java.io.BufferedReader;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

import org.json.JSONArray;
import org.json.JSONObject;

class APICall {
    //服务器地址
    private String server = "http://ifnya.com:2333";

    //验证用户名与密码
    boolean auth(String userId, String userPwd) {
        try {
            String url = "http://mids.nju.edu.cn/_ids_mobile/webLogin20_2?loginLT=0000000-0000-0000-0000-000000000000&username=" +
                    URLEncoder.encode(userId, "UTF-8") + "&password=" + URLEncoder.encode(userPwd, "UTF-8");

            URL obj = new URL(url);
            HttpURLConnection con = (HttpURLConnection) obj.openConnection();
            con.setRequestMethod("GET");

            return con.getResponseCode() == 200;
        } catch (Exception ignore) {
            return false;
        }
    }

    //获取用户 Tags
    JSONArray getTags(String userId) {
        try {
            String tagsJson = sendGet("getTags", "id=" + userId);
            JSONObject tags = new JSONObject(tagsJson);
            return tags.getJSONArray("tags");
        } catch (Exception ignore) {
            return null;
        }
    }

    //批量修改 Tag
    boolean editTags(JSONObject data){
        try {
            String editResultJson = sendEditTagsPost("editTags", data.toString());
            JSONObject editResult = new JSONObject(editResultJson);
            int res = editResult.getInt("status");
            return res == 0;
        } catch (Exception ignore) {
            return false;
        }
    }

    //改变某门课的收藏状态

    int favor(String id, String oname){
        try {
            JSONObject data = new JSONObject();
            data.put("id", id);
            data.put("oname", oname);
            String editResultJson = sendEditTagsPost("favor", data.toString());
            JSONObject editResult = new JSONObject(editResultJson);
            int res = editResult.getInt("status");
            return res;
        } catch (Exception ignore) {
            return -1;
        }
    }

    //GET 方式调用 API
    private String sendGet(String api, String urlParameters) throws Exception {
        String url = server + "/" + api + "?" + urlParameters;

        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return response.toString();
    }

    //POST 方式调用编辑 Tag API
    private String sendEditTagsPost(String api, String urlParameters) throws Exception {
        String url = server + "/" + api;
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("POST");
        con.setDoOutput(true);

        DataOutputStream wr = new DataOutputStream(con.getOutputStream());

        //注意编码问题
        wr.writeBytes(URLEncoder.encode(urlParameters, "UTF-8"));
        wr.flush();
        wr.close();

        BufferedReader in = new BufferedReader(new InputStreamReader(con.getInputStream()));
        String inputLine;
        StringBuilder response = new StringBuilder();

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);
        in.close();

        return response.toString();
    }
}