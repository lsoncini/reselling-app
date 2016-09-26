package me.raptor.resellingapp.activity;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.util.Date;
import java.util.List;

import butterknife.BindString;
import butterknife.BindView;
import butterknife.ButterKnife;
import me.raptor.resellingapp.R;
import me.raptor.resellingapp.model.Sale;
import me.raptor.resellingapp.store.GroupStore;
import me.raptor.resellingapp.store.SaleStore;
import me.raptor.resellingapp.view.SaleList;

/**
 * Created by Lucas on 23/09/2016.
 */
public class SaleEditFragment extends LoadingFragment {

    @BindString(R.string.sale_edition_title) String editing_title;
    @BindString(R.string.new_sale_title) String new_sale_title;
    @BindString(R.string.bad_arguments_msg) String bad_arguments_msg;
    @BindString(R.string.no_group_msg) String no_group_msg;
    @BindString(R.string.new_group) String new_group;

    @BindView(R.id.group_alt_menu) EditText groupTV;
    @BindView(R.id.group_spinner) Spinner groupSpinner;
    @BindView(R.id.ok_button) Button okButton;
    @BindView(R.id.cancel_button) Button cancelButton;

    private boolean isNew = false;
    private SaleList.SaleListListener listener;
    private List<String> groups;
    private GroupStore groupStore;
    private SaleStore saleStore;
    private Sale sale;

    @Override
    public String getTitle() {
        return isNew ? new_sale_title : editing_title;
    }

    public SaleEditFragment setSale(Sale sale) {
        this.sale = sale;
        return this;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_sale_edition, container, false);
        ButterKnife.bind(this, view);
        this.groupStore = GroupStore.getInstance(getContext());
        this.saleStore = SaleStore.getInstance(getContext());

        groups = groupStore.getGroups();
        groups.add(new_group);
        sale = new Sale(saleStore.getNextID(), new Date(), null);
        return view;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateView();
        getActivity().invalidateOptionsMenu();
    }

    private void updateView() {
        if (getView() == null) return;
        if(groups.size() > 1) {
            groupSpinner.setVisibility(View.VISIBLE);
            groupTV.setVisibility(View.GONE);
        }
        ArrayAdapter<String> groupsAdapter = new ArrayAdapter<>(getContext(),
                android.R.layout.simple_spinner_item, groups);
        groupsAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        groupSpinner.setAdapter(groupsAdapter);
        groupSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String item = groups.get(position);
                if (item.equals(new_group)){
                    groupSpinner.setVisibility(View.GONE);
                    groupTV.setVisibility(View.VISIBLE);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        if(sale!=null && sale.getGroup() != null) {
            groupTV.setText(sale.getGroup(), TextView.BufferType.EDITABLE);
            groupSpinner.setSelection(groups.indexOf(sale.getGroup()));
        }
        okButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String groupTV_value = groupTV.getText().toString();
                String groupSpinnerValue = (String) groupSpinner.getSelectedItem();
                String group = (groupTV.getVisibility() == View.VISIBLE) ? groupTV_value : groupSpinnerValue;
                if (group.isEmpty())
                    group = null;

                if(group == null){
                    Toast.makeText(getContext(),bad_arguments_msg,Toast.LENGTH_LONG).show();
                    Toast.makeText(getContext(),no_group_msg,Toast.LENGTH_LONG).show();
                    return;
                }

                if(groupTV.getVisibility() == View.VISIBLE) {
                    groupStore.addGroup(groupTV_value);
                    groups = groupStore.getGroups();
                    groups.add(new_group);
                }

                sale.setGroup(group);
                if (isNew){
                    saleStore.insertSale(sale);
                } else{
                    saleStore.updateSale(sale);
                }
                listener.onSalesListChanged();
                getActivity().onBackPressed();
            }
        });

        cancelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

    }

    @Override
    public void onResume() {
        super.onResume();
        if(sale != null)
            setSale(sale);
        getActivity().invalidateOptionsMenu();
    }

    public void setNew(boolean isNew){
        this.isNew = isNew;
    }
    public void setSaleListener(SaleList.SaleListListener l){
        listener = l;
    }
}
