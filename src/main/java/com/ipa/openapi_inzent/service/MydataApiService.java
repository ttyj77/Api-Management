package com.ipa.openapi_inzent.service;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.ipa.openapi_inzent.dao.MydataApiDAO;
import com.ipa.openapi_inzent.model.GetDataDTO;
import com.ipa.openapi_inzent.model.MdProviderDTO;
import com.ipa.openapi_inzent.model.MdReqInfoDTO;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.util.List;
import java.util.UUID;

@Service
public class MydataApiService {

    private MydataApiDAO mydataApiDAO;
    private MydataService mydataService;

    public MydataApiService(MydataApiDAO mydataApiDAO, MydataService mydataService) {
        this.mydataApiDAO = mydataApiDAO;
        this.mydataService = mydataService;
    }

    public void reqHistoryInsert(MdProviderDTO mdProviderDTO) {
        mydataApiDAO.providerHistoryInsert(mdProviderDTO);
    }

    public int reqInfoInsert(MdReqInfoDTO mdReqInfoDTO) {
        mydataApiDAO.reqInfoInsert(mdReqInfoDTO);
        return mdReqInfoDTO.getId();
    }

    private static HttpURLConnection getHttpURLConnection(StringBuilder urlBuilder) throws IOException {
        URL url = new URL(urlBuilder.toString());
        HttpURLConnection con = (HttpURLConnection) url.openConnection();
        return con;
    }

    /*금투-001*/
    public JsonObject invest001(String org_code, String limit, String token) {
        JsonObject jsonObject = new JsonObject();
        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts"); //URL enc: "UTF-8") + "=" + org_code); // org_code enc: "UTF-8") + "=" + limit); // limit
            urlBuilder.append("?" + URLEncoder.encode("org_code", "UTF-8") + "=" + org_code);
            urlBuilder.append("&" + URLEncoder.encode("limit", "UTF-8") + "=" + limit);

            HttpURLConnection con = getHttpURLConnection(urlBuilder);

            con.setRequestMethod("GET");
            con.setRequestProperty("Authorization", "Bearer" + token);
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

            JsonParser jsonParser = new JsonParser();
            jsonObject = (JsonObject) jsonParser.parse(response.toString());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    /*금투-002*/
    public JsonObject invest002(String body, String token) {
        System.out.println(body);
        System.out.println("token = " + token);

        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        JsonObject jsonObject = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts/basic");
            HttpURLConnection con = getHttpURLConnection(urlBuilder);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + token);
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
            jsonObject = (JsonObject) parser.parse(result);


//          DB insert
//          dataService.insert(dataDTO);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }


    /* 금투-003 */
    public JsonObject invest003(String body, String token) {
        System.out.println("body = " + body);
        System.out.println("token = " + token);

        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        StringBuffer response = null;
        JsonObject jsonObject = null;
        try {
            StringBuilder urlBuilder = new StringBuilder("https://developers.mydatakorea.org:9443/v1/invest/accounts/transactions"); //URL

            HttpURLConnection con = getHttpURLConnection(urlBuilder);
            con.setRequestMethod("POST");
            con.setRequestProperty("Authorization", "Bearer" + token);
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

            String result = response.toString();
            JsonParser parser = new JsonParser();
            jsonObject = (JsonObject) parser.parse(result); // response



        } catch (Exception e) {
            e.printStackTrace();
        }
        return jsonObject;
    }

    public void insertResult(GetDataDTO getDataDTO) {
        mydataApiDAO.dataInsert(getDataDTO);
    }

    public void invest001Insert(String orgCode,
                                int responseCode, double result, JsonObject jsonObject, String apiCode, String apiResouceUri) {
        System.out.println("resu = " + result);
        System.out.println("runtime = " + (result));

        String x_api_tran_id = "1168119031SAA202303171424";
        String x_api_type = "user-search";
        UUID uuid = UUID.randomUUID();

        List<MdReqInfoDTO> reqInfoList = mydataService.mdReqAll();
        String cliNum = "9704261153";

        int reqId = mydataService.selectReqInfoId(orgCode, cliNum);


        if (mydataService.selectReqInfoId(orgCode, cliNum) > 0) {
            MdProviderDTO mdProviderDTO = new MdProviderDTO();

            mdProviderDTO.setX_api_type(x_api_type);
            mdProviderDTO.setX_api_tran_id(x_api_tran_id);
            mdProviderDTO.setOrg_code(orgCode);
            mdProviderDTO.setApiResources(apiResouceUri);
            mdProviderDTO.setResCode(String.valueOf(responseCode));
            mdProviderDTO.setUniqueNum(String.valueOf(uuid));
            mdProviderDTO.setCustomerNum("9704261153");
            mdProviderDTO.setReqInfoId(reqId);
            mdProviderDTO.setRuntime(result);
            mdProviderDTO.setReqHeader("{\n" +
                    "  \"accept\": \"application/json; charset=UTF-8\",\n" +
                    "  \"x-api-type\": \"user-search\",\n" +
                    "  \"X-FSI-MEM-NO\": \"FSI00002899\",\n" +
                    "  \"x-api-tran-id\": \"1168119031SAA202303171424\",\n" +
                    "  \"X-FSI-UTCT-TYPE\": \"TGC00001\",\n" +
                    "  \"X-FSI-BUS-SEQ-NO\": \"105\",\n" +
                    "  \"X-FSI-SVC-DATA-KEY\": \"Y\"\n" +
                    "}");

//          DB insert
            mdProviderDTO.setResData(jsonObject.toString());
            mdProviderDTO.setApiCode(apiCode);
            if (responseCode == 200) {
                mdProviderDTO.setResMsg("성공");
            } else {
                mdProviderDTO.setResMsg("실패");
            }
            mdProviderDTO.setStatusInfo("(통합인증)자산선택");
            mdProviderDTO.setResData(jsonObject.toString());
            mdProviderDTO.setApiCode(apiCode);

            reqHistoryInsert(mdProviderDTO);
        } else {
            System.out.println("조건문 clientNum 안같음 else문");

            // 같은 기관 코드와 같은 정보주체 번호를 가진 전송요구테이블이 없을 때에는 reqInfo 추가
            MdReqInfoDTO mdReqInfoDTO = new MdReqInfoDTO();
            mdReqInfoDTO.setCode(orgCode);
            mdReqInfoDTO.setAgencyName("인젠트금융투자");
            mdReqInfoDTO.setReqSEQ(String.valueOf(uuid));

            mdReqInfoDTO.setServiceName("마이데이터 서비스");
            mdReqInfoDTO.setReqType("마이데이터");
            mdReqInfoDTO.setClientNum("9704261153");

        }
    }

}
