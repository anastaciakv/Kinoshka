package de.proximity.kinoshka.ui.moviedetails;


import android.arch.lifecycle.LifecycleFragment;
import android.arch.lifecycle.ViewModelProvider;
import android.arch.lifecycle.ViewModelProviders;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import org.parceler.Parcels;

import javax.inject.Inject;

import de.proximity.kinoshka.R;
import de.proximity.kinoshka.binding.FragmentDataBindingComponent;
import de.proximity.kinoshka.databinding.FragmentMovieDetailsBinding;
import de.proximity.kinoshka.di.Injectable;
import de.proximity.kinoshka.entity.Movie;

/**
 * Use the {@link MovieDetailsFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class MovieDetailsFragment extends LifecycleFragment implements Injectable {
    FragmentMovieDetailsBinding binding;
    android.databinding.DataBindingComponent dataBindingComponent = new FragmentDataBindingComponent(this);
    @Inject
    ViewModelProvider.Factory viewModelFactory;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param movie
     * @return A new instance of fragment MovieDetailsFragment.
     */
    public static MovieDetailsFragment newInstance(Movie movie) {
        MovieDetailsFragment fragment = new MovieDetailsFragment();
        Bundle extras = new Bundle();
        extras.putParcelable(Movie.ITEM_KEY, Parcels.wrap(movie));
        fragment.setArguments(extras);
        return fragment;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if (getArguments() != null) {
            Movie movie = Parcels.unwrap(getArguments().getParcelable(Movie.ITEM_KEY));
            MovieDetailsViewModel viewModel = ViewModelProviders.of(this, viewModelFactory).get(MovieDetailsViewModel.class);
            viewModel.setMovie(movie);
            binding.setMovie(movie);
            binding.setVmodel(viewModel);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        ((AppCompatActivity) getActivity()).getSupportActionBar().hide();
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_movie_details, container, false, dataBindingComponent);
        return binding.getRoot();
    }


}

