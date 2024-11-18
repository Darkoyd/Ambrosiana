package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the Author type in your schema. */
public final class AuthorPath extends ModelPath<Author> {
  private BookPath books;
  AuthorPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, Author.class);
  }
  
  public synchronized BookPath getBooks() {
    if (books == null) {
      books = new BookPath("books", true, this);
    }
    return books;
  }
}
