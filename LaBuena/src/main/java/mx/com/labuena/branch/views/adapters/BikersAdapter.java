package mx.com.labuena.branch.views.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import mx.com.labuena.branch.R;
import mx.com.labuena.branch.models.Biker;

/**
 * Created by clerks on 8/22/16.
 */

public class BikersAdapter extends RecyclerView.Adapter<BikersAdapter.MainHolder> {
    private final Context context;
    private final List<Biker> bikers;
    private final BikerClickListener bikerClickListener;

    public BikersAdapter(Context context, List<Biker> bikers, BikerClickListener bikerClickListener) {
        this.context = context;
        this.bikers = bikers;
        this.bikerClickListener = bikerClickListener;
    }

    @Override
    public MainHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.biker_row, parent, false);
        return new BikersAdapter.MainHolder(view);
    }

    @Override
    public void onBindViewHolder(MainHolder holder, int position) {
        Biker biker = bikers.get(position);
        holder.bikerNameTextView.setText(biker.getName());
        holder.bikerNameTextView.setContentDescription(biker.getName());
        holder.bikerPhoneTextView.setText(biker.getPhone());
        holder.bikerPhoneTextView.setContentDescription(biker.getPhone());
        holder.bikerStockTextView.setText(String.format(context.getString(R.string.amount_to_deliver),
                biker.getLastStock()));
        holder.bikerStockTextView.setContentDescription(String.format(context.
                getString(R.string.amount_to_deliver),
                biker.getLastStock()));
        holder.bikerEmailTextView.setText(biker.getEmail());
        holder.bikerEmailTextView.setContentDescription(biker.getEmail());
    }

    @Override
    public int getItemCount() {
        return bikers.size();
    }

    public class MainHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView bikerEmailTextView;
        private TextView bikerStockTextView;
        private TextView bikerPhoneTextView;
        private TextView bikerNameTextView;

        public MainHolder(View itemView) {
            super(itemView);
            bikerNameTextView = (TextView) itemView.findViewById(R.id.nameTextView);
            bikerPhoneTextView = (TextView) itemView.findViewById(R.id.phoneTextView);
            bikerStockTextView = (TextView) itemView.findViewById(R.id.stockTextView);
            bikerEmailTextView = (TextView) itemView.findViewById(R.id.emailTextView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int position = this.getAdapterPosition();
            Biker biker = bikers.get(position);
            bikerClickListener.onBikerClick(biker);
        }
    }

    public interface BikerClickListener {
        void onBikerClick(Biker biker);
    }
}
