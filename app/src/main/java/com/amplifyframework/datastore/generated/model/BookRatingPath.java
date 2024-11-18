package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the BookRating type in your schema. */
public final class BookRatingPath extends ModelPath<BookRating> {
  private BookPath book;
  BookRatingPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, BookRating.class);
  }
  
  public synchronized BookPath getBook() {
    if (book == null) {
      book = new BookPath("book", false, this);
    }
    return book;
  }
}
