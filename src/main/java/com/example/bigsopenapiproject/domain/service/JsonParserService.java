package com.example.bigsopenapiproject.domain.service;

import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class JsonParserService {

    public List<Map<String, Object>> parseJsonResponse(String jsonResponse) {
        JSONObject jsonObject = new JSONObject(jsonResponse);
        JSONObject response = jsonObject.getJSONObject("response");
        JSONObject body = response.getJSONObject("body");
        JSONObject items = body.getJSONObject("items");
        JSONArray itemList = items.getJSONArray("item");

        List<Map<String, Object>> itemListMap = new ArrayList<>();
        for (int i = 0; i < itemList.length(); i++) {
            JSONObject item = itemList.getJSONObject(i);
            Map<String, Object> itemMap = new HashMap<>();
            itemMap.put("baseDate", item.getString("baseDate"));
            itemMap.put("baseTime", item.getString("baseTime"));
            itemMap.put("category", item.getString("category"));
            itemMap.put("fcstDate", item.getString("fcstDate"));
            itemMap.put("fcstTime", item.getString("fcstTime"));
            itemMap.put("fcstValue", item.getString("fcstValue"));
            itemMap.put("nx", item.getInt("nx"));
            itemMap.put("ny", item.getInt("ny"));
            itemListMap.add(itemMap);
        }

        return itemListMap;
    }
}

