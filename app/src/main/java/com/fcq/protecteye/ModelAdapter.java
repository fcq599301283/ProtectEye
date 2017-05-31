package com.fcq.protecteye;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
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

public class ModelAdapter extends RealmBaseAdapter<Model> {
    private Context context;

    public ModelAdapter(@NonNull Context context, @Nullable OrderedRealmCollection<Model> data) {
        super(context, data);
        this.context = context;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder;
        if (convertView == null) {
            convertView = View.inflate(context, R.layout.item_model, null);
            viewHolder = new ViewHolder(convertView);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        final Model model = getItem(position);
        viewHolder.name.setText(model.getName());

        viewHolder.custom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModelDisplayActivity.start(context, true, model.getName());
            }
        });

        return convertView;
    }

    static class ViewHolder {
        @Bind(R.id.name)
        TextView name;
        @Bind(R.id.custom)
        RelativeLayout custom;

        ViewHolder(View view) {
            ButterKnife.bind(this, view);
        }
    }
}
