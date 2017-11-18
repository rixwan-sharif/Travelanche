package com.example.rixwansharif.travelanche;

/**
 * Created by Rixwan Sharif on 8/22/2017.
 */
import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.RatingBar;
import android.widget.TextView;

/**
 * Created by Belal on 9/22/2015.
 */

public class custom_row_for_company extends ArrayAdapter<String> {
    private String[] company_names;
    private String[] company_cities;
    private String[] company_contact_person;
    private String[] company_adress;
    private String[] company_rating;

    private Activity context;

    public custom_row_for_company(Activity context, String[] company_names, String[] company_cities, String[] company_contact_person,
                                  String[] company_adress,String[] company_rating)
    {
        super(context, R.layout.custome_row_company,company_names);
        this.context = context;
        this.company_names = company_names;
        this.company_cities = company_cities;
        this.company_contact_person = company_contact_person;
        this.company_adress = company_adress;
        this.company_rating = company_rating;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent)
    {
        LayoutInflater inflater = context.getLayoutInflater();
        View listViewItem = inflater.inflate(R.layout.custome_row_company, null, true);

        TextView Company_Name = (TextView) listViewItem.findViewById(R.id.company_name_txt);
        TextView Company_City = (TextView) listViewItem.findViewById(R.id.company_city_txt);
        TextView Company_Contact_Person = (TextView) listViewItem.findViewById(R.id.contact_person_txt);
        TextView Company_Address = (TextView) listViewItem.findViewById(R.id.company_address_txt);
        RatingBar Company_Rating=(RatingBar) listViewItem.findViewById(R.id.company_rating_bar);

        Company_Name.setText(company_names[position]);
        Company_City.setText(company_cities[position]);
        Company_Contact_Person.setText(company_contact_person[position]);
        Company_Address.setText(company_adress[position]);
        Company_Rating.setRating(Float.parseFloat(company_rating[position]));


        return listViewItem;
    }
}