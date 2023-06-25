package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.config.auth.UserCustomDetails;
import com.ipa.openapi_inzent.model.DataDTO;
import com.ipa.openapi_inzent.model.MdProviderDTO;
import com.ipa.openapi_inzent.model.MdReqInfoDTO;
import com.ipa.openapi_inzent.service.MydataApiService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.UUID;

@RestController
@RequestMapping("/mydata/api")
public class MydataApiController {

    private MydataApiService mydataApiService;

    @Autowired
    public MydataApiController(MydataApiService mydataApiService) {
        this.mydataApiService = mydataApiService;
    }

    @GetMapping("/invest/accounts")
    @ResponseBody
    public StringBuffer invest_001(@RequestParam String org_code, @RequestParam String limit, HttpServletRequest header) {

        long start = System.currentTimeMillis();
        Date date = new Date();
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm");


        String token = header.getHeader("Authorization");

        header.getHeaderNames().asIterator().forEachRemaining(
                headerName -> System.out.println(headerName + ": " + header.getHeader(headerName))

        );


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
            // 특정 코드 실행 되고 난 후 시간
            long end = System.currentTimeMillis();

            // 초 단위 실행시간
            double result = (end - start) / 1000.0;

            br.close();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());
            String apiCode = "금투-001";
            String apiResouceUri = "/accounts";
            /* data insert */
            mydataApiService.invest001Insert(org_code, responseCode, result, jsonObject, apiCode, apiResouceUri);

        } catch (Exception e) {
            System.out.println(e);
        }


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
        long start = System.currentTimeMillis();
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

            // 특정 코드 실행 되고 난 후 시간
            long end = System.currentTimeMillis();

            // 초 단위 실행시간
            double timeResult = (end - start) / 1000.0;

            br.close();

            String result = response.toString();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(result); // response

            JsonObject org_code = (JsonObject) parser.parse(body); // org_code


            String apiCode = "금투-002";
            String apiResouceUri = "/accounts/basic";
            /* data insert */
            mydataApiService.invest001Insert(org_code.toString(), responseCode, timeResult, jsonObject, apiCode, apiResouceUri);

//          DB insert
//          dataService.insert(dataDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return response;
    }

    //
//
//   금투 - 003
    @PostMapping("/invest/accounts/transactions")
    @ResponseBody
    public StringBuffer transactionsAPi(@RequestBody String body, HttpServletRequest header) {
        long start = System.currentTimeMillis();
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

            // 특정 코드 실행 되고 난 후 시간
            long end = System.currentTimeMillis();

            // 초 단위 실행시간
            double timeResult = (end - start) / 1000.0;

            br.close();

            String result = response.toString();
            JsonParser parser = new JsonParser();
            JsonObject jsonObject = (JsonObject) parser.parse(result); // response

            JsonObject org_code = (JsonObject) parser.parse(body); // org_code

            String apiCode = "금투-003";
            String apiResouceUri = "/accounts/transactions";
            /*Db insert */
            mydataApiService.invest001Insert(org_code.get("org_code").toString(), responseCode, timeResult, jsonObject, apiCode, apiResouceUri);

        } catch (Exception e) {
            System.out.println(e);
        }
        return response;
    }

}


