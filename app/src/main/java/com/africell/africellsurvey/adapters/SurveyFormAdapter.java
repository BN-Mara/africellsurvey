package com.africell.africellsurvey.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.africell.africellsurvey.R;
import com.africell.africellsurvey.databinding.ListFormBinding;
import com.africell.africellsurvey.model.SurveyForm;
import com.africell.africellsurvey.ui.MainActivity;
import com.africell.africellsurvey.ui.fragments.FormsFragment;
import com.africell.africellsurvey.viewmodel.SurveyFormViewModel;

import java.util.ArrayList;

import static java.util.logging.Logger.global;

public class SurveyFormAdapter extends RecyclerView.Adapter<SurveyFormAdapter.FormViewHolder> {
    private Context mContext;
    private ArrayList<SurveyForm> mList;
    private ListFormBinding binding;
    private MyOnclikListner myOnclikListner;
    private int  mPreviousIndex = -1;

    public SurveyFormAdapter(Context mContext, MyOnclikListner myOnclikListner, ArrayList<SurveyForm> mList){
        this.mContext = mContext;
        this.mList = mList;
        this.myOnclikListner=myOnclikListner;
    }

    @NonNull
    @Override
    public FormViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(mContext);
        binding = ListFormBinding.inflate(inflater,parent,false);

        return new FormViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull FormViewHolder holder, int position) {
        holder.itemBiding.ftitle.setText(mList.get(position).getTitle());
        holder.itemBiding.fversion.setText(mList.get(position).getVersion());

        /*if(mPreviousIndex != -1){
            if(mPreviousIndex==position){
                holder.itemBiding.fstatusIc.setImageResource(R.drawable.ic_check);
                holder.itemBiding.fstatusIc.setEnabled(false);
                //color on item selecting item
            }

        }*///else{
        if(mPreviousIndex==position){
            //mList.get(position).setIsDownloaded(1);
            //notifyDataSetChanged();
            //holder.itemBiding.fstatusIc.setImageResource(R.drawable.ic_check);
            //holder.itemBiding.fstatusIc.setEnabled(false);
            //color on item selecting item
        }
            if(mList.get(position).getIsDownloaded() == 0){
                holder.itemBiding.fstatusIc.setImageResource(R.drawable.ic_download);
                holder.itemBiding.fstatusIc.setEnabled(true);
            }else if(mList.get(position).getIsDownloaded() == -1){
                holder.itemBiding.fstatusIc.setImageResource(R.drawable.ic_sync);
                holder.itemBiding.fstatusIc.setEnabled(true);
            }
            else if(mList.get(position).getIsDownloaded() == 1){
                holder.itemBiding.fstatusIc.setImageResource(R.drawable.ic_check);
                holder.itemBiding.fstatusIc.setEnabled(false);
            }else{
                holder.itemBiding.fstatusIc.setEnabled(true);
            }


        //}



        holder.itemBiding.fstatusIc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                SurveyForm sf = mList.get(position);
                //Toast.makeText(mContext,""+sf.getId(),Toast.LENGTH_LONG).show();
                // holder.itemBiding.ftitle.setText("");
                //setButtonImage(position);
                mPreviousIndex = position;
                if (myOnclikListner != null) {
                    myOnclikListner.setOnclickLister(sf,position);
                    //mList.get(position).setIsDownloaded(1);
                    //sf.setIsDownloaded(1);
                }

                //notifyItemChanged(position);
                notifyDataSetChanged();


                //SurveyFormViewModel vm = new SurveyFormViewModel()
            }
        });
        //Glide.with(mContext).load(mList.get(position).getUrl())
        //       .into(holder.itemBinding.pokemonImage);
    }
    public void setButtonImage(int position){
        notifyItemChanged(position);
        this.mList.get(position).setIsDownloaded(1);
        notifyItemChanged(position);
        notifyDataSetChanged();



    }



    @Override
    public int getItemCount() {

        return mList == null ? 0 : mList.size();
    }

    class FormViewHolder extends RecyclerView.ViewHolder{
        private ListFormBinding itemBiding;

        public FormViewHolder(ListFormBinding itemBiding) {
            super(itemBiding.getRoot());
            this.itemBiding = itemBiding;

        }
    }
    public ArrayList<SurveyForm> getmList(){
        return mList;
    }
    public  void updateList(ArrayList<SurveyForm> updatedList){
        mList = updatedList;
        notifyDataSetChanged();
    }
    public void addToList(ArrayList<SurveyForm> addedList){
        mList.addAll(addedList);
        notifyDataSetChanged();
    }


    public  SurveyForm getFormAt(int position){
        return mList.get(position);
    }
    public interface MyOnclikListner{
        void setOnclickLister(SurveyForm sf,int position);
    }
}
