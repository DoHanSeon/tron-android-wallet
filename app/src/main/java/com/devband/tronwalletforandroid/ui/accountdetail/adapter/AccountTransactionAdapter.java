package com.devband.tronwalletforandroid.ui.accountdetail.adapter;

import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.devband.tronwalletforandroid.R;
import com.devband.tronwalletforandroid.common.AdapterDataModel;
import com.devband.tronwalletforandroid.common.AdapterView;
import com.devband.tronwalletforandroid.common.Constants;
import com.devband.tronwalletforandroid.ui.transaction.dto.TransactionInfo;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AccountTransactionAdapter extends RecyclerView.Adapter<AccountTransactionAdapter.TransactionViewHolder>  implements AdapterDataModel<TransactionInfo>, AdapterView {

    private List<TransactionInfo> mList = new ArrayList<>();

    private Context mContext;

    private View.OnClickListener mOnItemClickListener;

    public AccountTransactionAdapter(Context context, View.OnClickListener onClickListener) {
        this.mContext = context;
        this.mOnItemClickListener = onClickListener;
    }

    @NonNull
    @Override
    public AccountTransactionAdapter.TransactionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.list_item_my_transaction, null);
        v.setLayoutParams(new RecyclerView.LayoutParams(RecyclerView.LayoutParams.MATCH_PARENT,
                RecyclerView.LayoutParams.WRAP_CONTENT));
        v.setOnClickListener(mOnItemClickListener);
        return new AccountTransactionAdapter.TransactionViewHolder(v);
    }

    @Override
    public void onBindViewHolder(@NonNull AccountTransactionAdapter.TransactionViewHolder holder, int position) {
        TransactionInfo info = mList.get(position);

        Date date = new Date(info.getTimestamp());

        long amount = info.getAmount();

        if (Constants.TRON_SYMBOL.equalsIgnoreCase(info.getTokenName())) {
            amount = (long) (amount / Constants.ONE_TRX);
        }

        if (info.isSend()) {
            holder.sendAddressText.setText(info.getTransferToAddress());
            holder.sendAmountText.setText(Constants.tronBalanceFormat.format(amount) + " " + info.getTokenName());
            holder.sendDateText.setText(Constants.sdf.format(date));

            holder.sendLayout.setVisibility(View.VISIBLE);
            holder.receiveLayout.setVisibility(View.GONE);

            holder.copyToAddressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(info.getTransferToAddress());
                    Toast.makeText(mContext, mContext.getString(R.string.copy_to_address_msg),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        } else {
            holder.receiveAddressText.setText(info.getTransferFromAddress());
            holder.receiveAmountText.setText(Constants.tronBalanceFormat.format(amount) + " " + info.getTokenName());
            holder.receiveDateText.setText(Constants.sdf.format(date));

            holder.sendLayout.setVisibility(View.GONE);
            holder.receiveLayout.setVisibility(View.VISIBLE);

            holder.copyFromAddressView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    copyToClipboard(info.getTransferFromAddress());
                    Toast.makeText(mContext, mContext.getString(R.string.copy_from_address_msg),
                            Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }

    private void copyToClipboard(String content) {
        ClipboardManager clipboard = (ClipboardManager) mContext.getSystemService(Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData.newPlainText("", content);
        clipboard.setPrimaryClip(clip);
    }

    public TransactionInfo getItem(int pos) {
        return mList.get(pos);
    }

    @Override
    public int getItemCount() {
        return mList.size();
    }

    public void refresh(List<TransactionInfo> datas) {
        mList.clear();
        mList.addAll(datas);
        notifyDataSetChanged();
    }

    @Override
    public void add(TransactionInfo model) {
        mList.add(model);
    }

    @Override
    public void addAll(List<TransactionInfo> list) {
        mList.addAll(list);
    }

    @Override
    public void remove(int position) {
        mList.remove(position);
    }

    @Override
    public TransactionInfo getModel(int position) {
        return mList.get(position);
    }

    @Override
    public int getSize() {
        return mList.size();
    }

    @Override
    public void clear() {
        mList.clear();
    }

    @Override
    public void refresh() {
        notifyDataSetChanged();
    }

    public class TransactionViewHolder extends RecyclerView.ViewHolder {

        @BindView(R.id.send_layout)
        LinearLayout sendLayout;

        @BindView(R.id.receive_layout)
        LinearLayout receiveLayout;

        @BindView(R.id.send_address_text)
        TextView sendAddressText;

        @BindView(R.id.receive_address_text)
        TextView receiveAddressText;

        @BindView(R.id.send_date_text)
        TextView sendDateText;

        @BindView(R.id.receive_date_text)
        TextView receiveDateText;

        @BindView(R.id.send_amount_text)
        TextView sendAmountText;

        @BindView(R.id.receive_amount_text)
        TextView receiveAmountText;

        @BindView(R.id.copy_from_address)
        ImageView copyFromAddressView;

        @BindView(R.id.copy_to_address)
        ImageView copyToAddressView;

        public TransactionViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
