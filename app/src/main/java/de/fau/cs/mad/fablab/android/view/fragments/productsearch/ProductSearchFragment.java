package de.fau.cs.mad.fablab.android.view.fragments.productsearch;

import android.graphics.drawable.AnimationDrawable;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.pedrogomez.renderers.RVRendererAdapter;

import javax.inject.Inject;
import butterknife.InjectView;

import de.fau.cs.mad.fablab.android.R;
import de.fau.cs.mad.fablab.android.util.UiUtils;
import de.fau.cs.mad.fablab.android.view.common.binding.AutoCompleteTextViewCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.binding.MenuItemCommandBinding;
import de.fau.cs.mad.fablab.android.view.common.fragments.BaseFragment;

import de.greenrobot.event.EventBus;

import xyz.danoz.recyclerviewfastscroller.sectionindicator.title.SectionTitleIndicator;
import xyz.danoz.recyclerviewfastscroller.vertical.VerticalRecyclerViewFastScroller;


public class ProductSearchFragment extends BaseFragment implements
        ProductSearchFragmentViewModel.Listener {

    private RVRendererAdapter<ProductSearchViewModel> mAdapter;
    private AnimationDrawable mAnimationDrawable;
    private EventBus mEventBus = EventBus.getDefault();
    private MenuItem mOrderByItem;

    @Inject
    ProductSearchFragmentViewModel mViewModel;

    @InjectView(R.id.product_search_text_view)
    AutoCompleteTextView mProductSearchTextView;
    @InjectView(R.id.product_recycler_view)
    RecyclerView mProductRecyclerView;
    @InjectView(R.id.product_fast_scroller)
    VerticalRecyclerViewFastScroller mProductFastScroller;
    @InjectView(R.id.product_fast_scroller_section_title_indicator)
    SectionTitleIndicator mProductSectionTitleIndicator;
    @InjectView(R.id.spinner)
    RelativeLayout loadingSpinnerContainer;
    @InjectView(R.id.spinner_image)
    ImageView mLoadingSpinnerImage;

    @Override
    public void onCreate (Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);
        mOrderByItem = menu.findItem(R.id.action_orderby);
        if(mOrderByItem != null) {
            mOrderByItem.setVisible(true);
            new MenuItemCommandBinding().bind(mOrderByItem.getSubMenu().getItem(0),
                    mViewModel.getOrderProductsByNameCommand());
            new MenuItemCommandBinding().bind(mOrderByItem.getSubMenu().getItem(1),
                    mViewModel.getOrderProductsByPriceCommand());
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_productsearch, container, false);
    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        mViewModel.loadProductNames();

        mLoadingSpinnerImage.setBackgroundResource(R.drawable.spinner);
        mAnimationDrawable = (AnimationDrawable) mLoadingSpinnerImage.getBackground();

        mAdapter = new ProductRVRendererAdapter(getLayoutInflater(savedInstanceState),
                new ProductSearchViewModelRendererBuilder(),
                mViewModel.getProductSearchViewModelCollection());

        mProductRecyclerView.setAdapter(mAdapter);
        mProductFastScroller.setRecyclerView(mProductRecyclerView);
        mProductRecyclerView.addOnScrollListener(mProductFastScroller.getOnScrollListener());
        mProductFastScroller.setSectionIndicator(mProductSectionTitleIndicator);

        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        layoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mProductRecyclerView.setLayoutManager(layoutManager);

        mProductSearchTextView.setThreshold(2);
        ArrayAdapter autoCompleteAdapter = new ArrayAdapter(getActivity(),
                android.R.layout.simple_dropdown_item_1line,
                mViewModel.getAutoCompleteWords());
        mProductSearchTextView.setAdapter(autoCompleteAdapter);

        new AutoCompleteTextViewCommandBinding().bind(mProductSearchTextView,
                mViewModel.getSearchCommand());

        mProductSearchTextView.setOnKeyListener(new View.OnKeyListener() {
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                // If the event is a key-down event on the "enter" button
                if ((event.getAction() == KeyEvent.ACTION_DOWN) && (keyCode == KeyEvent.KEYCODE_ENTER)) {
                    mViewModel.getSearchCommand().execute(mProductSearchTextView.getText().toString());
                    return true;
                }
                return false;
            }
        });

        mViewModel.setListener(this);
        mViewModel.initialize();
    }

    @Override
    public void onResume() {
        super.onResume();
        mEventBus.register(this);
        mViewModel.resume();
        setDisplayOptions(R.id.drawer_item_productsearch, true, true);
    }

    @Override
    public void onPause() {
        super.onPause();
        mEventBus.unregister(this);
        mViewModel.pause();
    }

    @Override
    public void onDataChanged() {
        mAdapter.notifyDataSetChanged();
    }

    @Override
    public void onSearchStateChanged() {
        if(mViewModel.getSearchState()){
            UiUtils.hideKeyboard(getActivity());
            mProductSearchTextView.dismissDropDown();
            mProductSearchTextView.clearFocus();
            loadingSpinnerContainer.setVisibility(View.VISIBLE);
            mAnimationDrawable.start();
        }
        else{
            loadingSpinnerContainer.setVisibility(View.GONE);
            mAnimationDrawable.stop();
        }
    }

    @Override
    public void onRetrofitErrorOccurred() {
        Toast.makeText(getActivity(), R.string.retrofit_callback_failure, Toast.LENGTH_LONG).show();
    }

    public void onNoProductsFound() {
        Toast.makeText(getActivity(), R.string.no_products_found, Toast.LENGTH_LONG).show();
    }

    @Override
    public void onProductOrderChanged() {
        if(mOrderByItem != null) {
            if(mViewModel.isOrderedByName()) {
                mOrderByItem.setIcon(R.drawable.sort_alpha);
                mOrderByItem.setTitle(R.string.appbar_orderedby_name);
                mProductSectionTitleIndicator.setVisibility(View.VISIBLE);
            } else {
                mOrderByItem.setIcon(R.drawable.sort_euro);
                mOrderByItem.setTitle(R.string.appbar_orderedby_price);
                mProductSectionTitleIndicator.setVisibility(View.INVISIBLE);
            }
        }
    }

    @SuppressWarnings("unused")
    public void onEvent(ProductClickedEvent event) {
        ProductDialogFragment dialogFragment = new ProductDialogFragment();
        Bundle arguments = new Bundle();
        arguments.putSerializable(ProductDialogFragmentViewModel.KEY_PRODUCT, event.getProduct());
        dialogFragment.setArguments(arguments);
        dialogFragment.show(getFragmentManager(), "ProductDialogFragment");
    }

}
