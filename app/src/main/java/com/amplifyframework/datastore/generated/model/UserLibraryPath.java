package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the UserLibrary type in your schema. */
public final class UserLibraryPath extends ModelPath<UserLibrary> {
  private UserPath user;
  private BookLibraryPath books;
  UserLibraryPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, UserLibrary.class);
  }
  
  public synchronized UserPath getUser() {
    if (user == null) {
      user = new UserPath("user", false, this);
    }
    return user;
  }
  
  public synchronized BookLibraryPath getBooks() {
    if (books == null) {
      books = new BookLibraryPath("books", true, this);
    }
    return books;
  }
}
