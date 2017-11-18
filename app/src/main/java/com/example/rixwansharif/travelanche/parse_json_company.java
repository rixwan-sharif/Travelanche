package com.example.rixwansharif.travelanche;

/**
 * Created by Rixwan Sharif on 8/23/2017.
 */
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by Belal on 9/22/2015.
 */
public class parse_json_company {

    public static String[] company_names;
    public static String[] company_cities;
    public static String[] company_contact_person;
    public static String[] company_adress;
    public static String[] company_rating;


    private JSONArray companies = null;

    private String json;

    public parse_json_company(String json) {
        this.json = json;
    }

    protected void parseJSON()

    {
        JSONObject jsonObject = null;
        try
        {
            jsonObject = new JSONObject(json);
            companies = jsonObject.getJSONArray("companies");

            company_names = new String[companies.length()];
            company_cities = new String[companies.length()];
            company_contact_person = new String[companies.length()];
            company_adress = new String[companies.length()];
            company_rating = new String[companies.length()];

            for (int i = 0; i < companies.length(); i++)
            {
                JSONObject jo = companies.getJSONObject(i);

                company_names[i] = jo.getString("company_name");
                company_adress[i] = jo.getString("address");
                company_cities[i] = jo.getString("city");
                company_contact_person[i] = jo.getString("contact_person");
                company_rating[i] = jo.getString("rating");
            }
        }

        catch (JSONException e)
        {
            e.printStackTrace();
        }
    }
}
