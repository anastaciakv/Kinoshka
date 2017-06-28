package de.proximity.kinoshka.ui.movielist;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingComponent;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.FragmentDataBindingComponent;
import de.proximity.kinoshka.databinding.FragmentMovieListBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.ui.NavigationController;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link MovieListFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieListFragment extends LifecycleFragment implements Injectable {
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    @Inject
    NavigationController navigationController;
    DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    private MovieListViewModel viewModel;
    FragmentMovieListBinding binding;
    MovieGridAdapter adapter;

    public static MovieListFragment newInstance() {
        MovieListFragment fragment = new MovieListFragment();
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieListViewModel.class);
        setHasOptionsMenu(true);
        initGrid();
        binding.setViewmodel(viewModel);
        viewModel.getMovies().observe(this, movies -> adapter.update(movies));
    }

    private void initGrid() {
        final GridLayoutManager layoutManager = new GridLayoutManager(getActivity(), getNumberOfColumns());
        binding.rvMovieList.setLayoutManager(layoutManager);
        binding.rvMovieList.setHasFixedSize(true);
        binding.rvMovieList.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int visibleItemCount = layoutManager.getChildCount();
                int totalItemCount = layoutManager.getItemCount();

                int firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition();

                viewModel.onListScrolled(visibleItemCount, totalItemCount, firstVisibleItemPosition);
            }
        });
        adapter = new MovieGridAdapter(dataBindingComponent,
                movie -> navigationController.navigateToMovieDetails(movie));
        binding.rvMovieList.setAdapter(adapter);
    }

    private int getNumberOfColumns() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int widthDivider = 400;
        int width = displayMetrics.widthPixels;
        int nColumns = width / widthDivider;
        if (nColumns < 2) return 2;
        return nColumns;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_list, container, false);
        ((AppCompatActivity) getActivity()).getSupportActionBar().show();
        return binding.getRoot();
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        getActivity().getMenuInflater().inflate(R.menu.movie_list, menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.menu_sort_by_most_popular:
                viewModel.onSortByMostPopular();
                item.setChecked(true);
                return true;
            case R.id.menu_sort_by_top_rated:
                viewModel.onSortByTopRated();
                item.setChecked(true);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}