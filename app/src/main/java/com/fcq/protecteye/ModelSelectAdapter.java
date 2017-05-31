package com.fcq.protecteye;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fcq.protecteye.data.Model;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.realm.OrderedRealmCollection;
import io.realm.RealmBaseAdapter;

/**
 * Created by FengChaoQun
 * on 2017/5/22
 */

public class ModelSelectAdapter extends RealmBaseAdapter<Model> {
    private Context context;
    private OnSelected onSelected;

    public ModelSelectAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Model> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_select_model, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        final Model model = getItem(position);
        viewHolder.name.setText(model.getName());
        viewHolder.rootView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onSelected != null) {
                    onSelected.onSelected(model.getName());
                }
            }
        });

        return convertView;
    }


    public interface OnSelected {
        void onSelected(String modelName);
    }

    public void setOnSelected(OnSelected onSelected) {
        this.onSelected = onSelected;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.rootView)
        LinearLayout rootView;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
