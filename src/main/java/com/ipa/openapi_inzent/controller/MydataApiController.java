package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.model.DataDTO;
import com.ipa.openapi_inzent.service.MydataService;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
@RequestMapping("/mydata")
public class MydataApiController {

    @GetMapping("/invest/accounts")
    @ResponseBody
    public StringBuffer invest_001(@RequestParam String org_code, @RequestParam String limit, HttpServletRequest header) {

        long start = System.currentTimeMillis();
        System.out.println("start = " + start);


        System.out.println(header.getHeader("Authorization"));
        System.out.println("org_code = " + org_code);
        String token = header.getHeader("Authorization");
        System.out.println("token = " + token);
        System.out.println("limit = " + limit);

        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts"); //URL enc: "UTF-8") + "=" + org_code); // org_code enc: "UTF-8") + "=" + limit); // limit
            urlBuilder.append("?" + URLEncoder.encode("org_code", "UTF-8") + "=" + org_code);
            urlBuilder.append("&" + URLEncoder.encode("limit", "UTF-8") + "=" + limit);

            HttpURLConnection con = getHttpURLConnection(urlBuilder);

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("x-api-tran-id", x_api_tran_id);
            con.setRequestProperty("x-api-type", x_api_type);
            con.setRequestProperty("X-FSI-SVC-DATA-KEY", "Y");
            con.setRequestProperty("accept", "application/json; charset=UTF-8");
            int responseCode = con.getResponseCode();

            System.out.println("responseCode = " + responseCode);

            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { //
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();

            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }

            br.close();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
            System.out.println("jsonObject = " + jsonObject);
            System.out.println(jsonObject.get("account_list").toString());

            DataDTO dataDTO = new DataDTO();
            dataDTO.setResponse(jsonObject.get("account_list").toString());
            dataDTO.setX_api_type(x_api_type);
            dataDTO.setX_api_tran_id(x_api_tran_id);
            dataDTO.setOrg_code(org_code);

//          DB insert
//            dataService.insert(dataDTO);

        } catch (Exception e) {
            System.out.println(e);
        }

        // 특정 코드 실행 되고 난 후 시간
        long end = System.currentTimeMillis();
        System.out.println("end = " + end);

        // 초 단위 실행시간
        double result = (end - start) / 1000.0;
        System.out.println("result = " + result);

        return response;
    }

    private static HttpURLConnection getHttpURLConnection(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return con;
    }

    // 금투-002
    @PostMapping("/invest/accounts/basic")
    @ResponseBody
    public StringBuffer getData2(@RequestBody String body, HttpServletRequest header) {
        String token = header.getHeader("Authorization");
        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts/basic");
            HttpURLConnection con = getHttpURLConnection(urlBuilder);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("x-api-tran-id", x_api_tran_id);
            con.setRequestProperty("x-api-type", x_api_type);
            con.setRequestProperty("User-Agent", "PostmanRuntime/7.32.2");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("accept", "application/json; charset=UTF-8");
            con.setRequestProperty("X-FSI-SVC-DATA-KEY", "Y");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(body.getBytes("utf-8")); // request body
            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 에러 발생
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }


            br.close();
            String result = response.toString();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(result); // response
            JsonObject org_code = (JsonObject) parser.parse(body); // org_code
            DataDTO dataDTO = new DataDTO();
            dataDTO.setResponse(jsonObject.get("basic_list").toString());
            dataDTO.setOrg_code(org_code.get("org_code").toString());
            dataDTO.setX_api_type(x_api_type);
            dataDTO.setX_api_tran_id(x_api_tran_id);

//          DB insert
//          dataService.insert(dataDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }


    //   금투 - 003
    @PostMapping("/invest/accounts/transactions")
    @ResponseBody
    public StringBuffer transactionsAPi(@RequestBody String body, HttpServletRequest header) {
        String token = header.getHeader("Authorization");
        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts/transactions"); //URL

            HttpURLConnection con = getHttpURLConnection(urlBuilder);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", token);
            con.setRequestProperty("x-api-tran-id", x_api_tran_id);
            con.setRequestProperty("x-api-type", x_api_type);
            con.setRequestProperty("User-Agent", "PostmanRuntime/7.32.2");
            con.setRequestProperty("Content-Type", "application/json");
            con.setRequestProperty("accept", "application/json; charset=UTF-8");
            con.setRequestProperty("X-FSI-SVC-DATA-KEY", "Y");
            con.setDoOutput(true);

            OutputStream os = con.getOutputStream();
            os.write(body.getBytes("utf-8")); //request body

            os.flush();
            os.close();

            int responseCode = con.getResponseCode();
            BufferedReader br;
            if (responseCode == 200) { // H 정상 호출
                br = new BufferedReader(new InputStreamReader(con.getInputStream()));
            } else { // 2
                br = new BufferedReader(new InputStreamReader(con.getErrorStream()));
            }

            String inputLine;
            response = new StringBuffer();
            while ((inputLine = br.readLine()) != null) {
                response.append(inputLine);
            }
            br.close();

            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString()); //response
            JsonObject org_code = (JsonObject) jsonParser.parse(body); // org_code
            DataDTO dataDTO = new DataDTO();
            dataDTO.setResponse(jsonObject.get("trans_list").toString());
            dataDTO.setX_api_tran_id(x_api_tran_id);
            dataDTO.setX_api_type(x_api_type);
            dataDTO.setOrg_code(org_code.get("org_code").toString());

//            DB insert
//            dataService.insert(dataDTO);
        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

}


