package com.example.lab7;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class Adapter extends RecyclerView.Adapter<Adapter.ViewHolder> {
    List<Todo> todo;
    private JobItemClickListener listener;
    //инициализируем переменные с помощью конструктура
    public Adapter(List<Todo> todo,  @Nullable JobItemClickListener listener){
        this.todo=todo;
        this.listener=listener;
    }
    //Создаем новые вюшки
    @NonNull
    @Override
    public Adapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item, parent,false);
        return new ViewHolder(view);
    }
    @Override
    public void onBindViewHolder(@NonNull Adapter.ViewHolder holder, int position) {
        // получаем элемет из базы с помошью позиции
        // заменяем контент вюшки этим элементом
        final Todo todo=todo.get(position);
        String image_src=todo.getCompanyLogo();
        Picasso.get()
                .load(image_src)
                .placeholder(R.drawable.image_placeholder)
                .into(holder.itemLogo);
        //holder.itemLogo.setImageResource(job.getCompanyLogo());
        holder.itemTitle.setText(todo.getTitle());
        holder.itemType.setText(todo.getType());
        holder.itemCreatedAt.setText(todo.getCreatedAt());
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            //Чтобы показать детальную информацию
            @Override
            public void onClick(View v) {
                listener.itemClick(position, todo);
            }
        });
    }
    //Возвращаем размер данных
    @Override
    public int getItemCount() {
        return todo.size();
    }

    // Предоставляем ссылку на представления для каждого элемента данных
    public class ViewHolder extends RecyclerView.ViewHolder {
        ImageView itemLogo;
        TextView itemTitle;
        TextView itemType;
        TextView itemCreatedAt;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            itemLogo=itemView.findViewById(R.id.companyLogo);
            itemTitle=itemView.findViewById(R.id.title);
            itemType=itemView.findViewById(R.id.type);
            itemCreatedAt=itemView.findViewById(R.id.createdAt);
        }
    }
    public interface JobItemClickListener{
        void itemClick(int position, Todo job);
    }
}
