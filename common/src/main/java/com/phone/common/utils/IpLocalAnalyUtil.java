package com.phone.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.maxmind.geoip2.DatabaseReader;
import com.maxmind.geoip2.model.AsnResponse;
import com.maxmind.geoip2.model.CityResponse;
import com.maxmind.geoip2.record.*;
import com.phone.common.utils.ip.IpUtil;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.net.InetAddress;
import java.text.NumberFormat;
import java.util.HashMap;
import java.util.Map;

public class IpLocalAnalyUtil {

    public static DatabaseReader readerCity = null;
    public static DatabaseReader readerASN = null;
    public static DatabaseReader readerCountry = null;

    static {
        try {
            ClassPathResource databaseCityResource = new ClassPathResource("ipinfodb/GeoLite2-City.mmdb");
            ClassPathResource databaseASNResource = new ClassPathResource("ipinfodb/GeoLite2-ASN.mmdb");
            ClassPathResource databaseCountryResource = new ClassPathResource("ipinfodb/GeoLite2-Country.mmdb");
            readerCity = new DatabaseReader.Builder(databaseCityResource.getInputStream()).build();
            readerASN = new DatabaseReader.Builder(databaseASNResource.getInputStream()).build();
            readerCountry = new DatabaseReader.Builder(databaseCountryResource.getInputStream()).build();
        } catch (IOException e) {
            // TODO Auto-generated catch blosck
            e.printStackTrace();
        }
    }

    public static Map<String, JSONObject> getIPMsg(String ip) {
        Map<String, JSONObject> res = new HashMap<String, JSONObject>();
        JSONObject jsonIpArea = new JSONObject();
        try {

            InetAddress ipAddress = InetAddress.getByName(ip);
            // Replace "city" with the appropriate method for your database, e.g.,
            // "country".
            CityResponse response = readerCity.city(ipAddress);
            Country country = response.getCountry();//国家
            Subdivision subdivision = response.getMostSpecificSubdivision();//省
            City city = response.getCity();//市
//	    	Postal postal = response.getPostal();//邮编
//	        System.out.println(postal.getCode()); '55455'
            Location location = response.getLocation();
            Continent continent = response.getContinent();//洲

            NumberFormat nf = NumberFormat.getInstance();
            nf.setGroupingUsed(false);

            jsonIpArea.put("ip_long",IpUtil.ipToDouble(ip));

            if (StringUtils.isNotEmpty(continent.getName())) {
                jsonIpArea.put("continent_name", continent.getName());
            } else {
                jsonIpArea.put("continent_name", "");
            }

            if (StringUtils.isNotEmpty(country.getIsoCode())) {
                jsonIpArea.put("country_iso_code", country.getIsoCode());
            } else {
                jsonIpArea.put("country_iso_code", "");
            }

            if (StringUtils.isNotEmpty(country.getName())) {
                jsonIpArea.put("country_name", country.getNames().get("zh-CN"));
            } else {
                jsonIpArea.put("country_name", "中国");
            }

            if (StringUtils.isNotEmpty(subdivision.getName())) {
                jsonIpArea.put("region_name", subdivision.getName());
            } else {
                jsonIpArea.put("region_name", "");
            }

            if (StringUtils.isNotEmpty(subdivision.getIsoCode())) {
                jsonIpArea.put("region_iso_code", subdivision.getIsoCode());
            } else {
                jsonIpArea.put("region_iso_code", "");
            }

            if (StringUtils.isNotEmpty(city.getName())) {
                jsonIpArea.put("city_name", city.getName());
            } else {
                jsonIpArea.put("city_name", "");
            }
            Map<String, Object> map1 = new HashMap<>();
            double lon = 0;
            double lat = 0;
            if (StringUtils.isNotNull(location.getLatitude())) {
                lat = location.getLatitude().doubleValue();
                map1.put("lat", lat);
            } else {
                map1.put("lat", lat);
            }
            if (StringUtils.isNotNull(location.getLongitude())) {
                lon = location.getLongitude().doubleValue();
                map1.put("lon", lon);
            } else {
                map1.put("lon", lon);
            }

            jsonIpArea.put("location", map1);

            res.put("area", jsonIpArea);

            AsnResponse responseAsn = readerASN.asn(ipAddress);

            Map<String, Object> map2 = new HashMap<>();
            double a = responseAsn.getAutonomousSystemNumber().intValue();
//            Decimal128 s=new Decimal128(a);
            map2.put("network", "");
            map2.put("asn", a);
            map2.put("organization_name", responseAsn.getAutonomousSystemOrganization());
            res.put("asn", new JSONObject(map2));

	    	System.err.println(res);

        } catch (Exception e) {
            // TODO: handle exception
        }
        return res;
    }


//    public static void main(String[] args) {
//
//        IpLocalAnalyUtil.getIPMsg("112.81.170.2");
//    }
}
