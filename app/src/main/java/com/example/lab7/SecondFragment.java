package com.example.lab7;

import android.os.Bundle;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.squareup.picasso.Picasso;

public class SecondFragment extends Fragment {
        ImageView companyLogo;
        TextView company;
        TextView createdAt;
        TextView title;
        TextView location;
        TextView type;
        TextView description;
        TextView howToApply;
        public static SecondFragment newInstance(Todo todo){
            SecondFragment fragment=new SecondFragment();
            //с помощью Bundle мы передаем данные в фрагмент
            Bundle bundle = new Bundle();
            bundle.putString("companyLogo", todo.getCompanyLogo());
            bundle.putString("company", todo.getCompany());
            bundle.putString("createdAt", todo.getCreatedAt());
            bundle.putString("title", todo.getTitle());
            bundle.putString("location", todo.getLocation());
            bundle.putString("type", todo.getType());
            bundle.putString("description", todo.getDescription());
            bundle.putString("howToApply", todo.getHowToApply());
            fragment.setArguments(bundle);
            return fragment;
        }

        @Nullable
        @Override
        public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
            View view = inflater.inflate(R.layout.fragment_detail, container, false);
            ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Detailed ");
            return view;
        }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            //расположение наших вюшек
            companyLogo=view.findViewById(R.id.detail_companyLogo);
            company=view.findViewById(R.id.detail_company);
            createdAt=view.findViewById(R.id.detail_createdAt);
            title=view.findViewById(R.id.detail_title);
            location=view.findViewById(R.id.detail_location);
            type=view.findViewById(R.id.detail_type);
            description=view.findViewById(R.id.detail_description);
            howToApply=view.findViewById(R.id.detail_howToApply);
            try {
                //getArguments() возвращает аргументы, переданные в setArguments (Bundle) и мы берем каждое по ключу
                //меняем эти данные вюшек
                Picasso.get()
                        .load(getArguments().getString("companyLogo"))
                        .placeholder(R.drawable.image_placeholder)
                        .into(companyLogo);
                company.setText(getArguments().getString("company"));
                createdAt.setText(getArguments().getString("createdAt"));
                title.setText(getArguments().getString("title"));
                String desc=getArguments().getString("description");
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                description.setText(Html.fromHtml(desc));
//            } else {
//                description.setText(Html.fromHtml(getArguments().getString("description")));
//            }
//            description.setText(HtmlCompat.fromHtml(desc, 0));
                location.setText(getArguments().getString("location"));
                type.setText(getArguments().getString("type"));
                description.setText(getArguments().getString("description"));
                howToApply.setText(getArguments().getString("howToApply"));
            }
            catch (Exception e) {
                Log.e("Error", e + " ");
            }
        }
}
