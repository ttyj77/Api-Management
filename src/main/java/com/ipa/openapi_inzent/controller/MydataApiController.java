package com.ipa.openapi_inzent.controller;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.model.DataDTO;
import org.springframework.web.bind.annotation.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

@RestController
public class MydataApiController {

    @GetMapping("/invest/accounts")
    @ResponseBody
    public StringBuffer invest_001(@RequestParam String org_code, @RequestParam String limit, @RequestHeader String Authorization) {
        System.out.println("Authorization = " + Authorization);
        String token = Authorization;
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

            br.close();
            JsonParser jsonParser = new JsonParser();
            JsonObject jsonObject = (JsonObject) jsonParser.parse(response.toString());

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

        return response;
    }

    private static HttpURLConnection getHttpURLConnection(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return con;
    }

}

