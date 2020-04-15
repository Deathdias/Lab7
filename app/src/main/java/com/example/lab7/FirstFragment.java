package com.example.lab7;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class FirstFragment extends Fragment {
    public RecyclerView recyclerView;
    public Adapter adapter;
    public List<Todo> todos = new ArrayList<>();
    private Adapter.JobItemClickListener listener = null;
    private Retrofit retrofit;
    private APIService apiService;
    public ProgressBar progressBar;
    public static FirstFragment newInstance() {
        return new FirstFragment();
    }
    public static FirstFragment newInstance(String description, String location, boolean type) {
        FirstFragment fragment = new FirstFragment();
        Bundle bundle = new Bundle();
        bundle.putString("description", description);
        bundle.putString("location", location);
        bundle.putBoolean("type", type);
        fragment.setArguments(bundle);
        return fragment;
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_main_list, container,false);
        ((AppCompatActivity)getActivity()).getSupportActionBar().setTitle("Developer Jobs");
        setHasOptionsMenu(true);
        progressBar=view.findViewById(R.id.progress);
        recyclerView=view.findViewById(R.id.recycler);
        //наши айтемы будут распологаться линейно
        RecyclerView.LayoutManager layoutManager=new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        //при нажатии на отдельный айтем "открывается" DetailFragment
        listener = new Adapter.JobItemClickListener() {
            @Override
            public void itemClick(int position, Todo todo) {
                FragmentManager fragmentManager=getFragmentManager();
                fragmentManager
                        .beginTransaction()
                        .replace(R.id.frameLayout, SecondFragment.newInstance(todo))
                        .addToBackStack("second")
                        .commit();
            }
        };
        adapter=new Adapter(todos, listener);
        recyclerView.setAdapter(adapter);
        return view;
    }

    //инициализация сервиса
    public void initService(){
        try{
            //логирует наш запрос
            HttpLoggingInterceptor loggingInterceptor=new HttpLoggingInterceptor();
            loggingInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);//насколько информативно должны быть логи который приходит с сервера
            //клиент который делает наши запросы, можем разные условии указать
            OkHttpClient okHttpClient=new OkHttpClient.Builder()
                    .addInterceptor(loggingInterceptor)
                    .build();
            //создаем инстанс ретрофита
            retrofit=new Retrofit.Builder()
                    .baseUrl("https://jobs.github.com/") //ссылка нашей базы
                    .addConverterFactory(GsonConverterFactory.create()) //нужен чтобы спарсить приходящий json в наш класс
                    .client(okHttpClient)
                    .build();
            //создаем интанс нашего сервиса внутри которого есть наши методы
            apiService= retrofit.create(APIService.class);
        } catch (Exception e){
            e.printStackTrace();
        }
    }
    public void getJobs(){
        //вызиваем метод гетДжобс с помошью метода енк
        Call<List<Todo>> call = apiService.getTodos();
        call.enqueue(new Callback<List<Todo>>() {
            //когда респонс приходить он заходить в этот метод
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                Log.e("Response size:",response.body().size()+"");
                progressBar.setVisibility(View.GONE);
                fetchResponse(response);
            }
            //если произошла ошибка заходить в этот метод
            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("Error:",t.getMessage());
            }
        });
    }
    public void getFilteredJobs(String description, String location, boolean type) {
        Call<List<Todo>> call = apiService.getFilteredJobs(description, location, type);
        call.enqueue(new Callback<List<Todo>>() {
            @Override
            public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                progressBar.setVisibility(View.GONE);
                fetchResponse(response);
            }
            @Override
            public void onFailure(Call<List<Todo>> call, Throwable t) {
                Log.e("fail get jobs", t.getMessage());
                Toast.makeText(getActivity().getApplicationContext(), "We could not find such a job", Toast.LENGTH_LONG).show();
            }
        });
    }

    public void fetchResponse(Response<List<Todo>> response) {
        if (response.body() != null) {
            todos.clear();
            todos.addAll(response.body());
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        this.initService();
        try {
            String description = getArguments().getString("description", "");
            String location = getArguments().getString("location", "");
            boolean type = getArguments().getBoolean("type");
            this.getFilteredJobs(description, location, type);
        }
        catch (Exception e) {
            this.getJobs();
        }
    }
    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.layout.menu, menu);
        MenuItem searchItem = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) searchItem.getActionView();
        searchView.setIconifiedByDefault(false);
        searchView.setQueryHint("Type to search by title...");
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                Call<List<Todo>> call = apiService.getJobsFromSearch(query);
                call.enqueue(new Callback<List<Todo>>() {
                    @Override
                    public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                        fetchResponse(response);
                    }

                    @Override
                    public void onFailure(Call<List<Todo>> call, Throwable t) {
                        Log.e("failed search: ", t.getMessage());
                    }
                });
                return false;
            }
            @Override
            public boolean onQueryTextChange(String newText) {
                Call<List<Todo>> call = apiService.getJobsFromSearch(newText);
                call.enqueue(new Callback<List<Todo>>() {
                    @Override
                    public void onResponse(Call<List<Todo>> call, Response<List<Todo>> response) {
                        fetchResponse(response);
                    }
                    @Override
                    public void onFailure(Call<List<Todo>> call, Throwable t) {
                        Log.e("failed search: ", t.getMessage());
                    }
                });
                return false;
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        if (id == R.id.filter) {
            FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
            FilterDialog filterDialog = FilterDialog.newInstance();
            filterDialog.show(fragmentTransaction, "dialog");
            return true;
        }
        return false;
    }
}
