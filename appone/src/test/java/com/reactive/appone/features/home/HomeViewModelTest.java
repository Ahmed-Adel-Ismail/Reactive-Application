package com.reactive.appone.features.home;

import android.support.annotation.NonNull;

import com.reactive.appone.entities.User;
import com.reactive.appone.mocks.UsersRepositoryMock;

import org.junit.Test;

import java.util.List;
import java.util.concurrent.Callable;

import io.reactivex.schedulers.Schedulers;
import io.reactivex.schedulers.TestScheduler;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;


public class HomeViewModelTest {


    @Test
    public void requestUsersWhileNotLoadingAndValidAgeThenUpdateUsers() throws Exception {

        // Arrange
        TestScheduler scheduler = new TestScheduler();
        HomeViewModel viewModel = viewModel(false, scheduler);

        viewModel.ageInput.onNext("11");
        viewModel.loading.onNext(false);

        // Act
        viewModel.requestUsers();
        scheduler.triggerActions();

        // Assert
        List<User> users = viewModel.users.getValue();
        assertEquals(1, users.size());

    }

    @NonNull
    private HomeViewModel viewModel(boolean error, TestScheduler scheduler) {
        UsersRepositoryMock repository = new UsersRepositoryMock(error);
        return new HomeViewModel(repository, scheduler);
    }


    @Test
    public void requestUsersWhileLoadingThenDoNotUpdateUsers() throws Exception {


        // Arrange
        TestScheduler scheduler = new TestScheduler();
        HomeViewModel viewModel = viewModel(false, scheduler);

        viewModel.ageInput.onNext("11");
        viewModel.loading.onNext(true);

        // Act
        viewModel.requestUsers();
        scheduler.triggerActions();

        // Assert
        assertTrue(viewModel.users.getValue().isEmpty());

    }

    @Test
    public void requestUsersWhileNotLoadingAndEmptyAgeThenDoNotUpdateUsers() throws Exception {
        // Arrange
        TestScheduler scheduler = new TestScheduler();
        HomeViewModel viewModel = viewModel(false, scheduler);

        viewModel.ageInput.onNext("");
        viewModel.loading.onNext(false);

        // Act
        viewModel.requestUsers();
        scheduler.triggerActions();

        // Assert
        assertTrue(viewModel.users.getValue().isEmpty());
    }


    @Test
    public void requestUsersWhileErrorResponseThenUpdateErrorMessage() throws Exception {

        // Arrange
        TestScheduler scheduler = new TestScheduler();
        HomeViewModel viewModel = viewModel(true, scheduler);

        viewModel.ageInput.onNext("11");
        viewModel.loading.onNext(false);

        boolean[] result = {false};
        viewModel.errorMessage
                .share()
                .subscribe(message -> result[0] = true);


        // Act
        viewModel.requestUsers();
        scheduler.triggerActions();

        // Assert
        assertTrue(result[0]);
    }


}