package com.zac4j.sample;

import android.content.Context;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.zac4j.decor.DashDivider;
import com.zac4j.decor.GridDashedDivider;
import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

  @Override protected void onCreate(Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    setContentView(R.layout.activity_main);
    RecyclerView stockListView = findViewById(R.id.main_rv_list);
    RecyclerView stockGridView = findViewById(R.id.main_rv_grid);

    updateListUi(stockListView);
    updateGridUi(stockGridView);
  }

  /**
   * Demonstrate for LinearLayoutManager divider
   */
  private void updateListUi(RecyclerView stockListView) {
    stockListView.setLayoutManager(
        new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
    RecyclerView.ItemDecoration dashDivider = new DashDivider.Builder(this).dashGap(5)
        .dashLength(5)
        .dashThickness(3)
        .color(ContextCompat.getColor(this, R.color.colorPrimary))
        .build();
    stockListView.addItemDecoration(dashDivider);

    StockListAdapter adapter = new StockListAdapter(this);
    stockListView.setAdapter(adapter);
    adapter.addStockList(fetchData());
  }

  /**
   * Demonstrate for GridLayoutManager divider
   */
  private void updateGridUi(RecyclerView stockGridView) {
    stockGridView.setLayoutManager(new GridLayoutManager(this, 4));
    RecyclerView.ItemDecoration dashDivider =
        new GridDashedDivider.Builder(this)
            .drawer(true, false, true, true)
            .hider(false, false, false, true)
            .offset(1.2f, 40, 1.2f, 40)
            .build();
    stockGridView.addItemDecoration(dashDivider);

    StockListAdapter adapter = new StockListAdapter(this);
    stockGridView.setAdapter(adapter);
    adapter.addStockList(fetchData());
  }

  private List<Stock> fetchData() {
    List<Stock> stockList = new ArrayList<>();
    String[] stocks = getResources().getStringArray(R.array.stocks);
    for (String stockInfos : stocks) {
      String[] stockInfo = stockInfos.split(";");
      Stock stock = new Stock(stockInfo[0], stockInfo[1]);
      stockList.add(stock);
    }
    return stockList.subList(0, 7);
  }

  class StockListAdapter extends RecyclerView.Adapter<StockListAdapter.StockViewHolder> {

    private Context mContext;
    private List<Stock> mStockList;

    StockListAdapter(Context context) {
      this.mContext = context;
      mStockList = new ArrayList<>();
    }

    void addStockList(List<Stock> stockList) {
      if (stockList == null || stockList.isEmpty()) {
        return;
      }
      int startPosition = mStockList.size();
      mStockList.addAll(stockList);
      notifyItemRangeChanged(startPosition, stockList.size());
    }

    @Override public StockViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
      View itemView = LayoutInflater.from(mContext).inflate(R.layout.list_item_main, parent, false);
      return new StockViewHolder(itemView);
    }

    @Override public void onBindViewHolder(StockViewHolder holder, int position) {
      Stock stock = mStockList.get(position);

      if (stock == null) {
        return;
      }

      holder.bindTo(stock);
    }

    @Override public int getItemCount() {
      if (mStockList == null || mStockList.isEmpty()) {
        return 0;
      }
      return mStockList.size();
    }

    class StockViewHolder extends RecyclerView.ViewHolder {

      private TextView mNameView;
      private TextView mCodeView;

      StockViewHolder(View itemView) {
        super(itemView);
        mNameView = itemView.findViewById(R.id.item_main_tv_name);
        mCodeView = itemView.findViewById(R.id.item_main_tv_code);
      }

      void bindTo(Stock stock) {
        if (TextUtils.isEmpty(stock.getName())) {
          String name = getString(R.string.empty_stock_name);
          mNameView.setText(name);
        } else {
          mNameView.setText(stock.getName());
        }

        if (TextUtils.isEmpty(stock.getCode())) {
          String code = getString(R.string.empty_stock_code);
          mNameView.setText(code);
        } else {
          mCodeView.setText(stock.getCode());
        }
      }
    }
  }
}
