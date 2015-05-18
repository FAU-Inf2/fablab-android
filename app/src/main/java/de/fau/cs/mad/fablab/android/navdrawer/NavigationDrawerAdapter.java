package de.fau.cs.mad.fablab.android.navdrawer;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import de.fau.cs.mad.fablab.android.R;

public class NavigationDrawerAdapter extends RecyclerView.Adapter<NavigationDrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;

    private NavigationDrawer navdrawer;

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int holderID;

        TextView navdrawer_textView;
        ImageView navdrawer_imageView;
        ImageView navdrawer_icon;
        TextView navdrawer_name;
        TextView navdrawer_email;


        public ViewHolder(View itemView,int ViewType) {
            super(itemView);

            if(ViewType == TYPE_ITEM) {
                navdrawer_textView = (TextView) itemView.findViewById(R.id.navdrawer_rowText);
                navdrawer_imageView = (ImageView) itemView.findViewById(R.id.navdrawer_rowIcon);
                holderID = 1;
            } else {
                navdrawer_name = (TextView) itemView.findViewById(R.id.navdrawer_name);
                navdrawer_email = (TextView) itemView.findViewById(R.id.navdrawer_email);
                navdrawer_icon = (ImageView) itemView.findViewById(R.id.navdrawer_circleView);
                holderID = 0;
            }
        }
    }

    public NavigationDrawerAdapter(NavigationDrawer navdrawer) {
       this.navdrawer = navdrawer;
    }

    @Override
    public NavigationDrawerAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        if(viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navdrawer_item_row,parent, false);
            return new ViewHolder(v,viewType);
        } else if(viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.navdrawer_header_login,parent,false);
            return new ViewHolder(v,viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(NavigationDrawerAdapter.ViewHolder holder, int position) {
        if(holder.holderID == 1) {
            holder.navdrawer_textView.setText(navdrawer.getItems().get(position - 1).getTitle());
            holder.navdrawer_imageView.setImageResource(navdrawer.getItems().get(position - 1).getIcon());
        } else {
            holder.navdrawer_icon.setImageResource(navdrawer.getIcon());
            holder.navdrawer_name.setText(navdrawer.getName());
            holder.navdrawer_email.setText(navdrawer.getEmail());
        }
    }

    @Override
    public int getItemCount() {
        return navdrawer.getItems().size() + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if(isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }
}
