package com.amplifyframework.datastore.generated.model;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.amplifyframework.core.model.ModelPath;
import com.amplifyframework.core.model.PropertyPath;

/** This is an auto generated class representing the ModelPath for the BookLibrary type in your schema. */
public final class BookLibraryPath extends ModelPath<BookLibrary> {
  private BookPath book;
  private UserLibraryPath library;
  BookLibraryPath(@NonNull String name, @NonNull Boolean isCollection, @Nullable PropertyPath parent) {
    super(name, isCollection, parent, BookLibrary.class);
  }
  
  public synchronized BookPath getBook() {
    if (book == null) {
      book = new BookPath("book", false, this);
    }
    return book;
  }
  
  public synchronized UserLibraryPath getLibrary() {
    if (library == null) {
      library = new UserLibraryPath("library", false, this);
    }
    return library;
  }
}
