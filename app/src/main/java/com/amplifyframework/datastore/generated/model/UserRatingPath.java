package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the UserRating type in your schema. */
public final class UserRatingPath extends ModelPath<UserRating> {
  private UserPath user;
  private UserPath ratedUser;
  UserRatingPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, UserRating.class);
  }
  
  public synchronized UserPath getUser() {
    if (user == null) {
      user = new UserPath("user", false, this);
    }
    return user;
  }
  
  public synchronized UserPath getRatedUser() {
    if (ratedUser == null) {
      ratedUser = new UserPath("ratedUser", false, this);
    }
    return ratedUser;
  }
}
